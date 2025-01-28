package sergey_white.org.deal.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import sergey_white.org.deal.dto.*;
import sergey_white.org.deal.entity.*;
import sergey_white.org.deal.mapper.Mapper;
import sergey_white.org.deal.repository.ClientRepository;
import sergey_white.org.deal.repository.CreditRepository;
import sergey_white.org.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static sergey_white.org.deal.enums.ApplicationStatus.*;
import static sergey_white.org.deal.enums.ChangeType.MANUAL;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealService {
    private final WebClient webClient;
    private final Mapper mapper;
    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final CreditRepository creditRepository;
    private final DocumentService dealProducerService;

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto input) {
        log.info("Starting to get loan offers : {}", input);
        List<LoanOfferDto> loanOffers;
        String url = "http://localhost:8080/calculator/offers";
        try {
            loanOffers = webClient.post()
                    .uri(url)
                    .body(Mono.just(input), LoanStatementRequestDto.class)
                    .retrieve()
                    .bodyToFlux(LoanOfferDto.class)
                    .collectList()
                    .block();
            log.info("Received loan offers: {}", loanOffers);
        } catch (WebClientResponseException.BadRequest ex) {
            throw ex;
        }
        Statement statement = saveClientAndStatement(input);
        assert loanOffers != null;
        return loanOffers
                .stream()
                .peek(e -> e.setStatementId(statement.getStatementId()))
                .toList();
    }

    public void selectOffer(LoanOfferDto input) {
        log.info("Selecting loan offer: {}", input);
        Statement statement = statementRepository.getReferenceById(input.getStatementId());
        statement.setStatus(APPROVED);
        StatusHistory statusHistory = new StatusHistory(APPROVED, LocalDateTime.now(), MANUAL);
        List<StatusHistory> history = statement.getStatusHistory();
        history.add(statusHistory);
        statement.setAppliedOffer(input);
        Credit credit = new Credit();
        credit.setAmount(input.getTotalAmount());
        credit.setTerm(input.getTerm());
        statementRepository.save(statement);
        dealProducerService.sendFinishRegMessage(statement.getStatementId());
        log.info("Loan offer selected");
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDto input, String statementId) {
        if (statementRepository.getReferenceById(UUID.fromString(statementId)).getCredit() == null) {
            log.info("Calculating credit for input: {} and statementId: {}", input, statementId);
            Statement statement = statementRepository.getReferenceById(UUID.fromString(statementId));
            CreditDto creditDto = getCreditDtoFromFinishRegReqDto(input, statementId);
            statement.setStatus(CC_APPROVED);
            StatusHistory statusHistory = new StatusHistory(CC_APPROVED, LocalDateTime.now(), MANUAL);
            List<StatusHistory> history = statement.getStatusHistory();
            history.add(statusHistory);
            Credit credit = mapper.fromCreditDtoToCredit(creditDto);
            updateClient(statement, input);
            statement.setCredit(credit);
            creditRepository.save(credit);
            statementRepository.save(statement);
            dealProducerService.sendPaperworkMessage(statement.getStatementId());
            log.info("Credit calculated and statement updated: {}", statement);
        }
    }

    private CreditDto getCreditDtoFromFinishRegReqDto(FinishRegistrationRequestDto input, String statementId) {
        log.info("Getting credit DTO from finish registration request for statementId: {}", statementId);
        ScoringDataDto scoringDataDto = getScoringDataFromParams(input, statementId);
        CreditDto creditDto;
        String url = "http://localhost:8080/calculator/calc";
        try {
            creditDto = webClient.post()
                    .uri(url)
                    .body(Mono.just(scoringDataDto), ScoringDataDto.class)
                    .retrieve()
                    .bodyToMono(CreditDto.class)
                    .block();

            log.info("Received credit DTO: {}", creditDto);
            return creditDto;
        } catch (WebClientResponseException.BadRequest ex) {
            throw ex;
        }
    }

    private ScoringDataDto getScoringDataFromParams(FinishRegistrationRequestDto input, String statementId) {
        log.info("Getting scoring data from params for statementId: {}", statementId);
        Statement statement = statementRepository.getReferenceById(UUID.fromString(statementId));
        LoanOfferDto loanOffer = statement.getAppliedOffer();
        Client client = statement.getClient();
        Passport passport = client.getPassport();
        LoanOfferDto appliedOffer = statement.getAppliedOffer();
        ScoringDataDto scoringDataDto = new ScoringDataDto(
                loanOffer.getTotalAmount(),
                loanOffer.getTerm(),
                client.getFirstName(),
                client.getLastName(),
                client.getMiddleName(),
                client.getGender(),
                client.getBirthDate(),
                passport.getSeries(),
                passport.getNumber(),
                input.getPassportIssueDate(),
                input.getPassportIssueBranch(),
                client.getMaritalStatus(),
                client.getDependentAmount(),
                input.getEmployment(),
                client.getAccountNumber(),
                appliedOffer.getIsInsuranceEnabled(),
                appliedOffer.getIsSalaryClient()
        );
        log.info("Scoring data DTO created: {}", scoringDataDto);
        return scoringDataDto;
    }

    private Statement saveClientAndStatement(LoanStatementRequestDto input) {
        log.info("Saving client and statement for input: {}", input);
        Client client = mapper.LoanStatementRequestDtoToClient(input);
        client.setAccountNumber(UUID.randomUUID().toString());
        Statement statement = new Statement();
        statement.setStatementId(UUID.randomUUID());
        statement.setClient(client);
        statement.setStatusHistory(new ArrayList<>());
        clientRepository.save(client);
        statementRepository.save(statement);
        log.info("Client and statement saved: {}", statement);
        return statement;
    }

    private void updateClient(Statement statement, FinishRegistrationRequestDto input) {
        log.info("Updating client for statement: {}", statement);
        Client client = statement.getClient();
        if (client.getEmployment() == null) {
            client.setGender(input.getGender());
            client.setMaritalStatus(input.getMaritalStatus());
            client.setDependentAmount(input.getDependentAmount());
            Employment employment = mapper.fromEmploymentDtoToEmployment(input.getEmployment());
            Passport passport = client.getPassport();
            passport.setIssueBranch(input.getPassportIssueBranch());
            passport.setIssueDate(input.getPassportIssueDate());
            client.setPassport(passport);
            client.setEmployment(employment);
            clientRepository.save(client);
            log.info("Client updated: {}", client);
        }
    }
}
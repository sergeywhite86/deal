package sergey_white.org.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sergey_white.org.deal.dto.*;
import sergey_white.org.deal.entity.*;
import sergey_white.org.deal.mapper.Mapper;
import sergey_white.org.deal.repository.ClientRepository;
import sergey_white.org.deal.repository.CreditRepository;
import sergey_white.org.deal.repository.StatementRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sergey_white.org.deal.enums.ApplicationStatus.PREAPPROVAL;
import static sergey_white.org.deal.enums.ApplicationStatus.PREPARE_DOCUMENTS;
import static sergey_white.org.deal.enums.ChangeType.MANUAL;
import static sergey_white.org.deal.service.ConstantsForDealServiceTest.*;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {
    @Mock
    private WebClient webClient;
    @Mock
    private Mapper mapper;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private StatementRepository statementRepository;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @InjectMocks
    private DealService dealService;

    @Test
    void testGetLoanOffers() {
        when(mapper.LoanStatementRequestDtoToClient(LOAN_STATEMENT_REQUEST_DTO)).thenReturn(new Client());
        when(statementRepository.save(any(Statement.class))).thenReturn(STATEMENT);

        List<LoanOfferDto> loanOffers = List.of(new LoanOfferDto(), new LoanOfferDto());
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(LoanStatementRequestDto.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(LoanOfferDto.class)).thenReturn(Flux.fromIterable(loanOffers));

        List<LoanOfferDto> result = dealService.getLoanOffers(LOAN_STATEMENT_REQUEST_DTO);

        assertEquals(2, result.size());
        verify(statementRepository, times(1)).save(any(Statement.class));
    }

    @Test
    void testGetLoanOffers_BadRequest() {
        when(mapper.LoanStatementRequestDtoToClient(LOAN_STATEMENT_REQUEST_DTO)).thenReturn(new Client());
        when(statementRepository.save(any(Statement.class))).thenReturn(STATEMENT);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(LoanStatementRequestDto.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenThrow(new WebClientResponseException(400, "Bad Request", null, null, null));

        assertThrows(WebClientResponseException.class, () -> dealService.getLoanOffers(LOAN_STATEMENT_REQUEST_DTO));
    }

    @Test
    void testSelectOffer() {
        when(statementRepository.getReferenceById(LOAN_OFFER_DTO.getStatementId())).thenReturn(STATEMENT);
        when(statementRepository.save(STATEMENT)).thenReturn(STATEMENT);

        dealService.selectOffer(LOAN_OFFER_DTO);

        assertEquals(PREPARE_DOCUMENTS, STATEMENT.getStatus());
        assertEquals(LOAN_OFFER_DTO, STATEMENT.getAppliedOffer());

        verify(statementRepository, times(1)).save(STATEMENT);
    }

    @Test
    void testCalculateCredit() {
        String statementId = UUID.randomUUID().toString();

        when(statementRepository.getReferenceById(UUID.fromString(statementId))).thenReturn(STATEMENT);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(ScoringDataDto.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CreditDto.class)).thenReturn(Mono.just(CREDIT_DTO));
        when(mapper.fromCreditDtoToCredit(CREDIT_DTO)).thenReturn(CREDIT);

        dealService.calculateCredit(FINISH_REGISTRATION_REQUEST_DTO, statementId);

        assertEquals(PREAPPROVAL, STATEMENT.getStatus());
        assertEquals(1, STATEMENT.getStatusHistory().size());
        StatusHistory statusHistory = STATEMENT.getStatusHistory().get(0);
        assertEquals(PREAPPROVAL, statusHistory.getStatus());
        assertEquals(MANUAL, statusHistory.getChangeType());

        Credit savedCredit = STATEMENT.getCredit();
        assertNotNull(savedCredit);
        assertEquals(CREDIT_DTO.getAmount(), savedCredit.getAmount());
        assertEquals(CREDIT_DTO.getTerm(), savedCredit.getTerm());

        verify(creditRepository, times(1)).save(CREDIT);
        verify(statementRepository, times(1)).save(STATEMENT);
    }

    @Test
    void testCalculateCredit_BadRequest() {
        String statementId = UUID.randomUUID().toString();

        when(statementRepository.getReferenceById(UUID.fromString(statementId))).thenReturn(STATEMENT);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), eq(ScoringDataDto.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenThrow(new WebClientResponseException(400, "Bad Request", null, null, null));

        assertThrows(WebClientResponseException.class,
                () -> dealService.calculateCredit(FINISH_REGISTRATION_REQUEST_DTO, statementId));
    }
}


package sergey_white.org.deal.service;

import sergey_white.org.deal.dto.CreditDto;
import sergey_white.org.deal.dto.FinishRegistrationRequestDto;
import sergey_white.org.deal.dto.LoanOfferDto;
import sergey_white.org.deal.dto.LoanStatementRequestDto;
import sergey_white.org.deal.entity.Client;
import sergey_white.org.deal.entity.Credit;
import sergey_white.org.deal.entity.Passport;
import sergey_white.org.deal.entity.Statement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static sergey_white.org.deal.enums.ApplicationStatus.PREPARE_DOCUMENTS;

public class ConstantsForDealServiceTest {
  static LoanStatementRequestDto LOAN_STATEMENT_REQUEST_DTO = new LoanStatementRequestDto();
  static Statement STATEMENT = new Statement();
  static LoanOfferDto LOAN_OFFER_DTO = new LoanOfferDto();
  static Client CLIENT = new Client();
  static Passport PASSPORT = new Passport();
  static FinishRegistrationRequestDto FINISH_REGISTRATION_REQUEST_DTO = new FinishRegistrationRequestDto();
  static CreditDto CREDIT_DTO = new CreditDto();
  static Credit CREDIT = new Credit();

  static {
      STATEMENT.setStatementId(UUID.randomUUID());
      STATEMENT.setStatusHistory(new ArrayList<>());
      STATEMENT.setStatus(PREPARE_DOCUMENTS);
      STATEMENT.setClient(CLIENT);
      STATEMENT.setAppliedOffer(LOAN_OFFER_DTO);
      LOAN_OFFER_DTO.setStatementId(UUID.randomUUID());
      LOAN_OFFER_DTO.setTotalAmount(BigDecimal.valueOf(10000.0));
      LOAN_OFFER_DTO.setTerm(12);
      PASSPORT.setSeries("1234");
      PASSPORT.setNumber("567890");
      CLIENT.setPassport(PASSPORT);
      CREDIT_DTO.setAmount(BigDecimal.valueOf(10000.0));
      CREDIT_DTO.setTerm(12);
      CREDIT.setAmount(CREDIT_DTO.getAmount());
      CREDIT.setTerm(CREDIT_DTO.getTerm());

  }
}

package sergey_white.org.deal.mapper;

import org.springframework.stereotype.Component;
import sergey_white.org.deal.dto.CreditDto;
import sergey_white.org.deal.dto.EmploymentDto;
import sergey_white.org.deal.dto.LoanStatementRequestDto;
import sergey_white.org.deal.entity.Client;
import sergey_white.org.deal.entity.Credit;
import sergey_white.org.deal.entity.Employment;
import sergey_white.org.deal.entity.Passport;

import static sergey_white.org.deal.enums.CreditStatus.CALCULATED;

@Component
public class Mapper {

    public  Client LoanStatementRequestDtoToClient(LoanStatementRequestDto dto){
        Client client = new Client();
        Passport passport = new Passport();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setMiddleName(dto.getMiddleName());
        client.setBirthDate(dto.getBirthdate());
        client.setEmail(dto.getEmail());
        passport.setSeries(dto.getPassportSeries());
        passport.setNumber(dto.getPassportNumber());
        client.setPassport(passport);
        return client;
    }

    public Credit fromCreditDtoToCredit(CreditDto dto) {
        Credit credit = new Credit();
        credit.setAmount(dto.getAmount());
        credit.setTerm(dto.getTerm());
        credit.setMonthlyPayment(dto.getMonthlyPayment());
        credit.setRate(dto.getRate());
        credit.setPsk(dto.getPsk());
        credit.setPaymentSchedule(dto.getPaymentSchedule());
        credit.setInsuranceEnabled(dto.getIsInsuranceEnabled());
        credit.setSalaryClient(dto.getIsSalaryClient());
        credit.setCreditStatus(CALCULATED);
        return credit;
    }

    public Employment fromEmploymentDtoToEmployment(EmploymentDto dto) {
        Employment employment = new Employment();
        employment.setStatus(dto.getEmploymentStatus());
        employment.setEmployerInn(dto.getEmployerINN());
        employment.setSalary(dto.getSalary());
        employment.setPosition(dto.getPosition());
        employment.setWorkExperienceTotal(dto.getWorkExperienceTotal());
        employment.setWorkExperienceCurrent(dto.getWorkExperienceCurrent());
        return employment;
    }
}

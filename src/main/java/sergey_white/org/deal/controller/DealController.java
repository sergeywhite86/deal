package sergey_white.org.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sergey_white.org.deal.dto.FinishRegistrationRequestDto;
import sergey_white.org.deal.dto.LoanOfferDto;
import sergey_white.org.deal.dto.LoanStatementRequestDto;
import sergey_white.org.deal.service.DealService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealController {
    private final DealService service;

    @Operation(summary = "Рассчитать предварительные предложения по кредиту",
            description = "Возвращает список кредитных предложений на основе запроса.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoanOfferDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping("/statement")
    public List<LoanOfferDto> calculateLoanOffers(@RequestBody LoanStatementRequestDto req) {
        return service.getLoanOffers(req);
    }

    @Operation(summary = "Выбрать кредитное предложение",
            description = "Выбирает конкретное кредитное предложение.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping("/offer/select")
    public void chooseLoanOffers(@RequestBody LoanOfferDto req) {
        service.selectOffer(req);
    }

    @Operation(summary = "Рассчитать кредит",
            description = "Рассчитывает кредит на основе данных регистрации.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    @PostMapping("/offer/calculate/{statementId}")
    public void calculateCredit(@RequestBody FinishRegistrationRequestDto request,
                                @Parameter(description = "Идентификатор заявления", required = true) @PathVariable String statementId) {
        service.calculateCredit(request, statementId);
    }
}
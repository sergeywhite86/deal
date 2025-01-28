package sergey_white.org.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sergey_white.org.deal.entity.Statement;
import sergey_white.org.deal.service.StatementService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("deal/admin/statement")
@RequiredArgsConstructor
public class AdminController {

  private final StatementService service;

    @Operation(summary = "Получить заявку по id",
            description = "Получение заявки по id.")
    @GetMapping("/{statementId}")
    public Statement getStatementById(@PathVariable UUID statementId) {
     return service.getStatementById(statementId);
    }

    @Operation(summary = "Получить все заявки",
            description = "Получение всех заявок.")
    @GetMapping()
    public List<Statement> statementId(){
     return service.getStatements();
    }

}

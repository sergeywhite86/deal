package sergey_white.org.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sergey_white.org.deal.service.DocumentService;

import java.util.UUID;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @Operation(
            summary = "запрос на отправку документов",
            description = "Отправляет документы для заявления с указанным ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Документы успешно отправлены",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Заявление не найдено")
            }
    )
    @PostMapping("/{statementId}/send")
    void requestToSendDocuments(
            @PathVariable UUID statementId) {
        documentService.sendDocumentMessage(statementId);
    }

    @Operation(
            summary = "запрос на подписание документов",
            description = "Отправляет ссылку для подписания документов для заявления с указанным ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ссылка успешно отправлена",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Заявление не найдено")
            }
    )
    @PostMapping("/{statementId}/sign")
    void requestToSignDocuments(
            @PathVariable UUID statementId) {
        documentService.sendSigningLinkMessage(statementId);
    }

    @Operation(
            summary = "подписание документов",
            description = "Подписывает документы для заявления с указанным ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Документы успешно подписаны",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Заявление не найдено")
            }
    )
    @PostMapping("/{statementId}/code")
    void signDocuments(
            @PathVariable UUID statementId) {
        documentService.singingDocuments(statementId);
    }
}
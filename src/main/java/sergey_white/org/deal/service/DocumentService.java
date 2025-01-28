package sergey_white.org.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sergey_white.org.deal.dto.EmailMessage;
import sergey_white.org.deal.entity.Statement;
import sergey_white.org.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static sergey_white.org.deal.enums.ApplicationStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;
    private final StatementRepository statementRepository;

    public void sendFinishRegMessage(UUID uuid) {
        Statement statement = statementRepository.getReferenceById(uuid);
        EmailMessage emailMessage = new EmailMessage()
                .setTheme(APPROVED)
                .setStatementId(statement.getStatementId())
                .setAddress(statement.getClient().getEmail())
                .setText("Завершите оформление");
        kafkaTemplate.send("finish-registration", emailMessage);
        log.info("Отправлено сообщение для завершения регистрации: {}", emailMessage); // Логирование
    }

    public void sendPaperworkMessage(UUID statementId) {
        Statement statement = statementRepository.getReferenceById(statementId);
        EmailMessage emailMessage = new EmailMessage()
                .setTheme(CC_APPROVED)
                .setStatementId(statement.getStatementId())
                .setAddress(statement.getClient().getEmail())
                .setText("Перейти к оформлению документов");
        kafkaTemplate.send("create-documents", emailMessage);
        log.info("Отправлено сообщение для оформления документов: {}", emailMessage); // Логирование
    }

    public void sendDocumentMessage(UUID statementId) {
        Statement statement = statementRepository.getReferenceById(statementId);
        statement.setStatus(DOCUMENT_CREATED);
        statementRepository.save(statement);
        EmailMessage emailMessage = new EmailMessage()
                .setTheme(DOCUMENT_CREATED)
                .setStatementId(statement.getStatementId())
                .setAddress(statement.getClient().getEmail())
                .setText("сформированные документы");
        kafkaTemplate.send("send-documents", emailMessage);
        log.info("Отправлено сообщение с документами: {}", emailMessage); // Логирование
    }

    public void sendSigningLinkMessage(UUID statementId) {
        Statement statement = statementRepository.getReferenceById(statementId);
        EmailMessage emailMessage = new EmailMessage()
                .setTheme(DOCUMENT_CREATED)
                .setStatementId(statement.getStatementId())
                .setAddress(statement.getClient().getEmail());
        String sesCode = String.valueOf((int) (Math.random() * 9 + 1));
        String ref = UUID.randomUUID().toString();
        emailMessage.setText(String.format("ссылка на документы - %s \n код - %s ", ref, sesCode));
        addSesCode(statement, sesCode);
        kafkaTemplate.send("send-ses", emailMessage);
        log.info("Отправлено сообщение с ссылкой на подписание: {}", emailMessage); // Логирование
    }

    public void singingDocuments(UUID statementId) {
        Statement statement = statementRepository.getReferenceById(statementId);
        statement.setSignDate(LocalDateTime.now());
        statement.setStatus(CREDIT_ISSUED);
        statementRepository.save(statement);
        log.info("Документы подписаны для заявления с ID: {}", statementId); // Логирование
    }

    private void addSesCode(Statement statement, String sesCode) {
        statement.setSesCode(sesCode);
        statementRepository.save(statement);
        log.info("Добавлен SES код для заявления с ID: {}", statement.getStatementId()); // Логирование
    }
}
package sergey_white.org.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sergey_white.org.deal.entity.Statement;
import sergey_white.org.deal.repository.StatementRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

    private final StatementRepository repository;

    public Statement getStatementById(UUID id) {
        log.info("Админ запрос на получение заявки по : {}", id);
        return repository.findById(id).orElseThrow(()->new RuntimeException("Заявка не обнаружена"));
    }

    public List<Statement> getStatements() {
        log.info("Админ запрос на получение заявок");
        return repository.findAll();
    }
}

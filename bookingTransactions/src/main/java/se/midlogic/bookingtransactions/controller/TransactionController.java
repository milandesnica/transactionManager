package se.midlogic.bookingtransactions.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import se.midlogic.bookingtransactions.service.CSVService;
import se.midlogic.bookingtransactions.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {

    final CSVService csvService;
    final TransactionService transactionService;

    public TransactionController(CSVService csvService, TransactionService transactionService) {
        this.csvService = csvService;
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public Mono<ResponseEntity<Object>> receiveBatchTransactions(@RequestBody String batchTransactions) {
        List<Transaction> transactions = csvService.parseCSVString(batchTransactions.replace("\"", ""));

        return transactionService.processTransactions(transactions)
                .map(rejectedTransactions -> {
                    if (!rejectedTransactions.isEmpty()) {
                        // Create a JSON response for rejected transactions
                        Map<String, List<Transaction>> responseMap = new HashMap<>();
                        responseMap.put("Rejected Transactions", rejectedTransactions);
                        return ResponseEntity.badRequest().body(responseMap);
                    } else {
                        return ResponseEntity.ok().build();
                    }
                });
    }

    @GetMapping("/users")
    public Mono<ResponseEntity<Object>> getAllUsers() {
        return Mono.just(ResponseEntity.ok(transactionService.getAllUsers()));
    }

}

package se.midlogic.bookingtransactions.service;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import se.midlogic.bookingtransactions.controller.Transaction;
import se.midlogic.bookingtransactions.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class TransactionService {

    Map<String, User> userDatabase = new ConcurrentHashMap<>();
    @PostConstruct // Initialize data after bean construction
    public void initializeDefaultUsers() {
        addUser(new User("Mary", "Lamb", "mary@lamb.com", 400.0));
        addUser(new User("Kia", "Karlsson", "kia@karlsson.com", 400.0));
        addUser(new User("Urban", "Landscape", "urban@landscape.com", 400.0));
    }
    public void addUser(User user) {
        userDatabase.put(user.getEmail(), user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userDatabase.values());
    }

    public void removeUser(String email) {
        userDatabase.remove(email);
    }
    public Mono<List<Transaction>> processTransactions(List<Transaction> transactions) {
        List<Transaction> rejectedTransactions = new ArrayList<>();

        return Flux.fromIterable(transactions)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(transaction -> {
                    String userId = transaction.getEmail();
                    User user = userDatabase.get(userId);

                    if (user != null) {
                        double transactionAmount = transaction.getAmount();
                        double newCreditLimit = user.getCreditLimit() - transactionAmount;

                        if (newCreditLimit >= 0) {
                            user.setCreditLimit(newCreditLimit);
                            user.getTransactions().add(transaction);
                            return Mono.just(transaction);
                        } else {
                            // Reject the transaction
                            rejectedTransactions.add(transaction);
                            return Mono.empty();
                        }
                    } else {
                        // User not found, handle accordingly
                        return Mono.empty();
                    }
                })
                .sequential()
                .collectList()
                .map(acceptedTransactions -> rejectedTransactions);
    }
}

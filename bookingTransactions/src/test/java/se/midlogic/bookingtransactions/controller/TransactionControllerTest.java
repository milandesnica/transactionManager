package se.midlogic.bookingtransactions.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.midlogic.bookingtransactions.model.User;
import se.midlogic.bookingtransactions.service.TransactionService;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
public class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void removeUsers() {
        Stream.of("john@doe.com", "john@doe1.com", "john@doe2.com").forEach(transactionService::removeUser);
    }


    @Test
    public void allTransactionsPerformedSuccessfully() {
        // put users in database

        transactionService.addUser(new User("John", "Doe", "john@doe.com", 400.0));
        transactionService.addUser(new User("John", "Doe1", "john@doe1.com", 400.0));
        transactionService.addUser(new User("John", "Doe2", "john@doe2.com", 400.0));

        // post body data
        String csvData = """
                John,Doe,john@doe.com,190,TR0001
                John,Doe1,john@doe1.com,200,TR0002
                John,Doe2,john@doe2.com,201,TR0003
                John,Doe,john@doe.com,9,TR0004
                John,Doe,john@doe.com,2,TR0005""";

        // Send a POST request with the test data
        webTestClient.post()
                .uri("/transaction")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(csvData)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> System.out.printf("Response status: %s%n", response.getStatus()));
    }

    @Test
    public void someTransactionsFailedNoCredit() {
        // put users in database

        transactionService.addUser(new User("John", "Doe", "john@doe.com", 400.0));
        transactionService.addUser(new User("John", "Doe1", "john@doe1.com", 400.0));
        transactionService.addUser(new User("John", "Doe2", "john@doe2.com", 400.0));

        // post body data
        String csvData = """
                John,Doe,john@doe.com,390,TR0001
                John,Doe1,john@doe1.com,200,TR0002
                John,Doe2,john@doe2.com,201,TR0003
                John,Doe,john@doe.com,9,TR0004
                John,Doe,john@doe.com,2,TR0005""";

        // Send a POST request with the test data
        webTestClient.post()
                .uri("/transaction")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(csvData)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    try {
                        JsonNode jsonNode = objectMapper.readTree(responseBody);
                        assertThat(jsonNode.get("Rejected Transactions").get(0).get("firstName").asText())
                                .isEqualTo("John");
                        assertThat(jsonNode.get("Rejected Transactions").get(0).get("email").asText())
                                .isEqualTo("john@doe.com");
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing JSON response", e);
                    }
                });
    }

}

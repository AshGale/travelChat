package co.uk.travelChat.repository;

import co.uk.travelChat.TravelChatApplication;
import co.uk.travelChat.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TravelChatApplication.class)
public class AccountCrudRepositoryManualTest {

    @Autowired
    AccountCrudRepository repository;

    @Test
    public void givenNickname_whenFindAllByNickname_thenFindAccount() {
        repository.save(new Account(null, "Bill", "bil", new ArrayList<>())).block();
        Mono<Account> accountFlux = repository.findFirst1ByNicknameIgnoreCase("bil");

        StepVerifier.create(accountFlux)
                .assertNext(account -> {
                    assertEquals("Bill", account.getName());
                    assertEquals("bil" , account.getNickname());
                    assertNotNull(account.getId());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void givenName_whenFindFirstByName_thenFindAccount() {
        repository.save(new Account(null, "Bill", "bil", new ArrayList<>())).block();
        Flux<Account> accountMono = repository.findAccountsByName("Bill");

        StepVerifier.create(accountMono)
                .assertNext(account -> {
                    assertEquals("Bill", account.getName());
                    assertEquals("bil" , account.getNickname());
                    assertNotNull(account.getId());
                })
                .expectComplete()
                .verify();



    }

    @Test
    public void givenAccount_whenSave_thenSaveAccount() {
        Mono<Account> accountMono = repository.save(new Account(null, "Bill", "bil", new ArrayList<>()));

        StepVerifier
                .create(accountMono)
                .assertNext(account -> assertNotNull(account.getId()))
                .expectComplete()
                .verify();
    }


}

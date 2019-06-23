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

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TravelChatApplication.class)
public class AccountCrudRepositoryManualTest {

    @Autowired
    AccountCrudRepository repository;

    @Test
    public void givenNickname_whenFindAllByNickname_thenFindAccount() {
        repository.save(new Account(null, "Bill", "bil", new LinkedHashMap<>())).block();
        Flux<Account> accountFlux = repository.findAllByNickname("bil");

        StepVerifier.create(accountFlux.last())
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
        repository.save(new Account(null, "Bill", "bil", new LinkedHashMap<>())).block();
        Mono<Account> accountMono = repository.findFirstByName("Bill");

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
        Mono<Account> accountMono = repository.save(new Account(null, "Bill", "bil", new LinkedHashMap<>()));

        StepVerifier
                .create(accountMono)
                .assertNext(account -> assertNotNull(account.getId()))
                .expectComplete()
                .verify();
    }


}

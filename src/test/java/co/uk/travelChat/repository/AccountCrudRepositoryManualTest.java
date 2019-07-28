package co.uk.travelChat.repository;

import co.uk.travelChat.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataMongoTest
@RunWith(SpringRunner.class)
public class AccountCrudRepositoryManualTest {

    @Autowired
    AccountCrudRepository repository;

    @Test
    public void findFirst1ByNicknameIgnoreCase() {
        repository.save(new Account(null, "Bill", "bil", new ArrayList<>())).subscribe();
        Mono<Account> accountFlux = repository.findFirst1ByNicknameIgnoreCase("bil");

        StepVerifier.create(accountFlux)
                .assertNext(account -> {
                    assertEquals("Bill", account.getName());
                    assertEquals("bil", account.getNickname());
                    assertNotNull(account.getId());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findAccountsByName() {
        repository.save(new Account(null, "Bill", "bil", new ArrayList<>())).subscribe();
        Flux<Account> accountMono = repository.findAccountsByName("Bill");

        StepVerifier.create(accountMono)
                .assertNext(account -> {
                    assertEquals("Bill", account.getName());
                    assertEquals("bil", account.getNickname());
                    assertNotNull(account.getId());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void AccountSave() {
        Mono<Account> accountMono = repository.save(new Account(null, "Bill", "bil", new ArrayList<>()));

        StepVerifier
                .create(accountMono)
                .assertNext(account -> assertNotNull(account.getId()))
                .expectComplete()
                .verify();
    }


}

package co.uk.travelChat.repository;

import co.uk.travelChat.model.Account;
import org.junit.Before;
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

@DataMongoTest
@RunWith(SpringRunner.class)
public class AccountCrudRepositoryTest {

    private static final String DEFAULT_ID = "5d46b4c5966049317459ea60";
    private static final String DEFAULT_NAME = "Adam";
    private static final String DEFAULT_NICKNAME = "andy123";
    private static final ArrayList<String> DEFAULT_TRIPS = new ArrayList<>();

    @Autowired
    AccountCrudRepository accountCrudRepository;

    @Before
    public void setUp() throws Exception {
        accountCrudRepository.deleteAll().subscribe();
        accountCrudRepository.save(new Account(DEFAULT_ID, DEFAULT_NAME, DEFAULT_NICKNAME, DEFAULT_TRIPS)).subscribe();
        Thread.sleep(100);//this is to ensure the data is loaded correct
    }

    @Test
    public void findFirst1ByNicknameIgnoreCase() {
        Mono<Account> accountFlux = accountCrudRepository.findFirst1ByNicknameIgnoreCase(DEFAULT_NICKNAME);

        StepVerifier.create(accountFlux)
                .assertNext(account -> {
                    assertEquals(DEFAULT_NAME, account.getName());
                    assertEquals(DEFAULT_NICKNAME, account.getNickname());
                    assertEquals(DEFAULT_TRIPS, account.getTrips());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findAccountsByName() {
        Flux<Account> accountMono = accountCrudRepository.findAccountsByName(DEFAULT_NAME);

        StepVerifier.create(accountMono)
                .assertNext(account -> {
                    assertEquals(DEFAULT_NAME, account.getName());
                    assertEquals(DEFAULT_NICKNAME, account.getNickname());
                    assertEquals(DEFAULT_TRIPS, account.getTrips());
                })
                .expectComplete()
                .verify();
    }
}

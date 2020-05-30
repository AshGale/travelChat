package co.uk.travelChat.service;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@DataMongoTest(includeFilters = @ComponentScan.Filter(Service.class))
@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @MockBean
    AccountCrudRepository accountCrudRepository;

    private String primaryKey = "nickname";
    private static final Account testAccount = new Account("nickname", "displayName", new ArrayList<>());

    @Before
    public void init() {

    }

    @Test
    public void getAccountById() {
        Mockito.when(accountCrudRepository.findById(primaryKey))
                .thenReturn(Mono.just(testAccount));

        Mono<Account> result = accountService.getAccountById(primaryKey);

        StepVerifier.create(result)
                .assertNext(account -> {
                    assertEquals(testAccount.getName(), account.getName());
                    assertEquals(testAccount.getNickname(), account.getNickname());
                    assertEquals(testAccount.getTrips(), account.getTrips());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void saveAccount() {
        Mockito.when(accountCrudRepository.save(testAccount))
                .thenReturn(Mono.just(testAccount));

        Mono<Account> result = accountService.saveAccount(testAccount);

        StepVerifier.create(result)
                .assertNext(account -> {
                    assertEquals(testAccount.getName(), account.getName());
                    assertEquals(testAccount.getNickname(), account.getNickname());
                    assertEquals(testAccount.getTrips(), account.getTrips());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getAccountByName() {
        Mockito.when(accountCrudRepository.getAllByName(testAccount.getName()))
                .thenReturn(Flux.just(testAccount));

        Flux<Account> result = accountService.getAccountsByName(testAccount.getName());

        StepVerifier.create(result)
                .assertNext(account -> {
                    assertEquals(testAccount.getName(), account.getName());
                    assertEquals(testAccount.getNickname(), account.getNickname());
                    assertEquals(testAccount.getTrips(), account.getTrips());
                })
                .expectComplete()
                .verify();
    }

//    @Test
//    public void getAccountsByNickname() {
//        Mockito.when(accountCrudRepository.findFirst1ByNicknameIgnoreCase(testAccount.getNickname()))
//                .thenReturn(Mono.just(testAccount));
//
//        Mono<Account> result = accountService.getAccountByNickname(testAccount.getNickname());
//
//        StepVerifier.create(result)
//                .assertNext(account -> {
//                    assertEquals(testAccount.getName(), account.getName());
//                    assertEquals(testAccount.getNickname(), account.getNickname());
//                    assertEquals(testAccount.getTrips(), account.getTrips());
//                })
//                .expectComplete()
//                .verify();
//    }

}
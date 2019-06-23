package co.uk.travelChat.service;

import co.uk.travelChat.TravelChatApplication;
import co.uk.travelChat.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TravelChatApplication.class)
public class AccountServiceTest {

    //TODO fix with mockito

    @Autowired
    AccountService accountService;

    @Test
    public void getAccountById() {
        String id = "4ecc05e55dd98a436ddcc47c";
        Account testAccount = new Account(id, "getAccountById", "getAccountById", new LinkedHashMap<>());
        accountService.saveAccount(testAccount).subscribe();

        Mono<Account> savedAccout = accountService.getAccountById(id);

        assertEquals(id, savedAccout.block().getId());
        accountService.deleteAccoutById(id);
    }

    @Test
    public void saveAccount() {
        String id = "4ecc05e55dd98a436ddcc47c";
        Account testAccount = new Account(id, "saveAccount", "saveAccount", new LinkedHashMap<>());
        accountService.saveAccount(testAccount).subscribe();

        Mono<Account> savedAccout = accountService.getAccountById(id);

        assertEquals(testAccount, savedAccout.block());
        accountService.deleteAccoutById(id);
    }

    @Test
    public void getAccountByName() {
        String id = "4ecc05e55dd98a436ddcc47c";
        String name = "namedAccount";
        Account testAccount = new Account(id, name, name, new LinkedHashMap<>());
        accountService.saveAccount(testAccount).subscribe();

        Mono<Account> retrivedAccout = accountService.getAccountByName(name);

        assertEquals(testAccount, retrivedAccout.block());
        accountService.deleteAccoutById(id);
    }

    @Test
    public void getAccountsByNickname() {
        String id = "4ecc05e55dd98a436ddcc47c";
        String nickname = "nicknamedAccount";
        Account testAccount = new Account(id, nickname, nickname, new LinkedHashMap<>());
        accountService.saveAccount(testAccount).subscribe();

        Flux<Account> retrivedAccout = accountService.getAccountsByNickname(nickname);

        assertEquals(testAccount, retrivedAccout.blockFirst());
        accountService.deleteAccoutById(id);
    }

}
package co.uk.travelChat.controller;

import co.uk.travelChat.TravelChatApplication;
import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import co.uk.travelChat.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TravelChatApplication.class)
@DataMongoTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(Controller.class)})
public class AccountControllerTest {

    private static final String defaultId = "507f1f77bcf86cd799439011";

    private static Account testAccount = new Account(null, "account", "account", new ArrayList<>());

    private static Account savedAccount = new Account(defaultId, "account", "account", new ArrayList<>());

    @Autowired
    private AccountCrudRepository accountCrudRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountController underTest;

    private WebTestClient webTestClient;

    @Before
    public void init() {
        this.webTestClient = WebTestClient.bindToController(new AccountController(accountService)).build();
    }

    @Test
    public void getAccountById() {

        accountCrudRepository.save(savedAccount).subscribe();

        webTestClient.get().uri("/account/" + defaultId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Account.class)
                .value(account -> {
                    assertEquals(savedAccount.getId(), account.getId());
                    assertEquals(savedAccount.getName(), account.getName());
                    assertEquals(savedAccount.getNickname(), account.getNickname());
                    assertEquals(savedAccount.getTrips(), account.getTrips());
                });
    }

    @Test
    public void saveAccount() {

        Mockito.when(accountService.saveAccount(testAccount))
                .thenReturn(Mono.just(savedAccount));

        webTestClient.post().uri("/account")
                .body(Mono.just(testAccount), Account.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Account.class)
                .isEqualTo(savedAccount);
    }

    @Test
    public void deleteAccountById() {

        Mockito.when(accountService.deleteAccountById(defaultId))
                .thenReturn(Mono.empty());

        webTestClient.delete().uri("/account")
                .exchange()
                .expectBody()
                .isEmpty();

    }

    @Test
    public void getAllAccounts() {

        Flux<Account> accountFlux = Flux.just(
                new Account(null, "Adam", "andy123", new ArrayList<>()),
                new Account(null, "Adam", "andy123", new ArrayList<>()),
                new Account(null, "Carol", "carloMeUp", new ArrayList<>()));

        Mockito.when(accountService.getAllAccounts())
                .thenReturn(accountFlux);

        WebTestClient.ListBodySpec<Account> result = webTestClient.get().uri("/account")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Account.class)
                .hasSize(3)
                .contains(accountFlux.blockFirst());
    }
}
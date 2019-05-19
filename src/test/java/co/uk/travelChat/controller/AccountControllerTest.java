package co.uk.travelChat.controller;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import co.uk.travelChat.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    private static final String defaultId = "507f1f77bcf86cd799439011";

    private static Account account = new Account(null, "account", "account");

    private static Account savedAccount = new Account(defaultId, "account", "account");

    @Mock
    private AccountCrudRepository accountCrudRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController underTest;

    @Autowired
    private WebTestClient webTestClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.webTestClient = WebTestClient.bindToController(new AccountController(accountService)).build();
    }

    @Test
    public void saveAccount() {

        Mockito.when(accountService.saveAccount(account))
                .thenReturn(Mono.just(savedAccount));

        webTestClient.post().uri("/account")
                .body(Mono.just(account), Account.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Account.class)
                .isEqualTo(savedAccount);
    }

    @Test
    public void deleteAccountById() {

        Mockito.when(accountService.deleteAccoutById(defaultId))
                .thenReturn(Mono.empty());

        webTestClient.delete().uri("/account")
                .exchange()
                .expectBody()
                .isEmpty();

    }

    @Test
    public void getAccountById() {

        Mockito.when(accountService.getAccountById(defaultId))
                .thenReturn(Mono.just(savedAccount));

        webTestClient.get().uri("/account/" + defaultId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Account.class)
                .isEqualTo(savedAccount);

    }

    @Test
    public void getAllAccounts() {

        Flux<Account> accountFlux = Flux.just(
                new Account(null, "Adam", "andy123"),
                new Account(null, "Adam", "andy123"),
                new Account(null, "Carol", "carloMeUp"));

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
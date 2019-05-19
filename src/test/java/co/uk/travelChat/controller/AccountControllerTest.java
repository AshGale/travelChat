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
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebFluxTest
//@ActiveProfiles("test")
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

//        Flux<Account> accountFlux = Flux.just(
//                new Account(null, "Adam", "andy123"),
//                new Account(null, "Adam", "andy123"),
//                new Account(null, "Carol", "carloMeUp"));
//
//        Mockito.when(accountCrudRepository.findAll())
//                .thenReturn(accountFlux);
//
//        FluxExchangeResult<Account> result = webTestClient.get().uri("/account")
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .returnResult(Account.class);
//
//        System.out.println(result.getResponseBody().map(account1 -> account1.toString()));
//
//        StepVerifier.create(result.getResponseBody())
//                .expectNext(accountFlux.blockFirst())
//                //.expectNextCount(1)
//                .expectComplete()
//                .verify();
    }

    @Test
    public void saveAccount() {
    }

    @Test
    public void deleteAccountById() {
    }
}
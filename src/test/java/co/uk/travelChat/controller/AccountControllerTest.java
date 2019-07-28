package co.uk.travelChat.controller;

import co.uk.travelChat.TravelChatApplication;
import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import co.uk.travelChat.service.AccountService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        webTestClient.post().uri("/account")
                .body(Mono.just(testAccount), Account.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Account.class)
                .value(account -> {
                    assertNotNull(account.getId());
                    assertEquals(savedAccount.getName(), account.getName());
                    assertEquals(savedAccount.getNickname(), account.getNickname());
                    assertEquals(savedAccount.getTrips(), account.getTrips());
                });
    }

    @Test
    public void deleteAccountById() {

        accountCrudRepository.save(savedAccount).subscribe();

        assertNotNull(accountCrudRepository.findById(defaultId));

        webTestClient.delete().uri("/account/" + defaultId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        webTestClient.get().uri("/account/" + defaultId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void getAllAccounts() {
        List<String> exampleTrips = new ArrayList<>();

        Map<Integer, Account> accountMap = new LinkedHashMap<>();
        accountMap.put(1, new Account(null, "Adam", "andy123", new ArrayList<>()));
        exampleTrips.add(ObjectId.get().toString());
        accountMap.get(1).setTrips(exampleTrips);

        exampleTrips = new ArrayList<>();
        accountMap.put(2, new Account(null, "Ben", "benny", new ArrayList<>()));
        exampleTrips.add(ObjectId.get().toString());
        exampleTrips.add(ObjectId.get().toString());
        accountMap.get(2).setTrips(exampleTrips);

        exampleTrips = new ArrayList<>();
        accountMap.put(3, new Account(null, "Carol", "carloMeUp", new ArrayList<>()));
        exampleTrips.add(ObjectId.get().toString());
        exampleTrips.add(ObjectId.get().toString());
        accountMap.get(3).setTrips(exampleTrips);

        exampleTrips = new ArrayList<>();
        accountMap.put(4, new Account(null, "Dean", "theDean", new ArrayList<>()));
        exampleTrips.add(ObjectId.get().toString());
        accountMap.get(4).setTrips(exampleTrips);

        accountMap.forEach((key, account) -> accountCrudRepository.save(account)
                .subscribe(savedTrip -> System.out.println(savedTrip.toString())));

        WebTestClient.ListBodySpec<Account> result = webTestClient.get().uri("/account")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Account.class)
                .hasSize(4)
                .contains(accountMap.get(2));
    }
}
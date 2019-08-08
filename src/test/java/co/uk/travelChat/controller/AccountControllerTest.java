package co.uk.travelChat.controller;

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(Controller.class)})
public class AccountControllerTest {

    private static final String defaultId = "507f1f77bcf86cd799439011";

    private static final List<String> tripList = Arrays.asList("507f1f77bcf86cd799439010");

//    private static Account testAccount = new Account(null, "account", "account", new ArrayList<>());

    private static final Account savedAccount = new Account(defaultId, "account", "account", tripList);

    @Autowired
    private AccountCrudRepository accountCrudRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountController underTest;

    private WebTestClient webTestClient;

    private List<String> exampleTrips = new ArrayList<>();
    private Map<Integer, Account> accountMap = new LinkedHashMap<>();

    @Before
    public void init() {
        this.webTestClient = WebTestClient.bindToController(new AccountController(accountService)).build();

        //SetupData
        accountCrudRepository.deleteAll().subscribe();
        exampleTrips = new ArrayList<>();
        accountMap = new LinkedHashMap<>();

        accountMap.put(0, new Account("507f1f77bcf86cd799439012", "Adam", "andy123", new ArrayList<>()));
        exampleTrips.add(ObjectId.get().toString());
        accountMap.get(0).setTrips(exampleTrips);

        exampleTrips = new ArrayList<>();
        accountMap.put(1, new Account("507f1f77bcf86cd799439013", "Ben", "benny", new ArrayList<>()));
        exampleTrips.add(ObjectId.get().toString());
        exampleTrips.add(ObjectId.get().toString());
        accountMap.get(1).setTrips(exampleTrips);

        exampleTrips = new ArrayList<>();
        accountMap.put(2, new Account("507f1f77bcf86cd799439014", "Carol", "carloMeUp", new ArrayList<>()));
        exampleTrips.add(ObjectId.get().toString());
        exampleTrips.add(ObjectId.get().toString());
        accountMap.get(2).setTrips(exampleTrips);

        exampleTrips = new ArrayList<>();
        accountMap.put(3, new Account("507f1f77bcf86cd799439015", "Dean", "theDean", new ArrayList<>()));
        exampleTrips.add(ObjectId.get().toString());
        accountMap.get(3).setTrips(exampleTrips);

        accountMap.forEach((key, account) -> accountCrudRepository.save(account)
                .subscribe(savedTrip -> System.out.println(savedTrip.toString())));
    }

    @Test
    public void getAllAccounts() {
        webTestClient.get().uri("/account")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Account.class)
                .hasSize(4)
                .contains(accountMap.get(2));
    }

    @Test
    public void saveAccount() {
        webTestClient.post().uri("/account")
                .body(Mono.just(savedAccount), Account.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Account.class)
                .value(account -> {
                    assertEquals(savedAccount.getId(), account.getId());
                    assertEquals(savedAccount.getName(), account.getName());
                    assertEquals(savedAccount.getNickname(), account.getNickname());
                    assertEquals(savedAccount.getTrips(), account.getTrips());
                });
    }

    @Test
    public void getAccountById() {
        webTestClient.get().uri("/account/" + accountMap.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Account.class)
                .value(account -> {
                    assertEquals(accountMap.get(0).getId(), account.getId());
                    assertEquals(accountMap.get(0).getName(), account.getName());
                    assertEquals(accountMap.get(0).getNickname(), account.getNickname());
                    assertEquals(accountMap.get(0).getTrips(), account.getTrips());
                });
    }

    @Test
    public void deleteAccountById() {
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
    public void getAccountByName() {
        webTestClient.get().uri("/account/name/" + accountMap.get(1).getName())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Account.class)
                .hasSize(1)
                .value(account -> {
                    assertEquals(accountMap.get(1).getId(), account.get(0).getId());
                    assertEquals(accountMap.get(1).getName(), account.get(0).getName());
                    assertEquals(accountMap.get(1).getNickname(), account.get(0).getNickname());
                    assertEquals(accountMap.get(1).getTrips(), account.get(0).getTrips());
                });
    }

    @Test
    public void getAccountByNickname() {
        webTestClient.get().uri("/account/nickname/" + accountMap.get(2).getNickname())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Account.class)
                .value(account -> {
                    assertEquals(accountMap.get(2).getId(), account.getId());
                    assertEquals(accountMap.get(2).getName(), account.getName());
                    assertEquals(accountMap.get(2).getNickname(), account.getNickname());
                    assertEquals(accountMap.get(2).getTrips(), account.getTrips());
                });
    }

    @Test
    public void getAllTripsForAccount() {
        webTestClient.get().uri("/account/" + accountMap.get(3).getId() + "/trips")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(List.class)
                .value(list -> {
                    assertEquals(accountMap.get(3).getTrips(), list);

                });
    }

    @Test
    public void getAllTripsForAccountByNickname() {
        webTestClient.get().uri("/account/nickname/" + accountMap.get(0).getNickname() + "/trips")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(List.class)
                .value(list -> {
                    assertEquals(accountMap.get(0).getTrips(), list);

                });
    }
}
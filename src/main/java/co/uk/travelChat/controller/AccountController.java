package co.uk.travelChat.controller;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("{pageSize}/{offset}")
    public Flux<Account> getAllAccounts(@PathVariable int pageSize, @PathVariable int offset) {
        return accountService.getAllAccounts(pageSize, offset);
    }

    @GetMapping(value = "/{id}")
    public Mono<Account> getAccountById(@PathVariable String id) {
        return accountService.getAccountById(id);
    }

    @PostMapping()
    public Mono<Account> saveAccount(@Valid @RequestBody Account account) {
        return accountService.saveAccount(account);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> deleteAccountById(@PathVariable String id) {
        return accountService.deleteAccountById(id);
    }

    @GetMapping(value = "/nickname/{nickname}")
    public Mono<Account> getAccountByNickname(@PathVariable String nickname) {
        return accountService.getAccountById(nickname);
    }

    @GetMapping(value = "/name/{name}")
    public Flux<Account> getAccountByName(@PathVariable String name) {
        return accountService.getAccountsByName(name);
    }

    @GetMapping(value = "/{id}/trips")
    public Mono<List<String>> getAllTripsForAccount(@PathVariable String id) {
        return accountService.getAllTripsForAccountById(id);
    }

    @GetMapping(value = "/nickname/{nickname}/trips")
    public Mono<List<String>> getAllTripsForAccountByNickname(@PathVariable String nickname) {
        return accountService.getAllTripsForAccountByNickname(nickname);
    }


}

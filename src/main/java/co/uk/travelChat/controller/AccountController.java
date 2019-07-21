package co.uk.travelChat.controller;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "")
    public Flux<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value = "/{id}")
    public Mono<Account> getAccountById(@PathVariable String id) {
        return accountService.getAccountById(id);
    }

    @PostMapping(value = "")
    public Mono<Account> saveAccount(@RequestBody Account account){
        return accountService.saveAccount(account);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> deleteAccountById(@PathVariable String id){
        return accountService.deleteAccountById(id);
    }

    @GetMapping(value = "/name/{name}")
    public Mono<Account> getAccountByName(@PathVariable String name) {
        return accountService.getAccountByName(name);
    }

    @GetMapping(value = "/nickname/{nickname}")
    public Flux<Account> getAccountByNickname(@PathVariable String nickname) {
        return accountService.getAccountsByNickname(nickname);
    }

}

package co.uk.travelChat.controller;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/account")
    public Flux<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping(value = "/account/{id}")
    public Mono<Account> getAccountById(@PathVariable String id) {
        return accountService.getAccountById(id);
    }

    @PostMapping(value = "/account")
    public Mono<Account> saveAccount(@RequestBody Account account){
        return accountService.saveAccount(account);
    }

    @DeleteMapping(value = "/accounts/{id}")
    public Mono<Void> deleteAccountById(@PathVariable String id){
        return accountService.deleteAccoutById(id);
    }


}

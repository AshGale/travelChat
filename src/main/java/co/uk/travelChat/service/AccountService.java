package co.uk.travelChat.service;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

    private final AccountCrudRepository accountCrudRepository;


    public AccountService(AccountCrudRepository accountCrudRepository) {
        this.accountCrudRepository = accountCrudRepository;
    }

    public Mono<Account> getAccountById(String id){
        return accountCrudRepository.findById(id);
    }

    public Mono<Account> saveAccount(Account account){
        return accountCrudRepository.save(account);
    }

    public Mono<Account> getAccountByName(String name){
        return accountCrudRepository.findByName(name);
    }

    public Flux<Account> getAccountsByNickname(String nickname){
        return accountCrudRepository.findAccountsByNickname(nickname);
    }

    public Mono<Void> deleteAccountById(String id) {
        return accountCrudRepository.deleteById(id);
    }

    public Flux<Account> getAllAccounts() {
        return  accountCrudRepository.findAll();
    }
}

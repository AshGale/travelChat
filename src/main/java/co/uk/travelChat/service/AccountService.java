package co.uk.travelChat.service;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    public Flux<Account> getAccountsByName(String name) {
        return accountCrudRepository.findAccountsByName(name);
    }

    public Mono<Account> getAccountByNickname(String nickname) {
        return accountCrudRepository.findFirst1ByNicknameIgnoreCase(nickname);
    }

    public Mono<Void> deleteAccountById(String id) {
        return accountCrudRepository.deleteById(id);
    }

    public Flux<Account> getAllAccounts() {
        return  accountCrudRepository.findAll();
    }

    public Mono<List<String>> getAllTripsForAccountById(String id) {
        return accountCrudRepository.findById(id).map(account -> account.getTrips());
    }

    public Mono<List<String>> getAllTripsForAccountByNickname(String nickname) {
        return accountCrudRepository.findFirst1ByNicknameIgnoreCase(nickname)
                .map(account -> account.getTrips());
    }
}

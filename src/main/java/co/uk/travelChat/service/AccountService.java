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

    public Flux<Account> getAllAccounts(int pageSize, int offset) {
        return accountCrudRepository.findPage(pageSize, offset);
    }

    public Mono<Account> getAccountById(String id) {
        return accountCrudRepository.findById(id.toLowerCase());
    }

    public Mono<Account> saveAccount(Account account) {
        account.setName(account.getName().toLowerCase());
        return accountCrudRepository.save(account);
    }

    public Flux<Account> getAccountsByName(String name) {
        return accountCrudRepository.getAllByName(name);
    }


    public Mono<Void> deleteAccountById(String id) {
        return accountCrudRepository.deleteById(id);
    }

    public Mono<List<String>> getAllTripsForAccountById(String id) {
        return accountCrudRepository.findById(id).map(account -> account.getTrips());
    }

    public Mono<List<String>> getAllTripsForAccountByNickname(String nickname) {
        return accountCrudRepository.findById(nickname.toLowerCase())
                .map(Account::getTrips);
    }
}

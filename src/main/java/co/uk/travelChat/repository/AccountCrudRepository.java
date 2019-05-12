package co.uk.travelChat.repository;

import co.uk.travelChat.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountCrudRepository
        extends ReactiveCrudRepository<Account, String> {

    Flux<Account> findAllByNickname(String Nickname);
    Mono<Account> findFirstByName(String name);
}

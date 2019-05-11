package co.uk.travelChat;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountCrudRepository
        extends ReactiveCrudRepository<Account, String> {

    Flux<Account> findAllByValue(double value);
    Mono<Account> findFirstByOwner(String owner);
}

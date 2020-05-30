package co.uk.travelChat.repository;

import co.uk.travelChat.model.Account;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AccountCrudRepository extends ReactiveCouchbaseSortingRepository<Account, String> {

    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} ORDER BY name LIMIT $1 OFFSET $2;")
    Flux<Account> findPage(int limit, int offset);

//    Mono<Account> findFirst1ByNicknameIgnoreCase(String Nickname);

    @Query("#{#n1ql.selectEntity} where #{#n1ql.filter} AND name = $1 ORDER BY name LIMIT 5 OFFSET 0;")
    Flux<Account> getAllByName(String name);
}

package co.uk.travelChat.repository;

import co.uk.travelChat.model.Location;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LocationCrudRepository
        extends ReactiveCrudRepository<Location, String> {

    Mono<Location> findByLongitudeAndLatitude(Double longitude, Double latitude);
    Flux<Location> findAllByName(String name);
}
package co.uk.travelChat.repository;

import co.uk.travelChat.model.Location;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LocationCrudRepository
        extends ReactiveCrudRepository<Location, String> {

    Mono<Location> findFirstByName(String name);

    Flux<Location> findLocationsByNameAndLongitudeAndLatitude(String name, double longitude, double latitude);

    Flux<Location> findLocationsByLongitudeAndLatitude(double longitude, double latitude);

    Flux<Location> findLocationsByName(String name);
}
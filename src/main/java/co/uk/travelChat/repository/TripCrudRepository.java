package co.uk.travelChat.repository;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface TripCrudRepository
        extends ReactiveCrudRepository<Trip, String> {

    Flux<Trip> findAllByLeavingAndDepartingAndMode(
            LocalDateTime leaving, Location departing, ModeOfTransport mode);
    Flux<Trip> findAllByLeavingAndArrivingAndDepartingAndDestinationAndMode(
            LocalDateTime leaving, LocalDateTime arriving, Location departing, Location destination, ModeOfTransport mode);
}
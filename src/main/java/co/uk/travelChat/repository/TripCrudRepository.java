package co.uk.travelChat.repository;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface TripCrudRepository extends ReactiveCrudRepository<Trip, String> {

    Flux<Trip> findAllByLeavingAndDepartingNameAndMode(
            LocalDateTime leaving, String departingName, ModeOfTransport mode);

    Flux<Trip> findTripsByLeavingAndArrivingAndDepartingAndDestinationAndMode(
            LocalDateTime leaving, LocalDateTime arriving, Location departing, Location destination, ModeOfTransport mode);

    Flux<Trip> findByDeparting_Name(String departing);

    Flux<Trip> findByDestination_Name(String destination);

    Flux<Trip> findByDeparting_NameAndDestination_Name(String departing, String destination);
}
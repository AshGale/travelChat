package co.uk.travelChat.repository;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Trip;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface TripCrudRepository extends ReactiveCouchbaseSortingRepository<Trip, String> {

    //note you can use _before a sub object's field, ie person has talets{name,skill} can use Talents_skill

    Flux<Trip> findAllByLeavingAndDepartingAndMode(
            Long leaving, String departingName, ModeOfTransport mode);

    Flux<Trip> findTripsByLeavingAndArrivingAndDepartingAndDestinationAndMode(
            LocalDateTime leaving, LocalDateTime arriving, String departing, String destination, ModeOfTransport mode);

    Flux<Trip> findByDeparting(String departing);

    Flux<Trip> findByDestination(String destination);

    Flux<Trip> findByDepartingAndDestination(String departing, String destination);

    Flux<Trip> findByDepartingAndLeaving(String departing, Long leaving);

    Flux<Trip> findByDestinationAndArriving(String destination, Long arrivingTime);

    Flux<Trip> findByDepartingAndLeavingAndDestinationAndArriving(
            String departing, Long leaving, String destination, Long arriving);

}
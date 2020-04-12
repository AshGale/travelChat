package co.uk.travelChat.service;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Trip;
import co.uk.travelChat.repository.LocationCrudRepository;
import co.uk.travelChat.repository.TripCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TripService {

    private final TripCrudRepository tripCrudRepository;
    private final LocationCrudRepository locationCrudRepository;

    public TripService(TripCrudRepository tripCrudRepository, LocationCrudRepository locationCrudRepository) {
        this.tripCrudRepository = tripCrudRepository;
        this.locationCrudRepository = locationCrudRepository;
    }

    public Flux<Trip> getAllTrips() {
        return tripCrudRepository.findAll();
    }

    public Mono<Void> deleteAllTrips() {
        return tripCrudRepository.deleteAll();
    }

    public Mono<Trip> createTrip(Trip trip) {
        return tripCrudRepository.save(trip);
    }

    public Mono<Trip> getTripById(String id) {
        return tripCrudRepository.findById(id);
    }

    public Mono<Void> deleteTripById(String id) {
        return tripCrudRepository.deleteById(id);
    }

//    public Flux<Trip> sameTimePlaceAndMode(Trip trip) {
//        return tripCrudRepository.findAllByLeavingAndDepartingAndMode(
//                trip.getLeaving(), trip.getDeparting(), trip.getMode());
//    }

    public Flux<Trip> sameTimePlaceAndMode(
            long time, String place, ModeOfTransport mode) {
        return tripCrudRepository.findAllByLeaving_timeAndDepartingNameAndMode(
                time, place, mode);
    }

    public Flux<Trip> findByDeparting_Name(String name) {
        return tripCrudRepository.findByDeparting_Name(name);
    }

    public Flux<Trip> findByDestination_Name(String name) {
        return tripCrudRepository.findByDestination_Name(name);
    }

    public Flux<Trip> findByDeparting_NameAndDestination_Name(String departing, String destination) {
        return tripCrudRepository.findByDeparting_NameAndDestination_Name(departing, destination);
    }

    public Flux<Trip> findByDeparting_NameAndLeaving(String departing, long leaving) {
        return tripCrudRepository.findByDeparting_NameAndLeaving_time(departing, leaving);
    }

    public Flux<Trip> findByDeparting_NameAndLeavingAndDestination_NameAndArriving(
            String departing, long leaving, String destination, long arriving) {
        return tripCrudRepository.
                findByDeparting_NameAndLeaving_timeAndDestination_NameAndArriving_time(departing, leaving, destination, arriving);
    }

    public Flux<Trip> findByDestination_NameAndArriving(String destination, long arrivingTime) {
        return tripCrudRepository.findByDestination_NameAndArriving_time(destination, arrivingTime);
    }
}

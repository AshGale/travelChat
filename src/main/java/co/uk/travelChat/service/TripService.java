package co.uk.travelChat.service;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import co.uk.travelChat.repository.TripCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TripService {

    private final TripCrudRepository tripCrudRepository;

    public TripService(TripCrudRepository tripCrudRepository) {
        this.tripCrudRepository = tripCrudRepository;
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
            LocalDateTime time, String place, ModeOfTransport mode) {
        return tripCrudRepository.findAllByLeavingAndDepartingNameAndMode(
                time, place, mode);
    }

    public Flux<Trip> getAllTripsByLocationName(String locationName) {
        return tripCrudRepository.findAllByDeparting(new Location(locationName));
    }
}

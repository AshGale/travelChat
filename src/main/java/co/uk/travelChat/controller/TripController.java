package co.uk.travelChat.controller;

import co.uk.travelChat.model.Trip;
import co.uk.travelChat.service.TripService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(path = "/trip", produces = MediaType.APPLICATION_JSON_VALUE)
public class TripController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public Flux<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @DeleteMapping
    public Mono<Void> deleteAllTrips() {
        return tripService.deleteAllTrips();
    }

    @PostMapping
    public Mono<Trip> createTrip(@RequestBody Trip trip) {
        //todo add in check to see if trip exists already, if so return that trip
        //TODO check who is in the attending, then alert those people about the trip
        return tripService.createTrip(trip);
    }

    @GetMapping("/{id}")
    public Mono<Trip> getTripById(@PathVariable String id) {
        return tripService.getTripById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTrip(@PathVariable String id) {
        //todo check if used anywhere, and then reject if so
        return tripService.deleteTripById(id);
    }

    @GetMapping("/departing/{departing}")
    public Flux<Trip> findTripsByDeparting(@PathVariable("departing") String departing) {
        return tripService.findByDeparting_Name(departing);
    }

    @GetMapping("/departing/{departing}/leaving/{leaving}")
    public Flux<Trip> findTripLeavingSamePlaceAndTime(@PathVariable("departing") String departing,
                                                      @PathVariable("leaving") String leaving) {
        LocalDateTime leavingTime = LocalDateTime.parse(leaving, formatter);
        return tripService.findByDeparting_NameAndLeaving(departing, leavingTime);
    }

    @GetMapping("/departing/{departing}/destination/{destination}")
    public Flux<Trip> findTripsByDestination(@PathVariable("departing") String departing,
                                             @PathVariable("destination") String destination) {
        return tripService.findByDeparting_NameAndDestination_Name(departing, destination);
    }

    @GetMapping("/destination/{destination}")
    public Flux<Trip> findTripsByDestination(@PathVariable("destination") String destination) {
        return tripService.findByDestination_Name(destination);
    }

    @GetMapping("/destination/{destination}/arriving/{arriving}")
    public Flux<Trip> findTripsByDestinationTime(@PathVariable("destination") String destination,
                                                 @PathVariable("arriving") String arriving) {
        LocalDateTime arrivingTime = LocalDateTime.parse(arriving, formatter);
        return tripService.findByDestination_NameAndArriving(destination, arrivingTime);
    }

    @GetMapping("/departing/{departing}/leaving/{leaving}/destination/{destination}/arriving/{arriving}")
    public Flux<Trip> findByDepartingDestinationTime(@PathVariable("departing") String departing,
                                                     @PathVariable("leaving") String leaving,
                                                     @PathVariable("destination") String destination,
                                                     @PathVariable("arriving") String arriving) {
        LocalDateTime leavingTime = LocalDateTime.parse(leaving, formatter);
        LocalDateTime arrivingTime = LocalDateTime.parse(arriving, formatter);
        return tripService.findByDeparting_NameAndLeavingAndDestination_NameAndArriving(
                departing, leavingTime, destination, arrivingTime);
    }

}

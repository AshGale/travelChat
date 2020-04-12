package co.uk.travelChat.controller;

import co.uk.travelChat.model.Trip;
import co.uk.travelChat.service.TripService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(path = "/trip", produces = MediaType.APPLICATION_JSON_VALUE)
public class TripController {


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
        System.out.println(trip);
        Trip returned = tripService.createTrip(trip).block();
        System.out.println(returned);
        return Mono.just(returned);
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
        System.out.println("find departing equal too: " + departing);
        return tripService.findByDeparting_Name(departing);
    }

    @GetMapping("/departing/{departing}/leaving/{leaving}")
    public Flux<Trip> findTripLeavingSamePlaceAndTime(@PathVariable("departing") String departing,
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                      @PathVariable("leaving") long leaving) {
        System.out.println(leaving);
        return tripService.findByDeparting_NameAndLeaving(departing, leaving);
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
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                 @PathVariable("arriving") long arriving) {
        return tripService.findByDestination_NameAndArriving(destination, arriving);
    }

    @GetMapping("/departing/{departing}/leaving/{leaving}/destination/{destination}/arriving/{arriving}")
    public Flux<Trip> findByDepartingDestinationTime(@PathVariable("departing") String departing,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @PathVariable("leaving") long leaving,
                                                     @PathVariable("destination") String destination,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                     @PathVariable("arriving") long arriving) {
        return tripService.findByDeparting_NameAndLeavingAndDestination_NameAndArriving(
                departing, leaving, destination, arriving);
    }

}

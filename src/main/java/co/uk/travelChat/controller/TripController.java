package co.uk.travelChat.controller;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import co.uk.travelChat.service.TripService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/trip", produces = MediaType.APPLICATION_JSON_VALUE)
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // * testing endpoints
    //get all trips
    //delete all trips

    @GetMapping
    public Flux<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @DeleteMapping
    public Mono<Void> deleteAllTrips() {
        return tripService.deleteAllTrips();
    }

    // * endpoints with use
    //create trip
    //update trip for group Venture// not implemented
    //get trip by id
    //delete trip by id
    //get all trips where leaving, departing and mode
    //get all trips where leaving, arriving, departing, destination, mode =
    //get all trips where departing and mode

    @PostMapping
    public Mono<Trip> createTrip(@RequestBody Trip trip) {
        return tripService.createTrip(trip);
    }

    @GetMapping("/{id}")
    public Mono<Trip> getTripById(@PathVariable String id) {
        return tripService.getTripById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTrip(@PathVariable String id) {
        return tripService.deleteTripById(id);
    }

//    @GetMapping("/sameTimePlaceAndMode")
//    public Flux<Trip> getAllWhereSameTimePlaceAndMode (@RequestBody Trip trip) {
//        return tripService.sameTimePlaceAndMode(trip);
//    }

    @GetMapping("sameTimePlaceAndMode")// test
    public Flux<Trip> getAllWhereSameTimePlaceAndMode(
            @PathParam("time") LocalDateTime time,
            @PathParam("place") Location place,
            @PathParam("mode") ModeOfTransport mode) {
        return tripService.sameTimePlaceAndMode(time, place, mode);
    }
}

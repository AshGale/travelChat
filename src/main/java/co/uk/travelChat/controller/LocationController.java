package co.uk.travelChat.controller;

import co.uk.travelChat.model.Location;
import co.uk.travelChat.service.LocationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/getAllLocationsByName")
    public Flux<Location> getAllLocationsByName(@PathParam("name") String name) {
        return locationService.getAllLocationsByName(name);
    }

    @GetMapping("/findFirstByName")
    public Mono<Location> findFirstByName(@PathParam("name") String name) {
        return locationService.findFirstByName(name);
    }


    @GetMapping("/getAllbyLongitudeAndLatitude")
    public Flux<Location> getAllbyLongitudeAndLatitude(
            @PathParam("longitude") double longitude,
            @PathParam("latitude") double latitude) {
        return locationService.getAllbyLongitudeAndLatitude(longitude, latitude);
    }

    @GetMapping("/getAllByLocation")
    public Flux<Location> getAllbyLocation(@PathParam("name") String name,
                                           @PathParam("longitude") double longitude,
                                           @PathParam("latitude") double latitude) {
        return locationService.getAllByLocation(name, longitude, latitude);
    }

    @PostMapping
    public Mono<Location> saveLocation(@RequestBody Location location) {
        return locationService.saveLocation(location);
    }
}

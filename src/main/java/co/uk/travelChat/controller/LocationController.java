package co.uk.travelChat.controller;

import co.uk.travelChat.model.Location;
import co.uk.travelChat.service.LocationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public Flux<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping
    public Mono<Location> saveLocation(@RequestBody Location location) {
        return locationService.saveLocation(location);
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteLocaionById(@PathVariable String id) {
        //TODO should really be check to see if used, and will requect otherwise
        return locationService.deleteLocationById(id);
    }

    @GetMapping("{id}")
    public Mono<Location> getLocationById(@PathVariable String id) {
        return locationService.getLocationById(id);
    }

    @GetMapping("/name/{name}")
    public Flux<Location> getAllLocationsByName(@PathVariable("name") String name) {
        return locationService.getAllLocationsByName(name);
    }

    @GetMapping("/name/{name}/first")
    public Mono<Location> findFirstByName(@PathVariable("name") String name) {
        return locationService.findFirstLocationByName(name);
    }


    @GetMapping("/longitude/{longitude}/latitude/{latitude}")
    public Flux<Location> getAllbyLongitudeAndLatitude(
            @PathVariable("longitude") double longitude,
            @PathVariable("latitude") double latitude) {
        return locationService.getAllbyLongitudeAndLatitude(longitude, latitude);
    }

    @GetMapping("/longitude/{longitude}/latitude/{latitude}/name/{name}")
    public Flux<Location> getAllbyLocation(@PathVariable("name") String name,
                                           @PathVariable("longitude") double longitude,
                                           @PathVariable("latitude") double latitude) {
        return locationService.getAllByLocation(name, longitude, latitude);
    }
}

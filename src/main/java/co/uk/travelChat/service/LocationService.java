package co.uk.travelChat.service;

import co.uk.travelChat.model.Location;
import co.uk.travelChat.repository.LocationCrudRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LocationService {

    private final LocationCrudRepository locationCrudRepository;

    public LocationService(LocationCrudRepository locationCrudRepository) {
        this.locationCrudRepository = locationCrudRepository;
    }

    public Flux<Location> getAllLocationsByName(String name) {
        return locationCrudRepository.findLocationsByName(name);
    }

    public Flux<Location> getAllByLocation(String name, double longitude, double latitude) {
        return locationCrudRepository.findLocationsByNameAndLongitudeAndLatitude(name, longitude, latitude);
    }

    public Flux<Location> getAllbyLongitudeAndLatitude(double longitude, double latitude) {
        return locationCrudRepository.findLocationsByLongitudeAndLatitude(longitude, latitude);
    }

    public Mono<Location> findFirstByName(String name) {
        return locationCrudRepository.findFirstByName(name);
    }
}

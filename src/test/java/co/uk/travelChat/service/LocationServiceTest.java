package co.uk.travelChat.service;

import co.uk.travelChat.model.Location;
import co.uk.travelChat.repository.LocationCrudRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

@DataMongoTest(includeFilters = @ComponentScan.Filter(Service.class))
@RunWith(SpringRunner.class)
public class LocationServiceTest {

    private static final String DEFAULT_ID = "5d46b4c5966049317459ea70";
    private static final String DEFAULT_NAME = "London Victoria";
    private static final Double DEFAULT_LONGITUDE = 51.495213;
    private static final Double DEFAULT_LATITUDE = -0.143897;

    @Autowired
    LocationCrudRepository locationCrudRepository;

    @Autowired
    LocationService locationService;

    @Before
    public void setUp() throws Exception {
        locationCrudRepository.deleteAll().subscribe();
        locationCrudRepository.save(new Location(DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE)).subscribe();
        Thread.sleep(100);//this is to ensure the data is loaded correctl
    }

//    @Test
//    public void getAllLocations() {
//
//        Flux<Location> result = locationService.getAllLocations(pageSize, ofset);
//
//        StepVerifier.create(result)
//                .assertNext(location -> {
//                    assertEquals(DEFAULT_NAME, location.getName());
//                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
//                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
//                })
//                .expectComplete()
//                .verify();
//    }

    @Test
    public void saveLocation() {
        Mono<Location> result = locationService.saveLocation(new Location(
                "savedLocation", 0.1, -0.2));

        StepVerifier.create(result)
                .assertNext(location -> {
                    assertEquals("savedLocation", location.getName());
                    assertEquals(0.1, location.getLongitude(), 0);
                    assertEquals(-0.2, location.getLatitude(), 0);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteLocationById() {

        Mono<Location> location = locationService.saveLocation(new Location(
                "delete", 0.1, -0.2));

        Mono<Void> nothing = locationService.deleteLocationById("delete");

        Mono<Location> result = locationCrudRepository.findById("delete");

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    public void getLocationById() {
        Mono<Location> result = locationService.getLocationById(DEFAULT_ID);

        StepVerifier.create(result)
                .assertNext(location -> {
                    assertEquals(DEFAULT_NAME, location.getName());
                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
                })
                .expectComplete()
                .verify();
    }

//    @Test
//    public void getAllLocationsByName() {
//        Flux<Location> result = locationService.getAllLocationsByName(DEFAULT_NAME);
//
//        StepVerifier.create(result)
//                .assertNext(location -> {
//                    assertEquals(DEFAULT_NAME, location.getName());
//                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
//                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
//                })
//                .expectComplete()
//                .verify();
//    }

//    @Test
//    public void getAllByLocation() {
//        Flux<Location> result = locationService.getAllByLocation(DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE);
//
//        StepVerifier.create(result)
//                .assertNext(location -> {
//                    assertEquals(DEFAULT_NAME, location.getName());
//                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
//                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
//                })
//                .expectComplete()
//                .verify();
//    }

    @Test
    public void getAllbyLongitudeAndLatitude() {
        Flux<Location> result = locationService.getAllbyLongitudeAndLatitude(DEFAULT_LONGITUDE, DEFAULT_LATITUDE);

        StepVerifier.create(result)
                .assertNext(location -> {
                    assertEquals(DEFAULT_NAME, location.getName());
                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
                })
                .expectComplete()
                .verify();
    }

//    @Test
//    public void findFirstLocationByName() {
//        Mono<Location> result = locationService.findFirstLocationByName(DEFAULT_NAME);
//
//        StepVerifier.create(result)
//                .assertNext(location -> {
//                    assertEquals(DEFAULT_NAME, location.getName());
//                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
//                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
//                })
//                .expectComplete()
//                .verify();
//    }
}
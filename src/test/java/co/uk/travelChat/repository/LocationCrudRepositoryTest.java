package co.uk.travelChat.repository;

import co.uk.travelChat.model.Location;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

@DataMongoTest
@RunWith(SpringRunner.class)
public class LocationCrudRepositoryTest {

    private static final String DEFAULT_ID = "5d46b4c5966049317459ea70";
    private static final String DEFAULT_NAME = "London Victoria";
    private static final Double DEFAULT_LONGITUDE = 51.495213;
    private static final Double DEFAULT_LATITUDE = -0.143897;

    @Autowired
    LocationCrudRepository locationCrudRepository;

    @Before
    public void setUp() throws Exception {
        locationCrudRepository.deleteAll().subscribe();
        locationCrudRepository.save(new Location(
                DEFAULT_ID, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE)).subscribe();
        Thread.sleep(100);//this is to ensure the data is loaded correctl
    }

    @Test
    public void findFirst1ByName() {
        Mono<Location> locationMono = locationCrudRepository.findFirst1ByName(DEFAULT_NAME);

        StepVerifier.create(locationMono)
                .assertNext(location -> {
                    assertEquals(DEFAULT_NAME, location.getName());
                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findLocationsByNameAndLongitudeAndLatitude() {
        Flux<Location> locationMono = locationCrudRepository
                .findLocationsByNameAndLongitudeAndLatitude(DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE);

        StepVerifier.create(locationMono)
                .assertNext(location -> {
                    assertEquals(DEFAULT_NAME, location.getName());
                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findLocationsByLongitudeAndLatitude() {
        Flux<Location> locationMono = locationCrudRepository
                .findLocationsByLongitudeAndLatitude(DEFAULT_LONGITUDE, DEFAULT_LATITUDE);

        StepVerifier.create(locationMono)
                .assertNext(location -> {
                    assertEquals(DEFAULT_NAME, location.getName());
                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void findLocationsByName() {
        Flux<Location> locationMono = locationCrudRepository
                .findLocationsByName(DEFAULT_NAME);

        StepVerifier.create(locationMono)
                .assertNext(location -> {
                    assertEquals(DEFAULT_NAME, location.getName());
                    assertEquals(DEFAULT_LONGITUDE, location.getLongitude(), 0);
                    assertEquals(DEFAULT_LATITUDE, location.getLatitude(), 0);
                })
                .expectComplete()
                .verify();
    }


}
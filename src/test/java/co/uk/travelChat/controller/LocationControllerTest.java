package co.uk.travelChat.controller;

import co.uk.travelChat.model.Location;
import co.uk.travelChat.repository.LocationCrudRepository;
import co.uk.travelChat.service.LocationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@DataMongoTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(Controller.class)})
@RunWith(SpringRunner.class)
public class LocationControllerTest {

    private static final String DEFAULT_ID = "5d46b4c5966049317459ea70";
    private static final String DEFAULT_NAME = "London Victoria";
    private static final Double DEFAULT_LONGITUDE = 51.495213;
    private static final Double DEFAULT_LATITUDE = -0.143897;

    private static final String save_id = "5d46b4c5966049317459ea71";
    private static final String delete_id = "5d46b4c5966049317459ea72";

    @Autowired
    private LocationCrudRepository locationCrudRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationController locationController;

    private WebTestClient webTestClient;

    @Before
    public void setUp() throws Exception {
        this.webTestClient = WebTestClient.bindToController(new LocationController(locationService)).build();
        locationCrudRepository.deleteAll().subscribe();
        locationCrudRepository.save(new Location(
                DEFAULT_ID, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE)).subscribe();
    }

    @Test
    public void getAllLocations() {
        webTestClient.get().uri("/location")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Location.class)
                .hasSize(1)
                .contains(new Location(DEFAULT_ID, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE));
    }

    @Test
    public void saveLocation() {
        webTestClient.post().uri("/location")
                .body(Mono.just(
                        new Location(save_id, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE)), Location.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Location.class)
                .hasSize(1)
                .contains(new Location(save_id, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE));
    }

    @Test
    public void deleteLocaionById() {
        Mono<Location> location = locationService.saveLocation(new Location(
                delete_id, "delete", 0.1, -0.2));

        webTestClient.delete().uri("/location/" + delete_id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void getLocationById() {
        webTestClient.get().uri("/location/" + DEFAULT_ID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Location.class)
                .hasSize(1)
                .contains(new Location(DEFAULT_ID, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE));
    }

    @Test
    public void getAllLocationsByName() {
        webTestClient.get().uri("/location/name/" + DEFAULT_NAME)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Location.class)
                .hasSize(1)
                .contains(new Location(DEFAULT_ID, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE));
    }

    @Test
    public void findFirstByName() {
        webTestClient.get().uri("/location/name/" + DEFAULT_NAME + "/first")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Location.class)
                .hasSize(1)
                .contains(new Location(DEFAULT_ID, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE));
    }

    @Test
    public void getAllbyLongitudeAndLatitude() {
        webTestClient.get().uri("/location/longitude/" + DEFAULT_LONGITUDE + "/latitude/" + DEFAULT_LATITUDE)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Location.class)
                .hasSize(1)
                .contains(new Location(DEFAULT_ID, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE));
    }

    @Test
    public void getAllbyLocation() {
        webTestClient.get().uri("/location/longitude/"
                + DEFAULT_LONGITUDE + "/latitude/"
                + DEFAULT_LATITUDE + "/name/"
                + DEFAULT_NAME)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Location.class)
                .hasSize(1)
                .contains(new Location(DEFAULT_ID, DEFAULT_NAME, DEFAULT_LONGITUDE, DEFAULT_LATITUDE));
    }
}
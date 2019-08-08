package co.uk.travelChat.controller;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import co.uk.travelChat.repository.TripCrudRepository;
import co.uk.travelChat.service.TripService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@DataMongoTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(Controller.class)})
@RunWith(SpringRunner.class)
public class TripControllerTest {


    private static final String DEFAULT_TIME = "2019-01-01 00:00";
    private static final LocalDateTime DEFAULT_LEAVING =
            LocalDateTime.parse(DEFAULT_TIME, Trip.getFormatter());
    private static final LocalDateTime DEFAULT_ARRIVING =
            LocalDateTime.parse(DEFAULT_TIME, Trip.getFormatter()).plusHours(1);

    private static final String DEFAULT_ID = "5d46b4c5966049317459ea50";
    private static final String SAVE_ID = "5d46b4c5966049317459ea51";
    private static final String DELETE_ID = "5d46b4c5966049317459ea52";

    private static final String DEPARTING_ID = "5d46b4c5966049317459ea70";
    private static final String DEPARTING_NAME = "London Victoria";
    private static final Double DEPARTING_LONGITUDE = 51.495213;
    private static final Double DEPARTING_LATITUDE = -0.143897;
    private static final Location DEFAULT_DEPARTING = new Location(
            DEPARTING_ID, DEPARTING_NAME, DEPARTING_LONGITUDE, DEPARTING_LATITUDE);

    private static final String DESTINATION_ID = "5d46b4c5966049317459ea71";
    private static final String DESTINATION_NAME = "London Bridge Station";
    private static final Double DESTINATION_LONGITUDE = 51.504527;
    private static final Double DESTINATION_LATITUDE = -0.086392;
    private static final Location DEFAULT_DESTINATION = new Location(
            DESTINATION_ID, DESTINATION_NAME, DESTINATION_LONGITUDE, DESTINATION_LATITUDE);

    private static final ModeOfTransport DEFAULT_MODE_OF_TRANSPORT = ModeOfTransport.Train;

    private static final String DEFAULT_NICKNAME = "Joe";
    private List<String> DEFAULT_ATTENDING = Arrays.asList(DEFAULT_NICKNAME);

    private static final boolean DEFAULT_DISCOVERALBE = true;


    @Autowired
    private TripCrudRepository tripCrudRepository;

    @Autowired
    private TripService tripService;

    private WebTestClient webTestClient;

    @Before
    public void init() throws Exception {
        this.webTestClient = WebTestClient.bindToController(new TripController(tripService)).build();
        tripCrudRepository.deleteAll().subscribe();
        tripCrudRepository.save(
                new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING))
                .subscribe();
        Thread.sleep(100);//this is to ensure the data is loaded correct
    }

    @Test
    public void getAllTrips() {
        webTestClient.get().uri("/trip")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Trip.class)
                .hasSize(1)
                .contains(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));
    }

    @Test
    public void createTrip() {
        webTestClient.post().uri("/trip")
                .body(Mono.just(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                                DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING)),
                        Trip.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Trip.class)
                .hasSize(1)
                .contains(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));
    }

    @Test
    public void getTripById() {
        webTestClient.get().uri("/trip/" + DEFAULT_ID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Trip.class)
                .hasSize(1)
                .contains(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));
    }

    @Test
    public void deleteTrip() {
        tripCrudRepository.save(new Trip(DELETE_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));

        webTestClient.delete().uri("/trip/" + DELETE_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Void.class);
    }

    @Test
    public void findTripsByDeparting() {
        webTestClient.get().uri("/trip/departing/" + DEFAULT_DEPARTING.getName())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Trip.class)
                .hasSize(1)
                .contains(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));
    }

    @Test
    public void findTripLeavingSamePlaceAndTime() {
        webTestClient.get().uri(
                "/trip/departing/" + DEFAULT_DEPARTING.getName() + "/leaving/" + Trip.formatToSting(DEFAULT_LEAVING))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Trip.class)
                .hasSize(1)
                .contains(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));
    }

    @Test
    public void findTripsByDestination() {
        webTestClient.get().uri(
                "/trip/destination/" + DEFAULT_DESTINATION.getName())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Trip.class)
                .hasSize(1)
                .contains(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));
    }

    @Test
    public void findTripsByDestinationTime() {
        webTestClient.get().uri(
                "/trip/destination/" + DEFAULT_DESTINATION.getName() + "/arriving/" + Trip.formatToSting(DEFAULT_ARRIVING))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Trip.class)
                .hasSize(1)
                .contains(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));
    }

    @Test
    public void findByDepartingDestinationTime() {
        webTestClient.get().uri(
                "/trip/departing/" + DEFAULT_DEPARTING.getName() + "/leaving/" + Trip.formatToSting(DEFAULT_LEAVING) +
                        "/destination/" + DEFAULT_DESTINATION.getName() + "/arriving/" + Trip.formatToSting(DEFAULT_ARRIVING))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Trip.class)
                .hasSize(1)
                .contains(new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));
    }
}
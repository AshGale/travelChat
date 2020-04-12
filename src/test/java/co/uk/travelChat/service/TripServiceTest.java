package co.uk.travelChat.service;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import co.uk.travelChat.repository.TripCrudRepository;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@DataMongoTest(includeFilters = @ComponentScan.Filter(Service.class))
@RunWith(SpringRunner.class)
public class TripServiceTest {

    private String pattern = "EEE MMM dd HH:mm z yyyy";
    private String end_time = "Sat Mar 28 21:00 GMT 2020";
    private String start_time = "Sat Mar 28 12:00 GMT 2020";
    private SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);

    private static Calendar DEFAULT_LEAVING;
    private static Calendar DEFAULT_ARRIVING;

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

    @Before
    public void init() throws Exception {

        DEFAULT_LEAVING = Calendar.getInstance();
        DEFAULT_ARRIVING = Calendar.getInstance();

        DEFAULT_LEAVING.setTime(sdf.parse(start_time));
        DEFAULT_ARRIVING.setTime(sdf.parse(end_time));

        tripCrudRepository.deleteAll().subscribe();
        tripCrudRepository.save(
                new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING))
                .subscribe();
        Thread.sleep(100);//this is to ensure the data is loaded correct
    }

    @Test
    public void getAllTrips() {
        Flux<Trip> result = tripService.getAllTrips();

        StepVerifier.create(result)
                .assertNext(trip -> {
                    assertEquals(DEFAULT_ID, trip.getId());
                    assertEquals(DEFAULT_LEAVING, trip.getLeaving());
                    assertEquals(DEFAULT_ARRIVING, trip.getArriving());
                    assertEquals(DEFAULT_DEPARTING, trip.getDeparting());
                    assertEquals(DEFAULT_DESTINATION, trip.getDestination());
                    assertEquals(DEFAULT_MODE_OF_TRANSPORT, trip.getMode());
                    assertEquals(DEFAULT_DISCOVERALBE, trip.getDiscoverable());
                    assertEquals(DEFAULT_ATTENDING, trip.getAttending());
                })
                .expectComplete()
                .verify();
    }

//    @Test
//    public void deleteAllTrips() {
//        tripCrudRepository.save( new Trip(DELETE_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
//                DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING)).subscribe();
//
//        Mono<Void> result = tripService.deleteAllTrips();
//
//        StepVerifier.create(result)
//                .expectComplete()
//                .verify();
//    }

    @Test
    public void createTrip() {
        Mono<Trip> result = tripService.createTrip(
                new Trip(SAVE_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING, DEFAULT_DESTINATION,
                        DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING));

        StepVerifier.create(result)
                .assertNext(trip -> {
                    assertEquals(SAVE_ID, trip.getId());
                    assertEquals(DEFAULT_LEAVING, trip.getLeaving());
                    assertEquals(DEFAULT_ARRIVING, trip.getArriving());
                    assertEquals(DEFAULT_DEPARTING, trip.getDeparting());
                    assertEquals(DEFAULT_DESTINATION, trip.getDestination());
                    assertEquals(DEFAULT_MODE_OF_TRANSPORT, trip.getMode());
                    assertEquals(DEFAULT_DISCOVERALBE, trip.getDiscoverable());
                    assertEquals(DEFAULT_ATTENDING, trip.getAttending());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getTripById() {
        Mono<Trip> result = tripService.getTripById(DEFAULT_ID);

        StepVerifier.create(result)
                .assertNext(trip -> {
                    assertEquals(DEFAULT_ID, trip.getId());
                    assertEquals(DEFAULT_LEAVING, trip.getLeaving());
                    assertEquals(DEFAULT_ARRIVING, trip.getArriving());
                    assertEquals(DEFAULT_DEPARTING, trip.getDeparting());
                    assertEquals(DEFAULT_DESTINATION, trip.getDestination());
                    assertEquals(DEFAULT_MODE_OF_TRANSPORT, trip.getMode());
                    assertEquals(DEFAULT_DISCOVERALBE, trip.getDiscoverable());
                    assertEquals(DEFAULT_ATTENDING, trip.getAttending());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteTripById() {
        tripCrudRepository.save(new Trip(DELETE_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING)).subscribe();

        Mono<Void> result = tripService.deleteTripById(DELETE_ID);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    public void sameTimePlaceAndMode() {
        Flux<Trip> result = tripService.sameTimePlaceAndMode(DEFAULT_LEAVING.getTimeInMillis(), DEFAULT_DEPARTING.getName(), DEFAULT_MODE_OF_TRANSPORT);

        StepVerifier.create(result)
                .assertNext(trip -> {
                    assertEquals(DEFAULT_ID, trip.getId());
                    assertEquals(DEFAULT_LEAVING, trip.getLeaving());
                    assertEquals(DEFAULT_ARRIVING, trip.getArriving());
                    assertEquals(DEFAULT_DEPARTING, trip.getDeparting());
                    assertEquals(DEFAULT_DESTINATION, trip.getDestination());
                    assertEquals(DEFAULT_MODE_OF_TRANSPORT, trip.getMode());
                    assertEquals(DEFAULT_DISCOVERALBE, trip.getDiscoverable());
                    assertEquals(DEFAULT_ATTENDING, trip.getAttending());
                })
                .expectComplete()
                .verify();

    }
}
package co.uk.travelChat.repository;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataMongoTest
@RunWith(SpringRunner.class)
public class TripCrudRepositoryTest {

    private static final LocalDateTime DEFAULT_LEAVING = LocalDateTime.now();
    private static final LocalDateTime DEFAULT_ARRIVING = DEFAULT_LEAVING.plusHours(1);

    private static final String DEFAULT_ID = "5d46b4c5966049317459ea50";

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

    @Before
    public void setUp() throws Exception {
        tripCrudRepository.deleteAll().subscribe();
        tripCrudRepository.save(
                new Trip(DEFAULT_ID, DEFAULT_LEAVING, DEFAULT_ARRIVING, DEFAULT_DEPARTING,
                        DEFAULT_DESTINATION, DEFAULT_MODE_OF_TRANSPORT, DEFAULT_DISCOVERALBE, DEFAULT_ATTENDING))
                .subscribe();
        Thread.sleep(100);//this is to ensure the data is loaded correct
    }

    @Test
    public void findAllByLeavingAndDepartingNameAndMode() {
        Flux<Trip> result = tripCrudRepository.findAllByLeavingAndDepartingNameAndMode(
                DEFAULT_LEAVING, DEFAULT_DEPARTING.getName(), DEFAULT_MODE_OF_TRANSPORT
        );

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
    public void findByDeparting_Name() {
        Flux<Trip> result = tripCrudRepository.findByDeparting_Name(DEFAULT_DEPARTING.getName());

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
    public void findByDestination_Name() {
        Flux<Trip> result = tripCrudRepository.findByDestination_Name(DEFAULT_DESTINATION.getName());

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
    public void findByDeparting_NameAndDestination_Name() {
        Flux<Trip> result = tripCrudRepository.findByDeparting_NameAndDestination_Name(
                DEFAULT_DEPARTING.getName(), DEFAULT_DESTINATION.getName());

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
    public void findByDeparting_NameAndLeaving() {
        Flux<Trip> result = tripCrudRepository.findByDeparting_NameAndLeaving(
                DEFAULT_DEPARTING.getName(), DEFAULT_LEAVING);

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
    public void findByDestination_NameAndArriving() {
        Flux<Trip> result = tripCrudRepository.findByDestination_NameAndArriving(
                DEFAULT_DESTINATION.getName(), DEFAULT_ARRIVING);

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
    public void findByDeparting_NameAndLeavingAndDestination_NameAndArriving() {
        Flux<Trip> result = tripCrudRepository.findByDeparting_NameAndLeavingAndDestination_NameAndArriving(
                DEFAULT_DEPARTING.getName(), DEFAULT_LEAVING, DEFAULT_DESTINATION.getName(), DEFAULT_ARRIVING);

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
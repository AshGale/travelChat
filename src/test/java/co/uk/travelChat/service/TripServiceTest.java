package co.uk.travelChat.service;

import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TripServiceTest {

    private Flux<Trip> tripsFlux;

    private Map<Integer, Location> locationMap = new LinkedHashMap<>();

    private List<Trip> tripMap = new ArrayList<>();

    private LocalDateTime localDateTime = LocalDateTime.now();

    private final String defaultId = "tripID";

    @Mock
    private TripService tripService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        locationMap.put(1, new Location(null, "London Victoria", 51.495213, -0.143897));
        locationMap.put(2, new Location(null, "London Bridge Station", 51.504527, -0.086392));
        locationMap.put(3, new Location(null, "Brighton Railway Station", 50.829467, -0.140960));


        //london victoria too Brighton
        tripMap.add(new Trip(null, localDateTime, localDateTime.plusHours(1), locationMap.get(1), locationMap.get(3), ModeOfTransport.Train, true, new ArrayList<>()));
        //london Victoria too London Bridge
        tripMap.add(new Trip(null, localDateTime, localDateTime.plusHours(1), locationMap.get(1), locationMap.get(2), ModeOfTransport.Train, true, new ArrayList<>()));
        //London Bridge to dover
        tripMap.add(new Trip(null, localDateTime, localDateTime.plusHours(1), locationMap.get(2), locationMap.get(3), ModeOfTransport.Train, true, new ArrayList<>()));

        tripsFlux = Flux.fromIterable(tripMap);
    }

    @Test
    public void getAllTrips() {
        Mockito.when(tripService.getAllTrips()).thenReturn(tripsFlux);

        Flux<Trip> dbTrips = tripService.getAllTrips();

        StepVerifier.create(dbTrips)
                .assertNext(firstTrip -> {
                    assertEquals("London Victoria", firstTrip.getDeparting().getName());
                    assertEquals(localDateTime, firstTrip.getLeaving());
                    assertEquals("Brighton Railway Station", firstTrip.getDestination().getName());
                    assertEquals(localDateTime.plusHours(1), firstTrip.getArriving());
                })
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteAllTrips() {
        Mockito.when(tripService.deleteAllTrips()).thenReturn(Mono.empty());

        Mono<Void> dbTrilps = tripService.deleteAllTrips();

        StepVerifier.create(dbTrilps)
                .verifyComplete();
    }

    @Test
    public void createTrip() {
        Trip trip = tripMap.get(0);

        Trip dbTrip = trip;
        dbTrip.setId(defaultId);

        Mockito.when(tripService.createTrip(trip))
                .thenReturn(Mono.just(dbTrip));

        Mono<Trip> returnedTrip = tripService.createTrip(trip);

        StepVerifier.create(returnedTrip)
                .assertNext(monoTrip -> {
                    assertEquals(defaultId, monoTrip.getId());
                    assertEquals(trip.getArriving(), monoTrip.getArriving());
                    assertEquals(trip.getDestination(), monoTrip.getDestination());
                    assertEquals(trip.getDeparting(), monoTrip.getDeparting());
                    assertEquals(trip.getLeaving(), monoTrip.getLeaving());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void getTripById() {
        Trip trip = tripMap.get(0);
        trip.setId(defaultId);

        Mockito.when(tripService.getTripById(defaultId)).thenReturn(Mono.just(trip));

        Mono<Trip> returnedTrip = tripService.getTripById(defaultId);

        StepVerifier.create(returnedTrip)
                .assertNext(monoTrip -> {
                    assertEquals(trip.getId(), monoTrip.getId());
                    assertEquals(trip.getArriving(), monoTrip.getArriving());
                    assertEquals(trip.getDestination(), monoTrip.getDestination());
                    assertEquals(trip.getDeparting(), monoTrip.getDeparting());
                    assertEquals(trip.getLeaving(), monoTrip.getLeaving());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void deleteTripById() {
        Mockito.when(tripService.deleteTripById(defaultId)).thenReturn(Mono.empty());

        Mono<Void> returnedTrip = tripService.deleteTripById(defaultId);

        StepVerifier.create(returnedTrip)
                .verifyComplete();
    }

    @Test
    public void sameTimePlaceAndMode() {
        Trip trip = tripMap.get(0);
        Flux matchingTrips = tripsFlux.take(2);

        LocalDateTime leaving = trip.getLeaving();
        String departing = trip.getDeparting().getName();
        ModeOfTransport mode = trip.getMode();

        Mockito.when(tripService.sameTimePlaceAndMode(leaving, departing, mode))
                .thenReturn(matchingTrips);

        Flux<Trip> returnedTrip = tripService.sameTimePlaceAndMode(
                leaving, departing, mode);

        StepVerifier.create(returnedTrip)
                .assertNext(monoTrip -> {
                    assertEquals(leaving, monoTrip.getLeaving());
                    assertEquals(departing, monoTrip.getDeparting());
                    assertEquals(mode, monoTrip.getMode());
                })
                .assertNext(monoTrip -> {
                    assertEquals(leaving, monoTrip.getLeaving());
                    assertEquals(departing, monoTrip.getDeparting());
                    assertEquals(mode, monoTrip.getMode());
                })
                .expectComplete()
                .verify();

    }
}
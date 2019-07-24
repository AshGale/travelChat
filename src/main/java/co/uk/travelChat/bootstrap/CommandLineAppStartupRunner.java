package co.uk.travelChat.bootstrap;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.model.Enums.ModeOfTransport;
import co.uk.travelChat.model.Location;
import co.uk.travelChat.model.Trip;
import co.uk.travelChat.repository.AccountCrudRepository;
import co.uk.travelChat.repository.LocationCrudRepository;
import co.uk.travelChat.repository.TripCrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Profile("init")
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);
    private final AccountCrudRepository accountCrudRepository;
    private final LocationCrudRepository locationCrudRepository;
    private final TripCrudRepository tripCrudRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LocalDateTime localDateTime;
    private Map<Integer, Location> locationMap;
    private Map<Integer, Trip> tripMap;

    public CommandLineAppStartupRunner(AccountCrudRepository accountCrudRepository, LocationCrudRepository locationCrudRepository, TripCrudRepository tripCrudRepository) {
        this.accountCrudRepository = accountCrudRepository;
        this.locationCrudRepository = locationCrudRepository;
        this.tripCrudRepository = tripCrudRepository;
    }

    @Override
    public void run(String...args) throws Exception {

        String time = "2019-01-01 00:00";
        localDateTime = LocalDateTime.parse(time, formatter);
//        localDateTime.minusSeconds(localDateTime.getSecond());

        loadLocations();
        loadTrips();
        loadAccounts();
    }

    private void loadLocations() {
        logger.info("Adding Locations");

        locationMap = new LinkedHashMap<>();
        locationMap.put(1, new Location(null, "London Victoria", 51.495213, -0.143897));
        locationMap.put(2, new Location(null, "London Bridge Station", 51.504527, -0.086392));
        locationMap.put(3, new Location(null, "Brighton Railway Station", 50.829467, -0.140960));
        locationMap.put(4, new Location(null, "Dover Priory", 51.126234, 1.304786));
        locationMap.put(5, new Location(null, "Ha Long", 20.972683d, 107.046353d));
        locationMap.put(6, new Location(null, "The Great Blue Hole", 17.315786, -87.534782));
        locationMap.put(7, new Location(null, "Desert Breath", 27.380369d, 33.632155d));

        locationCrudRepository.deleteAll().subscribe();

        locationMap.forEach((key, location) -> locationCrudRepository.save(location).subscribe());

        locationCrudRepository.findAll().doOnNext(location -> logger.info(location.toString())).subscribe();
    }

    private void loadTrips() {
        logger.info("Adding Trips");

        tripMap = new LinkedHashMap<>();
        //london victoria too London Bridge Station
        tripMap.put(1, new Trip(null, localDateTime.plusHours(0), localDateTime.plusHours(1), locationMap.get(1), locationMap.get(2), ModeOfTransport.Train, true, new ArrayList<>()));
        //london Victoria too Brighton Railway Station
        tripMap.put(2, new Trip(null, localDateTime.plusHours(0), localDateTime.plusHours(1), locationMap.get(1), locationMap.get(3), ModeOfTransport.Train, true, new ArrayList<>()));
        //London Bridge to Brighton Railway Station
        tripMap.put(3, new Trip(null, localDateTime.plusHours(0), localDateTime.plusHours(1), locationMap.get(2), locationMap.get(3), ModeOfTransport.Train, true, new ArrayList<>()));
        //dover to Brighton
        tripMap.put(4, new Trip(null, localDateTime.plusHours(1), localDateTime.plusHours(2), locationMap.get(4), locationMap.get(3), ModeOfTransport.Train, true, new ArrayList<>()));
        //london victoria too London Bridge Station + 2hr
        tripMap.put(5, new Trip(null, localDateTime.plusHours(2), localDateTime.plusHours(3), locationMap.get(1), locationMap.get(2), ModeOfTransport.Train, true, new ArrayList<>()));
        //London Bridge Station too london victoria
        tripMap.put(6, new Trip(null, localDateTime.plusHours(1), localDateTime.plusHours(2), locationMap.get(2), locationMap.get(1), ModeOfTransport.Train, true, new ArrayList<>()));

        tripCrudRepository.deleteAll().subscribe();

        tripMap.forEach((key, trip) -> tripCrudRepository.save(trip).subscribe());

//        tripCrudRepository.findAll().doOnNext(trip -> logger.info(trip.toString())).subscribe();
    }

    private void loadAccounts() {
        logger.info("Adding Accounts");

        Map<Integer, Trip> adamsTrips = new LinkedHashMap<>();
        adamsTrips.put(1, tripMap.get(1));
        tripMap.get(1).getAttending().add("andy123");

        Map<Integer, Trip> bensTrips = new LinkedHashMap<>();
        bensTrips.put(1, tripMap.get(1));
        bensTrips.put(2, tripMap.get(3));
        tripMap.get(1).getAttending().add("benny");
        tripMap.get(3).getAttending().add("benny");

        Map<Integer, Trip> carolsTrips = new LinkedHashMap<>();
        carolsTrips.put(1, tripMap.get(3));
        tripMap.get(3).getAttending().add("carloMeUp");

        Map<Integer, Trip> deansTrips = new LinkedHashMap<>();
        deansTrips.put(1, tripMap.get(1));
        deansTrips.put(2, tripMap.get(6));
        deansTrips.put(3, tripMap.get(5));
        tripMap.get(1).getAttending().add("theDean");
        tripMap.get(5).getAttending().add("theDean");
        tripMap.get(6).getAttending().add("theDean");

        Map<Integer, Account> accountMap = new LinkedHashMap<>();
        accountMap.put(1, new Account(null, "Adam", "andy123", adamsTrips));
        accountMap.put(2, new Account(null, "Ben", "benny", bensTrips));
        accountMap.put(3, new Account(null, "Carol", "carloMeUp", carolsTrips));
        accountMap.put(4, new Account(null, "Dean", "theDean", deansTrips));

        //Update trips due to updated
        tripMap.forEach((key, trip) -> tripCrudRepository.save(trip).subscribe());
        tripCrudRepository.findAll().doOnNext(trip -> logger.info(trip.toString())).subscribe();

        accountCrudRepository.deleteAll().subscribe();

        accountMap.forEach((key, account) -> accountCrudRepository.save(account).subscribe());

        accountCrudRepository.findAll().doOnNext(account -> logger.info(account.toString())).subscribe();
}

}

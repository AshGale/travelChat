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

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Profile("init")
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);
    private final AccountCrudRepository accountCrudRepository;
    private final LocationCrudRepository locationCrudRepository;
    private final TripCrudRepository tripCrudRepository;
    private Map<Integer, Location> locationMap;
    private Map<Integer, Trip> tripMap;
    private String pattern = "EEE MMM dd HH:mm z yyyy";
    private Long startTime = 1500000000L;
    private Long midTime = 1510000000L;
    private Long returnTime = 1520000000L;
    private Long endTime = 1510000000L;
    private SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);


    public CommandLineAppStartupRunner(AccountCrudRepository accountCrudRepository, LocationCrudRepository locationCrudRepository, TripCrudRepository tripCrudRepository) {
        this.accountCrudRepository = accountCrudRepository;
        this.locationCrudRepository = locationCrudRepository;
        this.tripCrudRepository = tripCrudRepository;
    }

    @Override
    public void run(String... args) throws Exception {

//        accountCrudRepository.deleteAll().subscribe();
//        tripCrudRepository.deleteAll().subscribe();
//        locationCrudRepository.deleteAll().subscribe();

//        TestRepos();

        loadLocations();
        Thread.sleep(100);//this is to ensure the data is loaded correctly
        loadTrips();
        Thread.sleep(100);//this is to ensure the data is loaded correctly// was getting nulls
        loadAccounts();
    }

    private void TestRepos() {
        locationCrudRepository.save(new Location("L1", 1L, 2L)).subscribe();
        tripCrudRepository.save(new Trip("T1", 1L, 2L, "A", "B", ModeOfTransport.Walk)).subscribe();
        accountCrudRepository.save(new Account("who", "Guy", new ArrayList<>())).subscribe();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        locationCrudRepository.findById("L1").subscribe(location -> logger.info("found: " + location.toString()));
    }

    private void loadLocations() {
        logger.info("Adding Locations");

        locationMap = new LinkedHashMap<>();
        locationMap.put(1, new Location("London Victoria", 51.495213, -0.143897));
        locationMap.put(2, new Location("London Bridge Station", 51.504527, -0.086392));
        locationMap.put(3, new Location("Brighton Railway Station", 50.829467, -0.140960));
        locationMap.put(4, new Location("Dover Priory", 51.126234, 1.304786));
        locationMap.put(5, new Location("Ha Long", 20.972683d, 107.046353d));
        locationMap.put(6, new Location("The Great Blue Hole", 17.315786, -87.534782));
        locationMap.put(7, new Location("Desert Breath", 27.380369d, 33.632155d));
        locationMap.put(8, new Location("A", 0.0d, 0.0d));
        locationMap.put(9, new Location("B", 10.0d, 10.0d));

        locationCrudRepository.saveAll(locationMap.values())
                .subscribe(location -> logger.info("Saved location: " + location));
        logger.info("Number of Locations to saved: " + locationMap.values().size());
    }

    private void loadTrips() {
        logger.info("Adding Trips");

        //TODO trip save is saving the location object too. this shouldn't happen
        // - commit progress and branch, then move the strucre to a just id model for locaiton and account, nicname and name

        tripMap = new LinkedHashMap<>();
        //london victoria too London Bridge Station
        tripMap.put(1, new Trip("5d8784fee246f610b84fdfce", startTime, midTime, locationMap.get(1).getName(), locationMap.get(2).getName(), ModeOfTransport.Train, true, new ArrayList<>()));
        //london Victoria too Brighton Railway Station
        tripMap.put(2, new Trip("5d8784fee246f610b84fdfcf", startTime, midTime, locationMap.get(1).getName(), locationMap.get(3).getName(), ModeOfTransport.Train, true, new ArrayList<>()));
        //London Bridge to Brighton Railway Station
        tripMap.put(3, new Trip("5d8784fee246f610b84fdfd0", startTime, midTime, locationMap.get(2).getName(), locationMap.get(3).getName(), ModeOfTransport.Train, true, new ArrayList<>()));
        //dover to Brighton
        tripMap.put(4, new Trip("5d8784fee246f610b84fdfd1", midTime, returnTime, locationMap.get(4).getName(), locationMap.get(3).getName(), ModeOfTransport.Train, true, new ArrayList<>()));
        //london victoria too London Bridge Station + 2hr
        tripMap.put(5, new Trip("5d8784fee246f610b84fdfd2", returnTime, endTime, locationMap.get(1).getName(), locationMap.get(2).getName(), ModeOfTransport.Train, true, new ArrayList<>()));
        //London Bridge Station too london victoria
        tripMap.put(6, new Trip("5d8784fee246f610b84fdfd4", midTime, returnTime, locationMap.get(2).getName(), locationMap.get(1).getName(), ModeOfTransport.Train, true, new ArrayList<>()));

        tripMap.put(6, new Trip("5d8784fee246f610b84fdfd5", startTime, endTime, locationMap.get(8).getName(), locationMap.get(9).getName(), ModeOfTransport.Train, true, new ArrayList<>()));

        tripCrudRepository.saveAll(tripMap.values())
                .subscribe(trip -> logger.info("Saved Trip: " + trip));
        logger.info("Number of Trips to saved: " + tripMap.values().size());
    }

    private void loadAccounts() {
        logger.info("Adding Accounts");

        List<String> adamsTrips = new ArrayList<>();
        adamsTrips.add(tripMap.get(1).getId());
        tripMap.get(1).getAttending().add("andy123");

        List<String> bensTrips = new ArrayList<>();
        bensTrips.add(tripMap.get(1).getId());
        bensTrips.add(tripMap.get(3).getId());
        tripMap.get(1).getAttending().add("benny");
        tripMap.get(3).getAttending().add("benny");

        List<String> carolsTrips = new ArrayList<>();
        carolsTrips.add(tripMap.get(3).getId());
        tripMap.get(3).getAttending().add("carlomeup");

        List<String> deansTrips = new ArrayList<>();
        deansTrips.add(tripMap.get(1).getId());
        deansTrips.add(tripMap.get(5).getId());
        deansTrips.add(tripMap.get(6).getId());
        tripMap.get(1).getAttending().add("thedean");
        tripMap.get(5).getAttending().add("thedean");
        tripMap.get(6).getAttending().add("thedean");

        Map<Integer, Account> accountMap = new LinkedHashMap<>();
        accountMap.put(1, new Account("andy123", "Adam", adamsTrips));
        accountMap.put(2, new Account("benny", "Ben", bensTrips));
        accountMap.put(3, new Account("carlomeup", "Carol", carolsTrips));
        accountMap.put(4, new Account("thedean", "Dean", deansTrips));

        tripCrudRepository.saveAll(tripMap.values())
                .subscribe(trip -> logger.info("Updated Trip: " + trip));

        accountCrudRepository.saveAll(accountMap.values())
                .subscribe(account -> logger.info("Saved Account: " + account));
        logger.info("Number of Accounts to saved: " + accountMap.values().size());
    }
}

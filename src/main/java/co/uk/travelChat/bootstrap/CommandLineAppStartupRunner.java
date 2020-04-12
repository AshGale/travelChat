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
    private Calendar startTime = Calendar.getInstance();
    private Calendar midTime = Calendar.getInstance();
    private Calendar returnTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);


    public CommandLineAppStartupRunner(AccountCrudRepository accountCrudRepository, LocationCrudRepository locationCrudRepository, TripCrudRepository tripCrudRepository) {
        this.accountCrudRepository = accountCrudRepository;
        this.locationCrudRepository = locationCrudRepository;
        this.tripCrudRepository = tripCrudRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        String start_time = "Sat Mar 28 12:00 GMT 2020";
        startTime.setTime(sdf.parse(start_time));
        String mid_time = "Sat Mar 28 16:00 GMT 2020";
        midTime.setTime(sdf.parse(mid_time));
        String return_time = "Sat Mar 28 20:00 GMT 2020";
        returnTime.setTime(sdf.parse(return_time));
        String end_time = "Sat Mar 28 21:00 GMT 2020";
        endTime.setTime(sdf.parse(end_time));

        accountCrudRepository.deleteAll().subscribe();
        tripCrudRepository.deleteAll().subscribe();
        locationCrudRepository.deleteAll().subscribe();

        loadLocations();
        Thread.sleep(100);//this is to ensure the data is loaded correctly
        loadTrips();
//        Thread.sleep(100);//this is to ensure the data is loaded correctly// was getting nulls
//        loadAccounts();
    }

    private void loadLocations() {
        logger.info("Adding Locations");

        locationMap = new LinkedHashMap<>();
        locationMap.put(1, new Location("5e81073def5c0e1d78581d10", "London Victoria", 51.495213, -0.143897));
        locationMap.put(2, new Location("5e81073def5c0e1d78581d22", "London Bridge Station", 51.504527, -0.086392));
        locationMap.put(3, new Location("5e81073def5c0e1d78581d23", "Brighton Railway Station", 50.829467, -0.140960));
        locationMap.put(4, new Location("5e81073def5c0e1d78581d24", "Dover Priory", 51.126234, 1.304786));
        locationMap.put(5, new Location("5e81073def5c0e1d78581d25", "Ha Long", 20.972683d, 107.046353d));
        locationMap.put(6, new Location("5e81073def5c0e1d78581d26", "The Great Blue Hole", 17.315786, -87.534782));
        locationMap.put(7, new Location("5e81073def5c0e1d78581d27", "Desert Breath", 27.380369d, 33.632155d));
        locationMap.put(8, new Location("5e81073def5c0e1d78581d28", "A", 0.0d, 0.0d));
        locationMap.put(9, new Location("5e81073def5c0e1d78581d29", "B", 10.0d, 10.0d));

        locationMap.forEach((key, location) -> locationCrudRepository
                .findLocationsByName(locationMap.get(key).getName())
                .doOnNext(empty -> System.out.println("found: " + locationMap.get(key)))
                .switchIfEmpty(locationCrudRepository.save(locationMap.get(key)))
                .subscribe(savedTrip -> logger.info(savedTrip.toString())));
    }

    private void loadTrips() {
        logger.info("Adding Trips");

        //TODO trip save is saving the location object too. this shouldn't happen
        // - commit progress and branch, then move the strucre to a just id model for locaiton and account, nicname and name

        tripMap = new LinkedHashMap<>();
        //london victoria too London Bridge Station
        tripMap.put(1, new Trip("5d8784fee246f610b84fdfce", startTime, midTime, locationMap.get(1), locationMap.get(2), ModeOfTransport.Train, true, new ArrayList<>()));
        //london Victoria too Brighton Railway Station
        tripMap.put(2, new Trip("5d8784fee246f610b84fdfcf", startTime, midTime, locationMap.get(1), locationMap.get(3), ModeOfTransport.Train, true, new ArrayList<>()));
        //London Bridge to Brighton Railway Station
//        tripMap.put(3, new Trip("5d8784fee246f610b84fdfd0", startTime, midTime, locationMap.get(2), locationMap.get(3), ModeOfTransport.Train, true, new ArrayList<>()));
        //dover to Brighton
//        tripMap.put(4, new Trip("5d8784fee246f610b84fdfd1", midTime, returnTime, locationMap.get(4), locationMap.get(3), ModeOfTransport.Train, true, new ArrayList<>()));
        //london victoria too London Bridge Station + 2hr
//        tripMap.put(5, new Trip("5d8784fee246f610b84fdfd2", returnTime, endTime, locationMap.get(1), locationMap.get(2), ModeOfTransport.Train, true, new ArrayList<>()));
        //London Bridge Station too london victoria
//        tripMap.put(6, new Trip("5d8784fee246f610b84fdfd4", midTime, returnTime, locationMap.get(2), locationMap.get(1), ModeOfTransport.Train, true, new ArrayList<>()));

        tripMap.put(6, new Trip("5d8784fee246f610b84fdfd5", startTime, endTime, locationMap.get(8), locationMap.get(9), ModeOfTransport.Train, true, new ArrayList<>()));
        tripMap.forEach((key, trip) -> tripCrudRepository
                .findById(tripMap.get(key).getId())
                .doOnNext(empty -> System.out.println("found: " + tripMap.get(key)))
                .switchIfEmpty(tripCrudRepository.save(tripMap.get(key)))
                .subscribe(savedTrip -> logger.info(savedTrip.toString())));
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
        tripMap.get(3).getAttending().add("carloMeUp");

        List<String> deansTrips = new ArrayList<>();
        deansTrips.add(tripMap.get(1).getId());
        deansTrips.add(tripMap.get(5).getId());
        deansTrips.add(tripMap.get(6).getId());
        tripMap.get(1).getAttending().add("theDean");
        tripMap.get(5).getAttending().add("theDean");
        tripMap.get(6).getAttending().add("theDean");

        Map<Integer, Account> accountMap = new LinkedHashMap<>();
        accountMap.put(1, new Account("5e8108e2ef5c0e3670afcbe1", "Adam", "andy123", adamsTrips));
        accountMap.put(2, new Account("5e8108e2ef5c0e3670afcbe2", "Ben", "benny", bensTrips));
        accountMap.put(3, new Account("5e8108e2ef5c0e3670afcbe3", "Carol", "carloMeUp", carolsTrips));
        accountMap.put(4, new Account("5e8108e2ef5c0e3670afcbe4", "Dean", "theDean", deansTrips));

        //Update trips due to updated
        logger.info("Updating Trips with Attending");
        tripMap.forEach((key, trip) -> tripCrudRepository
                .findById(trip.getId())
                .switchIfEmpty(tripCrudRepository.save(tripMap.get(key)))
                .subscribe(savedTrip -> logger.info(savedTrip.toString())));

        accountMap.forEach((key, account) -> accountCrudRepository
                .findFirst1ByNicknameIgnoreCase(account.getNickname())
                .switchIfEmpty(accountCrudRepository.save(accountMap.get(key)))
                .subscribe(savedTrip -> logger.info(savedTrip.toString())));
    }
}

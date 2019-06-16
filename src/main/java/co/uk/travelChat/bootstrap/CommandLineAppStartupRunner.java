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
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Profile("init")
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);
    private final AccountCrudRepository accountCrudRepository;
    private final LocationCrudRepository locationCrudRepository;
    private final TripCrudRepository tripCrudRepository;

    public CommandLineAppStartupRunner(AccountCrudRepository accountCrudRepository, LocationCrudRepository locationCrudRepository, TripCrudRepository tripCrudRepository) {
        this.accountCrudRepository = accountCrudRepository;
        this.locationCrudRepository = locationCrudRepository;
        this.tripCrudRepository = tripCrudRepository;
    }

    @Override
    public void run(String...args) throws Exception {
        loadAccounts();
        loadLocations();
        loadTrips();
    }

    private void loadLocations() {
        logger.info("Adding Locations");

        Map<Integer, Location> locationMap = new LinkedHashMap<>();
        locationMap.put(1, new Location(null, "Ha Long", 20.972683d, 107.046353d));
        locationMap.put(2, new Location(null, "The Great Blue Hole", 17.315786, -87.534782));
        locationMap.put(3, new Location(null, "Desert Breath", 27.380369d, 33.632155d));

        locationCrudRepository.deleteAll().subscribe();

        locationMap.forEach((key, location) -> locationCrudRepository.save(location).subscribe());

        locationCrudRepository.findAll().doOnNext(location -> logger.info(location.toString())).subscribe();
    }

    private void loadTrips() {
        logger.info("Adding Trips");

        Map<Integer, Location> locationMap = new LinkedHashMap<>();
        locationMap.put(1, new Location(null, "London Victoria", 51.495213, -0.143897));
        locationMap.put(2, new Location(null, "London Bridge Station", 51.504527, -0.086392));
        locationMap.put(3, new Location(null, "Dover Priory", 51.126234, 1.304786));//
        locationMap.put(4, new Location(null, "Brighton Railway Station", 50.829467, -0.140960));

        locationMap.forEach((key, location) -> locationCrudRepository.save(location).subscribe());

        LocalDateTime localDateTime = LocalDateTime.now();

        Map<Integer, Trip> tripMap = new LinkedHashMap<>();
        //london victoria too Brighton
        tripMap.put(1, new Trip(null, localDateTime, localDateTime.plusHours(1), locationMap.get(1), locationMap.get(4), ModeOfTransport.Train,true));
        //london Victoria too London Bridge
        tripMap.put(2, new Trip(null, localDateTime, localDateTime.plusHours(1), locationMap.get(1), locationMap.get(2), ModeOfTransport.Train,true));
        //London Bridge to dover
        tripMap.put(3, new Trip(null, localDateTime, localDateTime.plusHours(1), locationMap.get(2), locationMap.get(3), ModeOfTransport.Train,true));

        tripCrudRepository.deleteAll().subscribe();

        tripMap.forEach((key, trip) -> tripCrudRepository.save(trip).subscribe());

        tripCrudRepository.findAll().doOnNext(trip -> logger.info(trip.toString())).subscribe();
    }

    private void loadAccounts() {
        logger.info("Adding Accounts");

        Map<Integer, Account> accountMap = new LinkedHashMap<>();
        accountMap.put(1, new Account(null, "Adam", "andy123"));
        accountMap.put(2, new Account(null, "Ben", "benny"));
        accountMap.put(3, new Account(null, "Carol", "carloMeUp"));

        accountCrudRepository.deleteAll().subscribe();

        accountMap.forEach((key, account) -> accountCrudRepository.save(account).subscribe());

        accountCrudRepository.findAll().doOnNext(account -> logger.info(account.toString())).subscribe();
}

}

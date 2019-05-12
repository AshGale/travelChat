package co.uk.travelChat.bootstrap;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);
    private final AccountCrudRepository accountCrudRepository;

    public CommandLineAppStartupRunner(AccountCrudRepository accountCrudRepository) {
        this.accountCrudRepository = accountCrudRepository;
    }

    @Override
    public void run(String...args) throws Exception {
        logger.info("Application started ");

        Map<Integer, Account> accountMap = new LinkedHashMap<>();
        accountMap.put(1, new Account(null, "Adam", "andy123"));
        accountMap.put(2, new Account(null, "Ben", "benny"));
        accountMap.put(3, new Account(null, "Carol", "carloMeUp"));


        accountCrudRepository.deleteAll().subscribe();

        accountMap.forEach((key, account) -> accountCrudRepository.save(account).subscribe());

        accountCrudRepository.findAll().doOnNext(System.out::println).subscribe();
    }


}

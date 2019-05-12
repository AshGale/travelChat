package co.uk.travelChat.bootstrap;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
        accountMap.put(1, new Account(null, "Adam", 10D));
        accountMap.put(2, new Account(null, "Ben", 10D));
        accountMap.put(3, new Account(null, "Carol", 10D));


        accountCrudRepository.deleteAll().subscribe();

        Mono<Account> savedAccount = accountCrudRepository.save(accountMap.get(1));
        logger.info("Saved to Db: " + savedAccount.block().toString());

        Mono<Account> initAccont = accountCrudRepository.findFirstByName(accountMap.get(1).getName());
        logger.info("Found from Db: " + initAccont.block().toString());

    }


}

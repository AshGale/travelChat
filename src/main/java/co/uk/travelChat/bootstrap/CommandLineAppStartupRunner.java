package co.uk.travelChat.bootstrap;

import co.uk.travelChat.model.Account;
import co.uk.travelChat.repository.AccountCrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

        Mono<Account> savedAccount = accountCrudRepository.save(new Account(null, "InitAccount", 10D));
        logger.info("Saved to Db: " + savedAccount.block().toString());

        Mono<Account> initAccont = accountCrudRepository.findFirstByOwner("InitAccount");
        logger.info("Found from Db: " + initAccont.block().toString());

    }


}

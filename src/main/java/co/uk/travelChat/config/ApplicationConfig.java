package co.uk.travelChat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractReactiveCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableReactiveCouchbaseRepositories;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableReactiveCouchbaseRepositories
class ApplicationConfig extends AbstractReactiveCouchbaseConfiguration {

    @Override
    protected List<String> getBootstrapHosts() {
        return Collections.singletonList("127.0.0.1");
    }

    @Override
    protected String getBucketName() {
        return "TravelChat";
    }

    @Override
    protected String getBucketPassword() {
        return "abc123";
    }
}

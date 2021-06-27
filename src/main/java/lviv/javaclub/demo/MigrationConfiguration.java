package lviv.javaclub.demo;

import flyway.NotificationBeanMigration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MigrationConfiguration implements FlywayConfigurationCustomizer {

    @Bean
    public NotificationBeanMigration beanMigration() {
        return new NotificationBeanMigration(null);
    }


    private FlywayConfigurationResolver notificationsResolver() {
        return new FlywayConfigurationResolver();
    }

    @Override
    public void customize(FluentConfiguration configuration) {
        configuration.resolvers(notificationsResolver());
    }
}

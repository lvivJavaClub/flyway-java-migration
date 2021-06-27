package lviv.javaclub.demo;

import flyway.NotificationBeanMigration;
import flyway.NotificationMigrationExecutor;
import lombok.SneakyThrows;
import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.resolver.Context;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;
import org.flywaydb.core.internal.util.ClassUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class FlywayConfigurationResolver implements MigrationResolver {


    @SneakyThrows
    @Override
    public Collection<ResolvedMigration> resolveMigrations(Context context) {
        var pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        var resources = pathMatchingResourcePatternResolver.getResources("classpath*:/notification*.yaml");

        return Arrays.stream(resources)
                .map(this::notificationMigration)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private ResolvedMigration notificationMigration(Resource resource) {
        return new ResolvedMigrationImpl(
                null,
                "Notification: " + resource.getFilename(),
                "Notification: " + resource.getFilename(),
                (int)resource.contentLength(),
                null,
                MigrationType.JDBC,
                ClassUtils.getLocationOnDisk(NotificationBeanMigration.class),
                new NotificationMigrationExecutor(new NotificationBeanMigration(resource))
        );
    }
}

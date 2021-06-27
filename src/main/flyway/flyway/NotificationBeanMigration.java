package flyway;

import lombok.RequiredArgsConstructor;
import model.NotificationModel;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.sql.Connection;
import java.sql.Types;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class NotificationBeanMigration implements JavaMigration {

    private final Resource resource;

    @Override
    public MigrationVersion getVersion() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Bean migration";
    }

    @Override
    public Integer getChecksum() {
        return null;
    }

    @Override
    public boolean isUndo() {
        return false;
    }

    @Override
    public boolean canExecuteInTransaction() {
        return true;
    }

    @Override
    public void migrate(Context context) throws Exception {
        var yaml = new Yaml(new Constructor(NotificationModel.class));
        try (var inStream = resource.getInputStream()) {
            var notification = yaml.<NotificationModel> load(inStream);

            var connection = context.getConnection();
            String sql = "INSERT INTO email(body) VALUES (?)";
            try (var ps = connection.prepareStatement(sql)) {
                ps.setString(1, notification.getBody());
                ps.executeUpdate();
            }

            System.out.println("Do migration with bean" + notification);


        }

    }
}

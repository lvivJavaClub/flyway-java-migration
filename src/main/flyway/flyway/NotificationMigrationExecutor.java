package flyway;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.executor.Context;
import org.flywaydb.core.api.executor.MigrationExecutor;
import org.flywaydb.core.internal.database.DatabaseExecutionStrategy;
import org.flywaydb.core.internal.database.DatabaseTypeRegister;
import org.flywaydb.core.internal.database.base.DatabaseType;
import org.flywaydb.core.internal.util.SqlCallable;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class NotificationMigrationExecutor implements MigrationExecutor {

    private final NotificationBeanMigration notificationBeanMigration;

    @Override
    public void execute(Context context) throws SQLException {
        DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(context.getConnection());

        DatabaseExecutionStrategy strategy = databaseType.createExecutionStrategy(context.getConnection());
        strategy.execute(new SqlCallable<Boolean>() {
            @Override
            public Boolean call() throws SQLException {
                executeOnce(context);
                return true;
            }
        });
    }

    private void executeOnce(final Context context) throws SQLException {
        try {
            notificationBeanMigration.migrate(new org.flywaydb.core.api.migration.Context() {
                @Override
                public Configuration getConfiguration() {
                    return context.getConfiguration();
                }

                @Override
                public Connection getConnection() {
                    return context.getConnection();
                }
            });
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new FlywayException("Migration failed !", e);
        }
    }


    @Override
    public boolean canExecuteInTransaction() {
        return true;
    }
}

package dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class used to create Database connection
 */
public class Database {
    private static Database database;
    private static ComboPooledDataSource comboPooledDataSource;

    private static String DB_CHOICE = System.getenv("DB_CHOICE");
    private static boolean isTest = isJUnitTest();


    private static final String IP_TO_VM = "129.241.96.191";

    private static String DB_URL = "jdbc:mysql://" + ((DB_CHOICE != null) ?
            (DB_CHOICE + ":3306/") :
            (IP_TO_VM + ((isTest) ? ":3308/" : ":3307/")));

    private static String DB_NAME = "myChat";
    private static String DB_USERNAME = "root";
    private static String DB_PW = "example";

    private static final String DB_USE_SSL = "?useSSL=false";

    /**
     * Sets up ComboPooledDataSource to handle multiple simultaneous queries to DB
     */
    private Database() {
        try {
            comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setJdbcUrl(DB_URL+DB_NAME+ DB_USE_SSL);
            comboPooledDataSource.setUser(DB_USERNAME);
            comboPooledDataSource.setPassword(DB_PW);

            comboPooledDataSource.setInitialPoolSize(5);
            comboPooledDataSource.setMinPoolSize(5);
            comboPooledDataSource.setMaxPoolSize(200);
            comboPooledDataSource.setAcquireIncrement(5);
            comboPooledDataSource.setIdleConnectionTestPeriod(60);
            comboPooledDataSource.setMaxStatements(0);
            comboPooledDataSource.setMaxIdleTime(60);
        } catch (Exception e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
        }
    }

    /**
     * Checks if there is an existing instance of Database. If not, it creates one.
     * @return An instance of Database, either the existing one, or a newly created one.
     */
    public static Database instance() {
        if (database == null) {
            database = new Database();
            return database;
        } else {
            return database;
        }
    }

    /**
     * Method to get a connection from the Database object.
     * @return A connection to the Database object.
     * @throws SQLException if failing to get connection
     */
    public Connection getConnection() throws SQLException {
        return comboPooledDataSource.getConnection();
    }

    /**
     * Check if connection is from tests or production build
     * @return true if connection from tests
     */
    private static boolean isJUnitTest() {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraces) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Closes connections to database, makes sure that resultSets, and statements gets closed properly
     * @param connection the connection to be closed
     * @param preparedStatement the preparedStatement to be closed
     * @param resultSet the resultSet to be closed
     */
    static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
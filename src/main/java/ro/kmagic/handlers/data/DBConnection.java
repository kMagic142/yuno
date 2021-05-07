package ro.kmagic.handlers.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import io.sentry.Sentry;
import ro.kmagic.Main;
import ro.kmagic.types.DataType;
import ro.kmagic.utils.Utils;

public class DBConnection {

    private Connection connection;
    private DataType type;
    private File sqlitedb;
    public CountDownLatch latch;

    public DBConnection(DataType type) {
        this.type = type;
        connect(null, null, null, null, 0);
    }

    public DBConnection(DataType type, String host, String database, String username, String password, int port) {
        this.type = type;
        connect(host, database, username, password, port);
    }

    private void connect(String host, String database, String username, String password, int port) {
        System.out.println("Trying to connect to the database.");

        try {
            switch(this.type) {
                case SQLite:
                    Class.forName("org.sqlite.JDBC");
                    this.sqlitedb = new File(Main.getDataFolder(), "database.db");
                    sqlitedb.createNewFile();
                    break;
                case MySQL:
                    Class.forName("com.mysql.jdbc.Driver");
                    break;
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        try {
            synchronized (this) {
                if (this.connection != null && !(this.connection.isClosed())) {
                    System.out.println("The connection has already been set. Note that switching connections might cause unexpected errors!");
                }
                switch(this.type) {
                    case MySQL:
                        latch = new CountDownLatch(3);
                        this.connection = DriverManager
                                .getConnection(Utils.DEFAULT_DB_URI_MYSQL + host + ":" + port + "/" + database, username, password);
                        break;
                    case SQLite:
                        this.connection = DriverManager
                                .getConnection(Utils.DEFAULT_DB_URI_SQLITE + sqlitedb.getAbsolutePath());
                        break;
                }
            }
            System.out.println("Successfully connected to the database! " + ((host != null) ? (host + ":" + port + "/" + username) : sqlitedb.getName() + " - Yuno"));
        } catch (SQLException e) {
            Sentry.captureException(e);
            System.out.println("A connection couldn't be established to the database. Consult the stacktrace above and try to solve the error!");
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}

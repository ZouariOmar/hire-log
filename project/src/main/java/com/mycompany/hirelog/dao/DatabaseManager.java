/**
 * DatabaseManager.java
 *
 * Database connection manager
 *
 * <p>None</p>
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 28/07/2025
 *
 * <a href="https://github.com/ZouariOmar/HireLog/blob/main/project/src/main/java/com.mycompany.hirelog/util/DatabaseManager.java">
 *  DatabaseManager.java
 * </a>
*/

// `DatabaseManager` pkg name
package com.mycompany.hirelog.dao;

// Core java imports
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Log4j java imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseManager {
  private static final Logger _LOGGER = LogManager.getLogger(DatabaseManager.class);

  private static final String _SQLITE_CLASS_NAME = "org.sqlite.JDBC";

  public static Connection connect() {
    Connection connection = null;
    try {
      Class.forName(_SQLITE_CLASS_NAME);
      _LOGGER.info("{} found it!", _SQLITE_CLASS_NAME);
      connection = DriverManager.getConnection("jdbc:sqlite:" + ensureWritableDatabase().toString());
      _LOGGER.info("Connection to SQLite database established successfully!");

    } catch (ClassNotFoundException e) {
      _LOGGER.error("org.sqlite.JDBC Not found!");
      e.printStackTrace();

    } catch (final IOException e) {
      _LOGGER.error("hirelog.db IO error: {}", e.getMessage());
      e.printStackTrace();

    } catch (SQLException e) {
      _LOGGER.error("SQLITE coneection error!");
      e.printStackTrace();
    }
    return connection;
  }

  public static Path getAppDataPath() throws IOException {
    String os = System.getProperty("os.name").toLowerCase();
    Path path;

    if (os.contains("win")) {
      path = Paths.get(System.getenv("LOCALAPPDATA"), "HireLog");

    } else if (os.contains("mac")) {
      path = Paths.get(System.getProperty("user.home"), "Library", "Application Support", "HireLog");

    } else { // Linux or others
      path = Paths.get(System.getProperty("user.home"), ".local", "share", "HireLog");
    }

    Files.createDirectories(path); // Ensure it exists
    return path;
  }

  public static Path ensureWritableDatabase() throws IOException {
    Path dbPath = getAppDataPath().resolve("hirelog.db");

    if (!Files.exists(dbPath)) {
      try (InputStream in = DatabaseManager.class.getResourceAsStream("/database/hirelog.db")) {
        if (in == null)
          throw new IOException("Embedded DB not found!");
        Files.copy(in, dbPath);
      }
    }
    return dbPath;
  }
} // Database class

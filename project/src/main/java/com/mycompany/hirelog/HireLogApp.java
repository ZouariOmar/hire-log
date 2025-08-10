/**
 * HireLogApp.java
 *
 * The HireLog entry point
 *
 * <p>By default, it will open the `HireLog - Login` interface</p>
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 27/07/2025
 * @serial [bc0b8d5]
 * 
 * <a href="https://github.com/ZouariOmar/HireLog/blob/main/project/src/main/java/com/mycompany/HireLog/HireLogApp.java">
 *  HireLogApp.java
 * </a>
 */

// `HireLogApp` pkg name
package com.mycompany.hirelog;

// Core java imports
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// `log4j` java imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycompany.hirelog.controller.LoginController;

// JavaFx imports
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HireLogApp extends Application {
  private static final Logger _LOGGER = LogManager.getLogger(HireLogApp.class);

  private static Properties emailProperties;

  public static void main(String[] args) {
    getMailServiceProperties(args);
    launch(args);
  }

  private static void getMailServiceProperties(String[] args) {
    String filePath = null;

    // Parse --file argument
    for (int i = 0; i < args.length - 1; i++) {
      if ("--file".equals(args[i])) {
        filePath = args[i + 1];
        break;
      }
    }

    if (filePath == null) {
      System.err.println("Usage: ./HireLog --file <PATH_TO_EMAIL_PROPERTIES_>" +
          "Tip: You Can just make an empty/template file to pass, but the mail service will not work");
      System.exit(1);
    }

    if (loadProperties(filePath)) {
      _LOGGER.info("Properties file loaded successfully!");
    } else {
      _LOGGER.error("Properties file load failed!");
    }
  }

  private static boolean loadProperties(String path) {
    emailProperties = new Properties();
    try (FileInputStream fis = new FileInputStream(path)) {
      emailProperties.load(fis);
      return true;
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
    return false;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
    loader.setControllerFactory(_ -> {
      return new LoginController(emailProperties);
    });
    final Parent root = loader.load();
    primaryStage.setTitle("HireLog - Login");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}

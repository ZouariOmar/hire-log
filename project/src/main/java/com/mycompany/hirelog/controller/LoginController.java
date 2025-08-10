/**
 * LoginController.java
 *
 * `Login.fxml` controller class
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 27/07/2025
 * 
 * <a href="https://github.com/ZouariOmar/HireLog/tree/main/project/src/test/java/com.mycompany.hirelog/controller/LoginController.java">
 *  LoginController.java
 * </a>
*/

// `LoginController` pkg name
package com.mycompany.hirelog.controller;

// Core java imports
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// `log4j` java imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Custom java imports
import com.mycompany.hirelog.dao.UserConnector;
import com.mycompany.hirelog.view.ViewUtils;

// `javafx` imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {
  private static final Logger _LOGGER = LogManager.getLogger(LoginController.class);

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="forgetPassword"
  private Hyperlink forgetPassword; // Value injected by FXMLLoader

  @FXML // fx:id="login"
  private Button login; // Value injected by FXMLLoader

  @FXML // fx:id="logo"
  private ImageView logo; // Value injected by FXMLLoader

  @FXML // fx:id="password"
  private PasswordField password; // Value injected by FXMLLoader

  @FXML // fx:id="sginUpHyperlink"
  private Hyperlink sginUpHyperlink; // Value injected by FXMLLoader

  @FXML // fx:id="username"
  private TextField username; // Value injected by FXMLLoader

  @FXML // fx:id="status"
  private Label status; // Value injected by FXMLLoader

  @FXML
  void onForgetPasswordAction(final ActionEvent event) throws IOException {
    final Parent root = FXMLLoader.load(getClass().getResource("/fxml/ForgetPassword.fxml"));
    // Get the current stage (window) from the event source and set the new scene
    final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setTitle("HireLog - Sign up");
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Slot connected to `login` signals
   *
   * <p>
   * This method is marked with &#64;FXML so it can be invoked by the FXML loader.
   * </p>
   *
   * @param event {@code ActionEvent}
   *
   * @see https://openjfx.io/javadoc/22/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html
   */
  @FXML
  private void onLoginAction(final ActionEvent event) throws IOException {
    final String enterdUsername = username.getText();

    if (enterdUsername.isEmpty() || enterdUsername.isBlank()) { // Display "Username field is empty/blank!" for 3s
      ViewUtils.showStatusMsg(status, "Username field is empty/blank!");
      _LOGGER.warn("Login Access Failed: `enterdUsername` isEmpty/isBlank!");
      return;
    }

    if (password.getText().isEmpty() || enterdUsername.isBlank()) { // Display "Password field is empty/blank!" for 3s
      ViewUtils.showStatusMsg(status, "Password field is empty/blank!");
      _LOGGER.warn("Login Access Failed: `eneterdPassword` isEmpty/isBlank!");
      return;
    }

    final int userId = UserConnector.isUser(enterdUsername, password.getText());
    if (userId != -1) { // Login success!
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
      loader.setControllerFactory(_ -> {
        return new DashboardController(userId);
      });

      final Parent root = loader.load();
      final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setTitle("HireLog - Sign up");
      stage.setScene(new Scene(root));
      stage.show();
      _LOGGER.info("Login Access Success: `{}` accessed with id `{}`!", enterdUsername, userId);

    } else { // Display "Username or password incoreect!" for 3s
      ViewUtils.showStatusMsg(status, "Username or password incoreect!");
      _LOGGER.warn("Login Access Failed: `{}` want to access!", enterdUsername);
    }
  }

  /**
   * Slot connected to `sginUpHyperlink` signals
   *
   * <p>
   * This method is marked with &#64;FXML so it can be invoked by the FXML loader.
   * </p>
   *
   * @throws IOException For `FXMLLoader.load()` method
   *
   * @param event {@code ActionEvent}
   */
  @FXML
  private void onSginUpHyperlinkAction(final ActionEvent event) throws IOException {
    final Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
    // Get the current stage (window) from the event source and set the new scene
    final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setTitle("HireLog - Sign up");
    stage.setScene(new Scene(root));
    stage.show();
  }

  @FXML // This method is called by the FXMLLoader when initialization is complete
  private void initialize() {
    assert forgetPassword != null : "fx:id=\"forgetPassword\" was not injected: check your FXML file 'Login.fxml'.";
    assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'Login.fxml'.";
    assert logo != null : "fx:id=\"logo\" was not injected: check your FXML file 'Login.fxml'.";
    assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'Login.fxml'.";
    assert sginUpHyperlink != null : "fx:id=\"sginUpHyperlink\" was not injected: check your FXML file 'Login.fxml'.";
    assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'Login.fxml'.";
    assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'Login.fxml'.";
  }
}

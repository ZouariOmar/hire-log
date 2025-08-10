/**
 * SignUpController.java
 *
 * 'SignUp.fxml' controller class
 *
 * <p>NONE</p>
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 29/07/2025
 *
 * <a href="https://github.com/ZouariOmar/HireLog/tree/main/project/src/test/java/com.mycompany.hirelog/controller/SignUpController.java">
 *  SignUpController.java
 * </a>
 */

// `SignUpController` pkg name
package com.mycompany.hirelog.controller;

// Core java imports
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// `log4j` imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Custom java imports
import com.mycompany.hirelog.dao.UserConnector;
import com.mycompany.hirelog.model.User;
import com.mycompany.hirelog.view.ViewUtils;

// `javafx` imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignUpController {
  private static final Logger _LOGGER = LogManager.getLogger(SignUpController.class);

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="banner"
  private ImageView banner; // Value injected by FXMLLoader
  //
  @FXML // fx:id="email"
  private TextField email; // Value injected by FXMLLoader

  @FXML // fx:id="logo"
  private ImageView logo; // Value injected by FXMLLoader

  @FXML // fx:id="name"
  private TextField name; // Value injected by FXMLLoader

  @FXML // fx:id="password"
  private PasswordField password; // Value injected by FXMLLoader

  @FXML // fx:id="prename"
  private TextField prename; // Value injected by FXMLLoader

  @FXML // fx:id="signUpBox"
  private AnchorPane signUpBox; // Value injected by FXMLLoader

  @FXML // fx:id="signUpBtn"
  private Button signUpBtn; // Value injected by FXMLLoader

  @FXML // fx:id="status"
  private Label status; // Value injected by FXMLLoader

  /**
   * @param event
   * @throws IOException
   */
  @FXML
  void onSignUpAction(final ActionEvent event) throws IOException {
    final String entredPrename = prename.getText(),
        entredName = name.getText(),
        entredEmail = email.getText(),
        entredPassword = password.getText();

    if (entredPrename.isEmpty() || entredPrename.isBlank()) { // Display "Prename field is empty/blank!" for 3s
      ViewUtils.showStatusMsg(status, "Prename field is empty/blank!");
      _LOGGER.warn("`enterdPrename` isEmpty/isBlank!");
      return;
    }

    if (entredName.isEmpty() || entredName.isBlank()) { // Display "Name field is empty/blank!" for 3s
      ViewUtils.showStatusMsg(status, "Name field is empty/blank!");
      _LOGGER.warn("`enterdName` isEmpty/isBlank!");
      return;
    }

    if (!UserConnector.isEmail(entredEmail)) { // Display "Invalid email format!" for 3s
      ViewUtils.showStatusMsg(status, "Invalid email format!");
      _LOGGER.warn("Invalid email format! - enterdEmail: `{}`", entredEmail);
      return;
    }

    if (UserConnector.isExistedEmail(entredEmail)) { // Display "This email already exist!" for 3s
      ViewUtils.showStatusMsg(status, "Email already exist!");
      _LOGGER.warn("Email already exist! - enterdEmail: `{}`", entredEmail);
      return;
    }

    if (!UserConnector.isPassword(entredPassword)) { // Display "Invalid password format!" for 3s
      ViewUtils.showStatusMsg(status, "Invalid password format!");
      _LOGGER.warn("Invalid password format! - enterdPassword: `{}`", entredPassword);
      return;
    }

    final String username = UserConnector.createUser(new User(
        entredPrename + entredName,
        entredEmail,
        entredPassword));

    if (username != null) { // SignUp success!
      ViewUtils.showStatusMsg(status, "Your username is: " + username, Color.GREEN, 10); // Display `username` for 10s
      _LOGGER.info(
          "SignUp Success: `{}` accessed! - prename: {} Name: {} Email: {} Password: {}",
          entredPrename,
          entredPrename,
          entredName,
          entredEmail,
          entredPassword);
    } // No need for else statment
  }

  @FXML // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    assert banner != null : "fx:id=\"banner\" was not injected: check your FXML file 'SignUp.fxml'.";
    assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'SignUp.fxml'.";
    assert logo != null : "fx:id=\"logo\" was not injected: check your FXML file 'SignUp.fxml'.";
    assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'SignUp.fxml'.";
    assert password != null : "fx:id=\"password\" was not injected: check your FXML file 'SignUp.fxml'.";
    assert prename != null : "fx:id=\"prename\" was not injected: check your FXML file 'SignUp.fxml'.";
    assert signUpBox != null : "fx:id=\"signUpBox\" was not injected: check your FXML file 'SignUp.fxml'.";
    assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'SignUp.fxml'.";
  }

  /**
   * Slot connected to `sginInHyperlink` signals
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
  private void onSginInHyperlinkAction(final ActionEvent event) throws IOException {
    final Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
    final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setTitle("HireLog - Dashboard");
    stage.setScene(new Scene(root));
    stage.show();
  }
}

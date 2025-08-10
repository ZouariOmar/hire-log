/**
 * FrogetPasswordController.java
 *
 * `FrogetPasswordController.fxml` controller
 *
 * <p>None</p>
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 09/08/2025
 *
 * <a href="https://github.com/ZouariOmar/HireLog/tree/main/project/src/main/java/com/mycompany/HireLog/controller/FrogetPasswordController.java">
 *  FrogetPasswordController.java
 * </a>
 */

// `FrogetPasswordController` pkg name
package com.mycompany.hirelog.controller;

// Core java imports
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

// Java Log4j imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Custom imports
import com.mycompany.hirelog.dao.UserConnector;
import com.mycompany.hirelog.service.MailSenderService;
import com.mycompany.hirelog.service.PasswordGeneratorService;
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

public class FrogetPasswordController {
  private static final Logger _LOGGER = LogManager.getLogger(FrogetPasswordController.class);

  private final Properties emailProperties;

  private static String generatedResetCode;

  private static String confermedEmail;

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="email"
  private TextField email; // Value injected by FXMLLoader

  @FXML // fx:id="emailImg"
  private ImageView emailImg; // Value injected by FXMLLoader

  @FXML // fx:id="logo"
  private ImageView logo; // Value injected by FXMLLoader

  @FXML // fx:id="newPassword"
  private PasswordField newPassword; // Value injected by FXMLLoader

  @FXML // fx:id="reset"
  private Button reset; // Value injected by FXMLLoader

  @FXML // fx:id="resetCode"
  private PasswordField resetCode; // Value injected by FXMLLoader

  @FXML // fx:id="send"
  private Button send; // Value injected by FXMLLoader

  @FXML // fx:id="signUpBox"
  private AnchorPane signUpBox; // Value injected by FXMLLoader

  @FXML // fx:id="status"
  private Label status; // Value injected by FXMLLoader

  public FrogetPasswordController() {
    emailProperties = null;
    generatedResetCode = null;
    confermedEmail = null;
  }

  public FrogetPasswordController(Properties emailProperties) {
    this.emailProperties = emailProperties;
    generatedResetCode = null;
    confermedEmail = null;
  }

  @FXML
  void onSendAction(final ActionEvent e) {
    final String entredEmail = email.getText();

    // Make email image animation
    ViewUtils.playGifAnimation(emailImg, "/assets/icons8-send-mail.gif");

    if (!UserConnector.isEmail(entredEmail)) { // Display "Invalid email format!" for 3s
      ViewUtils.showStatusMsg(status, "Invalid email format!");
      _LOGGER.warn("Invalid email format! - enterdEmail: `{}`", entredEmail);
      return;
    }

    if (!UserConnector.isExistedEmail(entredEmail)) { // Display "Email Not founded!" for 3s
      ViewUtils.showStatusMsg(status, "Email Not founded!");
      _LOGGER.warn("Email Not founded in db! - enterdEmail: `{}`", entredEmail);
      return;
    }

    // === Client pass email verification part ===

    // Assign the confirmed mail
    confermedEmail = entredEmail;

    // Generate the reset code
    generatedResetCode = PasswordGeneratorService.generate();

    // Send the mail
    MailSenderService.send(
        emailProperties,
        confermedEmail,
        "do-not-reply - Reset Password",
        "Reset Code: " + generatedResetCode);

    // Disable `email` TextField && `send` Button
    email.setDisable(true);
    send.setDisable(true);

    // Enable `resetCode`, `newPassword` && `reset`
    resetCode.setDisable(false);
    newPassword.setDisable(false);
    reset.setDisable(false);
  }

  @FXML
  void onResetAction(final ActionEvent e) {
    final String entredResetCode = resetCode.getText(),
        entredPassword = newPassword.getText();

    if (entredResetCode.isEmpty() || entredResetCode.isBlank()) { // Display "Reset Code field is empty/blank!" for 3s
      ViewUtils.showStatusMsg(status, "Reset Code field is empty/blank!");
      _LOGGER.warn("`entredResetCode` isEmpty/isBlank!");
      return;
    }

    if (entredPassword.isEmpty() || entredPassword.isBlank()) { // Display "New Password field is empty/blank!" for 3s
      ViewUtils.showStatusMsg(status, "New Password field is empty/blank!");
      _LOGGER.warn("`entredPassword` isEmpty/isBlank!");
      return;
    }

    if (!UserConnector.isPassword(entredPassword)) { // Display "Invalid password format!" for 3s
      ViewUtils.showStatusMsg(status, "Invalid password format!");
      _LOGGER.warn("Invalid password format! - enterdPassword: `{}`", entredPassword);
      return;
    }

    // `generatedResetCode` can't be `null`, but for safety :)
    if (generatedResetCode == null || !generatedResetCode.equals(entredResetCode)) { // Display "Reset Code not
                                                                                     // verified!" for 3s
      ViewUtils.showStatusMsg(status, "Reset Code not verified!");
      _LOGGER.warn("`entredResetCode` is wrong! - entredResetCode: {}", entredResetCode);
      return;
    }

    // === Client pass Reset Code verification part ===
    // Disable `resetCode`, `newPassword` && `reset`
    resetCode.setDisable(true);
    newPassword.setDisable(true);
    reset.setDisable(true);

    // Update client password (lance the query)
    UserConnector.updatePassword(entredPassword, confermedEmail);

    ViewUtils.showStatusMsg( // Display "Password Update itsuccessfully!" for 3s
        status,
        "Password Update it successfully!",
        Color.GREEN);
    _LOGGER.info("Password Update it successfully!");
  }

  @FXML
  void onSginInHyperlinkAction(final ActionEvent e) throws IOException {
    final Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
    final Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
    stage.setTitle("HireLog - Dashboard");
    stage.setScene(new Scene(root));
    stage.show();
  }

  @FXML // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    assert email != null : "fx:id=\"email\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
    assert emailImg != null : "fx:id=\"emailImg\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
    assert logo != null : "fx:id=\"logo\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
    assert newPassword != null : "fx:id=\"newPassword\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
    assert reset != null : "fx:id=\"reset\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
    assert resetCode != null : "fx:id=\"resetCode\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
    assert send != null : "fx:id=\"send\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
    assert signUpBox != null : "fx:id=\"signUpBox\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
    assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'ForgetPassword.fxml'.";
  }
}

/**
 * HireLogFormController.java
 *
 * 'HireLogDialog.fxml' controller class
 *
 * <p>None</p>
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 04/08/2025
 *
 * <a href="https://github.com/ZouariOmar/HireLog/tree/main/project/src/main/java/com/mycompany/HireLog/controller/HireLogFormController.java">
 *  HireLogFormController.java
 * </a>
 */

// `HireLogDialogController` pkg name
package com.mycompany.hirelog.controller;

// Core java imports
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

// `log4j` java imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Custom java imports
import com.mycompany.hirelog.dao.HireLogConnector;
import com.mycompany.hirelog.flag.HireLogEvents;
import com.mycompany.hirelog.model.HireLog;
import com.mycompany.hirelog.view.LogTableUi;
import com.mycompany.hirelog.view.ViewUtils;

// `javafx` imports
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HireLogFormController {
  private static final Logger _LOGGER = LogManager.getLogger(HireLogFormController.class);

  private final int userId;

  private final LogTableUi jobTrackerData;

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="cancel"
  private Button cancel; // Value injected by FXMLLoader

  @FXML // fx:id="comments"
  private TextArea comments; // Value injected by FXMLLoader

  @FXML // fx:id="date"
  private DatePicker date; // Value injected by FXMLLoader

  @FXML // fx:id="event"
  private ChoiceBox<String> event; // Value injected by FXMLLoader

  @FXML // fx:id="jobTitle"
  private TextField jobTitle; // Value injected by FXMLLoader

  @FXML // fx:id="status"
  private Label status; // Value injected by FXMLLoader

  @FXML // fx:id="submit"
  private Button submit; // Value injected by FXMLLoader

  /**
   * @param userId
   */
  public HireLogFormController(final int userId) {
    this.userId = userId;
    this.jobTrackerData = null;
  }

  public HireLogFormController(final int userId, final LogTableUi row) {
    this.userId = userId;
    this.jobTrackerData = row;
  }

  @FXML
  void onSubmitAction(final ActionEvent e) {
    final String jobTitleText = jobTitle.getText();

    if (jobTitleText.isEmpty() || jobTitleText.isBlank()) { // Display "JobTitle field is empty/blank!" for 3s
      ViewUtils.showStatusMsg(status, "JobTitle field is empty/blank!");
      _LOGGER.warn(
          "com.mycompany.hirelog.controller.HireLogFormController#onSubmitAction: `jobTitle` isEmpty/isBlank!");
      return;
    }

    final boolean isInsert = (jobTrackerData == null);
    final HireLog hireLog = new HireLog(
        (isInsert) ? null : jobTrackerData.getLogId(),
        userId,
        jobTitleText,
        event.getValue(),
        java.sql.Date.valueOf(date.getValue()),
        comments.getText());

    if (isInsert) { // Insert query
      HireLogConnector.create(hireLog);
      ViewUtils.showStatusMsg(status, "Job track added successfully!", Color.GREEN);

    } else { // Update query
      HireLogConnector.update(hireLog);
      ViewUtils.showStatusMsg(status, "Job track updated successfully!", Color.GREEN);
    }

    _LOGGER.info("`com.mycompany.hirelog.controller#onSubmitAction()` passed!");
  }

  @FXML
  void onCancelAction(final ActionEvent e) {
    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
    stage.close();
  }

  @FXML // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'HireLogForm.fxml'.";
    assert comments != null : "fx:id=\"comments\" was not injected: check your FXML file 'HireLogForm.fxml'.";
    assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'HireLogForm.fxml'.";
    assert event != null : "fx:id=\"event\" was not injected: check your FXML file 'HireLogForm.fxml'.";
    assert jobTitle != null : "fx:id=\"jobTitle\" was not injected: check your FXML file 'HireLogForm.fxml'.";
    assert status != null : "fx:id=\"status\" was not injected: check your FXML file 'HireLogForm.fxml'.";
    assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'HireLogForm.fxml'.";

    // Set `event` items
    event.setItems(
        FXCollections.observableArrayList(
            HireLogEvents.APPLIED.getEventName(),
            HireLogEvents.INTERVIEWED.getEventName(),
            HireLogEvents.HIRED.getEventName(),
            HireLogEvents.REJECTED.getEventName(),
            HireLogEvents.OTHER.getEventName()));

    if (jobTrackerData != null) {
      this.jobTitle.setText(jobTrackerData.getJobTitle());
      this.date.setValue(jobTrackerData.getDate().toLocalDate());
      this.event.setValue(jobTrackerData.getEvent());
      this.comments.setText(jobTrackerData.getComments());
    } else {
      event.setValue(HireLogEvents.INTERVIEWED.getEventName()); // Set `Interviewed` as default option
      date.setValue(LocalDate.now());
    }
  }
} // HireLogDialogController class

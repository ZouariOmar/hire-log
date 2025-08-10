/**
 * HireLogConnector.java
 *
 * `hire_logs` DAO
 *
 * <p>`hire_logs` CRUD</p>
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 03/08/2025
 *
 * <a href="https://github.com/ZouariOmar/HireLog/tree/main/project/src/main/java/com/mycompany/HireLog/dao/HireLogConnector.java">
 *  HireLogConnector.java
 * </a>
 */

// `HireLogConnector` pkg name
package com.mycompany.hirelog.dao;

// Java Sql core imports
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// Log4j java imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Custom java imports
import com.mycompany.hirelog.model.HireLog;
import com.mycompany.hirelog.view.LogTableUi;

// JavaFx imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HireLogConnector {
  private static final Logger _LOGGER = LogManager.getLogger(HireLogConnector.class);

  private static final String _CTREATE_QUERY = "INSERT INTO hire_logs (user_id, job_title, event_type, event_date, comments) VALUES(?, ?, ?, ?, ?)";

  private static final String _UPDATE_QUERY = "UPDATE hire_logs SET job_title = ?, event_type = ?, event_date = ?, comments = ? WHERE user_id = ? AND log_id = ?";

  private static final String _FETCH_ALL_QUERY = "SELECT * FROM hire_logs WHERE user_id = ?";

  private static final String _LAST_INSERTED_HIRE_LOGS_ID_QUERY = "SELECT last_insert_rowid() AS last_id FROM hire_logs";

  private static String _DELETE_HIRE_LOGS_PRE_QUERY = "DELETE FROM hire_logs WHERE user_id = ? AND log_id in (";

  /**
   * @param hireLog
   */
  public static final void create(final HireLog hireLog) {
    PreparedStatement pstmt = null;

    try {
      pstmt = DatabaseManager.connect().prepareStatement(_CTREATE_QUERY);

      pstmt.setInt(1, hireLog.userId());
      pstmt.setString(2, hireLog.jobTitle());
      pstmt.setString(3, hireLog.eventType());
      pstmt.setDate(4, hireLog.date());
      pstmt.setString(5, hireLog.comments());

      pstmt.executeUpdate();
      _LOGGER.info("`HireLogConnector#create` query executed successfully!");

    } catch (final SQLException e) {
      _LOGGER.error("`HireLogConnector#create` query Failed!");
      e.printStackTrace();

    } finally {
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param jobTracker
   */
  public static final void update(final HireLog hireLog) {
    PreparedStatement pstmt = null;

    try {
      pstmt = DatabaseManager.connect().prepareStatement(_UPDATE_QUERY);

      pstmt.setString(1, hireLog.jobTitle());
      pstmt.setString(2, hireLog.eventType());
      pstmt.setDate(3, hireLog.date());
      pstmt.setString(4, hireLog.comments());
      pstmt.setInt(5, hireLog.userId());
      pstmt.setInt(6, hireLog.logId());

      pstmt.executeUpdate();
      _LOGGER.info("`com.mycompany.hirelog.dao.HireLogConnector#update` query executed successfully!");

    } catch (final SQLException e) {
      _LOGGER.error("`com.mycompany.hirelog.dao.HireLogConnector#update` query Failed!");
      e.printStackTrace();

    } finally {
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param userId
   * @return
   */
  public static final ObservableList<LogTableUi> fetchAll(final int userId) {
    final ObservableList<LogTableUi> hireLogs = FXCollections.observableArrayList();
    PreparedStatement pstmt = null;
    ResultSet res = null;

    try {
      pstmt = DatabaseManager.connect().prepareStatement(_FETCH_ALL_QUERY);

      pstmt.setInt(1, userId);

      res = pstmt.executeQuery();
      _LOGGER.info("`HireLogConnector#fetchAll` query executed successfully!");

      while (res.next()) {
        final LogTableUi hireLog = new LogTableUi(new HireLog(
            res.getInt("log_id"),
            res.getInt("user_id"),
            res.getString("job_title"),
            res.getString("event_type"),
            res.getDate("event_date"),
            res.getString("comments")));
        hireLogs.add(hireLog);
      }

    } catch (final SQLException e) {
      _LOGGER.error("`HireLogConnector#fetchAll` query Failed!");
      e.printStackTrace();

    } finally {
      try {
        if (pstmt != null)
          pstmt.close();
        if (res != null)
          res.close();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }

    return hireLogs;
  }

  /**
   * @return {@code int}
   */
  public static final int getLastInsertedId() {
    try (ResultSet rs = DatabaseManager.connect().createStatement().executeQuery(_LAST_INSERTED_HIRE_LOGS_ID_QUERY)) {
      if (rs.next())
        return rs.getInt("last_id");

    } catch (final SQLException e) {
      _LOGGER.error("`HireLogConnector#getLastInsertedId` query Failed!");
      e.printStackTrace();
    }

    return -1; // Query failed
  }

  /**
   * `hire_logs` db delete action
   *
   * <p>
   * - We take `user_id` and the list of `log_id` that we wanna to delete
   * - This method make us delete many rows in one query
   * </p>
   *
   * @param userId      {@code final int}
   * @param identifiers {@code final List<Integer>}
   *
   * @see com.mycompany.hirelog.controller.DashboardController#onDeleteBtnAction
   *
   *      <pre>
   * {@code
   * HireLogConnector.delete(userId, identifiers);
   * }</pre>
   */
  public static final void delete(final int userId, final List<Integer> identifiers) {
    final int len = identifiers.size();
    if (len == 0) { // isEmpty (imposible to be empty, because the delete button is disabled)
      _LOGGER.warn("com.mycompany.hirelog.dao.HireLogConnector#delete : identifiers list empty!");
      return;
    }

    // Build the query
    final StringBuilder queryBuilder = new StringBuilder(_DELETE_HIRE_LOGS_PRE_QUERY);
    queryBuilder.append("?");
    for (int i = 1; i < len; ++i)
      queryBuilder.append(", ?");
    queryBuilder.append(")");

    PreparedStatement pstmt = null;
    final String finalDeleteQuery = queryBuilder.toString();
    try {
      pstmt = DatabaseManager.connect().prepareStatement(finalDeleteQuery);
      pstmt.setInt(1, userId);
      for (int i = 0; i < len; ++i) {
        pstmt.setInt(i + 2, identifiers.get(i));
      }
      pstmt.executeUpdate();
      _LOGGER.info("`com.mycompany.hirelog.dao.HireLogConnector#delete` query executed successfully!");

    } catch (final SQLException e) {
      _LOGGER.error("`com.mycompany.hirelog.dao.HireLogConnector#delete` query Failed! : {}", finalDeleteQuery);
      e.printStackTrace();

    } finally {
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }
}

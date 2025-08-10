/**
 * UserHelper.java
 *
 * `users` DAO
 *
 * <p>`users` CRUD</p>
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 28/07/2025
 * 
 * <a href="https://github.com/ZouariOmar/HireLog/blob/main/project/src/main/java/com.mycompany.hirelog/util/UserConnector.java">
 *  UserConnector.java
 * </a> 
 */

// `UserConnector` pkg name
package com.mycompany.hirelog.dao;

// Core java imports
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

// Log4j java imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// BCrypt java import
import org.springframework.security.crypto.bcrypt.BCrypt;

// Java custom imports
import com.mycompany.hirelog.model.User;

public final class UserConnector {
  private static final Logger _LOGGER = LogManager.getLogger(UserConnector.class);

  private static final String _IS_USER_QUERY = "SELECT * FROM users WHERE username = ?";

  private static final String _AVAILBLE_USER_ID_QUERY = "SELECT MAX(rowid) AS next_id FROM users";

  private static final String _IS_EXISTED_USER_EMAIL_QUERY = "SELECT email from users WHERE email = ?";

  private static final String _CREATE_USER_QUERY = "INSERT INTO users (username, password, email) VALUES(?, ?, ?)";

  private static final String _UPDATE_USER_PASSWORD_QUERY = "UPDATE users set password = ? WHERE email = ?";

  private static final String _EMAIL_REGEX_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

  private static final String _PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

  /**
   * Verify the validity of the given `username` and `password` and return the
   * `user_id`
   *
   * <h1>About <code>isUser</code> method</h1>
   * NONE
   * </br>
   * <h2>Exceptions</h2>
   * <ul>
   * <li>SQLException</li>
   * <li>Exceptions</li>
   * </ul>
   *
   * @param username {@code final String}
   * @param password {@code final String}
   *
   * @return {@code int}
   *
   * @see https://www.tutorialspoint.com/sqlite/sqlite_java.htm
   *
   *      <pre>
   * {@code
   * if (UserHelper.isUser("admin", "admin") != -1)
   *  System.out.println("Passed!");
   * else
   *  System.err.println(":-)");
   * }</pre>
   */
  public final static int isUser(final String username, final String password) {
    ResultSet res = null;
    try (PreparedStatement pstmt = DatabaseManager.connect().prepareStatement(_IS_USER_QUERY)) {
      pstmt.setString(1, username);
      res = pstmt.executeQuery();
      _LOGGER.info("`isUser` query executed successfully!");
      if (res.next() && BCrypt.checkpw(password, res.getString("password")))
        return res.getInt("user_id");

    } catch (final SQLException e) {
      _LOGGER.error("`isUser` query Failed!");
      e.printStackTrace();
    } finally {
      try {
        if (res != null)
          res.close();
      } catch (final SQLException e) {
        e.printStackTrace();
      }
    }

    return -1;
  }

  public static final boolean isExistedEmail(final String email) {
    try (PreparedStatement pstmt = DatabaseManager.connect().prepareStatement(_IS_EXISTED_USER_EMAIL_QUERY)) {
      pstmt.setString(1, email);
      if (pstmt.executeQuery().next()) {
        _LOGGER.info("`createUser` query executed successfully!");
        return true;
      }

    } catch (final SQLException e) {
      _LOGGER.error("`createUser` query Failed!");
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Create new user account and return the generated `username`
   *
   * <p>
   * - Note: username = prename + name + lastAvailbleId
   * - Warr: if we found username like `testtest-1` that mean he is corrupted
   * (getLastAvailbleId() failed)
   * </p>
   *
   * @param prename  {@code String}
   * @param name     {@code String}
   * @param email    {@code String}
   * @param password {@code String}
   * @return {@code String}
   *
   * @see Database
   *
   *      <pre>
   * {@code
   * String username = UserHelper.createUser(
   * entredPrename,
   * entredName,
   * entredEmail,
   * entredPassword);
   *
   * if (username != null) // SignUp success!
   * else                  // :)
   * }</pre>
   */
  public static final String createUser(final User user) {
    final String username = user.username().toLowerCase() + Integer.toString(getAvailbleUserId());

    try (PreparedStatement pstmt = DatabaseManager.connect().prepareStatement(_CREATE_USER_QUERY)) {
      pstmt.setString(1, username); // If `username` null, it will be catch it by `SQLException`
      pstmt.setString(2, BCrypt.hashpw(user.password(), BCrypt.gensalt()));
      pstmt.setString(3, user.email());
      pstmt.executeUpdate(); // Use executeUpdate() — not executeQuery() — for INSERT, UPDATE, or DELETE.
      _LOGGER.info("`createUser` query executed successfully!");
      return username; // Return `username`

    } catch (final SQLException e) {
      _LOGGER.error("`createUser` query Failed!");
      e.printStackTrace();
    }

    return null; // dummy
  }

  public static final void updatePassword(final String newPassword, final String email) {
    try (PreparedStatement pstmt = DatabaseManager.connect().prepareStatement(_UPDATE_USER_PASSWORD_QUERY)) {
      pstmt.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
      pstmt.setString(2, email);
      pstmt.executeUpdate();

    } catch (final SQLException e) {
      _LOGGER.error("`updatePassword` query Failed!");
      e.printStackTrace();
    }

    _LOGGER.info("`updatePassword` query executed successfully!");
  }

  /**
   * Verify the validity of the passed `email`
   *
   * <p>
   * None
   * </p>
   *
   * @param email {@code String}
   * @return {@code boolean}
   *
   * @see #isPassword
   * 
   *      <pre>
   * {@code
   * if (UserHelper.isEmail("test@test.test")
   *  // Passed
   * else
   *  // :)
   * }</pre>
   */
  public static final boolean isEmail(final String email) {
    return Pattern.compile(_EMAIL_REGEX_PATTERN, Pattern.CASE_INSENSITIVE).matcher(email).matches();
  }

  /**
   * Verify the validity of the passed `password`
   *
   * <p>
   * ^ # start-of-string
   * (?=.*[0-9]) # a digit must occur at least once
   * (?=.*[a-z]) # a lower case letter must occur at least once
   * (?=.*[A-Z]) # an upper case letter must occur at least once
   * (?=.*[@#$%^&+=]) # a special character must occur at least once
   * (?=\S+$) # no whitespace allowed in the entire string
   * .{8,} # anything, at least eight places though
   * $ # end-of-string
   * </p>
   *
   * @param password {@code String}
   * @return {@code boolean}
   *
   * @see #isEmail
   * 
   *      <pre>
   * {@code
   * if (UserHelper.isPassword("test")
   *  // Passed
   * else
   *  // :)
   * }</pre>
   */
  public static final boolean isPassword(final String password) {
    return Pattern.compile(_PASSWORD_REGEX_PATTERN, Pattern.CASE_INSENSITIVE).matcher(password).matches();
  }

  /**
   * Get an availble id based on `ROWID`
   *
   * <p>
   * None
   * </p>
   *
   * @return {@code int}
   * @see #createUser
   *
   *      <a href="https://www.sqlite.org/rowidtable.html">rowidtable</a>
   *
   *      <pre>
   * {@code
   * String username = prename.toLowerCase() + name.toLowerCase() + Integer.toString(getLastAvailbleId());
   * }</pre>
   */
  private static final int getAvailbleUserId() {
    try (ResultSet rs = DatabaseManager.connect().prepareStatement(_AVAILBLE_USER_ID_QUERY)
        .executeQuery()) {
      if (rs.next())
        return rs.getInt("next_id");

    } catch (final SQLException e) {
      _LOGGER.error("`getLastAvailbleId` query Failed!");
      e.printStackTrace();
    }

    return -1;
  }
} // UserHelper class

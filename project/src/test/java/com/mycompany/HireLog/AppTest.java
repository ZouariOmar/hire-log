package com.mycompany.HireLog;

// import com.mycompany.hirelog.service.MailSenderService;
// import com.mycompany.hirelog.service.PasswordGeneratorService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  /**
   * Test the mail service
   */
  public static void testMailSenderService() {
    // MailSenderService.send(
    // "zouariomar20@gmail.com",
    // "do-not-reply - Reset Password",
    // "Reset Code: " + PasswordGeneratorService.generate(10));
  }

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }
}

/**
 * MailSenderService.java
 *
 * Objective: Send the new password to our client mail
 *
 * <p>None</p>
 *
 * @author @ZouariOmar (zouariomar20@gmail.com)
 * @version 1.0
 * @since 08/08/2025
 *
 * <a href="https://www.simplejavamail.org" >simplejavamail</a>
 */

// `MailSenderService` pkg name
package com.mycompany.hirelog.service;

// Core java imports
import java.util.Properties;

// `log4j` imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// `simplejavamail` imports
import org.simplejavamail.MailException;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class MailSenderService {
  private static final Logger _LOGGER = LogManager.getLogger(MailSenderService.class);

  public static final void send(
      final Properties emailProperties,
      final String to,
      final String subject,
      final String htmlText) {
    try {
      MailerBuilder
          .withSMTPServer(
              emailProperties.getProperty("SMTP_HOST"),
              Integer.parseInt(emailProperties.getProperty("SMTP_PORT", "587")),
              emailProperties.getProperty("SMTP_USERNAME"),
              emailProperties.getProperty("SMTP_TOKEN"))
          .withTransportStrategy(TransportStrategy.SMTP_TLS)
          .buildMailer() // Building the Mailer
          .sendMail( // Send the mail
              EmailBuilder
                  .startingBlank()
                  .from("Hire Log", emailProperties.getProperty("SMTP_USERNAME"))
                  .to(to)
                  .withSubject(subject)
                  .withHTMLText(htmlText)
                  .buildEmail() // Building the Email
          );

    } catch (final MailException e) {
      e.printStackTrace();
      _LOGGER.warn("com.mycompany.hirelog.service.MailSenderService#send - Email sending error!");
    }
  }
} // MailSenderService

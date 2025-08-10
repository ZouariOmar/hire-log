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

// `log4j` imports
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// `simplejavamail` imports
import org.simplejavamail.MailException;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

// `dotenv` imports
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class MailSenderService {
  private static final Logger _LOGGER = LogManager.getLogger(MailSenderService.class);

  public static final void send(final String to, final String subject, final String htmlText) {
    try {
      Dotenv dotenv = Dotenv.load();
      MailerBuilder
          .withSMTPServer(
              dotenv.get("SMTP_HOST"),
              Integer.parseInt(dotenv.get("SMTP_PORT")),
              dotenv.get("SMTP_USERNAME"),
              dotenv.get("SMTP_TOKEN"))
          .withTransportStrategy(TransportStrategy.SMTP_TLS)
          .buildMailer() // Building the Mailer
          .sendMail( // Send the mail
              EmailBuilder
                  .startingBlank()
                  .from("Hire Log", dotenv.get("SMTP_USERNAME"))
                  .to(to)
                  .withSubject(subject)
                  .withHTMLText(htmlText)
                  .buildEmail() // Building the Email
          );
    } catch (final DotenvException e) {
      e.printStackTrace();
      _LOGGER.warn("com.mycompany.hirelog.service.MailSenderService#send - `.env` loading error!");
    } catch (final MailException e) {
      e.printStackTrace();
      _LOGGER.warn("com.mycompany.hirelog.service.MailSenderService#send - Email sending error!");
    }
  }
} // MailSenderService

package email;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import craigslist.Result;
import craigslist.Search;
import util.ConfigManager;

/**
 * Class with static methods for sending email.
 */
public class Email {
  
  /**
   * Send an enrollment email for a new search.
   * 
   * @param search search object
   * @param results list of results
   */
  public static void sendEnrollmentEmail(Search search, List<Result> results) {
    String recipient = search.getEmailAddress().toString();
    String subject = "You are now following a Craigslist search: " + search.getName();
    
    // Create the message
    StringBuilder messageBuilder = new StringBuilder();
    messageBuilder.append("<p>You have created a new search: " + search.getSearchURL().toString() + "</p>");
    
    // Add the unsubscribe link
    String unsubscribeLink = ConfigManager.getInstance().getBaseURL() + ConfigManager.getInstance().getContextPath() + "/Unsubscribe?uuid=" + search.getUUID();
    messageBuilder.append("<p><a href='" + unsubscribeLink + "'>Click here</a> to unsubscribe.</p>");
    
    // If there are existing results, add them to the message
    if (results == null) {
        messageBuilder.append("<p>We weren't able to find any current results for that search time. We will try again in " + search.getFrequency().getDescription() + "</p>");
    } else {
    messageBuilder.append("<p>Here are some recent search results:</p>");
        for (Result result : results) {
            messageBuilder.append("<p><a href='" + result.getLink() + "'>" + result.getTitle() + "</a><br>");
        }
    }
    
    sendEmail(recipient, subject, messageBuilder.toString());
  }
  
  /**
   * Send an email with recent search results.
   * 
   * @param search search object
   * @param results list of results
   */
  public static void sendResultsEmail(Search search, List<Result> results) {
    // Do nothing is there are no results 
    if (results == null) {
      return;
    }
    
    // Create the message
    String recipient = search.getEmailAddress().toString();
    String subject = "New results for your Craigslist search: " + search.getName();
    StringBuilder messageBuilder = new StringBuilder();
    messageBuilder.append("<p>We found some new results for the search you were following:</p>");

    // Add all the results
    for (Result result : results) {
      messageBuilder.append("<p><a href='" + result.getLink() + "'>" + result.getTitle() + "</a><br>");
    }
    
    // Add the unsubscribe link
    String unsubscribeLink = ConfigManager.getInstance().getBaseURL() + ConfigManager.getInstance().getContextPath() + "/Unsubscribe?uuid=" + search.getUUID();
    messageBuilder.append("<p><a href='" + unsubscribeLink + "'>Click here</a> to unsubscribe.</p>");
    
    sendEmail(recipient, subject, messageBuilder.toString());
  }

  /**
   * Send an email. 
   * 
   * @param recipient recipient email address
   * @param subject subject
   * @param messageText message (can be HTML)
   */
  private static void sendEmail(String recipient, String subject, String messageText) {
    // Create the properties for the session
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");
    properties.put("mail.debug", "false");

    // Get the email settings from the config manager
    ConfigManager configManager = ConfigManager.getInstance();
    String username = configManager.getEmailUsername();
    String password = configManager.getEmailPassword();
    String name = configManager.getEmailName();

    // Create the session
    Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
    session.setDebug(false);

    // Send the message
    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username, name));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
      message.setSubject(subject);
      message.setText(messageText, "utf-8", "html");
      Transport.send(message);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
  }

  /**
   * Check whether an email address is valid.
   * @param address email address string
   * @return true if the string is a valid email address, false otherwise
   */
  public static boolean isAddressValid(String address) {
    try {
      InternetAddress emailAddr = new InternetAddress(address);
      emailAddr.validate();
      return true;
    } catch (AddressException ex) {
      return false;
    }
  }

}

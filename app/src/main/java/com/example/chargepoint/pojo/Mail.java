package com.example.chargepoint.pojo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail {

    private static Message message;

    public static void sendMail(String to, Receipt receipt) {

        final String username = "aarthur.francois@gmail.com";
        final String password = "****";

        final String host = "smtp.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from-email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject("Receipt");

            Date timeToDate = receipt.getDatetime().toDate();
            String time = String.valueOf(timeToDate.getTime());

            Date date = receipt.getDatetime().toDate();
            String pattern = "MMMM dd, yyyy";
            DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
            String dateString = df.format(date);

            BodyPart bodyPart = new MimeBodyPart();
            String html = "<table style='border: 2px solid black; border-collapse: collapse;'>\n" +
                    "    <thead >\n" +
                    "        <tr style='border-bottom-style: double;'>\n" +
                    "            <th colspan=\"2\">Receipt from : " + date + "</th>\n" +
                    "        </tr>\n" +
                    "    </thead>\n" +
                    "    <tbody>\n" +
                    "        <tr >\n" +
                    "            <td style='border: 1px solid black;'>Location </td>\n" +
                    "            <td style='border: 1px solid black;'>" + receipt.getGeopoint() + "</td>\n" +
                    "        </tr>\n" +
                    "              <tr>\n" +
                    "            <td style='border: 1px solid black;'>MapId </td>\n" +
                    "            <td style='border: 1px solid black;'>" + receipt.getMap_id() + "</td>\n" +
                    "        </tr>\n" +
                    "              <tr>\n" +
                    "            <td style='border: 1px solid black;'>Card </td>\n" +
                    "            <td style='border: 1px solid black;'>" + receipt.getCard() + "</td>\n" +
                    "        </tr>\n" +
                    "              <tr>\n" +
                    "            <td style='border: 1px solid black;'>Consumption</td>\n" +
                    "            <td style='border: 1px solid black;'>" + receipt.getElectricity() + "kW</td>\n" +
                    "        </tr>\n" +
                    "         <tr style='border-top-style: double;'>\n" +
                    "            <td style='border: 1px solid black;'>Amount : </td>\n" +
                    "            <td style='border: 1px solid black;'>&euro; " + receipt.getCost() + "</td>\n" +
                    "        </tr>\n" +
                    "    </tbody>\n" +
                    "</table>";
            bodyPart.setContent(html, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);

            message.setContent(multipart);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                        System.out.println("Sent !");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

        }  catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }
}

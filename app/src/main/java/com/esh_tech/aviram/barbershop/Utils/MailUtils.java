package com.esh_tech.aviram.barbershop.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.data.Config;
import com.esh_tech.aviram.barbershop.R;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
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

public class MailUtils {
//
//    public static String fromUserName = "irit.lovenberg@gmail.com";
//    public static String fromPassword = "160876";
    static String myEmail;
    static String password;


    public static final String TAG = "TestMails";

    private static Properties SMTPprops = new Properties();
     static
    {
        SMTPprops.put("mail.smtp.auth", "true");
        SMTPprops.put("mail.smtp.starttls.enable", "true");
        SMTPprops.put("mail.smtp.host", "smtp.gmail.com");
        SMTPprops.put("mail.smtp.port", "587");
     }


    public static boolean sendMail(Context context, String targetEmail, String attachmentFilePath)
    {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Session session = Session.getInstance(SMTPprops,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
//                        TODO Support email
                        return new PasswordAuthentication(settings.getString(UserDBConstants.USER_EMAIL,""),
                                settings.getString(UserDBConstants.USER_EMAIL_PASSWORD,""));
                    }
                });

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
        try
        {
            // first mail is to the Admin - order details
            Message message = new MimeMessage(session);
            message = constructMail(context, message,
                    new InternetAddress(settings.getString(UserDBConstants.USER_EMAIL,"")),
                    targetEmail, attachmentFilePath);

            Transport.send(message);



        }

        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;

    }



    public static Message constructMail(Context context, Message message,
                                        InternetAddress fromAddress, String toAddress, String attachmentFilePath)
    {
        String messageStr = "";
        Multipart multipart = new MimeMultipart();
        try
        {
            message.setFrom(fromAddress);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            //String messageBody = "<body align='right'>Report Attached</body>";
            message.setSubject(context.getString(R.string.MailCN_MailSubject));
            //messageStr += messageBody;

            // Create the message part
            //BodyPart messageBodyPart = new MimeBodyPart();
            //messageBodyPart.setText(messageStr);
            //multipart.addBodyPart(messageBodyPart);

            multipart = addAttachment(multipart , attachmentFilePath, "Report Attached");

            message.setContent(multipart, "text/html" );

        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return message;

    }

    public static Multipart addAttachment(Multipart multipartMail, String filename, String subject) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("Report.csv");
        multipartMail.addBodyPart(messageBodyPart);

        BodyPart messageBodyPart2 = new MimeBodyPart();
        messageBodyPart2.setText(subject);

        multipartMail.addBodyPart(messageBodyPart2);

        return multipartMail;
    }



}

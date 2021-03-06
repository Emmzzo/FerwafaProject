package ferwafa.common;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class GenerateNotificationTemplete {
 SendEmail sendMail=new SendEmail();
private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

public  String sendEmailNotification(String receiverEmail,String receiverNames ,String subJect,String notificationMassage ) {
	String head="";
	String footer="";
	String content="";
	head="<html>\n"
            + "<title>Mail</title>\n"
            + "<head>\n"
            + "<style>\n"
            + "body {\n"
            + "	font-family:\"Lucida Grande\", \"Lucida Sans Unicode\", \"Lucida Sans\", \"DejaVu Sans\", \"Verdana\", \"sans-serif\";\n"
            + "	font-weight: 400;\n"
            + "	color: #333;\n"
            + "	font-size:13px;\n"
            + "	line-height:1.4em;\n"
            + "	margin-left:20px;\n"
            + "	margin-top:10px;\n"
            + "}\n"
            + ".labelbold{font-weight:bold;}\n"
            + "table{font-size:13px; border-collapse: collapse;}\n"
            + "table, th, td  {border: 1px solid black;}\n"
            + "a {color: #00779a;}\n"
            + ".generated{font-size:12px; font-weight: 500;}\n"
            + ".footer{font-size:11px; text-align:justify;line-height:1.2em;}\n"
            + "</style>\n"
            + "\n"
            + "\n"
            + "<body><br/>"
            + "<b>Dear :</b>" +receiverNames + ""
            + " <br/><br/>";
           
	
	content="<b>"+notificationMassage+"</b>";
	
	
	footer= "<br/>"
           + "<br/>"
            + "Regards <br/>"
           + "This email was generated automatically. Please do not respond. "
            + "<br/>"
            + "DISCLAIMER<br/><p style=\"font-family: 'Courier New',Courier,mono;font-size: 10pt;\">The information contained in this email is intended to inform the recipient(s) and represents private, confidential or privileged content. This information should not be reproduced , redistributed or shared directly or indirectly in any form to any other person.\n"
            + "</p>\n"
            + "\n"
            + "</body>\n"
            + "</html>";
	
	String fullMail="";
	fullMail=head+content+footer;
	try {
		sendMail.sendEmail(receiverEmail, subJect, fullMail);
		LOGGER.info("Email notification sent successfull to::"+receiverEmail);
	} catch (AddressException e) {
		LOGGER.info("This content"+notificationMassage+" was not send to MY BE wrong address check email ::"+receiverEmail+" on "+timestamp);
		LOGGER.info("sendEmailNotification :: Notifaction Exception check email  ::"+e.getMessage());
		e.printStackTrace();
	} catch (MessagingException e) {
	LOGGER.info("This content"+notificationMassage+" was not send to ::"+receiverEmail+" on "+timestamp);
		LOGGER.info("sendEmailNotification :: Notifaction  MessagingException  ::"+e.getMessage());
		e.printStackTrace();
	}
	
	
	
	return "";
}


public static void main(String ...arg ) {
	//String receiverEmail,String receiverNames ,String subJect,String notificationMassage
	//sendEmailNotification("ngaboericngabo@gmail.com","Ngabo Eric","Test Email"," amakuru yawe");
	
}
}

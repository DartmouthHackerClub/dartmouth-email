import java.io.*;
import java.net.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;				

public class Spammer {
	public static String SMTP_SERVER = "mailhub.dartmouth.edu";
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Must specify 2 arguments:\n1) Filename of email to send\n2) Filename of list recipients");
			return;
		}
		String body = "";
		LinkedList<String> recipients = new LinkedList<String>();
		try {
			BufferedReader emailIn = new BufferedReader(new FileReader(args[0]));
			BufferedReader listIn = new BufferedReader(new FileReader(args[1]));
			
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = emailIn.readLine()) != null) {
				buffer.append(line+"\n");
			}
			body = buffer.toString();
			
			while ((line = listIn.readLine()) != null) {
				recipients.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error getting email or recipients - aborting...");
			return;
		}
		// System.out.println(body);
		// System.out.println(recipients);
		int counter = 0;
		Properties props=new Properties();
		props.put("mail.smtp.host", SMTP_SERVER);
		Session session=Session.getDefaultInstance(props,null);
		for(String recipient : recipients) {
			System.out.println(++counter + ":\t\t" + recipient);
			try {
				Message message=new MimeMessage(session);
				message.setFrom(new InternetAddress("dartmouthwiki@gmail.com", "Dartwiki"));
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				message.setSubject("dartwiki");
				message.setText(body);
				message.setSentDate(new Date());
				Transport.send(message);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}

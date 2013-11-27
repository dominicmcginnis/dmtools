package dmtools.util;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Session;
import javax.mail.Transport;

public class Email {

	private MimeMessage message = null;
	
	public Email() {
				
	}
	
	public void setupEmailClient(String mailHost, String fromAddress, String toAddress) {
        if(mailHost == null || mailHost.equals("") || toAddress == null || toAddress.equals("")) { 
        	return;
        }
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "SMTP");
        properties.put("mail.host", mailHost);
        
        Session session = Session.getDefaultInstance(properties);

        try {
        	message = new MimeMessage(session);

        	message.setFrom(new InternetAddress(fromAddress));        	
        	StringTokenizer st = new StringTokenizer(toAddress, ";");
        	while (st.hasMoreTokens()) {
        		String s = st.nextToken();
        		message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(s));
        	}

        } catch (Exception e) {
            e.printStackTrace();        	
        }
		
	}
	public void sendEmail(String subject, String body) {
		try {
			if(message != null) {
				message.setSubject(subject);
				message.setText(body);
    	
				Transport.send(message);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
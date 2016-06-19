package domain.message;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * Test entity message.
 * 
 * @author romain_mencattini
 *
 */

public class MessageTest {
	
	/**
	 * test the constructors , and some equality between instances.
	 */
	@Test
	public void shouldBeEqual() {
		Date date = new Date();
		Time time = Time.valueOf("00:00:00");
		Message m1 = new Message();
		Message m2 = new Message();
		
		Message m3 = new Message("test..1..2", time, date, (long) 1000, (long) 1001);
		Message m4 = new Message("test..1..2", time, date, (long) 1000, (long) 1001);
		
		// on regarde si les hash code sont les mêmes, et on regarde si l'override du equals fonctionne
		Assert.assertEquals("m1.hashCode() == m2.hashCode()", m1.hashCode(),m2.hashCode());
		Assert.assertEquals("m1 == m2 ", m1,m2);
		
		Assert.assertEquals("m3.hashCode() == m4.hashCode()", m3.hashCode(),m4.hashCode());
		Assert.assertEquals("m3 == m4 ", m3,m4);
		
		// on véfrifie pour le hashcode
		m1.setReceiverId((long) 1000);
		Assert.assertEquals("m1.hashCode() == m2.hashCode()", m1.hashCode(),m2.hashCode());
		
		m1.setMessageDate(null);
		Assert.assertEquals("m1.hashCode() == m2.hashCode()", m1.hashCode(),m2.hashCode());

		m1.setMessageTime(null);
		Assert.assertEquals("m1.hashCode() == m2.hashCode()", m1.hashCode(),m2.hashCode());

	}
	
	/**
	 * test some invitations and non-equality.
	 */
	@Test
	public void shouldBeNotEqual() {
		Time time = Time.valueOf("00:00:00");
		Date date = new Date();
		
		// on crée le même message
		Message m3 = new Message("test..1..2", time, date, (long) 1000, (long) 1001);
		Message m4 = new Message("test..1..2", time, date, (long) 1000, (long) 1001);
		
		//  on change un élément, et on vérifie que ce ne soit plus égal au sens du equals
		m3.setMessageId((long) 1000);
		Assert.assertFalse("m3.getMessageId() == m4.getMessageId())", m3.getMessageId() == m4.getMessageId());
		Assert.assertFalse("m3.equals(m4)",m3.equals(m4));
		m4.setMessageId((long) 1000);
		// on vérifie que c'est bien égal ensuite
		Assert.assertTrue("m3.equals(m4)",m3.equals(m4));
		
		// on change la date du message
		m3.setMessageDate(null);
		Assert.assertFalse("m3.getMessageDate() == m4.getMessageDate()",m3.getMessageDate() == m4.getMessageDate());
		Assert.assertFalse("m3.equals(m4)",m3.equals(m4));
		m4.setMessageDate(null);
		// on vérifie que le changement rndent les messages égaux
		Assert.assertTrue("m3.equals(m4)",m3.equals(m4));
		
		// on change l'heure du message
		m3.setMessageTime(null);
		Assert.assertFalse("m3.getMessageTime() == m4.getMessageTime()",m3.getMessageTime() == m4.getMessageTime());
		Assert.assertFalse("m3.equals(m4)",m3.equals(m4));
		m4.setMessageTime(null);
		// cela doit fonctionner après changement
		Assert.assertTrue("m3.equals(m4)",m3.equals(m4));
		
		// on change le text
		m3.setMessageText("test..1..2..3");
		Assert.assertFalse("m3.getMessageText() == m4.getMessageText()",m3.getMessageText() == m4.getMessageText());
		Assert.assertFalse("m3.equals(m4)",m3.equals(m4));
		m4.setMessageText("test..1..2..3");
		Assert.assertTrue("m3.equals(m4)",m3.equals(m4));
		
		// on change l'envoyeur
		m3.setSenderId((long) 1);
		Assert.assertFalse("m3.getSenderId() == m4.getSenderId()",m3.getSenderId() == m4.getSenderId());
		Assert.assertFalse("m3.equals(m4)",m3.equals(m4));
		m4.setSenderId((long) 1);
		Assert.assertTrue("m3.equals(m4)",m3.equals(m4));
		
		// pour le receveur
		m3.setReceiverId((long) 1);
		Assert.assertFalse("m3.getReceiverId() == m4.getReceiverId()",m3.getReceiverId() == m4.getReceiverId());
		Assert.assertFalse("m3.equals(m4)",m3.equals(m4));
		m4.setReceiverId((long) 1);
		Assert.assertTrue("m3.equals(m4)",m3.equals(m4));
		
		Assert.assertFalse("m3.equals(null) == false ", m3.equals(new Long(10)));

		
	}
	
	/**
	 * it checks if an entity has the right string (json) format
	 * @throws Exception
	 */
	@Test
	public void shouldToStringHaveTheRightForm() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Time time = Time.valueOf("00:00:00");
		
		Date date = dateFormat.parse("2020-12-5");
		Message m1 = new Message("test..1..2", time, date, (long) 1000, (long) 1001);
		
		// on vérifie que le retour en json est le même que celui espréré
		Assert.assertEquals("m1.toString()", 
				"{\"messageId\":null, \"messageText\":\"test..1..2\", \"messageTime\":\"00:00:00\", "
				+ "\"messageDate\":\"Sat Dec 05 00:00:00 CET 2020\", \"senderId\":1000, \"receiverId\":1001}",
						m1.toString());
		
	}
	
	/**
	 * test the behavior of copy methode. (the equality)
	 */
	@Test
	public void shouldCopyFromStudentBeEqualToSource() throws ParseException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Time time = Time.valueOf("00:00:00");
		
		Date date = dateFormat.parse("2020-12-5");
		Message m1 = new Message("test..1..2", time, date, (long) 1000, (long) 1001);
		Message m2 = new Message("....",time,date,(long)10293, (long) 1928);
		Message m3 = new Message();
		
		m2.copyBusinessFieldsFrom(m1);
		m3.copyBusinessFieldsFrom(m1);
		
		Assert.assertTrue("m1.equals(m2)",m1.equals(m2));
		Assert.assertTrue("m1.equals(m3)",m1.equals(m3));
	}

	/**
	 * we test the comparTo methode between two instances.
	 * Test if it behavior is correct in every situations.
	 */
	@Test
	public void testComparTo() throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = dateFormat.parse("2020-12-5");
		Time time = Time.valueOf("00:00:00");
		
		Message m3 = new Message("test..1..2", time, date, (long) 1000, (long) 1001);
		Message m4 = new Message("test..1..2", time, date, (long) 1000, (long) 1001);
		
		m3.setMessageId(new Long(0));
		m4.setMessageId(new Long(4));
		
		Assert.assertEquals("m3.compareTo(m4) == -1", m3.compareTo(m4) , -1);
		Assert.assertEquals("m4.compareTo(m3) == 1",m4.compareTo(m3) , 1);
		Assert.assertEquals("m3.compareTo(m3) == 0", m3.compareTo(m3), 0);
	}
}

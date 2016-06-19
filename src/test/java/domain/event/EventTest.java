package domain.event;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.junit.Assert;
import org.junit.Test;

import domain.user.*;

/**
 * 
 * Test entity event.
 * 
 * @author romain_mencattini
 *
 */

public class EventTest {
	
	/**
	 * we test the constructor and the equality between two instances.
	 * Using hashcode and equals
	 */
	@Test
	public void shouldBeEqual() {
		Date date = new Date();
		Date birthDate = new Date();
		Time time = new Time(new Long(1234));
		
		// on crée un user
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",birthDate,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		
		Event e1 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 175.56, (float) -39.26, u1.getUserId());
		Event e2 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 175.56, (float) -39.26, u1.getUserId());
		
		// on regarde si les hash code sont les mêmes, et on regarde si l'override du equals fonctionne
		Assert.assertEquals("e1=e2", e1.hashCode(),e2.hashCode());
		Assert.assertEquals("e1=e2", e1,e2);
		// on change la description de l'un
		e1.setDescription("Description de l'événement 1");
		
		// on vérifie que le hashcode et le equals régaissent bien
		Assert.assertEquals("e1=e2",e1.hashCode(),e2.hashCode());
		Assert.assertEquals("e1=e2",e1,e2);
		
		// on vérifie si on change le flags des messages
		e1.setLat((float)123.00);
		e2.setLng((float)137.00);
		
		Assert.assertEquals("e1=e2",e1.hashCode(),e2.hashCode());
		Assert.assertEquals("e1=e2",e1,e2);
		
		e1.setEvtOwnerName("Romain");
		e2.setEvtOwnerName("Romain");
		
		Assert.assertTrue("e1.getEvtOwnerName() == e2.getEvtOwnerName()", e1.getEvtOwnerName().equals(e2.getEvtOwnerName()));
		
	}
	
	
	/**
	 * we test the comparTo methode between two instances.
	 * Test if it behavior is correct in every situations.
	 */
	@Test
	public void testComparTo(){
		
		Date birthDate = new Date();
		Date date = new Date();
		
		Time time = new Time(new Long(1234));
		
		// on crée le même user
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",birthDate,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		
		Event e1 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 175.56, (float) -39.26, u1.getUserId());
		Event e2 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 175.56, (float) -39.26, u1.getUserId());
	
		e1.setEvtId(new Long(12));
		e2.setEvtId(new Long(23));
		
		Assert.assertEquals("e1.comparto(e2) == -1 ", e1.compareTo(e2),-1);
		Assert.assertEquals("e2.compareTo(e1) == -1 ", e2.compareTo(e1), 1);
		
		e2.setEvtId(new Long(1));
		e1.setEvtId(new Long(1));
		
		Assert.assertEquals("e1.comparto(e2) == 0 ", e1.compareTo(e2), 0);
		
		
	}
	
	/**
	 * test in some cases, that entities aren't the same.
	 */
	@Test
	public void shouldBeNotEqual() {
		
		Date birthDate = new Date();
		Date date = new Date();
		
		Time time = new Time(new Long(1234));
		
		// on crée le même user
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",birthDate,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		
		Event e1 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 175.56, (float) -39.26, u1.getUserId());
		Event e2 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 175.56, (float) -39.26, u1.getUserId());
		
		e1.setEvtId((long) 129384);
		
		Assert.assertFalse("e1 = e2", e1.equals(e2));
		Assert.assertFalse("e1 = e2", e1.hashCode() == e2.hashCode());
	
		e2.setEvtId((long) 129384);
		
		Assert.assertTrue("e1 = e2", e1.equals(e2));
		
		e1.setEvtName("new event name");
		
		Assert.assertFalse("e1 = e2", e1.equals(e2));
		Assert.assertFalse("e1 = e2", e1.hashCode() == e2.hashCode());
		
		Assert.assertFalse("e1.equals(null) == false ", e1.equals(new Long(10)));
		
	}
	
	/**
	 * it checks if an entity has the right string (json) format
	 * @throws Exception
	 */
	@Test
	public void shouldToStringHaveTheRightForm() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = dateFormat.parse("2020-12-5");
		
		Time time = Time.valueOf("17:22:03");
		
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",date,"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		
		Event e1 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 175.56, (float)-39.26, u1.getUserId());

		// on vérifie que le retour en json est le même que celui espréré
		Assert.assertEquals("s1.toString()", 
				"{\"evtId\":null, \"evtName\":\"Rencontre de volley\", \"sport\":\"volley\", \"evtDate\":\"Sat Dec 05 00:00:00 CET 2020\","
				+ " \"evtTime\":\"17:22:03\", \"adress\":\"21 route de chêne\", \"lng\":175.56, \"lat\":-39.26"
				+ ", \"description\":\"null\", \"evtLevel\":0, \"maxSubscribers\":0, \"evaluation\":0}",
						e1.toString());
		
	}
	
	/**
	 * test the behavior of copy methode. (the equality)
	 */
	@Test
	public void shouldCopyFromStudentBeEqualToSource() {

		Date date = new Date();
		Time time = new Time(new Long(1234));
		
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",new Date(),"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);

		Event e1 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 135.56, (float) -39.26, u1.getUserId());
		Event e2 = new Event("Rencontre de tennis", "squash", date, time, "21 route de chêne", (float) 175.56, (float) -34.26, u1.getUserId());
		Event e3 = new Event();

		e1.copyBusinessFieldsFrom(e2);
		e3.copyBusinessFieldsFrom(e1);
		
		Assert.assertEquals("e1=e2", e1, e2);
		Assert.assertEquals("e1 = e2", e1.hashCode(),e2.hashCode());
		Assert.assertEquals("e3 = e1, hashcode",e1.hashCode(),e3.hashCode());
		Assert.assertEquals("e3 = e1, equals",e1,e3);
		
	}
	
	/**
	 * test the id of a new event. It must be null.
	 */
	@Test 
	public void shoudlIdBeNull(){
		Date date = new Date();
		Time time = new Time(new Long(1234));
		
		User u1 = new User("Romain Mencattini","mrl@bluewin.ch",new Date(),"http://une.addresse.au.bol.ch","Description de Romain Mencattini",true);
		Event e1 = new Event("Rencontre de volley", "volley", date, time, "21 route de chêne", (float) 135.56, (float) -39.26, u1.getUserId());
		
		Assert.assertEquals("e1.userId == null", e1.getEvtId(), null );
	}
	
}
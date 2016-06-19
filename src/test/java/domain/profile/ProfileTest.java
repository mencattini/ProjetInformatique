package domain.profile;

import java.util.Date;
import org.junit.Test;
import org.junit.Assert;

/**
 * 
 * Class to test the entity Profile
 * 
 * @author romain
 *
 */

public class ProfileTest {
	
	/**
	 * test the empty constructor and the constructor with parameters. (check behavior).
	 */
	@Test
	public void testProfile(){
		
		Date date = new Date();
		
		Profile p1 = new Profile();
		Profile p2 = new Profile("Romain Mencattini","email@gmail.com",date,"passwd");
		
		// we test taht the constructor works
		Assert.assertTrue("p1 != null", p1 != null);
		// we test the attribut of p2
		Assert.assertEquals("p2.getUserName() == Romain Mencattini", p2.getUserName(), "Romain Mencattini");
		Assert.assertEquals("p2.getEmail() == email@gmail.com", p2.getEmail(), "email@gmail.com");
		Assert.assertEquals("p2.getDate() == date", p2.getBirthDate(), date);		
	}
	
	/**
	 * 
	 * Method to test the overrided equals method
	 * 
	 */
	@Test
	public void testEquals(){
		
		Date date = new Date();
		
		Profile p1 = new Profile("Romain Mencattini","email@gmail.com",date,"passwd");
		Profile p2 = new Profile("Romain Mencattini","email@gmail.com",date,"passwd");
		
		// trivial
		Assert.assertTrue("p1.equals(p1) == true",p1.equals(p1));
		// more complicated
		Assert.assertTrue("p1.equals(p2) == true", p1.equals(p2));
		// false
		Assert.assertFalse("p1.equals(null) == false", p1.equals(null));
		// false
		Assert.assertFalse("p1.equals(new Profile()) == false", p1.equals(new Profile()));
		
	}
	
	/** 
	 * test the validity and the creation of hashcode
	 * 
	 */
	@Test
	public void testHashCode(){
		Date date = new Date();
		
		Profile p1 = new Profile("Romain Mencattini","email@gmail.com",date, "passwd");
		Profile p2 = new Profile("Romain Mencattini","email@gmail.com",date, "pa");
		Profile p3 = new Profile("Romain Mencattini","com",date, "passwd");
		
		Assert.assertEquals("p1.hashcode() == p1.hashcode()", p1.hashCode(), p1.hashCode());
		
		Assert.assertNotEquals("p1.hashcode() != p2.hashcode()", p1.hashCode(), p2.hashCode());
		
		Assert.assertNotEquals("p1.hashcde() != p3.hashcode()", p1.hashCode(), p3.hashCode());
	}
}
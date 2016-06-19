package domain.credential;

import org.junit.Test;
import org.junit.Assert;

/**
 * 
 * Test class for entity Credential
 * 
 * @author romain
 *
 */

public class CredentialTest{
	
	/**
	 * We test if the empty constructor works, then we add value
	 * with setteur.
	 */
	@Test
	public void testCredential(){
		
		Credential c1 = new Credential();
		
		c1.setEmail("addres@email.com");
		c1.setPasswd("passwd");
		c1.setRole("user");
		
		Credential c2 = new Credential("addres@email.com","passwd","user");
		
		// the two entity must be the same
		Assert.assertEquals("c1 == c2", c1,c2);
		// we test the value of credential with getteur
		Assert.assertEquals("c1.getEmail() == addres@email.com", c1.getEmail(),"addres@email.com");
		Assert.assertEquals("c1.getPasswd() == passwd", c1.getPasswd(),"passwd");
		Assert.assertEquals("c1.getRole() == user",c1.getRole(),"user");
		// we change the value and compare if it's false
		// c2 Email
		String c2B = c2.getEmail();
		c2.setEmail("HackTheWorld@net.work");
		Assert.assertFalse("c1 == c2",c1.equals(c2));
		c2.setEmail(c2B);
		// c2 passd
		c2B = c2.getPasswd();
		c2.setPasswd("ThsForThisDB");
		Assert.assertFalse("c1 == c2",c1.equals(c2));
		c2.setPasswd(c2B);
		// c2 Role
		c2B = c2.getRole();
		c2.setRole("AdminXD");
		Assert.assertFalse("c1 == c2",c1.equals(c2));
		c2.setRole(c2B);
		// Check backup
		Assert.assertTrue("c1 == c2",c1.equals(c2));
	}
	
	/**
	 * 
	 * We test the overrided method equals.
	 */
	@Test
	public void testEquals(){
		Credential c1 = new Credential("addres@email.com","passwd","user");
		Credential c2 = new Credential("addres@email.com","passwd","user");
		
		// trivial:
		Assert.assertTrue("c1.equals(c1) == true", c1.equals(c1));
		// more complicated
		Assert.assertTrue("c1.equals(c2) == true", c1.equals(c2));
		// false
		Assert.assertFalse("c1.equals(null) == false", c1.equals(null));
	}
	
	/**
	 * 
	 * We test the copybuisness
	 */
	@Test
	public void testCopyBuisnessForFields(){
		Credential c1 = new Credential("addres@email.com","passwd","user");
		Credential c2 = new Credential();
		
		c2.copyBusinessFieldsFrom(c1);
		
		Assert.assertEquals("c1 == c2 ", c1, c2);
	}
	
}
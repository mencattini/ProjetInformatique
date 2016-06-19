package domain.subscription;

import org.junit.Test;
import org.junit.Assert;

/**
 * 
 * Class to test the subscription class
 * 
 * @author romain
 *
 */

public class SubscriptionTest{
	
	/**
	 * 
	 * Check if the constructors are right.
	 * 
	 */
	@Test
	public void testSubscription(){
		
		Long userId = new Long(1);
		Long evtId = new Long(100);
		
		Subscription s1 = new Subscription(userId,evtId);
		Subscription s2 = new Subscription(userId,evtId);
		
		Assert.assertEquals("s1 == s2", s1,s2);
		
		s1 = new Subscription();
		s2 = new Subscription();
		
		Assert.assertEquals("s1 == s2", s1,s2);
	}
	
	/**
	 * Check for the getteur/setteur
	 */
	@Test
	public void testGetSet(){
		Long userId = new Long(1);
		Long evtId = new Long(100);
		
		Subscription s1 = new Subscription(userId,evtId);
		s1.setEventId(new Long(200));
		s1.setUserId(new Long(2));
		
		Assert.assertEquals("s1.getEvtId == 200", s1.getEventId(), new Long(200));
		Assert.assertEquals("s1.getUserId() == 2", s1.getUserId(), new Long(2));
		
	}
	
	/**
	 * 
	 * Check the override of equals
	 * 
	 */
	@Test
	public void testEqualsAndNotEquals(){
		
		Long userId = new Long(1);
		Long evtId = new Long(100);
		
		Subscription s1 = new Subscription(userId,evtId);
		Subscription s2 = new Subscription(userId,evtId);
		
		Assert.assertTrue("s1.equals(s2) == true", s1.equals(s2));
		// we change the value and compare if it's false
		// s2 UserId
		Long idB = s2.getUserId();
		s2.setUserId(new Long(12));
		Assert.assertFalse("s1.equals(s2) == false", s1.equals(s2));
		s2.setUserId(idB);
		// Check backup
		Assert.assertTrue("s1.equals(s2) == true", s1.equals(s2));
		
		Assert.assertFalse("s1.equals(null) == false", s1.equals(null));
		
	}
	
	/**
	 * Test the hash code creation
	 */
	@Test
	public void testHashCode(){
		Long userId = new Long(1);
		Long evtId = new Long(100);
		
		Subscription s1 = new Subscription(userId,evtId);
		Subscription s2 = new Subscription(new Long(2),new Long(101));
		
		Assert.assertEquals("s1.hashcode() == s1.hashcode()", s1.hashCode(), s1.hashCode());
		
		Assert.assertNotEquals("s1.hashcode() != s2.hashcode()", s1.hashCode(), s2.hashCode());
		
	}
}
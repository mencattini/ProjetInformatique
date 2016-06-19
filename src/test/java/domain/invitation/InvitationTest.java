package domain.invitation;

import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;

import domain.invitation.Invitation;

/**
 * 
 * Test entity invitation.
 * 
 * @author Yan
 *
 */

public class InvitationTest{
	
	/**
	 * test the constructors, and some equality between instances.
	 */
	@Test
	@InSequence(1)
	public void shouldBeEqual() {
		
		Invitation i1 = new Invitation();
		Invitation i2 = new Invitation();
		Invitation i3 = new Invitation(new Long(1),new Long(1));
		Invitation i4 = new Invitation(new Long(1),new Long(1));
		
		// on regarde si les hash code sont les mêmes, et on regarde si l'override du equals fonctionne
		Assert.assertEquals("i1.hashCode() == i2.hashCode()", i1.hashCode(),i2.hashCode());
		Assert.assertEquals("i1 == i2 ", i1,i2);
		
		// on regarde si les hash code sont les mêmes, et on regarde si l'override du equals fonctionne
		Assert.assertEquals("i3.hashCode() == i4.hashCode()", i3.hashCode(),i4.hashCode());
		Assert.assertEquals("i3 == i4 ", i3,i4);
				
	}
	
	/**
	 * test some invitations and non-equality.
	 */
	@Test
	@InSequence(2)
	public void shouldBeNotEqual() {
		//Creation de 2 invitations identiques
		Invitation i5 = new Invitation(new Long(4),new Long(2));
		Invitation i6 = new Invitation(new Long(4),new Long(2));
		Assert.assertEquals("i5.equals(i6)",i5,i6);
		
		//on change un element et verifions que les elements ne sont pas identiques
		//on change EventId
		i5.setEventId(new Long(5));
		Assert.assertFalse("i5.getEventId == i6.getEventId",i5.getEventId().equals(i6.getEventId()));
		Assert.assertFalse("i5.equals(i6)",i5.equals(i6));
		i6.setEventId(new Long(5));
		Assert.assertTrue("i5.equals(i6)",i5.equals(i6));
		
		//Cas ou obj == null
		Assert.assertFalse("i5.equals(null)",i5.equals(null));
		
		//on change EventId
		i5.setInvitedId(new Long(6));
		Assert.assertFalse("i5.getInvitedId == i6.getInvitedId",i5.getInvitedId() == i6.getInvitedId());
		Assert.assertFalse("i5.equals(i6)",i5.equals(i6));
		i6.setInvitedId(new Long(6));
		Assert.assertTrue("i5.equals(i6)",i5.equals(i6));
		
	}
}
package service.invitation;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import domain.event.Event;
import domain.invitation.Invitation;
import domain.user.User;
import service.event.EventService;
import service.invitation.InvitationService;
import service.user.UserService;

@Stateless
@RunWith(Arquillian.class) 
public class InvitationServiceImplTest{
	
	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,Invitation.class.getPackage())
				.addPackages(true,InvitationService.class.getPackage())
				.addPackages(true,User.class.getPackage(),UserService.class.getPackage())
				.addPackages(true,Event.class.getPackage(),EventService.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}
	
	@Inject
	private InvitationService service;
	
	@Inject
	private EventService eventService;
	
	@Inject
	private UserService invitedService;
	
	@PersistenceContext
	EntityManager em;
	
	/**
	 * We test the result when we get some invitations for a given userId.
	 * @throws InvitationAlreadyExists
	 */
	@Test
	@InSequence(1)
	public void testInviteAndGetInvitationsForInvitedId() throws InvitationAlreadyExists {
		//Here an invitation is created and tested with getInvitationsForInvitedId()
		List<Invitation> l0 = service.getInvitationsForInvitedId(new Long(2));
		Assert.assertEquals("Size of l0 == 0",l0.size(),0);
		Invitation i1 = new Invitation(new Long(2),new Long(4));
		service.invite(i1);
		List<Invitation> l1 = service.getInvitationsForInvitedId(new Long(2));
		Assert.assertEquals("Size of l1 == 1",l1.size(),1);
		Assert.assertEquals("i1 == l1[1]",l1.get(0),i1);
	}
	
	/**
	 * We test the result when we get some invitations for a given event.
	 * @throws InvitationAlreadyExists
	 */
	@Test
	@InSequence(2)
	public void testGetInvitationsForEventId() throws InvitationAlreadyExists {
		//Here is tested getInvitationsForEventId() then we add an invitation
		List<Invitation> l1 = service.getInvitationsForEventId(new Long(4));
		Assert.assertEquals("Size of l1 == 1",l1.size(),1);
		Invitation i2 = new Invitation(new Long(34),new Long(4));
		service.invite(i2);
		List<Invitation> l2 = service.getInvitationsForEventId(new Long(4));
		Assert.assertEquals("Size of l2 == 2",l2.size(),2);
		Assert.assertEquals("i2 == l2[2]",l2.get(1),i2);
	}
	
	/**
	 * We test the result when we get some events for a given invitation.
	 * @throws InvitationAlreadyExists
	 */
	@Test
	@InSequence(3)
	public void testGetEventIdsForInvited() throws InvitationAlreadyExists {
		//Here is tested getEventIdsForInvited() then we add an invitation
		List<Long> l1 = service.getEventIdsForInvited(new Long(5));
		Assert.assertEquals("Size of l1 == 1",l1.size(),0);
		Invitation i2 = new Invitation(new Long(5),new Long(123));
		service.invite(i2);
		Long v1 = new Long(123);
		List<Long> l2 = service.getEventIdsForInvited(new Long(5));
		Assert.assertEquals("Size of l2 == 2",l2.size(),1);
		Assert.assertEquals("v1 == l2[1]",l2.get(0),v1);
	}
	
	/**
	 * We test the result when we get some user for a given invitation.
	 * 
	 * @throws InvitationAlreadyExists
	 */
	@Test
	@InSequence(4)
	public void testGetInvitedIdsForEvent() throws InvitationAlreadyExists {
		//Here is tested getInvitedIdsForEvent() then we add an invitation
		List<Long> l1 = service.getInvitedIdsForEvent(new Long(123));
		Assert.assertEquals("Size of l1 == 1",l1.size(),1);
		Assert.assertEquals("l1[1]==5",l1.get(0),new Long(5));
		Invitation i2 = new Invitation(new Long(1),new Long(123));
		service.invite(i2);
		Long v1 = new Long(1);
		Long v2 = l1.get(0);
		List<Long> l2 = service.getInvitedIdsForEvent(new Long(123));
		Assert.assertEquals("Size of l2 == 2",l2.size(),2);
		Assert.assertEquals("l2[2] == v2 ",l2.get(1),v2);
		Assert.assertEquals("l2[1] == v1 ",l2.get(0),v1);
	}

	/**
	 * We test the result when we get some events for a given user.
	 * @throws InvitationAlreadyExists
	 */
	@Test
	@InSequence(5)
	public void testGetEventsForInvited() throws InvitationAlreadyExists {
		//Event should exist on the DB before testing
		//Here is tested getEventsForInvited() then we add an invitation
		Long e1 = new Long(123);
		Long u1 = new Long(14);
		List<Event> l1 = service.getEventsForInvited(u1);
		Assert.assertEquals("Size of l1 == 0",l1.size(),0);
		Invitation i1 = new Invitation(u1,e1);
		service.invite(i1);
		List<Event> l2 = service.getEventsForInvited(u1);
		Assert.assertEquals("Size of l2 == 1",l2.size(),1);
		Assert.assertEquals("l2.get(0) == Event[123]",l2.get(0),eventService.getById(e1));
	}
	
	/**
	 * We test the result when we get some user for a given event.
	 * @throws InvitationAlreadyExists
	 */
	@Test
	@InSequence(6)
	public void testGetInvitedForEvent() throws InvitationAlreadyExists {
		//User should exist on the DB before testing
		//Here is tested getInvitedForEvent() then we add an invitation
		Long e1 = new Long(50);
		Long u1 = new Long(3);
		List<User> l1 = service.getInvitedForEvent(e1);
		Assert.assertEquals("Size of l1 == 0",l1.size(),0);
		Invitation i1 = new Invitation(u1,e1);
		service.invite(i1);
		List<User> l2 = service.getInvitedForEvent(e1);
		Assert.assertEquals("Size of l2 == 1",l2.size(),1);
		Assert.assertEquals("l2.get(0) == User[3]",l2.get(0),invitedService.getById(u1));
	}

	/**
	 * We check when there is double invitations.
	 * @throws InvitationAlreadyExists
	 */
	@Test(expected=InvitationAlreadyExists.class)
	@InSequence(7)
	public void testInviteTwice() throws InvitationAlreadyExists{
		Long e1 = new Long(50);
		Long u1 = new Long(3);
		Invitation i1 = new Invitation(u1,e1);
		service.invite(i1);
	}
	
}
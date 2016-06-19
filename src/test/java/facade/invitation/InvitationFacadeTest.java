package facade.invitation;

/**
 * 
 * Class to check the validity of invitationfacade's methods
 * 
 * @author Yan
 *
 */



import java.net.URISyntaxException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
import domain.invitation.Invitation;
import domain.event.Event;
import domain.user.User;
import service.invitation.InvitationService;
import service.event.EventService;
import service.user.UserService;

@Stateless
@RunWith(Arquillian.class)
public class InvitationFacadeTest{
	
	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,Invitation.class.getPackage(),InvitationService.class.getPackage())
				.addPackages(true,InvitationFacade.class.getPackage())
				.addPackages(true,User.class.getPackage(),UserService.class.getPackage())
				.addPackages(true,Event.class.getPackage(),EventService.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}
	
	@Inject
	private InvitationFacade facade;
	
	@Inject
	private UserService invitedService;
	
	@Inject
	private EventService eventService;
	
	
	/**
	 * We test the persistence of invitation using the facade.
	 * @throws URISyntaxException
	 */
	@Test 
	@InSequence(1)
	public void testInvite() throws URISyntaxException{
		int codeReussi = facade.invite(new Invitation(new Long(2),new Long(123))).getStatus();
		Assert.assertEquals("codeReussi[invit] == 201 ",codeReussi,201);
	}
	
	/**
	 * We check if we get the right invited user for a specific event.
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(2)
	public void testShowInvitedForEvent(){
		//This User Exist
		Long v1 = new Long(2);
		User u1=invitedService.getById(v1);
		Long e1 = new Long(123);
		List<User> l1 = (List<User>) facade.showInvitedForEvent(e1).getEntity();
		int codeSuccess = facade.showInvitedForEvent(e1).getStatus();
		Assert.assertEquals("codeSuccess[SIFE] == 200 ", codeSuccess,200);
		Assert.assertEquals("l1.size[SIFE] ==1 ", l1.size(),1);
		Assert.assertEquals("l1[1][SIFE] == i1 ", l1.get(0),u1);		
	}	
	
	/**
	 * We check if we get the right events for a specific user
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(3)
	public void testGetEventsForInvited(){
		//This Event Exist
		Long v1 = new Long(123);
		Long u1 = new Long(2);
		Event e1 = eventService.getById(v1);
		List<Event> l1 = (List<Event>) facade.getEventsForInvited(u1).getEntity();
		int codeSuccess = facade.getEventsForInvited(u1).getStatus();
		Assert.assertEquals("codeSuccess[GEFI] == 200 ", codeSuccess,200);
		Assert.assertEquals("l1.size[GEFI] ==1 ", l1.size(),1);
		Assert.assertEquals("l1[1][GEFI] == i1 ", l1.get(0),e1);		
	}
	
	/**
	 * Check if there is an error when persisting an existing Invitations.
	 * @throws URISyntaxException
	 */
	@Test
	@InSequence(4)
	public void testInviteTwice() throws URISyntaxException{
		int codeEchec = facade.invite(new Invitation(new Long(2),new Long(123))).getStatus();
		Assert.assertEquals("codeEchec[invit] == 409 ",codeEchec,409);
	}
	
	/**
	 * Test the showUnknownInnvitedForEvent.
	 * @throws URISyntaxException
	 */
	@Test
	@InSequence(5)
	public void testShowUnknownInvitedForEvent() throws URISyntaxException{
		//This User Doesnt Exist
		Long v1 = new Long(27);
		//User u1=invitedService.getById(v1);
		Long e1 = new Long(123);
		int codeInvit = facade.invite(new Invitation(v1,e1)).getStatus();
		Assert.assertEquals("codeInvit[SIFE] == 201 ",codeInvit,201);		
		
		int codeFail = facade.showInvitedForEvent(e1).getStatus();
		Assert.assertEquals("codeFail[SIFE] == 404 ", codeFail,404);
	}

	/**
	 * Test the set for unknowEventsForInvited.
	 * @throws URISyntaxException
	 */
	@Test
	@InSequence(6)
	public void testGetUnknownEventsForInvited() throws URISyntaxException{
		//This Event Exist
		Long v1 = new Long(1418);
		Long u1 = new Long(2);
		//Event e1 = eventService.getById(v1);
		int codeInvit = facade.invite(new Invitation(u1,v1)).getStatus();
		Assert.assertEquals("codeInvit[GEFI] == 201 ",codeInvit,201);		
		
		int codeFail = facade.getEventsForInvited(u1).getStatus();
		Assert.assertEquals("codeFail[GEFI] == 404 ", codeFail,404);
	}

}


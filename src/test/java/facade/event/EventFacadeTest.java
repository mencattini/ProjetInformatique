package facade.event;

import java.net.URISyntaxException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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
import domain.event.Event;
import domain.user.User;
import service.event.EventService;
import service.user.UserService;

/**
 * 
 * Class to check the validity of eventfacade's methods
 * 
 * @author romain
 *
 */
@Stateless
@RunWith(Arquillian.class)
public class EventFacadeTest{
	
	/**
	 * We create an archive to test our web app.
	 * @return an archive which will be deploy for the test.
	 */
	@Deployment
	public static Archive<?> create(){
		return ShrinkWrap.create(WebArchive.class, "test-SportAround.war")
				.addPackages(true,Event.class.getPackage(),EventService.class.getPackage(),UserService.class.getPackage())
				.addPackages(true,User.class.getPackage(),EventFacade.class.getPackage())
				.addAsResource("test-persistence.xml","META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
	}

	@Inject
	private EventService service;
	
	@Inject
	private EventFacade facade;
	

	
	/**
	 * 
	 * Check the getEventById method.
	 * We take the event 123, and check if it's right.
	 * Then we try with an unexisting Event for 404 error.
	 * 
	 */
	@Test
	@InSequence(1)
	public void testGetEventById(){
		Event e1 = (Event) facade.getEventById(new Long(123)).getEntity();
		int codeE1 = facade.getEventById(new Long(123)).getStatus();
		Event e2 = service.getById(new Long(123));
		int code404 = facade.getEventById(new Long(-1)).getStatus();
		
		Assert.assertEquals("e1 == e2", e1,e2);
		Assert.assertEquals("codeE1 == 200 ", codeE1, 200);
		Assert.assertEquals("code404 == 404", 404, code404);

	}
	
	/**
	 * 
	 * Check the methode searchEventsByPos. We check if the liste has the right size.
	 * Then we check the http answer which must be 200.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(2)
	public void testGetByPosition(){
		List<Event> l1 = (List<Event>) this.facade.searchEventsByPos(170.0, 176. , -40.0, -35).getEntity();
		List<Event> l2 = this.service.getEventAtPos(170., 176., -40.0, -35);
		int codeRight = this.facade.searchEventsByPos(170.0, 176. , -40.0, -35).getStatus();

		Assert.assertEquals("l1.size == l2.size", l1.size(),l2.size());
		Assert.assertEquals("l1.size() == 1", l1.size(), 1);
		Assert.assertEquals("l1.get(0) == l2.get(0)", l1.get(0),l2.get(0));
		Assert.assertEquals("codeRight == 200", codeRight,200);
	}	
	
	
	/**
	 * 
	 * Check the validity of getEventByName.
	 * It give us an entity. We check the entity, the answer code and the error.
	 * 
	 */
	@Test
	@InSequence(3)
	public void testGetEventByName(){
		Event e1 = (Event) this.facade.getEventByName("Hokey").getEntity();
		Event e2 = this.service.getByName("Hokey");
		int codeRight = this.facade.getEventByName("Hokey").getStatus();
		int codeWrong = this.facade.getEventByName("qaks").getStatus();
		
		Assert.assertEquals("e1 == e2", e1,e2);
		Assert.assertEquals("codeRight == 200", codeRight,200);
		Assert.assertEquals("codeWrong == 404", codeWrong,404);
	}

	/**
	 * Check the validity of searching an event by name.
	 * It must return a list with a known size.
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(4)
	public void testSearchEventByName(){
		List<Event> l1 = (List<Event>) this.facade.searchEventByName("Basket").getEntity();
		List<Event> l2 = this.service.searchByName("Basket");
		int codeRight = this.facade.searchEventByName("Basket").getStatus();
		
		Assert.assertEquals("l1.size == li2.size", l1.size(),l2.size());
		Assert.assertEquals("l1.size == 2", l1.size(), 2);
		Assert.assertEquals("codeRight", codeRight,200);
	}
	
	/**
	 * 
	 * Check the validity of add/delete user.
	 * We check that the entry doesn't exist. We create an event. We add it.
	 * We controle the http answer. Finally we delete it and check http again.
	 * @throws URISyntaxException
	 */
	@Test
	@InSequence(5)
	public void testAddDeleteUser() throws URISyntaxException{
		Date date = new Date();
		Time time = Time.valueOf("00:00:00");
		
		Event e1 = new Event("Ping Pong", "tennis de table", date , time , "12 route de drize", (float)42,(float) 42 , new Long(1));
		
		Assert.assertEquals("facade.getbyName(Ping Pong) == 404 ", this.facade.getEventByName("Ping Pong").getStatus(), 404);
		
		this.facade.add(e1);
		
		Assert.assertEquals("facade.getByName(Ping Pong) == 200", this.facade.getEventByName("Ping Pong").getStatus(), 200);
		e1 = (Event) this.facade.getEventByName("Ping Pong").getEntity();
		this.facade.remove(e1.getEvtId());
		
		Assert.assertEquals("facade.getbyName(Ping Pong) == 404 ", this.facade.getEventByName("Ping Pong").getStatus(), 404);
	}
	
	
	/**
	 * We check if the update works, then we retablish the data base.
	 * @throws URISyntaxException 
	 */
	@Test
	@InSequence(7)
	public void testUpdate() throws URISyntaxException{
		
		// we take an event
		Event backup = (Event) this.facade.getEventById(new Long(12)).getEntity();
		// we take the event from where we want to update
		Event e1 = (Event) this.facade.getEventById(new Long(123)).getEntity();
		
		// we copy e1 into event(12);
		e1.setEvtId(new Long(12));
		
		this.facade.update(e1);
		
		// we check that the update is the same as the original
		Assert.assertEquals("facade.get(e1) == e1", this.facade.getEventById(new Long(12)).getEntity(), e1);
		
		// we clean the data base.
		this.facade.update(backup);
		
		// we check the clean
		Assert.assertEquals("facade.get(backup)==backup", this.facade.getEventById(new Long(12)).getEntity(), backup);
	}
	
	/**
	 * We test the methode search by sport"
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(8)
	public void testSearchBySport(){
		
		List<Event> l1 = this.service.getEventBySport("BB");
		List<Event> lEmpty1 = this.service.getEventBySport("alskdj");
		List<Event> l2 = (List<Event>) this.facade.searchEventBySport("BB").getEntity();
		List<Event> lEmpty2 = (List<Event>) this.facade.searchEventByName("alskdj").getEntity();
		int code = this.facade.searchEventBySport("BB").getStatus();
		
		// size of l1, l2 must be equal and equal to 1
		Assert.assertEquals("l1.size == l2.size", l1.size(), l2.size());
		Assert.assertTrue("l2.size == 1", l2.size() == 1);
		
		// the element must be the same
		Assert.assertEquals("l1.get(0) == l2.get(0)", l1.get(0),l2.get(0));
		
		// size of emptylist must be the same and equal to 0
		Assert.assertTrue("lempty1.size == lempty2.size", lEmpty1.size() == lEmpty2.size());
		Assert.assertTrue("lempty2.size == 0", lEmpty2.size() == 0);
		
		// http code must be 200
		Assert.assertEquals("code == 200", code,200);
		
		
	}
	
	/**
	 * 
	 * Check if the rest method works
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(9)
	public void testSearchByOwner(){
		
		List<Event> l1 = (List<Event>) this.facade.searchEventByOwner(new Long(2)).getEntity();
		List<Event> l2 = this.service.getEventByOwner(new Long(2));
		List<Event> lEmpty = (List<Event>) this.facade.searchEventByOwner(new Long(100)).getEntity();
		int code = this.facade.searchEventByOwner(new Long(1)).getStatus();
		
		// l1 and l2 must have the same size, and it must be 2
		Assert.assertEquals("l1.size() == l2.size()", l1.size(), l2.size());
		Assert.assertEquals("l1.size() == 2", l1.size(), 2);
		
		// the first event of the two list must be the same
		Assert.assertEquals("l1.get(0) == l2.get(0)", l1.get(0), l2.get(0));
		
		// http code must be 200
		Assert.assertEquals("code == 200", code, 200);
		
		// lEmpty must be empty
		Assert.assertEquals("lEmpty == []", lEmpty, new ArrayList<Event>());
		
		
	}
	
	
	/**
	 * Test the searching event. With differents parameters.
	 */
	@SuppressWarnings("unchecked")
	@Test
	@InSequence(10)
	public void testSearchEvent(){
		List<Event> l1 = (List<Event>) this.facade.searchEvent("Basket", null, null, null, null, null, null,null).getEntity();
		// it must be 2 of length
		Assert.assertEquals("l1.size() == 2 ", l1.size(), 2);
		
		l1 = (List<Event>) this.facade.searchEvent("Basket","Hokey",null,null,null,null,null,null).getEntity();
		// check this is empty
		Assert.assertEquals("l1.size() == 0 ", l1.size(), 0);
		
		// check the http code
		int code = this.facade.searchEvent("", "BB", null, null, null, null, null,null).getStatus();
		Assert.assertEquals("code == 200", code, 200);
		
		// check the eventof user aurelien coet
		l1 = (List<Event>) this.facade.searchEvent(null, null, null, null, null, null, "Aurelien Coet",null).getEntity();
		Assert.assertEquals("l1.size() == 2", l1.size(), 2);
		
		// check the event with level 2
		l1 = (List<Event>) this.facade.searchEvent(null, null, null, null, null, null, null, new Integer(2)).getEntity();
		Assert.assertEquals("l1 == []", l1, new ArrayList<Event>());
		
		// search with lat
		l1 = (List<Event>) this.facade.searchEvent(null, null, null, null, new Float(-20), new Float(+21), null, null).getEntity();
		Assert.assertEquals("l1.size() == 2", l1.size(), 1);
		
		// search with lng
		l1 = (List<Event>) this.facade.searchEvent(null, null, new Float(170), new Float(180), null, null, null, null).getEntity();
		Assert.assertEquals("l1.size() == 1", l1.size(),1);
		
		// search with a user that doesn't exist
		l1 =(List<Event>) this.facade.searchEvent(null, null, null, null, null, null, "100", null).getEntity();
		Assert.assertEquals("l1 == []", l1, new ArrayList<Event>());
	}
}

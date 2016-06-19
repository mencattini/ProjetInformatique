package facade.subscription;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.subscription.Subscription;
import service.subscription.SubscriptionAlreadyExists;
import service.subscription.SubscriptionService;

/**
 * Provides a set of REST services for the Subscription class.
 * 
 * @author aurelien_coet
 *
 */
@Path("/subscriptions")
public class SubscriptionFacade {
	
	@Inject
	private SubscriptionService subService;
	
	/**
	 * Retrieves the list of events to which the user with the given id has subscribed. 
	 * 
	 * @param userId the id of the user for which to retrieve the list of events.
	 * @return the list of events to which the given user has subscribed.
	 */
	@GET
	@Path("/showEventsForUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventsForUser(@QueryParam("id") Long userId){
		try {
			return Response.ok().entity(subService.getEventsForUser(userId)).build();
		}
		catch (EntityNotFoundException | NoResultException e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "context", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * Retrieves the list of participants to the event with the given id. 
	 * 
	 * @param eventId the id of the event for which to retrieve the list of participants.
	 * @return the list of participants to the given event.
	 */
	@GET
	@Path("/showParticipantsForEvent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showParticipantsForEvent(@QueryParam("id") Long eventId){
		try {
			return Response.ok().entity(subService.getUsersForEvent(eventId)).build();
		}
		catch (EntityNotFoundException | NoResultException e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "context", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	/**
	 * Adds a new subscription to the database.
	 * 
	 * @param subscription a subscription object to add. 
	 * @return a Http Response indicating the URI of the event. 
	 * @throws SubscriptionAlreadyExists
	 * @throws URISyntaxException
	 */
	@POST
	@Path("/addSubscription")
	@Consumes("application/json")
	public Response subscribe(@NotNull Subscription subscription) throws URISyntaxException {
		try {
			subService.subscribe(subscription);
			return Response.status(201).contentLocation(new URI("/subscriptions/byEventsId/" + subscription.getEventId())).build();
		} catch (SubscriptionAlreadyExists e) {
			return Response.status(409).build();
		}
	}
	
	/**
	 * Removes a subscription from the database.
	 * 
	 * @param subscription the subscription to remove from database.
	 * @return an Http Response indicating the success of the operation.
	 */
	@POST
	@Path("/removeSubscription")
	public Response remove(@NotNull Subscription subscription) {
		try {
			subService.unsubscribe(subscription);
			return Response.status(201).build();
		} catch (Exception e){
			return Response.status(404).build();
		}
	}
	
	/**
	 * Removes all the subscriptions linked to a given event.
	 * @param eventId the id of teh event for which to remove the subscriptions.
	 * @return a Http Response indicating the completion of the operation.
	 */
	@DELETE
	@Path("removeAllForEvent")
	public Response removeAllForEvent(@QueryParam("id") Long eventId){
		subService.removeAllSubscriptionsForEvent(eventId);
		return Response.status(201).build();
	}
	
}

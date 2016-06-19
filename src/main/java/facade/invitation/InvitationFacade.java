package facade.invitation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
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

import domain.invitation.Invitation;
import service.invitation.InvitationAlreadyExists;
import service.invitation.InvitationService;

/**
 * REST services set of Invitation Service
 * 
 * @author Laura Juan Galmes
 *
 */

@Path("/invitations")
public class InvitationFacade {
	
	@Inject
	private InvitationService invitationService;
	
	/**
	 * Retrieves the list of events to which the user has been invited.
	 * 
	 * @param userId the id of the user.
	 * @return the list of events he has been invited to.
	 */
	@GET
	@Path("/showEventsForInvited")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventsForInvited(@QueryParam("id") Long userId) {
		try {
			return Response.ok().entity(invitationService.getEventsForInvited(userId)).build();
		} catch (EJBTransactionRolledbackException e) {
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "No result found for this user.", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * Retrieves the list of invited people to an event.
	 * 
	 * @param eventId the id of the event.
	 * @return the list of invited people.
	 */
	@GET
	@Path("/showInvitedForEvent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showInvitedForEvent(@QueryParam("id") Long eventId){
		try {
			return Response.ok().entity(invitationService.getInvitedForEvent(eventId)).build();
		}
		catch (EJBTransactionRolledbackException e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "No result found for this event.", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	/**
	 * Creates a new invitation
	 * 
	 * @param invitation the invitation to add to the DB
	 * @return a Http Response indicating the URI of the event. 
	 * @throws InvitationAlreadyExists
	 * @throws URISyntaxException
	 */
	@POST
	@Path("/addInvitation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response invite(@NotNull Invitation invitation) throws URISyntaxException {
		try {
			invitationService.invite(invitation);
			return Response.status(201).contentLocation(new URI("/invitations/byEventsId/" + invitation.getEventId())).build();
		} catch (InvitationAlreadyExists e) {
			return Response.status(409).build();
		}
	}
	
	/**
	 * Removes a given invitation. 
	 * @param invitation the invitation to remove.
	 * @return a Http Response indicating the completion of the action.
	 */
	@POST
	@Path("/removeInvitation")
	public Response remove(@NotNull Invitation invitation){
		try {
			invitationService.remove(invitation);
			return Response.status(201).build();
		} catch(Exception e){
			return Response.status(404).build();
		}
	}

	/**
	 * Removes all the invitations linked to a given event.
	 * @param eventId the id of the event for which to remove the invites.
	 * @return a Http Response indicating the completion of the action.
	 */
	@DELETE
	@Path("/removeAllForEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeAllForEvent(@QueryParam("id") Long eventId){
		invitationService.removeAllInvitesForEvent(eventId);
		return Response.status(201).build();
	}
}

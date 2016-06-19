package service.invitation;

import java.util.List;

import domain.event.Event;
import domain.invitation.Invitation;
import domain.user.User;

/**
 * Interface for Invitation entities
 * 
 * @author Laura Juan Galmés
 *
 */

public interface InvitationService {
	
	/**
	 * Tests if there already are credentials in the database with the given email.
	 * 
	 * @param email the email for which to check if it is already present in the database.
	 * @return a boolean value indicating if the given email is already attributed to credentials.
	 */
	boolean invitationsAlreadyExist(Invitation invitation);
	/**
	 * Retrieves the list of invitations recived by a user.
	 * 
	 * @param id the id of the invited user.
	 * @return the list of invitations he has recived.
	 */
	List<Invitation> getInvitationsForInvitedId(Long id);
	
	/**
	 * Retrieves the list of invitations sent about a certain event.
	 * 
	 * @param id the id of the event.
	 * @return the list of invitations that has been sent.
	 */
	List<Invitation> getInvitationsForEventId(Long id);

	/**
	 * Retrieves the list of event ids for which a user has been invited.
	 * 
	 * @param id the id of the user which has been invited.
	 * @return the list of event ids in the invitation table for the given user.
	 */
	List<Long> getEventIdsForInvited(Long id);
	
	/**
	 * Retrieves the list of user ids that have been invited to an event.
	 * 
	 * @param id the id of the event.
	 * @return the list of ids of invited people.
	 */
	List<Long> getInvitedIdsForEvent(Long id);
	/**
	 * Retrieves the list of events to which a given user has been invited.
	 * 
	 * @param id the id of the invited user.
	 * @return the list of events to which the user has been invited.
	 */
	List<Event> getEventsForInvited(Long id);
	
	/**
	 * Retrieves the list of users which have been invited to an event.
	 * 
	 * @param id the id of the event.
	 * @return the list of invited users.
	 */
	List<User> getInvitedForEvent(Long id);
	
	/**
	 * Makes a new invitation.
	 * 
	 * @param invitation the user-event pair to persist.
	 */
	void invite(Invitation invitation) throws InvitationAlreadyExists;

	/**
	 * Removes an invitation from the database.
	 * 
	 * @param invitation the invitation to remove from the database.
	 */
	void remove(Invitation invitation);
	
	/**
	 * Removes all the invitations from the databse for a given event.
	 * @param eventId the id of the event for which to remove all invites. 
	 */
	void removeAllInvitesForEvent(Long eventId);
}

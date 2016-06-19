package service.subscription;

import java.util.List;

import domain.event.Event;
import domain.subscription.Subscription;
import domain.user.User;

/**
 * Interface for Subscription entities.
 * 
 * @author aurelien_coet
 *
 */
public interface SubscriptionService {
	
	/**
	 * Tests if there already are credentials in the database with the given email.
	 * 
	 * @param email the email for which to check if it is already present in the database.
	 * @return a boolean value indicating if the given email is already attributed to credentials.
	 */
	boolean subscriptionsAlreadyExist(Subscription subscription);
	/**
	 * Retrieves the list of subscriptions to events for a given user.
	 * 
	 * @param id the id of the user for which to retrieve the subscriptions.
	 * @return the list of subscriptions to events for the given user.
	 */
	List<Subscription> getSubscriptionsForUserId(Long id);
	
	/**
	 * Retrieves the list of subscriptions for a given event.
	 * 
	 * @param id the id of the event for which to retrieve the subscriptions.
	 * @return the list of subscriptions of users to the given event.
	 */
	List<Subscription> getSubscriptionsForEvtId(Long id);
	
	/**
	 * Retrieves the list of event ids in the SUBSCRIPTIONS table for a given user.
	 * 
	 * @param id the id of the user for which to retrieve the event ids.
	 * @return the list of event ids in the table for the given user.
	 */
	List<Long> getEventIdsForUser(Long id);
	
	/**
	 * Retrieves the list of user ids in the SUBSCRIPTIONS table for the given event.
	 * 
	 * @param id the id of the event for which to retrieve the user ids.
	 * @return the list of user ids in the table for the given event.
	 */
	List<Long> getUserIdsForEvent(Long id);
	
	/**
	 * Retrieves the list of events to which a given user has subscribed.
	 * 
	 * @param id the id of the user for which to retrieve the events.
	 * @return the list of events to which the user has subscribed.
	 */
	List<Event> getEventsForUser(Long id);
	
	/**
	 * Retrieves the list of users who have subscribed to a given event.
	 * 
	 * @param id the id of the event for which to retrieve the participants.
	 * @return the list of users who have subscribed to the given event.
	 */
	List<User> getUsersForEvent(Long id);
	
	/**
	 * Makes a new subscription.
	 * 
	 * @param subscription the user-event pair to persist.
	 */
	void subscribe(Subscription subscription) throws SubscriptionAlreadyExists;
	
	/**
	 * Removes a subscription, if it existed.
	 * 
	 * @param subscription the user-event pair to remove.
	 */
	void unsubscribe(Subscription subscription);
	
	/**
	 * Removes all the subscriptions to a given event.
	 * 
	 * @param eventId the id of the event for which to remove the subscriptions. 
	 */
	void removeAllSubscriptionsForEvent(Long eventId);
}

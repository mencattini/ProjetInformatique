package service.event;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import domain.event.Event;

/**
 * Provides a set of services for the event objects.
 * 
 * @author aurelien_coet
 *
 */
public interface EventService extends Serializable {
	
	/**
	 * Retrieves all the events in the database.
	 * 
	 * @return all the created events
	 */
	List<Event> getAll();
	
	/**
	 * Searches the events located in a given interval of coordinates.
	 * 
	 * @return events between given coordinates
	 */
	List<Event> getEventAtPos(double lngMin, double lngMax, double latMin, double latMax);
	
	/**
	 * Counts the number of events present in the database.
	 * 
	 * @return the number of events.
	 */
	int getNbEvents();
	
	/**
	 * Retrieves the events for a given sport. 
	 * 
	 * @param sport the sport for which to retrieve the events.
	 * @return the events of this sports
	 */
	List<Event> getEventBySport(String sport);
	
	/**
	 * Retrieves the list of events owned by a given user.
	 * 
	 * @param id the id of the user.
	 * @return the events owned by a given user.
	 */
	List<Event> getEventByOwner(Long id);
	
	/**
	 * 
	 * @param eventName
	 * @param sport
	 * @param lngMin
	 * @param lngMax
	 * @param latMin
	 * @param latMax
	 * @param eventOwner
	 * @return the event(s) that verify this searching parameters
	 */
	List<Event> searchEvent(String eventName, String sport, Float lngMin, Float lngMax, Float latMin, Float latMax, String eventOwner, Integer level);
	
	/**
	 * Adds an event to the database.
	 * 
	 * @param event to add
	 * @return the id of the added event.
	 */
	Long addEvent(@NotNull Event event);
	
	/**
	 * Retrieves an event by its id.
	 * 
	 * @param id of the event
	 * 
	 * @return the event with the required id
	 */
	Event getById(Long id);
	
	/**
	 * Retrieves an event by its name.
	 * 
	 * @param name of the event
	 * 
	 * @return the event with the given name
	 */
	Event getByName(String name);
	
	/**
	 * Updates the event infos.
	 * @param eventId
	 * @param event
	 */
	void update(Long eventId, @NotNull Event event);
	
	/**
	 * Removes an event
	 * @param eventId
	 */
	void remove(Long eventId);

	/**
	 *  Searches in the database for events with a given pattern in their name.
	 * 
	 * @param name the name of the event to search, or part of it.
	 * @return the list of events whose name contains the name (pattern)
	 * 			input as a parameter.
	 */
	List<Event> searchByName(String name);

}

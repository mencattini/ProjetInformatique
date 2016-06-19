package facade.event;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.event.Event;
import service.event.EventService;

/**
 * Provides a set of REST services for the Event class.
 * 
 * @author aurelien_coet
 *
 */
@Path("/events")
public class EventFacade {

	/**
	 * Uses an EventService implementation to retrieve, add or update
	 * information about events in the database.
	 */
	@Inject 
	private EventService service;

	/**
	 * Serves a Http response containing information in JSON about an event
	 * when receiving a request on the URI "/showEventById".
	 * 
	 * @param eventId the id of the event whose information is requested.
	 * @return a Http Response object containing information about the selected 
	 * 			event in JSON.
	 */
	@GET
	@Path("/showEventById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventById(@QueryParam("id") Long eventId){
		try {
			return Response.ok().entity(service.getById(eventId)).build();
		}
		catch (Exception e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "context", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	/**
	 * Serves a Http response containing information in JSON about an event
	 * when receiving a request on the URI "/showEvent".
	 * 
	 * @param eventName the name of the event whose information is requested.
	 * @return a Http Response object containing information about the selected 
	 * 			event in JSON.
	 */
	@GET
	@Path("/showEvent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventByName(@QueryParam("name") String eventName) {
		try {
			return Response.ok().entity(service.getByName(eventName)).build();
		}
		catch (Exception e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "context", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	/**
	 * Serves a Http response containing a list of events whose coordinates are between two specified coordinates.
	 * 
	 * @param minimal longitude, maximal longitude, minimal latitude and maximal latitude.
	 * @return the list of events satisfying the condition.
	 */
	@GET
	@Path("/searchEventsByPos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchEventsByPos(@QueryParam("lngMin") double lngMin, @QueryParam("lngMax") double lngMax, @QueryParam("latMin") double latMin, @QueryParam("latMax") double latMax){
		try {
			return Response.ok().entity(service.getEventAtPos((double)lngMin, (double)lngMax, (double)latMin, (double)latMax)).build();
		} catch (Exception e){
			return Response.status(404).build();
		}
	}

	/**
	 * Serves a Http response containing a list of events whose name is matching the
	 * parameter name input to the GET request.
	 * 
	 * @param name the name of the event to search, or part of it.
	 * @return the list of events whose names match the pattern in the parameter name.
	 */
	@GET
	@Path("/searchEventByName")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchEventByName(@QueryParam("name") String name){
		return Response.ok().entity(service.searchByName(name)).build();
	}
	
	/**
	 * Serves a Http response containing a list of events for a given sport.
	 * 
	 * @param sport the type of sport for which to search events.
	 * @return a list of events for the given sport.
	 */
	@GET
	@Path("searchEventBySport")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchEventBySport(@QueryParam("sport") String sport){
		return Response.ok().entity(service.getEventBySport(sport)).build();	}

	
	/**
	 * Serves a Http response containing a list of events owned by a given user.
	 * 
	 * @param username the username of the event owner for which to search events.
	 * @return a list of events matching the request.
	 */
	@GET
	@Path("searchEventByOwner")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchEventByOwner(@QueryParam("userId") Long id){
		return Response.ok().entity(service.getEventByOwner(id)).build();	}
	
	/**
	 * 
	 * Rest service that searches events matching a given query.
	 * 
	 * @param eventName
	 * @param sport
	 * @param lngMin
	 * @param lngMax
	 * @param latMin
	 * @param latMax
	 * @param eventOwner
	 * @return a http response contatining a list of events matching the request.
	 */
	@GET
	@Path("searchEvent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchEvent(@QueryParam("eventName") String eventName, @QueryParam("sport") String sport, @QueryParam("lngMin") Float lngMin, @QueryParam("lngMax") Float lngMax, @QueryParam("latMin") Float latMin, @QueryParam("latMax") Float latMax, @QueryParam("eventOwner") String eventOwner, @QueryParam("level") Integer level){
		return Response.ok().entity(service.searchEvent(eventName, sport, lngMin, lngMax, latMin, latMax, eventOwner, level)).build();
	}
	
	/**
	 * Adds a new event to the database.
	 * 
	 * @param event an event object containing the info on the event to add to the database. 
	 * @return a Http Response indicating the URI of the newly added resource. 
	 * @throws URISyntaxException
	 */
	@PUT
	@Path("/addEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(@NotNull Event event) throws URISyntaxException {
		Long newId = service.addEvent(event);
		return Response.status(201).entity("evtId="+newId).build();
	}

	/**
	 * Updates the info about an event in the database.
	 * 
	 * @param event an Event object containing the info on the event to update. 
	 * @return a Http Response indicating the success of the update. 
	 * @throws URISyntaxException
	 */
	@POST
	@Path("/updateEvent")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@NotNull Event event) throws URISyntaxException {
		service.update(event.getEvtId(), event);
		return Response.status(201).build();
	}

	/**
	 * Removes an event from the database.
	 * 
	 * @param eventId the id of the event to remove.
	 * @return an Http Response indicating the success of the operation.
	 * @throws URISyntaxException
	 */
	@DELETE
	@Path("/removeEvent")
	public Response remove(@QueryParam("id") Long eventId) throws URISyntaxException {
		try {
			service.remove(eventId);
			return Response.status(201).build();
		} catch(Exception e){
			return Response.status(404).build();
		}
	}
}

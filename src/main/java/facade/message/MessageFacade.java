package facade.message;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.message.Message;
import domain.user.User;
import service.message.MessageService;

/**
 * Provides a set of REST services for the Event class.
 * 
 * @author Yan
 *
 */
@Path("/messages")
public class MessageFacade {
	
	/**
	 * Uses an MessageService implementation to retrieve, add or update
	 * information about events in the database.
	 */
	@Inject 
	private MessageService service;
	
	/**
	 * Serves a Http response containing information in JSON about a message
	 * when receiving a request on the URI "/showMessageById".
	 * 
	 * @param messageId the id of the message whose information is requested.
	 * @return a Http Response object containing information about the selected 
	 * 			message in JSON.
	 */
	@GET
	@Path("/showMessageById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessageById(@QueryParam("id") Long messageId){
		try {
			return Response.ok().entity(service.getById(messageId)).build();
		}
		catch (Exception e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "context", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	/**
	 * Returns the list of users who are in a conversation with the user
	 * with the input id.
	 * 
	 * @param userId the id of the user for whom to retrieve the participants to a conversation.
	 * @return a list of users who are in a conversation with the user having the input id.
	 */
	@GET
	@Path("/showSender")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showSender(@QueryParam("id") Long userId){
		HashSet<User> sendersReceivers = new HashSet<User>();
		sendersReceivers.addAll(service.getSender(userId));
		sendersReceivers.addAll(service.getReceiver(userId));
		return Response.ok().entity(new ArrayList<User>(sendersReceivers)).build();
	}
	
	/**
	 * Serves a Http response containing information in JSON about a message
	 * when receiving a request on the URI "/showMessageBySender".
	 * 
	 * @param senderId the id of the sender whose information is requested.
	 * @return a Http Response object containing list about the selected 
	 * 			sender in JSON.
	 */
	@GET
	@Path("/getMessagesBySender")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessagesBySender(@QueryParam("id") Long senderId){
			return Response.ok().entity(service.getAllSent(senderId)).build();
	}
	
	/**
	 * Serves a Http response containing information in JSON about a message
	 * when receiving a request on the URI "/showMessageByReceiver".
	 * 
	 * @param receiverId the id of the receiver whose information is requested.
	 * @return a Http Response object containing list about the selected 
	 * 			receiver in JSON.
	 */
	@GET
	@Path("/getMessagesByReceiver")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessagesByReceiver(@QueryParam("id") Long receiverId){
			return Response.ok().entity(service.getAllReceived(receiverId)).build();
	}
	
	/**
	 * Serves a Http response containing information in JSON about a message
	 * when receiving a request on the URI "/showMessageByReceiver".
	 * 
	 * @param receiverId the id of the receiver whose information is requested.
	 * @return a Http Response object containing list about the selected 
	 * 			receiver in JSON.
	 */
	@GET
	@Path("/getAllMessages")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMessages(){
			return Response.ok().entity(service.getAll()).build();
	}
	
	/**
	 * Give the discussion between two users.
	 */
	@GET
	@Path("/showDiscussion")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showDiscussion(@QueryParam("userOne") Long userIdOne, @QueryParam("userTwo") Long userIdTwo){
		return Response.ok().entity(service.getDiscussion(userIdOne, userIdTwo)).build();
	}
	
	
	/**
	 * Adds a new message to the database.
	 * 
	 * @param message an message object containing the info on the message to add. 
	 * @return a Http Response indicating the URI of the newly added resource. 
	 * @throws URISyntaxException
	 */
	@PUT
	@Path("/addMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMessage(@NotNull Message message) throws URISyntaxException {
		service.addMessage(message);
		return Response.status(201).contentLocation(new URI("messages/byMessageId/" + message.getMessageId())).build();
	}
	
	/**
	 * Removes a message from the database.
	 * 
	 * @param messageId the id of the message to remove.
	 * @return an Http Response indicating the success of the operation.
	 * @throws URISyntaxException
	 */
	@DELETE
	@Path("/removeMessage")
	public Response remove(@QueryParam("id") Long messageId) throws URISyntaxException {
		service.remove(messageId);
		return Response.status(201).build();
	}
}

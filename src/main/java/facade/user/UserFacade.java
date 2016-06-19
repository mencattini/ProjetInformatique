package facade.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.credential.Credential;
import domain.profile.Profile;
import domain.user.User;
import service.credential.CredentialService;
import service.credential.CredentialsAlreadyExist;
import service.user.UserService;

/**
 * Provides a set of REST services for the User class.
 * 
 * @author aurelien_coet
 *
 */
@Path("/users")
public class UserFacade {

	/**
	 * Uses a UserService implementation to retrieve, add or update
	 * information about users in the database.
	 */
	@Inject
	private UserService service;
	
	@Inject
	private CredentialService credService;

	/**
	 * Serves a Http response containing information about a user in JSON
	 * when receiving a request on the URI "/showUserById".
	 * 
	 * @param userId the id of the user whose information must be retrieved.
	 * @return a Http Response object, containing information about a user in JSON.
	 */
	@GET
	@Path("/showUserById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showUserById(@QueryParam("id") Long userId) {
		try {
			return Response.ok().entity(service.getById(userId)).build();
		}
		catch (Exception e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "context", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}

	}

	/**
	 * Serves a Http response containing information about a user in JSON
	 * when receiving a request on the URI "/showUser".
	 * 
	 * @param userName the name of the user whose information must be retrieved.
	 * @return a Http Response object containing information about a user in JSON.
	 */
	@GET
	@Path("/showUserByName")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showUserByName(@QueryParam("userName") String userName) {
		try {
			return Response.ok().entity(service.getByName(userName)).build();
		}
		catch (Exception e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "context", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	/**
	 * Serves a Http response containing a list of users whose email is matching the
	 * parameter email input to the GET request.
	 * 
	 * @param email the email of the user to search
	 * @return the unique user whose email matches the pattern in the parameter name.
	 */
	@GET
	@Path("/searchUserByEmail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUserByEmail(@QueryParam("email") String email){
		try {
			return Response.ok().entity(service.searchByEmail(email)).build();
		}
		catch (Exception e){
			Logger logger = Logger.getLogger("Global Logger Name");
			logger.log(Level.WARNING, "context", e);
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	/**
	 * Serves a Http response containing a list of users whose name is matching the
	 * parameter name input to the GET request.
	 * 
	 * @param name the name of the user to search, or part of it.
	 * @return the list of users whose name matches the pattern in the parameter name.
	 */
	@GET
	@Path("/searchUserByName")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUserByName(@QueryParam("name") String name){
		return Response.ok().entity(service.searchByName(name)).build();
	}

	/**
	 * Adds a new user to the database.
	 * 
	 * @param user a user object containing the info on the user to add. 
	 * @return a Http Response indicating the URI of the newly added resource. 
	 * @throws URISyntaxException
	 * @throws NoSuchAlgorithmException 
	 */
	@POST
	@Path("/addUser")
	@Consumes("application/json")
	public Response addUser(@NotNull Profile profile) throws URISyntaxException, NoSuchAlgorithmException {
		if (!credService.credentialsAlreadyExist(profile.getEmail())){
			User user = new User(profile.getUserName(), profile.getEmail(), profile.getBirthDate(), "picture", null, false);
			Credential credential = new Credential(profile.getEmail(), credService.hashPasswd(profile.getPasswd()), "user");
			service.addUser(user);
			try {
				credService.addCredentials(credential);
			} catch (CredentialsAlreadyExist e) {
				return Response.status(409).build();
			}
			return Response.status(201).contentLocation(new URI("users/byUserId/" + user.getUserId())).build();
		} else {
			return Response.status(409).build();
		}
	}

	/**
	 * Updates the info about a user in the database.
	 * 
	 * @param user a user object containing the info on the user to update. 
	 * @return a Http Response indicating the success of the update. 
	 * @throws URISyntaxException
	 */
	@POST
	@Path("/updateUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@NotNull User user) throws URISyntaxException {
		service.update(user.getUserId(), user);
		return Response.status(201).build();
	}
	
	/**
	 * Updates the password of a user in the Credentials Table.
	 * @param cred the Credentials object to use for the update.
	 * @return a Http Response indicating the completion of the action.
	 */
	@POST
	@Path("/updatePassword")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePassword(@NotNull Credential cred){
		try {
			cred.setPasswd(credService.hashPasswd(cred.getPasswd()));
			cred.setRole("user");
			credService.updateCredentials(cred.getEmail(), cred);
			return Response.status(201).build();
		} catch (Exception e){
			return Response.status(404).build();
		}
	}
}

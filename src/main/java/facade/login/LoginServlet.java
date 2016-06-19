package facade.login;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.user.UserService;

/**
 * Provides methods for authentication on the server.
 * 
 * @author aurelien_coet
 *
 */
@WebServlet(name = "/login", urlPatterns={"/login"})
public class LoginServlet extends HttpServlet {
	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -1516258385227218380L;
	
	@Inject
	private UserService userService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// The parameters for the authentication are retrieved.
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		// We try to perform authentication on the server with the input parameters.
		try{
			req.login(username, password);
			Long id = userService.getByEmail(username).getUserId();
			resp.setStatus(200);
			// The id of the user who authenticated is returned in the body of the response.
			resp.getWriter().print("id="+Long.toString(id));
		} catch(ServletException e){
			resp.setStatus(401);
		}
	}
}

package facade.logout;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Provides methods to allow logout on the server.
 * @author Romain Mencattini.
 *
 */
@WebServlet(name = "/logout", urlPatterns={"/logout"})
public final class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// Creation of the session cookie.
		Cookie c = new Cookie("JSESSIONID","674684641");

		// Definition of the validity of the cookie.
		c.setMaxAge(0);

		// Cookie sent in the Http Response.
		response.addCookie(c);
		
		// Invalidation of the session.
		request.logout();
		session.invalidate();
	}
}
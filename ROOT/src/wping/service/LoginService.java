package wping.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import wping.message.LoginRequest;
import wping.message.LoginResponse;
import wping.security.Token;

@Path("/login")
public class LoginService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginRequest request) {
		
		if ("123".equals(request.pwd)) {
			LoginResponse response = new LoginResponse();
			response.tkn = Token.create();
			return Response.ok(response).build();
		}
		
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
}

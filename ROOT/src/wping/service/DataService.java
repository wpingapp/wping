package wping.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import wping.websocket.WebSocket;

@Path("/data")
public class DataService {

	private static File log = new File("C:\\server\\wesrv.log");
	
	static {
		if (!log.exists()) {
			try {
				log.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	public Response data(String request) {
		
		if (log.length() > 512*1024*1024) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		FileWriter wr = null;
		
		try {
			wr = new FileWriter(log, true);
			wr.write(request + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (wr != null) {
					wr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		WebSocket.broadcast(request);
		
		return Response.ok().build();
	}
	
	
}

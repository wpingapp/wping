package wping.application;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class Application extends ResourceConfig {

	public Application() {
		packages("wesrv.service");
		
		register(JacksonFeature.class);
//		register(MultiPartFeature.class);
		
	}
	
}

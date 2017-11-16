package wping.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocket {

	private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public void onMessage(Session session, String message, boolean last) {

	}

	@OnOpen
	public void open(Session session) {
		sessions.add(session);
		System.out.println("opened websocket sessions: " + sessions.size());
	}

	@OnClose
	public void close(Session session) {
		sessions.remove(session);
		System.out.println("opened websocket sessions: " + sessions.size());
	}

	@OnError
	public void onError(Throwable error) {
	}

	public static void broadcast(String message) {

		for (Session session : sessions) {

			try {
				
				if (session.isOpen()) {
					session.getBasicRemote().sendText(message);
				}
				
			} catch (IOException e) {
				try {
					session.close();
				} catch (IOException e1) {
					// ignore
				}

				sessions.remove(session);
				
			}
		}
	}

}

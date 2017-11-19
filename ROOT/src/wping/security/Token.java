package wping.security;

import java.security.SecureRandom;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

public class Token {

	
	private static byte[] secret = new byte[32];
	
	static {
		new SecureRandom().nextBytes(secret);
	}
	
	public static String create() {
		Algorithm algorithm = Algorithm.HMAC256(secret);
	    String token = JWT.create()
	    	.withExpiresAt(new Date(System.currentTimeMillis() + 2000 ))
	    	.sign(algorithm);
	    
	    return token;
	}


	public static boolean verify(String token) {
			try {
				Algorithm algorithm = Algorithm.HMAC256(secret);
				JWTVerifier verifier = JWT.require(algorithm).build(); 
				verifier.verify(token);
				
				return true;
			} catch (JWTVerificationException e) {
				return false;
			}
	}
}

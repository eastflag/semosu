package com.minho.utils;

import com.minho.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Properties;

public class CommonUtil {
	
	public static String createJWT(String id, String issuer, String subject, long ttlMillis) {
		try {
			Resource resource = new ClassPathResource("/application.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			String key = props.getProperty("auth.key");

			//The JWT signature algorithm we will be using to sign the token
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

			long nowMillis = System.currentTimeMillis();
			Date now = new Date(nowMillis);

			//We will sign our JWT with our ApiKey secret
			byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
			Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

			//Let's set the JWT Claims
			JwtBuilder builder = Jwts.builder().setId(id)
					.setIssuedAt(now)
					.setSubject(subject)
					.setIssuer(issuer)
					.signWith(signatureAlgorithm, signingKey);

			//if it has been specified, let's add the expiration
			if (ttlMillis >= 0) {
				long expMillis = nowMillis + ttlMillis;
				Date exp = new Date(expMillis);
				builder.setExpiration(exp);
			}

			//Builds the JWT and serializes it to a compact, URL-safe string
			System.out.println("builder.compact(): " + builder.compact());

			return builder.compact();
		} catch (IOException e) {
			return null;
		}
	}
	
	public static String parseJWT(String jwt) throws Exception {
		Resource resource = new ClassPathResource("/application.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
		String key = props.getProperty("auth.key");
		
		//This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser()         
		   .setSigningKey(DatatypeConverter.parseBase64Binary(key))
		   .parseClaimsJws(jwt)
		   .getBody();
		
/*		System.out.println("ID: " + claims.getId());
		System.out.println("Subject: " + claims.getSubject());
		System.out.println("Issuer: " + claims.getIssuer());
		System.out.println("Expiration: " + claims.getExpiration());*/
		
		
/*		boolean isExpired = System.currentTimeMillis() > claims.getExpiration().getTime() ? true : false;
		System.out.println("expired:" + isExpired);
		
		TokenVO tokenVO = new TokenVO();
		tokenVO.setId(claims.getId());
		tokenVO.setIssuer(claims.getIssuer());
		tokenVO.setSubject(claims.getSubject());
		tokenVO.setIssuedAt(claims.getIssuedAt());
		tokenVO.setExpired(isExpired);*/
		
		return createJWT(claims.getId(), claims.getIssuer(), claims.getSubject(), Constant.SESSION_TIMEOUT);

	}

}

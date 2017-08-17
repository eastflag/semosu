package com.minho;

import com.minho.utils.CommonUtil;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

public class JwtFilter extends GenericFilterBean {

	@Override
	public void doFilter(final ServletRequest req,
            final ServletResponse res,
            final FilterChain chain) throws IOException, ServletException {
		
		//System.out.println("doFilter");
		
		final HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse)res;

        final String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            //throw new ServletException("Missing or invalid Authorization header.");
        	System.out.println("token null");
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        	return;
        }

        //final String token = authHeader.substring(7); // The part after "Bearer "

        String new_token = null;
        try {
        	new_token = CommonUtil.parseJWT(token);
        }
        catch (SignatureException e) {
            //throw new ServletException("Invalid token.");
        	System.out.println("parse error");
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        	return;
        } catch (IOException e) {
            //throw new ServletException("Invalid token.");
        	System.out.println("IOException error");
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        	return;
        } catch (Exception e) {
            //throw new ServletException("Invalid token.");
        	System.out.println("expire error");
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        	return;
        }

        response.addHeader("Authorization", new_token);
        chain.doFilter(req, res);
	}

}

package com.store.book.filter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.annotation.WebFilter;

@Component
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
	
	private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		      throws IOException, ServletException {

		    HttpServletRequest req = (HttpServletRequest) request;
		    HttpServletResponse res = (HttpServletResponse) response;
		    if (req.getServletPath().equals("/login") || req.getServletPath().equals("/register") || req.getServletPath().equals("/")) {	    	
		    	chain.doFilter(request, response);
		    	return;
		    }

		    if (isUserAuthenticated(req)) {
		    	chain.doFilter(req, res);
			    return;
		    }
		    
		    logger.error("Filter Redirecting to login");
		    res.sendRedirect(req.getContextPath());
		  }

		  private boolean isUserAuthenticated(HttpServletRequest req ) {
		    HttpSession session = req.getSession(false);
		    if ( (session != null) && (session.getAttribute("userName")!=null) && (session.getAttribute("role")!=null)) {
		    	return true;
		    }
		    return false;
		  }
}

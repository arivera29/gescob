package com.are.gescob;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.are.gescob.entity.User;


@Component
@WebFilter(urlPatterns = "/*")
public class SessionFilter implements Filter {
	
	
	private final static Logger logger = Logger.getLogger("SessionFilter");
	
	private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
	        Arrays.asList("", "/login", "/logout", "/register","/images")));
	
	@Override
	 public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("########## Initiating SessionFilter filter ##########");
	 }

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		
		String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "");
		logger.info("##### PATH: " + path);
		
		HttpSession session = req.getSession(false);
		User user = null;
		if (session != null) {
			user = (User)session.getAttribute("user");
		}
		
		boolean loggedIn = (session != null && user != null);
        boolean allowedPath = ALLOWED_PATHS.contains(path) || path.endsWith("png") || path.endsWith("css") || path.endsWith("js");
		logger.info("Request URL: " + req.getRequestURL().toString());
        if (loggedIn || allowedPath) {
            logger.info("User is active");
        	chain.doFilter(request, response);
        }
        else {
        	logger.info("User not is active!");
            res.sendRedirect(req.getContextPath() + "/login");
        }
		
		
	}

}

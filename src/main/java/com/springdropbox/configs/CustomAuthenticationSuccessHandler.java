package com.springdropbox.configs;

import com.springdropbox.entities.User;
import com.springdropbox.services.FileServiceImpl;
import com.springdropbox.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private UserService userService;

	@Autowired
	FileServiceImpl fileService;

	public static HttpSession session;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String userName = authentication.getName();
		User theUser = userService.findByUserName(userName);
		session = request.getSession();
		session.setAttribute("user", theUser);
		FileServiceImpl.session = this.session;
		FileServiceImpl.currentUser = ((User) session.getAttribute("user"));
		FileServiceImpl.rootLocation = Paths.get("./storage/" + ((User) session.getAttribute("user")).getUserName());
		if(!request.getHeader("referer").contains("login")) {
			response.sendRedirect(request.getHeader("referer"));
		} else {
			response.sendRedirect(request.getContextPath() + "/" + userName);
		}
	}
}

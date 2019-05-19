package com.are.gescob.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.Alert;
import com.are.gescob.entity.User;
import com.are.gescob.model.UserRepository;
import com.are.gescob.util.EncriptarMD5;
import com.are.gescob.util.PasswordGenerator;
import com.are.gescob.util.SendMail;



@Controller
public class UserController {

	@Autowired
	UserRepository repository;
	
	
	@Autowired
	MessageSource messageSource;
	
	private final static Logger logger = Logger.getLogger("UserController");
	
	@RequestMapping("/users")
	public ModelAndView home(HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("ADM")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		return showView(new User(), new Alert(),userLogin.getAccount());
	}
	
	@PostMapping("/users")
	@Transactional
	public ModelAndView add(@Valid User user, 
			BindingResult bindResult, 
			@RequestParam("action") String action, 
			Model model,
			HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("ADM")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		if (bindResult.hasErrors()) {
			
			if (action.equals("add")) {
				return showView(user, new Alert(),userLogin.getAccount());
			}
			if (action.equals("edit")) {
				ModelAndView view = new ModelAndView("user_edit");
				view.addObject("user", user);
				return view;
			}
			
			
		}
		
		
		Alert alert = new Alert();
		logger.info("Action user form: " + action);
		
		if (action.equals("add")) {
			logger.info("Add new user record");
			logger.info("Find user by username");
			User temp = repository.findByUsername(user.getUsername());
			if (temp != null) {
				logger.info("User record found by username");
				alert.setLevel(Alert.DANGER);
				alert.setMessage(messageSource.getMessage("valid.username_exist", null,LocaleContextHolder.getLocale()));
				
				
				return showView(user, alert,userLogin.getAccount());
			}else {
				
				logger.info("User record not found by username");
				user.setChangePassword(true);
				String password = PasswordGenerator.getPassword(10);
				user.setPassword(EncriptarMD5.getMD5(password));
				user.setParent(false);
				
				
				
				user.setAccount(userLogin.getAccount());
				
				logger.info("Saving new user");
				repository.save(user);
				
				logger.info("Sending password at email");
				sendPasswordToEmail(user.getUsername(),user.getEmail(), password);
				
				alert.setLevel(Alert.INFO);
				alert.setMessage(messageSource.getMessage("crud.save", null,LocaleContextHolder.getLocale()));
			}
		}
		
		
		
		if (action.equals("edit")) {
			User temp = repository.findById(user.getId()).get();
			if (temp.getUsername().equals(user.getUsername())) {
				
				user.setPassword(temp.getPassword());
				user.setChangePassword(temp.getChangePassword());
				user.setParent(temp.getParent());
				user.setAccount(temp.getAccount());
				
				repository.save(user);
				
				alert.setLevel(Alert.INFO);
				alert.setMessage(messageSource.getMessage("crud.update", null,LocaleContextHolder.getLocale()));
				
			}else {
				User temp2 = repository.findByUsername(user.getUsername());
				if (temp2 == null) {
					user.setPassword(temp.getPassword());
					user.setChangePassword(temp.getChangePassword());
					user.setParent(temp.getParent());
					user.setAccount(temp.getAccount());
					
					repository.save(user);
					alert.setLevel(Alert.INFO);
					alert.setMessage(messageSource.getMessage("crud.update", null,LocaleContextHolder.getLocale()));
				}else {
					alert.setLevel(Alert.DANGER);
					alert.setMessage(messageSource.getMessage("valid.username_exist", null,LocaleContextHolder.getLocale()));
					ModelAndView view = new ModelAndView("user_edit");
					view.addObject("user", user);
					view.addObject("alert", alert);
					return view;
				}
			}
		}	
			
		if (action.equals("remove")) {
			
			repository.delete(user);
			alert.setLevel(Alert.INFO);
			alert.setMessage(messageSource.getMessage("crud.remove", null,LocaleContextHolder.getLocale()));
		}
		
		if (action.equals("reset")) {
			
			User temp = repository.findById(user.getId()).get();
			
			temp.setChangePassword(true);
			String password = PasswordGenerator.getPassword(10);
			temp.setPassword(EncriptarMD5.getMD5(password));
			repository.save(temp);
			logger.info("Sending password at email");
			sendPasswordToEmail(temp.getUsername(),temp.getEmail(), password);
			
			alert.setLevel(Alert.INFO);
			alert.setMessage(messageSource.getMessage("crud.password_reset", null,LocaleContextHolder.getLocale()));
		}
		
		
		return showView(new User(),alert,userLogin.getAccount());
	}
	
	@RequestMapping("/user/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id,
			HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("ADM")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		User user = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("user_edit");
		view.addObject("user", user);
		
		
		return view;
	}
	
	@RequestMapping("/user/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("ADM")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		User user = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("user_remove");
		view.addObject("user", user);
		
		return view;
	}
	
	@RequestMapping("/user/resetpassword/{id}")
	public ModelAndView resetPassword(@PathVariable("id") Long id,
			HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("ADM")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		User user = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("user_reset_password");
		view.addObject("user", user);
		
		
		return view;
	}
	
	@RequestMapping("/user/changepassword")
	public ModelAndView changePassword(HttpSession session) {
		
		
		
		User user = (User)session.getAttribute("user");
		
		ModelAndView view = new ModelAndView("user_change_password");
		view.addObject("user", user);
		
		
		return view;
	}
	
	@PostMapping("/user/changepassword")
	public ModelAndView changePasswordBD(HttpSession session,
			@RequestParam("password") String password,
			@RequestParam("re_password") String re_password,
			Model model) {
		
		User userLogin = (User)session.getAttribute("user");
			
		
		if (!password.equals(re_password)) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Password not equals. Verify please");
			ModelAndView view = new ModelAndView("user_change_password");
			view.addObject("user", userLogin);
			view.addObject("alert",alert);
			return view;
		}
		
		if (password.length() < 8) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Minimum length of password must be 8 characters. Verify please");
			ModelAndView view = new ModelAndView("user_change_password");
			view.addObject("user", userLogin);
			view.addObject("alert",alert);
			return view;
		}
		
		
		
		User user = repository.findById(userLogin.getId()).get();
		user.setPassword(EncriptarMD5.getMD5(password));
		user.setChangePassword(false);
		
		repository.save(user);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage(messageSource.getMessage("crud.password_updated", null,LocaleContextHolder.getLocale()));
		
		ModelAndView view = new ModelAndView("/login");
		
		view.addObject("alert", alert);
		
		return view;
	}
	
	
	
	public ModelAndView showView(User user, Alert alert, Account account) {
		Iterable<User> users = repository.findAllOrderByName(account);
		
		ModelAndView view = new ModelAndView("users");
		view.addObject("users", users);
		view.addObject("user", user);
		view.addObject("alert", alert);
		
		return view;
	}
	
	private void sendPasswordToEmail(String username, String email, String password) {
		String subject = "Welcome to Survey Sofware";
		String body = String.format("Welcome %s to Survey Software \n\nYour password is %s \n\nAll rights reserved SOLUCIONES INTEGRALES ARE ", username, password);
		SendMail.SendMailGmail(email, subject, body);
	}
	
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView dataIntegrityViolationExceptionHandler( Exception ex, Model modelo, HttpSession session) {
		User userLogin = (User)session.getAttribute("user");
		Alert alert = new Alert();
		alert.setLevel(Alert.DANGER);
		alert.setMessage(ex.getMessage());
		logger.info("Database error. Violation integrity data when was to remove office record");
		return showView(new User(),alert,userLogin.getAccount());
	}
	
	
}

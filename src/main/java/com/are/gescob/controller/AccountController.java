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
import com.are.gescob.model.AccountRepository;
import com.are.gescob.model.UserRepository;
import com.are.gescob.util.EncriptarMD5;
import com.are.gescob.util.PasswordGenerator;
import com.are.gescob.util.SendMail;



@Controller
public class AccountController {

	@Autowired
	AccountRepository repository;
	
	@Autowired
	UserRepository userRepository;
	
	
	@Autowired
	MessageSource messageSource;
	
	
	private final static Logger logger = Logger.getLogger("AccountController");
	
	@RequestMapping("/accounts")
	public ModelAndView home(HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		
		if (!userLogin.getRole().equals("MNG")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		return showView(new Account(), new Alert());
	}
	
	@PostMapping("/accounts")
	@Transactional
	public ModelAndView add(@Valid Account account, 
			BindingResult bindResult, 
			@RequestParam("action") String action, 
			Model model,
			HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("MNG")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		if (bindResult.hasErrors()) {
			
			if (action.equals("add")) {
				return showView(account, new Alert());
			}
			if (action.equals("edit")) {
				ModelAndView view = new ModelAndView("account_edit");
				view.addObject("account", account);
				return view;
			}
			
			
		}
		
		Alert alert = new Alert();
		logger.info("Action account form: " + action);
		
		if (action.equals("add")) {
			logger.info("Add new account record");
			logger.info("Find account by email");
			Account temp = repository.findByEmail(account.getEmail());
			if (temp != null) {
				logger.info("Account record found by email");
				alert.setLevel(Alert.DANGER);
				alert.setMessage(messageSource.getMessage("valid.email_exist", null,LocaleContextHolder.getLocale()));
				
				
				return showView(account, alert);
			}else {
				
				logger.info("Account record not found by email");
				//account.setChangePassword(true);
				//String password = PasswordGenerator.getPassword(10);
				//account.setPassword(EncriptarMD5.getMD5(password));
				
				logger.info("Saving new account");
				account = repository.save(account);
				
				if (userRepository.findByUsername(account.getEmail())==  null) {
					User user = new User();
					user.setUsername(account.getEmail());
					user.setName(account.getName());
					user.setEmail(account.getEmail());
					user.setActive(true);
					user.setChangePassword(true);
					
					String password = PasswordGenerator.getPassword(10);
					user.setPassword(EncriptarMD5.getMD5(password));
					user.setAccount(account);
					user.setRole("ADM");
					
					userRepository.save(user);
					
					sendPasswordToEmail(user.getUsername(),user.getEmail(), password);
					
					
				}
				
				
				
				
				logger.info("Sending password at email");
				//sendPasswordToEmail(account.getName(),account.getEmail(), password);
				
				alert.setLevel(Alert.INFO);
				alert.setMessage(messageSource.getMessage("crud.save", null,LocaleContextHolder.getLocale()));
			}
		}
		
		
		
		if (action.equals("edit")) {
			Account temp = repository.findById(account.getId()).get();
			if (temp.getEmail().equals(account.getEmail())) {
				//account.setPassword(temp.getPassword());
				//account.setChangePassword(temp.getChangePassword());
				repository.save(account);
				
				alert.setLevel(Alert.INFO);
				alert.setMessage(messageSource.getMessage("crud.update", null,LocaleContextHolder.getLocale()));
				
			}else {
				Account temp2 = repository.findByEmail(account.getEmail());
				if (temp2 == null) {
					//account.setPassword(temp.getPassword());
					//account.setChangePassword(temp.getChangePassword());
					
					repository.save(account);
					alert.setLevel(Alert.INFO);
					alert.setMessage(messageSource.getMessage("crud.update", null,LocaleContextHolder.getLocale()));
				}else {
					alert.setLevel(Alert.DANGER);
					alert.setMessage(messageSource.getMessage("valid.email_exist", null,LocaleContextHolder.getLocale()));
					ModelAndView view = new ModelAndView("account_edit");
					view.addObject("account", account);
					view.addObject("alert", alert);
					return view;
				}
			}
		}	
			
		if (action.equals("remove")) {
			
			repository.delete(account);
			alert.setLevel(Alert.INFO);
			alert.setMessage(messageSource.getMessage("crud.remove", null,LocaleContextHolder.getLocale()));
		}
		
		
		return showView(new Account(),alert);
	}
	
	@RequestMapping("/account/update/{id}")
	public ModelAndView update(@PathVariable("id") Long id,
			HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("MNG")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		Account account = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("account_edit");
		view.addObject("account", account);
		
		
		return view;
	}
	
	@RequestMapping("/account/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User userLogin = (User)session.getAttribute("user");
		if (!userLogin.getRole().equals("MNG")) {
			return new ModelAndView("redirect:/access_denied");
		}
		
		Account account = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("account_remove");
		view.addObject("account", account);
		
		return view;
	}
	
	
	
	
	public ModelAndView showView(Account account, Alert alert) {
		Iterable<Account> accounts = repository.findAllOrderByName();
		ModelAndView view = new ModelAndView("accounts");
		view.addObject("accounts", accounts);
		view.addObject("account", account);
		view.addObject("alert", alert);
		
		return view;
	}
	
	public void sendPasswordToEmail(String username, String email, String password) {
		String subject = "Welcome to Survey Sofware";
		String body = String.format("Welcome %s to Survey Software \n\nYour password is %s \n\nAll rights reserved SOLUCIONES INTEGRALES ARE ", username, password);
		SendMail.SendMailGmail(email, subject, body);
	}
	
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView dataIntegrityViolationExceptionHandler( Exception ex, Model modelo) {
		Alert alert = new Alert();
		alert.setLevel(Alert.DANGER);
		alert.setMessage(ex.getMessage());
		logger.info("Database error. Violation integrity data when was to remove office record");
		return showView(new Account(),alert);
	}
}

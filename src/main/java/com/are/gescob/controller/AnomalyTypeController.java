package com.are.gescob.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.Alert;
import com.are.gescob.entity.AnomalyType;
import com.are.gescob.entity.User;
import com.are.gescob.model.AnomalyTypeRepository;

@Controller
public class AnomalyTypeController {
	
	@Autowired
	AnomalyTypeRepository repository;
	
	@GetMapping("/anomalies")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new AnomalyType(),new Alert(),user.getAccount());
	}
	
	@PostMapping("/anomalies")
	public ModelAndView add(@Valid AnomalyType anomaly, BindingResult result,
			@RequestParam("action") String action,
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Alert alert = new Alert();
		
		anomaly.setAccount(user.getAccount());
		
		if (result.hasErrors()) {
			
			switch(action) {
			case "edit":
				model.addAttribute("anomaly", anomaly);
				return new ModelAndView("anomaly_edit",model);
				
			case "add":
				return getView(anomaly, alert, user.getAccount());
			case "remove":
				model.addAttribute("anomaly", anomaly);
				return new ModelAndView("anomaly_remove",model);
			}

		}

		if (!action.equals("remove")) {
			
			if (action.equals("add")) {
				
				Optional<AnomalyType> temp = repository.findByCodeAndAccount(user.getAccount(), anomaly.getCode());
				if (temp.isPresent()) {
					alert.setLevel(Alert.DANGER);
					alert.setMessage("Code exist in database");
					return getView(anomaly, alert, user.getAccount());
					
				}
				
				anomaly.setCreatedDate(new java.util.Date());
				anomaly.setCreatedUser(user);
			}
			
			AnomalyType saved = repository.save(anomaly);
			
			if (saved == null) {
				alert.setLevel(Alert.DANGER);
				alert.setMessage("Error save record");
				
			}else {
				
				alert.setLevel(Alert.INFO);
				alert.setMessage("Record saved");
				
			}
		}else {
			
			repository.delete(anomaly);
			alert.setLevel(Alert.INFO);
			alert.setMessage("Record removed");
			
		}
		
		
		return getView(new AnomalyType(),alert,user.getAccount());
	}
	
	@GetMapping("/anomaly/update/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id, 
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		AnomalyType anomaly = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("anomaly_edit");
		view.addObject("alert", new Alert());
		view.addObject("anomaly", anomaly);
		
		return view;
		
	}
	
	@GetMapping("/anomaly/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		AnomalyType anomaly = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("anomaly_remove");
		view.addObject("alert", new Alert());
		view.addObject("anomaly", anomaly);
		
		return view;
		
	}
	
	public ModelAndView getView (AnomalyType anomaly, Alert alert, Account account) {
		ModelAndView view = new ModelAndView("anomalies");
		
		Iterable<AnomalyType> anomalies = repository.findAllOrderByName(account);
		
		view.addObject("anomalies", anomalies);
		view.addObject("anomaly", anomaly);
		view.addObject("alert", alert);
		
		
		
		return view;
		
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView dataIntegrityViolationExceptionHandler( Exception ex, Model modelo, HttpSession session) {
		User userLogin = (User)session.getAttribute("user");
		Alert alert = new Alert();
		alert.setLevel(Alert.DANGER);
		alert.setMessage("Database error. Violation integrity data when was to remove anomaly record");
		return getView(new AnomalyType(),alert,userLogin.getAccount());
	}
}

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
import com.are.gescob.entity.ResultType;
import com.are.gescob.entity.User;
import com.are.gescob.model.ResultTypeRepository;

@Controller
public class ResultTypeController {
	@Autowired
	ResultTypeRepository repository;
	
	@GetMapping("/results")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new ResultType(),new Alert(),user.getAccount());
	}
	
	@PostMapping("/results")
	public ModelAndView add(@Valid ResultType result, BindingResult bind,
			@RequestParam("action") String action,
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Alert alert = new Alert();
		
		result.setAccount(user.getAccount());
		
		if (bind.hasErrors()) {
			
			switch(action) {
			case "edit":
				model.addAttribute("result", result);
				return new ModelAndView("result_edit",model);
				
			case "add":
				return getView(result, alert, user.getAccount());
			case "remove":
				model.addAttribute("result", result);
				return new ModelAndView("result_remove",model);
			}

		}

		if (!action.equals("remove")) {
			
			if (action.equals("add")) {
				
				Optional<ResultType> temp = repository.findByCodeAndAccount(user.getAccount(), result.getCode());
				if (temp.isPresent()) {
					alert.setLevel(Alert.DANGER);
					alert.setMessage("Code exist in database");
					return getView(result, alert, user.getAccount());
					
				}
				
				result.setCreatedDate(new java.util.Date());
				result.setCreatedUser(user);
			}
			
			ResultType saved = repository.save(result);
			
			if (saved == null) {
				alert.setLevel(Alert.DANGER);
				alert.setMessage("Error save record");
				
			}else {
				
				alert.setLevel(Alert.INFO);
				alert.setMessage("Record saved");
				
			}
		}else {
			
			repository.delete(result);
			alert.setLevel(Alert.INFO);
			alert.setMessage("Record removed");
			
		}
		
		
		return getView(new ResultType(),alert,user.getAccount());
	}
	
	@GetMapping("/result/update/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id, 
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		ResultType result = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("result_edit");
		view.addObject("alert", new Alert());
		view.addObject("result", result);
		
		return view;
		
	}
	
	@GetMapping("/result/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		ResultType result = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("result_remove");
		view.addObject("alert", new Alert());
		view.addObject("result", result);
		
		return view;
		
	}
	
	public ModelAndView getView (ResultType result, Alert alert, Account account) {
		ModelAndView view = new ModelAndView("results");
		
		Iterable<ResultType> results = repository.findAllOrderByName(account);
		
		view.addObject("results", results);
		view.addObject("result", result);
		view.addObject("alert", alert);
		
		
		
		return view;
		
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ModelAndView dataIntegrityViolationExceptionHandler( Exception ex, Model modelo, HttpSession session) {
		User userLogin = (User)session.getAttribute("user");
		Alert alert = new Alert();
		alert.setLevel(Alert.DANGER);
		alert.setMessage("Database error. Violation integrity data when was to remove result record");
		return getView(new ResultType(),alert,userLogin.getAccount());
	}
}

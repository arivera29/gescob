package com.are.gescob.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.Account;
import com.are.gescob.entity.Alert;
import com.are.gescob.entity.Collector;
import com.are.gescob.entity.User;
import com.are.gescob.model.CollectorRepository;

@Controller
public class CollectorController {

	@Autowired
	CollectorRepository repository;
	
	@GetMapping("/collectors")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new Collector(),new Alert(),user.getAccount());
	}
	
	@PostMapping("/collectors")
	public ModelAndView add(@Valid Collector collector, BindingResult result,
			@RequestParam("action") String action,
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Alert alert = new Alert();
		
		collector.setAccount(user.getAccount());
		
		if (result.hasErrors()) {
			
			switch(action) {
			case "edit":
				model.addAttribute("collector", collector);
				return new ModelAndView("collector_edit",model);
				
			case "add":
				return getView(collector, alert, user.getAccount());
			case "remove":
				model.addAttribute("collector", collector);
				return new ModelAndView("collector_remove",model);
			}

		}

		if (!action.equals("remove")) {
			
			if (action.equals("add")) {
				collector.setCreatedDate(new java.util.Date());
				collector.setCreatedUser(user);
			}
			
			Collector saved = repository.save(collector);
			
			if (saved == null) {
				alert.setLevel(Alert.DANGER);
				alert.setMessage("Error save record");
				
			}else {
				
				alert.setLevel(Alert.INFO);
				alert.setMessage("Record saved");
				
			}
		}else {
			
			repository.delete(collector);
			alert.setLevel(Alert.INFO);
			alert.setMessage("Record removed");
			
		}
		
		
		return getView(new Collector(),alert,user.getAccount());
	}
	
	@GetMapping("/collector/update/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id, 
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Collector collector = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("collector_edit");
		view.addObject("alert", new Alert());
		view.addObject("collector", collector);
		
		return view;
		
	}
	
	@GetMapping("/collector/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Collector collector = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("collector_remove");
		view.addObject("alert", new Alert());
		view.addObject("collector", collector);
		
		return view;
		
	}
	
	public ModelAndView getView (Collector collector, Alert alert, Account account) {
		ModelAndView view = new ModelAndView("collectors");
		
		Iterable<Collector> collectors = repository.findAllOrderByName(account);
		
		view.addObject("collectors", collectors);
		view.addObject("collector", collector);
		view.addObject("alert", alert);
		
		
		
		return view;
		
	}
}

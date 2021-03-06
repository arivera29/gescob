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
import com.are.gescob.entity.User;
import com.are.gescob.entity.Zone;
import com.are.gescob.model.ZoneRepository;

@Controller
public class ZoneController {

	@Autowired
	ZoneRepository repository;
	
	@GetMapping("/zones")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new Zone(),new Alert(),user.getAccount());
	}
	
	@PostMapping("/zones")
	public ModelAndView add(@Valid Zone zone, BindingResult result,
			@RequestParam("action") String action,
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Alert alert = new Alert();
		
		zone.setAccount(user.getAccount());
		
		if (result.hasErrors()) {
			
			switch(action) {
			case "edit":
				model.addAttribute("zone", zone);
				return new ModelAndView("zone_edit",model);
				
			case "add":
				return getView(zone, alert, user.getAccount());
			case "remove":
				model.addAttribute("zone", zone);
				return new ModelAndView("zone_remove",model);
			}

		}

		if (!action.equals("remove")) {
			
			if (action.equals("add")) {
				zone.setCreatedDate(new java.util.Date());
				zone.setCreatedUser(user);
			}
			
			Zone saved = repository.save(zone);
			
			if (saved == null) {
				alert.setLevel(Alert.DANGER);
				alert.setMessage("Error save record");
				
			}else {
				
				alert.setLevel(Alert.INFO);
				alert.setMessage("Record saved");
				
			}
		}else {
			
			repository.delete(zone);
			alert.setLevel(Alert.INFO);
			alert.setMessage("Record removed");
			
		}
		
		
		return getView(new Zone(),alert,user.getAccount());
	}
	
	@GetMapping("/zone/update/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id, 
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Zone zone = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("zone_edit");
		view.addObject("alert", new Alert());
		view.addObject("zone", zone);
		
		return view;
		
	}
	
	@GetMapping("/zone/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Zone zone = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("zone_remove");
		view.addObject("alert", new Alert());
		view.addObject("zone", zone);
		
		return view;
		
	}
	
	public ModelAndView getView (Zone zone, Alert alert, Account account) {
		ModelAndView view = new ModelAndView("zones");
		
		Iterable<Zone> zones = repository.findAllOrderByName(account);
		
		view.addObject("zones", zones);
		view.addObject("zone", zone);
		view.addObject("alert", alert);
		
		
		
		return view;
		
	}
}

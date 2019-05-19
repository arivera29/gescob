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
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.Alert;
import com.are.gescob.entity.User;
import com.are.gescob.entity.Zone;
import com.are.gescob.model.ZoneRepository;

@Controller
public class ZoneController {

	@Autowired
	ZoneRepository repository;
	
	@GetMapping("/zone")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new Zone(),new Alert(),"add");
	}
	
	@PostMapping("/zone")
	public ModelAndView add(@Valid Zone zone, BindingResult result, 
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		zone.setAccount(user.getAccount());
		
		if (result.hasErrors()) {
			return getView(zone,new Alert(),"add");
		}
		
		Zone saved = repository.save(zone);
		if (saved == null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Error save record");
			
			return getView(zone,alert,"add");
		}
		
		
		return getView(new Zone(),new Alert(),"add");
	}
	
	@GetMapping("/zone/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id) {
		
		Zone zone = repository.findById(id).get();
		
		return getView(zone,new Alert(),"edit");
		
	}
	
	@GetMapping("/zone/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Zone zone = repository.findById(id).get();
		
		return getView(zone,new Alert(),"remove");
		
	}
	
	@PostMapping("/zone/{id}")
	public ModelAndView save(@PathVariable("id") Long id,@Valid Zone zone, 
			BindingResult result,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		if (result.hasErrors()) {
			return getView(zone,new Alert(),"edit");
		}
		
		zone.setAccount(user.getAccount());
		
		repository.save(zone);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage("Record saved");
		
		
		return getView(new Zone(),alert,"add");
		
	}
	
	@PostMapping("/zone/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id, ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Zone zone = repository.findById(id).get();
		if (zone ==  null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Record no found");
			return getView(new Zone(), alert, "add");
		}
		
		repository.delete(zone);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage("Record removed");
		
		
		
		Iterable<Zone> zones = repository.findAllOrderByName();
		
		model.addAttribute("zones", zones);
		model.addAttribute("zone", new Zone());
		model.addAttribute("alert", alert);
		model.addAttribute("labelActionButton", "Add");
		
		
		
		return new ModelAndView("redirect:/zone", model);
		
	}
	
	
	public ModelAndView getView (Zone zone, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("profile");
		
		Iterable<Zone> zones = repository.findAllOrderByName();
		
		view.addObject("zones", zones);
		view.addObject("zone", zone);
		view.addObject("alert", alert);
		
		switch (mode) {
		case "add":
			view.addObject("labelActionButton", "Add");
			break;
		case "edit":
			view.addObject("labelActionButton", "Update");
			break;
		case "remove":
			view.addObject("labelActionButton", "Remove");
			break;
		default:
			
		}
		
		return view;
		
	}
}

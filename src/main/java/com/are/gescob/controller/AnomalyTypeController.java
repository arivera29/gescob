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
import com.are.gescob.entity.AnomalyType;
import com.are.gescob.entity.User;
import com.are.gescob.model.AnomalyTypeRepository;

@Controller
public class AnomalyTypeController {
	
	@Autowired
	AnomalyTypeRepository repository;
	
	@GetMapping("/anomaly_type")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new AnomalyType(),new Alert(),"add");
	}
	
	@PostMapping("/anomaly_type")
	public ModelAndView add(@Valid AnomalyType at, BindingResult result,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		if (result.hasErrors()) {
			return getView(at,new Alert(),"add");
		}
		
		at.setAccount(user.getAccount());
		
		AnomalyType saved = repository.save(at);
		if (saved == null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Error save record");
			
			return getView(at,alert,"add");
		}
		
		
		return getView(new AnomalyType(),new Alert(),"add");
	}
	
	@GetMapping("/anomaly_type/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		AnomalyType at = repository.findById(id).get();
		
		return getView(at,new Alert(),"edit");
		
	}
	
	@GetMapping("/anomaly_type/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		AnomalyType at = repository.findById(id).get();
		
		return getView(at,new Alert(),"remove");
		
	}
	
	@PostMapping("/anomaly_type/{id}")
	public ModelAndView save(@PathVariable("id") Long id,@Valid AnomalyType at, 
			BindingResult result,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		if (result.hasErrors()) {
			return getView(at,new Alert(),"edit");
		}
		
		at.setAccount(user.getAccount());
		
		repository.save(at);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage("Record saved");
		
		
		return getView(new AnomalyType(),alert,"add");
		
	}
	
	@PostMapping("/anomaly_type/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id, ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		AnomalyType at = repository.findById(id).get();
		if (at ==  null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Record no found");
			return getView(new AnomalyType(), alert, "add");
		}
		
		repository.delete(at);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage("Record removed");
		
		
		
		Iterable<AnomalyType> ats = repository.findAllOrderByName();
		
		model.addAttribute("ats", ats);
		model.addAttribute("at", new AnomalyType());
		model.addAttribute("alert", alert);
		model.addAttribute("labelActionButton", "Add");
		
		
		
		return new ModelAndView("redirect:/anomaly_type", model);
		
	}
	
	
	public ModelAndView getView (AnomalyType at, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("anomaly_type");
		
		Iterable<AnomalyType> ats = repository.findAllOrderByName();
		
		view.addObject("ats", ats);
		view.addObject("at", at);
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

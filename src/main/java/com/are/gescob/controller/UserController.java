package com.are.gescob.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.User;
import com.are.gescob.model.Alert;
import com.are.gescob.model.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	UserRepository repository;
	
	@GetMapping("/user")
	public ModelAndView home() {
		return getView(new User(),new Alert(),"add");
	}
	
	@PostMapping("/user")
	public ModelAndView add(@Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return getView(user,new Alert(),"add");
		}
		
		User saved = repository.save(user);
		if (saved == null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.LEVEL_DANGER);
			alert.setMessage("Error save record");
			
			return getView(user,alert,"add");
		}
		
		
		return getView(new User(),new Alert(),"add");
	}
	
	@GetMapping("/user/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id) {
		
		User user = repository.findById(id).get();
		
		return getView(user,new Alert(),"edit");
		
	}
	
	@GetMapping("/user/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id) {
		
		User user = repository.findById(id).get();
		
		return getView(user,new Alert(),"remove");
		
	}
	
	@PostMapping("/user/{id}")
	public ModelAndView save(@PathVariable("id") Long id,@Valid User user, BindingResult result) {
		
		if (result.hasErrors()) {
			return getView(user,new Alert(),"edit");
		}
		
		repository.save(user);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.LEVEL_INFO);
		alert.setMessage("Record saved");
		
		
		return getView(new User(),alert,"add");
		
	}
	
	@PostMapping("/user/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id, ModelMap model ) {
		
		User user = repository.findById(id).get();
		if (user ==  null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.LEVEL_DANGER);
			alert.setMessage("Record no found");
			return getView(new User(), alert, "add");
		}
		
		repository.delete(user);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.LEVEL_INFO);
		alert.setMessage("Record removed");
		
		
		
		Iterable<User> users = repository.findAllOrderByName();
		
		model.addAttribute("users", users);
		model.addAttribute("user", new User());
		model.addAttribute("alert", alert);
		model.addAttribute("labelActionButton", "Add");
		
		
		
		return new ModelAndView("redirect:/user", model);
		
	}
	
	
	public ModelAndView getView (User user, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("user");
		
		Iterable<User> users = repository.findAllOrderByName();
		
		view.addObject("users", users);
		view.addObject("user", user);
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

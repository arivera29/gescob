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
import com.are.gescob.entity.Collection;
import com.are.gescob.entity.User;
import com.are.gescob.model.CollectionRepository;

@Controller
public class CollectionController {

	@Autowired
	CollectionRepository repository;
	
	@GetMapping("/collection")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new Collection(),new Alert(),"add");
	}
	
	@PostMapping("/collection")
	public ModelAndView add(@Valid Collection collection, 
			BindingResult result,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		if (result.hasErrors()) {
			return getView(collection,new Alert(),"add");
		}
		collection.setAccount(user.getAccount());
		
		Collection saved = repository.save(collection);
		if (saved == null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Error save record");
			
			return getView(collection,alert,"add");
		}
		
		
		return getView(new Collection(),new Alert(),"add");
	}
	
	@GetMapping("/collection/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Collection collection = repository.findById(id).get();
		
		return getView(collection,new Alert(),"edit");
		
	}
	
	@GetMapping("/collection/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Collection collection = repository.findById(id).get();
		
		return getView(collection,new Alert(),"remove");
		
	}
	
	@PostMapping("/collection/{id}")
	public ModelAndView save(@PathVariable Long id,
			@Valid Collection collection, 
			BindingResult result,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		if (result.hasErrors()) {
			return getView(collection,new Alert(),"edit");
		}
		
		collection.setAccount(user.getAccount());
		
		repository.save(collection);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage("Record saved");
		
		
		return getView(new Collection(),alert,"add");
		
	}
	
	@PostMapping("/collection/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id, 
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Collection collection = repository.findById(id).get();
		if (collection ==  null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Record no found");
			return getView(new Collection(), alert, "add");
		}
		
		repository.delete(collection);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage("Record removed");
		
		
		
		Iterable<Collection> collections = repository.findAllOrderByName();
		
		model.addAttribute("collections", collections);
		model.addAttribute("collection", new Collection());
		model.addAttribute("alert", alert);
		model.addAttribute("labelActionButton", "Add");
		
		
		
		return new ModelAndView("redirect:/collection", model);
		
	}
	
	
	public ModelAndView getView (Collection collection, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("collection");
		
		Iterable<Collection> collections = repository.findAllOrderByName();
		
		view.addObject("collections", collections);
		view.addObject("collection", collection);
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

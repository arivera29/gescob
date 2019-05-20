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
import com.are.gescob.entity.Collection;
import com.are.gescob.entity.User;
import com.are.gescob.model.ClientRepository;
import com.are.gescob.model.CollectionRepository;
import com.are.gescob.model.ZoneRepository;

@Controller
public class CollectionController {

	@Autowired
	CollectionRepository repository;
	
	@Autowired
	ZoneRepository zoneRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	@GetMapping("/collections")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new Collection(),new Alert(),user.getAccount());
	}
	
	@PostMapping("/collections")
	public ModelAndView add(@Valid Collection collection, BindingResult result,
			@RequestParam("action") String action,
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Alert alert = new Alert();
		
		collection.setAccount(user.getAccount());
		
		if (result.hasErrors()) {
			
			switch(action) {
			case "edit":
				model.addAttribute("collection", collection);
				model.addAttribute("zones", zoneRepository.findAllOrderByName(user.getAccount()));
				model.addAttribute("clients", clientRepository.findAllOrderByName(user.getAccount()));
				return new ModelAndView("collection_edit",model);
				
			case "add":
				return getView(collection, alert, user.getAccount());
			case "remove":
				model.addAttribute("collection", collection);
				return new ModelAndView("collection_remove",model);
			}

		}

		if (!action.equals("remove")) {
			
			if (action.equals("add")) {
				collection.setCreatedDate(new java.util.Date());
				collection.setCreatedUser(user);
			}
			
			Collection saved = repository.save(collection);
			
			if (saved == null) {
				alert.setLevel(Alert.DANGER);
				alert.setMessage("Error save record");
				
			}else {
				
				alert.setLevel(Alert.INFO);
				alert.setMessage("Record saved");
				
			}
		}else {
			
			repository.deleteById(collection.getId());
			alert.setLevel(Alert.INFO);
			alert.setMessage("Record removed");
			
		}
		
		
		return getView(new Collection(),alert,user.getAccount());
	}
	
	@GetMapping("/collection/update/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id, 
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Collection collection = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("collection_edit");
		view.addObject("alert", new Alert());
		view.addObject("collection", collection);
		view.addObject("clients", clientRepository.findAllOrderByName(user.getAccount()));
		view.addObject("zones", zoneRepository.findAllOrderByName(user.getAccount()));
		
		return view;
		
	}
	
	@GetMapping("/collection/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Collection collection = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("collection_remove");
		view.addObject("alert", new Alert());
		view.addObject("collection", collection);
		view.addObject("clients", clientRepository.findAllOrderByName(user.getAccount()));
		view.addObject("zones", zoneRepository.findAllOrderByName(user.getAccount()));
		
		return view;
		
	}
	
	public ModelAndView getView (Collection collection, Alert alert, Account account) {
		ModelAndView view = new ModelAndView("collections");
		
		Iterable<Collection> collections = repository.findAllOrderByCreatedDate(account);
		
		view.addObject("collections", collections);
		view.addObject("collection", collection);
		view.addObject("clients", clientRepository.findActives(account));
		view.addObject("zones", zoneRepository.findActives(account));
		view.addObject("alert", alert);
		
		
		
		return view;
		
	}
}

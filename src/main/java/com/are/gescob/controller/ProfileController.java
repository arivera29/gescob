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

import com.are.gescob.entity.Profile;
import com.are.gescob.model.Alert;
import com.are.gescob.model.ProfileRepository;

@Controller
public class ProfileController {
	
	@Autowired
	ProfileRepository repository;
	
	@GetMapping("/profile")
	public ModelAndView home() {
		return getView(new Profile(),new Alert(),"add");
	}
	
	@PostMapping("/profile")
	public ModelAndView add(@Valid Profile profile, BindingResult result) {
		if (result.hasErrors()) {
			return getView(profile,new Alert(),"add");
		}
		
		Profile saved = repository.save(profile);
		if (saved == null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.LEVEL_DANGER);
			alert.setMessage("Error save record");
			
			return getView(profile,alert,"add");
		}
		
		
		return getView(new Profile(),new Alert(),"add");
	}
	
	@GetMapping("/profile/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id) {
		
		Profile profile = repository.findById(id).get();
		
		return getView(profile,new Alert(),"edit");
		
	}
	
	@GetMapping("/profile/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id) {
		
		Profile profile = repository.findById(id).get();
		
		return getView(profile,new Alert(),"remove");
		
	}
	
	@PostMapping("/profile/{id}")
	public ModelAndView save(@PathVariable("id") Long id,@Valid Profile profile, BindingResult result) {
		
		if (result.hasErrors()) {
			return getView(profile,new Alert(),"edit");
		}
		
		repository.save(profile);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.LEVEL_INFO);
		alert.setMessage("Record saved");
		
		
		return getView(new Profile(),alert,"add");
		
	}
	
	@PostMapping("/profile/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id, ModelMap model ) {
		
		Profile profile = repository.findById(id).get();
		if (profile ==  null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.LEVEL_DANGER);
			alert.setMessage("Record no found");
			return getView(new Profile(), alert, "add");
		}
		
		repository.delete(profile);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.LEVEL_INFO);
		alert.setMessage("Record removed");
		
		
		
		Iterable<Profile> profiles = repository.findAllOrderByName();
		
		model.addAttribute("profiles", profiles);
		model.addAttribute("profile", new Profile());
		model.addAttribute("alert", alert);
		model.addAttribute("labelActionButton", "Add");
		
		
		
		return new ModelAndView("redirect:/profile", model);
		
	}
	
	
	public ModelAndView getView (Profile profile, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("profile");
		
		Iterable<Profile> profiles = repository.findAllOrderByName();
		
		view.addObject("profiles", profiles);
		view.addObject("profile", profile);
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

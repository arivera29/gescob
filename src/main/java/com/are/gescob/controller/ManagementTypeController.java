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

import com.are.gescob.entity.ManagementType;
import com.are.gescob.model.Alert;
import com.are.gescob.model.ManagementTypeRepository;

@Controller
public class ManagementTypeController {

	@Autowired
	ManagementTypeRepository repository;
	
	@GetMapping("/management_type")
	public ModelAndView home() {
		return getView(new ManagementType(),new Alert(),"add");
	}
	
	@PostMapping("/management_type")
	public ModelAndView add(@Valid ManagementType mt, BindingResult result) {
		if (result.hasErrors()) {
			return getView(mt,new Alert(),"add");
		}
		
		ManagementType saved = repository.save(mt);
		if (saved == null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.LEVEL_DANGER);
			alert.setMessage("Error save record");
			
			return getView(mt,alert,"add");
		}
		
		
		return getView(new ManagementType(),new Alert(),"add");
	}
	
	@GetMapping("/management_type/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id) {
		
		ManagementType mt = repository.findById(id).get();
		
		return getView(mt,new Alert(),"edit");
		
	}
	
	@GetMapping("/management_type/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id) {
		
		ManagementType mt = repository.findById(id).get();
		
		return getView(mt,new Alert(),"remove");
		
	}
	
	@PostMapping("/management_type/{id}")
	public ModelAndView save(@PathVariable("id") Long id,@Valid ManagementType mt, BindingResult result) {
		
		if (result.hasErrors()) {
			return getView(mt,new Alert(),"edit");
		}
		
		repository.save(mt);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.LEVEL_INFO);
		alert.setMessage("Record saved");
		
		
		return getView(new ManagementType(),alert,"add");
		
	}
	
	@PostMapping("/management_type/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id, ModelMap model ) {
		
		ManagementType mt = repository.findById(id).get();
		if (mt ==  null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.LEVEL_DANGER);
			alert.setMessage("Record no found");
			return getView(new ManagementType(), alert, "add");
		}
		
		repository.delete(mt);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.LEVEL_INFO);
		alert.setMessage("Record removed");
		
		
		
		Iterable<ManagementType> mts = repository.findAllOrderByName();
		
		model.addAttribute("mts", mts);
		model.addAttribute("mt", new ManagementType());
		model.addAttribute("alert", alert);
		model.addAttribute("labelActionButton", "Add");
		
		
		
		return new ModelAndView("redirect:/management_type", model);
		
	}
	
	
	public ModelAndView getView (ManagementType mt, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("management_type");
		
		Iterable<ManagementType> mts = repository.findAllOrderByName();
		
		view.addObject("mts", mts);
		view.addObject("mt", mt);
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

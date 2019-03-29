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

import com.are.gescob.entity.ResultType;
import com.are.gescob.model.Alert;
import com.are.gescob.model.ResultTypeRepository;

@Controller
public class ResultTypeController {
	@Autowired
	ResultTypeRepository repository;
	
	@GetMapping("/result_type")
	public ModelAndView home() {
		return getView(new ResultType(),new Alert(),"add");
	}
	
	@PostMapping("/result_type")
	public ModelAndView add(@Valid ResultType result, BindingResult binder) {
		if (binder.hasErrors()) {
			return getView(result,new Alert(),"add");
		}
		
		ResultType saved = repository.save(result);
		if (saved == null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.LEVEL_DANGER);
			alert.setMessage("Error save record");
			
			return getView(result,alert,"add");
		}
		
		
		return getView(new ResultType(),new Alert(),"add");
	}
	
	@GetMapping("/result_type/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id) {
		
		ResultType result = repository.findById(id).get();
		
		return getView(result,new Alert(),"edit");
		
	}
	
	@GetMapping("/result_type/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id) {
		
		ResultType result = repository.findById(id).get();
		
		return getView(result,new Alert(),"remove");
		
	}
	
	@PostMapping("/result_type/{id}")
	public ModelAndView save(@PathVariable("id") Long id,@Valid ResultType result, BindingResult binder) {
		
		if (binder.hasErrors()) {
			return getView(result,new Alert(),"edit");
		}
		
		repository.save(result);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.LEVEL_INFO);
		alert.setMessage("Record saved");
		
		
		return getView(new ResultType(),alert,"add");
		
	}
	
	@PostMapping("/result_type/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id, ModelMap model ) {
		
		ResultType result = repository.findById(id).get();
		if (result ==  null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.LEVEL_DANGER);
			alert.setMessage("Record no found");
			return getView(new ResultType(), alert, "add");
		}
		
		repository.delete(result);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.LEVEL_INFO);
		alert.setMessage("Record removed");
		
		
		
		Iterable<ResultType> results = repository.findAllOrderByName();
		
		model.addAttribute("results", results);
		model.addAttribute("result", new ResultType());
		model.addAttribute("alert", alert);
		model.addAttribute("labelActionButton", "Add");
		
		
		
		return new ModelAndView("redirect:/result_type", model);
		
	}
	
	
	public ModelAndView getView (ResultType result, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("result_type");
		
		Iterable<ResultType> results = repository.findAllOrderByName();
		
		view.addObject("results", results);
		view.addObject("result", result);
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

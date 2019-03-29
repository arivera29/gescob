package com.are.gescob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.Collection;
import com.are.gescob.model.Alert;
import com.are.gescob.model.CollectionClientRepository;
import com.are.gescob.model.CollectionRepository;

@Controller
public class CollectionClientController {

	@Autowired
	CollectionClientRepository repository;
	@Autowired
	CollectionRepository collectionRepository;
	
	@PostMapping("/collection/upload_clients/{id}")
	public ModelAndView uploadClientes(@PathVariable("id") Long id,
			@RequestParam("file") MultipartFile file) {
		
		
		
		if (file.isEmpty()) {
			return getView(new Collection(), new Alert(Alert.LEVEL_DANGER,"File is empty"), "add");
		}
		
		
		Alert alert = new Alert();
		
		return getView(new Collection(), alert, "add");
	}
	
	
	
	public ModelAndView getView (Collection collection, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("collection");
		
		Iterable<Collection> collections = collectionRepository.findAllOrderByName();
		
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

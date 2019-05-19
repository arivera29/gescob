
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
import com.are.gescob.entity.Client;
import com.are.gescob.entity.User;
import com.are.gescob.model.ClientRepository;

@Controller
public class ClientController {

	@Autowired
	ClientRepository repository;
	
	@GetMapping("/client")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new Client(),new Alert(),"add");
	}
	
	@PostMapping("/client")
	public ModelAndView add(@Valid Client client, 
			BindingResult result,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		if (result.hasErrors()) {
			return getView(client,new Alert(),"add");
		}
		
		Client saved = repository.save(client);
		if (saved == null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Error save record");
			
			return getView(client,alert,"add");
		}
		
		
		return getView(new Client(),new Alert(),"add");
	}
	
	@GetMapping("/client/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Client client = repository.findById(id).get();
		
		return getView(client,new Alert(),"edit");
		
	}
	
	@GetMapping("/client/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Client client = repository.findById(id).get();
		
		return getView(client,new Alert(),"remove");
		
	}
	
	@PostMapping("/client/{id}")
	public ModelAndView save(@PathVariable("id") Long id,
			@Valid Client client, 
			BindingResult result,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		if (result.hasErrors()) {
			return getView(client,new Alert(),"edit");
		}
		
		repository.save(client);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage("Record saved");
		
		
		return getView(new Client(),alert,"add");
		
	}
	
	@PostMapping("/client/remove/{id}")
	public ModelAndView remove(@PathVariable("id") Long id, 
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Client client = repository.findById(id).get();
		if (client ==  null) {
			Alert alert = new Alert();
			alert.setLevel(Alert.DANGER);
			alert.setMessage("Record no found");
			return getView(new Client(), alert, "add");
		}
		
		repository.delete(client);
		
		Alert alert = new Alert();
		alert.setLevel(Alert.INFO);
		alert.setMessage("Record removed");
		
		
		
		Iterable<Client> clients = repository.findAllOrderByName();
		
		model.addAttribute("clients", clients);
		model.addAttribute("client", new Client());
		model.addAttribute("alert", alert);
		model.addAttribute("labelActionButton", "Add");
		
		
		
		return new ModelAndView("redirect:/client", model);
		
	}
	
	
	public ModelAndView getView (Client client, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("client");
		
		Iterable<Client> clients = repository.findAllOrderByName();
		
		view.addObject("clients", clients);
		view.addObject("client", client);
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


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
import com.are.gescob.entity.Client;
import com.are.gescob.entity.User;
import com.are.gescob.model.ClientRepository;

@Controller
public class ClientController {

	@Autowired
	ClientRepository repository;
	
	@GetMapping("/clients")
	public ModelAndView home(HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		return getView(new Client(),new Alert(),user.getAccount());
	}
	
	@PostMapping("/clients")
	public ModelAndView add(@Valid Client client, BindingResult result,
			@RequestParam("action") String action,
			ModelMap model,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Alert alert = new Alert();
		
		client.setAccount(user.getAccount());
		
		if (result.hasErrors()) {
			
			switch(action) {
			case "edit":
				alert.setLevel(Alert.DANGER);
				alert.setMessage(result.getNestedPath());
				model.addAttribute("alert", alert);
				model.addAttribute("client", client);
				
				return new ModelAndView("client_edit",model);
				
			case "add":
				return getView(client, alert, user.getAccount());
			case "remove":
				model.addAttribute("client", client);
				return new ModelAndView("client_remove",model);
			}

		}

		if (!action.equals("remove")) {
			
			if (action.equals("add")) {
				client.setCreatedDate(new java.util.Date());
				client.setCreatedUser(user);
			}
			
			Client saved = repository.save(client);
			
			if (saved == null) {
				alert.setLevel(Alert.DANGER);
				alert.setMessage("Error save record");
				
			}else {
				
				alert.setLevel(Alert.INFO);
				alert.setMessage("Record saved");
				
			}
		}else {
			
			repository.delete(client);
			alert.setLevel(Alert.INFO);
			alert.setMessage("Record removed");
			
		}
		
		
		return getView(new Client(),alert,user.getAccount());
	}
	
	@GetMapping("/client/update/{id}")
	public ModelAndView findUpdate(@PathVariable("id") Long id, 
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Client client = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("client_edit");
		view.addObject("alert", new Alert());
		view.addObject("client", client);
		
		return view;
		
	}
	
	@GetMapping("/client/remove/{id}")
	public ModelAndView findRemove(@PathVariable("id") Long id,
			HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if (!user.getRole().equals("ADM")) {
			return new ModelAndView("redirect:access_denied");
		}
		
		Client client = repository.findById(id).get();
		
		ModelAndView view = new ModelAndView("client_remove");
		view.addObject("alert", new Alert());
		view.addObject("client", client);
		
		return view;
		
	}
	
	public ModelAndView getView (Client client, Alert alert, Account account) {
		ModelAndView view = new ModelAndView("clients");
		
		Iterable<Client> clients = repository.findAllOrderByName(account);
		
		view.addObject("clients", clients);
		view.addObject("client", client);
		view.addObject("alert", alert);
		
		
		
		return view;
		
	}
}

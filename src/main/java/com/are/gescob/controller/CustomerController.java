package com.are.gescob.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.are.gescob.entity.Alert;
import com.are.gescob.entity.Collection;
import com.are.gescob.entity.Customer;
import com.are.gescob.model.CustomerRepository;
import com.are.gescob.model.CollectionRepository;
import com.opencsv.CSVReader;

@Controller
public class CustomerController {

	private static String UPLOADED_FOLDER = "/temp";

	@Autowired
	CustomerRepository repository;
	@Autowired
	CollectionRepository collectionRepository;

	@PostMapping("/collection/upload_clients/{id}")
	public ModelAndView uploadClientes(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws ParseException {

		if (file.isEmpty()) {
			return getView(new Collection(), new Alert(Alert.DANGER, "File is empty"), "add");
		}

		Path path = null;

		try {

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);

		} catch (IOException e) {
			return getView(new Collection(), new Alert(Alert.DANGER, e.getMessage()), "add");
		}

		if (path != null) {

			try (CSVReader csvReader = new CSVReader(new FileReader(new File(path.toString())))) {

				String[] nextRecord;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				while ((nextRecord = csvReader.readNext()) != null) {
					// Save CollectionClient entity
					Customer entity = new Customer();
					entity.setNumCamp(nextRecord[0]);
					
					entity.setFechaEntrega(sdf.parse(nextRecord[1]));
					entity.setUnicom(nextRecord[2]);
					entity.setNic(nextRecord[3]);
					entity.setNis(nextRecord[4]);
					
					

				}
			} catch (FileNotFoundException e) {
				return getView(new Collection(), new Alert(Alert.DANGER, e.getMessage()), "add");
			} catch (IOException e) {

				return getView(new Collection(), new Alert(Alert.DANGER, e.getMessage()), "add");
			}

			try {
				// Delete file upload
				Files.delete(path);
			} catch (IOException e) {
				return getView(new Collection(), new Alert(Alert.DANGER, e.getMessage()), "add");
			}

		}

		Alert alert = new Alert();

		return getView(new Collection(), alert, "add");
	}

	public ModelAndView getView(Collection collection, Alert alert, String mode) {
		ModelAndView view = new ModelAndView("collection");

		//Iterable<Collection> collections = collectionRepository.findAllOrderByName();

		//view.addObject("collections", collections);
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

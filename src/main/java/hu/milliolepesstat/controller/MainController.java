package hu.milliolepesstat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hu.milliolepesstat.service.SchoolService;

@Controller
public class MainController {

	@Autowired
	SchoolService schoolService;
	
	@GetMapping("/")
	public String mainPage(Model model) {
		schoolService.addSchoolDataToModel(model);
		schoolService.addCalendarDataToModel(model);
		return "main";
	}
}

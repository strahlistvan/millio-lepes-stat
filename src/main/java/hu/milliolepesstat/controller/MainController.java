package hu.milliolepesstat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hu.milliolepesstat.service.SchoolService;
import hu.milliolepesstat.util.Calculator;

@Controller
public class MainController {

	@Autowired
	SchoolService schoolService;
	
	@GetMapping("/")
	public String mainPage(Model model) {
		schoolService.addSchoolDataToModel(model);
		schoolService.addRefreshDateToModel(model);
		System.out.println("Ennyi nap van hátra még a versenyből: " + Calculator.getRemainingDays());
		return "main";
	}
}

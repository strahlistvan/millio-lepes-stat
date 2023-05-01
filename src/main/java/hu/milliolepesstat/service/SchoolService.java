package hu.milliolepesstat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import hu.milliolepesstat.entity.School;
import hu.milliolepesstat.util.Calculator;
import hu.milliolepesstat.util.WebScraper;

@Service
public class SchoolService {

	public void addSchoolListToModel(Model model) {
		try {
			List<School> schoolList = WebScraper.scapeAllSchools();
			model.addAttribute("schools", schoolList);
			model.addAttribute("allOKK", Calculator.sumAllOKK(schoolList));
			model.addAttribute("allParticipant", Calculator.sumParticipants(schoolList));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
}

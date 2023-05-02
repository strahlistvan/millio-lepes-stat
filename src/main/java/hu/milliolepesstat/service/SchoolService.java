package hu.milliolepesstat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import hu.milliolepesstat.entity.School;
import hu.milliolepesstat.util.Calculator;
import hu.milliolepesstat.util.WebScraper;

@Service
public class SchoolService {

	public void setSchoolsWinProbability(List<School> schoolList, Integer allOkkCount) {
		for (School sch: schoolList) {
			sch.calcWinProbability(allOkkCount);
			sch.calcprizeExpectedValue(allOkkCount);
		}
	}

	public void addSchoolListToModel(Model model) {
		try {
			List<School> schoolList = WebScraper.scapeAllSchools();
			Integer allOkkCount = Calculator.sumAllOKK(schoolList);
			this.setSchoolsWinProbability(schoolList, allOkkCount);
			model.addAttribute("schools", schoolList);
			model.addAttribute("allOKK", allOkkCount);
			model.addAttribute("allParticipant", Calculator.sumParticipants(schoolList));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

	
}

package hu.milliolepesstat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import hu.milliolepesstat.entity.School;
import hu.milliolepesstat.util.Calculator;
import hu.milliolepesstat.util.WebScraper;

@Service
public class SchoolService {

	public void setSchoolsCalculatedData(List<School> schoolList, Integer allOkkCount) {
		for (School sch: schoolList) {
			sch.calcWinProbability(allOkkCount);
			sch.calcprizeExpectedValue(allOkkCount);

			Long steps = Calculator.convertOkkToSteps(sch.getOkkNumber());
			Long dist = Calculator.getBestSchoolSteps(schoolList) - steps;
			sch.setDistanceFromBestSteps(dist);

			Long dailyStepsNeed = Long.MAX_VALUE;
			if (sch.getParticipants() > 0 && Calculator.getRemainingDays() > 0) {
				dailyStepsNeed = dist / ( sch.getParticipants() * Calculator.getRemainingDays() );
			}
			sch.setDailyStepsNeedToBest(dailyStepsNeed);
		}
	}

	public void addSchoolDataToModel(Model model) {
		try {
			List<School> schoolList = WebScraper.scapeAllSchools();
			Integer allOkkCount = Calculator.sumAllOKK(schoolList);
			this.setSchoolsCalculatedData(schoolList, allOkkCount);
			model.addAttribute("schools", schoolList);
			model.addAttribute("allOKK", allOkkCount);
			model.addAttribute("allParticipant", Calculator.sumParticipants(schoolList));
			
			Double allDistanceInOkk = Calculator.sumAllDistance(schoolList);
			model.addAttribute("allDistanceInOkk", allDistanceInOkk);
			model.addAttribute("allDistanceInKm", Calculator.convertOkkToKm(allDistanceInOkk));
			model.addAttribute("allDistanceInSteps", Calculator.convertOkkToSteps(allDistanceInOkk));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addRefreshDateToModel(Model model) {
		try {
			model.addAttribute("refreshDate", WebScraper.scrapeRefreshDate());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}

package hu.milliolepesstat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import hu.milliolepesstat.entity.School;
import hu.milliolepesstat.util.Calculator;
import hu.milliolepesstat.util.DateUtil;
import hu.milliolepesstat.util.WebScraper;

@Service
public class SchoolService {

	/** Just some forecasting, but not really precise... */	
	private void setSchoolsEstimatedData(List<School> schoolList) {
		Long bestSchoolFinalSteps = Calculator.getBestSchoolFinalSteps(schoolList);
		
		for (School sch: schoolList) {
			Long estSteps = Calculator.convertOkkToSteps(sch.getEstimatedFinalOkk());
			Long estDistFromBest = bestSchoolFinalSteps - estSteps;
			sch.setEstimatedFinalDistanceFromBestSteps(estDistFromBest);
		}
	}
	
	public void setSchoolsCalculatedData(List<School> schoolList, Integer allOkkCount) {
		Integer remainingDays = DateUtil.getRemainingDays();
		Integer elapsedDays = DateUtil.getElapsedDays();

		for (School sch: schoolList) {

			sch.calcAverageDailySteps(elapsedDays);
			sch.calcWinProbability(allOkkCount);
			sch.calcprizeExpectedValue(allOkkCount);

			Long steps = Calculator.convertOkkToSteps(sch.getOkkNumber());
			Long dist = Calculator.getBestSchoolSteps(schoolList) - steps;
			sch.setDistanceFromBestSteps(dist);

			Long dailyStepsNeed = Long.MAX_VALUE;
			if (sch.getParticipants() > 0 && remainingDays > 0) {
				dailyStepsNeed = dist / ( sch.getParticipants() * remainingDays );
			}
			sch.setDailyStepsNeedToBest(dailyStepsNeed);

			sch.calcEstimatedFinalOkk(elapsedDays, remainingDays);
		}
		
		// call the prediction process
		setSchoolsEstimatedData(schoolList);
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
	
	public void addCalendarDataToModel(Model model) {
		try {
			model.addAttribute("refreshDate", WebScraper.scrapeRefreshDate());
			model.addAttribute("remainingDays", DateUtil.getRemainingDays());
			model.addAttribute("elapsedDays", DateUtil.getElapsedDays());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}

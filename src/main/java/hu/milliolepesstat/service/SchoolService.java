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

			/* Just some forecasting, but not really precise... */
			sch.calcEstimatedFinalOkk(elapsedDays, remainingDays);

			//this is the bonus distance that the school MUST COMPLETE to win
			Long bonusStepsNeed = Long.MAX_VALUE;
			Long estSteps = Calculator.convertOkkToSteps(sch.getEstimatedFinalOkk());
			Long estDistFromBest = Calculator.getBestSchoolFinalSteps(schoolList) - estSteps;
			sch.setEstimatedFinalDistanceFromBestSteps(estDistFromBest);
			
			if (sch.getParticipants() > 0 && remainingDays > 0) {
				bonusStepsNeed = estDistFromBest / ( sch.getParticipants() * remainingDays );
			}

			// this is the distance that the school WILL COMPLETE
			Integer participants = sch.getParticipants() == 0 ? 1 : sch.getParticipants();
			Long estimatedSteps = Calculator.convertOkkToSteps(sch.getEstimatedFinalOkk() - sch.getOkkNumber()) / participants;
			
			if (estDistFromBest != 0) {
				sch.setEstimatedDailyStepsNeedToBestFinal(bonusStepsNeed + estimatedSteps);

			}
			else {
				sch.setEstimatedDailyStepsNeedToBestFinal(0L);
			}
			
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

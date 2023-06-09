package hu.milliolepesstat.util;

import java.util.*;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

import hu.milliolepesstat.entity.School;

public class Calculator {

	public static final Double STEP_SIZE_IN_METERS = 0.7;
	public static final Double DIST_OKK_IN_METERS = 2550000.0;
	public static final Integer PRIZE_COUNT = 100;
	public static final Integer PRIZE_AMOUNT_FT = 1000000;

	/** Sums up the all participants of the competition */
	public static Integer sumParticipants(List<School> schoolList) {
		return schoolList.stream().map(item -> item.getParticipants()).mapToInt(Integer::intValue).sum();
	}
	
	/** Sums up the schools finished "Országos Kék Kör" - every OKK means 1 ticket in the lottery  */
	public static Integer sumAllOKK(List<School> schoolList) {
		return schoolList.stream().map(item -> (int) Math.floor(item.getOkkNumber())).mapToInt(Integer::intValue).sum();
	}

	/** Sums up the schools distance 
	 *  Return measurement is OKK = "Országos Kék Kör"
	 **/
	public static Double sumAllDistance(List<School> schoolList) {
		return schoolList.stream().map(item -> item.getOkkNumber()).mapToDouble(Double::doubleValue).sum();
	}
	
	/** Calculate the probability to win at least one prize */
	public static Double calcWinProbability(Integer myTicketCount, Integer allTicketCount) {
		// Error handling
		if (allTicketCount < PRIZE_COUNT) {
			allTicketCount = PRIZE_COUNT;
		}
		if (myTicketCount == 0) {
			return 0.0;
		}

		HypergeometricDistribution distr = new HypergeometricDistribution(allTicketCount, PRIZE_COUNT, myTicketCount);
		return 1.0 - distr.probability(0);
	}

	/** Calculate the probability to win exactly N prize (N=exactWinCount) */
	private static Double calcWinCountProbability(Integer myTicketCount, Integer allTicketCount, Integer exactWinCount) {
		// Error handling
		if (allTicketCount < PRIZE_COUNT) {
			allTicketCount = PRIZE_COUNT;
		}
		if (exactWinCount > PRIZE_COUNT) {
			exactWinCount = PRIZE_COUNT;
		}

		HypergeometricDistribution distr = new HypergeometricDistribution(allTicketCount, PRIZE_COUNT, myTicketCount);
		return distr.probability(exactWinCount);

	}
	
	/** How many prizes can we expect with the given ticket counts? 
	 *  Weighted sum of prize counts multiply by the probability 
	 **/
	public static Double calcPrizeExcpectedValueCheck(Integer myTicketCount, Integer allTicketCount) {
		Double expValue = 0.0;
		for (int k = 1; k < myTicketCount; ++k) {
			expValue += (double) k * calcWinCountProbability(myTicketCount, allTicketCount, k);
		}
		return expValue;
	}

	/** How many prizes can we expect with the given ticket counts?
	 *  E(X) = n * (K/N) 
	 *  where n is the sample size K a the success elements and N the population size
	 **/
	public static Double calcPrizeExcpectedValue(Integer myTicketCount, Integer allTicketCount) {
		return (double) myTicketCount * PRIZE_COUNT / allTicketCount;
	}
	
	public static Double convertOkkToKm(Double okk) {
		return DIST_OKK_IN_METERS * okk / 1000.0;
	}

	public static Long convertOkkToSteps(Double okk) {
		return (long) Math.floor(DIST_OKK_IN_METERS * STEP_SIZE_IN_METERS * okk);
	}

	public static Double convertStepsToOkk(Long steps) {
		return (double) steps / DIST_OKK_IN_METERS / STEP_SIZE_IN_METERS;
	}
	
	public static Long getBestSchoolSteps(List<School> schoolList) {
		Long result = 0L;
		if (schoolList != null && schoolList.size() > 0) {
				result = convertOkkToSteps(schoolList.get(0).getOkkNumber()); // the list sorted by distance
		}
		return result;
	}

	private static List<School> getSortedSchoolListByEstimatedFinalOkk(List<School> schoolList) {
		List<School> schoolListSortable = new ArrayList<School>();
		schoolListSortable.addAll(schoolList);

		Comparator<School> comparator = new Comparator<School>() {
			@Override
			public int compare(School sch1, School sch2) {
				return Double.compare(sch2.getEstimatedFinalOkk(), sch1.getEstimatedFinalOkk());
			}
		};

		schoolListSortable.sort(comparator);
		return schoolListSortable;
	}
	
	public static Long getBestSchoolFinalSteps(List<School> schoolList) {
		Long result = 0L;
		if (schoolList != null && schoolList.size() > 0) {
			List<School> schoolListSorted = getSortedSchoolListByEstimatedFinalOkk(schoolList);
			result = convertOkkToSteps(schoolListSorted.get(0).getEstimatedFinalOkk());
		}
		return result;
	}

}

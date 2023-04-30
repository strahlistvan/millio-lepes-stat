package hu.milliolepesstat.util;

import java.util.List;

import hu.milliolepesstat.entity.School;

public class Calculator {

	public static final Double STEP_SIZE_IN_METERS = 0.7;
	public static final Double DIST_OKK_IN_METERS = 2550000.0;
	public static final Integer PRIZECOUNT = 5;


	public static Integer sumAllOKK(List<School> schoolList) {
		return schoolList.stream().map(item -> (int) Math.floor(item.getOkkNumber())).reduce(0, Integer::sum);
	}

	/** Returns value of Binomial Coefficient C(n, k) */
	public static Integer binomialCoeff(Integer n, Integer k)
	{

		if (k > n) {
			return 0;
		}
		if (k == 0 || k == n) {
			return 1;
		}
 
		return binomialCoeff(n - 1, k - 1) + binomialCoeff(n - 1, k);
	}
	
	/** Calculate the probability to win at least one prize */
	public static Double calcWinProbability(Integer myTicketCount, Integer allTicketCount) {
		// Error handling
		if (allTicketCount < PRIZECOUNT) {
			allTicketCount = PRIZECOUNT;
		}

		Integer failureCounts = binomialCoeff(allTicketCount - PRIZECOUNT, myTicketCount);
		Integer allCaseCounts = binomialCoeff(allTicketCount, myTicketCount);
		Double failureProbability = (double) failureCounts / allCaseCounts;
		return 1.0 - failureProbability;
	}

	/** Calculate the probability to win exactly N prize (N=exactWinCount) */
	public static Double calcWinCountProbability(Integer myTicketCount, Integer allTicketCount, Integer exactWinCount) {
		// Error handling
		if (allTicketCount < PRIZECOUNT) {
			allTicketCount = PRIZECOUNT;
		}
		if (exactWinCount < PRIZECOUNT) {
			exactWinCount = PRIZECOUNT;
		}

		Integer successCounts = binomialCoeff(PRIZECOUNT, exactWinCount);
		Integer failureCounts = binomialCoeff(allTicketCount - PRIZECOUNT, myTicketCount - exactWinCount);
		Integer allCaseCounts = binomialCoeff(allTicketCount, myTicketCount);
		
		Double probability =  (double) (successCounts * failureCounts) / allCaseCounts;

		return probability;
	}
	
	/** How many prizes can we expect with the given ticket counts? */
	public static Double calcPrizeExcpectedValue(Integer myTicketCount, Integer allTicketCount) {
		Double expValue = 0.0;
		for (int k = 1; k < myTicketCount; ++k) {
			expValue += k * calcWinCountProbability(myTicketCount, allTicketCount, k);
		}
		return expValue;
	}

}

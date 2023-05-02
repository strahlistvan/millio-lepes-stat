package hu.milliolepesstat.util;

import java.util.List;

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

	// BAD PERFORMANCE !
/*	public static Integer binomialCoeff(Integer n, Integer k) {

		if (k > n) {
			return 0;
		}
		if (k == 0 || k == n) {
			return 1;
		}
 
		return binomialCoeff(n - 1, k - 1) + binomialCoeff(n - 1, k);
	}
*/
	/** Returns value of Binomial Coefficient C(n, k) */
	public static Long binomialCoeff(Long n, Long k) {
		
		if (k > n) {
			return 0L;
		}
		if (k == 0 || k == n) {
			return 1L;
		}
		
		Long numerator = 1L, denominator = 1L;
		for (long i = n; i > n - k; --i) {
			numerator *= i;
		}
		for (long i = 2; i <= k; ++i) {
			denominator *= i;
		}
		System.out.println(numerator + "/" + denominator );
		return (denominator == 0) ? 0 : numerator / denominator;
		
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
		
		//Long failureCounts = binomialCoeff((long) (allTicketCount - PRIZECOUNT), (long) myTicketCount);
		//Long allCaseCounts = binomialCoeff((long) allTicketCount, (long) myTicketCount);
		//Double failureProbability = (double) failureCounts / allCaseCounts;
		
		System.out.println("allTicketCount = " + allTicketCount + " Prizecount = " + PRIZE_COUNT + "myTicketcont = " + myTicketCount);
		HypergeometricDistribution distr = new HypergeometricDistribution(allTicketCount, PRIZE_COUNT, myTicketCount);
		
		return 1.0 - distr.probability(0);
	}

	/** Calculate the probability to win exactly N prize (N=exactWinCount) */
	public static Double calcWinCountProbability(Integer myTicketCount, Integer allTicketCount, Integer exactWinCount) {
		// Error handling
		if (allTicketCount < PRIZE_COUNT) {
			allTicketCount = PRIZE_COUNT;
		}
		if (exactWinCount > PRIZE_COUNT) {
			exactWinCount = PRIZE_COUNT;
		}

	//	Long successCounts = binomialCoeff((long)PRIZECOUNT, (long)exactWinCount);
//		Long failureCounts = binomialCoeff((long)(allTicketCount - PRIZECOUNT), (long)(myTicketCount - exactWinCount));
//		Long allCaseCounts = binomialCoeff((long) allTicketCount, (long)myTicketCount);

		HypergeometricDistribution distr = new HypergeometricDistribution(allTicketCount, PRIZE_COUNT, myTicketCount);
		System.out.println("allTicketCount = " + allTicketCount + " Prizecount = " + PRIZE_COUNT 
				+ "myTicketcont = " + myTicketCount
				+ " az esély pontosan " + exactWinCount + " nyereményre: " + exactWinCount );
		return distr.probability(exactWinCount);

	}
	
	/** How many prizes can we expect with the given ticket counts? 
	 *  Weighted sum of prize counts multiply by the probability 
	 **/
	public static Double calcPrizeExcpectedValue(Integer myTicketCount, Integer allTicketCount) {
		Double expValue = 0.0;
		for (int k = 1; k < myTicketCount; ++k) {
			expValue += (double) k * calcWinCountProbability(myTicketCount, allTicketCount, k);
		}
		return expValue;
	}

	/** How many prizes can we expect with the given ticket counts?
	 * E(X) = n * (K/N) 
	 * where n is the sample size K a the success elements and N the population size
	 *  */
	public static Double calcPrizeExcpectedValue2(Integer myTicketCount, Integer allTicketCount) {
		return (double) myTicketCount * PRIZE_COUNT / allTicketCount;
	}
	
}

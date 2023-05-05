package hu.milliolepesstat.entity;

import hu.milliolepesstat.util.Calculator;
import lombok.*;

@Data
public class School {
	private String id;
	private Integer position;
	private String name;
	private Double okkNumber;
	private Integer participants;
	private String city;
	private String county;

	@Setter(AccessLevel.NONE)
	private Double winProbability;
	
	@Setter(AccessLevel.NONE)
	private Integer prizeExpectedValue;

	@Setter(AccessLevel.NONE)
	private Double averageDailiySteps;
	
	private Long distanceFromBestSteps;
	private Long dailyStepsNeedToBest;

	@Setter(AccessLevel.NONE)
	private Double estimatedFinalOkk;
	
	private Long estimatedFinalDistanceFromBestSteps;
	private Long estimatedDailyStepsNeedToBestFinal;

	/** It is NOT a simple setter! Calculates probability by given parameters in Calculator class. */
	public void calcWinProbability(Integer allOkkNumber) {
		this.winProbability = 100 * Calculator.calcWinProbability((int) Math.floor(this.okkNumber), allOkkNumber);
	}

	public void calcAverageDailySteps(Integer elapsedDays) {
		this.averageDailiySteps = (double) Calculator.convertOkkToSteps(this.okkNumber) / (this.participants * elapsedDays); 
	}

	/** It is NOT a simple setter! Calculate the expected value of the prize in Forint  */
	public void calcprizeExpectedValue(Integer allOkkNumber) {
		Double expectedPrizeCount = Calculator.calcPrizeExcpectedValue((int) Math.floor(this.okkNumber), allOkkNumber);
		this.prizeExpectedValue = (int) Math.round(Calculator.PRIZE_AMOUNT_FT * expectedPrizeCount) ;
	}

	/** It is NOT a simple setter! Calculate estimated final distance. */
	public void calcEstimatedFinalOkk(Integer elapsedDays, Integer remainingDays) {
		this.estimatedFinalOkk = ( 1.0 + (double) remainingDays / elapsedDays ) * this.okkNumber;
	}

}


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
	
	/** It is NOT a simple setter! Calculates probability by given parameters in Calculator class. */
	public void calcWinProbability(Integer allOkkNumber) {
		this.winProbability = 100 * Calculator.calcWinProbability((int) Math.floor(this.okkNumber), allOkkNumber);
	}
	
	/** It is NOT a simple setter! The excpected value of the . */
	public void calcprizeExpectedValue(Integer allOkkNumber) {
		Double expectedPrizeCount = Calculator.calcPrizeExcpectedValue2((int) Math.floor(this.okkNumber), allOkkNumber);
		this.prizeExpectedValue = (int) Math.round(Calculator.PRIZE_AMOUNT_FT * expectedPrizeCount) ;
	}
}


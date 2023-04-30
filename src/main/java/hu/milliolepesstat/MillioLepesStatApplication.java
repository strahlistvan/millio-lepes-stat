package hu.milliolepesstat;

import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.milliolepesstat.entity.School;
import hu.milliolepesstat.util.Calculator;
import hu.milliolepesstat.util.WebScraper;

@SpringBootApplication
public class MillioLepesStatApplication {

	public static void main(String[] args) {
		List<School> schoolList = new ArrayList<School>();
		try {
			schoolList = WebScraper.scape();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println( Calculator.calcWinProbability(3, 100));
		System.out.println( "Ennyi kékkört tettek már meg: " + Calculator.sumAllOKK(schoolList) );
		SpringApplication.run(MillioLepesStatApplication.class, args);
	}

}

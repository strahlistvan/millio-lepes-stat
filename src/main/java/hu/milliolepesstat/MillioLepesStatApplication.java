package hu.milliolepesstat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.milliolepesstat.util.WebScraper;

@SpringBootApplication
public class MillioLepesStatApplication {

	public static void main(String[] args) {
		try {
			WebScraper.scape();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SpringApplication.run(MillioLepesStatApplication.class, args);
	}

}

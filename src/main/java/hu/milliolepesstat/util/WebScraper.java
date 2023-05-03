package hu.milliolepesstat.util;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import hu.milliolepesstat.entity.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;


public class WebScraper {

	private static School scrapeSchool(DomNode schoolNode) {
		School school = new School();
		school.setId(schoolNode.getAttributes().getNamedItem("id").getNodeValue());
		
		DomNodeList<DomNode> schoolData = schoolNode.getChildNodes();

		for (DomNode node: schoolData) {
			
			DomNode classNode = (DomNode) node.getAttributes().getNamedItem("class");
			if (classNode != null) {
				String cls = classNode.getNodeValue();
				if ("poz".equals(cls)) {
					Integer position = Integer.parseInt(node.getTextContent());
					school.setPosition(position);
				}
				else if ("school".equals(cls)) {
					String schoolName = node.getFirstChild().getTextContent();
					school.setName(schoolName);
					for (DomNode schoolDetails: node.getChildNodes()) {
						if ( schoolDetails.getAttributes().getNamedItem("class") != null 
							&& "sct".equals(schoolDetails.getAttributes().getNamedItem("class").getNodeValue()) ) 
						{
							Scanner scanner = new Scanner(schoolDetails.getTextContent());
							Integer participants = scanner.nextInt();
							school.setParticipants(participants);
							scanner.close();
							
							for (DomNode detail: schoolDetails.getChildren()) {
								DomNode actionVal = (DomNode) detail.getAttributes().getNamedItem("onclick");
								if (actionVal != null && actionVal.toString().contains("telepules")) {
									String city = detail.getVisibleText();
									school.setCity(city);
								}
								if (actionVal != null && actionVal.toString().contains("varmegye")) {
									String county = detail.getVisibleText().replace("(", "").replace(")", "");
									school.setCounty(county);
								}

							}

						}
					}
				}
				else if ("okk".equals(cls)) {
					Double kkNumber = Double.parseDouble(node.getTextContent());
					school.setOkkNumber(kkNumber);
				}
			}
		}

		return school;
	}

	public static List<School> scapeAllSchools() throws MalformedURLException, IOException  {

		List<School> schoolList = new ArrayList<School>();

		try (WebClient webClient = new WebClient()) {
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			
			String url = "https://milliolepes.hu/iskolak-toplistaja/";
			HtmlPage page = webClient.getPage(url);
			
			DomNodeList<DomNode> schoolNodes = page.querySelectorAll("div.line");
			
			for (DomNode schoolNode: schoolNodes) {
				School school = WebScraper.scrapeSchool(schoolNode);
				schoolList.add(school);
			}
		}
		return schoolList;
	}

	public static String scrapeRefreshDate() throws MalformedURLException, IOException {
		
		String result = "Ismeretlen";
		
		try (WebClient webClient = new WebClient()) {
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			
			String url = "https://milliolepes.hu/iskolak-toplistaja/";
			HtmlPage page = webClient.getPage(url);
			
			result = page.querySelector("strong").getTextContent();						
		}
		return result;
	}
}

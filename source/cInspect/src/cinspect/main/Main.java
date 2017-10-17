package cinspect.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jenkov.crawler.mt.io.CrawlerMT;
import com.jenkov.crawler.util.SameWebsiteOnlyFilter;

import cinspect.inspector.VulnerabilityAssessment;
import cinspect.inspector.XSSInspector;
import cinspect.inspector.LFIInspector;
import cinspect.inspector.RCEInspector;
import cinspect.inspector.RFIInspector;
import cinspect.inspector.SQLInspector;
import cinspect.web.ResourceRequestType;
import cinspect.web.WebDatabase;
import cinspect.web.WebResource;

public class Main {

	public static void main(String[] args) {
		/**
		 * Welcome to the playground, yay.
		 * 
		 * Once you are done prototyping, make sure nothing is broken and commit it. 
		 * If you commit something that is broken you are probably wasting both our time. 
		 * Don't push it without asking me first, please. 
		 */
		
		//Create our inspector object. 

        String url = "http://192.168.1.29/";
        CrawlerMT crawler  = new CrawlerMT(new SameWebsiteOnlyFilter(url));
        crawler.addUrl(url);
        crawler.crawl();
        
        System.out.println("------------ DONE ----------");
        WebDatabase.printDatabase();
		
        System.out.println("\n\n\n\n\n\n\n\n");
		
		List<WebResource> resources = WebDatabase.getDatabase(); //this needs to be updated. 
		
		System.out.println("\n");
		
		for(WebResource resource : resources) {
			System.out.println("Testing : " + resource.getUrlPath() + resource.getParametersAsEncodedString());
			if(!resource.getParameters().isEmpty() ) {
				testSQLInspector(resource);
				testRCEInspector(resource);
				testLFIInspector(resource);
				testXSSInspector(resource);
				//testRFIInspector(resource);
			}
		}
		
		System.out.println("--- DONE ---");
		
	}

	private static void testSQLInspector(WebResource resource) {
		ArrayList<WebResource> resources = new ArrayList<WebResource>();
		resources.add(resource);
		testSQLInspector(resources);
	}
	
	private static void testSQLInspector(List<WebResource> resources) {
		SQLInspector inspector = new SQLInspector();
		for(WebResource resource : resources) {
			Map<String, VulnerabilityAssessment> assessment = inspector.isVulnerable(resource);
			
			if(!assessment.isEmpty()) {
				System.out.println(resource.getUrlPath() + " SQL vulnerable !!!");
			} else {
				System.out.println(resource.getUrlPath() + " not SQL vulnerable");
			}
		}
	}

	private static void testRCEInspector(WebResource resource) {
		ArrayList<WebResource> resources = new ArrayList<WebResource>();
		resources.add(resource);
		testRCEInspector(resources);
	}
	
	private static void testRCEInspector(List<WebResource> resources) {
		RCEInspector inspector = new RCEInspector();
		for(WebResource resource : resources) {
			Map<String, VulnerabilityAssessment> assessment = inspector.isVulnerable(resource);
			
			if(!assessment.isEmpty())
				System.out.println(resource.getUrlPath() + " RCE vulnerable !!!");
			else
				System.out.println(resource.getUrlPath() + " not RCE vulnerable");
		}	
	}
	
	private static void testLFIInspector(WebResource resource) {
		ArrayList<WebResource> resources = new ArrayList<WebResource>();
		resources.add(resource);
		testLFIInspector(resources);
	}
	
	private static void testLFIInspector(List<WebResource> resources) {
		LFIInspector inspector = new LFIInspector();
		for(WebResource resource : resources) {
			Map<String, VulnerabilityAssessment> assessment = inspector.isVulnerable(resource);
			
			if(!assessment.isEmpty())
				System.out.println(resource.getUrlPath() + " LFI vulnerable !!!");
			else
				System.out.println(resource.getUrlPath() + " not LFI vulnerable");
		}	
	}
	
	private static void testXSSInspector(WebResource resource) {
		ArrayList<WebResource> resources = new ArrayList<WebResource>();
		resources.add(resource);
		testXSSInspector(resources);
	}
	
	private static void testXSSInspector(List<WebResource> resources) {
		XSSInspector inspector = new XSSInspector();
		for(WebResource resource : resources) {
			Map<String, VulnerabilityAssessment> assessment = inspector.isVulnerable(resource);
			
			if(!assessment.isEmpty())
				System.out.println(resource.getUrlPath() + " XSS vulnerable !!!");
			else
				System.out.println(resource.getUrlPath() + " not XSS vulnerable");
		}	
	}
	
	private static void testRFIInspector(WebResource resource) {
		ArrayList<WebResource> resources = new ArrayList<WebResource>();
		resources.add(resource);
		testRFIInspector(resources);
	}
	
	private static void testRFIInspector(List<WebResource> resources) {
		RFIInspector inspector = new RFIInspector();
		for(WebResource resource : resources) {
			Map<String, VulnerabilityAssessment> assessment = inspector.isVulnerable(resource);
			
			if(!assessment.isEmpty())
				System.out.println(resource.getUrlPath() + " RFI vulnerable !!!");
			else
				System.out.println(resource.getUrlPath() + " not RFI vulnerable");
		}	
	}
	
	
	
}

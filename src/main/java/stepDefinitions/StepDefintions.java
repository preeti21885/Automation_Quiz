package stepDefinitions;

import java.time.Year;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefintions {

	WebDriver webDriver = null;
	int unfilteredSearchResults = 0;
	int filteredSearchResults = 0;
	public static Scenario scenario;

	
	
	@Before
	public void setup(Scenario myScenario) {
		scenario = myScenario;
	}
	
	// Given I want to open "Chrome"
	@Given("^User Opens Browser \"(.*)\"$")
	public void openBrowser(String browserType) {
		String currentDir = System.getProperty("user.dir");
		System.out.println(currentDir + "/src/main/resources/chromedriver");
		System.setProperty("webdriver.chrome.driver", currentDir + "/src/main/resources/chromedriver");
		webDriver = new ChromeDriver();
	}

	// When User opened "www.bauction.com"
	@When("Navigates to {string}")
	public void navigates_to(String application) {
		System.out.println("In application :: " + application);
		webDriver.get(application);
	}

	@When("Searches for {string}")
	public void searches_for(String searchString) {
		System.out.println("Searched :: " + searchString);
		webDriver.findElement(By.id("simple-keyword-search")).sendKeys(searchString);
		webDriver.findElement(By.id("keyword-submit")).click();

	}

	// Then Capture the number of results returned
	@Then("Capture the number of {string} results returned")
	public void capture_the_number_of_results_returned(String filtered_unfiltered) throws Exception {

		String searchResultsReturnedString = webDriver.findElement(By.id("lblShowResultsFor")).getText();

		String tempString = searchResultsReturnedString.substring("Showing ".length());
		int results = Integer.parseInt(tempString.substring(0, tempString.indexOf(" ")));
		if (filtered_unfiltered.equalsIgnoreCase("unfiltered"))
			unfilteredSearchResults = results;
		else
			filteredSearchResults = results;

		scenario.log("Number of Results returned ::: " + results);
		
		
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("window.scrollBy(0,0)", "");
		Thread.sleep(4000);
		final byte[] screenshot = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES);
		scenario.attach(screenshot, "image/png", "SEARCH RESULTS");
		System.out.println(results);
	}

	// Then Assert that the first result on the page has the word "Ford F-150" in it
	@Then("Assert that the first result on the page has the word \"(.*)\" in it$")
	public void assertNumberOfResults(String stringContains) {
		String firstResultString = webDriver.findElement(By.xpath("//div[@id='searchResultsList']//div/main/h3"))
				.getText();
		System.out.println(firstResultString);
		if (firstResultString.contains(stringContains)) {
		} else {
			Assert.assertTrue("The first result on the page DOESN'T contains : " + firstResultString, false);
		}

	}

	// Given I am on the results page
	/*
	 * @Given("I am on the results page$") public void filterResults() {
	 * 
	 * System.out.println("I am on the results page");
	 * webDriver.findElement(By.xpath(
	 * "//div[@id='manufacturer_year_dt']//div/input[1]")).clear(); }
	 */

	@When("Applies the Year filter to be from “{int}” to current year")
	public void applies_the_year_filter_to_be_from_to_current_year(Integer int1) throws Exception {

		// SET from year
		webDriver.findElement(By.xpath("//div[@id='manufacturer_year_dt']//div/input[1]")).clear();
		webDriver.findElement(By.xpath("//div[@id='manufacturer_year_dt']//div/input[1]")).sendKeys("2010");

		// SET to year as current
		Thread.sleep(5000);
		webDriver.findElement(By.xpath("//div[@id='manufacturer_year_dt']//div/input[2]")).clear();
		webDriver.findElement(By.xpath("//div[@id='manufacturer_year_dt']//div/input[2]"))
				.sendKeys(Year.now().toString());

	}

	@Then("Verifies the number of results returned is different")
	public void verifies_the_number_of_results_returned_is_different() {
		if (unfilteredSearchResults == filteredSearchResults) {
			Assert.assertTrue("The number of results returned are same", false);
		}else {
			scenario.log("Unfiltered results = " + unfilteredSearchResults + " is not equal to filtered result = " + filteredSearchResults);
		}

	}

}

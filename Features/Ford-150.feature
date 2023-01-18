@rbauction
Feature: Search Ford-150
	@search
  Scenario Outline: Automation Quiz
    Given User Opens Browser "Chrome"
    When Navigates to "https://www.rbauction.com/"
    And Searches for "Ford F-150"
    Then Capture the number of "unfiltered" results returned
    Then Assert that the first result on the page has the word "Ford F-150" in it
  	When Applies the Year filter to be from “2010” to current year
    Then Capture the number of "filtered" results returned
    Then Verifies the number of results returned is different
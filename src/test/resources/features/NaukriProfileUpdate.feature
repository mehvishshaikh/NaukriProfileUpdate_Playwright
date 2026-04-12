Feature: Update Naukri Profile Daily

  Background:
    Given I launch the Naukri application
    And I wait for the page to load
    Then I Click on the Login button

  @smoke
  Scenario: User logs into Naukri and updates profile
    When I enter valid credentials
    And I click the login button
    Then I should be redirected to the dashboard
    And I click on the view profile button
    Then I edit the resume headline
    And I save the changes
    Then I should see a confirmation message for the profile update
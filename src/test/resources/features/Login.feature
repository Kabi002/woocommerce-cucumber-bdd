Feature: WooCommerce Login Functionality

  Background:
    Given I navigate to the login page

  Scenario: Successful login with valid credentials
    When I enter username "test_email@12taste.com"
    And I enter password "test_email@12taste.com"
    And I click on the login button
    Then I should be logged in successfully

  Scenario: Login with invalid credentials
    When I enter username "invalid@test.com"
    And I enter password "wrongpassword"
    And I click on the login button
    Then I should see an error message

  Scenario: Login with empty credentials shows HTML5 validation
    When I click on the login button
    Then I should see HTML5 validation message
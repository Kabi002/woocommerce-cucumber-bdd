Feature: WooCommerce User Registration

  Background:
    Given I navigate to the registration page

  Scenario: Successful user registration with valid details
    When I enter first name "Kabilan"
    And I enter last name "Test"
    And I enter email address "testuser12345@gmail.com"
    And I click on the register button
    Then I should be registered successfully
    And I should see the dashboard

  Scenario: Registration with empty first name shows HTML5 validation
    When I enter last name "Test"
    And I enter email address "test@gmail.com"
    And I click on the register button
    Then I should see registration HTML5 validation message

  Scenario: Registration with invalid email format
    When I enter first name "Kabilan"
    And I enter last name "Test"
    And I enter email address "invalidemail"
    And I click on the register button
    Then I should see email format validation message

  Scenario: Registration with duplicate email address
    When I enter first name "Kabilan"
    And I enter last name "Test"
    And I enter email address "test_email@12taste.com"
    And I click on the register button
    Then I should see duplicate email error message
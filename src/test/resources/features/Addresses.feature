@addresses
Feature: WooCommerce Addresses Functionality

  Background:
    Given I navigate to the login page
    When I enter username "test_email@12taste.com"
    And I enter password "test_email@12taste.com"
    And I click on the login button
    Then I should be logged in successfully
    And I navigate to the addresses page


  Scenario: Verify addresses page loads with billing and shipping sections
    Then I should see billing address section
    And I should see shipping address section
    And I should see edit billing address link
    And I should see edit shipping address link

  Scenario: Edit billing address successfully
    When I click on edit billing address link
    Then I should be on billing address page
    When I clear and enter billing first name "Dev"
    And I clear and enter billing last name "Tester"
    And I clear and enter billing company "Test Company"
    And I select billing country "CH"
    And I clear and enter billing street address "123 Test Street"
    And I clear and enter billing city "Test City"
    And I select billing state "AG"
    And I clear and enter billing postcode "1000"
    And I clear and enter billing phone "9876543210"
    And I clear and enter billing email "test_email@12taste.com"
    And I click save address button
    Then I should see address saved success message

  Scenario: Edit billing address with empty first name shows error
    When I click on edit billing address link
    Then I should be on billing address page
    When I clear billing first name
    And I click save address button
    Then I should see billing required field error

  Scenario: Edit shipping address successfully
    When I click on edit shipping address link
    Then I should be on shipping address page
    When I clear and enter shipping first name "Testing"
    And I clear and enter shipping last name "Purpose"
    And I clear and enter shipping company "Test Company"
    And I select shipping country "CH"
    And I clear and enter shipping street address "123 Test Street"
    And I clear and enter shipping city "Test City"
    And I select shipping state "AG"
    And I clear and enter shipping postcode "1000"
    And I click save address button
    Then I should see address saved success message

  Scenario: Edit shipping address with empty first name shows error
    When I click on edit shipping address link
    Then I should be on shipping address page
    When I clear shipping first name
    And I click save address button
    Then I should see shipping required field error
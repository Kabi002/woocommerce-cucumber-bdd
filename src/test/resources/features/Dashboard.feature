@dashboard
Feature: WooCommerce Dashboard Functionality

  Background:
    Given I navigate to the login page
    When I enter username "test_email@12taste.com"
    And I enter password "test_email@12taste.com"
    And I click on the login button
    Then I should be logged in successfully

  Scenario: Verify all sidebar links are visible on dashboard
    Then I should see all sidebar links on the dashboard

  Scenario: Verify dashboard order counts are displayed
    Then I should see "Orders and quotes" count on the dashboard
    And I should see "Processing orders" count on the dashboard
    And I should see "Awaiting payments" count on the dashboard

  Scenario: Navigate and verify all sidebar links one by one
    When I click and verify all sidebar links
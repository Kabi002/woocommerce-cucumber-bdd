@accountdetails
Feature: WooCommerce Account Details Functionality

  Background:
    Given I navigate to the login page
    When I enter username "test_email@12taste.com"
    And I enter password "test_email@12taste.com"
    And I click on the login button
    Then I should be logged in successfully
    And I navigate to the account details page

  Scenario: Verify account details page loads with all fields
    Then I should see all account detail fields

  Scenario: Update first name and last name successfully
    When I clear and enter first name "Dev"
    And I clear and enter last name "Tester"
    And I enter current password "test_email@12taste.com"
    And I enter new password "test_email@12taste.com"
    And I enter confirm new password "test_email@12taste.com"
    And I click save changes button
    Then I should see account details saved success message

 Scenario: Update display name successfully
    When I clear and enter display name "Dev Tester"
    And I enter current password "test_email@12taste.com"
    And I enter new password "test_email@12taste.com"
    And I enter confirm new password "test_email@12taste.com"
    And I click save changes button
    Then I should see account details saved success message

  Scenario: Update email with invalid format shows validation error
    When I clear and enter account email "invalidemail"
    And I click save changes button
    Then I should see email format validation error

Scenario: Save changes without password fields should succeed
  When I clear password fields
  And I clear and enter first name "Dev"
  And I click save changes button
  Then I should see account details saved success message
  #Scenario: Save changes without password fields shows error
    #When I clear and enter first name "Dev"
    #And I click save changes button
    #Then I should see password required error message

  Scenario: Save changes with wrong current password shows error
    When I enter current password "wrongpassword123"
    And I enter new password "Newpass@123"
    And I enter confirm new password "Newpass@123"
    And I click save changes button
    Then I should see wrong password error message

  Scenario: Leave first name empty shows validation error
    When I clear and enter first name ""
    And I click save changes button
    Then I should see required field validation error
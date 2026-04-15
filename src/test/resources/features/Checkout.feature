@checkout
Feature: WooCommerce Checkout Functionality

  Background:
    Given I navigate to the login page
    When I enter username "test_email@12taste.com"
    And I enter password "test_email@12taste.com"
    And I click on the login button
    Then I should be logged in successfully
    And I navigate directly to product and add to cart
    And I navigate to the cart page
    And I click complete your order button

  Scenario: Verify billing details page loads with pre-filled details
    Then I should be on checkout page
    And I should see billing details pre-filled

  Scenario: Verify order summary on billing page
    Then I should see order summary with product "Test Product 1"
    And I should see order total displayed

  Scenario: Complete full checkout process and place order
    When I click to shipping button
    Then I should see shipping details page
    When I click to comments button
    Then I should see order comments page
    When I enter purchase order number "PO-TEST-001"
    And I enter order notes "This is a test order"
    And I click to payment button
    Then I should see finalize order page
    When I select payment method "Pay by invoice"
    And I accept terms and conditions
    And I click place order button
    Then I should see order confirmation message
    And I should see order number displayed

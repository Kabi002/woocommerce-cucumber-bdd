@cart
Feature: WooCommerce Cart Functionality

  Background:
    Given I navigate to the login page
    When I enter username "mskabilan4@gmail.com"
    And I enter password "mskabilan4@gmail.com"
    And I click on the login button
    Then I should be logged in successfully
    And I navigate directly to product and add to cart
    And I navigate to the cart page

  Scenario: Verify product is in cart
    Then I should see product in cart "Test Product 1 - 1 LITRE CAN"

  Scenario: Verify cart total is displayed
    Then I should see cart total displayed

  Scenario: Increase product quantity in cart
    When I increase cart quantity
    Then I should see updated subtotal

  Scenario: Decrease product quantity in cart
    When I decrease cart quantity
    Then I should see updated subtotal

  Scenario: Verify checkout button is visible
    Then I should see complete your order button

  Scenario: Remove product from cart
    When I click remove product from cart
    Then I should see empty cart message

  Scenario: Clear purchase list
    When I click clear purchase list button
    Then I should see empty cart message
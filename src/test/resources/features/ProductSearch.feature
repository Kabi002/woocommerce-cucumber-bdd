@productsearch
Feature: WooCommerce Product Search and Add to Cart

  Background:
    Given I navigate to the login page
    When I enter username "test_email@12taste.com"
    And I enter password "test_email@12taste.com"
    And I click on the login button
    Then I should be logged in successfully

  Scenario: Search for a product and verify results appear
    When I search for product "Test"
    Then I should see search results for "Test"
    And I should see result count message

  Scenario: Search with invalid keyword shows no results
    When I search for product "xyzinvalidproduct123"
    Then I should see no results message

  Scenario: Click on a product and verify product page loads
    When I search for product "Test"
    Then I should see search results for "Test"
    When I click on product "Test Product 1"
    Then I should be on product page "test-product-1"
    And I should see product title "Test Product 1"

  Scenario: Add product to cart successfully
    When I navigate directly to product page
    And I select packaging option "1 LITRE CAN"
    And I increase quantity by 1
    And I click add to cart button
    Then I should see add to cart success message
    And I should see view cart button
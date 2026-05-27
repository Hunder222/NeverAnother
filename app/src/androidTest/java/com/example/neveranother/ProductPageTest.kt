package com.example.neveranother

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.neveranother.productpage.Dropdown
import com.example.neveranother.productpage.Product
import com.example.neveranother.productpage.Readmore
import com.example.neveranother.viewmodels.NAViewModel
import org.junit.Rule
import org.junit.Test

class ProductPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dropdown_expands_and_collapses_on_click() {
        val testTitle = "Materiale komposition"
        val testContent = "41% Filea cupro"

        composeTestRule.setContent {
            Dropdown(title = testTitle) {
                Text(text = testContent)
            }
        }

        // Initially content should not be displayed
        composeTestRule.onNodeWithText(testContent).assertDoesNotExist()

        // Act: Click on the title text to expand (the entire Row is clickable)
        composeTestRule.onNodeWithText(testTitle).performClick()

        // Assert: Content should now be displayed
        composeTestRule.onNodeWithText(testContent).assertIsDisplayed()

        // Act: Click the arrow to collapse
        composeTestRule.onNodeWithContentDescription("Arrow that collapses dropdown").performClick()

        // Assert: Content should be gone again
        composeTestRule.onNodeWithText(testContent).assertDoesNotExist()
    }

    @Test
    fun readmore_expands_and_collapses_on_click() {
        val longText = "Dette er en lang tekst, der skal kunne foldes ud."

        composeTestRule.setContent {
            Readmore(text = longText)
        }

        // Initially shows "Læs mere"
        composeTestRule.onNodeWithText("Læs mere").assertIsDisplayed()

        // Click to expand
        composeTestRule.onNodeWithText("Læs mere").performClick()

        // Should now show "Læs mindre"
        composeTestRule.onNodeWithText("Læs mindre").assertIsDisplayed()

        // Click to collapse
        composeTestRule.onNodeWithText("Læs mindre").performClick()
        composeTestRule.onNodeWithText("Læs mere").assertIsDisplayed()
    }

    @Test
    fun product_quantity_logic_works() {
        val viewModel = NAViewModel()

        composeTestRule.setContent {
            Product(naViewModel = viewModel, _onCartClick = {})
        }

        // Check initial quantity is 1
        composeTestRule.onNodeWithText("1").assertIsDisplayed()

        // Click plus icon
        composeTestRule.onNodeWithContentDescription("plus icon").performClick()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()

        // Click minus icon
        composeTestRule.onNodeWithContentDescription("minus icon").performClick()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()

        // Ensure it doesn't go below 1
        composeTestRule.onNodeWithContentDescription("minus icon").performClick()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun product_color_selection_updates_label() {
        val viewModel = NAViewModel()

        composeTestRule.setContent {
            Product(naViewModel = viewModel, _onCartClick = {})
        }

        // Initial color is white
        composeTestRule.onNodeWithText("Farve: white").assertIsDisplayed()

        // Click black circle
        composeTestRule.onNodeWithContentDescription("Black color circle").performClick()
        composeTestRule.onNodeWithText("Farve: black").assertIsDisplayed()

        // Click white circle
        composeTestRule.onNodeWithContentDescription("White color circle").performClick()
        composeTestRule.onNodeWithText("Farve: white").assertIsDisplayed()
    }

}

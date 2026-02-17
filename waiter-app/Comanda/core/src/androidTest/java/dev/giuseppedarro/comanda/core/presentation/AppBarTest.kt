package dev.giuseppedarro.comanda.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicBoolean


class AppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun comandaTopAppBar_displaysCorrectTitle() {
        // Arrange
        val testTitle = "My Test Title"

        // Act
        composeTestRule.setContent {
            ComandaTheme {
                ComandaTopAppBar(title = testTitle)
            }
        }

        // Assert
        composeTestRule.onNodeWithText(testTitle).assertIsDisplayed()
    }

    @Test
    fun comandaTopAppBar_navigationIconClick_invokesCallback() {
        // Arrange
        val wasClicked = AtomicBoolean(false)
        val navigationIconContentDescription = "Navigate Back"

        // Act
        composeTestRule.setContent {
            ComandaTheme {
                ComandaTopAppBar(
                    title = "Test",
                    navigationIcon = {
                        IconButton(onClick = { wasClicked.set(true) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = navigationIconContentDescription
                            )
                        }
                    }
                )
            }
        }

        // Find the navigation icon and click it
        composeTestRule.onNodeWithContentDescription(navigationIconContentDescription).performClick()

        // Assert
        assertThat(wasClicked.get()).isTrue()
    }

    @Test
    fun comandaTopAppBar_displaysActionIcons() {
        // Arrange
        val actionIconContentDescription = "Settings Action"

        // Act
        composeTestRule.setContent {
            ComandaTheme {
                ComandaTopAppBar(
                    title = "Test",
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = actionIconContentDescription
                            )
                        }
                    }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription(actionIconContentDescription).assertIsDisplayed()
    }
}
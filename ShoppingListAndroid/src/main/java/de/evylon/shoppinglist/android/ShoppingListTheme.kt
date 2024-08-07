package de.evylon.shoppinglist.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Suppress("MagicNumber")
@Composable
fun ShoppingListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) shoppingListDarkColors else shoppingListLightColors
    val materialColors = if (darkTheme) {
        darkColors(
            primary = Color(0xFFBB86FC),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5),
            background = Color.Black
        )
    } else {
        lightColors(
            primary = Color(0xFF6200EE),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5),
            background = Color.LightGray
        )
    }
    val typography = Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    CompositionLocalProvider(LocalShoppingListColors provides colors) {
        MaterialTheme(
            colors = materialColors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

data class ShoppingListColors(
    val primaryTextColor: Color = Color.Unspecified
)

val shoppingListLightColors = ShoppingListColors(
    primaryTextColor = Color.Black
)

val shoppingListDarkColors = ShoppingListColors(
    primaryTextColor = Color.White
)

val LocalShoppingListColors = staticCompositionLocalOf { ShoppingListColors() }

object ShoppingListTheme {
    val colors: ShoppingListColors
        @Composable
        get() = LocalShoppingListColors.current
}

package com.example.personalfinancecompanionmobileapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalfinancecompanionmobileapp.data.model.TransactionCategory
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

fun getCategoryIcon(category: TransactionCategory) = when (category) {
    TransactionCategory.DINING -> Icons.Default.Fastfood
    TransactionCategory.GROCERIES -> Icons.Default.Fastfood
    TransactionCategory.TRANSPORT -> Icons.Default.Train
    TransactionCategory.ENTERTAINMENT -> Icons.Default.Movie
    TransactionCategory.SHOPPING -> Icons.Default.ShoppingCart
    TransactionCategory.HEALTH -> Icons.Default.HealthAndSafety
    TransactionCategory.SALARY, TransactionCategory.FREELANCE, TransactionCategory.INVESTMENT -> Icons.Default.Money
    else -> Icons.Default.Category
}

fun getCategoryColor(category: TransactionCategory): Color = when (category) {
    TransactionCategory.DINING -> Color(0xFFBC6C25) // EarthTerra
    TransactionCategory.GROCERIES -> Color(0xFFDDA15E) // EarthGold
    TransactionCategory.TRANSPORT -> Color(0xFF606C38) // EarthOlive
    TransactionCategory.ENTERTAINMENT -> Color(0xFF506841) // Darker Olive
    TransactionCategory.SHOPPING -> Color(0xFF869255) // Lighter Olive
    TransactionCategory.HEALTH -> Color(0xFFA57C53) // Muted Terra
    else -> Color(0xFF3B4828) // Muted Forest
}

@Composable
fun CategoryDonutChart(
    expensesByCategory: Map<TransactionCategory, Double>,
    currency: String = "₹",
    modifier: Modifier = Modifier
) {
    val total = expensesByCategory.values.sum()
    if (total <= 0.0) return // Don't draw if 0
    
    val chartSize = 250.dp
    val strokeWidth = 50.dp
    val density = LocalDensity.current
    
    // Evaluate target sweeps and bind them to interactive animation states
    val animatedSweeps = TransactionCategory.values().associateWith { cat ->
        val targetSweep = if (expensesByCategory.containsKey(cat)) ((expensesByCategory[cat]!! / total) * 360f).toFloat() else 0f
        val sweepAngle by animateFloatAsState(
            targetValue = targetSweep,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            label = "sweep_${cat.name}"
        )
        sweepAngle
    }
    
    Box(
        modifier = modifier
            .size(chartSize)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Draw the colored arcs
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f
            val canvasSize = size.minDimension
            val radius = canvasSize / 2
            val drawRadius = radius - (strokeWidth.toPx() / 2f)
            
            val arcSize = Size(drawRadius * 2, drawRadius * 2)
            val topLeft = Offset(
                (size.width - arcSize.width) / 2f,
                (size.height - arcSize.height) / 2f
            )

            val activeCategories = TransactionCategory.values().filter { animatedSweeps[it] ?: 0f > 0.1f }

            activeCategories.forEach { cat ->
                val sweepAngle = animatedSweeps[cat] ?: 0f
                val color = getCategoryColor(cat)
                
                // Small gap between lines looks premium, so we sweep slightly less
                val gap = if (activeCategories.size > 1) 2f else 0f
                val drawSweep = (sweepAngle - gap).coerceAtLeast(1f)
                
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = drawSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
                )
                
                startAngle += sweepAngle
            }
        }
        
        // Draw total amount in the center
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Total", 
                style = MaterialTheme.typography.titleMedium, 
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = "$currency${String.format(Locale.getDefault(), "%.0f", total)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Overlay the icons
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val centerDP = maxWidth / 2f
            // Radius of stroke midline
            val radiusDP = centerDP - (strokeWidth / 2f)
            
            var startAngle = -90f
            
            val activeCategories = TransactionCategory.values().filter { animatedSweeps[it] ?: 0f > 0.1f }
            
            activeCategories.forEach { cat ->
                val sweepAngle = animatedSweeps[cat] ?: 0f
                if (sweepAngle > 15f) { // Only show icon if there's enough space
                    val midAngle = startAngle + (sweepAngle / 2f)
                    val midAngleRad = Math.toRadians(midAngle.toDouble())
                    
                    val iconXOffset = (radiusDP.value * cos(midAngleRad)).dp
                    val iconYOffset = (radiusDP.value * sin(midAngleRad)).dp
                    
                    val iconSize = 28.dp
                    // Offset from top-left (center + trigonometric offset - half icon size)
                    val offsetX = centerDP + iconXOffset - (iconSize / 2f)
                    val offsetY = centerDP + iconYOffset - (iconSize / 2f)
                    
                    Box(
                        modifier = Modifier
                            .offset(x = offsetX, y = offsetY)
                            .size(iconSize)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(cat),
                            contentDescription = cat.displayName,
                            tint = Color.White,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                
                startAngle += sweepAngle
            }
        }
    }
}

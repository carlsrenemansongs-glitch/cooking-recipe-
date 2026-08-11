package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.OnEmeraldContainer
import java.util.Locale

@Composable
fun CostBadge(
    costPerServing: Double,
    modifier: Modifier = Modifier,
    backgroundColor: Color = EmeraldContainer,
    contentColor: Color = OnEmeraldContainer
) {
    val formattedCost = String.format(Locale.US, "$%.2f", costPerServing)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AttachMoney,
            contentDescription = "Meal Cost",
            tint = contentColor,
            modifier = Modifier.padding(end = 2.dp)
        )
        Text(
            text = formattedCost,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            fontSize = 12.sp
        )
    }
}

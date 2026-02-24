package com.picapica.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.picapica.app.ui.theme.DarkBlue
import com.picapica.app.ui.theme.PicaOrange

@Composable
fun HistoryView(onBack: () -> Unit, history: Map<String, String>) {
    val workingDays = listOf(
        "MONDAY" to "Lun",
        "TUESDAY" to "Mar",
        "WEDNESDAY" to "Mie",
        "THURSDAY" to "Jue",
        "FRIDAY" to "Vie"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // Dim background
            .padding(horizontal = 0.dp, vertical = 20.dp), // More height for the card
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo/Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = DarkBlue)) {
                                append("Pica")
                            }
                            withStyle(style = SpanStyle(color = PicaOrange)) {
                                append("Pica")
                            }
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-1.5).sp,
                        modifier = Modifier.clickable { onBack() }
                    )
                    val closeInteractionSource = remember { MutableInteractionSource() }
                    val isClosePressed by closeInteractionSource.collectIsPressedAsState()
                    val closeScale by androidx.compose.animation.core.animateFloatAsState(if (isClosePressed) 1.1f else 1.0f)

                    IconButton(
                        onClick = onBack,
                        interactionSource = closeInteractionSource
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = if (isClosePressed) PicaOrange else DarkBlue,
                            modifier = Modifier.size(32.dp).graphicsLayer(scaleX = closeScale, scaleY = closeScale)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(workingDays) { (fullDayName, shortName) ->
                        val timeStr = history[fullDayName] ?: "00:00 - 00:00"
                        Text(
                            text = "$shortName: $timeStr",
                            fontSize = 18.sp,
                            color = DarkBlue.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Volver Button
                val volverInteractionSource = remember { MutableInteractionSource() }
                val isVolverPressed by volverInteractionSource.collectIsPressedAsState()

                OutlinedButton(
                    onClick = onBack,
                    interactionSource = volverInteractionSource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    border = if (isVolverPressed) null else BorderStroke(2.dp, DarkBlue),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isVolverPressed) DarkBlue else Color.Transparent,
                        contentColor = if (isVolverPressed) Color.White else DarkBlue
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(text = "Volver", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


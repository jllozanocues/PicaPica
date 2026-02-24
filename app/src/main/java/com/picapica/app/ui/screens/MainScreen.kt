package com.picapica.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import com.picapica.app.viewmodel.PicaPicaViewModel

@Composable
fun MainScreen(viewModel: PicaPicaViewModel, onOpenHistory: () -> Unit) {
    val timerText by viewModel.timerText.collectAsState()
    val intervalText by viewModel.intervalText.collectAsState()
    val isPiqueActive by viewModel.isPiqueActive.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // Simulated background
            .padding(horizontal = 0.dp, vertical = 16.dp), // 100% width (0 horizontal padding)
        contentAlignment = Alignment.Center
    ) {
        // Glass Card
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
                        letterSpacing = (-1.5).sp
                    )
                    Row {
                        val historyInteractionSource = remember { MutableInteractionSource() }
                        val isHistoryPressed by historyInteractionSource.collectIsPressedAsState()
                        val historyScale by androidx.compose.animation.core.animateFloatAsState(if (isHistoryPressed) 1.1f else 1.0f)
                        
                        IconButton(
                            onClick = onOpenHistory,
                            interactionSource = historyInteractionSource
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = "History",
                                tint = if (isHistoryPressed) PicaOrange else DarkBlue,
                                modifier = Modifier.size(32.dp).graphicsLayer(scaleX = historyScale, scaleY = historyScale)
                            )
                        }

                        val exitInteractionSource = remember { MutableInteractionSource() }
                        val isExitPressed by exitInteractionSource.collectIsPressedAsState()
                        val exitScale by androidx.compose.animation.core.animateFloatAsState(if (isExitPressed) 1.1f else 1.0f)

                        IconButton(
                            onClick = { 
                                viewModel.stopIfActive()
                                (context as? android.app.Activity)?.finish() 
                            },
                            interactionSource = exitInteractionSource
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Exit",
                                tint = if (isExitPressed) PicaOrange else DarkBlue,
                                modifier = Modifier.size(32.dp).graphicsLayer(scaleX = exitScale, scaleY = exitScale)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Timer
                Text(
                    text = timerText,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isPiqueActive) PicaOrange else DarkBlue
                )
                Text(
                    text = intervalText,
                    fontSize = 18.sp,
                    color = DarkBlue.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Pica Button
                val picaInteractionSource = remember { MutableInteractionSource() }
                val isPicaPressed by picaInteractionSource.collectIsPressedAsState()
                
                Button(
                    onClick = { viewModel.togglePique() },
                    interactionSource = picaInteractionSource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPicaPressed) DarkBlue else PicaOrange
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(text = "Pica", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Reset Button
                val resetInteractionSource = remember { MutableInteractionSource() }
                val isResetPressed by resetInteractionSource.collectIsPressedAsState()

                OutlinedButton(
                    onClick = { viewModel.reset() },
                    interactionSource = resetInteractionSource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    border = if (isResetPressed) null else BorderStroke(2.dp, DarkBlue),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isResetPressed) DarkBlue else Color.Transparent,
                        contentColor = if (isResetPressed) Color.White else DarkBlue
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(text = "Reset", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Credits
                Text(
                    text = "Desarrollado por JLozano para MAD",
                    fontSize = 12.sp,
                    color = DarkBlue.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Normal,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Default
                )
            }
        }
    }
}


package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.AppTheme

@Composable
fun LanguageSelectorDialog(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onDismissRequest: () -> Unit
) {
    val tokens = AppTheme.tokens

    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(16.dp),
        containerColor = tokens.surface,
        modifier = Modifier.border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
        title = {
            Text(
                text = "Select Language",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = tokens.textPrimary
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppLanguage.entries.forEach { lang ->
                    val isSelected = lang == currentLanguage
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageSelected(lang)
                                onDismissRequest()
                            }
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = lang.flag, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = lang.displayName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) tokens.primary else tokens.textPrimary
                            )
                        }
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                onLanguageSelected(lang)
                                onDismissRequest()
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = tokens.primary)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Close", color = tokens.primary)
            }
        }
    )
}

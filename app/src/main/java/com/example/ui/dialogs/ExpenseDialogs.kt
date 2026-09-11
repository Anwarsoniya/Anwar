package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ExpenseEntity
import com.example.ui.theme.BorderRose
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.util.BanglaUtils

@Composable
fun ExpenseDialog(
    expenses: List<ExpenseEntity>,
    onDismiss: () -> Unit,
    onAddExpense: (amount: Double, reason: String) -> Unit,
    onOpenHistory: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var reasonText by remember { mutableStateOf("") }

    val todayStr = remember { BanglaUtils.getCurrentDateStr() }
    val todayExpenses by remember(expenses, todayStr) {
        derivedStateOf { expenses.filter { it.dateStr == todayStr } }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = DangerRed)
                    Text("দোকান খরচ লিপিবদ্ধ করুন", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("খরচের টাকার পরিমাণ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_expense_amount")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = reasonText,
                    onValueChange = { reasonText = it },
                    label = { Text("খরচের কারণ") },
                    placeholder = { Text("যেমন: দোকান ভাড়া, নাস্তা, কারিগর বিল") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_expense_reason")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val amt = amountText.toDoubleOrNull() ?: 0.0
                        if (amt > 0 && reasonText.isNotBlank()) {
                            onAddExpense(amt, reasonText)
                            amountText = ""
                            reasonText = ""
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_save_expense")
                ) {
                    Text("খরচ সেভ করুন", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Today's expenses listing
                Text("আজকের খরচসমূহ:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SlateDark)
                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFF9FA),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderRose),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        if (todayExpenses.isEmpty()) {
                            Text("আজকের কোনো খরচ নেই", fontSize = 12.sp, color = SlateLight, fontWeight = FontWeight.SemiBold)
                        } else {
                            todayExpenses.forEach { exp ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(exp.reason, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                                    Text(BanglaUtils.formatTaka(exp.amount), fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DangerRed)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = onOpenHistory,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("সকল খরচের খাতা দেখুন", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("বন্ধ করুন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ExpenseHistoryDialog(
    expenses: List<ExpenseEntity>,
    isAdmin: Boolean,
    onDismiss: () -> Unit,
    onDeleteExpense: (ExpenseEntity) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = DangerRed)
                    Text("সকল খরচের ইতিহাস", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (expenses.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("কোনো খরচের তথ্য পাওয়া যায়নি", color = SlateLight, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(expenses, key = { it.id }) { expense ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderRose),
                                color = Color(0xFFFFF9FA),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(expense.reason, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = SlateDark)
                                        Text(expense.dateStr, fontSize = 11.sp, color = SlateLight, fontWeight = FontWeight.SemiBold)
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = BanglaUtils.formatTaka(expense.amount),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            color = DangerRed
                                        )

                                        if (isAdmin) {
                                            IconButton(
                                                onClick = { onDeleteExpense(expense) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("বন্ধ করুন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

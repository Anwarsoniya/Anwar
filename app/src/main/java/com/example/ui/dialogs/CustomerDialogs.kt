package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CustomerEntity
import com.example.data.model.SaleEntity
import com.example.data.model.TransactionEntity
import com.example.ui.theme.BorderRose
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.util.BanglaUtils

@Composable
fun AddCustomerDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, tint = PrimaryCrimson)
                    Text("নতুন কাস্টমার খাতা খুলুন", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("কাস্টমারের নাম") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_cust_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("মোবাইল নাম্বার") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_cust_phone")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("বাতিল", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onSave(name, phone) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("btn_save_new_customer")
                    ) {
                        Text("খাতা সেভ করুন", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerDetailsDialog(
    customer: CustomerEntity,
    sales: List<SaleEntity>,
    transactions: List<TransactionEntity>,
    onDismiss: () -> Unit,
    onTakePayment: (Double) -> Unit,
    onViewSaleInvoice: (SaleEntity) -> Unit
) {
    var showPaymentInput by remember { mutableStateOf(false) }
    var paymentAmountText by remember { mutableStateOf("") }

    val customerDueSales by remember(sales, customer) {
        derivedStateOf { sales.filter { it.customerId == customer.id && it.saleType == "due" } }
    }

    val customerPayments by remember(transactions, customer) {
        derivedStateOf { transactions.filter { it.customerId == customer.id && it.type == "DUE_PAYMENT" } }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = customer.name,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            color = SlateDark
                        )
                        Text(
                            text = if (customer.phone.isNotBlank()) customer.phone else "ফোন নম্বর নেই",
                            fontSize = 12.sp,
                            color = SlateLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SlateLight)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Due balance card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryCrimsonLight),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderRose),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text("মোট বকেয়া পাওনা", color = DangerRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = BanglaUtils.formatTaka(customer.due),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DangerRed
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { showPaymentInput = true },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                            modifier = Modifier.testTag("btn_take_due_payment")
                        ) {
                            Icon(Icons.Default.Paid, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("টাকা জমা নিন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                // Payment input expansion
                if (showPaymentInput) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("জমা নেওয়ার পরিমাণ (টাকা):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SlateDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = paymentAmountText,
                                    onValueChange = { paymentAmountText = it },
                                    placeholder = { Text("0") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("input_due_pay_amount"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )

                                Button(
                                    onClick = {
                                        val amt = paymentAmountText.toDoubleOrNull() ?: 0.0
                                        if (amt > 0) {
                                            onTakePayment(amt)
                                            showPaymentInput = false
                                            paymentAmountText = ""
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Text("জমা", fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { showPaymentInput = false },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Text("বাতিল")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "পূর্বের লেনদেন ও মেমো:",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = SlateDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Ledger entries
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (customerDueSales.isEmpty() && customerPayments.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("কোনো লেনদেনের তথ্য নেই", color = SlateLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    // Due sales entries
                    items(customerDueSales) { sale ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEE2E2)),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onViewSaleInvoice(sale) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 3.dp,
                                        color = DangerRed,
                                        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                                    )
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = SlateDark
                                        ) {
                                            Text(
                                                text = "মেমো #${sale.id.toString().takeLast(4)}",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text("বাকি বিক্রি", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SlateDark)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(sale.dateStr, fontSize = 10.sp, color = SlateLight, fontWeight = FontWeight.SemiBold)
                                }

                                Text(
                                    text = "-${BanglaUtils.formatTaka(sale.finalTotal)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DangerRed
                                )
                            }
                        }
                    }

                    // Payments entries
                    items(customerPayments) { payment ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1FAE5)),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 3.dp,
                                        color = SuccessGreen,
                                        shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                                    )
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                        Text("বকেয়া পরিশোধ (নগদ জমা)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SuccessGreen)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(payment.dateStr, fontSize = 10.sp, color = SlateLight, fontWeight = FontWeight.SemiBold)
                                }

                                Text(
                                    text = "+${BanglaUtils.formatTaka(payment.amount)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

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

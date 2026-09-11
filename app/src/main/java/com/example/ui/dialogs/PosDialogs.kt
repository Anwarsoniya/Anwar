package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ProductEntity
import com.example.data.model.SaleEntity
import com.example.ui.theme.BorderRose
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonDark
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateMedium
import com.example.ui.theme.SuccessGreen
import com.example.ui.util.BanglaUtils

@Composable
fun QuantityPickerDialog(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var quantityText by remember { mutableStateOf("1") }
    val currentQty = quantityText.toDoubleOrNull() ?: 1.0

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Product preview icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(PrimaryCrimsonLight, RoundedCornerShape(16.dp))
                        .border(1.5.dp, BorderRose, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = PrimaryCrimson,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = product.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = SlateDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "${BanglaUtils.formatTaka(product.sellPrice)} / ${product.unit} (মজুদ: ${BanglaUtils.formatNumber(product.stock)} ${product.unit})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCrimson
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "পরিমাণ (Quantity) নির্বাচন করুন",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quantity Stepper with text field
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val newQty = (currentQty - 1.0).coerceAtLeast(0.5)
                            quantityText = if (newQty % 1.0 == 0.0) newQty.toInt().toString() else newQty.toString()
                        },
                        shape = CircleShape,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = SlateDark)
                    }

                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryCrimson,
                            textAlign = TextAlign.Center
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryCrimson,
                            unfocusedBorderColor = PrimaryCrimson
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .height(52.dp)
                            .testTag("input_qty_dialog")
                    )

                    OutlinedButton(
                        onClick = {
                            val newQty = currentQty + 1.0
                            quantityText = if (newQty % 1.0 == 0.0) newQty.toInt().toString() else newQty.toString()
                        },
                        shape = CircleShape,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = SlateDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Presets (1, 2, 5, 10)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(1, 2, 5, 10).forEach { preset ->
                        OutlinedButton(
                            onClick = { quantityText = preset.toString() },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${BanglaUtils.toBanglaDigits(preset)} ${product.unit}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SlateDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
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
                        onClick = {
                            val finalQty = quantityText.toDoubleOrNull() ?: 1.0
                            if (finalQty > 0) {
                                onConfirm(finalQty)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("btn_confirm_add_cart")
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("কার্টে যোগ করুন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun InvoiceDialog(
    sale: SaleEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val items = remember(sale) { sale.parseItems() }

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
                // Cash Memo Content
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderRose),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = null,
                            tint = PrimaryCrimson,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "আরবি চুড়ি ঘর",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = SlateDark
                        )
                        Text(
                            text = "কাচের, মেটাল ও বাহারি চুড়ির বিশ্বস্ত প্রতিষ্ঠান",
                            fontSize = 11.sp,
                            color = SlateLight,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Metadata Box
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFF9FA),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "মেমো নং: #${sale.id.toString().takeLast(4)}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = PrimaryCrimson
                                )
                                Text(
                                    text = "তারিখ: ${sale.dateStr} | সময়: ${sale.timeStr}",
                                    fontSize = 12.sp,
                                    color = SlateMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (sale.customerName.isNotBlank() && sale.customerName != "সাধারণ কাস্টমার") {
                                    Text(
                                        text = "কাস্টমার: ${sale.customerName} ${if (sale.customerPhone.isNotBlank()) "(${sale.customerPhone})" else ""}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SlateDark
                                    )
                                }
                                Text(
                                    text = "বিক্রেতা: ${sale.soldBy} | বিক্রয় ধরন: ${if (sale.saleType == "cash") "নগদ" else "বাকি"}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SlateMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = PrimaryCrimson,
                                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                )
                                .background(PrimaryCrimsonLight)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("চুড়ির বিবরণ", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = PrimaryCrimson)
                            Text("টাকা", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = PrimaryCrimson)
                        }

                        // Items list
                        items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(width = 0.5.dp, color = Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = SlateDark
                                    )
                                    Text(
                                        text = "(${BanglaUtils.formatNumber(item.quantity)} ${item.unit} × ${BanglaUtils.formatTaka(item.unitPrice)})",
                                        fontSize = 10.sp,
                                        color = SlateLight,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = BanglaUtils.formatTaka(item.lineTotal),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = SlateDark
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = PrimaryCrimson)

                        // Grand Total
                        if (sale.discount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("উপ-মোট:", fontSize = 12.sp, color = SlateLight, fontWeight = FontWeight.Bold)
                                Text(BanglaUtils.formatTaka(sale.subtotal), fontSize = 12.sp, color = SlateLight, fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("ছাড় / ডিসকাউন্ট:", fontSize = 12.sp, color = DangerRed, fontWeight = FontWeight.Bold)
                                Text("-${BanglaUtils.formatTaka(sale.discount)}", fontSize = 12.sp, color = DangerRed, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "সর্বমোট বিল:",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = SlateDark
                            )
                            Text(
                                text = BanglaUtils.formatTaka(sale.finalTotal),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = PrimaryCrimson
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "ধন্যবাদ! আবার আসবেন।",
                            color = PrimaryCrimsonDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions: Share & Close
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { shareInvoiceText(context, sale, items) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("মেমো শেয়ার", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("বন্ধ করুন", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private fun shareInvoiceText(context: Context, sale: SaleEntity, items: List<com.example.data.model.SoldItem>) {
    val sb = StringBuilder()
    sb.append("✨ আরবি চুড়ি ঘর ✨\n")
    sb.append("মেমো নং: #${sale.id.toString().takeLast(4)}\n")
    sb.append("তারিখ: ${sale.dateStr} | সময়: ${sale.timeStr}\n")
    if (sale.customerName.isNotBlank() && sale.customerName != "সাধারণ কাস্টমার") {
        sb.append("কাস্টমার: ${sale.customerName}\n")
    }
    sb.append("বিক্রেতা: ${sale.soldBy}\n")
    sb.append("-----------------------------\n")
    items.forEach { item ->
        sb.append("• ${item.name} (${BanglaUtils.formatNumber(item.quantity)} ${item.unit}) = ${BanglaUtils.formatTaka(item.lineTotal)}\n")
    }
    sb.append("-----------------------------\n")
    if (sale.discount > 0) {
        sb.append("উপ-মোট: ${BanglaUtils.formatTaka(sale.subtotal)}\n")
        sb.append("ছাড়: -${BanglaUtils.formatTaka(sale.discount)}\n")
    }
    sb.append("সর্বমোট বিল: ${BanglaUtils.formatTaka(sale.finalTotal)} (${if (sale.saleType == "cash") "নগদ" else "বাকি"})\n\n")
    sb.append("ধন্যবাদ! আবার আসবেন।")

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "আরবি চুড়ি ঘর - ক্যাশ মেমো #${sale.id.toString().takeLast(4)}")
        putExtra(Intent.EXTRA_TEXT, sb.toString())
    }
    context.startActivity(Intent.createChooser(intent, "ক্যাশ মেমো শেয়ার করুন"))
}

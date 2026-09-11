package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ProductEntity
import com.example.ui.theme.BorderRose
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.util.BanglaUtils

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, unit: String, buyPrice: Double, sellPrice: Double, stock: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("ডজন") }
    var buyPriceText by remember { mutableStateOf("") }
    var sellPriceText by remember { mutableStateOf("") }
    var stockText by remember { mutableStateOf("") }

    val units = listOf("ডজন", "জোড়া", "সেট", "পিস", "বক্স", "মুঠা")
    var unitDropdownExpanded by remember { mutableStateOf(false) }

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
                    Icon(Icons.Default.Diamond, contentDescription = null, tint = PrimaryCrimson)
                    Text("নতুন চুড়ি যুক্ত করুন", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("চুড়ির নাম ও সাইজ") },
                    placeholder = { Text("যেমন: জয়পুরী কাচের চুড়ি - 2.6") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_prod_name")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Unit selection dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("একক / Unit") },
                        trailingIcon = {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { unitDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = unitDropdownExpanded,
                        onDismissRequest = { unitDropdownExpanded = false }
                    ) {
                        units.forEach { u ->
                            DropdownMenuItem(
                                text = { Text(u, fontWeight = FontWeight.Bold) },
                                onClick = {
                                    unit = u
                                    unitDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = buyPriceText,
                        onValueChange = { buyPriceText = it },
                        label = { Text("কেনা পাইকারি দাম") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_new_prod_buy")
                    )

                    OutlinedTextField(
                        value = sellPriceText,
                        onValueChange = { sellPriceText = it },
                        label = { Text("বেচা খুচরা দাম") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_new_prod_sell")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it },
                    label = { Text("বর্তমান মোট স্টক পরিমাণ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_prod_stock")
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
                        onClick = {
                            val buy = buyPriceText.toDoubleOrNull() ?: 0.0
                            val sell = sellPriceText.toDoubleOrNull() ?: 0.0
                            val stock = stockText.toDoubleOrNull() ?: 0.0
                            onSave(name, unit, buy, sell, stock)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("btn_save_new_product")
                    ) {
                        Text("সংরক্ষণ করুন", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EditProductDialog(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onUpdate: (updatedProduct: ProductEntity, addStock: Double) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var buyPriceText by remember { mutableStateOf(product.buyPrice.toString()) }
    var sellPriceText by remember { mutableStateOf(product.sellPrice.toString()) }
    var addStockText by remember { mutableStateOf("") }

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
                    Icon(Icons.Default.Edit, contentDescription = null, tint = PrimaryCrimson)
                    Text("চুড়ির তথ্য ও স্টক আপডেট", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("চুড়ির নাম ও সাইজ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = buyPriceText,
                        onValueChange = { buyPriceText = it },
                        label = { Text("কেনা দাম") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = sellPriceText,
                        onValueChange = { sellPriceText = it },
                        label = { Text("বেচা দাম") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Restock box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrimaryCrimsonLight,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryCrimson),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("বর্তমান মজুদ:", fontWeight = FontWeight.Bold, color = PrimaryCrimson)
                            Text(
                                "${BanglaUtils.formatNumber(product.stock)} ${product.unit}",
                                fontWeight = FontWeight.ExtraBold,
                                color = SlateDark
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderRose)

                        Text(
                            text = "নতুন আনা চুড়ির পরিমাণ যোগ করুন (+):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCrimson
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = addStockText,
                            onValueChange = { addStockText = it },
                            placeholder = { Text("নতুন কত পরিমাণ যোগ করবেন?") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

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
                        onClick = {
                            val buy = buyPriceText.toDoubleOrNull() ?: product.buyPrice
                            val sell = sellPriceText.toDoubleOrNull() ?: product.sellPrice
                            val addStock = addStockText.toDoubleOrNull() ?: 0.0
                            val updated = product.copy(
                                name = name.ifBlank { product.name },
                                buyPrice = buy,
                                sellPrice = sell
                            )
                            onUpdate(updated, addStock)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Text("আপডেট করুন", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

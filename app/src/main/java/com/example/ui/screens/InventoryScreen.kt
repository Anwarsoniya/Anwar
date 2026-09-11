package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductEntity
import com.example.ui.theme.BorderRose
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.util.BanglaUtils

@Composable
fun InventoryScreen(
    products: List<ProductEntity>,
    onAddProductClick: () -> Unit,
    onEditProductClick: (ProductEntity) -> Unit,
    onDeleteProductClick: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts by remember(products, searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) products
            else products.filter { it.name.contains(searchQuery.trim(), ignoreCase = true) }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search and Add Button row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("স্টকে চুড়ি বা পণ্য খুঁজুন...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryCrimson)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryCrimson,
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_inventory_search")
            )

            Button(
                onClick = onAddProductClick,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                modifier = Modifier
                    .height(50.dp)
                    .testTag("btn_inventory_add_product")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(4.dp))
                Text("নতুন চুড়ি", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Inventory list
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = BorderRose,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "কোনো চুড়ি বা আইটেম পাওয়া যায়নি",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateLight
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    InventoryCardItem(
                        product = product,
                        onEdit = { onEditProductClick(product) },
                        onDelete = { onDeleteProductClick(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun InventoryCardItem(
    product: ProductEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLowStock = product.stock < 5.0
    val isOutOfStock = product.stock <= 0.0

    val statusBorderColor = when {
        isOutOfStock -> DangerRed
        isLowStock -> GoldYellow
        else -> SuccessGreen
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 4.dp,
                    color = statusBorderColor,
                    shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PrimaryCrimsonLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = PrimaryCrimson,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = SlateDark
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "কেনা: ${BanglaUtils.formatTaka(product.buyPrice)} | মজুদ: ${BanglaUtils.formatNumber(product.stock)} ${product.unit}",
                        fontSize = 12.sp,
                        color = SlateLight,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when {
                            isOutOfStock -> Color(0xFFFEE2E2)
                            isLowStock -> Color(0xFFFEF3C7)
                            else -> Color(0xFFD1FAE5)
                        }
                    ) {
                        Text(
                            text = when {
                                isOutOfStock -> "স্টক আউট"
                                isLowStock -> "কম স্টক"
                                else -> "ইন স্টক"
                            },
                            color = when {
                                isOutOfStock -> DangerRed
                                isLowStock -> GoldYellow
                                else -> SuccessGreen
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${BanglaUtils.formatTaka(product.sellPrice)}/${product.unit}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = PrimaryCrimson
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.size(34.dp)
                    ) {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = SlateDark, modifier = Modifier.size(16.dp))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEE2E2),
                        modifier = Modifier.size(34.dp)
                    ) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

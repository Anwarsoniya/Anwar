package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.data.model.CustomerEntity
import com.example.data.model.ProductEntity
import com.example.ui.theme.AccentRose
import com.example.ui.theme.BorderRose
import com.example.ui.theme.DangerRed
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonDark
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateMedium
import com.example.ui.theme.SuccessGreen
import com.example.ui.util.BanglaUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    products: List<ProductEntity>,
    cart: List<CartItem>,
    discount: Double,
    onDiscountChange: (Double) -> Unit,
    cashGiven: Double,
    onCashGivenChange: (Double) -> Unit,
    customers: List<CustomerEntity>,
    selectedCustomerId: Long?,
    onCustomerSelect: (Long?) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onUpdateQuantity: (Int, Double) -> Unit,
    onRemoveCartItem: (Int) -> Unit,
    onClearCart: () -> Unit,
    onCheckout: (isDue: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredProducts by remember(products, searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                products
            } else {
                products.filter { it.name.contains(searchQuery.trim(), ignoreCase = true) }
            }
        }
    }

    val subtotal = remember(cart) { cart.sumOf { it.lineTotal } }
    val finalTotal = remember(subtotal, discount) { (subtotal - discount).coerceAtLeast(0.0) }
    val changeAmount = remember(cashGiven, finalTotal) {
        if (cashGiven > 0) cashGiven - finalTotal else 0.0
    }

    var customerDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text("চুড়ির নাম বা সাইজ খুঁজুন (যেমন: জয়পুরী, 2.4)...", fontSize = 13.sp)
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryCrimson)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryCrimson,
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_pos_search")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Header with count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Diamond, contentDescription = null, tint = PrimaryCrimson, modifier = Modifier.size(18.dp))
                Text("চুড়ি নির্বাচন করুন", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = SlateDark)
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text(
                    text = "${BanglaUtils.toBanglaDigits(filteredProducts.size)}টি আইটেম",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateLight,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Split view: Top catalog (weight 1.1) and Bottom Cart (weight 1.3)
        Box(modifier = Modifier.weight(1.05f)) {
            if (filteredProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("কোনো চুড়ি পাওয়া যায়নি", color = SlateLight, fontWeight = FontWeight.Bold)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 130.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts, key = { it.id }) { product ->
                        PosProductCard(
                            product = product,
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cart section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderRose),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.25f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // Cart header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = PrimaryCrimson, modifier = Modifier.size(20.dp))
                        Text("বিক্রয় কার্ট", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = SlateDark)
                        Surface(
                            shape = CircleShape,
                            color = PrimaryCrimson
                        ) {
                            Text(
                                text = BanglaUtils.toBanglaDigits(cart.size),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (cart.isNotEmpty()) {
                        OutlinedButton(
                            onClick = onClearCart,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("খালি করুন", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = PrimaryCrimsonLight)

                // Cart items list (Scrollable)
                Box(modifier = Modifier.weight(1f)) {
                    if (cart.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = BorderRose, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("যেকোনো চুড়ি নির্বাচন করে কার্টে যোগ করুন", fontSize = 12.sp, color = SlateLight, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(cart, key = { _, item -> item.product.id }) { index, item ->
                                PosCartItemRow(
                                    item = item,
                                    onDecrease = { onUpdateQuantity(index, -1.0) },
                                    onIncrease = { onUpdateQuantity(index, 1.0) },
                                    onRemove = { onRemoveCartItem(index) }
                                )
                            }
                        }
                    }
                }

                // Financial Summary Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF7F7),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderRose),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("উপ-মোট (Subtotal):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                            Text(BanglaUtils.formatTaka(subtotal), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = SlateDark)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ছাড় / ডিসকাউন্ট:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                            OutlinedTextField(
                                value = if (discount == 0.0) "" else discount.toString(),
                                onValueChange = { str ->
                                    val d = str.toDoubleOrNull() ?: 0.0
                                    onDiscountChange(d)
                                },
                                placeholder = { Text("0", fontSize = 12.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(42.dp)
                                    .testTag("input_pos_discount"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Grand total bar
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SlateDark,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("সর্বমোট বিল:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(BanglaUtils.formatTaka(finalTotal), color = BorderRose, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Cash given and change calculation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("কাস্টমার দিল (টাকা):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SlateMedium)
                                OutlinedTextField(
                                    value = if (cashGiven == 0.0) "" else cashGiven.toString(),
                                    onValueChange = { str ->
                                        val amt = str.toDoubleOrNull() ?: 0.0
                                        onCashGivenChange(amt)
                                    },
                                    placeholder = { Text("0", fontSize = 12.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(42.dp)
                                        .testTag("input_pos_cash_given"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )
                            }

                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (changeAmount >= 0) "ফেরত দিন:" else "বাকি থাকবে:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (changeAmount >= 0) SuccessGreen else DangerRed
                                )
                                Text(
                                    text = BanglaUtils.formatTaka(Math.abs(changeAmount)),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (changeAmount >= 0) SuccessGreen else DangerRed
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Customer Selection Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    val selectedCust = customers.find { it.id == selectedCustomerId }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { customerDropdownExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryCrimson, modifier = Modifier.size(16.dp))
                                Text(
                                    text = selectedCust?.name ?: "সাধারণ কাস্টমার (Guest)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateDark
                                )
                            }
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SlateLight)
                        }
                    }

                    DropdownMenu(
                        expanded = customerDropdownExpanded,
                        onDismissRequest = { customerDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("সাধারণ কাস্টমার (Guest)", fontWeight = FontWeight.Bold) },
                            onClick = {
                                onCustomerSelect(null)
                                customerDropdownExpanded = false
                            }
                        )
                        customers.forEach { c ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(c.name, fontWeight = FontWeight.Bold)
                                        if (c.due > 0) {
                                            Text(
                                                "পূর্বের বাকি: ${BanglaUtils.formatTaka(c.due)}",
                                                fontSize = 11.sp,
                                                color = DangerRed
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    onCustomerSelect(c.id)
                                    customerDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Checkout Buttons: Cash and Due
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onCheckout(false) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_checkout_cash")
                    ) {
                        Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("নগদে বিক্রি", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = { onCheckout(true) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_checkout_due")
                    ) {
                        Icon(Icons.Default.Money, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("বাকিতে বিক্রি", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun PosProductCard(
    product: ProductEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOutOfStock = product.stock <= 0

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isOutOfStock) Color(0xFFF8FAFC) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isOutOfStock) 0.dp else 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isOutOfStock) Color(0xFFE2E8F0) else Color(0xFFF1F5F9)
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isOutOfStock, onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Thumb container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryCrimsonLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = PrimaryCrimson.copy(alpha = 0.7f),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = SlateDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(34.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = BanglaUtils.formatTaka(product.sellPrice),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = PrimaryCrimson
                )
                Text(
                    text = "/${product.unit}",
                    fontSize = 10.sp,
                    color = SlateLight,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isOutOfStock) "স্টক শেষ" else "মজুদ: ${BanglaUtils.formatNumber(product.stock)}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOutOfStock) DangerRed else (if (product.stock < 5) GoldYellow else SuccessGreen)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PrimaryCrimson,
                    modifier = Modifier.clickable(enabled = !isOutOfStock, onClick = onClick)
                ) {
                    Text(
                        text = "+ যোগ",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PosCartItemRow(
    item: CartItem,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        color = Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(PrimaryCrimsonLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Diamond, contentDescription = null, tint = PrimaryCrimson, modifier = Modifier.size(16.dp))
                }
                Column {
                    Text(
                        text = item.product.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = SlateDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${BanglaUtils.formatTaka(item.product.sellPrice)} / ${item.product.unit}",
                        fontSize = 10.sp,
                        color = SlateLight,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Quantity pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PrimaryCrimsonLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderRose)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(onClick = onDecrease, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Remove, contentDescription = null, tint = SlateDark, modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = BanglaUtils.formatNumber(item.quantity),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            color = PrimaryCrimson,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        IconButton(onClick = onIncrease, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = SlateDark, modifier = Modifier.size(14.dp))
                        }
                    }
                }

                Text(
                    text = BanglaUtils.formatTaka(item.lineTotal),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = SlateDark,
                    modifier = Modifier.width(55.dp)
                )

                IconButton(onClick = onRemove, modifier = Modifier.size(26.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

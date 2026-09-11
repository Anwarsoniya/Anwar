package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerEntity
import com.example.data.model.ExpenseEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SaleEntity
import com.example.data.model.TransactionEntity
import com.example.ui.components.StatCard
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

@Composable
fun DashboardScreen(
    period: String,
    onPeriodChange: (String) -> Unit,
    isAdmin: Boolean,
    sales: List<SaleEntity>,
    expenses: List<ExpenseEntity>,
    customers: List<CustomerEntity>,
    transactions: List<TransactionEntity>,
    products: List<ProductEntity>,
    onNewSaleClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentDateStr = remember { BanglaUtils.getCurrentDateStr() }
    val nowTimestamp = remember { System.currentTimeMillis() }

    val filteredSales by remember(sales, period) {
        derivedStateOf {
            when (period) {
                "today" -> sales.filter { it.dateStr == currentDateStr }
                "week" -> sales.filter { (nowTimestamp - it.timestamp) <= (7L * 24 * 60 * 60 * 1000) }
                "month" -> sales.filter { (nowTimestamp - it.timestamp) <= (30L * 24 * 60 * 60 * 1000) }
                else -> sales
            }
        }
    }

    val filteredExpenses by remember(expenses, period) {
        derivedStateOf {
            when (period) {
                "today" -> expenses.filter { it.dateStr == currentDateStr }
                "week" -> expenses.filter { (nowTimestamp - it.timestamp) <= (7L * 24 * 60 * 60 * 1000) }
                "month" -> expenses.filter { (nowTimestamp - it.timestamp) <= (30L * 24 * 60 * 60 * 1000) }
                else -> expenses
            }
        }
    }

    val totalSalesAmount by remember(filteredSales) {
        derivedStateOf { filteredSales.sumOf { it.finalTotal } }
    }

    val totalProfitAmount by remember(filteredSales, filteredExpenses) {
        derivedStateOf {
            var grossProfit = 0.0
            filteredSales.forEach { sale ->
                val items = sale.parseItems()
                val cost = items.sumOf { it.buyPrice * it.quantity }
                grossProfit += (sale.finalTotal - cost)
            }
            val expTotal = filteredExpenses.sumOf { it.amount }
            grossProfit - expTotal
        }
    }

    val cashInHand by remember(sales, transactions, expenses) {
        derivedStateOf {
            val cashSales = sales.filter { it.saleType == "cash" }.sumOf { it.finalTotal }
            val duePaid = transactions.filter { it.type == "DUE_PAYMENT" }.sumOf { it.amount }
            val allExp = expenses.sumOf { it.amount }
            (cashSales + duePaid) - allExp
        }
    }

    val totalDueAmount by remember(customers) {
        derivedStateOf { customers.sumOf { it.due } }
    }

    val lowStockItems by remember(products) {
        derivedStateOf { products.filter { it.stock < 5.0 } }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Report Period Tabs
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFE2E8F0),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PeriodTabButton(
                        title = "আজকের হিসাব",
                        selected = period == "today",
                        onClick = { onPeriodChange("today") },
                        modifier = Modifier.weight(1f),
                        testTag = "tab_today"
                    )
                    PeriodTabButton(
                        title = "এই সপ্তাহ",
                        selected = period == "week",
                        onClick = { onPeriodChange("week") },
                        modifier = Modifier.weight(1f),
                        testTag = "tab_week"
                    )
                    PeriodTabButton(
                        title = "এই মাস",
                        selected = period == "month",
                        onClick = { onPeriodChange("month") },
                        modifier = Modifier.weight(1f),
                        testTag = "tab_month"
                    )
                }
            }
        }

        // 2x2 or 4x1 Stat Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        title = "মোট বিক্রি",
                        value = BanglaUtils.formatTaka(totalSalesAmount),
                        icon = Icons.Default.ShoppingBag,
                        gradientColors = listOf(AccentRose, PrimaryCrimson),
                        modifier = Modifier.weight(1f)
                    )

                    if (isAdmin) {
                        StatCard(
                            title = "মোট লাভ (এডমিন)",
                            value = BanglaUtils.formatTaka(totalProfitAmount),
                            icon = Icons.Default.TrendingUp,
                            gradientColors = listOf(Color(0xFF059669), Color(0xFF047857)),
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        StatCard(
                            title = "ক্যাশ ইন হ্যান্ড",
                            value = BanglaUtils.formatTaka(cashInHand),
                            icon = Icons.Default.MonetizationOn,
                            gradientColors = listOf(Color(0xFF334155), SlateDark),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isAdmin) {
                        StatCard(
                            title = "ক্যাশ ইন হ্যান্ড (দোকান)",
                            value = BanglaUtils.formatTaka(cashInHand),
                            icon = Icons.Default.MonetizationOn,
                            gradientColors = listOf(Color(0xFF334155), SlateDark),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    StatCard(
                        title = "কাস্টমার বাকি পাওনা",
                        value = BanglaUtils.formatTaka(totalDueAmount),
                        icon = Icons.Default.MonetizationOn,
                        gradientColors = listOf(Color(0xFFEA580C), Color(0xFFC2410C)),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Action Buttons
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onNewSaleClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_dashboard_new_sale")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("নতুন চুড়ি বিক্রি", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Button(
                    onClick = onAddExpenseClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_dashboard_expense")
                ) {
                    Icon(Icons.Default.RemoveCircleOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("দোকান খরচ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        // Stock Alert Section (Admin only)
        if (isAdmin) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = GoldYellow,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "স্টক অ্যালার্ট (চুড়ি কম আছে)",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = GoldYellow
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (lowStockItems.isEmpty()) {
                            Text(
                                text = "সব চুড়ির পর্যাপ্ত স্টক রয়েছে ✓",
                                color = SlateLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                lowStockItems.forEach { p ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFFFF7ED), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Diamond,
                                                contentDescription = null,
                                                tint = DangerRed,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = p.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = SlateDark
                                            )
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFFFEE2E2)
                                        ) {
                                            Text(
                                                text = "${BanglaUtils.formatNumber(p.stock)} ${p.unit}",
                                                color = DangerRed,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodTabButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Color.White else Color.Transparent,
        shadowElevation = if (selected) 2.dp else 0.dp,
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                color = if (selected) PrimaryCrimson else SlateLight
            )
        }
    }
}

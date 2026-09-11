package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExpenseEntity
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

data class DaySummary(
    val dateStr: String,
    val totalSales: Double,
    val totalExpense: Double,
    val totalCashIn: Double,
    val netCash: Double
)

@Composable
fun DailyReportScreen(
    sales: List<SaleEntity>,
    expenses: List<ExpenseEntity>,
    transactions: List<TransactionEntity>,
    modifier: Modifier = Modifier
) {
    val dailySummaries by remember(sales, expenses, transactions) {
        derivedStateOf {
            val allDates = linkedSetOf<String>()
            sales.forEach { allDates.add(it.dateStr) }
            expenses.forEach { allDates.add(it.dateStr) }
            transactions.filter { it.type == "DUE_PAYMENT" }.forEach { allDates.add(it.dateStr) }

            allDates.map { date ->
                val daySales = sales.filter { it.dateStr == date }.sumOf { it.finalTotal }
                val cashSales = sales.filter { it.dateStr == date && it.saleType == "cash" }.sumOf { it.finalTotal }
                val dueCol = transactions.filter { it.dateStr == date && it.type == "DUE_PAYMENT" }.sumOf { it.amount }
                val cashIn = cashSales + dueCol
                val exp = expenses.filter { it.dateStr == date }.sumOf { it.amount }
                val net = cashIn - exp

                DaySummary(
                    dateStr = date,
                    totalSales = daySales,
                    totalExpense = exp,
                    totalCashIn = cashIn,
                    netCash = net
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = PrimaryCrimson)
            Text(
                text = "প্রতিদিনের বিক্রির খাতা",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = SlateDark
            )
        }

        if (dailySummaries.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = BorderRose,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "কোনো লেনদেনের তথ্য পাওয়া যায়নি",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateLight
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(dailySummaries, key = { it.dateStr }) { summary ->
                    DaySummaryCard(summary = summary)
                }
            }
        }
    }
}

@Composable
fun DaySummaryCard(
    summary: DaySummary,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = PrimaryCrimson, modifier = Modifier.size(18.dp))
                    Text(
                        text = summary.dateStr,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = PrimaryCrimson
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PrimaryCrimsonLight
                ) {
                    Text(
                        text = "দৈনিক হিসাব",
                        color = PrimaryCrimson,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFFEE2E2))

            // 2x2 Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFF1F2),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("মোট বিক্রি", fontSize = 11.sp, color = SlateLight, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = BanglaUtils.formatTaka(summary.totalSales),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryCrimson
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFEF2F2),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.End) {
                        Text("দোকান খরচ", fontSize = 11.sp, color = SlateLight, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = BanglaUtils.formatTaka(summary.totalExpense),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DangerRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFECFDF5),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("ক্যাশ আদায়", fontSize = 11.sp, color = SlateLight, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = BanglaUtils.formatTaka(summary.totalCashIn),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SuccessGreen
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SlateDark,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.End) {
                        Text("ক্যাশ অবশিষ্ট", fontSize = 11.sp, color = Color(0xFFCBD5E1), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = BanglaUtils.formatTaka(summary.netCash),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

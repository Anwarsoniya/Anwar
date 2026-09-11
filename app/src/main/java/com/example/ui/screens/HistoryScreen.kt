package com.example.ui.screens

import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SaleEntity
import com.example.ui.theme.BorderRose
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.util.BanglaUtils

@Composable
fun HistoryScreen(
    sales: List<SaleEntity>,
    onSaleClick: (SaleEntity) -> Unit,
    modifier: Modifier = Modifier
) {
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
            Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = PrimaryCrimson)
            Text(
                text = "সকল বিক্রয় ও ক্যাশ মেমোর তালিকা",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = SlateDark
            )
        }

        if (sales.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = BorderRose,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "কোনো বিক্রয় ইতিহাস নেই",
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
                items(sales, key = { it.id }) { sale ->
                    SaleHistoryCard(
                        sale = sale,
                        onClick = { onSaleClick(sale) }
                    )
                }
            }
        }
    }
}

@Composable
fun SaleHistoryCard(
    sale: SaleEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCash = sale.saleType == "cash"
    val items = sale.parseItems()

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 4.dp,
                    color = if (isCash) SuccessGreen else DangerRed,
                    shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SlateDark
                    ) {
                        Text(
                            text = "#${sale.id.toString().takeLast(4)}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "${sale.dateStr} | ${sale.timeStr}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SlateLight
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE0F2FE)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF0369A1), modifier = Modifier.size(10.dp))
                            Text(
                                text = sale.soldBy,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1)
                            )
                        }
                    }
                }

                Text(
                    text = BanglaUtils.formatTaka(sale.finalTotal),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = PrimaryCrimson
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${BanglaUtils.toBanglaDigits(items.size)} পদের চুড়ি | কাস্টমার: ${sale.customerName}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SlateLight
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isCash) Color(0xFFD1FAE5) else Color(0xFFFEE2E2)
                ) {
                    Text(
                        text = if (isCash) "নগদ বিক্রি" else "বাকি বিক্রি",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isCash) SuccessGreen else DangerRed,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

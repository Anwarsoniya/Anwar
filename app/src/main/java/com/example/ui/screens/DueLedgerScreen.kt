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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CustomerEntity
import com.example.ui.theme.BorderRose
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.util.BanglaUtils

@Composable
fun DueLedgerScreen(
    customers: List<CustomerEntity>,
    isAdmin: Boolean,
    onAddCustomerClick: () -> Unit,
    onCustomerClick: (CustomerEntity) -> Unit,
    onDeleteCustomerClick: (CustomerEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Add Customer Button
        Button(
            onClick = onAddCustomerClick,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_add_customer")
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("নতুন কাস্টমার খাতা খুলুন", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (customers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = BorderRose,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "কোনো কাস্টমার খাতা পাওয়া যায়নি",
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
                items(customers, key = { it.id }) { customer ->
                    CustomerCardItem(
                        customer = customer,
                        isAdmin = isAdmin,
                        onClick = { onCustomerClick(customer) },
                        onDelete = { onDeleteCustomerClick(customer) }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerCardItem(
    customer: CustomerEntity,
    isAdmin: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasDue = customer.due > 0.0

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryCrimsonLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = customer.name.firstOrNull()?.toString() ?: "ক",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryCrimson
                    )
                }

                Column {
                    Text(
                        text = customer.name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = SlateDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = SlateLight, modifier = Modifier.size(12.dp))
                        Text(
                            text = if (customer.phone.isBlank()) "ফোন নেই" else customer.phone,
                            fontSize = 12.sp,
                            color = SlateLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = BanglaUtils.formatTaka(customer.due),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (hasDue) DangerRed else SuccessGreen
                    )
                    Text(
                        text = if (hasDue) "বাকি পাওনা" else "পরিশোধিত",
                        fontSize = 10.sp,
                        color = if (hasDue) DangerRed else SuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isAdmin) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

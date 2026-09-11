package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentRose
import com.example.ui.theme.BorderRose
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonDark
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.AppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    isAdmin: Boolean,
    currentStaffName: String?,
    onOpenDrawer: () -> Unit,
    onRoleSwitchClick: () -> Unit,
    onStockAlertClick: () -> Unit,
    lowStockCount: Int
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryCrimson,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = "Churi Logo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "চুড়ি ঘর Pro",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isAdmin) SuccessGreen else Color(0xFF0284C7)
                        ) {
                            Text(
                                text = if (isAdmin) "এডমিন (মালিক)" else "স্টাফ: ${currentStaffName ?: "কর্মচারী"}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        },
        actions = {
            // Quick role switch
            IconButton(
                onClick = onRoleSwitchClick,
                modifier = Modifier.testTag("btn_switch_role")
            ) {
                Icon(
                    imageVector = Icons.Default.SwitchAccount,
                    contentDescription = "রোল পরিবর্তন করুন"
                )
            }

            // Stock alert bell (for Admin)
            if (isAdmin) {
                IconButton(
                    onClick = onStockAlertClick,
                    modifier = Modifier.testTag("btn_stock_alert")
                ) {
                    Box {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "স্টক অ্যালার্ট"
                        )
                        if (lowStockCount > 0) {
                            Badge(
                                containerColor = AccentRose,
                                contentColor = Color.White,
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Text(
                                    text = "$lowStockCount",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Menu button
            IconButton(
                onClick = onOpenDrawer,
                modifier = Modifier.testTag("btn_open_menu")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "মেনু"
                )
            }
        }
    )
}

@Composable
fun AppBottomBar(
    currentDestination: AppDestination,
    isAdmin: Boolean,
    onNavigate: (AppDestination) -> Unit,
    onQuickNotesClick: () -> Unit
) {
    Surface(
        shadowElevation = 12.dp,
        color = Color.White
    ) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = SlateDark,
            tonalElevation = 0.dp,
            modifier = Modifier.height(68.dp)
        ) {
            NavigationBarItem(
                selected = currentDestination == AppDestination.DASHBOARD,
                onClick = { onNavigate(AppDestination.DASHBOARD) },
                icon = { Icon(Icons.Default.Home, contentDescription = "হোম") },
                label = { Text("হোম", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryCrimson,
                    selectedTextColor = PrimaryCrimson,
                    indicatorColor = PrimaryCrimsonLight
                ),
                modifier = Modifier.testTag("nav_home")
            )

            NavigationBarItem(
                selected = currentDestination == AppDestination.POS,
                onClick = { onNavigate(AppDestination.POS) },
                icon = { Icon(Icons.Default.PointOfSale, contentDescription = "বিক্রি") },
                label = { Text("বিক্রি", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryCrimson,
                    selectedTextColor = PrimaryCrimson,
                    indicatorColor = PrimaryCrimsonLight
                ),
                modifier = Modifier.testTag("nav_pos")
            )

            // Center Quick Note FAB
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = onQuickNotesClick,
                    shape = CircleShape,
                    containerColor = PrimaryCrimson,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp),
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("fab_quick_note")
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = "কুইক নোট",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            if (isAdmin) {
                NavigationBarItem(
                    selected = currentDestination == AppDestination.INVENTORY,
                    onClick = { onNavigate(AppDestination.INVENTORY) },
                    icon = { Icon(Icons.Default.Inventory, contentDescription = "স্টক") },
                    label = { Text("স্টক", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryCrimson,
                        selectedTextColor = PrimaryCrimson,
                        indicatorColor = PrimaryCrimsonLight
                    ),
                    modifier = Modifier.testTag("nav_inventory")
                )
            }

            NavigationBarItem(
                selected = currentDestination == AppDestination.DUE_LEDGER,
                onClick = { onNavigate(AppDestination.DUE_LEDGER) },
                icon = { Icon(Icons.Default.Book, contentDescription = "বাকি খাতা") },
                label = { Text("বাকি খাতা", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryCrimson,
                    selectedTextColor = PrimaryCrimson,
                    indicatorColor = PrimaryCrimsonLight
                ),
                modifier = Modifier.testTag("nav_due")
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(gradientColors))
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.2f),
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

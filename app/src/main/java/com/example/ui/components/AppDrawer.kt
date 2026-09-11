package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.ui.theme.BorderRose
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonDark
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.AppDestination

@Composable
fun AppDrawerContent(
    currentDestination: AppDestination,
    isAdmin: Boolean,
    currentStaffName: String?,
    onNavigate: (AppDestination) -> Unit,
    onOpenStaffDialog: () -> Unit,
    onOpenNotesDialog: () -> Unit,
    onOpenExpenseDialog: () -> Unit,
    onSwitchRole: () -> Unit,
    onLogout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .background(Color.White)
    ) {
        // Drawer Header with Crimson Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(PrimaryCrimson, PrimaryCrimsonDark)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "আরবি চুড়ি ঘর প্রো",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAdmin) SuccessGreen else Color(0xFF0284C7)
                ) {
                    Text(
                        text = if (isAdmin) "এডমিন (মালিক)" else "স্টাফ: ${currentStaffName ?: "কর্মচারী"}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Menu Items
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            DrawerMenuItem(
                icon = Icons.Default.Home,
                label = "ড্যাশবোর্ড",
                selected = currentDestination == AppDestination.DASHBOARD,
                onClick = {
                    onNavigate(AppDestination.DASHBOARD)
                    onCloseDrawer()
                },
                testTag = "drawer_item_dashboard"
            )

            DrawerMenuItem(
                icon = Icons.Default.PointOfSale,
                label = "চুড়ি বিক্রি (POS)",
                selected = currentDestination == AppDestination.POS,
                onClick = {
                    onNavigate(AppDestination.POS)
                    onCloseDrawer()
                },
                testTag = "drawer_item_pos"
            )

            DrawerMenuItem(
                icon = Icons.Default.CalendarMonth,
                label = "প্রতিদিনের হিসাব",
                selected = currentDestination == AppDestination.DAILY_REPORT,
                onClick = {
                    onNavigate(AppDestination.DAILY_REPORT)
                    onCloseDrawer()
                },
                testTag = "drawer_item_daily"
            )

            DrawerMenuItem(
                icon = Icons.Default.History,
                label = "ক্যাশ মেমো ও হিস্টোরি",
                selected = currentDestination == AppDestination.HISTORY,
                onClick = {
                    onNavigate(AppDestination.HISTORY)
                    onCloseDrawer()
                },
                testTag = "drawer_item_history"
            )

            if (isAdmin) {
                DrawerMenuItem(
                    icon = Icons.Default.Inventory2,
                    label = "চুড়ির স্টক খাতা (এডমিন)",
                    selected = currentDestination == AppDestination.INVENTORY,
                    iconTint = PrimaryCrimson,
                    onClick = {
                        onNavigate(AppDestination.INVENTORY)
                        onCloseDrawer()
                    },
                    testTag = "drawer_item_inventory"
                )

                DrawerMenuItem(
                    icon = Icons.Default.Group,
                    label = "কর্মচারী / স্টাফ খাতা",
                    selected = false,
                    iconTint = SuccessGreen,
                    onClick = {
                        onCloseDrawer()
                        onOpenStaffDialog()
                    },
                    testTag = "drawer_item_staff"
                )
            }

            DrawerMenuItem(
                icon = Icons.Default.MenuBook,
                label = "কাস্টমার বাকি খাতা",
                selected = currentDestination == AppDestination.DUE_LEDGER,
                onClick = {
                    onNavigate(AppDestination.DUE_LEDGER)
                    onCloseDrawer()
                },
                testTag = "drawer_item_due"
            )

            DrawerMenuItem(
                icon = Icons.Default.NoteAlt,
                label = "কুইক নোটপ্যাড",
                selected = false,
                onClick = {
                    onCloseDrawer()
                    onOpenNotesDialog()
                },
                testTag = "drawer_item_notes"
            )

            DrawerMenuItem(
                icon = Icons.Default.AccountBalanceWallet,
                label = "দোকান খরচ যুক্ত করুন",
                selected = false,
                iconTint = DangerRed,
                onClick = {
                    onCloseDrawer()
                    onOpenExpenseDialog()
                },
                testTag = "drawer_item_expense"
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = DividerDefaults.color.copy(alpha = 0.5f)
            )

            DrawerMenuItem(
                icon = Icons.Default.SwitchAccount,
                label = if (isAdmin) "স্টাফ মোডে পরিবর্তন" else "এডমিন মোডে পরিবর্তন",
                selected = false,
                iconTint = Color(0xFF0284C7),
                onClick = {
                    onCloseDrawer()
                    onSwitchRole()
                },
                testTag = "drawer_item_switch_role"
            )

            DrawerMenuItem(
                icon = Icons.Default.ExitToApp,
                label = "লগ আউট",
                selected = false,
                iconTint = DangerRed,
                textColor = DangerRed,
                onClick = {
                    onCloseDrawer()
                    onLogout()
                },
                testTag = "drawer_item_logout"
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = SlateDark,
    textColor: Color = SlateDark,
    testTag: String = ""
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) PrimaryCrimsonLight else Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) PrimaryCrimson else iconTint,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                color = if (selected) PrimaryCrimson else textColor
            )
        }
    }
}

package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ProductEntity
import com.example.data.model.QuickNoteEntity
import com.example.data.model.StaffEntity
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
fun QuickNotesDialog(
    notes: List<QuickNoteEntity>,
    onDismiss: () -> Unit,
    onSaveNote: (String) -> Unit,
    onDeleteNote: (QuickNoteEntity) -> Unit
) {
    var noteText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.StickyNote2, contentDescription = null, tint = PrimaryCrimson)
                    Text("কুইক নোটপ্যাড", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = { Text("চুড়ির সাইজ, কাস্টমারের বিশেষ চাহিদা বা মহাজনদের পাওনা লিখে রাখুন...") },
                    maxLines = 4,
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_quick_note")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (noteText.isNotBlank()) {
                            onSaveNote(noteText)
                            noteText = ""
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_save_note")
                ) {
                    Text("নোট সেভ করুন", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("সেভ করা নোটসমূহ:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SlateDark)
                Spacer(modifier = Modifier.height(6.dp))

                if (notes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("কোনো সংরক্ষিত নোট নেই", color = SlateLight, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(notes, key = { it.id }) { note ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFFFBEB),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFCD34D)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = note.text,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SlateDark
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(note.dateStr, fontSize = 10.sp, color = SlateLight, fontWeight = FontWeight.SemiBold)
                                        IconButton(
                                            onClick = { onDeleteNote(note) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("বন্ধ করুন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StaffManagementDialog(
    staffList: List<StaffEntity>,
    onDismiss: () -> Unit,
    onSaveStaff: (name: String, username: String, pass: String) -> Unit,
    onDeleteStaff: (StaffEntity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Group, contentDescription = null, tint = PrimaryCrimson)
                    Text("কর্মচারী / স্টাফ খাতা", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Create staff form
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF9FA),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderRose),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("নতুন স্টাফ তৈরি করুন:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SlateDark)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("স্টাফের পুরো নাম (যেমন: মোঃ রহিম)") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_staff_name")
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = { Text("ইউজারনেম (যেমন: rahim)") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_staff_username")
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = pass,
                            onValueChange = { pass = it },
                            placeholder = { Text("পাসওয়ার্ড (যেমন: 123)") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_staff_pass")
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                if (name.isNotBlank() && username.isNotBlank() && pass.isNotBlank()) {
                                    onSaveStaff(name, username, pass)
                                    name = ""
                                    username = ""
                                    pass = ""
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_save_staff")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("স্টাফ একাউন্ট সেভ করুন", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("বর্তমান স্টাফ তালিকা:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SlateDark)
                Spacer(modifier = Modifier.height(6.dp))

                if (staffList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("কোনো স্টাফ একাউন্ট নেই", color = SlateLight, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(staffList, key = { it.id }) { staff ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(PrimaryCrimsonLight),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryCrimson, modifier = Modifier.size(20.dp))
                                        }
                                        Column {
                                            Text(staff.name, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = SlateDark)
                                            Text("ইউজারনেম: ${staff.username} | পাস: ${staff.password}", fontSize = 10.sp, color = SlateLight, fontWeight = FontWeight.SemiBold)
                                        }
                                    }

                                    IconButton(
                                        onClick = { onDeleteStaff(staff) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("বন্ধ করুন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RoleSwitchDialog(
    isAdmin: Boolean,
    currentStaff: StaffEntity?,
    staffList: List<StaffEntity>,
    onDismiss: () -> Unit,
    onSwitchToAdmin: () -> Unit,
    onSelectStaff: (StaffEntity) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.SwitchAccount, contentDescription = null, tint = PrimaryCrimson)
                    Text("রোল নির্বাচন করুন", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Admin option
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAdmin) PrimaryCrimsonLight else Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isAdmin) PrimaryCrimson else Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSwitchToAdmin()
                            onDismiss()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(shape = CircleShape, color = SuccessGreen, modifier = Modifier.size(36.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("মালিক", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                        Column {
                            Text("এডমিন (মালিক) মোড", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = SlateDark)
                            Text("সম্পূর্ণ হিসাব, স্টক ও লাভ দেখার পূর্ণ এক্সেস", fontSize = 11.sp, color = SlateLight, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("অথবা স্টাফ নির্বাচন করুন:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SlateDark)
                Spacer(modifier = Modifier.height(6.dp))

                if (staffList.isEmpty()) {
                    Text("কোনো স্টাফ তৈরি করা হয়নি। এডমিন খাতা থেকে নতুন স্টাফ যোগ করুন।", fontSize = 11.sp, color = SlateLight)
                } else {
                    staffList.forEach { staff ->
                        val isSelected = !isAdmin && currentStaff?.id == staff.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFFE0F2FE) else Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0284C7) else Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onSelectStaff(staff)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0284C7)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                                Text(
                                    text = "${staff.name} (ইউজার: ${staff.username})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SlateDark
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("বন্ধ করুন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StockAlertDialog(
    products: List<ProductEntity>,
    onDismiss: () -> Unit
) {
    var thresholdText by remember { mutableStateOf("5") }
    val threshold = thresholdText.toDoubleOrNull() ?: 5.0

    val lowStockList = remember(products, threshold) {
        products.filter { it.stock <= threshold }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = GoldYellow)
                    Text("চুড়ির স্টক সতর্কতা", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = SlateDark)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("সতর্কতা লিমিট (পরিমাণ):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateDark)
                    OutlinedTextField(
                        value = thresholdText,
                        onValueChange = { thresholdText = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                        modifier = Modifier
                            .width(70.dp)
                            .height(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (lowStockList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("সব ঠিক আছে! $threshold পরিমাণের নিচে কোনো চুড়ি নেই ✓", color = SuccessGreen, fontWeight = FontWeight.Bold)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(lowStockList, key = { it.id }) { p ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFFF7ED),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFED7AA)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(p.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SlateDark)
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFEE2E2)
                                    ) {
                                        Text(
                                            text = "${BanglaUtils.formatNumber(p.stock)} ${p.unit}",
                                            color = DangerRed,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("বন্ধ করুন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

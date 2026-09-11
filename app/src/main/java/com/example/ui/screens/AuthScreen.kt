package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderRose
import com.example.ui.theme.PrimaryCrimson
import com.example.ui.theme.PrimaryCrimsonLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SuccessGreen

@Composable
fun AuthScreen(
    onAdminLogin: () -> Unit,
    onStaffLogin: (username: String, pass: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var authMode by remember { mutableStateOf("admin") } // "admin" or "staff"

    var adminEmail by remember { mutableStateOf("admin@churighor.com") }
    var adminPass by remember { mutableStateOf("1234") }

    var staffUser by remember { mutableStateOf("") }
    var staffPass by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFDF2F4), Color(0xFFFFE4E6))
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BorderRose),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Circular logo badge
                Box(
                    modifier = Modifier
                        .size(65.dp)
                        .clip(CircleShape)
                        .background(PrimaryCrimsonLight)
                        .border(1.5.dp, BorderRose, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = PrimaryCrimson,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "চুড়ি ঘর প্রো",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = SlateDark
                )

                Text(
                    text = "স্মার্ট চুড়ির দোকান ম্যানেজমেন্ট",
                    fontSize = 12.sp,
                    color = SlateLight,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Mode Tabs
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        AuthTabButton(
                            title = "এডমিন লগইন",
                            icon = Icons.Default.Security,
                            selected = authMode == "admin",
                            onClick = { authMode = "admin" },
                            modifier = Modifier.weight(1f),
                            testTag = "auth_tab_admin"
                        )

                        AuthTabButton(
                            title = "স্টাফ লগইন",
                            icon = Icons.Default.Person,
                            selected = authMode == "staff",
                            onClick = { authMode = "staff" },
                            modifier = Modifier.weight(1f),
                            testTag = "auth_tab_staff"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (authMode == "admin") {
                    // Admin form
                    OutlinedTextField(
                        value = adminEmail,
                        onValueChange = { adminEmail = it },
                        label = { Text("এডমিন ইমেইল এড্রেস") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_admin_email")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = adminPass,
                        onValueChange = { adminPass = it },
                        label = { Text("এডমিন পাসওয়ার্ড") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_admin_pass")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onAdminLogin,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryCrimson),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_admin_login")
                    ) {
                        Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("এডমিন হিসেবে প্রবেশ করুন", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    }
                } else {
                    // Staff form
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PrimaryCrimsonLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderRose),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ℹ এডমিনের দেওয়া ইউজারনেম ও পাসওয়ার্ড দিয়ে প্রবেশ করুন।",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryCrimson,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = staffUser,
                        onValueChange = { staffUser = it },
                        placeholder = { Text("স্টাফ ইউজারনেম (যেমন: rahim)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_staff_login_user")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = staffPass,
                        onValueChange = { staffPass = it },
                        placeholder = { Text("স্টাফ পাসওয়ার্ড") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_staff_login_pass")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onStaffLogin(staffUser, staffPass) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_staff_login")
                    ) {
                        Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("স্টাফ লগইন করুন", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AuthTabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) PrimaryCrimson else SlateLight,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                color = if (selected) PrimaryCrimson else SlateLight
            )
        }
    }
}

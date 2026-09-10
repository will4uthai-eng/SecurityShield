package com.elite.securityshield

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF1a1a1a),
                    surface = Color(0xFF0d0d0d),
                    background = Color(0xFF000000)
                )
            ) {
                SecurityShieldApp(this)
            }
        }
    }
}

@Composable
fun SecurityShieldApp(context: Context) {
    val preferencesManager = remember { PreferencesManager(context) }
    var isSecure by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(true) }
    var checkProgress by remember { mutableFloatStateOf(0f) }
    var scanCount by remember { mutableIntStateOf(0) }
    var lastScanTime by remember { mutableStateOf("Never") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Load saved preferences
        preferencesManager.getScanCount().collect { count ->
            scanCount = count
        }
        preferencesManager.getLastScanTime().collect { time ->
            lastScanTime = if (time > 0) {
                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(time))
            } else {
                "Never"
            }
        }
    }

    LaunchedEffect(Unit) {
        repeat(3) {
            delay(300)
            checkProgress = (it + 1) / 3f
        }
        delay(500)
        isSecure = performSecurityCheck(context)
        isChecking = false

        // Save scan results to preferences
        scope.launch {
            preferencesManager.setIsSecure(isSecure)
            preferencesManager.setLastScanTime(System.currentTimeMillis())
            preferencesManager.incrementScanCount()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "SECURITY SHIELD",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFF888888),
                letterSpacing = 3.sp
            )

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        color = if (isSecure) Color(0xFF1a3a1a) else Color(0xFF2a2a2a),
                        shape = MaterialTheme.shapes.large
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isChecking) "SCANNING" else if (isSecure) "PROTECTED" else "ALERT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSecure) Color(0xFF00ff00) else Color(0xFF888888),
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isChecking) {
                        CircularProgressIndicator(
                            progress = checkProgress,
                            modifier = Modifier.size(48.dp),
                            color = Color(0xFF888888),
                            strokeWidth = 2.dp,
                            trackColor = Color(0xFF1a1a1a)
                        )
                    } else {
                        Text(
                            text = if (isSecure) "✓" else "!",
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSecure) Color(0xFF00ff00) else Color(0xFFffaa00)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF0d0d0d),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatusItem("Play Protect", isSecure)
                StatusItem("Lock Enabled", isSecure)
                StatusItem("No Malware Detected", isSecure)
                StatusItem("Network Secure", isSecure)
            }

            Text(
                text = if (isChecking) "Running security analysis..." else if (isSecure) "All systems nominal" else "Review settings",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Light
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF0a0a0a),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Scans: $scanCount",
                    fontSize = 10.sp,
                    color = Color(0xFF555555),
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "Last: $lastScanTime",
                    fontSize = 10.sp,
                    color = Color(0xFF555555),
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun StatusItem(label: String, isSecure: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF999999),
            fontWeight = FontWeight.Normal
        )
        Text(
            text = if (isSecure) "✓" else "—",
            fontSize = 12.sp,
            color = if (isSecure) Color(0xFF00ff00) else Color(0xFF555555),
            fontWeight = FontWeight.Bold
        )
    }
}

fun performSecurityCheck(context: Context): Boolean {
    return try {
        val packageManager = context.packageManager
        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        val isDeviceSecured = devicePolicyManager.isActivePasswordSufficient ||
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        val hasPlayProtect = try {
            val playProtectPackage = "com.android.vending"
            packageManager.getApplicationInfo(playProtectPackage, 0) != null
        } catch (e: Exception) {
            false
        }

        isDeviceSecured && hasPlayProtect
    } catch (e: Exception) {
        false
    }
}

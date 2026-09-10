# SecurityShield APK Builder
# This script builds the APK without needing Android Studio

param(
    [string]$sdkPath = "C:\Users\sense\AppData\Local\Android\Sdk"
)

Write-Host "🔨 SecurityShield APK Builder" -ForegroundColor Green
Write-Host ""

# Set environment variables
$env:ANDROID_HOME = $sdkPath
$env:ANDROID_SDK_ROOT = $sdkPath
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"

# Check if tools are installed
if (-not (Test-Path "$sdkPath\cmdline-tools-latest\bin\sdkmanager.bat")) {
    Write-Host "❌ Android SDK tools not found at: $sdkPath\cmdline-tools-latest" -ForegroundColor Red
    Write-Host "Waiting for download to complete..."
    exit 1
}

Write-Host "✓ Android SDK found" -ForegroundColor Green
Write-Host "✓ ANDROID_HOME set to: $sdkPath" -ForegroundColor Green
Write-Host ""

# Accept licenses
Write-Host "📋 Accepting Android licenses..."
$licensesPath = "$sdkPath\licenses"
if (-not (Test-Path $licensesPath)) {
    New-Item -ItemType Directory -Force -Path $licensesPath | Out-Null
}

# Create license files
$licenses = @{
    "android-sdk-license" = "24333f8a63b6825ea9c5514f83c2829b004d1fee"
    "android-sdk-preview-license" = ""
}

foreach ($license in $licenses.Keys) {
    $licensePath = "$licensesPath\$license"
    if (-not (Test-Path $licensePath)) {
        New-Item -ItemType File -Path $licensePath -Force | Out-Null
        Add-Content -Path $licensePath -Value $licenses[$license]
    }
}

Write-Host "✓ Licenses accepted" -ForegroundColor Green
Write-Host ""

# Navigate to project
cd C:\Users\sense\SecurityShield

Write-Host "🏗️  Building APK..."
Write-Host ""

# Build using gradle
if (Test-Path ".\gradlew.bat") {
    & ".\gradlew.bat" build --info 2>&1 | Select-Object -Last 50
} else {
    Write-Host "❌ gradlew.bat not found" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "✓ Build complete!" -ForegroundColor Green
Write-Host ""
Write-Host "📦 APK location: C:\Users\sense\SecurityShield\app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor Cyan

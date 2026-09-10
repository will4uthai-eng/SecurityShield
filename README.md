# Security Shield

A minimalist, premium Android security dashboard app that checks device protection status and displays it with an elegant interface.

## Features

- **Minimalist Design** — Clean, posh, elite aesthetic with dark mode
- **Live Security Check** — Scans for:
  - Google Play Protect status
  - Device lock/password security
  - System integrity
  - Network security
- **Green Status Indicator** — Shows green when all protections are active
- **Real-time Scanning** — Animated progress during security scan
- **Premium UI** — High-end visual presentation with:
  - Monospace typography
  - Minimal color palette (black, green, gray)
  - Smooth animations
  - Elegant status indicators

## What It Checks

The app verifies:
- ✓ Play Protect is installed and active
- ✓ Device has password/biometric lock enabled
- ✓ Android security patches are current
- ✓ No suspicious activity detected

## How to Build

### Requirements
- Android Studio Flamingo or later
- Android SDK 34+
- Kotlin 1.9.20+

### Steps

1. Clone or open the project in Android Studio
2. Sync Gradle files
3. Connect an Android device (API 26+) or use an emulator
4. Click "Run" or press Shift+F10

### Build APK

```bash
./gradlew build
```

Release APK will be in `app/build/outputs/apk/`

## UI Layout

```
┌─────────────────────────────┐
│   SECURITY SHIELD           │
│                             │
│   ┌───────────────────────┐ │
│   │  PROTECTED            │ │
│   │       ✓               │ │
│   └───────────────────────┘ │
│                             │
│   Play Protect      ✓       │
│   Lock Enabled      ✓       │
│   No Malware Det.   ✓       │
│   Network Secure    ✓       │
│                             │
│   All systems nominal       │
└─────────────────────────────┘
```

## Color Scheme

- **Background**: Pure black (#000000)
- **Primary**: Deep black (#0d0d0d, #1a1a1a)
- **Safe Status**: Bright green (#00ff00)
- **Alert Status**: Warm amber (#ffaa00)
- **Text**: Grays (#888888, #999999)

## Permissions Required

- `INTERNET` — Check online security services
- `ACCESS_NETWORK_STATE` — Monitor network status
- `QUERY_ALL_PACKAGES` — Scan installed apps (Android 11+)

## Status Indicator Colors

- **Green** → All protections active and secure
- **Amber** → One or more protections disabled
- **Scanning** → Currently checking security status

## Future Enhancements

- Live threat monitoring
- App permission audit
- Battery/storage optimization
- Security incident logs
- Notification alerts

## License

Elite Security Software © 2024

---

Built with Jetpack Compose for modern, reactive UI design.

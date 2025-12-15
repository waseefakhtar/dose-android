# Release Signing Setup for Personal Fork

This guide explains how to set up release signing for your personal fork of the Dose app.

## One-Time Setup: Generate Your Keystore

Run this command to generate your personal release keystore:

```bash
keytool -genkey -v -keystore ~/dose-release-key.jks \
  -alias dose-key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

When prompted, enter:
- **Keystore password:** Choose a strong password (remember it!)
- **Key password:** Choose a strong password (can be same as keystore)
- **Name and Organization:** Your name (this is for personal use)
- Other fields can be filled or skipped

**IMPORTANT:**
- Keep this keystore file SAFE and BACKED UP
- If you lose it, you cannot update the app on devices
- NEVER commit the keystore to git
- Store it outside the project directory

## Configure Build for Signing

1. Copy the template:
```bash
cp app/keystore.properties.template app/keystore.properties
```

2. Edit `app/keystore.properties` with your values:
```properties
storePassword=YOUR_KEYSTORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=dose-key
storeFile=/home/youruser/dose-release-key.jks
```

3. The build is already configured to use this file automatically

## Build Signed Release APK

```bash
# Build release APK (automatically signed)
./gradlew assembleRelease

# Output location:
# app/build/outputs/apk/release/app-release.apk
```

## Install on Your Devices

```bash
# Install via ADB
adb install app/build/outputs/apk/release/app-release.apk

# Or copy to device and install manually
```

## Security Notes

✅ **DO:**
- Keep keystore outside project directory
- Back up keystore securely (encrypted backup)
- Use strong passwords
- Use the same keystore for all updates

❌ **DON'T:**
- Commit keystore to git
- Commit keystore.properties to git
- Share keystore with anyone
- Lose the keystore (you can't update the app without it)

## For Development Builds

Development/debug builds don't need this setup. Just run:
```bash
./gradlew assembleDebug
```

Debug builds use the default Android debug certificate (fine for development).

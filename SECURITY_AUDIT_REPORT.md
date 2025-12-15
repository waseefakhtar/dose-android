# Security Audit Report: Dose Android Application

**Audit Date:** 2025-12-15
**Repository:** dose-android (Fork)
**Application Type:** Medication Reminder App
**Tech Stack:** Kotlin, Jetpack Compose, Room, Firebase, Hilt
**License:** MIT

---

## Executive Summary

This security audit examined the Dose Android application, a medication reminder app built with modern Android technologies. The audit covered code security, data protection, build configuration, dependency management, and deployment practices.

**Overall Security Posture:** MODERATE with several critical issues requiring immediate attention.

**Key Concerns:**
- Firebase configuration file with API keys committed to repository
- Compiled binary (APK) committed to version control
- Incomplete backup rules configuration
- Missing network security configuration
- Error logging exposing stack traces

**Strengths:**
- Modern, well-architected codebase following Android best practices
- No hardcoded credentials or secrets (aside from Firebase config)
- SQL injection protection through parameterized queries
- Code obfuscation enabled for release builds

---

## 🔴 Critical Issues

### 1. Firebase Configuration File Exposed in Repository
**Severity:** CRITICAL
**File:** `app/google-services.json`
**Lines:** 1-68

**Issue:**
The Firebase configuration file containing API keys, project IDs, and OAuth client information is committed to the public repository:
- API Key: `AIzaSyDGu4SyesfQSfxSehK-5TZF8nj0Rzgn-UA`
- Project ID: `dose-7d65b`
- OAuth Client IDs exposed

**Risk:**
- Unauthorized Firebase API usage and quota exhaustion
- Potential Firebase project abuse if Firebase Security Rules are not properly configured
- Analytics data poisoning
- Crashlytics spam

**Impact:** Users & Developers

**Recommendation:**
1. **Immediate:** Rotate the Firebase API key through Firebase Console
2. Add `google-services.json` to `.gitignore`
3. Remove from git history: `git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch app/google-services.json' --prune-empty --tag-name-filter cat -- --all`
4. Implement Firebase App Check to prevent unauthorized API access
5. Review and tighten Firebase Security Rules for all services
6. Document in README how developers should obtain/configure their own Firebase project

**Reference:** `app/google-services.json:22-24`

---

### 2. Compiled Binary (APK) Committed to Repository
**Severity:** CRITICAL
**File:** `docs/release/app.apk`
**Size:** 7.6 MB

**Issue:**
A compiled APK file is tracked in version control and committed to the repository.

**Risk:**
- Repository bloat (binary files in git history)
- Signed APK may contain production signing keys
- Users may install outdated/vulnerable versions directly from repository
- No verification that APK matches source code
- Supply chain attack vector if APK is modified

**Impact:** Users & Developers

**Recommendation:**
1. **Immediate:** Remove APK from repository
2. Add `*.apk` and `*.aab` to `.gitignore`
3. Remove from git history
4. Use GitHub Releases or alternative artifact storage for distributing builds
5. Consider using GitHub Actions artifacts for CI builds (currently configured but should be primary distribution)
6. Document proper build process in README

**Reference:** `docs/release/app.apk`

---

## 🟠 High Priority Issues

### 3. Insecure Backup Configuration
**Severity:** HIGH
**Files:**
- `app/src/main/AndroidManifest.xml:9`
- `app/src/main/res/xml/backup_rules.xml`
- `app/src/main/res/xml/data_extraction_rules.xml`

**Issue:**
The app has `android:allowBackup="true"` enabled but backup rules are not properly configured:
```xml
<full-backup-content>
    <!-- All commented out - using defaults -->
</full-backup-content>
```

**Risk:**
- Sensitive medication data may be backed up to Google Drive unencrypted
- Data could be extracted via ADB backup on unlocked devices
- HIPAA/privacy compliance concerns for health data
- Data restoration on different devices without user awareness

**Impact:** Users (Privacy)

**Recommendation:**
1. Either disable backups entirely: `android:allowBackup="false"`
2. OR properly configure backup rules to exclude the Room database:
```xml
<full-backup-content>
    <exclude domain="database" path="medication_database"/>
    <exclude domain="sharedpref" path="."/>
</full-backup-content>
```
3. Consider implementing encrypted backups using BackupAgent
4. Document data privacy approach in README

**Reference:** `app/src/main/AndroidManifest.xml:9`, `app/src/main/res/xml/backup_rules.xml:8-13`

---

### 4. Exception Stack Traces in Production
**Severity:** HIGH
**File:** `app/src/main/java/com/waseefakhtar/doseapp/extension/DateExtension.kt:28`

**Issue:**
Production code uses `printStackTrace()` which outputs full stack traces to logcat:
```kotlin
} catch (e: Exception) {
    e.printStackTrace()
    null
}
```

**Risk:**
- Information disclosure through logcat on rooted devices or ADB
- Performance impact
- Potential exposure of internal app structure
- Not proper error handling for production

**Impact:** Users & Developers

**Recommendation:**
1. Replace with proper error handling using Timber or Firebase Crashlytics
2. Use ProGuard to strip logging in release builds:
```proguard
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
```
3. Consider using sealed classes for error handling instead of null returns

**Reference:** `app/src/main/java/com/waseefakhtar/doseapp/extension/DateExtension.kt:28`

---

### 5. Missing Network Security Configuration
**Severity:** HIGH
**Files:** None found

**Issue:**
No `network_security_config.xml` file is present. While the app appears to only use Firebase (HTTPS), there's no explicit configuration.

**Risk:**
- No certificate pinning for API communications
- Vulnerable to MITM attacks on compromised networks
- No clear policy on cleartext traffic

**Impact:** Users

**Recommendation:**
1. Create `res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
</network-security-config>
```
2. Reference in AndroidManifest.xml:
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
```
3. Consider certificate pinning for Firebase endpoints if implementing sensitive features

**Reference:** `app/src/main/AndroidManifest.xml`

---

### 6. Incomplete ProGuard Configuration
**Severity:** HIGH
**File:** `app/proguard-rules.pro`

**Issue:**
ProGuard file contains only commented-out default configuration. No custom rules for:
- Room database entities
- Gson serialization
- Firebase models
- Kotlin coroutines

**Risk:**
- Release builds may crash due to over-aggressive obfuscation
- Reflection-based libraries may fail
- Difficult to debug production crashes without proper mappings
- Potential data corruption if serialization breaks

**Impact:** Users & Developers

**Recommendation:**
Add proper ProGuard rules:
```proguard
# Keep Room entities
-keep class com.waseefakhtar.doseapp.data.entity.** { *; }

# Keep Gson models
-keepclassmembers class com.waseefakhtar.doseapp.** {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Parcelize
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Preserve line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
```

**Reference:** `app/proguard-rules.pro:1-21`

---

## 🟡 Medium Priority Issues

### 7. Deep Link Not Registered in Manifest
**Severity:** MEDIUM
**Files:**
- `app/src/main/java/com/waseefakhtar/doseapp/MedicationNotificationReceiver.kt:36`
- `app/src/main/AndroidManifest.xml`

**Issue:**
Code creates deep links (`doseapp://medication/{id}`) but MainActivity doesn't declare an intent filter for this scheme in the manifest.

**Risk:**
- Deep links won't work from external sources
- Potential for malicious apps to intercept if scheme is registered elsewhere
- Inconsistent navigation behavior

**Impact:** Users (Functionality)

**Recommendation:**
Add intent filter to MainActivity if deep linking from external sources is intended:
```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="doseapp" android:host="medication" />
</intent-filter>
```
If deep links are only for internal navigation, this is acceptable as-is but should be documented.

**Reference:** `app/src/main/java/com/waseefakhtar/doseapp/MedicationNotificationReceiver.kt:36`

---

### 8. Unencrypted Local Database
**Severity:** MEDIUM
**File:** `app/src/main/java/com/waseefakhtar/doseapp/data/MedicationDatabase.kt`

**Issue:**
Room database stores medication data unencrypted on device storage. Medication data is considered personal health information (PHI).

**Risk:**
- Data accessible on rooted devices
- Data accessible via ADB backup (if backup rules not fixed)
- Data exposure if device is lost/stolen
- Privacy compliance concerns (HIPAA, GDPR)

**Impact:** Users (Privacy)

**Recommendation:**
1. Implement SQLCipher for database encryption:
```kotlin
val passphrase = // retrieve from Android Keystore
Room.databaseBuilder(context, MedicationDatabase::class.java, "medication_database")
    .openHelperFactory(SupportFactory(passphrase))
    .build()
```
2. Use Android Keystore System to securely store encryption key
3. Document encryption in privacy policy/README

**Reference:** `app/src/main/java/com/waseefakhtar/doseapp/data/MedicationDatabase.kt`

---

### 9. BroadcastReceiver Not Explicitly Protected
**Severity:** MEDIUM
**File:** `app/src/main/AndroidManifest.xml:31`

**Issue:**
MedicationNotificationReceiver is enabled but doesn't explicitly set `android:exported="false"`.

**Risk:**
- On Android 12+, implicit export could cause issues
- Potential for other apps to trigger medication notifications (though requires Parcelable data)

**Impact:** Users

**Recommendation:**
Explicitly set export status:
```xml
<receiver
    android:name=".MedicationNotificationReceiver"
    android:enabled="true"
    android:exported="false" />
```

**Reference:** `app/src/main/AndroidManifest.xml:31`

---

### 10. Missing Dependency Vulnerability Scanning
**Severity:** MEDIUM
**Files:** `.github/workflows/android.yml`, `build.gradle.kts`

**Issue:**
No automated dependency vulnerability scanning in CI/CD pipeline.

**Risk:**
- Using dependencies with known vulnerabilities
- No alerts for security updates
- Supply chain attack risks

**Impact:** Users & Developers

**Recommendation:**
1. Add Dependabot configuration (`.github/dependabot.yml`):
```yaml
version: 2
updates:
  - package-ecosystem: "gradle"
    directory: "/"
    schedule:
      interval: "weekly"
```
2. Consider adding OWASP Dependency Check to Gradle build
3. Enable GitHub Security Advisories

**Reference:** `.github/workflows/android.yml`

---

## ✅ Security Best Practices Followed

### 1. Modern Android Architecture
**Files:** Throughout codebase

**Good Practice:**
- Follows MVVM architecture with clean separation of concerns
- Uses Hilt for dependency injection (reduces hardcoded dependencies)
- Jetpack Compose for UI (modern, type-safe UI framework)
- Repository pattern for data access

**Security Benefit:**
- Reduced complexity makes security reviews easier
- Dependency injection facilitates testing and mocking
- Type safety reduces runtime errors

---

### 2. SQL Injection Protection
**File:** `app/src/main/java/com/waseefakhtar/doseapp/data/MedicationDao.kt`

**Good Practice:**
All database queries use Room's parameterized queries:
```kotlin
@Query("SELECT * FROM medicationentity WHERE id = :id")
suspend fun getMedicationById(id: Long): MedicationEntity?
```

**Security Benefit:**
- Complete protection against SQL injection attacks
- Type-safe query parameters

**Reference:** `app/src/main/java/com/waseefakhtar/doseapp/data/MedicationDao.kt:24-44`

---

### 3. Code Obfuscation Enabled
**File:** `app/build.gradle.kts:34`

**Good Practice:**
R8/ProGuard enabled for release builds:
```kotlin
getByName("release") {
    isMinifyEnabled = true
    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
}
```

**Security Benefit:**
- Makes reverse engineering more difficult
- Reduces APK size
- Removes unused code

**Reference:** `app/build.gradle.kts:33-36`

---

### 4. Secure PendingIntent Flags
**File:** `app/src/main/java/com/waseefakhtar/doseapp/MedicationNotificationReceiver.kt:43`

**Good Practice:**
Uses `FLAG_IMMUTABLE` for PendingIntents (Android 12+ requirement):
```kotlin
PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
```

**Security Benefit:**
- Prevents malicious apps from modifying PendingIntent
- Complies with Android 12+ security requirements

**Reference:** `app/src/main/java/com/waseefakhtar/doseapp/MedicationNotificationReceiver.kt:39-44`

---

### 5. No Hardcoded Secrets (Except Firebase)
**Finding:** Comprehensive code scan

**Good Practice:**
No hardcoded API keys, passwords, or tokens found in source code (apart from the Firebase config file which should be environment-specific).

**Security Benefit:**
- Reduces risk of credential exposure
- Facilitates proper secrets management

---

### 6. Proper Dependency Management
**File:** `gradle/libs.versions.toml`

**Good Practice:**
- Uses Gradle version catalog for centralized dependency management
- Dependencies specify explicit versions
- Uses BOM (Bill of Materials) for Firebase and OkHttp

**Security Benefit:**
- Easier to audit and update dependencies
- Consistent versions across modules
- Reduced dependency confusion attacks

**Reference:** `gradle/libs.versions.toml`

---

### 7. Runtime Permission Handling
**Files:** `AndroidManifest.xml`, Dependencies include Accompanist Permissions

**Good Practice:**
- Declares required permissions (POST_NOTIFICATIONS, SCHEDULE_EXACT_ALARM)
- Uses runtime permission library for proper permission handling
- Targets API 35 (latest Android version)

**Security Benefit:**
- Follows Android permission model
- User consent for sensitive operations
- Minimal permission footprint

**Reference:** `app/src/main/AndroidManifest.xml:5-6`

---

### 8. Automated CI/CD Pipeline
**File:** `.github/workflows/android.yml`

**Good Practice:**
- Automated builds on PR and main branch
- Runs tests and lint checks
- Gradle caching for performance
- Build artifacts uploaded for review

**Security Benefit:**
- Consistent build environment
- Code quality gates
- Audit trail for builds

**Reference:** `.github/workflows/android.yml`

---

### 9. Code Quality Tools
**Files:** `build.gradle.kts`, CI workflow

**Good Practice:**
- ktlint for Kotlin code style enforcement
- HTML and plain text reporters for lint results

**Security Benefit:**
- Consistent code style aids security reviews
- Reduces bugs through code quality

**Reference:** `build.gradle.kts:17-25`

---

### 10. Open Source License
**File:** `LICENSE`

**Good Practice:**
MIT License clearly defines usage terms and disclaims liability.

**Security Benefit:**
- Clear legal framework for contributions
- Users understand warranty limitations
- Encourages security community participation

**Reference:** `LICENSE`

---

## 📊 Dependency Analysis

### Core Dependencies (libs.versions.toml)

| Dependency | Version | Status | Notes |
|------------|---------|--------|-------|
| Kotlin | 2.0.21 | ✅ Current | Latest stable |
| Android Gradle | 8.6.1 | ✅ Current | Recent version |
| Compose | 1.7.6 | ✅ Current | Modern UI framework |
| Room | 2.6.1 | ✅ Current | Parameterized queries |
| Hilt | 2.49 | ✅ Current | DI framework |
| Firebase BOM | 33.8.0 | ✅ Current | Latest BOM |
| OkHttp | 4.10.0 | ⚠️ Older | Consider updating to 4.12+ |
| Target SDK | 35 | ✅ Current | Android 15 |
| Min SDK | 21 | ⚠️ Old | Android 5.0 (2014) |

**Recommendation:**
- Update OkHttp to 4.12.0 for latest security fixes
- Consider raising minSdk to 24 (Android 7.0) to drop legacy code

---

## 🔒 Privacy & Compliance Considerations

### Data Collected
Based on code analysis, the app collects:
1. **Medication Information:** Name, dosage, schedule, dates
2. **Usage Analytics:** Firebase Analytics tracks user interactions
3. **Crash Reports:** Firebase Crashlytics

### Privacy Concerns
1. **Health Data:** Medication information is considered Personal Health Information (PHI)
2. **Unencrypted Storage:** Local database not encrypted
3. **Cloud Backups:** Unclear if data is backed up to Google
4. **Analytics:** Firebase Analytics without clear user consent mechanism
5. **Third-Party Sharing:** Data shared with Google (Firebase)

### Compliance Recommendations
1. Add privacy policy covering:
   - What data is collected
   - How data is used
   - Where data is stored
   - User rights (access, deletion)
2. Implement GDPR consent mechanism if applicable
3. Provide data export/deletion functionality
4. Document compliance approach for HIPAA if targeting US healthcare

---

## 🛡️ Recommendations Summary

### Immediate Actions (Critical)
1. ✅ Remove `google-services.json` from repository and git history
2. ✅ Rotate Firebase API keys
3. ✅ Remove `docs/release/app.apk` from repository
4. ✅ Add sensitive files to `.gitignore`
5. ✅ Configure Firebase App Check

### Short-term (1-2 weeks)
1. ✅ Configure proper backup rules or disable backups
2. ✅ Remove `printStackTrace()` and add proper error logging
3. ✅ Add network security configuration
4. ✅ Complete ProGuard rules for all libraries
5. ✅ Set `android:exported="false"` on BroadcastReceiver
6. ✅ Add Dependabot configuration

### Medium-term (1 month)
1. ✅ Implement database encryption with SQLCipher
2. ✅ Add deep link validation or document internal-only usage
3. ✅ Update OkHttp to latest version
4. ✅ Add dependency vulnerability scanning to CI
5. ✅ Create privacy policy and consent mechanism

### Long-term (Future releases)
1. ✅ Consider raising minimum SDK to 24+
2. ✅ Implement certificate pinning for sensitive communications
3. ✅ Add security testing to CI pipeline (SAST/DAST)
4. ✅ Implement encrypted backup mechanism
5. ✅ Add user authentication if app grows to multi-device sync

---

## 📝 Security Checklist for Developers

For developers forking or contributing to this repository:

- [ ] Set up own Firebase project and configure `google-services.json` locally
- [ ] Never commit signing keys or keystores
- [ ] Never commit APK/AAB files
- [ ] Test with ProGuard enabled before release
- [ ] Review permissions before adding new dependencies
- [ ] Validate all user inputs
- [ ] Use parameterized queries for all database operations
- [ ] Test on multiple Android versions (min SDK 21 to latest)
- [ ] Review Firebase Security Rules periodically
- [ ] Keep dependencies updated
- [ ] Document security decisions in code comments

---

## 🔍 Testing Recommendations

1. **Static Analysis:**
   - Run Android Lint: `./gradlew lint`
   - Add SpotBugs or Detekt for security-focused static analysis

2. **Dynamic Analysis:**
   - Test with StrictMode enabled
   - Use Frida or objection for runtime analysis
   - Test ADB backup/restore scenarios

3. **Penetration Testing:**
   - Test with rooted device
   - Attempt SQL injection on all inputs
   - Test deep link handling with malformed URIs
   - Attempt to trigger receiver from external app

4. **Privacy Testing:**
   - Monitor network traffic with mitmproxy
   - Verify no sensitive data in logs
   - Check database contents on device

---

## 📚 References

- [OWASP Mobile Security Testing Guide](https://mobile-security.gitbook.io/mobile-security-testing-guide/)
- [Android Security Best Practices](https://developer.android.com/topic/security/best-practices)
- [Firebase Security Rules](https://firebase.google.com/docs/rules)
- [Android Data Storage Security](https://developer.android.com/topic/security/data)

---

## Conclusion

The Dose Android application demonstrates **good architectural practices** and follows many modern Android development standards. However, several **critical security issues require immediate attention**, particularly:

1. Exposed Firebase configuration
2. Committed binary artifacts
3. Insufficient data protection for health information

**For End Users:**
- Wait for security fixes before using with real medication data
- Be aware that medication data is stored unencrypted on device
- Understand that analytics data is shared with Google/Firebase

**For Developers:**
- Follow the immediate action items before deploying
- Set up proper Firebase Security Rules
- Implement the recommended security controls
- Consider this audit as a baseline, not a guarantee

**Risk Rating:** MODERATE → HIGH (due to health data sensitivity)

**Overall Grade:** C+ (Good foundation, critical gaps)

---

*This audit was conducted through static code analysis and does not include dynamic testing or penetration testing. A full security assessment would require runtime analysis, network traffic inspection, and testing on physical devices.*

**Auditor:** Claude (AI Security Analysis)
**Audit Scope:** Source code, configuration, build setup, dependencies
**Out of Scope:** Firebase backend, production infrastructure, third-party services

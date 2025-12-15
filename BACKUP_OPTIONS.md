# Backup Configuration Options

This document explains how to handle medication data backups securely while allowing data transfer to new devices.

## Current Issue

- `android:allowBackup="true"` backs up everything to Google Drive
- Medication database contains sensitive health information
- No control over what's backed up or where it goes

## Three Solutions

---

## Option 1: Manual Export/Import ⭐ RECOMMENDED

**Best for:** Personal use where you control all devices

### How It Works

1. Add "Export Data" feature in app settings
2. Exports medication database to encrypted JSON file
3. Save to location of your choice (local storage, your cloud service, etc.)
4. On new phone: Install app → Import file

### Benefits

✅ **Full Control**: You decide when and where to backup
✅ **Privacy**: No automatic uploads to Google
✅ **Encryption**: Can encrypt the export file with a password
✅ **Portability**: Works across any devices, even different Google accounts
✅ **Selective**: Export only what you need

### Implementation

I can add this feature with:
- Settings → Export Data (saves JSON to Downloads)
- Settings → Import Data (restore from file)
- Optional: Password encryption for the export file

**Time to implement:** ~30 minutes

---

## Option 2: Selective Auto Backup

**Best for:** If you want some automatic backups but not medication data

### Configuration

Exclude the database from auto-backup, use manual export for medications:

```xml
<!-- app/src/main/res/xml/backup_rules.xml -->
<full-backup-content>
    <!-- Backup app preferences -->
    <include domain="sharedpref" path="."/>

    <!-- Exclude sensitive medication database -->
    <exclude domain="database" path="medication_db"/>
    <exclude domain="database" path="medication_db-shm"/>
    <exclude domain="database" path="medication_db-wal"/>
</full-backup-content>
```

### What Gets Backed Up

✅ App settings and preferences
✅ UI state
❌ Medication data (use manual export)

### Benefits

✅ Automatic backup of app settings
✅ Medication data protected from auto-backup
✅ Still need manual export for medications

### Drawbacks

⚠️ Two-step process on new phone (restore settings + import medications)
⚠️ App settings still go to Google Drive

---

## Option 3: Allow Full Auto Backup (Least Secure)

**Best for:** Maximum convenience, minimal privacy concerns for personal use

### Configuration

```xml
<!-- app/src/main/res/xml/backup_rules.xml -->
<full-backup-content>
    <!-- Backup everything including medication database -->
    <include domain="database" path="."/>
    <include domain="sharedpref" path="."/>
</full-backup-content>
```

### How It Works

- Everything backed up to Google Drive automatically
- When you sign in on new phone, data restores automatically
- No manual intervention needed

### Benefits

✅ Zero effort - completely automatic
✅ Data transfers seamlessly to new devices
✅ Can't forget to backup

### Privacy Trade-offs

⚠️ Medication data stored on Google servers
⚠️ Subject to Google's privacy policy
⚠️ Not end-to-end encrypted (Google can access)
⚠️ Backed up whenever Google decides
⚠️ Restored when signing into ANY device (could be unexpected)

### When This Makes Sense

- You already use Google services extensively
- You trust Google with your health data
- Convenience is more important than privacy
- All your devices use the same Google account

---

## Comparison Table

| Feature | Manual Export | Selective Backup | Full Auto Backup |
|---------|--------------|------------------|------------------|
| **Privacy** | ⭐⭐⭐⭐⭐ Excellent | ⭐⭐⭐⭐ Good | ⭐⭐ Fair |
| **Convenience** | ⭐⭐ Manual | ⭐⭐⭐ Semi-auto | ⭐⭐⭐⭐⭐ Automatic |
| **Control** | ⭐⭐⭐⭐⭐ Full | ⭐⭐⭐⭐ High | ⭐⭐ Limited |
| **Security** | ⭐⭐⭐⭐⭐ Can encrypt | ⭐⭐⭐⭐ Protected | ⭐⭐⭐ Google-dependent |
| **Setup Time** | 5 min | 2 min | 1 min |
| **Portability** | ⭐⭐⭐⭐⭐ Any device | ⭐⭐⭐⭐ Google account | ⭐⭐⭐⭐ Google account |

---

## My Recommendation

**For your personal fork:** Use **Option 1 (Manual Export/Import)**

### Why?

1. **You've already prioritized privacy** by removing Firebase
2. **You control all devices** - manual export is easy
3. **Health data is sensitive** - medications reveal health conditions
4. **Export gives flexibility** - save to your preferred location
5. **Can encrypt if desired** - additional security layer
6. **Works offline** - no cloud dependency

### Getting a New Phone Process

**With Manual Export:**
```
Old Phone:
1. Open app → Settings → Export Data
2. Save file to preferred location (email to self, USB transfer, etc.)

New Phone:
1. Install app (sideload your signed APK)
2. Open app → Settings → Import Data
3. Select exported file
4. Done!
```

**Time: ~2 minutes**

---

## Option 1 Implementation Preview

I can add this feature with the following:

### Files to Create/Modify:

1. **Export/Import Repository** (`MedicationBackupRepository.kt`)
   - Export all medications to JSON
   - Import and validate JSON file
   - Optional encryption with user password

2. **Settings Screen** (add to existing settings)
   - "Export Data" button
   - "Import Data" button
   - Shows export location

3. **Export Format:**
```json
{
  "version": "1.0",
  "exportDate": "2025-12-15T10:30:00Z",
  "medications": [
    {
      "name": "Aspirin",
      "dosage": 100,
      "recurrence": "daily",
      "medicationTime": "2025-12-15T08:00:00Z",
      "endDate": "2025-12-30T00:00:00Z"
    }
  ]
}
```

### User Experience:

1. Tap "Export Data"
2. Choose location (Downloads folder by default)
3. File saved: `dose_backup_2025-12-15.json`
4. Confirmation: "Data exported successfully"

---

## Alternative: Cloud Sync (Future Enhancement)

If you want automatic backups WITHOUT Google, you could later add:

- **Syncthing**: Open-source file sync
- **Your own server**: WebDAV, FTP, etc.
- **Encrypted cloud**: ProtonDrive, Tresorit, etc.

But for getting started, manual export is simplest and most secure.

---

## Quick Decision Guide

**Choose Manual Export if:**
- ✅ You switch phones rarely (once a year or less)
- ✅ Privacy is important to you
- ✅ You want control over backup location
- ✅ You can remember to export before switching phones

**Choose Selective Backup if:**
- ✅ You want some automatic backups
- ✅ You're okay with manual medication export
- ✅ You use Google account on all devices

**Choose Full Auto Backup if:**
- ✅ Convenience is paramount
- ✅ You trust Google with health data
- ✅ You might forget to backup manually
- ✅ You switch phones frequently

---

## Next Steps

Let me know which option you prefer, and I can:

1. **Option 1**: Implement export/import feature (~30 min)
2. **Option 2**: Configure selective backup rules (~2 min)
3. **Option 3**: Configure full auto backup (~1 min)

Or mix approaches - for example, Option 2 + manual export for medications.

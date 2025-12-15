# Dependabot Dependency Updates Guide

This repository uses Dependabot to automatically check for dependency updates and security vulnerabilities.

## What Dependabot Does

- **Scans monthly** for updates to Gradle dependencies and GitHub Actions
- **Creates Pull Requests** when updates are available
- **Groups related updates** (e.g., all AndroidX libraries together) to reduce PR noise
- **Labels PRs** with "dependencies" and "automated" for easy filtering

## How It Works

### When Updates Are Available

1. **First of each month**: Dependabot scans dependencies
2. **PRs created**: Up to 5 PRs for Gradle deps, 3 for Actions
3. **You review**: Check the PR description for what changed
4. **You decide**: Merge, close, or ignore

### PR Grouping

Updates are grouped to reduce noise:

- **`androidx` group**: All AndroidX library updates together
- **`compose` group**: All Compose library updates together
- **`kotlin` group**: All Kotlin-related updates together
- **`testing` group**: All testing library updates together

This means you might see one PR like:
> "Bump androidx group from 1.0.0 to 1.1.0"

Instead of 10 separate PRs for each AndroidX library.

## What To Do With Dependabot PRs

### Security Updates (High Priority) 🔴

**Indicators:**
- PR title includes "security" or mentions CVE
- Description mentions vulnerability
- GitHub Security Advisory linked

**Action:**
1. Review the security advisory
2. Test the update locally if possible
3. Merge promptly

**Example:**
```bash
# Locally test a security update
git fetch origin
git checkout dependabot/gradle/androidx.core-1.12.0
./gradlew assembleRelease
# Test app functionality
# If good, merge the PR
```

### Regular Updates (Review When Convenient) 🟡

**Types:**
- Minor version bumps (1.0.0 → 1.1.0)
- Patch updates (1.0.0 → 1.0.1)
- New features

**Action:**
1. Review changelog (linked in PR description)
2. Merge if you want new features
3. Close/ignore if not needed

**You don't need to merge every update!** Only merge if:
- Security vulnerability fixed
- Bug fixes you need
- New features you want
- Keeping dependencies reasonably current (once or twice a year)

### Major Updates (Careful Review) 🟠

**Indicators:**
- Major version change (1.x.x → 2.x.x)
- PR description mentions "breaking changes"

**Action:**
1. **Don't auto-merge** - breaking changes likely
2. Review migration guide (linked in PR)
3. Test thoroughly before merging
4. May require code changes

## Managing PR Noise

### If Too Many PRs:

**Option 1: Close Ones You Don't Need**
- Click "Close pull request" with comment: "Not needed right now"
- Dependabot won't recreate until next version

**Option 2: Reduce Frequency** (Edit `.github/dependabot.yml`)
```yaml
schedule:
  interval: "monthly"  # Change to "quarterly" for less noise
```

**Option 3: Pause Dependabot Temporarily**
- Go to repository Settings → Code security and analysis
- Pause Dependabot version updates
- Resume when convenient

### If Not Enough Activity:

Dependabot only creates PRs when updates exist. If you see no PRs for months, it means:
- ✅ Your dependencies are current
- ✅ No security vulnerabilities detected
- ✅ Everything is fine!

## Reviewing Grouped PRs

When Dependabot groups updates, check each change:

**Example PR:**
> Bump androidx group from 1.0.0 to 1.1.0

**Click "Files changed" to see:**
```diff
[versions]
-androidx-core = "1.10.0"
-androidx-lifecycle = "2.5.0"
+androidx-core = "1.12.0"
+androidx-lifecycle = "2.6.0"
```

Review each library's changelog before merging.

## Testing Updates

### Quick Test (Recommended):
```bash
# Let CI handle it - if build passes, likely safe to merge
# Check the GitHub Actions build status on the PR
```

### Thorough Test (For Major Updates):
```bash
# Check out the branch
git fetch origin
git checkout <dependabot-branch-name>

# Build and test
./gradlew clean
./gradlew assembleRelease
./gradlew test

# Install and test on device
adb install app/build/outputs/apk/release/app-release.apk
# Test app functionality
```

## Security Alerts

Dependabot also creates **Security Advisories** when vulnerabilities are found.

**Where to find them:**
- Repository → Security tab → Dependabot alerts
- Email notifications (if enabled)

**What to do:**
1. Review the advisory
2. Check if Dependabot created a PR
3. If yes: Review and merge
4. If no: Update manually or wait for next scan

## Configuration Details

**Current settings:**
- **Scan frequency**: Monthly (first of each month, 9am UTC)
- **Max open PRs**: 5 for Gradle, 3 for GitHub Actions
- **Grouping**: Yes (reduces noise)
- **Auto-rebase**: Yes (keeps PRs up-to-date)
- **Labels**: `dependencies`, `automated`

**To modify:**
Edit `.github/dependabot.yml` and commit changes.

## Common Questions

### Q: Should I merge every Dependabot PR?
**A:** No! Only merge:
- Security updates (always)
- Bug fixes you need
- Features you want
- Periodic updates to stay reasonably current

### Q: How do I know if an update is safe?
**A:** Check:
1. CI build status (must pass)
2. Changelog for breaking changes
3. Version type (patch/minor = usually safe, major = review carefully)

### Q: What if a dependency update breaks my app?
**A:**
1. Revert the merge
2. Comment on the PR about the issue
3. Wait for a fix or pin the old version temporarily

### Q: Can I disable Dependabot?
**A:** Yes, but not recommended:
- Delete `.github/dependabot.yml` to disable completely
- Or pause in repository settings
- Security updates are valuable even if you skip feature updates

## For This Project Specifically

**Your dependencies are low-risk:**
- ✅ All from trusted sources (Google, JetBrains)
- ✅ Well-maintained with regular updates
- ✅ No sketchy third-party libraries
- ✅ App is offline (no network exposure)

**Recommended approach:**
1. **Security updates**: Merge quickly
2. **Regular updates**: Review quarterly, merge what makes sense
3. **Major updates**: Test thoroughly before merging
4. **Don't stress**: It's okay to let PRs sit for a while

## More Info

- [Dependabot documentation](https://docs.github.com/en/code-security/dependabot)
- [Configuration options](https://docs.github.com/en/code-security/dependabot/dependabot-version-updates/configuration-options-for-the-dependabot.yml-file)

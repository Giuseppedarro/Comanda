# Comanda Android – CI/CD Setup Guide

This document describes how to set up the automated build and release pipeline for the Comanda Android app.

---

## Overview

| Workflow | File | Trigger | Purpose |
|---|---|---|---|
| Android CI | `.github/workflows/android-ci.yml` | PR / push to `main` | Run unit tests and verify the app builds |
| Android Release | `.github/workflows/android-release.yml` | Git tag `v*.*.*` | Build signed AAB, upload to Play Store, create GitHub Release |

---

## 1. Create a Release Keystore

Run this command once on your local machine and keep the `.jks` file safe (never commit it to git):

```bash
keytool -genkey -v \
  -keystore comanda-release.jks \
  -alias comanda-key \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

You will be prompted for:
- A **keystore password** (remember it – you'll need it as a secret)
- A **key password** (can be the same as the keystore password)
- Distinguished-name fields (name, organisation, country, etc.)

---

## 2. Convert the Keystore to Base64

The keystore file must be stored as a base64-encoded string in GitHub Secrets.

```bash
# macOS / Linux
base64 -i comanda-release.jks | tr -d '\n'

# Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("comanda-release.jks"))
```

Copy the entire output – you'll paste it as the `KEYSTORE_FILE` secret.

---

## 3. Add GitHub Secrets

Go to your repository on GitHub → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**.

Add the following five secrets:

| Secret name | Value |
|---|---|
| `SERVICE_ACCOUNT_JSON` | Full contents of the Google Cloud service-account JSON file |
| `KEYSTORE_FILE` | Base64-encoded keystore (output of Step 2) |
| `KEYSTORE_PASSWORD` | Password you chose for the keystore |
| `KEY_ALIAS` | Key alias used when creating the keystore (e.g. `comanda-key`) |
| `KEY_PASSWORD` | Password for the individual key (often same as `KEYSTORE_PASSWORD`) |

---

## 4. First Upload to Google Play (Manual – Required Once)

Google Play requires at least **one manually uploaded release** before the API can manage subsequent ones.

1. Build a signed AAB locally (Android Studio → **Build** → **Generate Signed Bundle / APK**).
2. Go to **Google Play Console** → your app → **Testing** → **Internal testing**.
3. Click **Create new release** and upload the `.aab` file manually.
4. Save and roll out the release.

After this one-time step the CD workflow can handle all future uploads automatically.

---

## 5. Performing a Release

Once GitHub Secrets are configured and the first manual upload is done:

```bash
# Make sure you are on the main branch and it is up to date
git checkout main
git pull

# Create and push a semantic version tag
git tag v1.0.0
git push origin v1.0.0
```

The `Android Release` workflow will automatically:
1. Build a signed AAB and APK for the `production` flavour
2. Upload the AAB to the **internal testing** track on Google Play
3. Create a GitHub Release with the AAB and APK as attachments

---

## 6. Build Flavors

| Flavor | Application ID | Purpose |
|---|---|---|
| `staging` | `dev.giuseppedarro.comanda.staging` | Development / testing builds |
| `production` | `dev.giuseppedarro.comanda` | Production releases |

---

## 7. Troubleshooting

### "Unauthorized" error when uploading to Play Store
- Confirm that the service account was invited in **Play Console** → **Users and permissions** with *Release Manager* (or equivalent) permissions for the Comanda app.
- Verify that `SERVICE_ACCOUNT_JSON` contains valid JSON (no extra whitespace or line breaks).

### Build fails with "keystore not found"
- Check that `KEYSTORE_FILE` is a valid base64 string with no newlines.
- Verify `KEYSTORE_PASSWORD`, `KEY_ALIAS`, and `KEY_PASSWORD` match the values used when creating the keystore.

### Version code / name is wrong
- The version name comes from the most recent git tag matching `v*.*.*`.
- The version code is the total number of commits (`git rev-list --count HEAD`).
- Make sure the tag exists and the checkout step uses `fetch-depth: 0`.

### First API upload fails with "APK specifies a version code that has already been used"
- Complete the one-time manual upload described in Step 4 first, then tag a **newer** version (e.g. `v1.0.1`) for the automated workflow.

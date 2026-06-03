# API Keys Setup

Karunada-Kala uses two external services: **Google Maps** (map tab) and **Firebase** (optional sync + workshop writes).

## Google Maps SDK for Android

### 1. Google Cloud project

1. Open [Google Cloud Console](https://console.cloud.google.com/).
2. Create or select a project linked to your Firebase project (recommended).

### 2. Enable Maps SDK

1. Go to **APIs & Services → Library**.
2. Search for **Maps SDK for Android**.
3. Click **Enable**.

### 3. Create an API key

1. **APIs & Services → Credentials → Create credentials → API key**.
2. Restrict the key (recommended):
   - **Application restrictions:** Android apps
   - **Package name:** `com.karunadakala.source`
   - **SHA-1:** from your debug keystore (`./gradlew signingReport` in Android Studio)
   - **API restrictions:** Maps SDK for Android

### 4. Add the key to the project

Create `local.properties` in the **project root** (copy from `local.properties.example`):

```properties
sdk.dir=C\:\\Users\\YOUR_USER\\AppData\\Local\\Android\\Sdk
MAPS_API_KEY=AIza...your_key_here
```

The build reads `MAPS_API_KEY` and injects it into `AndroidManifest.xml` as:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="${MAPS_API_KEY}" />
```

### 5. Test the map

1. Sync Gradle.
2. Run the app → **Map** tab.
3. Grant location permission when prompted.
4. Yellow pins = workshops, red pins = performances.

If the map is blank, verify the key, package name, SHA-1 restriction, and that Maps SDK is enabled.

## Firebase (summary)

See [FIREBASE_SETUP.md](FIREBASE_SETUP.md) for `google-services.json` and Firestore.

## Security reminders

- Never commit `local.properties` or real API keys to GitHub.
- Use placeholder `google-services.json` in the repo; replace locally with your download.
- Rotate keys if they were exposed publicly.

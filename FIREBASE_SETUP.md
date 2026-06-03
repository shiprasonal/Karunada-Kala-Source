# Firebase Setup

Follow these steps to connect Karunada-Kala to your Firebase project. The app works **offline without Firebase** using bundled sample data.

## 1. Create a Firebase project

1. Open [Firebase Console](https://console.firebase.google.com/).
2. Click **Add project** and name it (e.g. `karunada-kala`).
3. Disable Google Analytics if you do not need it (optional for student projects).

## 2. Register the Android app

1. In the project overview, click **Add app** → **Android**.
2. **Android package name:** `com.karunadakala.source` (must match `applicationId` in `app/build.gradle.kts`).
3. Download **`google-services.json`**.
4. Place it at:

   ```text
   app/google-services.json
   ```

   Replace the placeholder file in the repository.

## 3. Enable Authentication

1. Go to **Build → Authentication → Sign-in method**.
2. Enable **Anonymous**.
3. The app signs in anonymously before writing workshop registrations so Firestore rules can require `request.auth != null`.

## 4. Enable Cloud Firestore

1. Go to **Build → Firestore Database**.
2. Click **Create database**.
3. Start in **test mode** for development, then tighten rules before production.

### Suggested security rules (starter)

```text
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /artForms/{docId} {
      allow read: if true;
      allow write: if false;
    }
    match /artisans/{docId} {
      allow read: if true;
      allow write: if false;
    }
    match /events/{docId} {
      allow read: if true;
      allow write: if false;
    }
    match /workshops/{docId} {
      allow create: if request.auth != null
        && request.resource.data.keys().hasAll([
          'userName','email','phone','artForm','date','experienceLevel'
        ]);
      allow read, update, delete: if false;
    }
  }
}
```

## 5. Optional: seed Firestore data

You can import documents that match the fields in `repository/FirestoreMappers.kt`. If collections are empty, **SampleData** still powers the UI.

### Example `artisans` document

| Field | Example |
|-------|---------|
| `name` | Lakshmi Devi |
| `artForm` | Mysore Painting |
| `district` | Mysuru |
| `phone` | +919844000001 |
| `latitude` | 12.2958 |
| `longitude` | 76.6394 |
| `markerKind` | `WORKSHOP` or `PERFORMANCE` |

## 6. Verify in Android Studio

1. **File → Sync Project with Gradle Files**
2. Run the app on an emulator.
3. Submit a workshop form; check **Firestore → workshops** for a new document.

## Troubleshooting

| Issue | Fix |
|-------|-----|
| `google-services.json` missing | Download from Firebase and place under `app/`. |
| Workshop submit fails | Enable Anonymous Auth; check rules and internet. |
| No remote data shown | Empty collections are OK; sample data should still appear. |

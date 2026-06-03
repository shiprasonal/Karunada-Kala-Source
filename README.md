# Karunada-Kala

**Karunada-Kala** is an Android application that helps users discover Karnataka’s traditional art forms, cultural events, artisan workshops, and heritage performances. Built for academic evaluation with clean MVVM architecture, offline-friendly sample data, and optional Firebase + Google Maps integration.

---

## Problem Statement

Karnataka’s rich cultural heritage—Yakshagana, Bidriware, Channapatna toys, Ilkal weaving, and many more—is often scattered across districts and known only locally. Students, tourists, and enthusiasts lack a single mobile guide to explore art forms, find artisans, register for workshops, and locate events on a map.

---

## Project Overview

Karunada-Kala provides a student-friendly cultural explorer:

- Browse featured art forms and read detailed histories
- Search and filter the art catalog
- View upcoming events by district
- Explore artisans on an interactive Karnataka map
- Register for workshops (saved to Firestore when configured)
- Works fully offline using realistic Karnataka sample data

**Architecture:** MVVM with Jetpack Compose, Navigation Compose, Kotlin Coroutines, and Flow.

---

## Features

| Feature | Description |
|---------|-------------|
| Splash & onboarding | First-launch intro; skip or complete onboarding |
| Home dashboard | Banner carousel, categories, events & workshop previews |
| Art explorer | Search art forms by name, region, or district |
| Art detail | History, region, video link, workshop registration CTA |
| Events tab | District filter chips + event cards |
| Artisan map | Google Maps with workshop (yellow) & performance (red) markers |
| Artisan profile | Bio, products, call artisan, register workshop |
| Workshop form | Validates input; anonymous Firebase auth + Firestore write |
| Profile tab | App info, workshop catalog, quick actions |
| Offline-first | `SampleData` loads instantly; Firestore merges when available |

---

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM |
| Navigation | Navigation Compose |
| Async | Coroutines, StateFlow |
| Images | Coil |
| Backend (optional) | Firebase Auth (Anonymous), Cloud Firestore |
| Maps | Google Maps SDK, Maps Compose |
| Preferences | DataStore |

---

## Architecture (MVVM)

```text
UI (Compose Screens)
        ↓ observes StateFlow
ViewModel
        ↓ calls repository APIs
Repository  →  SampleData (offline) + Firestore (online merge)
        ↓
Model (data classes)
```

See [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) for the full folder breakdown.

---

## Folder Structure

```text
app/src/main/java/com/karunadakala/source/
├── model/                 # ArtForm, Artisan, CulturalEvent, WorkshopProgram
├── repository/            # Repositories, SampleData, Firestore mappers
├── viewmodel/             # Home, Explore, Map, Events, Workshop, …
├── ui/
│   ├── screens/           # splash, onboarding, home, explore, map, events, …
│   ├── components/        # Cards, carousel, top bar
│   └── theme/             # Karnataka-inspired colors
├── navigation/          # Routes + NavHost
└── utils/                 # ViewModel factory, map markers, dial intents
```

---

## Firebase Setup

1. Create a Firebase project and add Android app `com.karunadakala.source`
2. Download `google-services.json` → place in `app/`
3. Enable **Anonymous Authentication**
4. Enable **Cloud Firestore**

Detailed steps: [FIREBASE_SETUP.md](FIREBASE_SETUP.md)

---

## Google Maps API Setup

1. Enable **Maps SDK for Android** in Google Cloud Console
2. Create an API key (restrict to package `com.karunadakala.source`)
3. Add to `local.properties`:

```properties
MAPS_API_KEY=your_android_maps_key
```

Full guide: [API_KEYS_SETUP.md](API_KEYS_SETUP.md)

---

## Installation Steps

### Prerequisites

- Android Studio Ladybug (2024.2+) or newer
- JDK 17
- Android SDK 35
- Emulator or physical device (API 26+)

### Clone and configure

```bash
git clone https://github.com/YOUR_USERNAME/karunada-kala.git
cd karunada-kala
```

1. Copy `local.properties.example` → `local.properties` and set `sdk.dir` + `MAPS_API_KEY`
2. Replace `app/google-services.json` with your Firebase file (optional for offline demo)
3. Open the project in Android Studio
4. **File → Sync Project with Gradle Files**

---

## Run Instructions

1. Select a device/emulator in Android Studio
2. Click **Run** (▶) or press `Shift+F10`
3. Grant **location** permission on the Map tab for “My Location”
4. Flow: **Splash → Onboarding → Home** (bottom navigation for all tabs)

Command line (after Gradle wrapper is present):

```bash
./gradlew :app:assembleDebug
```

---

## Screenshots

Add captures to the `screenshots/` folder before submission:

| Preview | Path |
|---------|------|
| Splash | `screenshots/01_splash.png` |
| Home | `screenshots/02_home.png` |
| Explore | `screenshots/03_explore.png` |
| Map | `screenshots/04_map.png` |
| Events | `screenshots/05_events.png` |
| Workshop form | `screenshots/06_workshop_form.png` |

*(Replace placeholder paths with your actual images.)*

---

## Future Enhancements

- User accounts and saved favorites
- Push notifications for nearby events
- Kannada / English localization
- In-app ticket booking for festivals
- AR previews of art forms
- Admin web panel for artisan onboarding

---

## Contributors

| Name | Role |
|------|------|
| Your Name | Android development, UI, Firebase integration |
| Team Member (optional) | Documentation, testing |

*Update this table with your team details before submission.*

---

## Manual Steps Before GitHub Submission

These cannot be fully automated:

1. **Create a public GitHub repository** and push with **5–10 meaningful commits** (not a single commit)
2. **Add screenshots** to `screenshots/`
3. **Configure Firebase** with your real `google-services.json`
4. **Add Maps API key** in `local.properties`
5. **Rebuild and test** all navigation paths
6. Ensure the repo is **public**, **cloneable**, and builds without red errors

Example commit messages:

```text
Added splash and onboarding screens
Implemented home screen UI
Integrated Firebase Firestore
Added Google Maps functionality
Updated README documentation
```

---

## Final Repository Checklist

Use this before submitting for evaluation:

| Item | Status |
|------|--------|
| Project builds (`assembleDebug`) | ☐ |
| No unresolved imports / red code | ☐ |
| All bottom-nav screens connected | ☐ |
| Offline sample data works without Firebase | ☐ |
| Firebase: `google-services.json` + Anonymous Auth | ☐ |
| Firestore rules allow workshop `create` | ☐ |
| Maps: `MAPS_API_KEY` in `local.properties` | ☐ |
| Screenshots added under `screenshots/` | ☐ |
| README + docs updated | ☐ |
| Public GitHub repo with multiple commits | ☐ |

---

## License

Academic / educational use. Update license text if your institution requires a specific notice.

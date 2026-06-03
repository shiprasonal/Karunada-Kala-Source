# Project Structure

Karunada-Kala follows **MVVM** (Model–View–ViewModel) with Jetpack Compose UI.

## Package layout

```text
app/src/main/java/com/karunadakala/source/
├── model/                 # Data classes (ArtForm, Artisan, CulturalEvent, …)
├── repository/            # Data access + SampleData + Firebase mappers
├── viewmodel/             # UI state and business logic
├── ui/
│   ├── screens/           # Full-screen composables per feature
│   ├── components/        # Reusable UI pieces (cards, carousel, top bar)
│   └── theme/             # Colors, typography, Material 3 theme
├── navigation/            # Routes + NavHost graph
├── utils/                 # Helpers (ViewModel factory, map markers, intents)
├── AppContainer.kt        # Repository wiring (dependency container)
├── KarunadaApplication.kt # Firestore offline persistence
├── MainActivity.kt        # Single activity entry point
└── KarunadaApp.kt         # Compose root + theme
```

## Layer responsibilities

| Layer | Role |
|-------|------|
| **Model** | Plain Kotlin data classes; no Android dependencies. |
| **Repository** | Exposes `Flow`/`suspend` APIs; merges `SampleData` with Firestore when online. |
| **ViewModel** | Survives configuration changes; exposes `StateFlow` to UI. |
| **UI** | Compose screens/components; observes ViewModels, triggers navigation callbacks. |

## Screen map

| Screen | Package | ViewModel |
|--------|---------|-----------|
| Splash | `ui.screens.splash` | — (uses preferences repo) |
| Onboarding | `ui.screens.onboarding` | — |
| Home | `ui.screens.home` | `HomeViewModel` |
| Explore | `ui.screens.explore` | `ExploreViewModel` |
| Map | `ui.screens.map` | `ArtisanMapViewModel` |
| Events | `ui.screens.events` | `EventsViewModel` |
| Profile | `ui.screens.profile` | — (uses `SampleData` workshops) |
| Art detail | `ui.screens.detail` | `ArtDetailViewModel` |
| Artisan profile | `ui.screens.artisan` | `ArtisanProfileViewModel` |
| Workshop form | `ui.screens.workshop` | `WorkshopRegistrationViewModel` |

## Resources

```text
app/src/main/res/
├── values/          # strings, colors, themes
├── mipmap-*/        # launcher icons
└── xml/             # backup rules
```

## Firebase collections (optional)

| Collection | Purpose |
|------------|---------|
| `artForms` | Catalog override / sync |
| `artisans` | Map markers + profiles |
| `events` | Event listings |
| `workshops` | User registration submissions |

When collections are empty, the app still runs using `repository/SampleData.kt`.

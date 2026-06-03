package com.karunadakala.source.navigation

object Routes {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Main = "main"

    const val Home = "home"
    const val Explore = "explore"
    const val Map = "map"
    const val Events = "events"
    const val Profile = "profile"

    const val ArtDetailPattern = "art_detail/{id}"
    fun artDetail(id: String): String = "art_detail/$id"

    const val ArtisanDetailPattern = "artisan_detail/{id}"
    fun artisanDetail(id: String): String = "artisan_detail/$id"

    const val WorkshopRegistrationPattern = "workshop_registration/{prefillArtFormId}"
    fun workshopRegistration(prefillArtFormId: String): String = "workshop_registration/$prefillArtFormId"
}

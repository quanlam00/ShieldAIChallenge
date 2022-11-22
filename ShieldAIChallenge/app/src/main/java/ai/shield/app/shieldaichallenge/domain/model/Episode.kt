package ai.shield.app.shieldaichallenge.domain.model

/**
 * Domain Model of a TV Show's Episode's Guide
 */
data class Episode(
    val _links: Links,
    val airdate: String,
    val airstamp: String,
    val airtime: String,
    val id: Int,
    val image: Image,
    val name: String,
    val number: Int,
    val runtime: Int,
    val season: Int,
    val summary: String,
    val url: String
)
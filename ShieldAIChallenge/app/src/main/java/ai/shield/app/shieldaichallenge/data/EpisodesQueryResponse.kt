package ai.shield.app.shieldaichallenge.data

import ai.shield.app.shieldaichallenge.domain.model.Episode

data class EpisodesQueryResponse(
    val episodes: List<Episode>
)
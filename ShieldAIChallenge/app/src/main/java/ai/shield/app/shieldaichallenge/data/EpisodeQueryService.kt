package ai.shield.app.shieldaichallenge.data

import ai.shield.app.shieldaichallenge.domain.model.Episode

/**
 * This service will provide its consumer a list functions that related to some TV Show's episodes
 */
abstract class EpisodeQueryService {
    /**
     * Get a list of GOT episodes and their details
     *
     * @throws: Exception when there is an error loading data
     */
    @Throws(Exception::class)
    abstract fun queryGOTEpisodes(): List<Episode>
}
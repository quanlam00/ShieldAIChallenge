package ai.shield.app.shieldaichallenge.data.ci

import ai.shield.app.shieldaichallenge.data.EpisodeQueryService
import ai.shield.app.shieldaichallenge.data.impl.TextBasedEpisodeQueryServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * A Hilt Module to specify how to bind the app's Episode Services
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class EpisodesModule {
    @Binds
    abstract fun bindsEpisodeQueryService(
        episodeQueryServiceImpl: TextBasedEpisodeQueryServiceImpl
    ): EpisodeQueryService
}
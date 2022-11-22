package ai.shield.app.shieldaichallenge.data.impl

import ai.shield.app.shieldaichallenge.R
import ai.shield.app.shieldaichallenge.data.EpisodeQueryService
import ai.shield.app.shieldaichallenge.data.EpisodesQueryResponse
import ai.shield.app.shieldaichallenge.domain.model.Episode
import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * A detail implementation of the EpisodeQueryService that will get the data from a
 * text file in Resource
 */
internal class TextBasedEpisodeQueryServiceImpl @Inject constructor(
    @ApplicationContext context: Context
): EpisodeQueryService() {
    private val contextReference: WeakReference<Context> = WeakReference(context)

    override fun queryGOTEpisodes(): List<Episode> {
        return Gson().fromJson(
            readFromFile(R.raw.game_of_thrones_episodes),
            EpisodesQueryResponse::class.java
        ).episodes
    }

    /**
     * Take a resource ID in Int, return its content as a String
     *
     * @param resourceFile Input's resource Id
     */
    private fun readFromFile(resourceFile: Int): String {
        return contextReference.get()?.let {
            val inputStream: InputStream = it.resources.openRawResource(
                resourceFile
            )
            val b = ByteArray(inputStream.available())
            inputStream.read(b)
            String(b)
        } ?: ""
    }
}
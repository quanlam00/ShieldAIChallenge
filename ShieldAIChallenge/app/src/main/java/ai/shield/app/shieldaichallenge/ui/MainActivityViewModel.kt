package ai.shield.app.shieldaichallenge.ui

import ai.shield.app.shieldaichallenge.data.EpisodeQueryService
import ai.shield.app.shieldaichallenge.domain.model.Episode
import ai.shield.app.shieldaichallenge.ui.uimodel.DetailContent
import ai.shield.app.shieldaichallenge.ui.uimodel.EpisodeListItem
import ai.shield.app.shieldaichallenge.ui.uimodel.mapper.toDetailContent
import ai.shield.app.shieldaichallenge.ui.uimodel.mapper.toEpisodeListItem
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val episodeQueryService: EpisodeQueryService,
) : ViewModel() {
    sealed class MainViewState {
        object Loading : MainViewState()
        class Loaded(
            val episodeList: List<EpisodeListItem>,
            val selectedPosition: Int
        ) : MainViewState()

        class Error(val exception: Exception) : MainViewState()
    }

    companion object {
        const val PREFERENCE_NAME = "SHIELD_PREFERENCE"
        const val EPISODE_SELECTED_KEY = "EPISODE_SELECTED"
        const val NOT_SELECTED = -1
    }

    private val mState: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState.Loading)
    val viewState get() = mState.asStateFlow()
    private val contextReference = WeakReference(context)

    private lateinit var episodeList: List<Episode>
    private var selectedPosition = NOT_SELECTED

    fun loadEpisodesData() {
        doInBackground {
            try {
                mState.update {
                    MainViewState.Loading
                }
                episodeList = episodeQueryService.queryGOTEpisodes()
                mState.update {
                    loadSelectedPosition()
                    MainViewState.Loaded(
                        episodeList.map {
                            it.toEpisodeListItem()
                        },
                        selectedPosition
                    )
                }
            } catch (e: Exception) {
                mState.update {
                    MainViewState.Error(e)
                }
            }
        }
    }

    //Save selected position to shared preference
    private fun saveSelectedPosition(position: Int) {
        contextReference.get()?.let {
            val editor = it.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            editor.putInt(EPISODE_SELECTED_KEY, position)
            editor.apply()
        }
    }

    private fun loadSelectedPosition() {
        contextReference.get()?.let {
            val pref = it.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            selectedPosition = pref.getInt(EPISODE_SELECTED_KEY, NOT_SELECTED)
        }
    }

    fun getDetailContent(position: Int): DetailContent {
        //First, persist the current selected position
        saveSelectedPosition(position)
        return episodeList[position].toDetailContent()
    }
}
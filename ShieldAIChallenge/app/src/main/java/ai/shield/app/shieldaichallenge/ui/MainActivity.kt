package ai.shield.app.shieldaichallenge.ui

import ai.shield.app.shieldaichallenge.R
import ai.shield.app.shieldaichallenge.databinding.ActivityMainBinding
import ai.shield.app.shieldaichallenge.ui.MainActivityViewModel.Companion.NOT_SELECTED
import ai.shield.app.shieldaichallenge.ui.uimodel.DetailContent
import ai.shield.app.shieldaichallenge.ui.uimodel.EpisodeListItem
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * Main activity of the app
 * Currently app only have this one activity
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        initViewStateObserver()
        viewModel.loadEpisodesData()

        setContentView(binding!!.root)
    }

    /**
     * Strategy/State design pattern.
     * View with react to change in viewModel::viewState
     * This is the only way to change the view's state.
     */
    private fun initViewStateObserver() {
        viewModel.viewState.onEach { state ->
            when (state) {
                is MainActivityViewModel.MainViewState.Error -> {
                    //Error! Make a toast!
                    Toast.makeText(
                        this,
                        "Error: ${state.exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is MainActivityViewModel.MainViewState.Loaded -> {
                    //Episode list successfully loaded
                    loadEpisodesList(state.episodeList, state.selectedPosition)
                }
                MainActivityViewModel.MainViewState.Loading -> {
                    //Loading. Make a toast!
                    Toast.makeText(
                        this,
                        "Loading...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    @SuppressLint("NotifyDataSetChanged")
    //Initiate the recycle view if have not done so. Refresh the recycle view if data changed.
    //Suppressing  NotifyDataSetChanged because we are only load all episode at once in the current
    //version.
    private fun loadEpisodesList(
        episodes: List<EpisodeListItem>,
        selectedPosition: Int
    ) {
        binding?.run {
            if (episodeList.adapter == null) {
                episodeList.layoutManager = LinearLayoutManager(this@MainActivity)
                episodeList.adapter = EpisodeListAdapter(
                    episodes,
                    {   p ->
                        //When item at position p is selected
                        loadDetailContent(viewModel.getDetailContent(p))
                    },
                    selectedPosition,
                    this@MainActivity)

                //If there is a last selected position from previous section,
                // wait a bit for the recycle view to finish initiating,
                // then scroll there
                if (selectedPosition != NOT_SELECTED) {
                    doInBackground {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            episodeList.scrollToPosition(selectedPosition)
                        }
                    }
                }
            } else {
                episodeList.adapter!!.notifyDataSetChanged()
            }
        }
    }

    //Load the detail summary of a selected episode to the Episode Detail section of the screen.
    private fun loadDetailContent(detailContent: DetailContent) {
        binding?.run {
            Picasso.get().load(
                detailContent.imageUrl,
            ).into(
                detailImage,
                object : Callback {
                    override fun onSuccess() {
                        Log.d("Main", "Success")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("Main", "Error ${e?.message} ${detailContent.imageUrl}")
                    }

                }
            )
            detailSummary.text =
                Html.fromHtml(
                    detailContent.detailSummary,
                    Html.FROM_HTML_MODE_COMPACT
                )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
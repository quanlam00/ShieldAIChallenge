package ai.shield.app.shieldaichallenge.ui

import ai.shield.app.shieldaichallenge.R
import ai.shield.app.shieldaichallenge.ui.MainActivityViewModel.Companion.NOT_SELECTED
import ai.shield.app.shieldaichallenge.ui.uimodel.EpisodeListItem
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference

/**
 * Adapter class for a list of EpisodeListItem
 * @param dataSet the list of EpisodeListItem
 * @param onItemSelected a callback to be trigger when an item is selected, on top of
 * default behavior stated here.
 */
class EpisodeListAdapter(
    private val dataSet: List<EpisodeListItem>,
    private val onItemSelected: (Int)->Unit,
    private var lastSelectedPosition: Int = NOT_SELECTED,
    context: Context
    ) :
    RecyclerView.Adapter<EpisodeListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val thumbnail: ImageView
        val entireView: View
        init {
            textView = view.findViewById(R.id.episode_name)
            thumbnail = view.findViewById(R.id.episode_thumbnail)
            entireView = view.findViewById(R.id.main_item_layout)
        }
    }

    private var lastSelectedItem: ViewHolder? = null
    private var contextReference: WeakReference<Context> = WeakReference(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.episode_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //On binding, load item's title and thumbnail
        viewHolder.textView.text = dataSet[viewHolder.adapterPosition].episodeTitle
        Picasso.get().load(dataSet[viewHolder.adapterPosition].imageUrl).into(
            viewHolder.thumbnail
        )

        //On item click: highlight it, unhighlight previously selected item and cache selection.
        //Also, trigger a callback from the view
        viewHolder.entireView.setOnClickListener {
            onItemUnselected(lastSelectedItem)
            onItemSelected(viewHolder)
            lastSelectedItem = viewHolder
            lastSelectedPosition = viewHolder.adapterPosition
            onItemSelected(viewHolder.adapterPosition)
        }

        if (position == lastSelectedPosition) {
            onItemSelected(viewHolder)
        } else {
            onItemUnselected(viewHolder)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    //Highlight a row
    private fun onItemSelected(viewHolder: ViewHolder) {
        viewHolder.entireView.background =
            contextReference.get()?.getDrawable(R.color.shield_primary_blue)
    }

    //Unhighlight a row
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onItemUnselected(viewHolder: ViewHolder?) {
        viewHolder?.entireView?.background =
            contextReference.get()?.getDrawable(R.color.white)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}


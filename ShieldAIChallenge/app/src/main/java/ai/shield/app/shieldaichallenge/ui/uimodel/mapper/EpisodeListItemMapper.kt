package ai.shield.app.shieldaichallenge.ui.uimodel.mapper

import ai.shield.app.shieldaichallenge.domain.model.Episode
import ai.shield.app.shieldaichallenge.ui.uimodel.EpisodeListItem

/**
 * Mapper function to convert from domain Episode model to an EpisodeListItem object that represent
 * the entry in the Episodes List Recycle View
 */
fun Episode.toEpisodeListItem(): EpisodeListItem {
    return EpisodeListItem(
        //Image endpoint not supporting http, converting to https
        this.image.medium.replace("http:", "https:"),
        //The list item title will have a format of S_ E_: Title
        //Ex: S1 E1: Winter is Coming
        "S${this.season} E${this.number}: ${this.name}"
    )
}
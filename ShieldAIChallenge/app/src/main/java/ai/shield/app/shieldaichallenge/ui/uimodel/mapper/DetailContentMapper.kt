package ai.shield.app.shieldaichallenge.ui.uimodel.mapper

import ai.shield.app.shieldaichallenge.domain.model.Episode
import ai.shield.app.shieldaichallenge.ui.uimodel.DetailContent

/**
 * Mapper method to convert from domain Episode model to an DetailContent object that represent
 * the Episode Detail's section of the Detail screen.
 */
fun Episode.toDetailContent(): DetailContent {
    return DetailContent(
        //Image endpoint not supporting http, converting to https
        this.image.original.replace("http:", "https:"),
        this.summary
    )
}
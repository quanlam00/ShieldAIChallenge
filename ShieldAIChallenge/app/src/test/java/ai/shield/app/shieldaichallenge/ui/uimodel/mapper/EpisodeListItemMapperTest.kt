package ai.shield.app.shieldaichallenge.ui.uimodel.mapper

import ai.shield.app.shieldaichallenge.domain.model.Episode
import ai.shield.app.shieldaichallenge.domain.model.Image
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class EpisodeListItemMapperTest {
    @Test
    fun `test EpisodeListItemMapper`() {
        //Given
        val mockEpisode = mockk<Episode>(relaxed = true)
        val mockImage = mockk<Image>(relaxed = true)
        every { mockEpisode.name } returns "Name"
        every { mockEpisode.number } returns 1
        every { mockEpisode.season } returns 1
        every { mockEpisode.image } returns mockImage
        every { mockImage.medium } returns "mockUrl"

        //When
        val mockEpisodeItem = mockEpisode.toEpisodeListItem()

        //Then
        assert(mockEpisodeItem.episodeTitle == "S1 E1: Name")
        assert(mockEpisodeItem.imageUrl == "mockUrl")
    }
}
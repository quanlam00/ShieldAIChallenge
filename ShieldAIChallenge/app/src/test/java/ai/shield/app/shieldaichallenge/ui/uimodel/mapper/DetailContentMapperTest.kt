package ai.shield.app.shieldaichallenge.ui.uimodel.mapper

import ai.shield.app.shieldaichallenge.domain.model.Episode
import ai.shield.app.shieldaichallenge.domain.model.Image
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class DetailContentMapperTest {
    @Test
    fun `test DetailContentMapper`() {
        //Given
        val mockEpisode = mockk<Episode>(relaxed = true)
        val mockImage = mockk<Image>(relaxed = true)
        every { mockEpisode.summary } returns "Mock Summary"
        every { mockEpisode.image } returns mockImage
        every { mockImage.original } returns "mockUrl"

        //When
        val detailContent = mockEpisode.toDetailContent()

        //Then
        assert(detailContent.detailSummary == "Mock Summary")
        assert(detailContent.imageUrl == "mockUrl")
    }
}
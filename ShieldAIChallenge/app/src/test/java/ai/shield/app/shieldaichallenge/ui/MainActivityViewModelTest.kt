package ai.shield.app.shieldaichallenge.ui

import ai.shield.app.shieldaichallenge.data.EpisodeQueryService
import ai.shield.app.shieldaichallenge.domain.model.Episode
import ai.shield.app.shieldaichallenge.domain.model.Image
import ai.shield.app.shieldaichallenge.ui.MainActivityViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {
    private val mockEpisodeQueryService = mockk<EpisodeQueryService>(relaxed = true)
    private val subject = MainActivityViewModel(mockk(relaxed = true), mockEpisodeQueryService)

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `test loadEpisodesData() succeed`() {
        //Given
        //Mock service result
        every { mockEpisodeQueryService.queryGOTEpisodes() } returns listOf(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )

        //When
        subject.loadEpisodesData()

        //Then
        assert(subject.viewState.value is MainActivityViewModel.MainViewState.Loading)
        //Necessary break for flow state value to be able to update view state from background
        Thread.sleep(500)

        assert(subject.viewState.value is MainActivityViewModel.MainViewState.Loaded)
        assert(
            (subject.viewState.value as MainActivityViewModel.MainViewState.Loaded)
                .episodeList.size == 3
        )
    }

    @Test
    fun `test loadEpisodesData() fail`() {
        //Given
        //Mock service result
        every { mockEpisodeQueryService.queryGOTEpisodes() } throws IllegalStateException()

        //When
        subject.loadEpisodesData()

        //Then
        assert(subject.viewState.value is MainActivityViewModel.MainViewState.Loading)
        //Necessary break for flow state value to be able to update view state from background
        Thread.sleep(500)

        assert(subject.viewState.value is MainActivityViewModel.MainViewState.Error)
    }

    @Test
    fun `test getDetailContent() succeed`() {
        //Given
        val mockEpisode = mockk<Episode>(relaxed = true)
        val mockImage = mockk<Image>(relaxed = true)
        every { mockEpisodeQueryService.queryGOTEpisodes() } returns listOf(
            mockEpisode
        )
        every { mockEpisode.summary } returns "Mock Summary"
        every { mockEpisode.image } returns mockImage
        every { mockImage.original } returns "mockUrl"

        //When
        subject.loadEpisodesData()

        //Then
        Thread.sleep(500)
        val detailContent = subject.getDetailContent(0)
        assert(detailContent.detailSummary == "Mock Summary")
        assert(detailContent.imageUrl == "mockUrl")
    }
}
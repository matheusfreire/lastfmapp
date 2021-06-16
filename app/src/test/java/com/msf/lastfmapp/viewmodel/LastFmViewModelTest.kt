package com.msf.lastfmapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.msf.lastfmapp.core.TestCoroutineRule
import com.msf.lastfmapp.model.Error
import com.msf.lastfmapp.model.MusicResult
import com.msf.lastfmapp.model.Results
import com.msf.lastfmapp.network.ResultWrapper
import com.msf.lastfmapp.repository.LastFmRepository
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest

class LastFmViewModelTest : KoinTest {

    private val repository: LastFmRepository = mockk(relaxed = true)
    private val loadingObserver: Observer<Boolean> = mockk(relaxed = true)
    private val errorObserver: Observer<Error> = mockk(relaxed = true)
    private val resultsObserver: Observer<Results> = mockk(relaxed = true)

    private lateinit var viewModel: LastFmViewModel

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    private val testCoroutineRule = TestCoroutineRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LastFmViewModel(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `GIVEN viewmodel fetch track is called WHEN there isn't other job THEN loading should be posted on true`() =
        testCoroutineRule.runBlockingTest {
            viewModel.liveDataLoading.observeForever(loadingObserver)

            viewModel.fetchTracks("one music")

            val slotLoading = slot<Boolean>()
            verify {
                loadingObserver.onChanged(capture(slotLoading))
            }

            slotLoading.captured shouldBe true
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `GIVEN viewmodel fetch track is called WHEN there isn't other job and API returns success result THEN success livedata should be changed`() =
        testCoroutineRule.runBlockingTest {
            val objectFromJson = Gson().fromJson(
                "{\"results\":{\"opensearch:Query\":{\"#text\":\"\",\"role\":\"request\",\"startPage\":\"1\"}," +
                        "\"opensearch:totalResults\":\"1674\",\"opensearch:startIndex\":\"0\",\"opensearch:itemsPerPage\":\"1\"," +
                        "\"trackmatches\":{\"track\":[" +
                        "{\"name\":\"Faroeste Caboclo\",\"artist\":\"Legião Urbana\"," +
                        "\"url\":\"https://www.last.fm/music/Legi%C3%A3o+Urbana/_/Faroeste+Caboclo\"," +
                        "\"streamable\":\"FIXME\",\"listeners\":\"122503\"," +
                        "\"image\":[{\"#text\":\"https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png\",\"size\":\"small\"}," +
                        "{\"#text\":\"https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png\",\"size\":\"medium\"}," +
                        "{\"#text\":\"https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png\",\"size\":\"large\"}," +
                        "{\"#text\":\"https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png\",\"size\":\"extralarge\"}]," +
                        "\"mbid\":\"7272bb44-36ad-49c3-aacf-09134f9b570a\"}]},\"@attr\":{}}}",
                MusicResult::class.java
            )
            coEvery { repository.fetchMusics(any()) } returns ResultWrapper.Success(objectFromJson)
            viewModel.liveDataResults.observeForever(resultsObserver)

            viewModel.fetchTracks("one music")

            val slotResults = slot<Results>()
            verify {
                resultsObserver.onChanged(capture(slotResults))
            }

            val capturedChange = slotResults.captured
            capturedChange shouldNotBe null
            capturedChange.itemsPerPage.toInt() shouldBe 1
            capturedChange.totalResults.toInt() shouldBe 1674
            capturedChange.startIndex.toInt() shouldBe 0
            capturedChange.openSearch.role shouldBe "request"
            capturedChange.openSearch.text.isEmpty() shouldBe true
            capturedChange.openSearch.startPage.toInt() shouldBe 1
            capturedChange.trackmatches shouldNotBe null
            capturedChange.trackmatches.track shouldNotBe null
            capturedChange.trackmatches.track.size shouldBe 1
            capturedChange.trackmatches.track[0].artist shouldBe "Legião Urbana"
            capturedChange.trackmatches.track[0].name shouldBe "Faroeste Caboclo"
            capturedChange.trackmatches.track[0].listeners.toInt() shouldBe 122503
            capturedChange.trackmatches.track[0].url shouldBe "https://www.last.fm/music/Legi%C3%A3o+Urbana/_/Faroeste+Caboclo"
            capturedChange.trackmatches.track[0].images.size shouldBe 4
            capturedChange.trackmatches.track[0].albumPhoto() shouldBe "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `GIVEN viewmodel fetch track is called WHEN there isn't other job and API returns errors result THEN error livedata should be changed`() =
        testCoroutineRule.runBlockingTest {
            val objectFromJson = Gson().fromJson(
                        "{\n" +
                        "  \"error\": 10,\n" +
                        "  \"message\": \"Invalid API key - You must be granted a valid key by last.fm\"\n" +
                        "}",
                Error::class.java
            )
            coEvery { repository.fetchMusics(any()) } returns ResultWrapper.GenericError(objectFromJson.code, objectFromJson)
            viewModel.liveDataError.observeForever(errorObserver)

            viewModel.fetchTracks("one music")

            val slotResults = slot<Error>()
            verify {
                errorObserver.onChanged(capture(slotResults))
            }

            val capturedChange = slotResults.captured
            capturedChange shouldNotBe null
            capturedChange.code shouldBe 10
            capturedChange.message shouldBe "Invalid API key - You must be granted a valid key by last.fm"
        }
}
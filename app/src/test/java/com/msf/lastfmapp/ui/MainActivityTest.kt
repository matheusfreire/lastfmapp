package com.msf.lastfmapp.ui

import android.os.Build
import android.view.View
import androidx.core.view.isVisible
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import com.msf.lastfmapp.R
import com.msf.lastfmapp.di.LastFmDi
import com.msf.lastfmapp.repository.LastFmRepository
import com.msf.lastfmapp.viewmodel.LastFmViewModel
import io.kotlintest.matchers.instanceOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.M])
class MainActivityTest {

    private val repository: LastFmRepository = mockk(relaxed = true)
    private val lastFmViewModel: LastFmViewModel = mockk(relaxed = true)

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(
                LastFmDi.module
            )
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `should show activity`() {
        val buildActivity = Robolectric.buildActivity(MainActivity::class.java)

        buildActivity shouldNotBe null
        buildActivity.get() shouldNotBe null
        buildActivity.get() shouldBe instanceOf(MainActivity::class)
    }


    @Test
    fun `GIVEN activity is created WHEN is resumed THEN msgLayout should be showed`(){
        val scenario = launchActivity<MainActivity>()
        scenario shouldNotBe null
        scenario.onActivity { activity ->
            activity.findViewById<View>(R.id.msgLayout).isVisible shouldBe true

            val imageMsg = activity.binding.msgLayout.imageEmptyLayout
            imageMsg.isVisible shouldBe true

            val textView = activity.binding.msgLayout.msgEmptyLayout
            textView.isVisible shouldBe true
            textView.text shouldBe "Find your musics,\ntype the name of it"
        }
    }

    @Test
    fun `GIVEN activity is created WHEN is resumed THEN recycler should be gone`(){
        val scenario = launchActivity<MainActivity>()
        scenario shouldNotBe null
        scenario.onActivity { activity ->
            activity.binding.recyclerViewMusics.isVisible shouldBe false
        }
    }

    @Test
    fun `GIVEN activity is created WHEN is resumed THEN progress should be gone`(){
        val scenario = launchActivity<MainActivity>()
        scenario shouldNotBe null
        scenario.onActivity { activity ->
            activity.binding.progressLastFm.isVisible shouldBe false
        }
    }

    @Test
    fun `GIVEN activity is created WHEN is resumed THEN progress should be visible on search begins`(){
        val scenario = launchActivity<MainActivity>()
        scenario shouldNotBe null
        scenario.onActivity { activity ->
            activity.binding.txtInputEdit.setText("ten")
            activity.binding.progressLastFm.isVisible shouldBe true
        }
    }

}

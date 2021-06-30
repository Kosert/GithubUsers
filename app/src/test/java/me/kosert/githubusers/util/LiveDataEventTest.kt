package me.kosert.githubusers.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import java.util.concurrent.Executors


class LiveDataEventTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `event is not handled when lifecycle is not started`() {
        val testLifecycle = TestLifecycleOwner(Lifecycle.State.CREATED)
        val liveDataEvent = LiveDataEvent<Int>()
        val mockedCallback: (t: Int) -> Unit = mock()
        liveDataEvent.observe(testLifecycle, mockedCallback)

        liveDataEvent.setValue(1)
        liveDataEvent.setValue(2)
        liveDataEvent.setValue(3)

        verify(mockedCallback, times(0))(any())
    }

    @Test
    fun `event is handled once`() {
        val testLifecycle = TestLifecycleOwner(Lifecycle.State.CREATED)
        val liveDataEvent = LiveDataEvent<Int>()
        val mockedCallback: (t: Int) -> Unit = mock()
        liveDataEvent.observe(testLifecycle, mockedCallback)

        testLifecycle.currentState = Lifecycle.State.STARTED
        liveDataEvent.setValue(7)
        testLifecycle.currentState = Lifecycle.State.CREATED
        testLifecycle.currentState = Lifecycle.State.STARTED

        verify(mockedCallback, times(1))(7)
    }

    @Test
    fun `multiple events are handled`() {
        val testLifecycle = TestLifecycleOwner(Lifecycle.State.CREATED)
        val liveDataEvent = LiveDataEvent<Int>()
        val mockedCallback: (t: Int) -> Unit = mock()
        liveDataEvent.observe(testLifecycle, mockedCallback)

        testLifecycle.currentState = Lifecycle.State.STARTED
        liveDataEvent.setValue(1)
        liveDataEvent.setValue(1)
        liveDataEvent.setValue(2)
        testLifecycle.currentState = Lifecycle.State.CREATED
        liveDataEvent.setValue(3)
        testLifecycle.currentState = Lifecycle.State.STARTED


        verify(mockedCallback, times(2))(1)
        verify(mockedCallback, times(1))(2)
        verify(mockedCallback, times(1))(3)
    }

}
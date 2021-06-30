package me.kosert.githubusers.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import java.util.concurrent.Executors

class KotlinLiveDataTest {

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
    fun `value changes`() {
        val liveData = KotlinLiveData<Int>(123)
        assertEquals(123, liveData.value)

        liveData.value = 456
        assertEquals(456, liveData.value)
    }

    @Test
    fun `observer not called when lifecycle is not started`() {
        val testLifecycle = TestLifecycleOwner(Lifecycle.State.CREATED)
        val liveData = KotlinLiveData<Int>(0)
        val mockedCallback: (t: Int) -> Unit = mock()
        liveData.observe(testLifecycle, mockedCallback)

        liveData.value = 1
        liveData.value = 2
        liveData.value = 3

        Mockito.verify(mockedCallback, times(0))(any())
    }

    @Test
    fun `multiple changes are handled`() {
        val testLifecycle = TestLifecycleOwner(Lifecycle.State.CREATED)
        val liveData = KotlinLiveData<Int>(1)
        val mockedCallback: (t: Int) -> Unit = mock()
        liveData.observe(testLifecycle, mockedCallback)

        testLifecycle.currentState = Lifecycle.State.STARTED
        liveData.value = 1
        liveData.value = 1
        liveData.value = 2
        testLifecycle.currentState = Lifecycle.State.CREATED
        liveData.value = 3
        testLifecycle.currentState = Lifecycle.State.STARTED


        Mockito.verify(mockedCallback, times(3))(1)
        Mockito.verify(mockedCallback, times(1))(2)
        Mockito.verify(mockedCallback, times(1))(3)
    }

    @Test
    fun `changes are not handled after destroy`() {
        val testLifecycle = TestLifecycleOwner(Lifecycle.State.CREATED)
        val liveData = KotlinLiveData<Int>(7)
        val mockedCallback: (t: Int) -> Unit = mock()
        liveData.observe(testLifecycle, mockedCallback)

        testLifecycle.currentState = Lifecycle.State.STARTED
        testLifecycle.currentState = Lifecycle.State.DESTROYED
        testLifecycle.currentState = Lifecycle.State.STARTED

        liveData.value = 1
        liveData.value = 7
        liveData.value = 5

        Mockito.verify(mockedCallback, times(1))(any())
    }
}
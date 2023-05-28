package id.wikosac.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.data.StoryRepository
import id.wikosac.storyapp.utils.DataDummy
import id.wikosac.storyapp.utils.MainDispatcher
import id.wikosac.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcher = MainDispatcher()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var homeViewModel: HomeViewModel
    private val dataDummy = DataDummy.storyDummy()
    private val updateCallback = object : ListUpdateCallback {

        override fun onInserted(position: Int, count: Int) {}

        override fun onRemoved(position: Int, count: Int) {}

        override fun onMoved(fromPosition: Int, toPosition: Int) {}

        override fun onChanged(position: Int, count: Int, payload: Any?) {}

    }

    @Before
    fun before() {
        homeViewModel = HomeViewModel(storyRepository)
    }

    @Test
    fun `When there is no story data`() = runTest {
        val noStory = MutableLiveData<PagingData<Story>>()
        noStory.value = PagingData.from(emptyList())
        Mockito.`when`(storyRepository.getPagedStory()).thenReturn(noStory)
        val storyData = homeViewModel.storyPaged().getOrAwaitValue()
        val pagingData = AsyncPagingDataDiffer(HomeAdapter.DIFF_CALLBACK, updateCallback, Dispatchers.Main)
        pagingData.submitData(storyData)
        Mockito.verify(storyRepository).getPagedStory()
        assertTrue(pagingData.itemCount == 0)
    }

    @Test
    fun `When successfully loading story data`() = runTest {
        val storyDummy = MutableLiveData<PagingData<Story>>()
        storyDummy.value = PagingData.from(dataDummy)
        Mockito.`when`(storyRepository.getPagedStory()).thenReturn(storyDummy)
        val storyData = homeViewModel.storyPaged().getOrAwaitValue()
        val pagingData = AsyncPagingDataDiffer(HomeAdapter.DIFF_CALLBACK, updateCallback, Dispatchers.Main)
        pagingData.submitData(storyData)
        Mockito.verify(storyRepository).getPagedStory()
        assertNotNull(pagingData)
        assertTrue(pagingData.getItem(0)?.id == dataDummy[0].id)
        assertTrue(pagingData.itemCount == dataDummy.size)
    }
}
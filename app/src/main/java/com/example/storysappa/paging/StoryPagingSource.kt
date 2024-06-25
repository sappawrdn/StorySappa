package com.example.storysappa.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storysappa.story.ListStoryItem
import com.example.storysappa.story.StoryApiService
import com.example.storysappa.story.StoryResponse

class StoryPagingSource(private val storyApiService: StoryApiService): PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = storyApiService.getStories2(position, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory?.filterNotNull() ?: emptyList(),
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}
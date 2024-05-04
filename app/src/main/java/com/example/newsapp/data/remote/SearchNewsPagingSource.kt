package com.example.newsapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.remote.dto.NewsApi
import com.example.newsapp.domain.model.Article

class SearchNewsPagingSource (
    private val newsApi: NewsApi,
    private val sources:String,
    private val searchQuery: String
): PagingSource<Int, Article>() {

    private var totalNewsCount=0
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPage ->
            val page = state.closestPageToPosition(anchorPage)
            page?.nextKey?.minus(1) ?: page?.prevKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page=params.key?:1
        return try {
            val newsResponse=newsApi.searchNews(searchQuery=searchQuery,sources = sources,page=page)
            totalNewsCount=newsResponse.articles.size
            val articles=newsResponse.articles.distinctBy {it.title }
            LoadResult.Page(
                data=articles,
                prevKey = null,
                nextKey = if(totalNewsCount==newsResponse.totalResults) null else page+1
            )
        }catch (e:Exception){
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }
}
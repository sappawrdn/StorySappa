package com.example.storysappa

import com.example.storysappa.story.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "Story $i",
                description = "Description $i",
                photoUrl = "https://example.com/photo$i.jpg",
                createdAt = "2021-09-14T06:51:25.000Z",
                lon = 0.0,
                lat = 0.0
            )
            items.add(story)
        }
        return items
    }
}
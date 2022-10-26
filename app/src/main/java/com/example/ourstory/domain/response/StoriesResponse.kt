package com.example.ourstory.domain.response

import com.example.ourstory.domain.model.StoryModel

data class StoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<StoryModel>
)
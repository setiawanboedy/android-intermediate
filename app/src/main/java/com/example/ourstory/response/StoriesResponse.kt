package com.example.ourstory.response

import com.example.ourstory.model.StoryModel

data class StoriesResponse (
    val error: Boolean,
    val message: String,
    val listStory: List<StoryModel>
)
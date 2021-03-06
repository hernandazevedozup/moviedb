package com.hernandazevedo.moviedb.domain.usecase.request

import com.hernandazevedo.moviedb.domain.usecase.base.BaseRequestValues

data class DeleteMovieFromLocalDatabaseRequest(val imdbId: String) : BaseRequestValues
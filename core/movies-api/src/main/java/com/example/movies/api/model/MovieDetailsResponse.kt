package com.example.movies.api.model

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
	val originalLanguage: String? = null,
	val imdbId: String? = null,
	val video: Boolean? = null,
	@field:SerializedName("title")
	val title: String? = null,
	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,
	val revenue: Int? = null,
	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,
	val popularity: Any? = null,
	val productionCountries: List<ProductionCountriesItem?>? = null,
	val id: Int? = null,
	val voteCount: Int? = null,
	val budget: Int? = null,
	@field:SerializedName("overview")
	val overview: String? = null,
	val originalTitle: String? = null,
	val runtime: Int? = null,
	val posterPath: String? = null,
	val originCountry: List<String?>? = null,
	val spokenLanguages: List<SpokenLanguagesItem?>? = null,
	val productionCompanies: List<ProductionCompaniesItem?>? = null,
	val releaseDate: String? = null,
	@field:SerializedName("vote_average")
	val voteAverage: Any? = null,
	val belongsToCollection: Any? = null,
	val tagline: String? = null,
	val adult: Boolean? = null,
	val homepage: String? = null,
	val status: String? = null
)

data class SpokenLanguagesItem(
	val name: String? = null,
	val iso6391: String? = null,
	val englishName: String? = null
)

data class ProductionCompaniesItem(
	val logoPath: String? = null,
	val name: String? = null,
	val id: Int? = null,
	val originCountry: String? = null
)

data class ProductionCountriesItem(
	val iso31661: String? = null,
	val name: String? = null
)

data class GenresItem(
	@field:SerializedName("name")
	val name: String? = null,
	val id: Int? = null
)



data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String
)


data class Movie(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Poster: String
)


package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {

		List<Movie> searchMovies = new ArrayList<>();

		String regexp = String.format("\\b%s\\b", queryText);
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);

		MoviesResponse response = movieDataService.fetchAll();
		if (!response.isEmpty()) {
			response.stream()
					.filter(q -> pattern.matcher(q.getTitle()).find())
					.forEach(r -> {
						Movie movie = new Movie();
						movie.setName(r.getTitle());
						movie.setActors(r.getCast());
						searchMovies.add(movie);
					});
		}
		return searchMovies;
	}
}

package com.wongnai.interview.movie.sync;

import javax.transaction.Transactional;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieDataService;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;

	@Transactional
	public void forceSync() {
		if (movieRepository.count() > 0) {
			movieRepository.deleteAll();
		}
		MoviesResponse moviesResponse = movieDataService.fetchAll();
		if (!moviesResponse.isEmpty()) {
			movieRepository.saveAll(mapToMovieList(moviesResponse));
		}
	}

	/**
	 * map MovieResponse (List of MovieData) to List of Movie
	 * @param movieData list of MovieData
	 * @return list of Movie
	 */
	private List<Movie> mapToMovieList(MoviesResponse movieData) {
		List<Movie> movies = new ArrayList<>();
		movieData.forEach(m -> {
			Movie movie = new Movie();
			movie.setName(m.getTitle());
			movie.setActors(m.getCast());
			movies.add(movie);
		});
		return movies;
	}
}

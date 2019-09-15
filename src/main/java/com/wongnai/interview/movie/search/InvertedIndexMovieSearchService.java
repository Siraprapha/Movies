package com.wongnai.interview.movie.search;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Optional;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieRepository movieRepository;

	private static Map<String, Set<Long>> wordsMapper = new HashMap<>();

	@Override
	public List<Movie> search(String queryText) {
		String[] words = queryText.split("\\W+");
		Set<Long> result = new HashSet<>();
		for (String word : words) {
			Set<Long> ids = wordsMapper.get(word.toLowerCase());
			if (ids == null) {
				continue;
			}
			if (result.isEmpty()) {
				result.addAll(ids);
			} else {
				result.retainAll(ids);
			}
		}
		return convertIdsToMovieList(result);
	}

	public void mapping(List<Movie> movies) {
		if (!movies.isEmpty()) {
			movies.forEach( m -> {
				for (String word : m.getName().split("\\W+")) {
					String w = word.toLowerCase();
					if (wordsMapper.containsKey(w)) {
						Set<Long> ids = wordsMapper.get(w);
						ids.add(m.getId());
						wordsMapper.put(w,ids);
					} else {
						wordsMapper.put(w, Sets.newHashSet(m.getId()));
					}
				}

			});
		}
	}

	private List<Movie> convertIdsToMovieList(Set<Long> ids) {
		List<Movie> movieList = new ArrayList<>();
		ids.forEach( id -> {
			Optional<Movie> movie = movieRepository.findById(id);
			movie.ifPresent(movieList::add);
		});
		return movieList;
	}
}

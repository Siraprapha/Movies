package com.wongnai.interview.movie.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class MovieDataServiceImpl implements MovieDataService {
	public static final String MOVIE_DATA_URL
			= "https://raw.githubusercontent.com/prust/wikipedia-movie-data/master/movies.json";

	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public MoviesResponse fetchAll() {
		ResponseEntity<String> response = restTemplate.getForEntity(MOVIE_DATA_URL, String.class);
		MoviesResponse moviesResponse = new MoviesResponse();
		try {
			moviesResponse = objectMapper.readValue(response.getBody(), MoviesResponse.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return moviesResponse;
	}
}

package io.javabrains.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.javabrains.models.CatalogItem;
import io.javabrains.models.Movie;
import io.javabrains.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;

	// @Autowired
	// private WebClient.Builder webClientBuilder;

	@RequestMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating userRating = restTemplate.getForObject("http://rating-data-service/ratingsdata/users/" + userId,
				UserRating.class);

		return userRating.getUserRating().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(),
					Movie.class);
			/*
			 * Movie movie = webClientBuilder.build() .get()
			 * .uri("http://localhost:8082/movies/" + rating.getMovieId()) .retrieve()
			 * .bodyToMono(Movie.class) .block();
			 */

			return new CatalogItem(movie.getMovieName(), "Desc", 4);
		}).collect(Collectors.toList());

	}
}

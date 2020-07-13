package ge.baski.moviecatalogservice.resources;

import ge.baski.moviecatalogservice.models.CatalogItem;
import ge.baski.moviecatalogservice.models.Movie;
import ge.baski.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    WebClient.Builder webClientBuilder;

//    @Autowired
//    private DiscoveryClient discoveryClient;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);

        return ratings.getUserRating().stream()
                .map(rating -> {
                    // For each movie ID, call movie-info-service and get details
                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
                    // Put them all together
                    System.out.println("Desct" + movie.getOverview());
                    return new CatalogItem(movie.getName(),movie.getOverview(), rating.getRating());
                })
                .collect(Collectors.toList());
    }
}

/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/

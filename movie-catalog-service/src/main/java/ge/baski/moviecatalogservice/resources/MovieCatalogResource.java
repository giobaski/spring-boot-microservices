package ge.baski.moviecatalogservice.resources;

import com.netflix.discovery.DiscoveryClient;
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

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {

            // For each movie ID, call movie-info-service and get details
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            // Put them all together
            return new CatalogItem(movie.getName(),"The description...", rating.getRating());

        })
        .collect(Collectors.toList());

    }
}

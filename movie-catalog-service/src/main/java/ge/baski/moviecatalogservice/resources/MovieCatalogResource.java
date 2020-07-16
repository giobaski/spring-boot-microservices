package ge.baski.moviecatalogservice.resources;

import ge.baski.moviecatalogservice.models.CatalogItem;
import ge.baski.moviecatalogservice.models.UserRating;
import ge.baski.moviecatalogservice.services.MovieInfo;
import ge.baski.moviecatalogservice.services.UserRatingInfo;
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
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;


//    @Autowired
//    WebClient.Builder webClientBuilder;

//    @Autowired
//    private DiscoveryClient discoveryClient;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        //get user's movie ratings from ratings-data-service using getUserRatin() method
        // we split into this two method for using separate fallbacks on each call if any API service is down
        //API call I. to ratings-data-service
        UserRating ratings = userRatingInfo.getUserRating(userId);

        //API Call II to Movie-info-service
        return ratings.getUserRating().stream()
                .map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());
    }

}//endOfClass

/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/

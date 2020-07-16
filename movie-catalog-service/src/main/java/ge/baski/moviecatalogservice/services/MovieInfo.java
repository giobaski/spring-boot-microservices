package ge.baski.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import ge.baski.moviecatalogservice.models.CatalogItem;
import ge.baski.moviecatalogservice.models.Movie;
import ge.baski.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItem(Rating rating) {
        // For each movie ID, call movie-info-service and get details
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        // Put them all together
        System.out.println("Desct" + movie.getOverview());
        return new CatalogItem(movie.getName(),movie.getOverview(), rating.getRating());
    }

    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found, Its's Fallback","No Desc", rating.getRating());
    }

}

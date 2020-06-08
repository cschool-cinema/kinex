package pl.termosteam.kinex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.repository.MovieRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepositoryMock;

    @InjectMocks
    private MovieService movieService;

    private Movie movie1;
    private Movie movie2;
    private Movie movie3;

    @BeforeEach
    public void init() {
        movie1 = new Movie(1, "movie_A",new Short("2000"), "A",new Short("50"), "good",new ArrayList<>());
        movie2 = new Movie(2, "movie_BA",new Short("2010"), "B",new Short("60"), "good",new ArrayList<>());
        movie3 = new Movie(3, "movie_C",new Short("2019"), "A",new Short("70"), "good",new ArrayList<>());
    }

    @Test
    public void findAllMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        given(movieRepositoryMock.findAll(Sort.by(Sort.Direction.ASC, "title"))).willReturn(movies);
        List<Movie> auList=movieService.findAllMovies();
        assertEquals(movies.size(), auList.size());
    }

    @Test
    public void findById() {
        given(movieRepositoryMock.findById(1)).willReturn(Optional.of(movie1));
        Movie movie=movieService.findById(1);
        assertEquals(movie, movie1);
    }

    @Test
    public void findMovieByName() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);
        given(movieRepositoryMock.findAllByTitleIgnoreCaseContainingOrderByTitleAsc("A")).willReturn(movies);
        List<Movie> auList=movieService.findMovieByName("A");
        assertEquals(movies.size(), auList.size());
    }

    @Test
    public void deleteMovie() {
        given(movieRepositoryMock.findById(2)).willReturn(Optional.of(movie2));
        String res=movieService.deleteMovie(2);
        assertEquals(res, "Movie has been successfully deleted.");
    }

    @Test
    public void createMovie() {
        given(movieRepositoryMock.save(movie1)).willReturn(movie1);
        Movie movie=movieService.createMovie(movie1);
        assertEquals(movie, movie1);
    }

    @Test
    public void updateMovie() {
        Movie movieToUpdate=movie2;
        movieToUpdate.setId(1);
        given(movieRepositoryMock.findById(1)).willReturn(Optional.of(movie1));
        given(movieRepositoryMock.save(movieToUpdate)).willReturn(movieToUpdate);
        Movie res=movieService.updateMovie(movie2,1);
        assertEquals(res, movie2);
    }

}

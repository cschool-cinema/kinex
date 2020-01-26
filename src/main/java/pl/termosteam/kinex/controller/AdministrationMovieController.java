package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.dto.MovieDto;
import pl.termosteam.kinex.service.MovieService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/administration/movie")
public class AdministrationMovieController {

    private final MovieService movieService;
    private final ModelMapper mm;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    public List<MovieDto> findAllMovies() {
        List<Movie> movies = movieService.findAllMovies();

        return Arrays.asList(mm.map(movies, MovieDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping(path = "{movieId}")
    public MovieDto findMovieById(@PathVariable int movieId) {
        Movie movie = movieService.findById(movieId);

        return mm.map(movie, MovieDto.class);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping(path = "search/{searchPhrase}")
    public List<MovieDto> findMoviesByTitleContaining(@PathVariable String searchPhrase) {
        List<Movie> movies = movieService.findMovieByName(searchPhrase);

        return Arrays.asList(mm.map(movies, MovieDto[].class));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping(path = "{movieId}")
    public String deleteMovie(@PathVariable int movieId) {
        return movieService.deleteMovie(movieId);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    public MovieDto createMovie(@RequestBody @Valid MovieDto movieDto) {
        Movie movie = mm.map(movieDto, Movie.class);

        Movie savedMovie = movieService.createMovie(movie);

        return mm.map(savedMovie, MovieDto.class);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping(path = "{movieId}")
    public MovieDto updateMovie(@RequestBody @Valid MovieDto movieDto, @PathVariable int movieId) {
        Movie movieToUpdate = mm.map(movieDto, Movie.class);

        Movie updatedMovie = movieService.updateMovie(movieToUpdate, movieId);

        return mm.map(updatedMovie, MovieDto.class);
    }
}
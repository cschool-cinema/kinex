package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.dto.MovieDto;
import pl.termosteam.kinex.dto.ScreeningDto;
import pl.termosteam.kinex.dto.ScreeningInputDto;
import pl.termosteam.kinex.service.MovieService;
import pl.termosteam.kinex.service.ScreeningService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("kinex")
@AllArgsConstructor
public class EmployeeController {

    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final ModelMapper mm;

    @GetMapping(path = "all-screenings")
    public List<ScreeningDto> findAllScreenings() {
        List<Screening> screenings = screeningService.findAllScreenings();

        return Arrays.asList(mm.map(screenings, ScreeningDto[].class));
    }

    @GetMapping(path = "screening/{screeningId}")
    public ScreeningDto findScreeningById(@PathVariable int screeningId) {
        Screening screening = screeningService.findScreeningById(screeningId);

        return mm.map(screening, ScreeningDto.class);
    }

    @DeleteMapping(path = "screening/{screeningId}")
    public String deleteScreening(@PathVariable int screeningId) {
        return screeningService.deleteScreening(screeningId);
    }

    @PostMapping(path = "screening")
    public ScreeningDto createScreening(@RequestBody @Valid ScreeningInputDto inputDto) {
        Screening screening = screeningService.createScreening(inputDto);

        return mm.map(screening, ScreeningDto.class);
    }

    @PutMapping(path = "screening/{screeningId}")
    public ScreeningDto updateScreening(@PathVariable int screeningId, @RequestBody @Valid ScreeningInputDto inputDto) {
        Screening screening = screeningService.updateScreeningDetails(inputDto, screeningId);

        return mm.map(screening, ScreeningDto.class);
    }

    @GetMapping(path = "movies")
    public List<MovieDto> findAllMovies() {
        List<Movie> movies = movieService.findAllMovies();

        return Arrays.asList(mm.map(movies, MovieDto[].class));
    }

    @GetMapping(path = "movie/{movieId}")
    public MovieDto findMovieById(@PathVariable int movieId) {
        Movie movie = movieService.findById(movieId);

        return mm.map(movie, MovieDto.class);
    }

    @GetMapping(path = "movies/{searchPhrase}")
    public List<MovieDto> findMoviesByTitleContaining(@PathVariable String searchPhrase) {
        List<Movie> movies = movieService.findMovieByName(searchPhrase);

        return Arrays.asList(mm.map(movies, MovieDto[].class));
    }

    @DeleteMapping(path = "movie/{movieId}")
    public String deleteMovie(@PathVariable int movieId) {
        return movieService.deleteMovie(movieId);
    }

    @PostMapping(path = "movie")
    public MovieDto createMovie(@RequestBody @Valid MovieDto movieDto) {
        Movie movie = mm.map(movieDto, Movie.class);

        Movie savedMovie = movieService.createMovie(movie);

        return mm.map(savedMovie, MovieDto.class);
    }

    @PutMapping(path = "movie/{movieId}")
    public MovieDto updateMovie(@RequestBody @Valid MovieDto movieDto, @PathVariable int movieId) {
        Movie movieToUpdate = mm.map(movieDto, Movie.class);

        Movie updatedMovie = movieService.updateMovie(movieToUpdate, movieId);

        return mm.map(updatedMovie, MovieDto.class);
    }
}

package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.exception.NotAllowedException;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.MovieRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.MOVIE_NOT_FOUND;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<Movie> findAllMovies() {
        return movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public Movie findById(int id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND));
    }

    public List<Movie> findMovieByName(String searchPhrase) {
        return movieRepository.findAllByTitleIgnoreCaseContainingOrderByTitleAsc(searchPhrase);
    }

    @Transactional
    public String deleteMovie(int id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND));

        if (movie.getScreenings().size() > 0) {
            throw new NotAllowedException("Delete not allowed! Screenings already exist for this movie.");
        }

        movieRepository.delete(movie);

        return "Movie has been successfully deleted.";
    }

    @Transactional
    public Movie createMovie(Movie movie) {
        validateMovieReleaseYear(movie);

        if (checkDuplicate(movie).isPresent()) {
            throw new NotAllowedException("This movie already exists in the database.");
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public Movie updateMovie(Movie movieInput, int id) {
        Movie movieToUpdate = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND));

        validateMovieReleaseYear(movieInput);

        Optional<Movie> duplicateMovie = checkDuplicate(movieInput);
        if (duplicateMovie.isPresent()) {
            Movie presentDuplicate = duplicateMovie.get();

            if (presentDuplicate.getId() != id) {
                throw new NotAllowedException("This movie already exists in the database.");
            }
        }

        movieToUpdate.setCategory(movieInput.getCategory());
        movieToUpdate.setDescription(movieInput.getDescription());
        movieToUpdate.setDurationMin(movieInput.getDurationMin());
        movieToUpdate.setReleaseYear(movieInput.getReleaseYear());
        movieToUpdate.setTitle(movieInput.getTitle());

        return movieRepository.save(movieToUpdate);
    }

    private void validateMovieReleaseYear(Movie movie) {
        if (movie.getReleaseYear() > LocalDate.now().getYear() + 5) {
            throw new NotAllowedException("Release year cannot be greater than current year + 5 years.");
        }
    }

    private Optional<Movie> checkDuplicate(Movie movie) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("releaseYear", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("category", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("durationMin", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("description", "id");
        Example<Movie> example = Example.of(movie, matcher);

        return movieRepository.findOne(example);
    }
}

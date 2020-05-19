package pl.termosteam.kinex.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.dto.MovieDto;
import pl.termosteam.kinex.service.MovieService;

@ExtendWith(MockitoExtension.class)

public class AdminMovieControllerTest {

    @Mock
    private MovieService movieService;
    @Mock
    private ModelMapper mm;
    @InjectMocks
    private AdminMovieController adminMovieController;

    private Movie movie, movie1, movie2, movie3;
    private MovieDto movieDto, movieDto1, MovieDto2;
    List<Movie> movies = new ArrayList<Movie>();
    List<Movie> allMovies = new ArrayList<Movie>();
    List<MovieDto> movieDtos = new ArrayList<MovieDto>();
    List<MovieDto> allMovieDtos = new ArrayList<MovieDto>();
    private MovieDto[] movieDtosList = new MovieDto[3];
    private MovieDto[] allMovieDtosList = new MovieDto[3];

    @BeforeEach
    public void init() {

        movie = new Movie(1, "title", new Short("1"), "category", new Short("4"), "desc", new ArrayList<Screening>());
        movie1 = new Movie(1, "title1", new Short("1"), "category", new Short("4"), "hello", new ArrayList<Screening>());
        movie2 = new Movie(2, "title2", new Short("1"), "category2", new Short("4"), "hi", new ArrayList<Screening>());
        movie3 = new Movie(3, "story", new Short("1"), "category3", new Short("4"), "nice", new ArrayList<Screening>());

        movies.add(movie3);

        allMovies.add(movie1);
        allMovies.add(movie2);
        allMovies.add(movie3);
    }


    @Test
    public void createMovie() {
        Movie movie = allMovies.get(0);
        movieDto = new MovieDto(movie.getId(), movie.getTitle(), movie.getReleaseYear(), movie.getCategory(), movie.getDurationMin(), movie.getDescription());
        when(mm.map(movieDto, Movie.class)).thenReturn(movie);
        when(mm.map(movie, MovieDto.class)).thenReturn(movieDto);
        when(movieService.createMovie(movie)).thenReturn(movie);
        MovieDto savedMovieDto = adminMovieController.createMovie(movieDto);
        assertEquals(movieDto, savedMovieDto);
    }

	@Test
	public void updateMovie(){
		Movie movie1 = allMovies.get(1);
		movieDto1 =  new MovieDto(movie1.getId(), movie1.getTitle(), movie1.getReleaseYear(), movie1.getCategory(), movie1.getDurationMin(), movie1.getDescription());
        when(mm.map(movieDto1, Movie.class)).thenReturn(movie1);
        when(mm.map(movie1, MovieDto.class)).thenReturn(movieDto1);
		when(movieService.updateMovie(movie1, 1)).thenReturn(movie1);
		MovieDto updatedMovieDto = adminMovieController.updateMovie(movieDto1, 1);
		assertEquals(movieDto1, updatedMovieDto);
	}

	@Test
	public void deleteMovie(){
		String deleteResult = "Movie has been successfully deleted.";
		when(movieService.deleteMovie(1)).thenReturn(deleteResult);
		String deleteMovieResult = adminMovieController.deleteMovie(1);
		assertEquals("Movie has been successfully deleted.", deleteMovieResult);
	}

	@Test
	public void findMoviesByTitleContaining(){
		Movie findMovie1 = allMovies.get(2);
		MovieDto findMovieDto1 = new MovieDto(findMovie1.getId(), findMovie1.getTitle(), findMovie1.getReleaseYear(), findMovie1.getCategory(), findMovie1.getDurationMin(), findMovie1.getDescription());
		movieDtosList[0]=findMovieDto1;
        when(mm.map(movies, MovieDto[].class)).thenReturn(movieDtosList);
		when(movieService.findMovieByName("story")).thenReturn(movies);
		List<MovieDto> findMovieDtosByName = Arrays.asList(adminMovieController.findMoviesByTitleContaining("story"));
		assertEquals(3, findMovieDtosByName.size());
	}

	@Test
	public void findMovieById(){
		Movie findById = allMovies.get(1);
		MovieDto movieDtoById = new MovieDto(findById.getId(), findById.getTitle(), findById.getReleaseYear(), findById.getCategory(), findById.getDurationMin(), findById.getDescription());
        when(mm.map(findById, MovieDto.class)).thenReturn(movieDtoById);
		when(movieService.findById(2)).thenReturn(movie2);
		MovieDto findMovieDtoById = adminMovieController.findMovieById(2);
		assertEquals(movieDtoById, findMovieDtoById);

	}

	@Test
	public void findAllMovies(){
		Movie findMovie1 = allMovies.get(0);
		Movie findMovie2 = allMovies.get(1);
		Movie findMovie3 = allMovies.get(2);
		MovieDto findMovieDto1 = new MovieDto(findMovie1.getId(), findMovie1.getTitle(), findMovie1.getReleaseYear(), findMovie1.getCategory(), findMovie1.getDurationMin(), findMovie1.getDescription());
		MovieDto findMovieDto2 = new MovieDto(findMovie2.getId(), findMovie2.getTitle(), findMovie2.getReleaseYear(), findMovie2.getCategory(), findMovie2.getDurationMin(), findMovie2.getDescription());
		MovieDto findMovieDto3 = new MovieDto(findMovie3.getId(), findMovie3.getTitle(), findMovie3.getReleaseYear(), findMovie3.getCategory(), findMovie3.getDurationMin(), findMovie3.getDescription());
		allMovieDtosList[0]=findMovieDto1;
		allMovieDtosList[1]=findMovieDto2;
		allMovieDtosList[2]=findMovieDto3;

        when(mm.map(allMovies, MovieDto[].class)).thenReturn(allMovieDtosList);
		when(movieService.findAllMovies()).thenReturn(allMovies);
		List<MovieDto> findAllMovieDtos = Arrays.asList(adminMovieController.findAllMovies());
		assertEquals(3, findAllMovieDtos.size());
		assertEquals("hi", findAllMovieDtos.get(1).getDescription());
		assertEquals("title1", findAllMovieDtos.get(0).getTitle());
	}
}

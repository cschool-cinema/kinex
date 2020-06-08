package pl.termosteam.kinex.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.dto.MovieDto;
import pl.termosteam.kinex.service.MovieService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"MANAGER"})
public class AdminMovieControllerTest {

	@MockBean
	private MovieService movieService;

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper mapper;
	private List<Movie> allMovies;

	@BeforeEach
	public void init() {
		allMovies = new ArrayList<>();
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		Movie movie = new Movie(1, "title", new Short("1800"),
				"category", new Short("4"), "desc", new ArrayList<>());
		Movie movie1 = new Movie(1, "title1", new Short("1900"),
				"category", new Short("4"), "hello", new ArrayList<>());
		Movie movie2 = new Movie(2, "title2", new Short("1990"),
				"category2", new Short("4"), "hi", new ArrayList<>());
		Movie movie3 = new Movie(3, "story", new Short("1890"),
				"category3", new Short("4"), "nice", new ArrayList<>());
		allMovies.add(movie);
		allMovies.add(movie1);
		allMovies.add(movie2);
		allMovies.add(movie3);
	}

	@Test
	public void createMovie() throws Exception {
		Movie movie = allMovies.get(0);
		when(movieService.createMovie(Mockito.any(Movie.class))).thenReturn(movie);
		MovieDto movieDto = new MovieDto(movie.getId(), movie.getTitle(), movie.getReleaseYear(), movie.getCategory(),
				movie.getDurationMin(), movie.getDescription());

		MvcResult result = mockMvc.perform(post("/api/admin/movie").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(movieDto))).andExpect(status().isOk()).andReturn();

		MovieDto response = mapper.readValue(result.getResponse().getContentAsString(), MovieDto.class);
		Assert.assertEquals(movie.getCategory(), response.getCategory());
		Assert.assertEquals(movie.getDescription(), response.getDescription());
		Assert.assertEquals(movie.getTitle(), response.getTitle());
		Assert.assertEquals(movie.getDurationMin(), response.getDurationMin());
		Assert.assertEquals(movie.getReleaseYear(), response.getReleaseYear());
	}

	@Test
	public void updateMovie() throws Exception {
		Movie movie = allMovies.get(1);
		MovieDto movieDto =  new MovieDto(movie.getId(), movie.getTitle(), movie.getReleaseYear(),
				movie.getCategory(), movie.getDurationMin(), movie.getDescription());
		when(movieService.updateMovie(Mockito.any(Movie.class), Mockito.anyInt())).thenReturn(movie);

		MvcResult result = mockMvc.perform(put("/api/admin/movie/1").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(movieDto))).andExpect(status().isOk()).andReturn();

		MovieDto response = mapper.readValue(result.getResponse().getContentAsString(), MovieDto.class);
		Assert.assertEquals(movie.getCategory(), response.getCategory());
		Assert.assertEquals(movie.getDescription(), response.getDescription());
		Assert.assertEquals(movie.getTitle(), response.getTitle());
		Assert.assertEquals(movie.getDurationMin(), response.getDurationMin());
		Assert.assertEquals(movie.getReleaseYear(), response.getReleaseYear());
	}

	@Test
	public void deleteMovie() throws Exception {
		String deleteResult = "Movie has been successfully deleted.";
		when(movieService.deleteMovie(Mockito.anyInt())).thenReturn(deleteResult);
		mockMvc.perform(delete("/api/admin/movie/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(deleteResult));
	}

	@Test
	public void findMoviesByTitleContaining() throws Exception {
		List<Movie> storyMovies = new ArrayList<>();
		storyMovies.add(allMovies.get(3));
		when(movieService.findMovieByName(Mockito.anyString())).thenReturn(storyMovies);
		MvcResult result = mockMvc.perform(get("/api/admin/movie/search/story").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		MovieDto[] response = mapper.readValue(result.getResponse().getContentAsString(), MovieDto[].class);
		Assert.assertEquals(response.length, 1);
	}

	@Test
	public void findMovieById() throws Exception {
		Movie movie = allMovies.get(1);
		when(movieService.findById(Mockito.anyInt())).thenReturn(movie);
		MvcResult result = mockMvc.perform(get("/api/admin/movie/2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		MovieDto response = mapper.readValue(result.getResponse().getContentAsString(), MovieDto.class);
		Assert.assertEquals(movie.getCategory(), response.getCategory());
		Assert.assertEquals(movie.getDescription(), response.getDescription());
		Assert.assertEquals(movie.getTitle(), response.getTitle());
		Assert.assertEquals(movie.getDurationMin(), response.getDurationMin());
		Assert.assertEquals(movie.getReleaseYear(), response.getReleaseYear());
	}

	@Test
	public void findAllMovies() throws Exception {
		when(movieService.findAllMovies()).thenReturn(allMovies);
		MvcResult result = mockMvc.perform(get("/api/admin/movie").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		MovieDto[] response = mapper.readValue(result.getResponse().getContentAsString(), MovieDto[].class);
		Assert.assertEquals(response.length, allMovies.size());
	}
}
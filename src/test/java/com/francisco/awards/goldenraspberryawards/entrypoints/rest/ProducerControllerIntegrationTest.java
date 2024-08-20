package com.francisco.awards.goldenraspberryawards.entrypoints.rest;

import com.francisco.awards.goldenraspberryawards.application.usecases.ProducerIntervalResponseDTO;
import com.francisco.awards.goldenraspberryawards.infrastructure.adapters.persistence.MovieRepository;

import com.francisco.awards.goldenraspberryawards.application.usecases.ProducerIntervalResponseDTO;
import com.francisco.awards.goldenraspberryawards.domain.entities.Movie;
import com.francisco.awards.goldenraspberryawards.infrastructure.adapters.persistence.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // Ativa o perfil de teste para usar o application-test.yml
public class ProducerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    public void setup() throws Exception {
        // Limpa o banco de dados antes de cada teste
        movieRepository.deleteAll();

        // Carrega o CSV e insere os dados no banco de dados H2
        ClassPathResource resource = new ClassPathResource("movielist.csv");
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Pula a primeira linha (cabeçalho)
                    continue;
                }
                String[] fields = line.split(";");
                if (fields.length >= 5) {
                    Movie movie = new Movie();
                    movie.setMovieYear(Integer.parseInt(fields[0]));
                    movie.setTitle(fields[1]);
                    movie.setStudios(fields[2]);
                    movie.setProducers(fields[3]);
                    movie.setWinner("yes".equalsIgnoreCase(fields[4]));
                    movies.add(movie);
                }
            }
        }
        movieRepository.saveAll(movies);
    }

    @Test
    public void testGetProducerIntervals() {
        String url = "http://localhost:" + port + "/producers/intervals";
        ResponseEntity<ProducerIntervalResponseDTO> response = this.restTemplate.getForEntity(url, ProducerIntervalResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        ProducerIntervalResponseDTO responseBody = response.getBody();
        assertThat(responseBody.getMin()).isNotEmpty();
        assertThat(responseBody.getMax()).isNotEmpty();

        // Verifique se o intervalo mínimo está correto
        assertThat(responseBody.getMin().get(0).getInterval()).isEqualTo(1);
        assertThat(responseBody.getMin().get(0).getProducer()).isNotEmpty();
        assertThat(responseBody.getMin().get(0).getPreviousWin()).isGreaterThan(0);
        assertThat(responseBody.getMin().get(0).getFollowingWin()).isGreaterThan(responseBody.getMin().get(0).getPreviousWin());

        // Verifique se o intervalo máximo está correto
        assertThat(responseBody.getMax().get(0).getInterval()).isGreaterThan(1);
        assertThat(responseBody.getMax().get(0).getProducer()).isNotEmpty();
        assertThat(responseBody.getMax().get(0).getPreviousWin()).isGreaterThan(0);
        assertThat(responseBody.getMax().get(0).getFollowingWin()).isGreaterThan(responseBody.getMax().get(0).getPreviousWin());
    }
}

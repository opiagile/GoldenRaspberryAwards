package com.francisco.awards.goldenraspberryawards.entrypoints.rest;

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

    private ProducerIntervalResponseDTO getProducerIntervalsResponse() {
        String url = "http://localhost:" + port + "/producers/intervals";
        ResponseEntity<ProducerIntervalResponseDTO> response = this.restTemplate.getForEntity(url, ProducerIntervalResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        return response.getBody();
    }

    @Test
    public void testGetProducerIntervals() {
        ProducerIntervalResponseDTO responseBody = getProducerIntervalsResponse();

        // Verifique os dados mínimos
        assertThat(responseBody.getMin()).isNotEmpty();
        assertThat(responseBody.getMin().get(0).getProducer()).isEqualTo("Joel Silver");
        assertThat(responseBody.getMin().get(0).getInterval()).isEqualTo(1);
        assertThat(responseBody.getMin().get(0).getPreviousWin()).isEqualTo(1990);
        assertThat(responseBody.getMin().get(0).getFollowingWin()).isEqualTo(1991);

        // Verifique os dados máximos
        assertThat(responseBody.getMax()).isNotEmpty();
        assertThat(responseBody.getMax().get(0).getProducer()).isEqualTo("Matthew Vaughn");
        assertThat(responseBody.getMax().get(0).getInterval()).isEqualTo(13);
        assertThat(responseBody.getMax().get(0).getPreviousWin()).isEqualTo(2002);
        assertThat(responseBody.getMax().get(0).getFollowingWin()).isEqualTo(2015);
    }

    @Test
    public void testMultipleProducersInSameMovie() {
        ProducerIntervalResponseDTO responseBody = getProducerIntervalsResponse();

        // Verifica se "Joel Silver" e "Matthew Vaughn" têm os intervalos calculados corretamente
        assertThat(responseBody.getMin()).anyMatch(dto ->
                dto.getProducer().equals("Joel Silver") && dto.getInterval() == 1 && dto.getPreviousWin() == 1990 && dto.getFollowingWin() == 1991
        );
        assertThat(responseBody.getMax()).anyMatch(dto ->
                dto.getProducer().equals("Matthew Vaughn") && dto.getInterval() == 13 && dto.getPreviousWin() == 2002 && dto.getFollowingWin() == 2015
        );
    }

    @Test
    public void testSingleWinProducerNotIncludedInIntervals() {
        ProducerIntervalResponseDTO responseBody = getProducerIntervalsResponse();

        // Verifica se "Allan Carr" (que venceu apenas em 1980) não aparece na lista
        assertThat(responseBody.getMin()).noneMatch(dto -> dto.getProducer().equals("Allan Carr"));
        assertThat(responseBody.getMax()).noneMatch(dto -> dto.getProducer().equals("Allan Carr"));
    }

}

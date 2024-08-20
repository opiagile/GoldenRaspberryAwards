package com.francisco.awards.goldenraspberryawards.application.usecases;

import com.francisco.awards.goldenraspberryawards.domain.entities.Movie;
import com.francisco.awards.goldenraspberryawards.infrastructure.adapters.persistence.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProducerService {

    @Autowired
    private MovieRepository movieRepository;

    public List<ProducerIntervalDTO> calculateIntervals() {
        List<Movie> winningMovies = movieRepository.findAll()
                .stream()
                .filter(Movie::isWinner)
                .collect(Collectors.toList());

        List<ProducerIntervalDTO> intervals = new ArrayList<>();

        // Loop de teste iterando sobre os filmes vencedores
        for (int i = 0; i < winningMovies.size(); i++) {
            Movie movie = winningMovies.get(i);

            // Apenas valores de teste - isso deve ser substituído pela lógica real
            intervals.add(new ProducerIntervalDTO(
                    movie.getProducers(),
                    i + 1,
                    movie.getMovieYear(),
                    movie.getMovieYear() + 2
            ));
        }

        return intervals;
    }
}


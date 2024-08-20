package com.francisco.awards.goldenraspberryawards.infrastructure.adapters;

import com.francisco.awards.goldenraspberryawards.domain.entities.Movie;
import com.francisco.awards.goldenraspberryawards.infrastructure.adapters.persistence.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvMovieImporter implements ApplicationRunner{

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/movielist.csv")))) {
            List<Movie> movies = reader.lines()
                    .skip(1) // Pular o cabeçalho
                    .map(line -> {
                        String[] fields = line.split(";");
                        Movie movie = new Movie();
                        movie.setMovieYear(Integer.parseInt(fields[0]));
                        movie.setTitle(fields[1]);
                        movie.setStudios(fields[2]);
                        movie.setProducers(fields[3]);
                        if (fields.length >= 5) {
                            movie.setWinner("yes".equalsIgnoreCase(fields[4]));
                        } else {
                            movie.setWinner(false); // Definir como false se o campo "winner" não estiver presente
                        }
                        return movie;
                    })
                    .collect(Collectors.toList());

            movieRepository.saveAll(movies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

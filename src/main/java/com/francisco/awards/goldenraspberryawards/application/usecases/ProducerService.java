package com.francisco.awards.goldenraspberryawards.application.usecases;

import com.francisco.awards.goldenraspberryawards.domain.entities.Movie;
import com.francisco.awards.goldenraspberryawards.infrastructure.adapters.persistence.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.*;

@Service
public class ProducerService {

    @Autowired
    private MovieRepository movieRepository;

    public ProducerIntervalResponseDTO calculateIntervals() {
        Map<String, List<Integer>> grupoProducer = new HashMap<>();
        List<Movie> listaFilme = movieRepository.findAll()
                .stream()
                .filter(Movie::isWinner)
                .collect(Collectors.toList());

        // Agrupar os anos de vitória por produtor
        for (Movie movie : listaFilme) {
            var splittedProducers = movie.getProducers().split(",\\s*|\\band\\bs*");
            for (String producer : splittedProducers) {
                var trimmedProducer = producer.trim();
                grupoProducer.computeIfAbsent(trimmedProducer, k -> new ArrayList<>()).add(movie.getMovieYear());
            }
        }

        // Montar as listas min e max com base nos intervalos calculados
        var respondeList = new ProducerIntervalResponseDTO();
        respondeList.setMin(montarDados(grupoProducer, false));
        respondeList.setMax(montarDados(grupoProducer, true));

        return respondeList;
    }

    private List<ProducerIntervalDTO> montarDados(Map<String, List<Integer>> grupoProducer, boolean isMax) {
        return grupoProducer.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .flatMap(entry -> {
                    String producer = entry.getKey();
                    List<Integer> years = entry.getValue();
                    Collections.sort(years); // Garantir que os anos estejam ordenados
                    List<ProducerIntervalDTO> dtos = new ArrayList<>();
                    for (int i = 1; i < years.size(); i++) {
                        int interval = years.get(i) - years.get(i - 1);
                        dtos.add(new ProducerIntervalDTO(producer, interval, years.get(i - 1), years.get(i)));
                    }
                    return dtos.stream();
                })
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        dtos -> {
                            dtos.sort(Comparator.comparingInt(ProducerIntervalDTO::getInterval).thenComparingInt(ProducerIntervalDTO::getPreviousWin));
                            if (isMax) {
                                dtos.sort(Comparator.comparingInt(ProducerIntervalDTO::getInterval).reversed());
                            }
                            int targetInterval = dtos.isEmpty() ? 0 : dtos.get(0).getInterval();
                            return dtos.stream()
                                    .filter(dto -> dto.getInterval() == targetInterval)
                                    .collect(Collectors.toList());
                        }
                ));
    }
}

package com.francisco.awards.goldenraspberryawards.entrypoints.rest;

import com.francisco.awards.goldenraspberryawards.application.usecases.ProducerIntervalResponseDTO;
import com.francisco.awards.goldenraspberryawards.application.usecases.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producers")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @GetMapping("/intervals")
    public ResponseEntity<ProducerIntervalResponseDTO> getProducerIntervals() {
        ProducerIntervalResponseDTO response = producerService.calculateIntervals();
        return ResponseEntity.ok(response);
    }
}
package com.francisco.awards.goldenraspberryawards.entrypoints.rest;

import com.francisco.awards.goldenraspberryawards.application.usecases.ProducerIntervalDTO;
import com.francisco.awards.goldenraspberryawards.application.usecases.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/producers")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @GetMapping("/intervals")
    public ResponseEntity<List<ProducerIntervalDTO>> getProducerIntervals() {
        List<ProducerIntervalDTO> intervals = producerService.calculateIntervals();
        return ResponseEntity.ok(intervals);
    }
}

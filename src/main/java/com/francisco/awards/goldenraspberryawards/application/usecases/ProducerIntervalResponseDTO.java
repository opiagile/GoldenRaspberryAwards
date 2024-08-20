package com.francisco.awards.goldenraspberryawards.application.usecases;

import java.util.List;

public class ProducerIntervalResponseDTO {

    private List<ProducerIntervalDTO> min;
    private List<ProducerIntervalDTO> max;

    public ProducerIntervalResponseDTO() {
        this.min = min;
        this.max = max;
    }

    // Getters e Setters

    public List<ProducerIntervalDTO> getMin() {
        return min;
    }

    public void setMin(List<ProducerIntervalDTO> min) {
        this.min = min;
    }

    public List<ProducerIntervalDTO> getMax() {
        return max;
    }

    public void setMax(List<ProducerIntervalDTO> max) {
        this.max = max;
    }
}


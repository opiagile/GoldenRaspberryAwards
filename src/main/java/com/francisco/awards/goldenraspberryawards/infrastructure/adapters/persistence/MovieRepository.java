package com.francisco.awards.goldenraspberryawards.infrastructure.adapters.persistence;

import com.francisco.awards.goldenraspberryawards.domain.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}


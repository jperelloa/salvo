package edu.example;

/**
 * Created by jpere on 02/01/2017.
 */

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameScoreRepository extends JpaRepository<GameScore, Long> {
}

package edu.example;

/**
 * Created by jpere on 12/12/2016.
 */
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
        List<GamePlayer> findById(Long id);
    }



/**
 * Created by jpere on 07/12/2016.
 */
package edu.example;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
    public interface PlayerRepository extends JpaRepository<Player, Long> {
        List<Player> findByUserName(String userName);
    }


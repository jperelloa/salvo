

/**
 * Created by jpere on 07/12/2016.
 */
package edu.example;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface PlayerRepository extends JpaRepository<Player, Long> {
        List<Player> findByUserName(String userName);
    }


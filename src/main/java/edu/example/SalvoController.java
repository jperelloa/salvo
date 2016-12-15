package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jpere on 14/12/2016.
 */

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository grepo;

    @RequestMapping("/games")
    public List<Object> getAll() {
        return  grepo
                .findAll()
                .stream()
                .map (game -> makeGameDTO(game))
                .collect(Collectors.toList());
    }

         private Map<String, Object> makeGameDTO(Game game) {
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("id", game.getId());
            dto.put("creationDate", game.getCreationDate());
            return dto;
        }
    /*@RequestMapping("/games")
    public List<Game> getAll() {
        return grepo.findAll();
    }*/
}
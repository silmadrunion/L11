package com.example.l11;

import gomoku.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController

@RequestMapping("/")
public class Controller {

    private final List<Player> playersList = new ArrayList<>();

    private Player findByID(int id) //function is temporary until I sit down and setup a database
    {
        for (Player p:playersList
             ) {
            if(p.getId()==id) return p;
        }

        return null;
    }


    @GetMapping
    public List<Player> getPlayersList()
    {
        return playersList;
    }

    @PostMapping
    public int createPlayer(@RequestParam String name)
    {
        int id=playersList.size()+1;
        playersList.add(new Player(name, id));
        return id;
    }

    @PutMapping
    public ResponseEntity<String> updatePlayer(@PathVariable int id, @RequestParam String name)
    {
        Player player = findByID(id); //Needs to be implemented
        if(player == null)
            return new ResponseEntity<>("Player with this ID not found", HttpStatus.NOT_FOUND);
        player.setPlayerName(name);
        return new ResponseEntity<>("Player name updated!", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deletePlayer(@PathVariable int id)
    {
        Player player = findByID(id); //Needs to be implemented
        if(player == null)
            return new ResponseEntity<>("Player with this ID not found", HttpStatus.NOT_FOUND);
        playersList.remove(player);
        return new ResponseEntity<>("Player removed!", HttpStatus.OK);
    }
}

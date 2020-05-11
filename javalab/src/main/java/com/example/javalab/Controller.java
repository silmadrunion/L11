package com.example.javalab;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@RestController
public class Controller {

    @GetMapping("/test")
public String test()
    {
        return "haha";
    } //test route originally implemented to see if the routes work, left here for posterity's sake

    @GetMapping("/getPlayers")
    public List<Player> getPlayers()
    {
        MongoDatabase database = JavalabApplication.mongoClient.getDatabase("test"); //note that mongoClient is a global static, so we can use it here
        MongoCollection<Document> collection = database.getCollection("Players");

        FindIterable<Document> iter = collection.find(); //grab all the elements in Players

        List<Player> playerList = new ArrayList<>();

        Iterator it = iter.iterator();
        while(it.hasNext()) //iterate through those elements
        {
            //System.out.println(it.next().toString());
            playerList.add(new Player(it.next().toString())); //pass the json-style strings through a special constructor
        }

        return playerList; //note that when turned to Json, the mongo ObjectId turns into a full explanation of timestamp, counter, and so on; this is done by Spring boot's Jackson automatically (I think), and it's not *that* bad
    }

    @PostMapping("/createPlayer")
    public ResponseEntity<String> createPlayer(@RequestBody Player player) //SpringBoot has Jackson convert json strings into POJOs by default, which is great
    {
        Player newPlayer = new Player(player);
        if(newPlayer == null)
        {
            throw new CustomException("EXCEPTION! COULD NOT CREATE PLAYER!"); //I doubt this'll ever happen, but it was a check that had to be made and might as well put the custom exception ere
        }

        MongoDatabase database = JavalabApplication.mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("Players");

        Document tObject = newPlayer.toMongoDocument(); //convertor function that returns a mongo Document

        collection.insertOne(tObject);

        return new ResponseEntity<>("Player created!", HttpStatus.OK);
    }

    @PutMapping("/updatePlayer/{id}")
    public ResponseEntity<String> updatePlayer(@RequestBody Player player, @PathVariable String id) //path variable String works just as expected
    {
        MongoDatabase database = JavalabApplication.mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("Players");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id)); //this is technically a superflous line, but it makes it obvious what the query parameter actually is
        if(!collection.find(query).cursor().hasNext()) return new ResponseEntity<>("Player with that object id not found", HttpStatus.NOT_FOUND);

        //note that objects are searched by ObjectId and not playerId;

        collection.updateOne(eq("_id", new ObjectId(id)), new Document("$set", new Document("name", player.getplayerName()).append("id", player.getPlayerId())));
        return new ResponseEntity<>("Player updated!", HttpStatus.OK);
    }

    @DeleteMapping("/deletePlayer/{id}")
    public ResponseEntity<String> removePlayer(@PathVariable String id) //this function works indentical to the one above, just simpler
    {
        MongoDatabase database = JavalabApplication.mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("Players");

        if(!ObjectId.isValid(id))
        {
            throw new CustomException("Invalid object Id:" + id); //This error will actually happen if id is an invalid string, i.e. could never be an ObjectId
        }

        if(!collection.find(eq("_id", new ObjectId(id))).cursor().hasNext()) return new ResponseEntity<>("Player with that object id not found", HttpStatus.NOT_FOUND);

        collection.deleteOne(eq("_id", new ObjectId(id)));
        return new ResponseEntity<>("Player removed!", HttpStatus.OK);
    }

    @GetMapping("/getGames")
    public List<Game> getGames() //identical implementation to the Players get route
    {
        MongoDatabase database = JavalabApplication.mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("Games");

        List<Document> list = new ArrayList<>();
        FindIterable<Document> iter = collection.find();

        List<Game> gameList = new ArrayList<>();

        Iterator it = iter.iterator();
        while (it.hasNext()) {
            //System.out.println(it.next().toString());
            gameList.add(new Game(it.next().toString())); //custom constructor, like with Playe
        }

        return gameList; //same note about the ObjectId when turned to Json as with the player Get
    }


        @PostMapping("/createGame")
        public ResponseEntity<String> createGame(@ModelAttribute Game game) //same point about Jackson as with player Post
        {
            Game newGame = new Game(game);
            if(newGame == null) return new ResponseEntity<>("Player could not be created", HttpStatus.NOT_FOUND);

            MongoDatabase database = JavalabApplication.mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("Players");

            Document tObject = newGame.toMongoDocument(); //conversion function

            collection.insertOne(tObject);

            return new ResponseEntity<>("Game created!", HttpStatus.OK);
        }
}

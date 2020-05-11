package com.example.javalab;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import static com.example.javalab.JavalabApplication.mongoClient;
import static com.mongodb.client.model.Filters.eq;

public class Game {

    public Game(Player blackPlayer, Player whitePlayer, String gameContent, Date gameDate, boolean blackWon) { //classic constructor
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.gameContent = gameContent;
        this.gameDate = gameDate;
        this.blackWon = blackWon;
    }

    public Game(Game game) { //copy constructor
        this.blackPlayer = game.blackPlayer;
        this.whitePlayer = game.whitePlayer;
        this.gameContent = game.gameContent;
        this.gameDate = game.gameDate;
        this.blackWon = game.blackWon;
    }

    public Game(String mongoString) //This solution is pretty hacky, but I think it's better than using external libraries for the purposes of a java lab
    {
        StringTokenizer tokenizer = new StringTokenizer(mongoString, ",}");

        String text = tokenizer.nextToken();
        id = new ObjectId(text.substring(text.lastIndexOf("_id=")+4));

        Player player = new Player(0, null);

        text = tokenizer.nextToken();
        player.setId(new ObjectId(text.substring(text.lastIndexOf("_id=")+4)));

        text = tokenizer.nextToken();
        player.setPlayerId(Integer.parseInt(text.substring(text.lastIndexOf("id=")+3)));

        text = tokenizer.nextToken();
        player.setplayerName(text.substring(text.lastIndexOf("name=")+5));

        blackPlayer = new Player(player);

        text = tokenizer.nextToken();
        System.out.println(text);
        player.setId(new ObjectId(text.substring(text.lastIndexOf("_id=")+4)));

        text = tokenizer.nextToken();
        player.setPlayerId(Integer.parseInt(text.substring(text.lastIndexOf("id=")+3)));

        text = tokenizer.nextToken();
        player.setplayerName(text.substring(text.lastIndexOf("name=")+5));

        whitePlayer = new Player(player);

        text = tokenizer.nextToken();
        gameContent = text.substring(text.lastIndexOf("gameContent=")+12);

        text = tokenizer.nextToken();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            gameDate = format.parse(text.substring(text.lastIndexOf("gameDate=")+9));
        } catch (ParseException e) {
            e.printStackTrace(); //parser exception, apparently
        }

        text = tokenizer.nextToken();
        if(text.lastIndexOf("true")!=-1) blackWon=true;
        else blackWon=false;

        //same comment as in player, this mght be ugly, it might not be good, but it's MINE and I love it
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public String getGameContent() {
        return gameContent;
    }

    public void setGameContent(String gameContent) {
        this.gameContent = gameContent;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public boolean isBlackWon() {
        return blackWon;
    }

    public void setBlackWon(boolean blackWon) {
        this.blackWon = blackWon;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    private ObjectId id; //the mongoDB id
    private Player blackPlayer; //Player objects for storing player data
    private Player whitePlayer;
    private String gameContent; //Content, which is a string theoretically formatted in SGF, practically just a string
    private Date gameDate; //Date object because why not
    private boolean blackWon; //I loved this boolean back in Gomoku, I love it here

    public Document toMongoDocument()
    {

        //conversion function
        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Document> collection = database.getCollection("Players"); //note that this time it actually has to connect to database, because it needs to grab the two players

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Document doc = new Document("blackPlayer", collection.find(eq("_id", blackPlayer.getId())).cursor().next())
                    .append("whitePlayer", collection.find(eq("_id", whitePlayer.getId())).cursor().next())
                    .append("gameContent", gameContent)
                    .append("gameDate", format.format(gameDate))
                    .append("blackWon", blackWon);

        return doc;
    }
}

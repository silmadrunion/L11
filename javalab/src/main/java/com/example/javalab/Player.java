package com.example.javalab;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.StringTokenizer;

public class Player {


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    private ObjectId id; //this is the MongoDB ID

    private int playerId; //our own "id"

    private String playerName; //name

    public Player(int playerId, String name)
    {
        this.playerId = playerId;
        this.playerName = name;
    }

    public Player(String mongoString) //This solution is pretty hacky, but I think it's better than using external libraries for the purposes of a java lab
    {
        StringTokenizer tokenizer = new StringTokenizer(mongoString, ",}"); //tokenizer by how a json string is formatter

        String text = tokenizer.nextToken();
        id = new ObjectId(text.substring(text.lastIndexOf("_id=")+4));

        text = tokenizer.nextToken();
        playerId = Integer.parseInt(text.substring(text.lastIndexOf("id=")+3));

        text = tokenizer.nextToken();
        playerName = text.substring(text.lastIndexOf("name=")+5);

        //This function is, in essence, no different from what a library would do for me; it might not be as efficient, but at least it's my own
    }

    public Player(Player player) //copy constructor
    {
        this.id = player.id;
        this.playerId = player.playerId;
        this.playerName = player.playerName;

    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int id) {
        this.playerId = playerId;
    }

    public String getplayerName() {
        return playerName;
    }

    public void setplayerName(String playerName) {
        this.playerName = playerName;
    }

    public Document toMongoDocument()
    {
        //conversion function
        Document doc = new Document("id", playerId).append("name", playerName); //Note that ObjectId is NOT written in this, because it would break mongo's internal id setups

        return doc;
    }
}
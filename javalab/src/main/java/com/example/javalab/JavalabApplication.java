package com.example.javalab;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@SpringBootApplication
public class JavalabApplication {
    public static MongoClient mongoClient;


    public static void main(String[] args) {

        mongoClient = new MongoClient("localhost", 27017);  //First off, this is the only actually required DB function, the rest are here to have some "default" db items
        MongoDatabase database = mongoClient.getDatabase("test");

        MongoCollection<Document> collection = database.getCollection("Players");
        collection.drop();
        Document tObject = new Document("id", "0").append("name", "name1");

        collection.insertOne(tObject);
        collection.insertOne(new Document("id", "1").append("name", "name2"));

        FindIterable<Document> iter = collection.find();

        Iterator it = iter.iterator();
        while(it.hasNext())
        {
            System.out.println(it.next()); //after having created the two dummy players, print them so we can make sure they exist
        }

        MongoCollection<Document> gameColl = database.getCollection("Games");
        gameColl.drop(); //note that we drop a collection first thing so that the database does not change between executions
        //Under normal circumstances, this obviously should not be here, but for testing purposes it's great

        gameColl.insertOne(new Document("blackPlayer", collection.find(eq("id", "0")).cursor().next())
                            .append("whitePlayer", collection.find(eq("id", "1")).cursor().next())
                            .append("gameContent", "DUMMY CONTENT GOES HERE")
                            .append("gameDate", "2020-04-20")
                            .append("blackWon", true)
        );
        gameColl.insertOne(new Document("blackPlayer", collection.find(eq("id", "1")).cursor().next())
                .append("whitePlayer", collection.find(eq("id", "0")).cursor().next())
                .append("gameContent", "DUMMY CONTENT GOES HERE TOO!")
                .append("gameDate", "1889-04-20")
                .append("blackWon", false)
        );

        iter = gameColl.find();
        it = iter.iterator();
        MongoCursor<Document> curs = iter.cursor();
        while(curs.hasNext())
        {
            System.out.println(curs.next().toJson()); //same as with players, two dummy objects
        }


        SpringApplication.run(JavalabApplication.class, args); //And the spring boot proper
    }

}

package DB;
import Indexer.Indexer;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.Set;

public class MongoDB {
    MongoClient mc;
    MongoDatabase db;
    //private static DBCollection IndexCollection;
    MongoCollection<Document> IndexCollection;
    MongoCollection<Document> RULTitles;
    public MongoDB(String db_Name) {
        try {
            ConnectionString Con_string = new ConnectionString("mongodb://localhost:27017");
            MongoClientSettings mon_settings = MongoClientSettings.builder().applyConnectionString(Con_string).
                    retryWrites(true).build();
            mc = MongoClients.create(mon_settings);
            db = mc.getDatabase(db_Name);

        }catch(Exception e){}
        IndexCollection =  db.getCollection("Indexer");
        RULTitles = db.getCollection("URLTitle");
        //org.bson.Document abbas = new Document("Abbas" , "Etman");
        //IndexCollection.insertOne(abbas);
    }

    public void AddWordURLs(String word, Set<String> urls){
        org.bson.Document WordIndex = new Document("Word",word).append("URLs",urls);
        IndexCollection.insertOne(WordIndex);
    }
    public void AddWordIndex(String word, String URL, ArrayList<Integer> pos){
        org.bson.Document WordIndex = new Document("Word" , word);
        BasicDBObject URL_Pos = new BasicDBObject();
        URL_Pos.put("URL",URL);
        URL_Pos.put("Positions",pos);
        WordIndex.append("URL_Pos",URL_Pos);
        IndexCollection.insertOne(WordIndex);
    }

    public void AddTitle (String URL, String Title){
        org.bson.Document URLTitle = new Document("URL" , URL).append("Title",Title);
        RULTitles.insertOne(URLTitle);
    }

}
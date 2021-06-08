import Indexer.Indexer;
import crawler.CrawlerMain;
import DB.MongoDB;

import java.util.ArrayList;
public class Main {
    public static void main(String [] args){
        try {
           CrawlerMain.main(args);
          Indexer Abbas = new Indexer();
          Abbas.ProcessAllFiles();
         Abbas.WriteOnDB();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
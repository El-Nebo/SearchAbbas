import Indexer.Indexer;
import crawler.CrawlerMain;

public class Main {
    public static void main(String [] args){
        try {
            //CrawlerMain.main(args);

            Indexer Abbas = new Indexer();
            Abbas.ProcessAllFiles();
            Abbas.Print();
            System.out.println("ABBBB "+ Abbas.WordMap.size());
        }
        catch(Exception e){
               e.printStackTrace();
        }

    }
}

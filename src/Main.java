import Indexer.Indexer;
import crawler.CrawlerMain;

public class Main {
    public static void main(String [] args){
        try {
            //CrawlerMain.main(args);
            //Indexer.ProcessHTMLFile("Htmls/112.html");

            Indexer Abbas = new Indexer();
            Abbas.ProcessHTMLFile("119.html");
//            Abbas.ProcessAllFiles();
//            Abbas.Print();
//            System.out.println("ABBBB "+ Abbas.WordMap.size());
        }
        catch(Exception e){
               e.printStackTrace();
        }

    }
}

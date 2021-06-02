package crawler;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.LinkedList;
import java.util.Queue;
public class Crawler {

    private HashSet<String> links;

    public Crawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL,Queue<String> q) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
                    System.out.println(URL);
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    q.add(page.attr("abs:href"));
                    //getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }



    public static void main(String[] args) throws IOException {
        Queue<String> q = new LinkedList();
        int MAXVISITS = 5000,NumVisited = 0;
        q.add("http://www.mkyong.com/");//whiel loop and insert all links in the queue
        q.add("http://www.mkyong.com/");
        q.add("http://www.mkyong.com/");
        q.add("http://www.github.com/");
        q.add("http://www.codeforces.com/");
        Crawler crw = new Crawler();
        while(q.size()>0 && NumVisited < MAXVISITS){

        crw.getPageLinks(q.remove());
            NumVisited++;
        }
        try{
            Class.forName("com.mysql.jdbc.Driver");

            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/search_engine?characterEncoding=latin1&useConfigs=maxPerformance","root","01149873532");

//here sonoo is database name, root is username and password
            Statement stmt=con.createStatement();

            ResultSet rs=stmt.executeQuery("select * from emp");

            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2));
            con.close();
        }catch(Exception e){ System.out.println(e);}




        String webPage = "http://webcode.me";

        String html = Jsoup.connect(webPage).get().html();

        // System.out.println(html);
    }
}
package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;

import java.sql.*;

public class CrawlerThread extends Thread {
    public static HashSet<String> links;
    public final int MAXVISITS = 200;
    public static LinkedList<String> q;
    public static Connection con;
    public static void init() {
       ConnectToMySql();
        links = new HashSet<String>();
        q = new LinkedList<String>();
    }

    @Override
    public void run() {

        try {

            while (links.size() < MAXVISITS) {

                synchronized (q) {
                    while (q.isEmpty())
                        q.wait();
                }
                String URL = q.poll();

                // 2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                // 3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                for (Element page : linksOnPage) {
                    String newUrl = page.attr("abs:href");
                    boolean createFile = false;
                    int fileNum = 0;

                    synchronized (q) {
                        if (!links.contains(newUrl) && links.size() < MAXVISITS) {
                            links.add(newUrl);
                            q.add(newUrl);
                            //String query = "INSERT INTO urls " + "value ( ' " +newUrl+ " ' );" ;
                            Statement stmt=con.createStatement();

                            // stmt.executeUpdate(query);
                            createFile = true;
                            fileNum = links.size();

                            q.notify(); // wake up only one thread as we only added one new link
                        }
                    }

                    if (createFile) {
                        // Create the file
                        File myfile = new File("Htmls/" + fileNum + ".html");
                        myfile.createNewFile();

                        // Write to the File
                        FileWriter fileWriter = new FileWriter(myfile);
                        try {
                            fileWriter.write(Jsoup.connect(newUrl).get().html());
                        } catch (Exception e) { // invalid url lead to empty file

                        }
                        fileWriter.close();

                    }

                }

            }
            // public void getPageLinks(String URL,Queue<String> q) {
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public static void ConnectToMySql(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/search_engine?characterEncoding=latin1&useConfigs=maxPerformance","root","01149873532");
            Statement stmt=con.createStatement();
        }catch(Exception e){ System.out.println(e);}
    }
}

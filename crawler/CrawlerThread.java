package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;

public class CrawlerThread extends Thread {
    public static HashSet<String> links;
    public final int MAXVISITS = 200;
    public static LinkedList<String> q;

    public static void init() {
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
                        } catch (org.jsoup.HttpStatusException e) { // invalid url lead to empty file
                        
                        }
                        fileWriter.close();

                    }

                }

            }
            // public void getPageLinks(String URL,Queue<String> q) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 4. Check if you have already crawled the URLs
        // (we are intentionally not checking for duplicate content in this example)

        // while(q.size()>0 && NumVisited < MAXVISITS){
        // URL=q.remove();
        // }

        // if (!links.contains(URL)) {

        // NumVisited++;
        // try {
        // //4. (i) If not add it to the index
        // if (links.add(URL)) {
        // System.out.println(URL);
        // }

        // //2. Fetch the HTML code
        // Document document = Jsoup.connect(URL).get();
        // //3. Parse the HTML to extract links to other URLs
        // Elements linksOnPage = document.select("a[href]");

        // //5. For each extracted URL... go back to Step 4.
        // for (Element page : linksOnPage) {
        // q.add(page.attr("abs:href"));
        // // getPageLinks(page.attr("abs:href"));
        // }
        // } catch (IOException e) {
        // System.err.println("For '" + URL + "': " + e.getMessage());
        // }
        // }

    }
}

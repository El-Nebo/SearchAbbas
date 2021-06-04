package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.sql.*;
import java.util.Scanner;

public class CrawlerThread extends Thread {
    public static HashSet<String> links;
    public static HashSet<String> Saved_hosts;
    public final int MAXVISITS = 250;
    public static LinkedList<String> q;
    public static Connection con;
  //  public static BufferedWriter bw1 , bw2;
    //public static FileWriter fw1 , fw2;
  public static  File f1 , f2;
    public static void init() {
        //ConnectToMySql();
        try {
            links = new HashSet<String>();
            q = new LinkedList<String>();
            // load visited links
            File myObj = new File("visited.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                links.add(data);
            }
            myReader.close();
            // load queue state
            File myObj2 = new File("queue.txt");
            Scanner myReader2 = new Scanner(myObj2);
            while (myReader2.hasNextLine()) {
                String data = myReader2.nextLine();
                if (!links.contains(data))
                     q.add(data);
            }
            myReader.close();
            ///////////////////////////////////////////////////////////////////
            f1 = new File("visited.txt");
            if(!f1.exists()) {
                f1.createNewFile();
            }
            f2 = new File("queue.txt");
            if(!f2.exists()) {
                f2.createNewFile();
            }
            //fw1 = new FileWriter(f1.getName(),true);
            //fw2 = new FileWriter(f2.getName(),true);
          //  bw1 = new BufferedWriter(fw1);
          //  bw2 = new BufferedWriter(fw2);
        }catch (IOException  e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            while (links.size() < MAXVISITS) {

                synchronized (q) {
                    while (q.isEmpty())
                        q.wait();
                }
                ////////////////// 1 ) msekt elly 3lih eldor
                String url = q.poll();



               // if (!links.contains(url)){
                int num = 0;
                Boolean test = true;
                synchronized (links) {


                    if (!links.contains(url)) {
                        links.add(url);
                        try {
                            FileWriter fw1 = new FileWriter(f1.getName(),true);
                            BufferedWriter bw1 = new BufferedWriter(fw1);
                            bw1.write(url + "\n");
                            bw1.close();
                        }
                        catch (Exception e){

                        }
                        num = links.size();

                        System.out.println(num);
                    }else{
                        test = false;
                    }
                }
                if (test){
                    try {
                        //Boolean check = CheckRobotTxt(url);
                    }
                    catch (Exception m){}


                    try {
                        //////////////////// 2) tl3t el links elly fih
                        Document document = Jsoup.connect(url).get();
                        Elements linksOnPage = document.select("a[href]");




                    ////////////////// 3) 3mltelhom save f el queue
                    for (Element page : linksOnPage) {
                        String newUrl = page.attr("abs:href");

                        synchronized (q){
                            q.add(newUrl);
                            try {
                                FileWriter fw2 = new FileWriter(f2.getName(),true);
                                BufferedWriter bw2 = new BufferedWriter(fw2);
                                bw2.write(newUrl + "\n");
                                bw2.close();
                            }
                            catch (IOException e){

                            }
                            q.notifyAll();
                        }
                    }
                    } catch (Exception e){}

                    //////////////////////////// 4) ba save el Html to a file
                    SaveHtmlToFile(url, num);



                    ////////////////////////// the end
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void CheckRobotTxt (String url) throws MalformedURLException {
        try {
            //System.out.println(url);
            URL my_url = new URL(url);
            String s = my_url.getHost();
            if (! Saved_hosts.contains(s)){
                Saved_hosts.add(s);
            }
            else{
                return ;
            }
            String pr = my_url.getProtocol();

            //
            s = pr+"://" + s + "/robots.txt";
           // System.out.println(s);
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(new URL(s).openStream()))) {
                String line = null;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
            //    e.printStackTrace();
            }
        }catch (Exception e){

        }
        return ;
    }







    public static void ConnectToMySql(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/search_engine?characterEncoding=latin1&useConfigs=maxPerformance","root","01149873532");
            Statement stmt=con.createStatement();
        }catch(Exception e){ System.out.println(e);}
    }
    public static void end() {
        //ConnectToMySql();
       // try {
            // fw1.close();
            // fw2.close();
         //   bw1.close();
           // bw2.close();
       // }catch (IOException e){
         //   e.printStackTrace();
        //}
    }

    public static void SaveHtmlToFile (String url , int fileNum){
        try {
            File myfile = new File("Htmls/" + fileNum + ".html");
            myfile.createNewFile();

            // Write to the File
            FileWriter fileWriter = new FileWriter(myfile);
            fileWriter.write(Jsoup.connect(url).get().html());
            fileWriter.close();
        }catch (Exception e){}
    }
}

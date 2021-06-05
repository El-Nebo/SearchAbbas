package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;

import java.util.Scanner;

public class CrawlerThread extends Thread {
    public static HashSet<String> links;
    public static HashSet<String> cont_disallow;
    public static HashSet<String> Saved_hosts;
    public static Map<String, List<String>> Disallowed_links = new HashMap<String, List<String>>();
    public final int MAXVISITS = 5000;
    public static LinkedList<String> q;
    public static Connection con;
    public static BufferedWriter bw1 , bw2;
    public static FileWriter fw1 , fw2;
    public static  File f1 , f2;
    public static void init() {
        try {
            links = new HashSet<String>();
            Saved_hosts = new HashSet<String>();
            cont_disallow = new HashSet<String>();
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
            fw1 = new FileWriter(f1.getName(),true);
            fw2 = new FileWriter(f2.getName(),true);
            bw1 = new BufferedWriter(fw1);
            bw2 = new BufferedWriter(fw2);
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



                int num = 0;
                Boolean test = true;
                synchronized (links) {


                    if (!links.contains(url) && !cont_disallow.contains(url)) {
                        Boolean disallowed = false;
                        try {
                            // System.out.println("hhhhhhhhhhhhhhhhhhhhhhh");
                           GetRobotTxt(url);
                            disallowed= CheckRobotTxt (url);
                            if (disallowed){
                                cont_disallow.add(url);
                                continue;
                            }
                        }
                        catch (Exception m){}
                        links.add(url);
                        try {
                            bw1.write(url + "\n");

                            bw1.flush();
                        }
                        catch (Exception e){

                        }
                        num = links.size();

                      // System.out.println(num);
                    }else{
                        test = false;
                    }
                }
                if (test){



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

                                  bw2.write(newUrl + "\n");
                                    bw2.flush();
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


    void GetRobotTxt (String url) throws MalformedURLException {
        try {
            if (url == null){
                return;
            }
            URL my_url = new URL(url);
            String h = my_url.getHost();
           if (! Saved_hosts.contains(h)){
                Saved_hosts.add(h);
            }
            else{
               return ;
            }
            String pr = my_url.getProtocol();

            //
            String s = pr+"://" + h + "/robots.txt";
            List<String> temp = new ArrayList<String>();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(new URL(s).openStream()))) {
                String line = null;
                while ((line = in.readLine()) != null) {
                    String dis = line.substring(0, Math.min(line.length(), 8));
                    if (dis.equals("Disallow")){
                        String d = pr+"://"+h+line.substring(10,line.length()) ;
                        temp.add(d);
                    }
                }
                Disallowed_links.put(h, temp);
            } catch (IOException e) {
                //    e.printStackTrace();
            }
        }catch (Exception e){
           // System.out.println(e);
        }
       // return ;
    }

    Boolean CheckRobotTxt (String url) throws MalformedURLException {
        URL my_url = new URL(url);
        String h = my_url.getHost();
        List<String> temp = new ArrayList<String>();
        temp = Disallowed_links.get(h);
        for (int i = 0 ; i < temp.size() ; i++){
            if (url.contains(temp.get(i))){
                System.out.println("Mamnoooooo3"+" "+url);
                return true;
            }
        }
        return false;
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
         try {
        // fw1.close();
        // fw2.close();
           bw1.close();
           bw2.close();
         }catch (IOException e){
           e.printStackTrace();
        }
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

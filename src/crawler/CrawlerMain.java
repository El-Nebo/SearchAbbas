package crawler;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.Scanner;
import java.sql.*;
public class CrawlerMain {
    static int ThreadsNum;
    public static void main(String[] args)throws Exception
    {
        //ConnectToMySql ();
        System.out.println("Start CrawlerThread");
        //Initialize Crawler Parameters
        CrawlerThread.init();

        //get ThreadsNumber
        System.out.println("Please Enter Threads Number :");
        Scanner scanner=new Scanner(System.in);
        ThreadsNum=scanner.nextInt();
        scanner.close();


        // Add Some Seeds
        CrawlerThread.q.add("https://en.wikipedia.org/wiki/");
        CrawlerThread.q.add("https://www.geeksforgeeks.org/");
        CrawlerThread.q.add("https://www.w3schools.com/");

        //Create Threads
        CrawlerThread [] threads=new CrawlerThread[ThreadsNum];
        for(int i = 0; i<ThreadsNum ;i++)
        {

            threads[i]=new CrawlerThread();
            threads[i].start();
        }
        for(int i = 0; i<ThreadsNum ;i++)
        {
            threads[i].join();
            System.out.println("Thread number "+i +" Finished");
        }

        //finised crawling
        int count=CrawlerThread.links.size();
       /* for (String url : CrawlerThread.links) {
            System.out.println(url);
        }*/
        System.out.println("Number of files:" +count);
        CrawlerThread.end();
    }
}

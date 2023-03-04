package crawler;
import java.util.Scanner;
import java.sql.*;
public class CrawlerMain {
    static int ThreadsNum;
    public static void main(String[] args)throws Exception
    {
        System.out.println("Start CrawlerThread");
        //Initialize Crawler Parameters
        CrawlerThread.init();

        //get ThreadsNumber
        System.out.println("Please Enter Threads Number :");
        Scanner scanner=new Scanner(System.in);
        ThreadsNum=scanner.nextInt();
        scanner.close();


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

        int count=CrawlerThread.links.size();
        System.out.println("Number of files:" +count);
        CrawlerThread.end();
    }
}

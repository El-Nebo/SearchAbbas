package indexer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

//import com.mongodb.MongoClient;
//import com.mongodb.client.AggregateIterable;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.*;

import java.util.Scanner;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.file.DirectoryStream;

import java.util.*;

public class Indexer {
    public  HashMap<String, HashMap<String, ArrayList<Integer>>> WordMap;

    ///////////////////DataBase//////
    public static final String DATABASE_NAME = "search_engine";
    public static final String DATABASE_HOST_ADDRESS = "localhost";
    public static final int DATABASE_PORT_NUMBER = 27017;

    public static final int Num = 5000;
    ArrayList<String> URLs;

   // MongoClient mongoClient;
    // MongoDatabase database;
    ///////////////////////////////
    public void Print() {
        WordMap.forEach(this::BBB);
    }

    void BBB(String a , HashMap<String,ArrayList<Integer>> b) {
        System.out.print("Word : "+ a + "   ");
        b.forEach(this::CCC);
    }
    void CCC(String a ,ArrayList<Integer> b ) {
        System.out.print("URL :  " + a + "   ");
        for (Integer integer : b) System.out.print(integer + "  ");
        System.out.println("");
    }

    public Indexer(){
        WordMap = new HashMap<String, HashMap<String, ArrayList<Integer>>>();


//        mongoClient = new MongoClient( DATABASE_HOST_ADDRESS,DATABASE_PORT_NUMBER);
//
//        database = mongoClient.getDatabase(DATABASE_NAME);


        //MongoDatabase database = mongoClient.getDatabase(Constants.DATABASE_NAME);


        URLs = new ArrayList<String>(Num);
        File visited = new File("visited.txt");
        Scanner myReader = null;
        try {
            myReader = new Scanner(visited);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                URLs.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String[] ProcessHTMLFile(String path) {
        String FileString = "";
        try {
            //  System.out.println("hhhhhhhhh");
            path = "Htmls/"+path;
            FileString = Files.readString(Paths.get(path));
            //System.out.println(FileString);
        }catch(IOException e){
        }
        return String_Parser(FileString);
    }

    public String[] String_Parser(String HTMLSTring){
        Document html = Jsoup.parse(HTMLSTring);
        String title = html.title();
        String ss = title+ html.body().text() ;
        System.out.println("After parsing, words : " + ss);
        String [] words = ss.split(" ");
        return words;
    }


    public void ProcessAllFiles(){
        Path path = Paths.get("Htmls/");
        try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
            int idx;
            for (Path file : listing) {
                if (file.getFileName().toString().toLowerCase()
                        .endsWith(".html")) {
                    String[] Words = ProcessHTMLFile(file.getFileName().toString());
                    String filename = file.getFileName().toString();
                    idx = Integer.parseInt(filename.substring(0,filename.length()-5));
                    IndexArray(Words,URLs.get(idx-1));
                }
            }
        }
        catch(IOException e){

        }
    }

    void IndexArray(String[] Words, String URL){
        int position = 1;
        for(String str: Words){
            IndexWord(str,URL,position++);
        }
    }

    void IndexWord(String word, String URL, int position) {
        WordMap.putIfAbsent(word, new HashMap<String, ArrayList<Integer>>());
        HashMap<String, ArrayList<Integer>> fileMap = WordMap.get(word);
        fileMap.putIfAbsent(URL, new ArrayList<Integer>());
        ArrayList<Integer> occurrence = fileMap.get(URL);
        occurrence.add(position);
    }

    public Set<String> getWordsUrls(String word) {
        Set<String> sites = WordMap.get(word).keySet();
        return sites;
    }



}
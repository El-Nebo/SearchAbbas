package Indexer;

import ca.rmen.porterstemmer.PorterStemmer;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.file.DirectoryStream;

import java.util.*;

import DB.MongoDB;
public class Indexer {
    public  HashMap<String, HashMap<String, ArrayList<Integer>>> WordMap;

    ///////////////////DataBase//////
    public static final String DATABASE_NAME = "SearchAbbas";
    public static final String DATABASE_HOST_ADDRESS = "localhost";
    public static final int DATABASE_PORT_NUMBER = 27017;

    MongoDB myDB = new MongoDB("SearchAbbas");

    public static final int Num = 5000;
    ArrayList<String> URLs;

    MongoClient mongoClient;
    MongoDatabase database;
    ///////////////////////////////

    public void WriteOnDB(){
        //System.out.println(WordMap.size());
        DBObject WordIndex = new BasicDBObject("word","Abbas");
        for (Map.Entry mapElement : WordMap.entrySet()) {
            String word = (String)mapElement.getKey();
            //System.out.println("Word  " + word);
            HashMap<String,ArrayList<Integer>> urls = (HashMap<String,ArrayList<Integer>>)(mapElement.getValue());
            Set<String> WordURLs = urls.keySet();
            myDB.AddWordURLs(word,WordURLs);
//            for(Map.Entry mapelement2 : urls.entrySet()){
//                String URL = (String)mapelement2.getKey();
//               // System.out.print("URL: "+URL+" ");
//                ArrayList<Integer> pos = (ArrayList<Integer>) mapelement2.getValue();
//                //////////////////////////////
//                myDB.AddWordIndex(word, URL, pos);
//               // System.out.println("Aaaaa: "+word + " " + URL + " " + pos.get(0));
//                ///////////////////////////////
//                for (Integer po : pos) System.out.print(po + " ");
//                //System.out.println("");
//            }
        }
    }

    public Indexer(){
        WordMap = new HashMap<String, HashMap<String, ArrayList<Integer>>>();
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
        for (int i = 0 ; i < URLs.size() ; i++){
          //  System.out.println(URLs.get(i));
        }
    }

    public String [] ProcessHTMLFile(String path, int idx) throws IOException {

        String FileString = Files.readString(Paths.get(path));
        Document html = Jsoup.parse(FileString);

        String TotalString=  (html.title() +" "+ html.body().text()).toLowerCase();                                           //Building TotalString

        myDB.AddTitle(URLs.get(idx-1),html.title());

        TotalString = TotalString.replaceAll("[^a-zA-Z]", " ");                                                           //Filter all garbage
        String[] partsbeforeremovingstopwords = TotalString.split(" ");

        ArrayList<String> partsafterremovingstopwords = new ArrayList<String>();



////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        HashSet<String> stpwords = new HashSet<String>();
        File myObj = new File("StopWords.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            stpwords.add(data);
        }
        myReader.close();

        for (String partsbeforeremovingstopword : partsbeforeremovingstopwords) {
            if (!stpwords.contains(partsbeforeremovingstopword)) {
                partsafterremovingstopwords.add(partsbeforeremovingstopword);
            }
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ArrayList<String> FinalWords = new ArrayList<String>();

        PorterStemmer stemmer = new PorterStemmer();
        for (String partsafterremovingstopword : partsafterremovingstopwords) {
            if (!partsafterremovingstopword.isBlank())
                FinalWords.add(stemmer.stemWord(partsafterremovingstopword));
        }

        String [] ret = new String[FinalWords.size()];
        for(int i=0;i< FinalWords.size();i++)
            ret[i]=FinalWords.get(i);
        return ret;
    }


    public void ProcessAllFiles(){
        Path path = Paths.get("Htmls/");
        try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
            int idx;
            for (Path file : listing) {
                if (file.getFileName().toString().toLowerCase().endsWith(".html")) {
                    String filename = file.getFileName().toString();
                    idx = Integer.parseInt(filename.substring(0,filename.length()-5));
                    String[] Words = ProcessHTMLFile("Htmls/" + file.getFileName().toString(),idx);
                    IndexArray(Words,URLs.get(idx-1));
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexing;

import com.indexing.util.FileWalker;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author irfannurhakim
 */
public class Indexing {

    public static long N_messagge = 0; // date, to, from, subject, body, all
    public static int top_k_token;
    public static final String codeName = "irfan_elisafina_pandapotan";
    public static long docID = -1;
    public static RandomAccessFile docMapping = null;
    public static RandomAccessFile invertedIndexDate = null;
    public static RandomAccessFile termMappingDate = null;
    public static RandomAccessFile invertedIndexFrom = null;
    public static RandomAccessFile termMappingFrom = null;
    public static RandomAccessFile invertedIndexTo = null;
    public static RandomAccessFile termMappingTo = null;
    public static RandomAccessFile invertedIndexSubject = null;
    public static RandomAccessFile termMappingSubject = null;
    public static RandomAccessFile invertedIndexBody = null;
    public static RandomAccessFile termMappingBody = null;
    public final static String NEWLINE="\r\n";
    public static LinkedHashMap<String,Long> indexDate = new LinkedHashMap<>();
    public static TreeMap<String,String> treeIndexDate = new TreeMap<>();
    public static LinkedHashMap<String,Long> indexFrom = new LinkedHashMap<>();
    public static TreeMap<String,String> treeIndexFrom = new TreeMap<>();
    public static LinkedHashMap<String,Long> indexTo = new LinkedHashMap<>();
    public static TreeMap<String,String> treeIndexTo = new TreeMap<>();
    public static LinkedHashMap<String,Long> indexSubject = new LinkedHashMap<>();
    public static TreeMap<String,String> treeIndexSubject = new TreeMap<>();
    public static LinkedHashMap<String,Long> indexBody = new LinkedHashMap<>();
    public static TreeMap<String,String> treeIndexBody = new TreeMap<>();
    
    public static List<String> test = new ArrayList<>();
    public static Integer counterCall=0;
    public static int jumFile=0;
    public static boolean isCompress = true;
    public static String com= isCompress? "com_":"";
    
    public static String [] fileNames ={com+"document_mapping.txt", 
        com+"inverted_index_date.txt",com+"term_mapping_date.txt",
        com+"inverted_index_from.txt", com+"term_mapping_from.txt", 
        com+"inverted_index_to.txt", com+"term_mapping_to.txt",
        com+"inverted_index_subject.txt", com+"term_mapping_subject.txt", 
        com+"inverted_index_body.txt", com+"term_mapping_body.txt"};
    /**
     * author irfannurhakim
     *
     * @param args the command line arguments
     *
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws IOException {

        /*
         * if(args.length != 2){ System.out.println("Usage: Tokenizer.jar
         * document_location top_t_token"); System.exit(0); }
         *
         * String root = args[0]; top_k_token = Integer.parseInt(args[1]);
         */

        //String root = "/Users/hadipratama/Documents/Kuliah/Search_Engine_Tech/enron_mail_20110402/maildir/beck-s";
        String root = "/Users/hadipratama/Documents/Kuliah/Search_Engine_Tech/enron_mail_20110402/maildir/dasovich-j";
        //String root = "D:\\Kuliah_S2\\IF6054_Teknologi_Mesin_Pencari\\Tugas\\enron_mail_20110402\\enron_mail_20110402\\maildir\\";

        top_k_token = 100;
        File f1;
        
        for (int i = 0; i < fileNames.length; i++) {
            String string = fileNames[i];
            f1 = new File(string);
            if(f1.exists())
            {
                f1.delete();
            }
            
        }
        docMapping = new RandomAccessFile(fileNames[0], "rw");
        invertedIndexDate = new RandomAccessFile(fileNames[1], "rw");
        termMappingDate = new RandomAccessFile(fileNames[2], "rw");
        invertedIndexFrom = new RandomAccessFile(fileNames[3], "rw");
        termMappingFrom = new RandomAccessFile(fileNames[4], "rw");
        invertedIndexTo = new RandomAccessFile(fileNames[5], "rw");
        termMappingTo = new RandomAccessFile(fileNames[6], "rw");
        invertedIndexSubject = new RandomAccessFile(fileNames[7], "rw");
        termMappingSubject = new RandomAccessFile(fileNames[8], "rw");
        invertedIndexBody = new RandomAccessFile(fileNames[9], "rw");
        termMappingBody = new RandomAccessFile(fileNames[10], "rw");
        FileVisitor<Path> fileVisitor = new FileWalker();
        Files.walkFileTree(Paths.get(root), fileVisitor);
        
        /*Indexing.invertedIndex.seek(26212);
                byte[] buffer = new byte[70-NEWLINE.getBytes().length];
                Indexing.invertedIndex.read(buffer);
                String str = new String(buffer);
                System.out.println(str);*/
        
        /*IndexController.addTerm("tono", "susis", index, treeIndex);
        IndexController.addTerm("santi", "susiss",index, treeIndex);
        IndexController.addTerm("ani", "susisss",index, treeIndex);
        IndexController.addTerm("budi", "susissss",index, treeIndex);
        IndexController.addTerm("ani", "susisssss",index, treeIndex);
        System.out.println(treeIndex);
        System.out.println(index);
        
        System.out.println(IndexController.getPosition("budi3", index));*/
        
        /*HashMap<String,String> a = new HashMap<String,String>();
        a.put("satu", "aaaaa");
        a.put("dua", "aaaaa");
        a.put("tiga", "aaaaa");
        HashMap<String,String> b = new HashMap<String,String>();
        b.put("dua", "bbbbb");
        b.put("empat", "aaaaa");
        b.put("satu", "bbbbb");
        
        IndexController.insertDocIndex(1, a, index, treeIndex, invertedIndex);
        IndexController.insertDocIndex(2, b, index, treeIndex, invertedIndex);
        
        IndexController.printTermMap(Indexing.index, Indexing.treeIndex, Indexing.termMapping);
        System.out.println(index);
        System.out.println(treeIndex);*/
        
    }
}

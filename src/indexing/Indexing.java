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
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 *
 * @author irfannurhakim
 */
public class Indexing {

    public static long N_messagge = 0; // date, to, from, subject, body, all
    public static int top_k_token;
    public static String codeName = "irfan_elisafina_pandapotan";
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
    
    public static String [] fileNames ={"document_mapping.txt", 
        "inverted_index_date.txt","term_mapping_date.txt",
        "inverted_index_from.txt", "term_mapping_from.txt", 
        "inverted_index_to.txt", "term_mapping_to.txt",
        "inverted_index_subject.txt", "term_mapping_subject.txt", 
        "inverted_index_body.txt", "term_mapping_body.txt"};
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
        //String root = "/Users/hadipratama/Documents/Kuliah/Search_Engine_Tech/enron_mail_20110402/maildir/beck-s";
        String root = "D:\\Kuliah_S2\\IF6054_Teknologi_Mesin_Pencari\\Tugas\\enron_mail_20110402\\enron_mail_20110402\\maildir\\allen-p";

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
        docMapping = new RandomAccessFile("document_mapping.txt", "rw");
        invertedIndexDate = new RandomAccessFile("inverted_index_date.txt", "rw");
        termMappingDate = new RandomAccessFile("term_mapping_date.txt", "rw");
        invertedIndexFrom = new RandomAccessFile("inverted_index_from.txt", "rw");
        termMappingFrom = new RandomAccessFile("term_mapping_from.txt", "rw");
        invertedIndexTo = new RandomAccessFile("inverted_index_to.txt", "rw");
        termMappingTo = new RandomAccessFile("term_mapping_to.txt", "rw");
        invertedIndexSubject = new RandomAccessFile("inverted_index_subject.txt", "rw");
        termMappingSubject = new RandomAccessFile("term_mapping_subject.txt", "rw");
        invertedIndexBody = new RandomAccessFile("inverted_index_body.txt", "rw");
        termMappingBody = new RandomAccessFile("term_mapping_body.txt", "rw");
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexing;

import com.indexing.controller.IndexController;
import com.indexing.util.FileWalker;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

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
    public static RandomAccessFile invertedIndex = null;
    public final String NEWLINE="\r\n";
    public static LinkedHashMap<String,Long> index = new LinkedHashMap<String,Long>();

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
        String root = "/Users/hadipratama/Documents/Kuliah/Search_Engine_Tech/enron_mail_20110402/maildir/beck-s";
        //String root = "D:\\Kuliah_S2\\IF6054_Teknologi_Mesin_Pencari\\Tugas\\enron_mail_20110402\\enron_mail_20110402\\maildir\\allen-p\\_sent_mail";
        top_k_token = 100;
        File f1 = new File("document_mapping.txt");
        if(f1.exists())
        {
            f1.delete();
        }
        File f2 = new File("inverted_index.txt");
        if(f2.exists())
        {
            f2.delete();
        }
        docMapping = new RandomAccessFile("document_mapping.txt", "rw");
        invertedIndex = new RandomAccessFile("inverted_index.txt", "rw");
        FileVisitor<Path> fileVisitor = new FileWalker();
        Files.walkFileTree(Paths.get(root), fileVisitor);
        
       /* IndexController.addTerm("budi1", "susi");
        IndexController.addTerm("budi2", "susi");
        IndexController.addTerm("budi3", "susi");
        IndexController.addTerm("budi4", "susi");
        IndexController.addTerm("budi2", "susi");*/
        
        System.out.println(index);
    }
}

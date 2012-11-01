/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexing;

import com.indexing.util.FileWalker;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author irfannurhakim
 */
public class Tokenizer {

    public static long N_messagge = 0; // date, to, from, subject, body, all
    public static int top_k_token;
    public static String codeName = "irfan_elisafina_pandapotan";
    public static long docID = -1;
    public static RandomAccessFile docMapping = null;

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

        docMapping = new RandomAccessFile("document mapping.txt", "rw");

        FileVisitor<Path> fileVisitor = new FileWalker();
        Files.walkFileTree(Paths.get(root), fileVisitor);
    }
}

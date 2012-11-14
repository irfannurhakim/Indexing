/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexing;

import com.indexing.controller.IndexCompression2;
import com.indexing.model.BigConcurentHashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Compression {

    static String fieldName = "body";
    static RandomAccessFile invertedIndex = null;
    static RandomAccessFile indexMapping = null;
    static RandomAccessFile cominvertedIndex = null;
    static RandomAccessFile comindexMapping = null;
    static LinkedHashMap<String, String> map = new LinkedHashMap<>();
    public final static String NEWLINE = "\r\n";

    public static void main(String[] args) {
        try {
            invertedIndex = new RandomAccessFile("inverted_index_" + fieldName + ".txt", "r");
            indexMapping = new RandomAccessFile("term_mapping_" + fieldName + ".txt", "r");
            cominvertedIndex = new RandomAccessFile("com_inverted_index_" + fieldName + ".txt", "rw");
            comindexMapping = new RandomAccessFile("com_term_mapping_" + fieldName + ".txt", "rw");


            String mapping;
            try {
                mapping = indexMapping.readLine();
                while (mapping != null) {
                    //System.out.println(mapping);
                    String raw[] = mapping.split("=");
                    String term = raw[0];
                    String termID = raw[1].split("\\|")[0];
                    map.put(termID, term);
                    mapping = indexMapping.readLine();
                }
                //System.out.println(map);
            } catch (IOException ex) {
                Logger.getLogger(Compression.class.getName()).log(Level.SEVERE, null, ex);
            }

            String indexing;
            //indexing="5842=611:160;100:249;615:68;712:34;779:64,380;816:245;1116:34;1260:68;1266:160;1490:22;1559:102,319,460,765;1565:68,228,336,557,698,1003;1615:38;1733:35;1784:60;2089:34;2225:68;2230:160;2384:35;";
            //String hasil = compressPostingList(indexing);
            //System.out.println(hasil);
            long position = 0;
            StringBuffer kumpul = new StringBuffer();
            try {
                indexing = invertedIndex.readLine();
                String raws[] = indexing.split("=");
                String ID = raws[0];
                while (indexing != null) {
                    if (kumpul.length() > 1000000) {
                        System.out.println(ID);
                        cominvertedIndex.seek(cominvertedIndex.length());
                        cominvertedIndex.write(kumpul.toString().getBytes());
                        kumpul = new StringBuffer();
                    }

                    
                    String hasil = compressPostingList(indexing) + NEWLINE;
                    long length = hasil.getBytes().length;
                    String value = map.get(ID);
                    map.put(ID, value + "|" + position + "|" + length);
                    position += length;
                    kumpul.append(hasil);

                    /*String raw[] = index.split("=");
                     String term = raw[0];
                     String termID = raw[1].split("\\|")[0];
                     map.put(termID, term);*/
                    indexing = invertedIndex.readLine();
                }

                try {
                    cominvertedIndex.seek(cominvertedIndex.length());
                    cominvertedIndex.write(kumpul.toString().getBytes());
                    kumpul = new StringBuffer();
                } catch (IOException ex) {
                    Logger.getLogger(BigConcurentHashMap.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            try {
                Map.Entry<String, String> entry = itr.next();
                String termID = entry.getKey();
                String raw[] = entry.getValue().split("\\|");
                comindexMapping.seek(comindexMapping.length());
                String toWrite = raw[0] + "=" + termID+"|"+raw[1]+"|"+raw[2]+ Indexing.NEWLINE;
                comindexMapping.write(toWrite.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(BigConcurentHashMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                //System.out.println(map);
            } catch (IOException ex) {
                Logger.getLogger(Compression.class.getName()).log(Level.SEVERE, null, ex);
            }


        } catch (FileNotFoundException ex) {
            Logger.getLogger(Compression.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String compressPostingList(String indexing) {
        TreeMap<Integer, ArrayList<Integer>> index = new TreeMap<>();
        String raw[] = indexing.split("=");
        String termID = raw[0];
        //System.out.println(termID);
        String postingList[] = raw[1].split(";");
        for (String post : postingList) {
            String temp[] = post.split(":");
            int docID = Integer.parseInt(temp[0]);
            String pos[] = temp[1].split(",");
            ArrayList<Integer> position = new ArrayList<>();
            for (String poss : pos) {
                position.add(Integer.parseInt(poss));
            }
            index.put(docID, position);

        }
        //System.out.println(index);

        ArrayList<Integer> docIDs = new ArrayList<>(index.keySet());
        //System.out.println(docIDs);
        String compresDocIDs = IndexCompression2.VByteToString(new LinkedList<>(docIDs));
        String compressPos = "";
        Iterator<Map.Entry<Integer, ArrayList<Integer>>> iter = index.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, ArrayList<Integer>> entry = iter.next();
            ArrayList<Integer> pos = entry.getValue();
            compressPos += IndexCompression2.VByteToString(new LinkedList<>(pos)) + ":";
        }
        String hasil = compresDocIDs + ";" + compressPos.substring(0, compressPos.length() - 1);
        //System.out.println(hasil);
        return termID + "=" + hasil;
    }
}

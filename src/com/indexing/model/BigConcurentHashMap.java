/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indexing.model;

import indexing.Indexing;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class BigConcurentHashMap {

    public static ConcurrentSkipListMap<Long, String> dateConcurentMap = new ConcurrentSkipListMap<Long, String>();
    public static ConcurrentSkipListMap<Long, String> toConcurentMap = new ConcurrentSkipListMap<Long, String>();
    public static ConcurrentSkipListMap<Long, String> fromConcurentMap = new ConcurrentSkipListMap<Long, String>();
    public static ConcurrentSkipListMap<Long, String> subjectConcurentMap = new ConcurrentSkipListMap<Long, String>();
    public static ConcurrentSkipListMap<Long, String> bodyConcurentMap = new ConcurrentSkipListMap<Long, String>();
    public static ConcurrentSkipListMap<Long, String> allConcurentMap = new ConcurrentSkipListMap<Long, String>();

    /**
     * author: Pandapotan untuk menggabungkan hashmap-hashmap kecil yang didapat
     * dari masing-masing file menjadi suatu concurent hashmap besar yang berisi
     * seluruh term, tf, td, bobot term dari semua file untuk field tertentu.
     *
     * @param a hashmap besar
     * @param b hashmap per file
     */
    public static void mergeBigHashMap(ConcurrentSkipListMap<Long, String> a, TreeMap<String, String> tree, HashMap<String, String> b, long docNumber) {
        // Get a set of the entries 

        if (b != null) {
            Set set = b.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                String newKey = (String) me.getKey();
                String newval = (String) me.getValue();

                Long idMapTerm = new Long(0);
                String maping = tree.get(newKey);
                while (maping == null) {
                    try {
                        tree.put(newKey, (tree.size() + 1) + "");
                        maping = tree.get(newKey);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println(newKey);
                        System.out.println(tree.size());
                        maping = tree.get(newKey);
                        System.out.println(maping);
                        //e.printStackTrace();
                    }
                }
                /*if(maping==null)
                 {
                 idMapTerm = new Long(tree.size()+1);
                 try {
                 tree.put(newKey, (tree.size()+1)+"");
                 } catch (Exception e) {
                 System.out.println(e.toString());
                 System.out.println(newKey);
                 System.out.println(tree.size());
                 //e.printStackTrace();
                 }
                
                 }
                 else
                 {
                
                 }*/

                idMapTerm = Long.parseLong(maping);
                String freq = (String) a.get(idMapTerm);
                if (freq == null) {
                    freq = docNumber + "|" + newval + "-";

                } else {
                    //System.out.println(newKey+" = "+(freq.getTotalDocument()+1));
                    freq = freq + "" + docNumber + "|" + newval + "-";
                    //System.out.println(freq);

                }
                a.put(idMapTerm, freq);
            }

        }
    }

    public static void printPartIndex(ConcurrentSkipListMap<Long, String> a, String fileName) {
        FileWriter fStream = null;
        try {
            fStream = new FileWriter(fileName, true);
            Set set = a.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                Long newKey = (Long) me.getKey();
                String newval = (String) me.getValue();
                fStream.append(newKey + "=" + newval + Indexing.NEWLINE);

            }
            fStream.flush();
            fStream.close();
            a.clear();
        } catch (IOException ex) {
            Logger.getLogger(BigConcurentHashMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getKeyfromValue(TreeMap<String, String> tree, String value) {
        String result = "";
        Iterator<Map.Entry<String, String>> iter = tree.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            if (entry.getValue().equals(value)) {
                result = entry.getKey();
                break;
            }
        }
        return result;
    }

    public static void mergeInvertedIndex(TreeMap<String, String> tree, String filepart, RandomAccessFile index, RandomAccessFile indexMapping) {
        String fileparts[] = new String[Indexing.jumFile];
        RandomAccessFile raf[] = new RandomAccessFile[Indexing.jumFile];

        for (int i = 0; i < Indexing.jumFile; i++) {
            fileparts[i] = filepart + (i+1) + ".txt";
            try {
                raf[i] = new RandomAccessFile(fileparts[i], "r");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        String[] temp = new String[Indexing.jumFile];
        long[] tempInt = new long[Indexing.jumFile];
        
        for (int i = 0; i < Indexing.jumFile; i++) {
            try {
                temp[i] = raf[i].readLine();
                tempInt[i] = Long.parseLong(temp[i].split("=")[0]);
            } catch (IOException ex) {
                Logger.getLogger(BigConcurentHashMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for (long i = 1; i < tree.size()+1; i++) {
            
                String tempString="";
                for(int j = 0; j < Indexing.jumFile; j++)
                {
                    if(tempInt[j]==i)
                    {
                        tempString+=temp[j].split("=")[1];
                        try {
                            temp[j] = raf[j].readLine();
                            //System.out.println("file ke - "+j+ " --- "+temp[j]);
                            tempInt[j] = Long.parseLong(temp[j].split("=")[0]);
                        } catch (Exception ex) {
                            System.out.println("File ke "+j+ "habis sampai "+i);
                            //Logger.getLogger(BigConcurentHashMap.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                }
                try {
                String key = getKeyfromValue(tree, i+"");
                long position = index.length();
                
                
                index.seek(index.length());
                String toWrite =i+"="+tempString+Indexing.NEWLINE;
                index.write(toWrite.getBytes());
                long length = toWrite.getBytes().length;
                tree.put(key, i+"|"+position+"|"+length);
            } catch (IOException ex) {
                Logger.getLogger(BigConcurentHashMap.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

        Iterator<Map.Entry<String, String>> itr = tree.entrySet().iterator();
        while (itr.hasNext()) {
            try {
                Map.Entry<String, String> entry = itr.next();
                String term = entry.getKey();
                String indexTerm = entry.getValue();
                 indexMapping.seek(indexMapping.length());
                    String toWrite =term+"="+indexTerm+Indexing.NEWLINE;
                    indexMapping.write(toWrite.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(BigConcurentHashMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        File f1;
        for (int i = 0; i < fileparts.length; i++) {
            try {
                raf[i].close();
            } catch (IOException ex) {
                Logger.getLogger(BigConcurentHashMap.class.getName()).log(Level.SEVERE, null, ex);
            }
            String string = fileparts[i];
            f1 = new File(string);
            if(f1.exists())
            {
                f1.delete();
            }
            
        }
    }

    /**
     * author: Pandapotan untuk menghitung bobot dari suatu term
     *
     * @param a
     * @param N_document
     * @return
     */
    public static LinkedHashMap<String, TermCounter> calculateTermWight(ConcurrentHashMap<String, TermCounter> a, long N_document) {
        Set set = a.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            String newKey = (String) me.getKey();
            TermCounter newval = (TermCounter) me.getValue();
            double tokenWeight = ((double) newval.getTotalTerm() / N_document) * Math.log(N_document / (double) newval.getTotalDocument());
            newval.setTokenWeight(tokenWeight);
            a.put(newKey, newval);
        }

        LinkedHashMap<String, TermCounter> hasil = (LinkedHashMap<String, TermCounter>) sortByComparator(a);
        // Collections.sort(a, new TermCounter.WeightComparator());
        return (hasil);
    }

    /**
     * author : Elisafina untuk mengurutkan isi map berdasarkan bobot term nya
     *
     * @param unsortMap
     * @return
     */
    public static Map sortByComparator(Map unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {

                Map.Entry e1 = (Map.Entry) o1;
                Map.Entry e2 = (Map.Entry) o2;
                TermCounter t1 = (TermCounter) e1.getValue();
                TermCounter t2 = (TermCounter) e2.getValue();
                return (Double.compare(t2.getTokenWeight(), t1.getTokenWeight()));

            }
        });

        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();

            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * author: Pandapotan untuk menuliskan term statistic suatu field ke dalam
     * file
     *
     * @param termList
     * @param field
     * @param totalMessage
     */
    public static void printStatistic(LinkedHashMap termList, String field, long totalMessage) {
        //System.out.println(field);
        int i = 1;
        String temp = "";
        Set set = termList.entrySet();
        Iterator it = set.iterator();
        long totAllTerm = 0;
        while (it.hasNext() && i <= Indexing.top_k_token) {
            Map.Entry me = (Map.Entry) it.next();
            String newKey = (String) me.getKey();
            TermCounter newval = (TermCounter) me.getValue();
            temp += Indexing.codeName + " " + field + " " + i + " " + newKey + " " + newval.getTotalTerm() + " " + newval.getTotalDocument() + " " + newval.getTokenWeight() + "\r\n";
            i++;
            totAllTerm += newval.getTotalTerm();

        }
        String hasil = Indexing.codeName + " " + field + " N " + totalMessage + "\r\n";
        hasil += Indexing.codeName + " " + field + " TO " + totAllTerm + "\r\n";
        hasil += Indexing.codeName + " " + field + " UT " + termList.size() + "\r\n";
        hasil += temp;

        writeToFile(Indexing.codeName + " " + field + ".txt", hasil);
    }

    /**
     * author: Pandapotan method menulis suatu string ke dalam file txt
     *
     * @param fileName
     * @param text
     */
    public static void writeToFile(String fileName, String text) {
        try {
            // Create file 
            //System.out.println(fileName);
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(text);
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indexing.controller;

import com.sun.org.apache.xalan.internal.xsltc.dom.KeyIndex;
import indexing.Indexing;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class IndexController {

    public static void insertInvertedIndex(String index, long position, RandomAccessFile file) {
        try {
            file.seek(position);
            long panjang = file.length() - position;
            if (panjang <= Integer.MAX_VALUE) {
                byte[] buffer = new byte[(int) panjang];
                file.read(buffer);
                file.seek(position);
                file.write(index.getBytes());
                file.write(buffer);
            } else {
                System.out.println("ERRRRRRRROOOOORR!!!");
            }
        } catch (IOException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addTerm(String key, String index, LinkedHashMap<String, Long> indexMap, TreeMap<String, String> treeMap) {
        String indexTerm = treeMap.get(key);
        if (indexTerm == null) {
            Long freq = (Long) indexMap.get(indexTerm);
            freq = new Long(index.getBytes().length);
            String keyIndex = (treeMap.size() + 1) + "";
            treeMap.put(key, keyIndex);
            indexMap.put(keyIndex, freq);
        } else {
            Long freq = (Long) indexMap.get(indexTerm);
            long value = freq.longValue();
            freq = new Long(value + index.getBytes().length);
            indexMap.put(indexTerm, freq);
        }

    }

    public static long getPosition(String key, LinkedHashMap<String, Long> indexMap) {
        long hasil = 0;
        String keys = "";
        Iterator<Entry<String, Long>> itr = indexMap.entrySet().iterator();
        while (itr.hasNext() && !keys.equals(key)) {
            Entry<String, Long> entry = itr.next();
            keys = entry.getKey();
            Long val = (Long) entry.getValue();
            //System.out.println("value: " + val);
            hasil += val;
            if (keys.equals(key)) {
                hasil -= Indexing.NEWLINE.getBytes().length;
            }
        }
        return hasil;
    }

    public static void insertDocIndex(long docNumber, HashMap<String, String> map, LinkedHashMap<String, Long> indexMap, TreeMap<String, String> treeMap, RandomAccessFile raf) {
        Iterator<Entry<String, String>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, String> entry = itr.next();
            String term = entry.getKey();
            String docPos = entry.getValue();
            // System.out.println(term);
            String checkonTree = treeMap.get(term);
            if (checkonTree == null) {
                try {
                    long pos = raf.length();
                    String input = (treeMap.size() + 1) + "=" + docNumber + "|" + docPos + "-" + Indexing.NEWLINE;
                    //System.out.println("new line "+term+" = "+input.getBytes().length);
                    insertInvertedIndex(input, pos, raf);
                    addTerm(term, input, indexMap, treeMap);
                } catch (IOException ex) {
                    Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                long pos = getPosition(checkonTree, indexMap);
                String input = docNumber + "|" + docPos + "-";
                //System.out.println("line "+term+" ,posisi = "+pos+" , input ="+input.getBytes().length);
                insertInvertedIndex(input, pos, raf);
                addTerm(term, input, indexMap, treeMap);
            }
        }
    }
    
    public static void insertDocIndex2(ConcurrentHashMap<String, String> map, LinkedHashMap<String, Long> indexMap, TreeMap<String, String> treeMap, RandomAccessFile raf) {
        Iterator<Entry<String, String>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, String> entry = itr.next();
            String term = entry.getKey();
            String docPos = entry.getValue();
            // System.out.println(term);
            String checkonTree = treeMap.get(term);
            if (checkonTree == null) {
                try {
                    long pos = raf.length();
                    String input = (treeMap.size() + 1) + "=" + docPos + Indexing.NEWLINE;
                    //System.out.println("new line "+term+" = "+input.getBytes().length);
                    insertInvertedIndex(input, pos, raf);
                    addTerm(term, input, indexMap, treeMap);
                } catch (IOException ex) {
                    Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                long pos = getPosition(checkonTree, indexMap);
                String input = docPos;
                //System.out.println("line "+term+" ,posisi = "+pos+" , input ="+input.getBytes().length);
                insertInvertedIndex(input, pos, raf);
                addTerm(term, input, indexMap, treeMap);
            }
        }
    }

    public static void printTermMap(LinkedHashMap<String, Long> indexMap, TreeMap<String, String> treeMap, RandomAccessFile raf) {
        long position = 0;
        long length = 0;
        Iterator<Entry<String, String>> itr = treeMap.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, String> entry = itr.next();
            String term = entry.getKey();
            String indexTerm = entry.getValue();
            Iterator<Entry<String, Long>> itr2 = indexMap.entrySet().iterator();
            String keys = "";
            while (itr2.hasNext() && !keys.equals(indexTerm)) {
                Entry<String, Long> entryLink = itr2.next();
                keys = entryLink.getKey();
                Long val = (Long) entryLink.getValue();
                //System.out.println("value: " + val);
                position += val;
                if (keys.equals(indexTerm)) {
                    position -= val;
                    length = val;
                }
            }
            try {
                raf.seek(raf.length());
                String toWrite =term+"="+indexTerm+"|"+position+"|"+length+Indexing.NEWLINE;
                raf.write(toWrite.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            }
            position=0;
            length=0;

        }
    }
}

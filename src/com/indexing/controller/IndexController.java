/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indexing.controller;

import indexing.Indexing;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class IndexController {
    
    public static void insertInvertedIndex(String index, long position, RandomAccessFile file)
    {
        try {
            file.seek(position);
            long panjang = file.length()-position;
            if(panjang >  Integer.MAX_VALUE)
            {
                byte[] buffer = new byte[(int)panjang];
                file.read(buffer);
                file.seek(position);
                file.write(index.getBytes());
                file.write(buffer);
            }
            else
            {
                System.out.println("ERRRRRRRROOOOORR!!!");
            }
        } catch (IOException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void addTerm(String key, String index, LinkedHashMap<String,Long> indexMap)
    {
         Long freq = (Long) indexMap.get(key);
        if (freq == null) {
            freq = new Long(index.getBytes().length);
        } else {
            long value = freq.longValue();
            freq = new Long(value + index.getBytes().length);
        }
        indexMap.put(key, freq);
    }
    
    public static long getPosition(String key, LinkedHashMap<String,Long> indexMap)
    {
        long hasil=0;
        Iterator<Entry<String,Long>> itr = indexMap.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String,Long> entry = itr.next();
            String keys = entry.getKey();
            System.out.println("key: " + key);
            Long val = (Long) entry.getValue();
            System.out.println("value: " + val);
        }
        return hasil;
    }
    
}

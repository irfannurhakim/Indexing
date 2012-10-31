/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indexing.controller;

import java.util.HashMap;

/**
 *
 * @author user
 */
public class dateTokenizer {

    /**
     * Author: Elisafina
     * memotong string date menjadi 5 bagian yaitu:
     * -hari : mon/tue/dll
     * -tanggal dalam format : 13dec2000
     * -jam dalam format : 16:39:00
     * - zona waktu : pdt/pst
     * - perbedaan waktu: -0700
     * @param date
     * @return hashmap yang berisi token dari date
     */
    public static HashMap<String, String> getListDate(String date) {
        HashMap<String, String> termList = new HashMap<String, String>();
        //String[] terms = date.split("\\W");
         String[] terms = date.split("\\W");
        
        long pos=0;
        
        for (int i = 0; i < terms.length; i++) {
            String key = terms[i];
           if (!key.equals("")) {
                String freq = (String) termList.get(key);
                pos++;
                if (freq == null) {
                    freq = pos+" ";
                } else {
                    String value = freq;
                    freq += pos+" ";
                }
                termList.put(key, freq);
            }
        }
       
        return termList;
    }
}

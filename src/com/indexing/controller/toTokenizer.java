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
public class toTokenizer {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * author:Elisafina adalah untuk melakukan tokenisasi pada field to dengan
     * cara memotong berdasarkan ", " atau "<>" sehingga didapatkan hashmap yang
     * berisi alamat email tujuan dan nama kontak email tersebut(jika ada)
     *
     * @param to
     * @return
     */
    public static HashMap<String, String> getListTo(String to) {
        HashMap<String, String> termList = new HashMap<>();
        String[] terms = to.split("_|\\W");
        long pos=0;
        
        for (int i = 0; i < terms.length; i++) {
            String key = terms[i];
           if (!key.equals("")) {
                String freq = (String) termList.get(key);
                pos++;
                if (freq == null) {
                    freq = pos+",";
                } else {
                    String value = freq;
                    freq += pos+",";
                }
                termList.put(key, freq);
            }
        }
        //System.out.println(termList);
        return termList;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.query.controller;

import java.io.IOException;
import java.io.RandomAccessFile;
import indexing.Indexing;
import java.util.ArrayList;

/**
 *
 * @author irfannurhakim
 */
public class QueryProcessor {

    private static final String PREFIX_INDEX_FILENAME = "inverted_index_";
    private static final String PREFIX_TERM_MAPPING_FILENAME = "term_mapping_";

    public static void doQuery(String field, String term) throws IOException {

        String indexFileName = PREFIX_INDEX_FILENAME + field + ".txt";

        RandomAccessFile file = new RandomAccessFile(indexFileName, "r");
        ArrayList<Object> position = getPositionTerm(field, term);

        if (position.size() == 3) {
            file.seek((Long) position.get(0));
            byte[] buffer = new byte[(int) position.get(1) - Indexing.NEWLINE.getBytes().length];
            file.read(buffer);
            String str = new String(buffer);
            System.out.println(str);
        } else {
            System.out.println("Term '" + term + "' Not Found in Field " + field);
        }
    }

    private static ArrayList<Object> getPositionTerm(String field, String term) throws IOException {
        String termMappingFileName = PREFIX_TERM_MAPPING_FILENAME + field + ".txt";

        RandomAccessFile file = new RandomAccessFile(termMappingFileName, "r");
        ArrayList<Object> ret = new ArrayList<>();
        file.seek(0);
        String line = file.readLine().split("=")[0];
        if (line == null || line.compareTo(term) >= 0) {
            /*
             * the start is greater than or equal to the target, so it is what
             * we are looking for.
             */
            System.out.println(line);
            return null;
        }

        /*
         * set up the binary search.
         */
        long beg = 0;
        long end = file.length();
        boolean found = false;
        long foundb = 0;
        while (beg <= end) {
            /*
             * find the mid point.
             */
            long mid = beg + (end - beg) / 2;
            file.seek(mid);
            file.readLine();
            line = file.readLine().split("=")[0];
            if (line == null || line.compareTo(term) >= 0) {
                /*
                 * what we found is greater than or equal to the target, so look
                 * before it.
                 */
                if (line.matches(term)) {
                    found = true;
                }
                end = mid - 1;
            } else {
                /*
                 * otherwise, look after it.
                 */
                beg = mid + 1;
            }
        }

        if (found) {
            file.seek(beg);
            file.readLine();
            String target = file.readLine();
            ret.add(Long.parseLong(target.split("\\|")[1]));
            ret.add(Integer.valueOf(target.split("\\|")[2]));
            ret.add(target.split("=")[0]);
        }
        return ret;
    }
}

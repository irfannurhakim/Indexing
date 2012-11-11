/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.query.controller;

import indexing.Indexing;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 *
 * @author irfannurhakim
 */
public class QueryProcessor {

    private static final String PREFIX_INDEX_FILENAME = "inverted_index_";
    private static final String PREFIX_TERM_MAPPING_FILENAME = "term_mapping_";
    private static final String DOC_MAPPING = "document_mapping.txt";

    public static void doQuery(String field, String term, final String path) throws IOException {

        String indexFileName = path + PREFIX_INDEX_FILENAME + field + ".txt";

        RandomAccessFile file = new RandomAccessFile(indexFileName, "r");
        ArrayList<Object> position = getPositionTerm(field, term, path);

        if (position.size() == 3) {
            file.seek((Long) position.get(0));
            byte[] buffer = new byte[(int) position.get(1) - Indexing.NEWLINE.getBytes().length];
            file.read(buffer);
            String str = new String(buffer);
            String content = str.split("=")[1];
            String[] msgs = content.split(";");
            System.out.println(str);
            String toWrite = term + " FIELD=" + field + " DF=" + msgs.length + "\r\n";

            List<Future<String>> list = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(8);
            for (final String string : msgs) {
                Callable<String> worker = new Callable<String>() {

                    @Override
                    public String call() throws Exception {
                        return "MSG ID=" + messageIdTranslation(string.split(":")[0], path) + " TF=" + string.split(":")[1].split(",").length + " " + string.split(":")[1] + "\r\n";
                    }
                };
                Future<String> submit = executor.submit(worker);
                list.add(submit);
            }

            executor.shutdown();
            // Wait until all threads are finish
            while (!executor.isTerminated()) {
            }

            for (Future<String> future : list) {
                try {
                    toWrite += future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            writeFile(toWrite, field, term);
        } else {
            System.out.println("Term '" + term + "' Not Found in Field " + field);
        }
    }

    private static ArrayList<Object> getPositionTerm(String field, String term, String path) throws IOException {
        String termMappingFileName = path + PREFIX_TERM_MAPPING_FILENAME + field + ".txt";

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

    private static void writeFile(String textToWrite, String field, String term) throws IOException {
        try (FileChannel rwChannel = new RandomAccessFile(Indexing.codeName + "-" + field + "-" + term + ".txt", "rw").getChannel()) {
            ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, textToWrite.length());
            wrBuf.put(textToWrite.getBytes());
        }
    }

    private static String messageIdTranslation(String idDocs, String path) throws IOException {
        String termMappingFileName = path + DOC_MAPPING;
        RandomAccessFile file = new RandomAccessFile(termMappingFileName, "r");
        long beg = 0, mid = 0;
        long end = file.length();
        boolean found = false;
        while (beg < end && !found) {
            mid = (beg + end) / 2;
            file.seek(mid);
            file.readLine();
            String line = file.readLine().split("=")[0];

            if (line.matches(idDocs)) {
                found = true;
                beg = mid;
            } else {
                if (Integer.valueOf(line) >= Integer.valueOf(idDocs)) {
                    end = mid - 1;
                } else {
                    beg = mid + 1;
                }
            }
        }

        if (found) {
            file.seek(beg);
            file.readLine();
            return file.readLine().split("=")[1];
        } else {
            return "null";
        }
    }
}

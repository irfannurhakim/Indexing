
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indexing.util;

import com.indexing.model.BigConcurentHashMap;
import indexing.Indexing;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author irfannurhakim
 */
public class FileWalker extends SimpleFileVisitor<Path> {

    private static long startTime;
    private int nrOfProcessors = Runtime.getRuntime().availableProcessors();
    private ExecutorService es = Executors.newFixedThreadPool(nrOfProcessors);
    private Runtime rt = Runtime.getRuntime();
    private int i = 0;

    public FileWalker() {
        startTime = System.nanoTime();
    }

    /**
     * Visit each files in directory and after that submit a job to worker to
     * read the file.
     *
     * @param aFile
     * @param aAttrs
     * @return enumerator
     * @throws IOException
     */
    @Override
    public FileVisitResult visitFile(
            Path aFile, BasicFileAttributes aAttrs) throws IOException {
        /**
         * skip .DS_Store file Print every 1000 jobs send to worker
         */
        if (!aFile.getFileName().toString().equalsIgnoreCase(".DS_Store")) {
            if (i % 1000 == 0) {
                System.out.println("job send " + i);
            }
            i++;
            /**
             * Instantiate the worker send job to worker with parameter
             * directory file and job counter
             */
            FileReader task = new FileReader(aFile, i);
            task.setCaller(this);
            es.submit(task);
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * This method visit all directory, after end with a file then return
     * enumeration with CONTINUE value to read the file skip for all_documents
     * folder
     *
     * @param aDir
     * @param aAttrs
     * @return
     * @throws IOException
     */
    @Override
    public FileVisitResult preVisitDirectory(
            Path aDir, BasicFileAttributes aAttrs) throws IOException {
        if (aDir.endsWith("all_documents/")) {
            return FileVisitResult.SKIP_SUBTREE;
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * After a job has finished by the worker, worker will call this method to
     * acknowledge that the job has done When the total number of finished job
     * equal to total number job that has been submitted, the system will
     * terminate and give a statistical result in a text file
     *
     * @param date
     * @param from
     * @param to
     * @param subject
     * @param body
     * @param allFieldMap
     * @param jobDone
     * @throws InterruptedException
     */
    public void callback(HashMap<String, Integer> date, HashMap<String, Integer> from, HashMap<String, Integer> to, HashMap<String, Integer> subject, HashMap<String, Integer> body, HashMap<String, Integer> allFieldMap, int jobDone) throws InterruptedException {
        /**
         * Print each 1000 executed job, and run the garbage collector for
         * memory efficiency
         */
        synchronized (Indexing.counterCall) {
            Indexing.counterCall++;
            synchronized (Indexing.test) {
                if (Indexing.test.size() >= 4) {
                    System.out.println(Indexing.test);
                }
            }


            if (Indexing.counterCall % 1000 == 0) {
                System.out.println("<==> job done " + Indexing.counterCall + " from: " + i + " in : " + ((System.nanoTime() - startTime) / 1000000000.0) + " secs");

                if (Indexing.counterCall % 10000 == 0) {


                    System.out.println("masuk1");
                    Indexing.jumFile++;
                    BigConcurentHashMap.printPartIndex(BigConcurentHashMap.dateConcurentMap, "tempDate" + Indexing.jumFile + ".txt");
                    BigConcurentHashMap.printPartIndex(BigConcurentHashMap.fromConcurentMap, "tempFrom" + Indexing.jumFile + ".txt");
                    BigConcurentHashMap.printPartIndex(BigConcurentHashMap.toConcurentMap, "tempTo" + Indexing.jumFile + ".txt");
                    BigConcurentHashMap.printPartIndex(BigConcurentHashMap.subjectConcurentMap, "tempSubject" + Indexing.jumFile + ".txt");
                    BigConcurentHashMap.printPartIndex(BigConcurentHashMap.bodyConcurentMap, "tempBody" + Indexing.jumFile + ".txt");
                    rt.gc();
                    rt.gc();
                }
                //rt.gc();
                //rt.gc();
            }



            /**
             * Terminate job while all job has finished
             */
            if (Indexing.counterCall >= i) {
                es.shutdown();
                es.awaitTermination((long) 100, TimeUnit.MILLISECONDS);


                /**
                 * Calculate all document statistics and print to file
                 */
                Indexing.N_messagge = i;
                try {
                    if (Indexing.counterCall % 10000 != 0) {
                        System.out.println("masuk2");
                        Indexing.jumFile++;
                        BigConcurentHashMap.printPartIndex(BigConcurentHashMap.dateConcurentMap, "tempDate" + Indexing.jumFile + ".txt");
                        BigConcurentHashMap.printPartIndex(BigConcurentHashMap.fromConcurentMap, "tempFrom" + Indexing.jumFile + ".txt");
                        BigConcurentHashMap.printPartIndex(BigConcurentHashMap.toConcurentMap, "tempTo" + Indexing.jumFile + ".txt");
                        BigConcurentHashMap.printPartIndex(BigConcurentHashMap.subjectConcurentMap, "tempSubject" + Indexing.jumFile + ".txt");
                        BigConcurentHashMap.printPartIndex(BigConcurentHashMap.bodyConcurentMap, "tempBody" + Indexing.jumFile + ".txt");
                        rt.gc();
                    }

                    BigConcurentHashMap.mergeInvertedIndex(Indexing.treeIndexDate, "tempDate", Indexing.invertedIndexDate, Indexing.termMappingDate);
                    BigConcurentHashMap.mergeInvertedIndex(Indexing.treeIndexFrom, "tempFrom", Indexing.invertedIndexFrom, Indexing.termMappingFrom);
                    BigConcurentHashMap.mergeInvertedIndex(Indexing.treeIndexTo, "tempTo", Indexing.invertedIndexTo, Indexing.termMappingTo);
                    BigConcurentHashMap.mergeInvertedIndex(Indexing.treeIndexSubject, "tempSubject", Indexing.invertedIndexSubject, Indexing.termMappingSubject);
                    BigConcurentHashMap.mergeInvertedIndex(Indexing.treeIndexBody, "tempBody", Indexing.invertedIndexBody, Indexing.termMappingBody);
                    /*
                     * LinkedHashMap dateList =
                     * BigConcurentHashMap.calculateTermWight(BigConcurentHashMap.dateConcurentMap,
                     * Indexing.N_messagge);
                     * BigConcurentHashMap.printStatistic(dateList, "date",
                     * Indexing.N_messagge); LinkedHashMap fromList =
                     * BigConcurentHashMap.calculateTermWight(BigConcurentHashMap.fromConcurentMap,
                     * Indexing.N_messagge);
                     * BigConcurentHashMap.printStatistic(fromList, "from",
                     * Indexing.N_messagge); LinkedHashMap toList =
                     * BigConcurentHashMap.calculateTermWight(BigConcurentHashMap.toConcurentMap,
                     * Indexing.N_messagge);
                     * BigConcurentHashMap.printStatistic(toList, "to",
                     * Indexing.N_messagge); LinkedHashMap subjectList =
                     * BigConcurentHashMap.calculateTermWight(BigConcurentHashMap.subjectConcurentMap,
                     * Indexing.N_messagge);
                     * BigConcurentHashMap.printStatistic(subjectList,
                     * "subject", Indexing.N_messagge); LinkedHashMap bodyList =
                     * BigConcurentHashMap.calculateTermWight(BigConcurentHashMap.bodyConcurentMap,
                     * Indexing.N_messagge);
                     * BigConcurentHashMap.printStatistic(bodyList, "body",
                     * Indexing.N_messagge); LinkedHashMap allList =
                     * BigConcurentHashMap.calculateTermWight(BigConcurentHashMap.allConcurentMap,
                     * Indexing.N_messagge);
                     * BigConcurentHashMap.printStatistic(allList, "all", Indexing.N_messagge);
                     */

                    /*
                     * synchronized (Indexing.invertedIndexDate) {
                     * IndexController.insertDocIndex2(BigConcurentHashMap.dateConcurentMap,
                     * Indexing.indexDate, Indexing.treeIndexDate,
                     * Indexing.invertedIndexDate);
                     * //BigConcurentHashMap.dateConcurentMap.clear();
                     * //System.out.println(BigConcurentHashMap.dateConcurentMap.size());
                     * } synchronized (Indexing.invertedIndexFrom) {
                     * IndexController.insertDocIndex2(BigConcurentHashMap.fromConcurentMap,
                     * Indexing.indexFrom, Indexing.treeIndexFrom,
                     * Indexing.invertedIndexFrom);
                     * //BigConcurentHashMap.fromConcurentMap.clear(); }
                     * synchronized (Indexing.invertedIndexTo) {
                     * IndexController.insertDocIndex2(BigConcurentHashMap.toConcurentMap,
                     * Indexing.indexTo, Indexing.treeIndexTo,
                     * Indexing.invertedIndexTo);
                     * //BigConcurentHashMap.toConcurentMap.clear(); }
                     * synchronized (Indexing.invertedIndexSubject) {
                     * IndexController.insertDocIndex2(BigConcurentHashMap.subjectConcurentMap,
                     * Indexing.indexSubject, Indexing.treeIndexSubject,
                     * Indexing.invertedIndexSubject);
                     * //BigConcurentHashMap.subjectConcurentMap.clear(); }
                     * synchronized (Indexing.invertedIndexBody) {
                     * IndexController.insertDocIndex2(BigConcurentHashMap.bodyConcurentMap,
                     * Indexing.indexBody, Indexing.treeIndexBody,
                     * Indexing.invertedIndexBody);
                     * //BigConcurentHashMap.bodyConcurentMap.clear(); }
                     *
                     * IndexController.printTermMap(Indexing.indexDate,
                     * Indexing.treeIndexDate, Indexing.termMappingDate);
                     * IndexController.printTermMap(Indexing.indexFrom,
                     * Indexing.treeIndexFrom, Indexing.termMappingFrom);
                     * IndexController.printTermMap(Indexing.indexTo,
                     * Indexing.treeIndexTo, Indexing.termMappingTo);
                     * IndexController.printTermMap(Indexing.indexSubject,
                     * Indexing.treeIndexSubject, Indexing.termMappingSubject);
                     * IndexController.printTermMap(Indexing.indexBody,
                     * Indexing.treeIndexBody, Indexing.termMappingBody);
                     */


                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Total execution time : " + ((System.nanoTime() - startTime) / 1000000000.0) + " secs");
                System.exit(0);
            }
        }

    }
}

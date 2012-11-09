/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import com.query.controller.QueryProcessor;
import java.io.IOException;

/**
 *
 * @author irfannurhakim
 */
public class QueryTerm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        if(args.length != 2 || !args[1].contains(":")){
            System.out.println("Usage : QueryTerm <index_file_location> <field:query_term>");
            System.exit(0);
        }
        
        String field = args[1].split(":")[0];
        String term = args[1].split(":")[1];
        // String field = "body";
        //String term = "enron";

        QueryProcessor.doQuery(field, term);
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

/**
 *
 * @author irfannurhakim
 */
public class QueryTerm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if(args.length != 2 || !args[1].contains(":")){
            System.out.println("Usage : QueryTerm <index_file_location> <field:query_term>");
            System.exit(0);
        }
        
        String field = args[1].split(":")[0];
        String term = args[1].split(":")[1];
        
        
    }
}

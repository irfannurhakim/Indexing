/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexing;

/**
 *
 * @author user
 */
public class NewClass {
    
    public static void main(String[] args) {
        
        new as(6).start();
        new as(7).start();
    }
    
    static class as extends Thread
    {
        int asd;

        public as(int asd) {
            this.asd = asd;
        }

        
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.print(asd);
            }
        }
        
    }
    
}

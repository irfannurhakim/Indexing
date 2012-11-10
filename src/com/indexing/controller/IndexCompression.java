/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indexing.controller;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class IndexCompression {

    public static int[] gapEncode(int[] input) {
        int[] hasil = new int[input.length];
        int temp = 0;
        for (int i = 0; i < hasil.length; i++) {
            if (i != 0) {
                hasil[i] = input[i] - input[i - 1];
            } else {
                hasil[i] = input[i];
            }
        }
        return hasil;
    }

    public static int[] gapDecode(int[] input) {
        int[] hasil = new int[input.length];
        int temp = 0;
        for (int i = 0; i < hasil.length; i++) {
            if (i != 0) {
                hasil[i] = input[i] + hasil[i - 1];
            } else {
                hasil[i] = input[i];
            }
        }
        return hasil;
    }
//    public static void encode (int [] input , ByteBuffer output)
//    {
//        for (int i : input) {
//            
//            while(i>=128)
//            {
//                //output.putInt(i & 0x7F);
//                System.out.println((i & 0x7F));
//                i>>>=7;
//            }
//            System.out.println((i | 0x80));
//            //output.putInt(i | 0x80);
//        }
//    }
//    
//    public static void decode (byte[] input, IntBuffer ouput)
//    {
//        for (int i = 0; i < input.length; i++) {
//            int position=0;
//            int result = ((int) input[i] & 0x7F);
//            while ((input[i] & 0x80) == 0) {                
//                i+=1;
//                position+=1;
//                int unsignByte = ((int) input[i] & 0x7F);
//                result |= (unsignByte << (7*position));
//            }
//            ouput.put(result);
//        }
//    }

    private static void innerEncode(int num, List<Byte> resultList) {

        int headNum = resultList.size();

        while (true) {
            byte n = (byte) (num % 128);
            resultList.add(headNum, n);
            if (num < 128) {
                break;
            }
            num = num >>> 7;
        }

        int lastIndex = resultList.size() - 1;
        Byte val = resultList.get(lastIndex);
        val = (byte) (val.byteValue() - 128);
        resultList.remove(lastIndex);
        resultList.add(val);

    }
    static int mask8bit = (1 << 8) - 1;

    public static byte[] encode(int[] numbers, boolean useArraySort) {


        if (useArraySort) {
            Arrays.sort(numbers);
        }

        List<Byte> resultList = new ArrayList<Byte>();
        int beforeNum = 0;
        for (int num : numbers) {
            innerEncode(num - beforeNum, resultList);
            beforeNum = num;
        }
        int listNum = resultList.size();

        byte[] resultArray = new byte[listNum + 4];
        int num = numbers.length;

        resultArray[0] = (byte) ((num >> 24) & mask8bit);
        resultArray[1] = (byte) ((num >> 16) & mask8bit);
        resultArray[2] = (byte) ((num >> 8) & mask8bit);
        resultArray[3] = (byte) (num & mask8bit);

        for (int i = 0; i < listNum; i++) {
            resultArray[ i + 4] = resultList.get(i);
        }

        return resultArray;

    }

   

        public static String encodeToString(int[] numbers, boolean useArraySort)
        {
            StringBuilder sb = new StringBuilder();
             byte[] bs = encode(numbers, useArraySort);
                    for(int i=0;i<bs.length;i++){
                        String tmp = Integer.toHexString(bs[i]);

                        if(tmp.length()<2) 
                            tmp = "0" + tmp;
                        else if(tmp.length()>2){
                            tmp = tmp.substring(tmp.length()-2);
                        }
                        sb.append(tmp);   
                    }
             return (sb.toString());
        }
        public static int[] decode ( byte[] encodedValue, boolean useGapList ){

                int dataNum =   ( ( encodedValue[0] & mask8bit ) << 24  | ( encodedValue[1] & mask8bit ) << 16 )
                                                                                                                                |
                                                ( ( encodedValue[2] & mask8bit ) <<  8  | ( encodedValue[3] & mask8bit ) );

                int[] decode = new int[dataNum];
                int id  = 0;
                int n   = 0;
                for( int i =4 ; i < encodedValue.length ; i++ ){

                        if( 0 <= encodedValue[i] )
                                n = ( n << 7 ) + encodedValue[i];
                        else{
                                n = ( n << 7 ) + ( encodedValue[i] + 128 );
                                decode[id++] = n;
                                n = 0;
                        }

                }

                if(useGapList)
                        for( int j =1 ; j < dataNum ; j++ )
                                decode[j] += decode[j-1];

                return decode;
        }
        
        public static String decodeFromString(String sb, boolean useGapList)
        {
            String hasil="";
             ArrayList<Byte> abs = new ArrayList<Byte>();
                    int res[];
                    byte from[];
                    for(int i=0;i<sb.length()/2;i++){
                        String temp = sb.substring((i*2), i*2+2);                                   
                       abs.add((byte)Integer.parseInt(temp, 16));
                    }
                    
                    from = new byte[abs.size()];
                    for(int i=0;i<from.length;i++){
                        from[i] = abs.get(i);
                    }
                    
                    res = decode(from, true);
                    for(int i=0;i<res.length;i++) 
                    {
                        hasil+=res[i] + " " ;
                    
                    }
                    return hasil;
        }
    
    public static void main(String[] args) {
        
         StringBuilder sb = new StringBuilder();
                   int[] a = new int[]{1,5,9,18,23,24,30,44,45,48};
                   
                   String as = encodeToString(a, false);
                   System.out.println(as);
                   String asd = decodeFromString("818484898581868e8183", true);
                   System.out.println(asd);
        
//        int [] a = {1,5,9,18,23,24,30,44,45,48};
//        
//        byte [] test = encode(a, false);
//        
//       String as = new String(test);
//        System.out.println(as);
//        System.out.println("");
//        for (int i = 0; i < test.length; i++) {
//            //System.out.print(test[i]+"|");
//            System.out.print(Integer.toHexString(test[i])+"|");
//        }
//        String as =test.toString();
//        System.out.println(test.toString());
//        System.out.println("");
//        int [] b = decode(as.getBytes(), true);
//        for (int i = 0; i < b.length; i++) {
//            System.out.print(b[i]+"|");
//        }
        
        /*int[] b = gapEncode(a);
        
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i]+"|");
        }
         */
//        ByteBuffer test = ByteBuffer.allocate(100);
//        
//        encode(a,test);
//        byte[] bytearr = new byte[test.remaining()];
//        test.get(bytearr);
//        String s = new String(bytearr);
//        System.out.println(s);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indexing.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author user
 */
public class IndexCompression2 {
    
    public static LinkedList<Integer> gapEncode(LinkedList<Integer> input) {
         Collections.sort(input);
        LinkedList<Integer> hasil = new LinkedList<Integer>();
        for (int i = 0; i < input.size(); i++) {
            if (i != 0) {
                hasil.add(input.get(i)-input.get(i-1)) ;
                        //hasil[i]= input[i] - input[i - 1];
            } else {
                hasil.add(input.get(i));
                //hasil[i] = input[i];
            }
        }
        return hasil;
    }

    public static ArrayList<Integer> gapDecode(ArrayList<Integer> input) {
        ArrayList<Integer> hasil = new ArrayList<Integer>();
        for (int i = 0; i < input.size(); i++) {
            if (i != 0) {
                
                hasil.add(input.get(i)+hasil.get(i-1)) ;
                //hasil[i] = input[i] + hasil[i - 1];
            } else {
                hasil.add(input.get(i));
            }
        }
        return hasil;
    }
    public static class Byte {
	int[] abyte;

	Byte(){
	    abyte = new int[8];
	}

	public void readInt(int n) {
	    // n must be less than 128 !!
            
                String bin = Integer.toBinaryString(n);

	    for(int i = 0 ; i < (8 - bin.length()) ; i++) {
		abyte[i] = 0;
	    }
	    for(int i = 0 ; i < bin.length() ; i++){
		abyte[i + (8 - bin.length())] = bin.charAt(i) - 48; // ASCII code for '0' is 48
	    }
            
	    
	    //System.out.println(" Byte ***** " + this.toString());
	}

	public void switchFirst(){
	    abyte[0] = 1;
	}

	public int toInt(){
	    //System.out.println(" Byte ***** " + this.toString());
	    int res = 0;
	    for (int i = 0 ; i < 8 ; i++){
		res += abyte[i] * Math.pow(2, (7 - i));
	    }
	    //System.out.println(" Value ***** " + res);
	    return res;
	}

	public String toString(){
	    String res ="";
	    for(int i = 0 ; i < 8 ; i++) {
		res += abyte[i];
	    }
	    return res;
	}

    }


    public static ArrayList<Byte> vbEncode(LinkedList<Integer> numbers) {
        
        LinkedList<Integer> numbers2 = gapEncode(numbers);
	ArrayList<Byte> code = new ArrayList<Byte>();
        try {
            
           
	while (numbers2.size() > 0) {
	    int n = numbers2.poll();
	    code.addAll(vbEncodeNumber(n));
	}
        } catch (Exception e) {
            System.out.println(numbers);
            System.out.println(numbers2);
            e.printStackTrace();
            System.exit(0);
        }
        
	return code;
    }

    public static LinkedList<Byte> vbEncodeNumber(int n) {
	LinkedList<Byte> bytestream = new LinkedList<Byte>();
	int num = n;
	while (true) {
	    Byte b = new Byte();
	    b.readInt(num % 128);
	    bytestream.addFirst(b);
	    if (num < 128) {break;}
	    num /= 128;     //right-shift of length 7 (128 = 2^7)
	} 
	Byte last = bytestream.get(bytestream.size() - 1); //retrieving the last byte
	last.switchFirst(); //setting the continuation bit to 1
	return bytestream;
    }

    public static ArrayList<Integer> vbDecode(LinkedList<Byte> code){
	ArrayList<Integer> numbers = new ArrayList<Integer>();
	int n = 0;
	for(int i = 0 ; !(code.isEmpty()) ; i++){
	    Byte b = code.poll(); // read leading byte
	    //System.out.println(" Reading byte " + b.toString() );

	    int  bi = b.toInt();  // decimal value of this byte
	    if (bi < 128) {       //continuation bit is set to 0
		n = 128 * n + bi;
	    } else {              // continuation bit is set to 1
		n = 128 * n + (bi - 128);
		numbers.add(n);   // number is stored
		n = 0;            // reset
	    }
	}
        numbers= gapDecode(numbers);
	return numbers;
    }
    
    public static String VByteToString(LinkedList<Integer> test)
    {
        String hasil="";
        ArrayList<Byte> code = vbEncode(test);
        //System.out.println(code);
        for(int i = 0 ; i < code.size() ; i++){
            String as=Integer.toHexString(code.get(i).toInt());
            if(as.length()<2)
            {
                hasil+="0" +as+ "";
                //System.out.println(as);
            }
            else
            {
                hasil+=as+ "";
            }
	}
        return  hasil;
    }
    
    public static ArrayList<Integer> StringToVByte (String sb)
    {
         LinkedList<Byte> abs = new LinkedList<Byte>();
                    int res[];
                    byte from[];
                    for(int i=0;i<sb.length()/2;i++){
                        String temp = sb.substring((i*2), i*2+2); 
                        //System.out.println(temp);
                        //String asd = Integer.
                        Byte tempb = new Byte();
                        tempb.readInt(Integer.parseInt(temp, 16));
                       abs.add(tempb);
                    }
        // System.out.println(abs);
         return vbDecode(abs);
    }

    public static void main(String[] args) {
        //96=4:200;5:2642;183:369,381;724:944;878:944;
        //256=848101b2049d019a;01c8|14d2|02f18c|07b0|07b0
         //77=5:181;142:1046;158:1046;159:1046;
        //77=85019202af03ce;01b5|0896|0896|0896
        String test = "848101b2049d019a;01c8:14d2:02f18c:07b0:07b0";
        String tests[] =test.split(";");
        String posID[]= tests[1].split(":");
        ArrayList<Integer> docID = StringToVByte(tests[0]);
        String temp="";
        for (int i = 0; i < docID.size(); i++) {
            temp+=docID.get(i)+":";
            ArrayList<Integer> posIDs =  StringToVByte(posID[i]);
            String temp2="";
            for (int j = 0; j < posIDs.size(); j++) {
                temp2+=posIDs.get(j)+",";
            }
            temp2=temp2.substring(0, temp2.length()-1);
            temp+=temp2+";";
            
        }
        System.out.println(temp);
//	LinkedList<Integer> testa = new LinkedList<Integer>();
//        
//        int[] a = new int[]{1,5,9,18,23,124,130,444,645,9048};
//	       for (int i : a) {
//            testa.add(i);
//        }
//               System.out.println(testa);
//               LinkedList<Integer> asd=gapEncode(testa);
//               System.out.println(asd);
//             // ArrayList<Integer> asdf= new ArrayList<>(asd);
//               ArrayList<Integer> asdf = gapDecode(new ArrayList<Integer>(asd));
//               System.out.println(asdf);
               
              /* String as = VByteToString(test);
               System.out.println(as);
               
               
               ArrayList<Integer> ab = StringToVByte(as);
               System.out.println(ab);*/
	//System.out.println("Input values: 5 - 824 - 1234");

          
//	LinkedList<Byte> code = vbEncode(test);
//
//	System.out.println("Variable-byte code:");
//	for(int i = 0 ; i < code.size() ; i++){
//	    System.out.print(Integer.toHexString(code.get(i).toInt()) + " ");
//	}
//	System.out.println();
//
//	LinkedList<Integer> decode = vbDecode(code);
//
//	System.out.println("After decoding:");
//	for(int i = 0 ; i < decode.size() ; i++){
//	    System.out.print(decode.get(i) + " ");
//	}
//	System.out.println();
    }
}

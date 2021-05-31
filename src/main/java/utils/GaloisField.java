package utils;


import java.util.BitSet;

public class GaloisField {
    public static Byte add(Byte b1, Byte b2){
        // convert to ints and xor
        int one = (int)b1;
        int two = (int)b2;
        int xor = one ^ two;

        // convert back to byte
        return (byte)(0xff & xor);
    }

    public static Byte product(Byte b1, Byte b2, int generator){
        String byte2 = Binary.getBinary(b2);
        int tmp = 0;
        boolean firstTime = true;
        for(int i = 0;i<byte2.length();i++){
            if(byte2.charAt(i) == '1') {
                int aux = ((int)b1) << byte2.length()-i-1;
                if(firstTime) {
                    tmp = aux;
                    firstTime = false;
                }else
                    tmp = tmp ^ aux;
            }
        }
        System.out.println(tmp);
        System.out.println(tmp >>> generator);
        System.out.println(tmp << 1);
        return (byte)tmp;
    }

    public static void main(String[] args) {
        Integer num1 = 84, num2 = 13;
        //System.out.println(Integer.toBinaryString(num1.byteValue() << 5));
        //System.out.println(product(num1.byteValue(),num2.byteValue(),(byte)0)); //Print as int
        //System.out.println(Binary.getBinary(product(num1.byteValue(),num2.byteValue(),(byte)0))); // Print as binary
        byte tmp = product(num1.byteValue(),num2.byteValue(),355);
        System.out.println(Binary.getBinary(tmp)+ " - " + tmp) ;

    }
    public static long getUnsignedInt(int x) {
        return x & (-1L >>> 32);
    }

}

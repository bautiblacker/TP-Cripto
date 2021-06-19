package models;

import lombok.*;
import utils.Binary;

import java.util.List;

@Data
public class Carrier {
    private byte[] header;
    private List<List<Byte>> imageBlockBytes;
    private String filePath;
    private int height;
    private int width;

    public Carrier(List<List<Byte>> imageBlockBytes, String filePath, byte[] header, int width,int height) {
        this.imageBlockBytes = imageBlockBytes;
        this.filePath = filePath;
        this.header = header;
        this.width = width;
        this.height = height;
    }

    public static Byte getFXFromBlock(Byte w, Byte v, Byte u) throws Exception {
        boolean[] wAsBitArray = Binary.toBits(w);
        boolean[] vAsBitArray = Binary.toBits(v);
        boolean[] uAsBitArray = Binary.toBits(u);
        boolean[] fxAsBitArray = new boolean[8];

        int index = 0;
        fxAsBitArray[index++] = wAsBitArray[5];
        fxAsBitArray[index++] = wAsBitArray[6];
        fxAsBitArray[index++] = wAsBitArray[7];
        fxAsBitArray[index++] = vAsBitArray[5];
        fxAsBitArray[index++] = vAsBitArray[6];
        fxAsBitArray[index++] = vAsBitArray[7];
        fxAsBitArray[index++] = uAsBitArray[6];
        fxAsBitArray[index++] = uAsBitArray[7];

        int count = 0;
        for(int i = 0;i < fxAsBitArray.length;i++){
            if(fxAsBitArray[i])
                count++;
        }
        int parity = uAsBitArray[5] ? 1 : 0;

        if(count % 2 == parity)
            throw new Exception("Bit parity check");

        return Binary.toByte(fxAsBitArray);
    }

    public void setXAtIndex(int index, Byte fx, Byte x) {
        imageBlockBytes.get(index).set(0,x);
        boolean[] fxAsBitArray = Binary.toBits(fx);
        List<Byte> block = imageBlockBytes.get(index);
        int xIndex = 0;
        boolean[] blockSquareAsBitArray;
        int i;
        for(i = 1; i < block.size() - 1; i++) {
            Byte blockSquare = block.get(i);
            blockSquareAsBitArray = Binary.toBits(blockSquare);
            blockSquareAsBitArray[5] = fxAsBitArray[xIndex++];
            blockSquareAsBitArray[6] = fxAsBitArray[xIndex++];
            blockSquareAsBitArray[7] = fxAsBitArray[xIndex++];
            block.set(i, Binary.toByte(blockSquareAsBitArray));
        }

        blockSquareAsBitArray = Binary.toBits(block.get(i));
        blockSquareAsBitArray[5] = getParityBit(fxAsBitArray);
        blockSquareAsBitArray[6] = fxAsBitArray[xIndex++];
        blockSquareAsBitArray[7] = fxAsBitArray[xIndex];
        block.set(i, Binary.toByte(blockSquareAsBitArray));
    }

    private boolean getParityBit(boolean[] bitArray) {
        int counter = 0;
        for(boolean b : bitArray) {
            counter += b ? 1 : 0;
        }

        return counter % 2 == 0;
    }

    public List<List<Byte>> getImageBlockBytes() {
        return imageBlockBytes;
    }

    public void saveToFile(String fileName) {

    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

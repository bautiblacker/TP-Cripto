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

    public void setXAtIndex(int index, Byte fx) {
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

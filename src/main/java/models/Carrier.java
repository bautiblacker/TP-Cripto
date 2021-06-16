package models;

import lombok.*;
import utils.Binary;

import java.util.List;

@Data
@AllArgsConstructor
public class Carrier {
    private List<List<Byte>> imageBlockBytes;

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
            block.set(i, Binary.toInt(blockSquareAsBitArray));
        }

        blockSquareAsBitArray = Binary.toBits(block.get(i));
        blockSquareAsBitArray[5] = getParityBit(fxAsBitArray);
        blockSquareAsBitArray[6] = fxAsBitArray[xIndex++];
        blockSquareAsBitArray[7] = fxAsBitArray[xIndex];
        block.set(i, Binary.toInt(blockSquareAsBitArray));
    }

    private boolean getParityBit(boolean[] bitArray) {
        int counter = 0;
        for(boolean b : bitArray) {
            counter += b ? 1 : 0;
        }

        return counter % 2 == 0;
    }


}

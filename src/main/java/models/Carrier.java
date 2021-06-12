package models;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class Carrier {
    private List<List<Byte>> imageBlockBytes;
}

package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Data
@AllArgsConstructor
public class Config {

    private int k;
    private String hideInDirectory;
    private String recoverFormDirectory;
    private boolean d;
    private boolean r;
    private File secretImage;

    public Config(String[] args) {
        try {
            parse(args);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to locate one of the provided files. Make sure that the files exist.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("The value provided for K is invalid. Please try again.");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough parameters! Parameters are [d/r] [SecretImagePath] [K] [CarrierImagesPath]");
            System.exit(1);
        }
    }

    /**
     * Parse console arguments.
     * K: the minimum amount of needed shadows to recover the secret image
     * D: the image given is hidden
     * R: the image give needs to be hidden
     * I: the image.
     * F: path to the folder in which the image is hidden or will be hidden
     *
     * @param args the console arguments
     */
    private void parse(String[] args) throws FileNotFoundException, NumberFormatException, ArrayIndexOutOfBoundsException {
        if(args[0].equals("d")) {
//            System.out.println("Hiding secret image...");
            this.d = true;
            this.hideInDirectory = args[3];
            this.secretImage = FileUtils.parseFile(args[1]);
        } else {
//            System.out.println("Recovering secret image...");
            this.r = true;
            this.recoverFormDirectory = args[3];
            this.secretImage = new File(args[1]);
        }
        this.k = Integer.parseInt(args[2]);
    }

    public int getK() {
        return k;
    }

    public String getHideInDirectory() {
        return hideInDirectory;
    }

    public String getRecoverFormDirectory() {
        return recoverFormDirectory;
    }

    public boolean isD() {
        return d;
    }

    public boolean isR() {
        return r;
    }

    public File getSecretImage() {
        return secretImage;
    }
}

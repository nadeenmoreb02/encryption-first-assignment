import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Decryption {


    static byte[] extractEmbeddedBytes(BufferedImage img, int size){

        int i = 0;
        int j = 0;
        //bytes are hidden bytes within embedded image
        byte [] bytes = new byte[size];

        for(int l=0;l<size;l++){
            for(int k=0 ; k<8 ; k++){
                int rgb = img.getRGB(j, i);
                bytes[l] = (byte) ((bytes[l]<<1) | ((rgb >> 0) & 1));
                j++;
            }
            i++;
        }
        return bytes;

    }
    static byte[] extractEmbeddedBytes2(BufferedImage img, int size){


        int width = img.getWidth()-1;
        int height = img.getHeight()-1;
        byte [] bytes = new byte[size];

        for(int l=0;l<size;l++){
            for(int k=0 ; k<8 ; k++){
                int rgb = img.getRGB(width, height);
                bytes[l] = (byte) ((bytes[l]<<1) | ((rgb >> 0) & 1));
                width--;
            }
            height--;
        }
        return bytes;

    }
    static String revealMsg(int msgLen){
        BufferedImage img = getNewImage();
        byte [] msgBytes = extractEmbeddedBytes(img,msgLen);
        if(msgBytes == null)
            return null;
        String msg = new String(msgBytes);
        return (msg);
    }
    static String revealMsg2(int msgLen){
        BufferedImage img = getNewImage();
        byte [] msgBytes = extractEmbeddedBytes2(img,msgLen);
        if(msgBytes == null)
            return null;
        String msg = new String(msgBytes);
        return (msg);
    }

    //fetch new embedded image that has the secret message
    static BufferedImage getNewImage(){
        File f = new File("C:\\Users\\AL-YAMEN\\Desktop\\steganography\\output\\new.png");
        BufferedImage img = null;
        try{
            img = ImageIO.read(f);
        }
        catch(Exception ex)
        {}
        return img;
    }

    static void createFileWithMessage(String extractedMsg){
        String filePath = "C:\\Users\\AL-YAMEN\\Desktop\\steganography\\output\\extractedFile.txt"; // Replace with the desired file path and name
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(extractedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

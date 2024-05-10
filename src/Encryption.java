import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Encryption {


    public static void main(String[] args) {

        // Load the original image and embedded image

        try {
            BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\AL-YAMEN\\Desktop\\steganography\\input\\ART-743-5.jpg"));
            BufferedImage embeddedImage = ImageIO.read(new File("C:\\Users\\AL-YAMEN\\Desktop\\steganography\\input\\ART-743-5.jpg"));

            // path of the file of the secret message
            String filePath = "C:\\Users\\AL-YAMEN\\Desktop\\steganography\\input\\message.txt";

            // Create a File object
            File messageFile = new File(filePath);
            Path fileName
                    = Path.of("C:\\Users\\AL-YAMEN\\Desktop\\steganography\\input\\message.txt");

            // read the file
            String str = Files.readString(fileName);
            String[] arrOfWords = str.split(" ");

            int chunksNumber = arrOfWords.length;

            HashMap<Integer, String> chunks = new HashMap<Integer, String>();

            for(int i=0; i<chunksNumber; i++){
                chunks.put(i, arrOfWords[i]);
            }
            for (Integer i : chunks.keySet()) {
                System.out.println("key: " + i + " value: " + chunks.get(i));
            }

            //byte chunks hashmap
            HashMap<Integer, byte[]> byteChunks = new HashMap<Integer, byte[]>();

            for(int i=0; i<chunksNumber; i++){
                byte[] byteArr = chunks.get(i).getBytes();
                byteChunks.put(i, byteArr);
            }

            // Printing the string
            System.out.println("str: " + str);

            // Ensure the images have the same dimensions
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int imageSize = width * height;

            if(imageSize<str.length()){
                System.out.println("Image size is smaller than message length, can't embed or hide the message!");
                return;
            }

            if(imageSize< messageFile.length()){
                System.out.println("Image size is smaller than message length, can't embed or hide the message!");
                return;
            }

            int messageIndex = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (messageIndex < byteChunks.get(0).length) {
                        int pixel = originalImage.getRGB(x, y);
                        int newPixel = (pixel & 0xFFFFFFFE) | (byteChunks.get(0)[messageIndex] & 1);
                        System.out.println("pixel: " + pixel);
                        System.out.println("new pixel: " + newPixel);
                        embeddedImage.setRGB(x, y, newPixel);
                        messageIndex++;
                    }
                }
            }

            hideMessage(originalImage, byteChunks);


        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    static void hideMessage(BufferedImage img, HashMap<Integer, byte[]> txt) throws Exception{

        int i = 0;
        int j = 0;
        int height = img.getHeight()-1;
        int width = img.getWidth()-1;

        int textLength1=0;
        int textLength2=0;
        int[] chunksSizes = new int[txt.size()];
        for(Integer n: txt.keySet()) {
            chunksSizes[n] = txt.get(n).length;
            if(n%2==0){
                textLength1+=txt.get(n).length;
            }
            else{
                textLength2+=txt.get(n).length;
            }
        }

        for(Integer n: txt.keySet()){
            if(n%2==0){
                for(byte b : txt.get(n)){
                    for(int k=7;k>=0;k--){
                        int bitVal = getBitValue(b, k);
                        int rgb = img.getRGB(j,i);
                        rgb = setBitValue( rgb, 0, bitVal);
                        img.setRGB(j,i, rgb);
                        if(j>width){
                            System.out.println("no more space");
                            return;
                        }
                        j++;
                    }
                    i++;
                }}
            else{
                for(byte b : txt.get(n)){
                    for(int k=7;k>=0;k--){
                        int bitVal = getBitValue(b, k);
                        int rgb = img.getRGB(width, height);
                        rgb = setBitValue( rgb, 0, bitVal);
                        img.setRGB(width, height, rgb);
                        width--;
                    }
                    height--;
                }}
        }

        System.out.println("Secret Message has been Embedded Successfully");
        createEmbeddedImg(img);
        System.out.println("Type Yes to decode or End To end the program");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        switch(in.readLine().trim()){
            case "Yes":
            case "YES":
            case "yes":
            {
                String k = Decryption.revealMsg(textLength1);
                String k2  = Decryption.revealMsg2(textLength2);
                String k3="";
                StringBuffer str1 = new StringBuffer(k+" ");
                StringBuffer str2 = new StringBuffer(k2+" ");
                StringBuffer str3 = new StringBuffer("");
                int prevSize =0;
                int s1=0;
                int s2=0;
                for(Integer n: txt.keySet()) {
                    if(n%2==0){
                        int spaceIndex = chunksSizes[n];
                        str3.append(str1.subSequence(s1,spaceIndex+s1));
                        s1+=spaceIndex;

                    }
                    else{
                        int spaceIndex = chunksSizes[n];
                        str3.append(str2.subSequence(s2,spaceIndex+s2));
                        s2+=spaceIndex;
                    }}

                for(Integer n: txt.keySet()) {
                    int spaceIndex = chunksSizes[n];

                    str3.insert(prevSize+spaceIndex, " ");
                    prevSize+=spaceIndex+1;

                }
                System.out.println("Text is: " + str3);
                Decryption.createFileWithMessage(String.valueOf(str3));
            }
            break;
            default:
                System.out.println("Program Ended");
                break;
        }
    }

    static void createEmbeddedImg(BufferedImage img){
        try{
            File output = new File("C:\\Users\\AL-YAMEN\\Desktop\\steganography\\output\\new.png");
            ImageIO.write(img, "png", output);
        }
        catch(Exception ex)
        {}
    }
    // Get the bit value at the specified location within a byte
    private static int getBitValue(byte b, int location) {
        return (b >> location) & 1;
    }

    // Set the bit at the specified location within a byte to the given bit value
    private static int setBitValue(int n, int location, int bit) {
        if (bit == 1) {
            n |= (1 << location); // Set the bit to 1
        } else {
            n &= ~(1 << location); // Set the bit to 0
        }
        return n;
    }


}



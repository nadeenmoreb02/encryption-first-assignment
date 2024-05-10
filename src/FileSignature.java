import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class FileSignature {
    public static void main(String[] args) throws Exception {
        try {
            int choice;
            //menu
            System.out.println("Hello, Please choose from the following menu by typing in the number:");
            System.out.println("1- Create Signature and embed it");
            System.out.println("2- Extract Signature and verify it for Text File");
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            switch (choice){
                case 1: {
                    // Load the original image
                    BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\AL-YAMEN\\Desktop\\watermark\\ART-743-5.jpg"));

                    //
                    //getting text file content
                    String filePath = "C:\\Users\\AL-YAMEN\\Desktop\\watermark\\message.txt";
                    //Load the file
                    BufferedReader reader = new BufferedReader(new FileReader(filePath));
                    String line;
                    StringBuilder content = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }

                    reader.close();

                    // Now, the content of the file is stored in the 'content' string variable
                    String fileContent = content.toString();
                    //System.out.println(fileContent);
                    //

                    //create the file signature as a string
                    String signature = createSignature(fileContent);
                    System.out.println("signature is : " + signature);

                    // Embed watermark signature using LSB
                    hideText(originalImage, signature.getBytes());

                    break; }
                case 2:{
                    // Load the original image
                    BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\AL-YAMEN\\Desktop\\watermark\\ART-743-5.jpg"));

                    //
                    //getting text file content
                    String filePath = "C:\\Users\\AL-YAMEN\\Desktop\\watermark\\message.txt";
                    //Load the file
                    BufferedReader reader = new BufferedReader(new FileReader(filePath));
                    String line;
                    StringBuilder content = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }

                    reader.close();

                    // Now, the content of the file is stored in the 'content' string variable
                    String fileContent = content.toString();
                   // System.out.println(fileContent);
                    //

                    //create the file signature as a string
                    String signature = createSignature(fileContent);
                    System.out.println("signature is : " + signature);


                    //new file to compare with the original
                    String newPath = "C:\\Users\\AL-YAMEN\\Desktop\\watermark\\new.txt";
                    //Load the file
                    BufferedReader reader2 = new BufferedReader(new FileReader(newPath));
                    String line2;
                    StringBuilder content2 = new StringBuilder();

                    while ((line2 = reader2.readLine()) != null) {
                        content2.append(line2).append("\n");
                    }

                    reader2.close();

                    // Now, the content of the file is stored in the 'content' string variable
                    String fileContent2 = content2.toString();
                  //  System.out.println(fileContent2);
                    //

                    //create the file signature as a string
                    String signature2 = createSignature(fileContent2);
                    System.out.println("signature 2 is : " + signature2);
                    if(signature.equals(signature2)){
                        System.out.println("the file content is the same as the original");
                    }
                    else{
                        System.out.println("the file content has been changed and is different from the original text file");
                    }
                    break;
            }
        }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create signature and return it as a string method
    static String createSignature(String fileContent) {
        //signature = (numSmallLetters + numCapitalLetters*2 + numberOfWords) /2
        int numberOfWords = 0;
        int numberOfSmallLetters = 0;
        int numberOfCapitalLetters = 0;
        //calculate number of words
        String[] words = fileContent.split(" ");
        numberOfWords = words.length;
        //calculate number of small letters and capital letters
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length(); j++) {
                if (words[i].charAt(j) >= 'A' && words[i].charAt(j) <= 'Z') {
                    numberOfCapitalLetters++;
                } else if (words[i].charAt(j) >= 'a' && words[i].charAt(j) <= 'z') {
                    numberOfSmallLetters++;
                }
            }
        }

        //calculate the signature
        int signature = (numberOfSmallLetters + numberOfCapitalLetters * 2 + numberOfWords) / 2;

        //return signature as a string
        return "" + signature;
    }



    static void createImgWithMsg(BufferedImage img) {
        try {
            File ouptut = new File("C:\\Users\\AL-YAMEN\\Desktop\\watermark\\watermarked_image.png");
            ImageIO.write(img, "png", ouptut);
        } catch (Exception ex) {
        }
    }

    static byte[] extractBytes(BufferedImage img, int size){

        int i = 0;
        int j = 0;
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
    static String revealMsg(int messageLength){
        BufferedImage img = getNewImage();
        byte [] msgBytes = extractBytes(img,messageLength);
        if(msgBytes == null)
            return null;
        String msg = new String(msgBytes);
        return (msg);
    }
    static BufferedImage getNewImage(){
        File f = new File("C:\\Users\\AL-YAMEN\\Desktop\\watermark\\watermarked_image.png");
        BufferedImage img = null;
        try{
            img = ImageIO.read(f);
        }
        catch(Exception ex)
        {}
        return img;
    }
    static void createFileWithMessage(String extractedMsg){
        String filePath = "C:\\Users\\AL-YAMEN\\Desktop\\steganography\\output\\extractedFile.txt";
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



    static void hideText(BufferedImage img, byte[] txt) throws Exception{

        int i = 0;
        int j = 0;
        for(byte b : txt){
            for(int k=7;k>=0;k--){
                int bitVal = getBitValue(b, k);
                int rgb = img.getRGB(j,i);
                rgb = setBitValue( rgb, 0, bitVal);
                img.setRGB(j,i, rgb);
                if(j>img.getWidth()){
                    System.out.println("no more space");
                    return;
                }
                j++;
            }
            i++;
        }

        System.out.println("Text is Embedded successfully");
        createImgWithMsg(img);

        String k = revealMsg(txt.length);
        //System.out.println("Text is: " + k);
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
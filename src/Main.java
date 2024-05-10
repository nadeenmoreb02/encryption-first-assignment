// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        double imgSize = 250000;

        double  fileSize = 18 ;

        //a formula to generate a number from 1 to 10 based on the stego/cover file size and the size of the secret message
        int chunkSize = (int)((imgSize* fileSize) % 10 + 1);


        System.out.printf("result: " + chunkSize);


    }
}
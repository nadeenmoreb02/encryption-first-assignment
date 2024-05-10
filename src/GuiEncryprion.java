import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class GuiEncryprion extends JFrame implements ActionListener {

   private int textLength1=0;
   private int textLength2=0;

    //byte chunks hashmap
    HashMap<Integer, byte[]> byteChunks = new HashMap<Integer, byte[]>();

    int[] chunksSizes = new int[byteChunks.size()];
    private JTextArea textArea;
    JButton open = new JButton("Choose Original Image");
    JButton embed = new JButton("Embed");
    JButton save = new JButton("Save into new file");
    JButton openFile = new JButton("Open Secret Message File");

    JButton reset = new JButton("Reset");

    JButton openDecode = new JButton("Open to Decode");
      JButton extract = new JButton("Extract");

    //  JTextArea message = new JTextArea(10,3);
    JLabel uploadFileLabel = new JLabel("Upload the file of secret message you want to embed");
    BufferedImage sourceImage = null;
    BufferedImage embeddedImage = null;
    BufferedImage imageToDecode = null;
    File textFile = null;
    JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JScrollPane originalPane = new JScrollPane();
    static JScrollPane embeddedPane = new JScrollPane();

    public GuiEncryprion() {
        super("Embed secret message in image");
        designInterface();
        openFile.setBackground(Color.white);
        openFile.setForeground(Color.black);
        openFile.setFont(new Font("Arial", Font.BOLD, 20));


        open.setBackground(Color.white);
        open.setForeground(Color.black);
        open.setFont(new Font("Arial", Font.BOLD, 20));

        embed.setBackground(Color.white);
        embed.setForeground(Color.black);
        embed.setFont(new Font("Arial", Font.BOLD, 20));

        save.setBackground(Color.white);
        save.setForeground(Color.black);
        save.setFont(new Font("Arial", Font.BOLD, 20));

        openDecode.setBackground(Color.white);
        openDecode.setForeground(Color.black);
        openDecode.setFont(new Font("Arial", Font.BOLD, 20));

        extract.setBackground(Color.white);
        extract.setForeground(Color.black);
        extract.setFont(new Font("Arial", Font.BOLD, 20));

        reset.setBackground(Color.white);
        reset.setForeground(Color.black);
        reset.setFont(new Font("Arial", Font.BOLD, 20));

        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        sp.setDividerLocation(0.5);
        this.validate();
    }

    public static void main(String[] args) {
        GuiEncryprion embedMessage = new GuiEncryprion();
    }
    BufferedImage hideMessage(BufferedImage img, HashMap<Integer, byte[]> txt) throws Exception{

        int i = 0;
        int j = 0;
        int height = img.getHeight()-1;
        int width = img.getWidth()-1;
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

                                JOptionPane.showMessageDialog(this, "image size is smaller than message size!",
                                        "Can't Embed!", JOptionPane.ERROR_MESSAGE);

                            return null;
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
        return img;
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

    private void designInterface() {
        JPanel p = new JPanel(new FlowLayout());
        p.add(openFile);
        p.add(open);
        p.add(embed);
        p.add(save);
        p.add(openDecode);
        p.add(extract);
        p.add(reset);
        p.setBackground(Color.black);
        this.getContentPane().add(p, BorderLayout.SOUTH);
        openFile.addActionListener(this);
        open.addActionListener(this);
        embed.addActionListener(this);
        save.addActionListener(this);
        openDecode.addActionListener(this);
        extract.addActionListener(this);
        reset.addActionListener(this);


        sp.setLeftComponent(originalPane);
        sp.setRightComponent(embeddedPane);
        sp.setBackground(Color.black);

        originalPane.setBackground(Color.black);
        embeddedPane.setBackground(Color.black);

        this.getContentPane().add(sp, BorderLayout.CENTER);
    }
    public void actionPerformed(ActionEvent a) {
        //  originalPane.setBorder(BorderFactory.createTitledBorder("Original Image"));

        // Create a titled border with a custom title, font, size, and color
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder("Original Image");
        titledBorder1.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder1.setTitleColor(Color.white);


        // Create a titled border with a custom title, font, size, and color
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder("Embedded Image");
        titledBorder2.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder2.setTitleColor(Color.white);

        // Create a titled border with a custom title, font, size, and color
        TitledBorder titledBorder3 = BorderFactory.createTitledBorder("Embedded Image");
        titledBorder3.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder3.setTitleColor(Color.white);

        // Create a titled border with a custom title, font, size, and color
        TitledBorder titledBorder4 = BorderFactory.createTitledBorder("Secret Message");
        titledBorder4.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder4.setTitleColor(Color.white);

        Object o = a.getSource();
        if(o == openFile){
            openFile();
            originalPane.setBorder(titledBorder1);
            embeddedPane.setBorder(titledBorder2);  }
       else if(o == open) {
            openImage();
            originalPane.setBorder(titledBorder1);
            embeddedPane.setBorder(titledBorder2);
        }
        else if(o == embed){
            embedMessage();
            originalPane.setBorder(titledBorder1);
            embeddedPane.setBorder(titledBorder2);}
        else if(o == save){
            saveImage();
        originalPane.setBorder(titledBorder1);
        embeddedPane.setBorder(titledBorder2);}
        else if( o == openDecode){
                openImageDecode();
        originalPane.setBorder(titledBorder3);
        embeddedPane.setBorder(titledBorder4);}
        else if(o == extract){
         decodeMessage();
            originalPane.setBorder(titledBorder3);
            embeddedPane.setBorder(titledBorder4);}
        else if(o == reset){
            resetInterface();
        originalPane.setBorder(null);
        embeddedPane.setBorder(null);}
    }

    private java.io.File showFileDialog(final boolean open) {
        JFileChooser fc = new JFileChooser("Open an image");
        javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File f) {
                String name = f.getName().toLowerCase();
                if(open)
                    return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                            name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".tiff") ||
                            name.endsWith(".bmp") || name.endsWith(".dib");
                return f.isDirectory() || name.endsWith(".png") ||    name.endsWith(".bmp");
            }
            public String getDescription() {
                if(open)
                    return "Image (*.jpg, *.jpeg, *.png, *.gif, *.tiff, *.bmp, *.dib)";
                return "Image (*.png, *.bmp)";
            }
        };
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(ff);

        java.io.File f = null;
        if(open && fc.showOpenDialog(this) == fc.APPROVE_OPTION)
            f = fc.getSelectedFile();
        else if(!open && fc.showSaveDialog(this) == fc.APPROVE_OPTION)
            f = fc.getSelectedFile();
        return f;
    }


    private java.io.File openFile(final boolean openFile) {
        JFileChooser fileChooser = new JFileChooser("Open a text file");
        javax.swing.filechooser.FileFilter textFileFilter = new javax.swing.filechooser.FileFilter() {
            public boolean accept(File file) {
                String name = file.getName().toLowerCase();
                return file.isDirectory() || name.endsWith(".txt");
            }

            public String getDescription() {
                return "Text Files (*.txt)";
            }
        };

        fileChooser.setFileFilter(textFileFilter);

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(textFileFilter);

        java.io.File f = null;
        if(openFile && fileChooser.showOpenDialog(this) == fileChooser.APPROVE_OPTION)
            f = fileChooser.getSelectedFile();
        else if(!openFile && fileChooser.showSaveDialog(this) == fileChooser.APPROVE_OPTION)
            f = fileChooser.getSelectedFile();
        return f;
    }

    private void openImage() {
        java.io.File f = showFileDialog(true);
        try {
            sourceImage = ImageIO.read(f);
            JLabel l = new JLabel(new ImageIcon(sourceImage));
            originalPane.getViewport().add(l);
            this.validate();
        } catch(Exception ex) { ex.printStackTrace(); }
    }
    private void openImageDecode() {
        java.io.File f = showFileDialog(true);
        try {
            imageToDecode = ImageIO.read(f);
            JLabel l = new JLabel(new ImageIcon(imageToDecode));
            originalPane.getViewport().add(l);
            this.validate();
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    private void openFile() {
        File file = openFile(true);

        if (file != null && file.isFile()) {
            try {
                String content = readFile(file);
                displayText(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}
    private String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
    }
    private void displayText(String content) {
        JFrame frame = new JFrame("Text File Viewer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        textArea = new JTextArea(content);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.getContentPane().add(scrollPane);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void embedMessage() {
        //get content of file that is printed in text area
        String mess = textArea.getText();
        embeddedImage = sourceImage.getSubimage(0,0, sourceImage.getWidth(),sourceImage.getHeight());

        String[] arrOfWords = mess.split(" ");

        int chunksNumber = arrOfWords.length;

        //hashmap for string words/chunks
        HashMap<Integer, String> chunks = new HashMap<Integer, String>();

        for(int i=0; i<chunksNumber; i++){
            chunks.put(i, arrOfWords[i]);
        }
     /*   for (Integer i : chunks.keySet()) {
            System.out.println("key: " + i + " value: " + chunks.get(i));
        }*/


        for(int i=0; i<chunksNumber; i++){
            byte[] byteArr = chunks.get(i).getBytes();
            //byte chunks is a hashmap as well but in bytes <integer, byte>
            byteChunks.put(i, byteArr);
        }

        //length of each word/chunk
        chunksSizes = new int[byteChunks.size()];

        // Ensure the images have the same dimensions
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int imageSize = width * height;
        int imageSizeInBytes = width * height * 3;

        //check if image size has enough space for embedding chunks
        if(imageSize<mess.length()){
            System.out.println("Image size is smaller than message length, can't embed or hide the message!");
            return;
        }
        try {
        embeddedImage =  hideMessage(sourceImage, byteChunks);
        JLabel l = new JLabel(new ImageIcon(embeddedImage));
       embeddedPane.getViewport().add(l);
            this.validate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void saveImage() {
        if(embeddedImage == null) {
            JOptionPane.showMessageDialog(this, "No message has been embedded!",
                    "Nothing to save", JOptionPane.ERROR_MESSAGE);
            return;
        }
        java.io.File f = showFileDialog(false);
        String name = f.getName();
        String ext = name.substring(name.lastIndexOf(".")+1).toLowerCase();
        if(!ext.equals("png") && !ext.equals("bmp") &&   !ext.equals("dib")) {
            ext = "png";
            f = new java.io.File(f.getAbsolutePath()+".png");
        }
        try {
            if(f.exists()) f.delete();
            ImageIO.write(embeddedImage, ext.toUpperCase(), f);
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    private void resetInterface() {
        //message.setText("");
        originalPane.getViewport().removeAll();
        embeddedPane.getViewport().removeAll();
        sourceImage = null;
        embeddedImage = null;
        sp.setDividerLocation(0.5);
        this.validate();
    }

    private void decodeMessage() {
        if (imageToDecode == null) {
            JOptionPane.showMessageDialog(null, "you should open a picture");
            return;
        }
        String k = Decryption.revealMsg(textLength1);
        String k2 = Decryption.revealMsg2(textLength2);
        String k3 = "";
        StringBuffer str1 = new StringBuffer(k + " ");
        StringBuffer str2 = new StringBuffer(k2 + " ");
        StringBuffer str3 = new StringBuffer("");
        int prevSize = 0;
        int s1 = 0;
        int s2 = 0;
        for (Integer n : byteChunks.keySet()) {
            if (n % 2 == 0) {
                int spaceIndex = chunksSizes[n];
                str3.append(str1.subSequence(s1, spaceIndex + s1));
                s1 += spaceIndex;

            } else {
                int spaceIndex = chunksSizes[n];
                str3.append(str2.subSequence(s2, spaceIndex + s2));
                s2 += spaceIndex;
            }
        }

        for (Integer n : byteChunks.keySet()) {
            int spaceIndex = chunksSizes[n];

            str3.insert(prevSize + spaceIndex, " ");
            prevSize += spaceIndex + 1;

        }
        System.out.println("Text is: " + str3);
        System.out.println("Text length 1: " + textLength1);
        System.out.println("Text length 2: " + textLength2);
        Decryption.createFileWithMessage(String.valueOf(str3));
        JLabel l = new JLabel("Hidden Text is: " + str3);
       embeddedPane.getViewport().add(l);
    }
}

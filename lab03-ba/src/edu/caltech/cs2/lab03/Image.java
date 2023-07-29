package edu.caltech.cs2.lab03;

import edu.caltech.cs2.libraries.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Image {
    private Pixel[][] pixels;


    public Image(File imageFile) throws IOException {
        BufferedImage img = ImageIO.read(imageFile);
        this.pixels = new Pixel[img.getWidth()][img.getHeight()];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                this.pixels[i][j] = Pixel.fromInt(img.getRGB(i, j));
            }
        }
    }


    private Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }


    public Image transpose() {
        Pixel[][] transposed = new Pixel[this.pixels[0].length][this.pixels.length];
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                transposed[col][row] = pixels[row][col];
            }
        }
        return new Image(transposed);
    }


    public String decodeText() {
        String message = "";
        String byt = "";
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                byt = pixels[row][col].getLowestBitOfR() + byt;
                if (byt.length() == 8) {
                    int temp = Integer.parseInt(byt,2);
                    if (temp != 0) {
                        message += (char) temp;
                    }
                    byt = "";
                }
            }
        }
        return message;
    }


    public Image hideText(String text) {
        Pixel[][] newImage = new Pixel[pixels.length][pixels[0].length];

        ArrayList<Integer> bits = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            String binaryCharacter = Integer.toBinaryString((int) text.charAt(i));
            while (binaryCharacter.length() < 8) {
                binaryCharacter = "0" + binaryCharacter;
            }
            for (int j = binaryCharacter.length() - 1; j >= 0; j--) {
                bits.add(binaryCharacter.charAt(j) == '0' ? 0 : 1);
            }
        }

        boolean done = false;
        int i = 0;
        for (int row = 0; row < newImage.length; row++) {
            for (int col = 0; col < newImage[0].length; col++) {
                if (!done) {
                    if (i < bits.size()) {
                        newImage[row][col] = pixels[row][col].fixLowestBitOfR(bits.get(i));
                        i++;
                    } else {
                        newImage[row][col] = pixels[row][col].fixLowestBitOfR(0);
                        done = true;
                    }
                } else {
                    newImage[row][col] = pixels[row][col].fixLowestBitOfR(0);
                }
            }
        }
        return new Image(newImage);
    }


    public BufferedImage toBufferedImage() {
        BufferedImage b = new BufferedImage(this.pixels.length, this.pixels[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < this.pixels.length; i++) {
            for (int j = 0; j < this.pixels[0].length; j++) {
                b.setRGB(i, j, this.pixels[i][j].toInt());
            }
        }
        return b;
    }


    public void save(String filename) {
        File out = new File(filename);
        try {
            ImageIO.write(this.toBufferedImage(), filename.substring(filename.lastIndexOf(".") + 1, filename.length()), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

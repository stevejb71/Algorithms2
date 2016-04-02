import edu.princeton.cs.algs4.Picture;

import java.awt.*;

class PictureFactory {
    private int[] reds;
    private int[] greens;
    private int[] blues;
    int width;
    int height;

    static PictureFactory fromPicture(Picture picture) {
        final PictureFactory pf = new PictureFactory(picture.width(), picture.height());
        int i = 0;
        for (int y = 0; y < pf.height; ++y) {
            for (int x = 0; x < pf.width; ++x) {
                final Color color = picture.get(x, y);
                pf.reds[i] = color.getRed();
                pf.greens[i] = color.getGreen();
                pf.blues[i] = color.getBlue();
                i++;
            }
        }
        return pf;
    }

    private PictureFactory(int width, int height) {
        this.reds = new int[width * height];
        this.greens = new int[width * height];
        this.blues = new int[width * height];
        this.width = width;
        this.height = height;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private PictureFactory transpose() {
        final PictureFactory t = new PictureFactory(height, width);
        int ptr = 0;
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                t.reds[x * height + y] = reds[ptr];
                t.greens[x * height + y] = greens[ptr];
                t.blues[x * height + y] = blues[ptr];
                ptr++;
            }
        }
        return t;
    }

    PictureFactory removeVerticalSeam(int[] xs) {
        final int[] toRemove = new int[xs.length + 1];
        for(int y = 0; y < height; ++y) {
            toRemove[y] = xs[y] + y * width;
        }
        toRemove[xs.length] = height * width;
        final PictureFactory copy = new PictureFactory(width - 1, height);
        int srcPtr = 0;
        int destPtr = 0;
        for (int r : toRemove) {
            final int lengthToCopy = r - srcPtr;
            System.arraycopy(reds, srcPtr, copy.reds, destPtr, lengthToCopy);
            System.arraycopy(greens, srcPtr, copy.greens, destPtr, lengthToCopy);
            System.arraycopy(blues, srcPtr, copy.blues, destPtr, lengthToCopy);
            srcPtr = r + 1;
            destPtr += lengthToCopy;
        }
        return copy;
    }

    Color get(int x, int y) {
        final int index = y * width + x;
        return new Color(reds[index], greens[index], blues[index]);
    }

    PictureFactory removeHorizontalSeam(int[] ys) {
        return transpose().removeVerticalSeam(ys).transpose();
    }

    Picture create() {
        final Picture p = new Picture(width, height);
        int i = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                p.set(x, y, new Color(reds[i], greens[i], blues[i]));
                i++;
            }
        }
        return p;
    }
}

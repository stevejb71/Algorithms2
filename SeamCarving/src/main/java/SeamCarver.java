import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.Arrays;

public class SeamCarver {
    private PictureFactory pictureFactory;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new NullPointerException();
        this.pictureFactory = PictureFactory.fromPicture(picture);
    }

    public Picture picture() {
        return pictureFactory.create();
    }

    public int width() {
        return pictureFactory.width;
    }

    public int height() {
        return pictureFactory.height;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }
        if (atBorder(x, width()) || atBorder(y, height())) {
            return 1000.0;
        }
        final double deltaXSquared = gradientSquared(pictureFactory.get(x - 1, y), pictureFactory.get(x + 1, y));
        final double deltaYSquared = gradientSquared(pictureFactory.get(x, y - 1), pictureFactory.get(x, y + 1));
        return Math.sqrt(deltaXSquared + deltaYSquared);
    }

    public int[] findHorizontalSeam() {
        final float[][] distTo = new float[width()][height()];
        Arrays.fill(distTo[0], 1000.0f);
        final int[][] edgeTo = new int[width()][height()];
        final float[] energies = new float[height()];
        for (int x = 1; x < width(); ++x) {
            for (int y = 0; y < height(); ++y) {
                energies[y] = (float) energy(x, y);
            }

            for (int y = 0; y < height(); ++y) {
                final float[] distToLeft = distTo[x - 1];
                final float distRightUp = y == 0 ? Float.MAX_VALUE : distToLeft[y - 1];
                final float distRight = distToLeft[y];
                final float distRightDown = y == height() - 1 ? Float.MAX_VALUE : distToLeft[y + 1];
                final int minIndex = minIndexOf3(distRightUp, distRight, distRightDown);
                distTo[x][y] = distToLeft[y + minIndex] + energies[y];
                edgeTo[x][y] = y + minIndex;
            }
        }

        final int[] seam = new int[width()];
        seam[width() - 1] = indexOfMin(distTo[width() - 1]);
        for (int x = width() - 2; x >= 1; x--) {
            seam[x] = edgeTo[x + 1][seam[x + 1]];
        }
        if (width() > 1) {
            seam[0] = seam[1];
        }
        return seam;
    }

    public int[] findVerticalSeam() {
        final float[][] distTo = new float[height()][width()];
        Arrays.fill(distTo[0], 1000.0f);
        final int[][] edgeTo = new int[height()][width()];
        final float[] energies = new float[width()];
        for (int y = 1; y < height(); ++y) {
            for (int x = 0; x < width(); ++x) {
                energies[x] = (float) energy(x, y);
            }

            for (int x = 0; x < width(); ++x) {
                final float[] distToAbove = distTo[y - 1];
                final float distUpLeft = x == 0 ? Float.MAX_VALUE : distToAbove[x - 1];
                final float distUp = distToAbove[x];
                final float distUpRight = x == width() - 1 ? Float.MAX_VALUE : distToAbove[x + 1];
                final int minIndex = minIndexOf3(distUpLeft, distUp, distUpRight);
                distTo[y][x] = distToAbove[x + minIndex] + energies[x];
                edgeTo[y][x] = x + minIndex;
            }
        }

        final int[] seam = new int[height()];
        seam[height() - 1] = indexOfMin(distTo[height() - 1]);
        for (int y = height() - 2; y >= 1; y--) {
            seam[y] = edgeTo[y + 1][seam[y + 1]];
        }
        if (height() > 1) {
            seam[0] = seam[1];
        }
        return seam;
    }

    private int minIndexOf3(double x1, double x2, double x3) {
        if (x1 < x2) {
            return x1 < x3 ? -1 : 1;
        } else {
            return x2 < x3 ? 0 : 1;
        }
    }

    private int indexOfMin(float[] values) {
        int minIndex = 0;
        double minValue = Float.MAX_VALUE;
        for (int i = 0; i < values.length; ++i) {
            final double value = values[i];
            if (value < minValue) {
                minIndex = i;
                minValue = value;
            }
        }
        return minIndex;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1) {
            throw new IllegalArgumentException("cannot remove seam, picture too small");
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException("bad seam length");
        }
        checkArrayValues(seam, height());

        pictureFactory = pictureFactory.removeHorizontalSeam(seam);
    }

    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1) {
            throw new IllegalArgumentException("cannot remove seam, picture too small");
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException("bad seam length");
        }
        checkArrayValues(seam, width());

        pictureFactory = pictureFactory.removeVerticalSeam(seam);
    }

    private void checkArrayValues(int[] xs, int bound) {
        if (xs[0] < 0 || xs[0] >= bound) {
            throw new IllegalArgumentException("out of bounds at 0");
        }
        for (int i = 1; i < xs.length; ++i) {
            final int x = xs[i];
            if (x < 0 || x >= bound) {
                throw new IllegalArgumentException("out of bounds at " + i);
            }
            if (Math.abs(xs[i] - xs[i - 1]) > 1) {
                throw new IllegalArgumentException("too jagged seam at " + i);
            }
        }
    }

    private double gradientSquared(Color first, Color second) {
        return sq(first.getRed() - second.getRed()) + sq(first.getGreen() - second.getGreen()) + sq(first.getBlue() - second.getBlue());
    }

    private double sq(double x) {
        return x * x;
    }

    private boolean atBorder(int point, int max) {
        return point == 0 || point == max - 1;
    }
}
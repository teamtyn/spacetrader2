/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.star_system;

import java.util.Arrays;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author Administrator
 */
public class NoiseGenerator {
    public enum NoiseMode {NONE, SQUARE, CUBE, ABS};
    
    private static final double F3 = 1.0/3.0;
    private static final double G3 = 1.0/6.0;
    private static final Grad[] GTAB = {new Grad(1, 1, 0), new Grad(-1, 1, 0), new Grad(1, -1, 0), new Grad(-1, -1, 0),
                                        new Grad(1, 0, 1), new Grad(-1, 0, 1), new Grad(1, 0, -1), new Grad(-1, 0, -1),
                                        new Grad(0, 1, 1), new Grad(0, -1, 1), new Grad(0, 1, -1), new Grad(0, -1, -1)};
    private static final short[] SUPPLY = new short[256];
    static {
        for (int i = 0; i < 256; i++) {
            SUPPLY[i] = (short) i;
        }
    }
    private short[] p = new short[SUPPLY.length];
    private final short[] perm;
    private final short[] permMod12 = new short[512];
    private final double baseFreq;
    private final double baseAmp;
    private final double lacunarity;
    private final double gain;
    private final int octaveCap;
    private final NoiseMode mode;
    private final ColorGradient colors;
    
    private float[][] noiseBuffer;
    private int width;
    private int height;
    
    public NoiseGenerator(long seed, double bF, double bA, double l, double g, int oC, NoiseMode m, ColorGradient c) {
        Random r = new Random(seed);
        perm = Arrays.copyOf(SUPPLY, 512);
        for (int i = 0; i < 256; i++) {
            int j = r.nextInt(256);
            short temp = perm[i];
            perm[i] = perm[j];
            perm[j] = temp;
        }
        for (int i = 0; i < 512; i++) {
            perm[i] = perm[i & 255];
            permMod12[i] = (short) (perm[i] % 12);
        }
        baseFreq = bF;
        baseAmp = bA;
        lacunarity = l;
        gain = g;
        octaveCap = oC;
        mode = m;
        colors = c;
    }
    
    private int fastfloor(double x) {
        int floorX = (int)x;
        return x < floorX ? floorX - 1 : floorX;
    }
    
    private static double dot(Grad g, double x, double y, double z) {
        return g.x*x + g.y*y + g.z*z; 
    }
    
    private double noise(double xin, double yin, double zin) {
        double n0, n1, n2, n3;
        double s = (xin + yin + zin) * F3;
        int i = fastfloor(xin + s);
        int j = fastfloor(yin + s);
        int k = fastfloor(zin + s);
        double t = (i + j + k) * G3;
        double X0 = i - t;
        double Y0 = j - t;
        double Z0 = k - t;
        double x0 = xin - X0;
        double y0 = yin - Y0;
        double z0 = zin - Z0;
        int i1, j1, k1;
        int i2, j2, k2;
        if (x0 >= y0) {
            if (y0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            } else if (x0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            } else {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            }
        } else {
            if (y0 < z0) {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 0;
                j2 = 1;
                k2 = 1;
            } else if (x0 < z0) {
                i1 = 0;
                j1 = 1;
                k1 = 0;
                i2 = 0;
                j2 = 1;
                k2 = 1;
            } else {
                i1 = 0;
                j1 = 1;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            }
        }
        double x1 = x0 - i1 + G3;
        double y1 = y0 - j1 + G3;
        double z1 = z0 - k1 + G3;
        double x2 = x0 - i2 + 2.0 * G3;
        double y2 = y0 - j2 + 2.0 * G3;
        double z2 = z0 - k2 + 2.0 * G3;
        double x3 = x0 - 1.0 + 3.0 * G3;
        double y3 = y0 - 1.0 + 3.0 * G3;
        double z3 = z0 - 1.0 + 3.0 * G3;
        int ii = i & 255;
        int jj = j & 255;
        int kk = k & 255;
        int gi0 = permMod12[ii + perm[jj + perm[kk]]];
        int gi1 = permMod12[ii + i1 + perm[jj + j1 + perm[kk + k1]]];
        int gi2 = permMod12[ii + i2 + perm[jj + j2 + perm[kk + k2]]];
        int gi3 = permMod12[ii + 1 + perm[jj + 1 + perm[kk + 1]]];
        double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
        if(t0 < 0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * dot(GTAB[gi0], x0, y0, z0);
        }
        double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
        if (t1 < 0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * dot(GTAB[gi1], x1, y1, z1);
        }
        double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
        if (t2 < 0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * dot(GTAB[gi2], x2, y2, z2);
        }
        double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
        if (t3 < 0) {
            n3 = 0.0;
        } else {
            t3 *= t3;
            n3 = t3 * t3 * dot(GTAB[gi3], x3, y3, z3);
        }
        return 32.0 * (n0 + n1 + n2 + n3);
    }
    
    public void initNoiseBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        noiseBuffer = new float[height][width];
    }
    
    public void clearBuffer() {
        width = 0;
        height = 0;
        noiseBuffer = null;
    }
    
    public void addOctaves() {
        float max = 0;
        int octaves = Math.min((int) (Math.log(1 / (baseFreq * 2.0 * Math.PI / width)) / Math.log(lacunarity)), octaveCap);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                double normU = i / (double)width;
                double normV = 1 - (2.0 * j / height);

                double x = Math.cos(2.0 * Math.PI * normU) * Math.sqrt(1.0 - normV * normV);
                double y = Math.sin(2.0 * Math.PI * normU) * Math.sqrt(1.0 - normV * normV);
                double z = normV;
                
                x *= baseFreq;
                y *= baseFreq;
                z *= baseFreq;
                
                float noiseSum = 0;
                double amplitude = baseAmp;
                for (int k = 1; k < octaves; k++) {
                    noiseSum += noise(x, y, z) * amplitude;
                    x *= lacunarity;
                    y *= lacunarity;
                    z *= lacunarity;
                    amplitude *= gain;
                }
                noiseBuffer[j][i] += noiseSum;
                if (Math.abs(noiseBuffer[j][i]) > max) {
                    max = Math.abs(noiseBuffer[j][i]);
                }
            }
        }
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                noiseBuffer[j][i] /= max;
                switch (mode) {
                    case NONE: noiseBuffer[j][i] = 0.5f * (1 + noiseBuffer[j][i]);
                        break;
                    case SQUARE: noiseBuffer[j][i] = (float) Math.pow(noiseBuffer[j][i], 2);
                        break;
                    case CUBE: noiseBuffer[j][i] = 0.5f * (1 + (float) Math.pow(noiseBuffer[j][i], 3));
                        break;
                    case ABS: noiseBuffer[j][i] = (float) Math.abs(noiseBuffer[j][i]);
                }
            }
        }
    }
    
    public Image getDiffuse() {
        int scaleFactor;
        if (width * height >= 500000) {
            scaleFactor = 2;
        } else {
            scaleFactor = 1;
        }
        
        int widthScaled = (int) Math.ceil((double)width / scaleFactor);
        int heightScaled = (int) Math.ceil((double)height / scaleFactor); 
        WritableImage img = new WritableImage(widthScaled, heightScaled);
        PixelWriter writer = img.getPixelWriter();
            for (int j = 0; j < heightScaled; j++) {
                for (int i = 0; i < widthScaled; i++) {
                    writer.setColor(i, j, colors.getColor(noiseBuffer[j * scaleFactor][i * scaleFactor]));
                }
            }
        return img;
    }
    
    public Image getNormal(double intensity) {
        if (width * height < 500000) {
            return null;
        }
        WritableImage img = new WritableImage(width, height);
        PixelWriter writer = img.getPixelWriter();
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                double tl = noiseBuffer[(height + j - 1) % height][(width + i - 1) % width];
                double t = noiseBuffer[(height + j - 1) % height][i];
                double tr = noiseBuffer[(height + j - 1) % height][(i + 1) % width];
                double r = noiseBuffer[j][(i + 1) % width];
                double br = noiseBuffer[(j + 1) % height][(i + 1) % width];
                double b = noiseBuffer[(j + 1) % height][i];
                double bl = noiseBuffer[(j + 1) % height][(width + i - 1) % width];
                double l = noiseBuffer[j][(width + i - 1) % width];
                double dx = (tl + 2.0 * l + bl) - (tr + 2.0 * r + br);
                double dy = (tl + 2.0 * t + tr) - (bl + 2.0 * b + br);
                double dz = 1.0 / intensity;
                double mag = Math.sqrt(dx * dx + dy * dy + dz * dz);
                dx = 0.5 * (1 + dx / mag);
                dy = 0.5 * (1 + dy / mag);
                dz = 0.5 * (1 + dz / mag);
                writer.setColor(i, j, Color.color(dx, dy, dz));
            }
        }
        return img;
    }

    private static class Grad {
        double x, y, z;

        Grad(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}

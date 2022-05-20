package Imagen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.DoubleStream;

import javax.imageio.ImageIO;

public class Matriz implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String IMG_PATH_03_B = "NUEVO.jpg";	
	int[][] matriz;
	
	Matriz() {}
	Matriz(int fs, int cs) {
		matriz = new int[fs][cs];
	}
	Matriz(int[][] matriz) {
		this.matriz = matriz;
	}
	
	int[][] getMatriz() {
		return matriz;
	}
	void setMatriz(int[][] matriz) {
		this.matriz = matriz;
	}
	void setMatriz(int valor) {
		for(int f = 0; f < getFilas(); f++) {
			for(int c = 0; c < getCols(); c++) {
				getMatriz()[f][c] = valor;
			}
		}
	}	
	
	int getFilas() {
		return matriz==null ? 0 : matriz.length;
	}
	int getCols() {
		return matriz==null ? 0 : matriz[0].length;
	}
	
	int getValor(int f, int c) {
		return matriz[f][c];
	}
	void setValor(int valor, int f, int c) {
		matriz[f][c] = valor;
	}		
	

	
	Matriz getSubMatriz(int initF, int finF, int initC, int finC) {
		Matriz subM = new Matriz(finF-initF+1, finC-initC+1);
		for(int f = initF; f <= finF; f++) {
			for(int c = initC; c <= finC; c++) {
				subM.setValor(this.getValor(f, c), f-initF, c-initC);
			}
		}
		return subM;
	}
	
	Matriz[] divideEnBloques(int numMat, int bloque) {
		Matriz[] chicas = new Matriz[numMat];
		int counter = 0;
		for(int f = 0; f < numMat; f+=bloque) {
			for(int c = 0; c < numMat; c+=bloque) {
				chicas[counter++] = this.getSubMatriz(f, f+bloque-1, c, c+bloque-1);
			}
		}
		return chicas;
	}
	
	void setSubMatriz(Matriz data, int initF, int finF, int initC, int finC) {
		for(int f = initF; f <= finF; f++) {
			for(int c = initC; c <= finC; c++) {
				this.setValor(data.getValor(f-initF, c-initC), f, c);
			}
		}
	}	
	
	static Matriz reuneBloques(Matriz[] chicas) {
		int numMat = chicas.length;
		int bloque = chicas[0].getCols();
		int matLado = (int) Math.sqrt(numMat);
		int cellLado = matLado * bloque;
		Matriz nueva = new Matriz(cellLado, cellLado);
		
		int counter = 0;
		for(int f = 0; f < numMat; f+=bloque) {
			for(int c = 0; c < numMat; c+=bloque) {
				nueva.setSubMatriz(chicas[counter++], f, f+bloque-1, c, c+bloque-1);
			}
		}
		
		return nueva;
	}
	
	int[][] limitesDivisionBloques(int bloques) {
		if(bloques % 2 != 0) return null;
		int[][] limites = new int[bloques][4];
		int bCols = 2;
		int bFilas = bloques / 2;
		int filas = getFilas() / bFilas;
		int cols = getCols() / bCols;		
		int colAct = 0;
		for(int c = 0; c < bCols; c++) {	
			int ctrlCol = c*bFilas;
			int filaAct = 0;
			for(int f = 0; f < bFilas; f++) {
				limites[ctrlCol+f][2] = colAct;
				limites[ctrlCol+f][0] = filaAct;			
				filaAct = (f==(bFilas-1)) ? getFilas()-1 : filaAct + filas - 1;
				limites[ctrlCol+f][1] = filaAct;
				filaAct += 1;
			}
			colAct = (c==(bCols-1)) ? getCols() - 1 : colAct + cols - 1;			
			for(int f = 0; f < bFilas; f++) {
				limites[ctrlCol+f][3] = colAct;		
			}
			colAct += 1;
		}		
			
		return limites;
	}
	
	static void haciaImagen(Matriz[] rgb, String imgFile) {	
		BufferedImage img = new BufferedImage(rgb[0].getFilas(), rgb[0].getCols(), BufferedImage.TYPE_INT_RGB);
		
        for(int row = 0; row < img.getHeight(); row++) {
            for(int col = 0; col < img.getWidth(); col++) {
                Color c = new Color(
                		(int)rgb[0].getValor(row, col), 
                		(int)rgb[1].getValor(row, col), 
                		(int)rgb[2].getValor(row, col));
                img.setRGB(col, row, c.getRGB());
            }
        }  		
		   
        try {
        	String ext = imgFile.substring(imgFile.length()-3);
        	 File outputfile = new File("C:/Users/danyc/Desktop/Nuevo.jpg");
			ImageIO.write(img, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	static Matriz[] desdeImagen(BufferedImage img) {
		Matriz[] rgb = new Matriz[3];

        int[][] r = new int[img.getHeight()][img.getWidth()];
        int[][] g = new int[img.getHeight()][img.getWidth()];
        int[][] b = new int[img.getHeight()][img.getWidth()];
        
        for(int row = 0; row < img.getHeight(); row++) {
            for(int col = 0; col < img.getWidth(); col++) {
                Color c = new Color(img.getRGB(col, row));
                r[row][col] = c.getRed();
                g[row][col] = c.getGreen();
                b[row][col] = c.getBlue();
            }
        }
        
        rgb[0] = new Matriz(r);
        rgb[1] = new Matriz(g);
        rgb[2] = new Matriz(b);
        
        return rgb;   			
	}

}

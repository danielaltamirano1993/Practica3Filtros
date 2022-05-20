
package Imagen;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


import mpi.MPI;

public class Suavizado {
	

	public static void main(String[] args) throws IOException {
		
		MPI.Init(args);
		
		FiltroMedia m = new FiltroMedia();	
		BufferedImage ima, ima3;
		ima = ImageIO.read(new File("C:/Users/danyc/Desktop/imagen1.jpg"));
		
		int master = 0;
		int numMat = 4;
		int bloque = ima.getHeight()/2;
		int[][] promedior = new int [bloque][bloque];
		int[][] promediog = new int [bloque][bloque];
		int[][] promediob = new int [bloque][bloque];

		Matriz[] chicasr = new Matriz[numMat];
		Matriz[] chicasg = new Matriz[numMat];
		Matriz[] chicasb = new Matriz[numMat];
		Matriz[] lasmatricesr = new Matriz[numMat];	
		Matriz[] lasmatricesg = new Matriz[numMat];	
		Matriz[] lasmatricesb = new Matriz[numMat];	
		Matriz[] mLocalr = new Matriz[1];	
		Matriz[] mLocalg = new Matriz[1];	
		Matriz[] mLocalb = new Matriz[1];	
		Matriz[] rgb = new Matriz[3];

		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		System.out.println("Hola # " + rank + " de " + size);
		
		rgb = m.Inicializa(ima);
			
			if(rank == master) {				
				
				chicasr = rgb[0].divideEnBloques(numMat, bloque);	
				chicasg = rgb[1].divideEnBloques(numMat, bloque);	
				chicasb = rgb[2].divideEnBloques(numMat, bloque);	

			}
					
			MPI.COMM_WORLD.Scatter(chicasr, 0, 1, MPI.OBJECT, mLocalr, 0, 1, MPI.OBJECT, master);						
			//promedior = m.Promedio(mLocalr, bloque);
			
			/*for(int f=0; f<bloque; f++) {
				for(int c=0; c<bloque; c++) {
					System.out.println(promedior[f][c]);
				}
			}*/
			//mLocalr[0].matriz = promedior;							
			MPI.COMM_WORLD.Gather(mLocalr, 0, 1, MPI.OBJECT, lasmatricesr, 0, 1, MPI.OBJECT, master);	
			
			if(rank==master) {
				for(Matriz q : lasmatricesr) System.out.println(q);
				rgb[0] = Matriz.reuneBloques(lasmatricesr);
			}
			
			MPI.COMM_WORLD.Scatter(chicasg, 0, 1, MPI.OBJECT, mLocalg, 0, 1, MPI.OBJECT, master);						
			//promediog = m.Promedio(mLocalg, bloque);	
			
			mLocalg[0].matriz = promediog;							
			MPI.COMM_WORLD.Gather(mLocalg, 0, 1, MPI.OBJECT, lasmatricesg, 0, 1, MPI.OBJECT, master);
			
			if(rank==master) {
				for(Matriz q : lasmatricesg) System.out.println(q);
				rgb[1] = Matriz.reuneBloques(lasmatricesg);
			}
			
			MPI.COMM_WORLD.Scatter(chicasb, 0, 1, MPI.OBJECT, mLocalb, 0, 1, MPI.OBJECT, master);						
			promediob = m.Promedio(mLocalb, bloque);				
			mLocalb[0].matriz = promediob;							
			MPI.COMM_WORLD.Gather(mLocalb, 0, 1, MPI.OBJECT, lasmatricesb, 0, 1, MPI.OBJECT, master);
					
			if(rank == master ) {
				for(Matriz q : lasmatricesb) System.out.println(q);
				//rgb[2] = Matriz.reuneBloques(lasmatricesb);
				
				/*for(int f=0; f<500; f++) {
					for(int c=0; c<500; c++) {
						System.out.print(f+"; "+c+" - ");
						System.out.println(rgb[0].getValor(f, c));
					}
				}*/

			}
			
			
			
			ima3 = m.Filtro_Media(ima, rgb);
		       
			File outputfile = new File("C:/Users/danyc/Desktop/Nuevo.jpg");
			
			ImageIO.write(ima3, "jpg", outputfile);
		
		//ima3.setRGB(i, j, new Color(promedio,promedio,promedio).getRGB());
		
		//System.out.println(rgb[0].getValor(1, 1));
		

			
		MPI.Finalize();

	}
	
}

package Imagen;

import java.awt.Color;

import java.awt.image.BufferedImage;


public class FiltroMedia {
	
	//imagenes
    BufferedImage Imagen;
    
    //indices,colores,ancho,alto de la imagen
    int i,j,w,h,r,g,b;
    int[][] matrizr,matrizg,matrizb;
  
    Color colorAuxiliar;
    
	public Matriz[] Inicializa(BufferedImage ima) {	
		 
		Imagen=new BufferedImage(ima.getWidth(),ima.getHeight(),BufferedImage.TYPE_INT_RGB) ; 
		Matriz[] rgb = new Matriz[3];
	     
		w=ima.getWidth();     
		h=ima.getHeight();  
		
		matrizr = new int[w][h];     
		matrizg = new int[w][h];     
		matrizb = new int[w][h];
	        	          
		for( i=0;i<ima.getWidth();i++){  	 
			for( j=0;j<ima.getHeight();j++){
				   	       	   
				colorAuxiliar=new Color(ima.getRGB(i, j));            	               
				r =colorAuxiliar.getRed();	               
				g=colorAuxiliar.getGreen();	               
				b =colorAuxiliar.getBlue();	               
				matrizr[i][j]=r;	               
				matrizg[i][j]=g;	               
				matrizb[i][j]=b;	               
				Imagen.setRGB(i, j, new Color(r,g,b).getRGB());				
	               
	         }
	     }
		
		rgb[0]= new Matriz(matrizr);
		rgb[1]= new Matriz(matrizg);
		rgb[2]= new Matriz(matrizb);
	        
	     return rgb;
	}
	
	public BufferedImage Filtro_Media(BufferedImage img, Matriz[] rgb) {
		
		int sumar,sumag,sumab;
		int[][] mr = rgb[0].matriz;
		int[][] mg = rgb[1].matriz;
		int[][] mb = rgb[2].matriz;		
		   
		for ( i = 1; i < img.getWidth() - 1; i++) {	           
			for ( j = 1; j < img.getHeight() - 1; j++) {			
	        
				sumar=0; sumag=0; sumab=0;
				

				//System.out.println(mr[i][j]);
		
	               
	            /*for(int x=-1; x<2; x++){	                   
	            	for(int y=-1; y<2; y++){
	                       
	            		sumar=sumar+mr[i+x][j+y];                     	                       
	            		sumag=sumag+mg[i+x][j+y];                       
	            		sumab=sumab+mb[i+x][j+y];
	            		                   
	            	}               
	            }*/
	                     
	            img.setRGB(i, j, new Color(mr[i][j],mg[i][j],mb[i][j]).getRGB());           
			}      
		}
	            
		return img;   
	}
	
	public int [][]Promedio(Matriz[] mlocal, int bloque) {
		
		int suma=0;
		int promedio=0;
		
		int[][] matriz = mlocal[0].matriz;
			
		for ( i = 0; i < bloque ; i++) {	           
			for ( j = 0; j < bloque ; j++) {	
				
				//System.out.println(i+"; "+j);			
				suma=0;
				promedio = 0;
	        
				suma=suma+matriz[i][j];
				
				promedio = promedio + (suma/9);
							
				//System.out.println(promedio);
	               
	            /*for(int x=-1; x<2; x++){	                   
	            	for(int y=-1; y<2; y++){
	                       
	            		suma=suma+mlocal[i+x][j+y];                     	                       
	            		                   
	            	}               
	            }*/
	                     
	            //img.setRGB(i, j, new Color(sumar/9,sumag/9,sumab/9).getRGB());
	            matriz[i][j]=promedio;
	            
			}      
		}
	            
		return matriz;   
	}
	
	
    
}
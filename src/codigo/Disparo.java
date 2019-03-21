
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * @author Daniel Bardera
 */
public class Disparo {
    // Inicializamos una variable situada en la siguientes coordenadas
    public Image imagen = null;  
    // Al principio la imagen no vale nada
    public int x = 0;
    public int y = 2000; // Al principio el disparo se pinta por debajo de la pantalla
    public boolean disparado = false;
    
    public Disparo(){ 
        try {
            // creamos un constructor que inicializa el objeto
            // los contructores no llevan no devuelven nada

            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo.png"));
            // Carga la imagen del disparo           
                    } catch (IOException ex) {
          
        }
    }
    public void mueve(){
        if(disparado){
             y-= 10;
        }
        
    }
    public void posicionaDisparo(Nave _nave){
        x = _nave.x + _nave.imagen.getWidth(null)/2 - imagen.getWidth(null)/2;
        y = _nave.y - _nave.imagen.getHeight(null)/2;
        
    }
}

/*
 * Realización de la nave del juego.
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/*
 * Autor: Daniel Bardera
 */

public class Nave {
    // Inicializamos una variable situada en la siguientes coordenadas
    public Image imagen = null;  
    // Al principio la imagen no vale nada
    public int x = 0;
    public int y = 0;
    
    public Nave(){ 
        try {
            // creamos un constructor que inicializa el objeto
            // los contructores no llevan no devuelven nada

            imagen = ImageIO.read(getClass().getResource("/imagenes/nave.png"));
            // Carga la imagen de la nave           
                    } catch (IOException ex) {
          
        }
    
        
    }
    
}

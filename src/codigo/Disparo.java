
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
    public int y = 0;

    
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
    
        y--;
    }
}

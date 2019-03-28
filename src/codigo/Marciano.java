
package codigo;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * Autor: Daniel Bardera
 */
public class Marciano {
    
    public Image imagen1, imagen2 = null;
    private int x = 0;
    private int y = 0;
    private int vX = 1;
    private int desplazY = 16;
    public boolean vivo = true;
    
    
    public Marciano(){
        
    }
  
    public void mueve(){
        setX(getX() + vX);
    }
    
    public void mueveEjeY() {
        setY(getY() + desplazY);
    }

    public void setvX(int vX) {
        this.vX = vX;
    }

    public int getvX() {
        return vX;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the desplazY
     */
    public int getDesplazY() {
        return desplazY;
    }

    /**
     * @param desplazY the desplazY to set
     */
    public void setDesplazY(int desplazY) {
        this.desplazY = desplazY;
    }
}

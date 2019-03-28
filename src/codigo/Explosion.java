/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/*
 * Autor: Daniel Bardera
 */


public class Explosion {
   
    private int x;
    private int y;
    private int tiempoDeVida = 50;
    
    Clip sonidoExplosion = null;
    Image boom = null;
    
    /**
     * Constructor de la clase
     * Inicializamos el sonido con cada llamada a Explosion
     */
    public Explosion() {
        try {
            //Inicializo el sonido
            sonidoExplosion = AudioSystem.getClip();
            //Abro el fichero de audio del sonido
            sonidoExplosion.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonidos/explosion.wav")));
        } catch (Exception e) {
        }
    }

    public int getTiempoDeVida() {
        return tiempoDeVida;
    }

    public void setTiempoDeVida(int tiempoDeVida) {
        this.tiempoDeVida = tiempoDeVida;
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
    
}



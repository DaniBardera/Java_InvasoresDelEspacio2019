/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/*
 * Autor: Daniel Bardera
 */

public class Explosion {
    
    Clip sonidoExplosion;
    Image boom = null;
     
//posición x-y de la explosion
    private int x = 0;
    private int y = 0;
    private int tiempoDeVida = 50;

    
    
    public int getTiempoDeVida() {
        return tiempoDeVida;
    }

    public void setTiempoDeVida(int tiempoDeVida) {
        this.tiempoDeVida = tiempoDeVida;
    }

    
    public Explosion (){
        try {
            sonidoExplosion = AudioSystem.getClip();
            sonidoExplosion.open(AudioSystem.getAudioInputStream(
                     getClass().getResource("/sonidos/explosion.wav")));
            
        } catch (Exception ex) {
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
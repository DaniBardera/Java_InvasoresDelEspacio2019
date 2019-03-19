
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

/*
 * Autor: Daniel Bardera
 */
public class VentanaJuego extends javax.swing.JFrame {

    static int ANCHOPANTALLA = 600;
    // Static si quieres que una variable no cambie durante el juego
    // se pone en mayúsculas el nombre de la variable
    static int ALTOPANTALLA = 450;
    
    // Número de marcianos que van a aparecer  
    int filas = 5;
    int columnas = 10;
    
    BufferedImage buffer = null;
    // El buffer almacena las operaciones que vamos realizando para no 
    //modificar el jPanel
    
    Nave miNave = new Nave();
    // creamos un objeto de tipo nave
    Disparo miDisparo = new Disparo();
    // creamos un objeto de tipo disparo
    // Marciano miMarciano = new Marciano();
    // creamos un objeto de tipo marciano
    Marciano [][] listaMarcianos = new Marciano [filas][columnas];
            //Array 2 dimensiones
    
    boolean direccionMarcianos = false;   
    
    // el contador sirve para decidir que imagen del marciano toca poner
    int contador = 0;
    
    Timer temporizador = new Timer (10, new ActionListener() {
         // 10 es el tiempo que va a tardar en llamar a la función
        @Override
        public void actionPerformed(ActionEvent e) {
           bucleDelJuego();
           // creamos un método que realizará el cambio de posición
        }
    });
   
    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        // con el bufferedImage le marcamos el tipo del que tiene que ser 
        buffer.createGraphics();
        // Para poder escribir en el Buffer
        temporizador.start();
        // Para que comience a realizar la acción
        
        //INICIALIZO POSICIÓN INICIAL DE LA NAVE
        miNave.x = ANCHOPANTALLA /2 - miNave.imagen.getWidth(this) /2;
        // para que salga centrado
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this) - 40;
         //menos 40 para que salga un poco por encima 
              
        // INICIALIZO EL ARRAY DE MARCIANOS
        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){
                listaMarcianos[i][j] = new Marciano();
                listaMarcianos[i][j].x = j*(15 + listaMarcianos[i][j].imagen1.getWidth(null));
                listaMarcianos[i][j].y = i*(10 + listaMarcianos[i][j].imagen1.getHeight(null));               
            }
        }   
        miDisparo.posicionaDisparo(miNave);
    }

    private void bucleDelJuego(){
        contador++;
    // Gobierna el redibujado de los objetos en el jPanel1
    // Primero borro todo lo que hay en el buffer
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
    // Lo que hace g2 es que busque en la zona de memoria donde está el buffer
        g2.setColor(Color.BLACK);
    // Le decimos que lo pinte en color negro
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
    // Borramos lo que hubiera en pantalla
 
    
    ///////////////////////////////////////////////////////////////////////////
    
    // Redibujamos aquí cada elemento 
    
    // DIBUJO NAVE
    g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);
    // DIBUJO DISPARO
    g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
    // DIBUJO MARCIANO
    
    pintaMarcianos(g2);
    chequeaColision();
    
     // Se mueve la nave
    miNave.mueve();
     // Se mueve el disparo
    miDisparo.mueve();
  
    
    
    ///////////////////////////////////////////////////////////////////////////
    //********** Fase Final: se dibuja el buffer sobre el jPanel1 ***********//
    
    g2 = (Graphics2D) jPanel1.getGraphics();
    g2.drawImage(buffer, 0, 0, null);
    // El draw nos indica lo que debe dibujar en primer lugar, en segundo la 
    // posición y null porque si no no funciona
    }
    
    private void chequeaColision(){
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        
        // Rectángulo disparo
        rectanguloDisparo.setFrame( miDisparo.x, 
                                    miDisparo.y, 
                                    miDisparo.imagen.getWidth(null),
                                    miDisparo.imagen.getHeight(null));
    
        // Rectángulo marciano
        for (int i=0; i<filas; i++){
              for (int j=0; j<columnas; j++){
                   rectanguloMarciano.setFrame(listaMarcianos[i][j].x,
                                                listaMarcianos[i][j].y,
                                                listaMarcianos[i][j].imagen1.getWidth(null),
                                                listaMarcianos[i][j].imagen1.getHeight(null)
                                                );
                   
            // chequea cuando se cruzan el disparo y marciano
            if (rectanguloDisparo.intersects(rectanguloMarciano)){
                // Mandamos al marciano fuera de la pantalla
                listaMarcianos[i][j].y = 2000;
                // lo colocamos en la nave
                miDisparo.posicionaDisparo(miNave);
            }
          }
       }
}
    
    private void cambiaDireccionMarcianos(){
         for (int i=0; i<filas; i++){
              for (int j=0; j<columnas; j++){
                   listaMarcianos[i][j].setvX(listaMarcianos[0][0].getvX()* -1);
              }
         }
}
    private void pintaMarcianos(Graphics2D _g2){
       
        int anchoMarciano = listaMarcianos[0][0].imagen1.getWidth(null);
        
        for (int i=0; i<filas; i++){
            for (int j=0; j<columnas; j++){
                listaMarcianos[i][j].mueve();
                
                // Chequeo si el marciano choca contra la pared para cambiar la dirección
                // de todos los marcianos
                if (listaMarcianos[i][j].x + anchoMarciano == ANCHOPANTALLA || listaMarcianos[i][j].x == 0){
                    cambiaDireccionMarcianos();
                }
                
                
                // ponemos la imagen 1
                if (contador < 50){
                    _g2.drawImage(listaMarcianos[i][j].imagen1,  
                              listaMarcianos[i][j].x,  
                              listaMarcianos[i][j].y,
                              null);
        }
                // ponemos la imagen 2
                else if (contador < 100){
                    _g2.drawImage(listaMarcianos[i][j].imagen2,  
                              listaMarcianos[i][j].x,  
                              listaMarcianos[i][j].y,
                              null);
                
            }
                else contador = 0; // reseteamos contador y lo ponemos a 0 de nuevo
        }
    }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(600, 450));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 587, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()){      
            case KeyEvent.VK_LEFT: miNave.setPulsadoIzquierda(true); break;
            // break para que no ejecute la linea de abajo
            case KeyEvent.VK_RIGHT: miNave.setPulsadoDerecha(true); break;
            case KeyEvent.VK_SPACE: miDisparo.posicionaDisparo(miNave); break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()){      
            case KeyEvent.VK_LEFT: miNave.setPulsadoIzquierda(false); break;
            case KeyEvent.VK_RIGHT: miNave.setPulsadoDerecha(false); break;
        }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}

package codigo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

/*
 * Autor: Daniel Bardera
 */
public class VentanaJuego extends javax.swing.JFrame {

    // Static si quieres que una variable no cambie durante el juego
    static int ANCHOPANTALLA = 600;

    // se pone en mayúsculas el nombre de la variable
    static int ALTOPANTALLA = 450;

    // Número de marcianos que van a aparecer  
    int filas = 8;
    int columnas = 8;

    //Variable de puntos
    int puntos = 0;

    //booleano de fin de juego
    boolean gameOver = false;

    //Booleano de colisiones bala-marciano
    boolean matado = false;

    // El buffer almacena las operaciones que vamos realizando para no 
    //modificar el jPanel
    BufferedImage buffer = null;

    // creamos un objeto de tipo nave
    Nave miNave = new Nave();

    // creamos un objeto de tipo disparo
    Disparo miDisparo = new Disparo();

    //Array 2 dimensiones, lista de marcianos
    Marciano[][] listaMarcianos = new Marciano[filas][columnas];

    //Declaro un objeto de tipo Explosion
    Explosion explosion = new Explosion();

    // Explosion [] listaExplosiones = new Explosion [filas][columnas];
    boolean direccionMarcianos = false;

    // el contador sirve para decidir que imagen del marciano toca poner
    int contador = 0;
    
    int numeroMarcianos = filas*columnas;

    // Imagen para cargar el SpriteSheet con todos los sprites del juego
    BufferedImage plantilla = null;

    //Array bidemensional de imagenes
    Image[][] imagenes;

    //Bucle propio del juego
    Timer temporizador = new Timer(10, new ActionListener() {
        // 10 es el tiempo que va a tardar en llamar a la función
        @Override
        public void actionPerformed(ActionEvent e) {
            bucleDelJuego();
            /*
            if(numeroMarcianos != 0)
            {
                
            }
            else{
                System.out.println("Ya has matado a todos los marcianos");
                //Nueva partida
                numeroMarcianos = filas * columnas;
                bucleDelJuego();
            }
            */
        }        
    });

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();

        //Para cargar el archivo de imágenes:
        // 1º Numbre del archivo
        // 2º Filas que tiene el SpriteSheet
        // 3º Columnas que tiene el SpriteSheet
        // 4º Lo que mide de ancho el SpriteSheet
        // 5º Lo que mide de alto el SpriteSheet
        // 6º Para cambiar el tamaño de los SpriteSheets          
        imagenes = cargaImagenes("/imagenes/invaders2.png", 5, 4, 64, 64, 2);

        // Disparo de SpriteSheet
        miDisparo.imagen = imagenes[3][3];

        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        // con el bufferedImage le marcamos el tipo del que tiene que ser 
        buffer.createGraphics();
        // Para poder escribir en el Buffer
        temporizador.start();
        // Para que comience a realizar la acción

        miNave.imagen = imagenes[4][2];

        //INICIALIZO POSICIÓN INICIAL DE LA NAVE
        miNave.x = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        // para que salga centrado
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this) - 40;
        //menos 40 para que salga un poco por encima 

        // INICIALIZO EL ARRAY DE MARCIANOS
        // Hacer esto usando mods (es decir, usando el bucle for anidado)
        // Parámetro 1: numero de filas de marcianos que estoy creando
        // Parámetro 2: fila dentro del spriteSheet del marciano que quiero pintar
        // Parámetro 3: columna dentro del spriteSheet del marciano que quiero pintar
        creaFilaDeMarcianos(0, 0, 2);
        creaFilaDeMarcianos(1, 2, 2);
        creaFilaDeMarcianos(2, 4, 0);
        creaFilaDeMarcianos(3, 0, 2);
        creaFilaDeMarcianos(4, 0, 2);
        creaFilaDeMarcianos(5, 0, 2);
        creaFilaDeMarcianos(6, 0, 2);
        creaFilaDeMarcianos(7, 0, 2);

    }

    private void reproduce(String cancion) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getClass().getResource(cancion)));
            clip.loop(0);

        } catch (Exception e) {
        }
    }

    private void creaFilaDeMarcianos(int numeroFila, int spriteFila, int spriteColumna) {

        for (int j = 0; j < columnas; j++) {

            listaMarcianos[numeroFila][j] = new Marciano();

            // Damos un valor concreto a imagen 1 o 2
            listaMarcianos[numeroFila][j].imagen1 = imagenes[spriteFila][spriteColumna];
            listaMarcianos[numeroFila][j].imagen2 = imagenes[spriteFila][spriteColumna + 1];

            listaMarcianos[numeroFila][j].setX(j * (15 + listaMarcianos[numeroFila][j].imagen1.getWidth(null)));
            listaMarcianos[numeroFila][j].setY(numeroFila * (10 + listaMarcianos[numeroFila][j].imagen1.getHeight(null)));
        }
    }

    /*
    Este método sirve para crear el array de imágenes con las pertenecientes
     al SpriteSheet. Devolverá un array de dos dimensiones con las imágenes
     que hay en el archivo
     */
    private Image[][] cargaImagenes(String nombreArchivoImagenes,
            int numFilas, int numColumnas, int ancho, int alto, int escala) {

        try { // intenta ejecutar lo que acabamos de añadir. Lo seleccionarnos al pulsar sobre el error
            plantilla = ImageIO.read(getClass().getResource(nombreArchivoImagenes));
        } catch (IOException ex) {
        }

        Image[][] arrayImagenes = new Image[numFilas + 1][numColumnas + 1];
        // Cargo las imagenes de forma individual en cada imagen del array de imagenes

        for (int i = 0; i < numFilas; i++) {
            // ponemos 6 porque la plantilla tiene 6 filas
            for (int j = 0; j < numColumnas; j++) {
                // ponemos 5 porque la plantilla tiene 5 columnas
                arrayImagenes[i][j] = plantilla.getSubimage(j * ancho, i * alto, ancho, alto);
                arrayImagenes[i][j] = arrayImagenes[i][j].getScaledInstance(ancho / escala, ancho / escala, Image.SCALE_SMOOTH);
            }
        }

        // La última fila del SpriteSheet solo mide 32cm de alto, asi que la hacemos a parte
        for (int j = 0; j < numColumnas; j++) {
            // ponemos 5 porque la plantilla tiene 5 columnas
            arrayImagenes[numFilas][j] = plantilla.getSubimage(j * ancho, numFilas * alto, ancho, alto / 2);
            arrayImagenes[numFilas][j] = arrayImagenes[numFilas][j].getScaledInstance(2 * ancho / escala, ancho / escala, Image.SCALE_SMOOTH);
        }

        // Cargo la última columna aparte
        for (int i = 0; i < numFilas; i++) {
            // ponemos 5 porque la plantilla tiene 5 columnas
            arrayImagenes[i][numColumnas] = plantilla.getSubimage(numColumnas * ancho, i * alto, ancho / 2, alto);
            arrayImagenes[i][numColumnas] = arrayImagenes[i][numColumnas].getScaledInstance(ancho / escala / 2, ancho / escala, Image.SCALE_SMOOTH);
        }
        return arrayImagenes;
    }

    private void bucleDelJuego() {
        // Gobierna el redibujado de los objetos en el jPanel1
        // Primero borro todo lo que hay en el buffer
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        //Si he perdido
        if (gameOver) {
            gameOver(g2);
        } 
        else {
            contador++;

            // Lo que hace g2 es que busque en la zona de memoria donde está el buffer
            g2.setColor(Color.BLACK);
            // Le decimos que lo pinte en color negro
            g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
            // Borramos lo que hubiera en pantalla

            ///////////////////////////////////////////////////////////////////////////
            // Redibujamos aquí cada elemento 
            // DIBUJO DISPARO
            g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
            // DIBUJO NAVE
            g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);

            pintaMarcianos(g2);
            matado = chequeaColision();
            pintaExplosiones(g2);

            // Se mueve la nave
            miNave.mueve();
            // Se mueve el disparo
            miDisparo.mueve();

        }
        ///////////////////////////////////////////////////////////////////////////
        //********** Fase Final: se dibuja el buffer sobre el jPanel1 ***********//
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);
        // El draw nos indica lo que debe dibujar en primer lugar, en segundo la 
        // posición y null porque si no no funciona
        
    }

    private void pintaExplosiones(Graphics2D g2) {
        //Cada vez que llamamos a la explosion voy haciendo que dure menos
        explosion.setTiempoDeVida(explosion.getTiempoDeVida() - 5);

        //Si la explosion dura mas de 10
        if (explosion.getTiempoDeVida() > 10) {
            g2.drawImage(explosion.boom, explosion.getX(), explosion.getY(), null);
        } //Si la explosion mide 9 o menos, la mando fuera de la pantalla y sumo puntos
        else {
            g2.drawImage(explosion.boom, explosion.getX() + 2000, explosion.getY() + 2000, null);
        }
    }

    private boolean chequeaColision() {
        boolean colisionado = false;

        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();

        // Rectángulo disparo
        rectanguloDisparo.setFrame(miDisparo.x,
                miDisparo.y,
                miDisparo.imagen.getWidth(null),
                miDisparo.imagen.getHeight(null));

        // Rectángulo marciano
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (listaMarcianos[i][j].vivo) {
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].getX(), listaMarcianos[i][j].getY(),
                            listaMarcianos[i][j].imagen1.getWidth(null),
                            listaMarcianos[i][j].imagen1.getHeight(null)
                    );
                }
                // chequea cuando se cruzan el disparo y marciano
                if (rectanguloDisparo.intersects(rectanguloMarciano)) {
                    // Pasamos de mandar al marciano fuera de la pantalla a eliminarlo
                    listaMarcianos[i][j].vivo = false;
                    //colocamos en la nave el disparo
                    miDisparo.posicionaDisparo(miNave);
                    miDisparo.y = 1000;
                    miDisparo.disparado = false;
                    //Pinto la explosion
                    explosion = new Explosion();
                    //Declaro su posicion
                    explosion.setX(listaMarcianos[i][j].getX());
                    explosion.setY(listaMarcianos[i][j].getY());
                    //Defino la imagen de la explosion
                    explosion.boom = imagenes[5][3];
                    //actualizo los puntos
                    puntos = puntos + 5;
                    colisionado = true;
                    numeroMarcianos--;
                }
            }
        }
        return colisionado;
    }

    private void chequeaColisionNave() {

        //Defino el rectangulo de un marciano
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        //Defino el rectangulo de una nave
        Rectangle2D.Double rectanguloNave = new Rectangle2D.Double();

        // Rectángulo disparo
        rectanguloNave.setFrame(miNave.x, miNave.y, miNave.imagen.getWidth(null), miNave.imagen.getHeight(null));

        // Rectángulo marciano
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (listaMarcianos[i][j].vivo) {
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].getX(), listaMarcianos[i][j].getY(),
                            listaMarcianos[i][j].imagen1.getWidth(null),
                            listaMarcianos[i][j].imagen1.getHeight(null)
                    );
                }
                // chequea cuando se cruzan el disparo y marciano
                if (rectanguloNave.intersects(rectanguloMarciano)) {
                    //MUERTO
                    gameOver = true;
                    puntos = 0;
                }
            }
        }
    }

    public void gameOver(Graphics2D g2) {
        //Sacar texto gameOver
        try {
            //Intento obtener la foto de gameOver
            Image imagenGameOver = ImageIO.read((getClass().getResource("/imagenes/looser.png")));
            //La pinto en el g2
            g2.drawImage(imagenGameOver, 0, 0, null);
        } catch (Exception e) {
        }
        reproduce("/sonidos/sonidos/nelsonjaja.wav");
    }

    private void cambiaDireccionMarcianos() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                //Cambias el valor de vX
                listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX() * -1);
                //Bajamos de fila a los marcianos
                listaMarcianos[i][j].mueveEjeY();
                //Compruebo, que los marcianos no han tocado la nave
                chequeaColisionNave();
            }
        }
    }

    private void pintaMarcianos(Graphics2D _g2) {

        //Declaro un ancho para el marciano
        int anchoMarciano = listaMarcianos[0][0].imagen1.getWidth(null);

        //Bucle para las filas
        for (int i = 0; i < filas; i++) {
            //Bucle para las columnas
            for (int j = 0; j < columnas; j++) {
                //Si el marciano esta vivo, se mueve a lo largo de la pantalla
                if (listaMarcianos[i][j].vivo) {
                    //Movemos los marcianos en el eje X hasta que cambien de direccion
                    listaMarcianos[i][j].mueve();
                    // Chequeo si el marciano choca contra la pared para cambiar la dirección de todos los marcianos
                    if (listaMarcianos[i][j].getX() + anchoMarciano == ANCHOPANTALLA || listaMarcianos[i][j].getX() == 0) {
                        direccionMarcianos = true;
                    }

                    // ponemos la imagen 1
                    if (contador < 50) {
                        _g2.drawImage(listaMarcianos[i][j].imagen1, listaMarcianos[i][j].getX(), listaMarcianos[i][j].getY(),
                                null);
                    } // ponemos la imagen 2
                    else if (contador < 100) {
                        _g2.drawImage(listaMarcianos[i][j].imagen2, listaMarcianos[i][j].getX(), listaMarcianos[i][j].getY(),
                                null);

                    } else {
                        contador = 0; // reseteamos contador y lo ponemos a 0 de nuevo
                    }
                }
            }
        }
        if (direccionMarcianos) {
            cambiaDireccionMarcianos();
            direccionMarcianos = false;
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
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(true);
                break;
            // break para que no ejecute la linea de abajo
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                reproduce("/sonidos/sonidos/aTomarPolCuloTodos.wav");
                miDisparo.posicionaDisparo(miNave);
                miDisparo.disparado = true;

                break;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(false);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(false);
                break;
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

package entidades;

import componentes.Estadisticas;
import componentes.Elemento;

public abstract class Enemigo {
    public String nombre;
    protected int xpRecompensa;
    protected int chatarraRecompensa;
    protected Estadisticas stats;
    protected float precision;
    
    /**
     * Constructor de la clase abstracta Enemigo.
     * Inicializa los atributos básicos de cualquier enemigo del juego.
     * * @param nombre Nombre del enemigo.
     * @param xpRecompensa Puntos de experiencia que otorga al ser derrotado.
     * @param chatarraRecompensa Cantidad de chatarra que deja caer al morir.
     * @param stats Objeto Estadisticas con los atributos de combate.
     * @param precision Probabilidad de acertar un ataque (valor entre 0.0 y 1.0).
     */
    public Enemigo(String nombre, int xpRecompensa, int chatarraRecompensa, Estadisticas stats, float precision) {
        this.nombre = nombre;
        this.xpRecompensa = xpRecompensa;
        this.chatarraRecompensa = chatarraRecompensa;
        this.stats = stats;
        this.precision = precision;
    }

    /**
     * Realiza un ataque físico contra el jugador basándose en la fuerza del enemigo y su precisión.
     * * @param cloud Objeto Jugador que recibe el ataque.
     */
    public void atacar(Jugador cloud) {
        int danoBase = (int)(stats.getFuerza() * 1.25); 

        if (Math.random() < precision) {
            System.err.println(nombre + " ataca e inflige " + danoBase + " de daño!");
            cloud.recibirDMG(danoBase);
        }
        else {
            System.err.println(nombre + " falló su ataque (MI BOMBO)."); 
        }
    }

    /**
     * Reduce el HP actual del enemigo considerando el daño recibido y su afinidad elemental.
     * * @param dano Cantidad de daño base a infligir.
     * @param elemento Tipo de elemento del ataque para calcular debilidades o resistencias.
     */
    public void recibirDMG(int dano, Elemento elemento) {
        stats.setHpActual(stats.getHpActual() - dano);
    }

    /**
     * Otorga la experiencia correspondiente al jugador una vez que el enemigo es derrotado.
     * * @param cloud Objeto Jugador que recibe la experiencia.
     */
    public void giveXpRecompensa(Jugador cloud) {
        System.out.println("    " + cloud.getNombre() + " recibe " + xpRecompensa + " de XP.");
        cloud.recibirXP(xpRecompensa);
    }
    
    public String getNombre() { return nombre; }
    public int getHpActual() { return stats.getHpActual(); }
    public int getChatarraRecompensa() { return chatarraRecompensa; }
}
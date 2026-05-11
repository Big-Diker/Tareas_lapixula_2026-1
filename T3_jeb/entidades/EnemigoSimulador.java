package entidades;

import componentes.Estadisticas;

public class EnemigoSimulador extends Enemigo {


    /**
     * Constructor de EnemigoSimulador.
     * Genera un soldado común con estadísticas base y experiencia aleatoria dentro de un rango de [15-20]
     */
    public EnemigoSimulador() {
        super(
            "Soldado común", 
            (int)(Math.random() * 6) + 15,
            0, 
            new Estadisticas(50, 50, 0, 0, 15, 0),
            0.85f);
    }

    /**
     * Verifica si el ataque físico del enemigo mataría al jugador de un solo golpe.
     * * @param cloud Objeto Jugador a evaluar.
     * @return true si el jugador sobrevivirá al golpe, false si el golpe sería fatal.
     */
    public boolean checkDanoSeguro(Jugador cloud) {
        if (cloud.getHpActual() <= (int)(this.stats.getFuerza()*1.25)) return false; // Golpe fatal
        else return true; 
    }
    
    /**
     * Realiza un ataque físico contra el jugador, deteniéndose si detecta que el golpe resultaría fatal.
     * * @param cloud Objeto Jugador que recibe el ataque.
     */
    @Override
    public void atacar(Jugador cloud) {
        if (!checkDanoSeguro(cloud)) {
            System.out.println("El " + this.getNombre() + " recuerda que le pagaron para no matarte y decide no herirte");
        }
        else {
        System.out.println("El " + this.getNombre() + " eleva su arma...");
        super.atacar(cloud);
        }
    }
}
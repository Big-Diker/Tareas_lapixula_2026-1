package entidades;

import componentes.Estadisticas;

public class EnemigoSimulador extends Enemigo {

    public EnemigoSimulador() {
        super(
            "Soldado común", 
            (int)(Math.random() * 6) + 15, // truncar con el *6 genera: +[0,5] -> numero entre 15 y 20
            0, 
            new Estadisticas(50, 50, 0, 0, 15, 0),
            0.85f);
    }

    public boolean checkDanoSeguro(Jugador cloud) {
        if (cloud.getHpActual() <= (int)(this.stats.getFuerza()*1.25)) return false; // Golpe fatal
        else return true; 
    }
    
    @Override
    public void atacar(Jugador cloud) {
        if (!checkDanoSeguro(cloud)) {
            System.out.println("El " + this.getNombre() + "se compadece de tu fragilidad y decide no herirte");
        }
        else {
        System.out.println("El " + this.getNombre() + " eleva su arma...");
        super.atacar(cloud);
        }
    }
}
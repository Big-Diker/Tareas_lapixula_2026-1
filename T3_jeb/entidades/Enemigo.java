package entidades;

import componentes.Estadisticas;

public abstract class Enemigo {
    public String nombre;
    protected int xpRecompensa;
    protected int chatarraRecompensa;
    protected Estadisticas stats;
    
    public Enemigo(String nombre, int xpRecompensa, int chatarraRecompensa, Estadisticas stats) {
        this.nombre = nombre;
        this.xpRecompensa = xpRecompensa;
        this.chatarraRecompensa = chatarraRecompensa;
        this.stats = stats;
    }

    public void atacar(Jugador cloud) {
        int danoBase = (int)(this.stats.getFuerza() * 1.25); 

        if (Math.random() < 0.85) {
            cloud.recibirDMG(danoBase);
            System.out.println(this.nombre + " ataca e inflige " + danoBase + " de daño!");
        }
        else {
            System.out.println(this.nombre + " falló su ataque (MI BOMBO)."); 
        }
    }

    public void recibirDMG() {
        // pendiente
    }

    public void giveXpRecompensa(Jugador cloud) {
        cloud.recibirXP(this.xpRecompensa);
        System.out.println(cloud.getNombre() + " recibe " + this.xpRecompensa + " de XP.");
    }
    
    public String getNombre() { return nombre; }
    public int getChatarraRecompensa() { return chatarraRecompensa; }
}
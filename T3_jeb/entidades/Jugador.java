package entidades;

import java.util.ArrayList;
import java.util.List;

import componentes.Elemento;
import componentes.Estadisticas;
import componentes.Materia;

public class Jugador {
    public String nombre = "Cloud";
    private int nivel;
    private int xpActual;
    private int chatarra;
    private int limiteActual;
    private Estadisticas stats;
    private List<Materia> mochila;
    private Arma busterSword;

    public Jugador() {
        nivel = 1;
        xpActual = 0;
        chatarra = 0;
        limiteActual = 0;
        stats = new Estadisticas(200, 200, 50, 50, 15, 15);
        mochila = new ArrayList<>();
        busterSword = new Arma();
    }

    
    public boolean estaMuerto() {
        if (stats.getHpActual() <= 0) { return true; }
        else { return false; }
    }
    
    public void recibirDMG(int dano) {
        stats.setHpActual(stats.getHpActual() - dano);

        limiteActual += (int)(dano / 2);
        if (limiteActual > 100) { limiteActual = 100; } // Overflow de limite

        if (dano < Integer.MAX_VALUE) {
            System.out.println("OUCH! " + this.nombre + " ha recibido " + dano + " de daño!!");
        }
        else {
            System.out.println("Cloud recibe " + dano + " de daño explosivo");
        }
    }

    public void recibirChatarra(int chatarra) {
        setChatarra(getChatarra() + chatarra);
    }
    
    public void lvlUp(){
        int umbralLvlUp = this.nivel * 10;
        
        if (this.xpActual >= umbralLvlUp) {
            this.xpActual -= umbralLvlUp; // "Cobra" la experiencia necesaria para subir de nivel
            this.nivel += 1;
            
            subirHp(10);
            subirMp(5);
            subirFuerza(4);
            subirMagia(6);;
            
            System.out.println(this.nombre + " ha subido a nivel " + this.nivel + "!" + System.lineSeparator() + 
            "   -MaxHP↗  10 (Nuevo max -> " + stats.getHpMaximo() + System.lineSeparator() +
            "   -MaxMP↗  5  (Nuevo max -> " + stats.getMpMaximo() + System.lineSeparator() +
            "   -Fuerza↗ 4  (Nuevo max -> " + stats.getFuerza() + System.lineSeparator() +
            "   -Magia↗  6  (Nuevo max -> " + stats.getMagia() + System.lineSeparator()
            );
        
            lvlUp(); // Recursividad por si es subida multiple de nivel
        }
    }

    public void recibirXP(int xp) {
        this.xpActual += xp;
        lvlUp();
    }

    public int getChatarra() { return chatarra; }
    public int getHpActual() { return stats.getHpActual(); }
    public String getNombre() { return nombre; }

    public void subirHp(int hp) {
        stats.setHpMaximo(stats.getHpMaximo() + hp);
        stats.setHpActual(stats.getHpActual() + hp);
    }
    public void subirMp(int mp) {
        stats.setMpMaximo(stats.getMpMaximo() + mp);
        stats.setMpActual(stats.getMpActual() + mp);
    }
    public void subirFuerza(int fuerza) { stats.setFuerza(stats.getFuerza() + fuerza); }
    public void subirMagia(int magia)   { stats.setMagia(stats.getMagia() + magia);    }

    public void setChatarra(int chatarra) { this.chatarra = chatarra; }


    public class Arma {
        public String nombre = "Buster Sword";
        private List<Materia> materiasEquipadas;

        public Arma() {
            this.materiasEquipadas = new ArrayList<>();
        }

        public int calcularDanoMagico(Elemento elemento) {
            return 0; // pendiente
        }

        public int calcularDanoFisico() {
            int danoFisico = (int)(stats.getFuerza() * 1.25);
            return danoFisico;
        }

        public int calcularDanoLimite() {
            int danoLimite = stats.getFuerza() * 5;
            return danoLimite;
        }
    }
}

package entidades;

import java.util.ArrayList;
import java.util.List;

import componentes.Elemento;
import componentes.Estadisticas;
import componentes.Vulnerable;

public class EnemigoSalvaje extends Enemigo implements Vulnerable {
    private List<Elemento> debilidades;
    private List<Elemento> resistencias;
    private List<Elemento> inmunidades;

    public EnemigoSalvaje(String nombre, int hp, int fuerza, int xp, int chatarra) {
        super(nombre, xp, chatarra, new Estadisticas(hp, hp, 0, 0, fuerza, 0), 0.85f);

        this.debilidades = new ArrayList<>();
        this.resistencias = new ArrayList<>();
        this.inmunidades = new ArrayList<>();
    }


    public void giveChatarraRecompensa(Jugador Cloud) {
        int chatarra = super.getChatarraRecompensa();
        Cloud.recibirChatarra(chatarra);
    }

    @Override
    public double evaluarDebilidad(Elemento elementoMagia) {
        if (this.debilidades.contains(elementoMagia))       { return 2.0; }
        else if (this.resistencias.contains(elementoMagia)) { return 0.5; }
        else if (this.inmunidades.contains(elementoMagia))  { return 0.0; }
        else                                                { return 1.0; }
    }

    public void añadirDebilidad(Elemento elemento)   { this.debilidades.add(elemento);  }
    public void añadirResistencia(Elemento elemento) { this.resistencias.add(elemento); }
    public void añadirInmunidad(Elemento elemento)   { this.inmunidades.add(elemento);  }
}

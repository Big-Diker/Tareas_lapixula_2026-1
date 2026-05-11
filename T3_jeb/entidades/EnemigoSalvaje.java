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

    /**
     * Constructor de EnemigoSalvaje.
     * Inicializa un monstruo de la selva con sus listas de afinidades elementales vacías.
     * * @param nombre Nombre del monstruo.
     * @param hp Vida máxima y actual del monstruo.
     * @param fuerza Atributo de daño físico.
     * @param xp Experiencia que otorga al morir.
     * @param chatarra Chatarra que otorga al morir.
     */
    public EnemigoSalvaje(String nombre, int hp, int fuerza, int xp, int chatarra) {
        super(nombre, xp, chatarra, new Estadisticas(hp, hp, 0, 0, fuerza, 0), 0.85f);

        this.debilidades = new ArrayList<>();
        this.resistencias = new ArrayList<>();
        this.inmunidades = new ArrayList<>();
    }

    /**
     * Otorga la chatarra correspondiente al jugador tras derrotar al enemigo en la selva.
     * * @param cloud Objeto Jugador que recibe la chatarra.
     */
    public void giveChatarraRecompensa(Jugador cloud) {
        cloud.recibirChatarra(getChatarraRecompensa());
        System.out.println(cloud.getNombre() + " recibe " + chatarraRecompensa + " de chatarra.");
    }

    /**
     * Evalúa la vulnerabilidad del enemigo frente a un elemento mágico específico.
     * * @param elementoMagia Elemento del ataque recibido.
     * @return Multiplicador de daño (2.0 debilidad, 0.5 resistencia, 0.0 inmunidad, 1.0 neutral).
     */
    @Override
    public double evaluarDebilidad(Elemento elementoMagia) {
        if (this.debilidades.contains(elementoMagia))       { return 2.0; }
        else if (this.resistencias.contains(elementoMagia)) { return 0.5; }
        else if (this.inmunidades.contains(elementoMagia))  { return 0.0; }
        else                                                { return 1.0; }
    }

    /**
     * Calcula y aplica el daño final recibido por el enemigo salvaje considerando sus afinidades elementales.
     * Imprime un mensaje en consola dependiendo de la efectividad del ataque (super efectivo, poco efectivo o inmune).
     * @param dano Cantidad de daño base del ataque.
     * @param elemento Elemento mágico del ataque para evaluar debilidades, resistencias o inmunidades.
     */
    @Override
    public void recibirDMG(int dano, Elemento elemento) {
        double multiplicador = evaluarDebilidad(elemento);
        int danoFinal = (int)(dano * multiplicador);
        
        this.stats.setHpActual(this.stats.getHpActual() - danoFinal);
        
        if (multiplicador > 1.0) System.out.println("Es super eficaz!");
        else if (multiplicador < 1.0 && multiplicador > 0) System.out.println("No es muy eficaz...");
        else if (multiplicador == 0) System.out.println("No afecta a este enemigo...");
    }
    /**
     * Añade un elemento a la lista de debilidades del enemigo.
     * * @param elemento Elemento al que será vulnerable (recibirá el doble de daño).
     */
    public void añadirDebilidad(Elemento elemento)   { this.debilidades.add(elemento);  }

    /**
     * Añade un elemento a la lista de resistencias del enemigo.
     * * @param elemento Elemento al que será resistente (recibirá la mitad de daño).
     */
    public void añadirResistencia(Elemento elemento) { this.resistencias.add(elemento); }

    /**
     * Añade un elemento a la lista de inmunidades del enemigo.
     * * @param elemento Elemento al que será inmune (no recibirá daño).
     */
    public void añadirInmunidad(Elemento elemento)   { this.inmunidades.add(elemento);  }
}

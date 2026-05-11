package mapa;

import entidades.*;
import componentes.Mejora;
import componentes.TipoStat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Sector7 extends Zona {
    private Scanner sc = new Scanner(System.in);

    /**
     * Constructor de Sector7.
     * Inicializa la zona principal del juego, la cual no tiene requerimiento de nivel.
     */
    public Sector7() {
        super("Sector 7", 0);
    }

    /**
     * Despliega el menú principal del Sector 7.
     * Actúa como base de operaciones permitiendo acceder al simulador, la tienda, recuperar vitalidad o viajar.
     * @param cloud Objeto Jugador que se encuentra en la base.
     */
    @Override
    public void accionZona(Jugador cloud) {
        boolean enMenu = true;
        while (enMenu) {
            System.out.println("\n      SECTOR 7\n");
            System.out.println("    Stats de Cloud: Nivel " + cloud.getNivel() + " | HP: " + cloud.getHpActual() + " | Chatarra: " + cloud.getChatarra());
            System.out.println("¿Qué deseas hacer?");
            System.out.println("1. Entrar al Simulador de Combate");
            System.out.println("2. Vitrinear el mall chino");
            System.out.println("3. Tomarte unas chelitas en la boti de la esquina");
            System.out.println("4. Revisar la mochila para ver que no te hayan cogoteado (Menú de materias)");
            System.out.println("0. Abrir gogul© maps (Viajar)");
            cloud.setLimite(0);

            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1: 
                    entrarSimulador(cloud);
                    break;
                case 2:
                    entrarTienda(cloud);
                    break;
                case 3:
                    descansar(cloud);
                    break;
                case 4:
                    cloud.gestionarEquipo();
                    break;
                case 0:
                    enMenu = false;
                    break;
                case 777:
                    cloud.cheatSubirNivel20();
                    break;
                default: System.err.println("Opción no válida.");
            }
        }
    }

    /**
     * Inicia un combate de práctica mediante el simulador.
     * Posee una regla especial donde el jugador no pierde si su HP llega a 0, sobreviviendo con 1 HP.
     * @param cloud Objeto Jugador que entra al combate simulado.
     */
    private void entrarSimulador(Jugador cloud) {
        System.out.println("\n      Entrando en combate simulador");
        List<Enemigo> enemigos = generarEnemigosSimulados();

        this.ejecutarBucleCombate(cloud, enemigos, true);

        if (cloud.getHpActual() <= 0) {
            cloud.setHpActual(1);
            System.out.println("Cloud se aferra a las ganas de ir a tomar y sobrevive el combate con 1 de HP");
        }
    }

    /**
     * Abre el menú de la tienda, permitiendo al jugador comprar mejoras de estadísticas a cambio de chatarra.
     * @param cloud Objeto Jugador que interactúa con la tienda.
     */
    private void entrarTienda(Jugador cloud) {
        while (true) {
            System.out.println("\n      MALL CHINO (Chatarra: " + cloud.getChatarra() + ")");
            System.out.println("1. Lays coreanas de pescado (Mejora de vitalidad +20 HP Máx) | Costo: 100 de chatarra");
            System.out.println("2. Ramune de lima (Mejora de Éter, +10 MP Máx) | Costo: 120 de chatarra");
            System.out.println("3. Buldak negro (Mejora Física +10 Fuerza) | Costo: 150 de chatarra");
            System.out.println("0. Salir");
            
            int eleccion = sc.nextInt();
            if (eleccion == 0) break;

            Mejora compra = null;
            switch (eleccion) {
                case 1: 
                    compra = new Mejora("Vitalidad", 100, TipoStat.HP_MAX, 20); 
                    break;
                case 2: 
                    compra = new Mejora("Éter", 120, TipoStat.MP_MAX, 10); 
                    break;
                case 3: 
                    compra = new Mejora("Física", 150, TipoStat.FUERZA, 10); 
                    break;
                default: 
                    System.err.println("Opción inválida.");
                    continue;
            }

            if (compra != null) {
                compra.aplicarMejora(cloud);
            }
        }
    }

    /**
     * Restaura completamente los puntos de vida (HP) y magia (MP) del jugador de forma gratuita.
     * @param cloud Objeto Jugador que toma un descanso.
     */
    private void descansar(Jugador cloud) {
        System.out.println("\nCloud se pide su tonta calafate...");
        cloud.setHpActual(cloud.getHpMaximo());
        cloud.setMpActual(cloud.getMpMaximo());
        System.out.println("Ahhhh un manjarshh (HP y MP restaurados al máximo)");
    }

    /**
     * Genera un grupo de enemigos genéricos para usar en el simulador de combate.
     * Siempre incluye un enemigo, con un 50% de probabilidad de generar un segundo.
     * @return Lista de enemigos generada para el simulador.
     */
    private List<Enemigo> generarEnemigosSimulados() {
        List<Enemigo> enemigos = new ArrayList<>();
        int chance = (int)(Math.random() * 50);

        enemigos.add(new EnemigoSimulador()); // Añade 1 si o si
        if (chance < 50) enemigos.add(new EnemigoSimulador()); // 50% chance de añadir otro mas

        return enemigos;
    }
}
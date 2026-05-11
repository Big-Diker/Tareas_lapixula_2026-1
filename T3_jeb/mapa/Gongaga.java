package mapa;

import entidades.*;
import componentes.Materia;
import componentes.Elemento;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Gongaga extends Zona {
    private List<Materia> poolMaterias;

    /**
     * Constructor de Gongaga.
     * Inicializa la zona con un requerimiento de nivel 5 y carga el pool de materias que se pueden encontrar.
     */
    public Gongaga() {
        super("Selva de Gongaga", 5);
        this.poolMaterias = new ArrayList<>();
        poolMaterias.add(new Materia("Fuego", Elemento.FUEGO));
        poolMaterias.add(new Materia("Hielo", Elemento.HIELO));
        poolMaterias.add(new Materia("Rayo", Elemento.RAYO));
        poolMaterias.add(new Materia("Cura", Elemento.CURA));
    }

    /**
     * Controla el bucle de exploración de la selva.
     * Permite al jugador buscar objetos, gestionar su equipo o retirarse, evaluando la penalización por muerte.
     * @param cloud Objeto Jugador que explora la zona.
     */
    @SuppressWarnings("resource")
    @Override
    public void accionZona(Jugador cloud) {
        Scanner sc = new Scanner(System.in);
        boolean explorando = true;

        while (explorando) {
            System.out.println("\n      Selva de Gongaga");
            System.out.println("    Qué deseas hacer?");
            System.out.println("1. Explorar la selva");
            System.out.println("2. Revisar la mochila para ver que no te hayan cogoteado (Menú de materias)");
            System.out.println("0. Volver al mapa");
            cloud.setLimite(0);
            
            if (!sc.hasNextInt()) {
                System.out.println("Opción inválida.");
                sc.next();
                continue;
            }

            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("\nTe adentras en la densa selva...");
                    int chance = (int)(Math.random() * 100);

                    if (chance < 30) {
                        encontrarMateria(cloud);
                    } else {
                        iniciarEmboscada(cloud);
                    }

                    if (cloud.getHpActual() <= 0) {
                        explorando = false;
                        System.out.println("Cloud recibe daño fatal y vuelve al Sector7");
                        cloud.setHpActual(cloud.getHpMaximo());
                        cloud.vaciarMochilaYChatarra();
                        return;
                    }
                    break;
                case 2:
                    cloud.gestionarEquipo();
                    break;
                case 0:
                    System.out.println("Te subes al cabify...");
                    explorando = false;
                    break;

                default:
                    System.out.println("Opción invalida.");
                    break;
            }
        }
    }

    /**
     * Selecciona una materia aleatoria del pool disponible en la zona y la añade a la mochila del jugador.
     * @param cloud Objeto Jugador que recibe el objeto encontrado.
     */
    private void encontrarMateria(Jugador cloud) {
        int index = (int)(Math.random() * poolMaterias.size());
        Materia encontrada = poolMaterias.get(index);
        
        Materia nueva = new Materia("Materia de " + encontrada.getNombre(), encontrada.getElemento());
        cloud.getMochila().add(nueva);
        System.out.println("¡Has encontrado una " + nueva.getNombre() + "!");
    }

    /**
     * Genera una emboscada obligando al jugador a entrar en combate con un grupo de enemigos salvajes.
     * @param cloud Objeto Jugador que sufre la emboscada.
     */
    private void iniciarEmboscada(Jugador cloud) {
        System.out.println("¡EMBOSCADA! Monstruos salvajes aparecen de la maleza.");
        List<Enemigo> grupo = generarGrupoEnemigo();
        
        System.out.println("Te enfrentas a " + grupo.size() + " enemigo(s).");
        
        ejecutarBucleCombate(cloud, grupo, true);
    }

    /**
     * Genera aleatoriamente un grupo de monstruos propios de la selva.
     * Determina una cantidad variable (1 a 3) y el tipo específico de enemigo, asignando recompensas aleatorias.
     * @return Lista con los enemigos generados para el encuentro.
     */
    public List<Enemigo> generarGrupoEnemigo() {
        List<Enemigo> grupo = new ArrayList<>();
        int p = (int)(Math.random() * 100);
        
        int cantidad = (p < 60) ? 1 : (p < 90) ? 2 : 3; // Numero enemigos en emboscada x chance

        for (int i = 0; i < cantidad; i++) {
            int tipo = (int)(Math.random() * 3);
            int xp = ((int)(Math.random() * 21) + 80);
            int chatarra = ((int)(Math.random() * 16) + 50);
            
            EnemigoSalvaje e;
            if (tipo == 0) {
                e = new EnemigoSalvaje("Planta Carnívora", 80, 15, xp, chatarra);
                e.añadirDebilidad(Elemento.FUEGO);
                e.añadirDebilidad(Elemento.HIELO);
                e.añadirInmunidad(Elemento.RAYO);     
            } else if (tipo == 1) {
                e = new EnemigoSalvaje("Sapo de la Jungla", 60, 12, xp, chatarra);
                e.añadirDebilidad(Elemento.HIELO);
                e.añadirDebilidad(Elemento.RAYO);
                e.añadirResistencia(Elemento.FUEGO);
            } else {
                e = new EnemigoSalvaje("Robot Centinela", 100, 20, xp, chatarra);
                e.añadirDebilidad(Elemento.RAYO);
                e.añadirResistencia(Elemento.FISICO);
                e.añadirResistencia(Elemento.HIELO);
            }
            grupo.add(e);
        }
        return grupo;
    }
}
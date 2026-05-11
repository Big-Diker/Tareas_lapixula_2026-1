package mapa;

import entidades.Enemigo;
import entidades.EnemigoSalvaje;
import entidades.Jugador;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import componentes.Elemento;

public abstract class Zona {
    public String nombre;
    protected int nivelRequerido;
    protected List<Enemigo> enemigosDisponibles;

    /**
     * Constructor de la clase abstracta Zona.
     * Inicializa el nombre de la zona y el nivel mínimo requerido para ingresar a ella.
     * @param nombre Nombre descriptivo de la zona.
     * @param nivelRequerido Nivel mínimo que debe tener el jugador para acceder.
     */
    public Zona(String nombre, int nivelRequerido) {
        this.nombre = nombre;
        this.nivelRequerido = nivelRequerido;
        this.enemigosDisponibles = new ArrayList<>();
    }

    /**
     * Método abstracto que define el comportamiento principal al entrar a una zona.
     * Debe ser implementado por cada zona específica para manejar sus propios menús y eventos.
     * @param cloud Objeto Jugador que interactúa con la zona.
     */
    public abstract void accionZona(Jugador cloud);

    /**
     * Verifica si el jugador cumple con el nivel necesario para entrar a la zona.
     * @param cloud Objeto Jugador que intenta acceder.
     * @return true si el jugador tiene el nivel suficiente, false en caso contrario.
     */
    public boolean validarAcceso(Jugador cloud) {
        if (cloud.getNivel() >= this.nivelRequerido) {
            return true;
        }
        else {
            System.out.println("Anda a farmear, necesitas ser nivel " + nivelRequerido + " para entrar a esta zona");
            return false;
        }
    }

    /**
     * Motor principal del sistema de combate por turnos.
     * Maneja el menú de acciones del jugador (atacar, magia, límite, huir), la respuesta de los enemigos,
     * la entrega de recompensas al derrotarlos y la ejecución de comandos ocultos (cheats).
     * @param cloud Objeto Jugador que participa en el combate.
     * @param enemigos Lista de enemigos a enfrentar en este encuentro.
     * @param puedeHuir Indica si el jugador tiene la opción de intentar escapar del combate.
     */
    protected void ejecutarBucleCombate(Jugador cloud, List<Enemigo> enemigos, boolean puedeHuir) {

        System.out.print("Entras en combate con:");
        for (Enemigo enemigo : enemigos) {
            System.out.println(" " + enemigo.getNombre());
            if (enemigo != enemigos.getLast()) {
            System.out.print(","); // Una coma para separarlos
            }
        }

        Scanner input = new Scanner(System.in);
        
        boolean huirExito = false;

        while (cloud.getHpActual() > 0 && !enemigos.isEmpty() && !huirExito) {
            System.out.println("\n      TURNO DE CLOUD");
            System.out.println("HP: " + cloud.getHpActual() + "/" + cloud.getHpMaximo());
            System.out.println("MP: " + cloud.getMpActual() + "/" + cloud.getMpMaximo());
            System.out.println("Limite: [" + cloud.getLimiteActual() + "/100]");
            
            System.out.println("\nEnemigos en el área:");
            for (int i = 0; i < enemigos.size(); i++) {
                System.out.println((i + 1) + ". " + enemigos.get(i).getNombre() + " (HP: " + enemigos.get(i).getHpActual() + ")");
            }

            System.out.println("\nAcciones: 1. Atacar | 2. Magia | 3. Limite" + (puedeHuir ? " | 4. Huir" : ""));
            int opcionMenu = input.nextInt();
            input.nextLine();
            boolean turnoConsumido = false;

            switch (opcionMenu) {
                case 1: // Atacar
                    while (!turnoConsumido) {
                        System.out.println("Elige a tu víctima (0 si quieres volver):");
                        for (int i = 0; i < enemigos.size(); i++) {
                            System.out.println((i + 1) + ". " + enemigos.get(i).getNombre() + " (HP: " + enemigos.get(i).getHpActual() + ")");
                        }
                        System.out.println("0. Volver");
                        int obj = input.nextInt() - 1;
                        input.nextLine();
                        if (obj >= 0 && obj < enemigos.size()) {
                            cloud.atacar("FISICO", enemigos.get(obj), Elemento.FISICO);
                            System.out.println("Cloud ataca a " + enemigos.get(obj).getNombre() + " e inflige " + cloud.getBusterSword().calcularDanoFisico() + " puntos de daño!");
                            turnoConsumido = true;
                        }
                        else if (obj == -1) { break; } // Salir del menu
                        else { System.err.println("Selecciona un enemigo válido."); }
                    }
                    break;
                case 2: // Magia
                    while (!turnoConsumido) {
                        System.out.println("Selecciona el tipo de magia: 1. Ofensiva | 2. Defensiva | 0. Volver");
                        int inputMagia = input.nextInt();
                        if (inputMagia == 0) break;

                        List<Elemento> disponibles = cloud.getBusterSword().getElementosPorTipo(inputMagia);
                        
                        if (disponibles.isEmpty()) {
                            System.err.println("No tienes magias de ese tipo equipadas.");
                            continue; // Vuelve al menú
                        }

                        System.out.println("Selecciona el elemento:");
                        for (int i = 0; i < disponibles.size(); i++) {
                            System.out.println((i + 1) + ". " + disponibles.get(i));
                        }

                        int sel = input.nextInt() - 1;
                        input.nextLine();

                        if (sel >= 0 && sel < disponibles.size()) {
                            Elemento elegido = disponibles.get(sel);

                            int costoMP = cloud.costoMP(elegido);

                            if (cloud.getMpActual() >= costoMP) {
                                if (elegido == Elemento.CURA) {
                                    System.out.println("\nCloud canaliza el flujo vital...");
                                    cloud.atacar("MAGICO", enemigos.get(0), elegido);
                                    turnoConsumido = true;
                                } 
                                else {
                                    System.out.println("\n--- Selecciona tu objetivo ---");
                                    for (int i = 0; i < enemigos.size(); i++) {
                                        System.out.println((i + 1) + ". " + enemigos.get(i).getNombre() + " (HP: " + enemigos.get(i).getHpActual() + ")");
                                    }
                                    System.out.println("0. Volver");
                                    
                                    int obj = input.nextInt() - 1;
                                    input.nextLine(); 
                                    
                                    if (obj >= 0 && obj < enemigos.size()) {
                                        cloud.atacar("MAGICO", enemigos.get(obj), elegido);
                                        turnoConsumido = true;
                                    } else if (obj == -1) { 
                                        continue;
                                    } else { 
                                        System.err.println("Objetivo inválido."); 
                                    }
                                }
                            }
                            else {
                                System.err.println("MP insuficiente para lanzar " + elegido + " (Costo: " + costoMP + ")");
                            }
                        }
                    }
                    break;

                case 3: // LimitBreaker
                    if (cloud.getLimiteActual() >= 100) {
                        int danoLim = cloud.getBusterSword().calcularDanoLimite();
                        System.out.println("OMNISLASH! Cloud causa " + danoLim + " de daño masivo.");
                        for (Enemigo e : enemigos) { // Daño en area
                            cloud.atacar("LIMITE", e, Elemento.LIMITE);
                        }
                        turnoConsumido = true;
                    }
                    else {
                        System.out.println("Límite insuficiente, pega o tanquea más");
                    }
                    break;

                case 4: // Huir
                    if (puedeHuir && Math.random() < 0.5) { // 50% exito
                        System.out.println("¡Escapaste con éxito!");
                        huirExito = true;
                        break;
                    }
                    else if (puedeHuir) {
                        System.out.println("¡Fallo al intentar huir! Pierdes el turno.");
                        turnoConsumido = true;
                    }
                    break;
                case 99:
                    cloud.setHpMaximo(10000);
                    cloud.setHpActual(10000);
                    System.out.println("Cloud recuerda que tenía un galletón quaker en la mochila (HP = 10000)");
                    break;
                case 727:
                    System.out.println("Cloud espera su destino... (Cooked dog.png)");
                    turnoConsumido = true;
                    break;
                default:
                    System.err.println("Selección inválida.");
                    break;
            }

            enemigos.removeIf(e -> {
                if (e.getHpActual() <= 0) {
                    System.out.println(e.getNombre() + " ha sido derrotado!");
                    e.giveXpRecompensa(cloud);
                    if (e instanceof EnemigoSalvaje) { ((EnemigoSalvaje) e).giveChatarraRecompensa(cloud); }
                    return true;
                }
                return false;
            });

            if (turnoConsumido && !enemigos.isEmpty() && !huirExito) {
                System.out.println("\n      TURNO ENEMIGO");
                
                int numEnemigos = enemigos.size();
                boolean ataqueConjunto = false;

                if (numEnemigos == 3 && Math.random() < 0.33) {
                    ataqueConjunto = true;
                } else if (numEnemigos == 2 && Math.random() < 0.50) {
                    ataqueConjunto = true;
                }
                // Si hay ataque conjunto, todos atacan
                if (numEnemigos > 1 && ataqueConjunto) {
                    for (Enemigo e : enemigos) {
                        e.atacar(cloud);
                    }
                }
                // Si no gana la probabilidad, ataca solo 1 enemigo cualquiera
                else {
                    Enemigo atacante = enemigos.get((int)(Math.random() * numEnemigos));
                    atacante.atacar(cloud);
                }
            }
        }

        if (cloud.getHpActual() <= 0) {
            return;
        }

        else if (enemigos.isEmpty()) {
            System.out.println("\nEnemigo(s) derrotado(s), saliendo de combate...\n");
            cloud.setLimite(0);
            return;
        }
    }
        
    

    public String getNombre() { return nombre; }
}
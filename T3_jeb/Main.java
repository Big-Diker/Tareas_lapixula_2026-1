import mapa.*;
import entidades.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Jugador cloud = new Jugador();
        Sector7 sector7 = new Sector7();
        Gongaga gongaga = new Gongaga();
        NucleoPlaneta nucleo = new NucleoPlaneta();
        System.out.println("        BIENVENIDO A LA ULTIMA FANTASIA");

        boolean esStart = true;
        if (esStart) {
            esStart = false;
            sector7.accionZona(cloud); // Inicia el juego en sector7, luego vuelves al menu de debajo
        }
        
        boolean jugando = true;
        while (jugando) {
            if (cloud.getHpActual() <= 0) {
                System.out.println("\n      Un uber se llevo a Cloud pa la casa... (Sector 7)\n");
                cloud.setHpActual(cloud.getHpMaximo());
                sector7.accionZona(cloud); 
                continue;
            }

            System.out.println("¿A dónde deseas viajar?");
            System.out.println("1. Sector 7 (Base / Tienda / Simulador)");
            System.out.println("2. Selva de Gongaga (Exploración / Farm)");
            System.out.println("3. Núcleo del Planeta (FINAL BOSS - Nivel 20)");

            int eleccion = sc.nextInt();
            sc.nextLine();

            switch (eleccion) {
                case 1:
                    sector7.accionZona(cloud);
                    break;
                case 2:
                    if (gongaga.validarAcceso(cloud)) {
                        gongaga.accionZona(cloud);
                    }
                    break;
                case 3:
                    int materiasEquipadas = cloud.getBusterSword().getMateriasEquipadas().size(); // Cantidad de materias en la espada
                    if (cloud.getNivel() >= 20 && materiasEquipadas >= 2) {
                        System.out.println("\nVamo a plantar cara...\n");
                        nucleo.accionZona(cloud);
                    } else {
                        System.err.println("\nNivel de Cloud o cantidad de materias insuficientes pa ir a dar la cara (Nivel 20 y 2 materias en tu arma requeridos)\n");
                    }
                    break;
                default:
                    System.err.println("\nSelecciona una opción válida.\n");
            }
        }
        sc.close();
    }
}

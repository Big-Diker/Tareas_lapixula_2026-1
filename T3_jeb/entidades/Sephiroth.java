package entidades;

import componentes.Estadisticas;

import java.util.Random;

public class Sephiroth extends Enemigo{
    private int contadorSuperNova;

    /**
     * Constructor de Sephiroth.
     * Inicializa al jefe final con sus estadísticas máximas y el contador de Supernova en 0.
     */
    public Sephiroth() {
        contadorSuperNova = 0;
        super("Sephiroth", 0, 0, 
        new Estadisticas(500, 500, 0, 0, 40, 0), 0.9f);
    }

    /**
     * Realiza un ataque físico contra el jugador y avanza el contador para el ataque definitivo.
     * * @param cloud Objeto Jugador que recibe el ataque.
     */
    @Override
    public void atacar(Jugador cloud) {
        super.atacar(cloud);
        avanzarContadorSuperNova(cloud);
    }

    /**
     * Reinicia el contador del ataque Supernova a 0. Usualmente tras recibir un ataque Límite.
     */
    public void resetSuperNova() {
        contadorSuperNova = 0;
    }

    /**
     * Incrementa el contador de turnos de Sephiroth. Al llegar a 10, ejecuta el ataque Supernova.
     * * @param cloud Objeto Jugador objetivo.
     */
    public void avanzarContadorSuperNova(Jugador cloud) {
        contadorSuperNova += 1;
        System.out.println("Contador de Super Nova -> " + contadorSuperNova);

        if (contadorSuperNova == 1) {
            System.out.println("   (No dejes que llegue a 10!! Recuerda que puedes reiniciarlo con tu Limit Breaker)");
        }
        else if (contadorSuperNova > 5 && contadorSuperNova < 9) {
            System.out.println("    Sientes una energia maligna creciendo...");
        }
        else if (contadorSuperNova == 9) {
            System.out.println("    EMPIEZAS A SUDAR FRIO");
        }
        else if (contadorSuperNova == 10) {
            esperar(1500);
            System.out.println("ggwp");
            esperar(2500);
            lanzarSuperNova(cloud);
        }
    }

    /**
     * Pausa la ejecución del hilo principal para sincronizar la aparición de textos en consola.
     * * @param ms Cantidad de milisegundos a esperar.
     */
    private void esperar(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Ejecuta la secuencia visual del ataque final y aplica un daño letal e inevitable al jugador.
     * * @param cloud Objeto Jugador que recibe el impacto.
     */
    public void lanzarSuperNova(Jugador cloud) {        
        String rojo = "\u001B[31m";
        String tachado = "\u001B[9m";
        String reset = "\u001B[0m";
        String limpiar = "\033[H\033[2J";
        System.out.print(limpiar);
        System.out.flush();

        System.out.println("            SEPHIROTH UTILIZA SUPERNOVA");
        esperar(2000);

        System.out.print(limpiar);
        System.out.flush();

        System.out.println("            TACTICAL NUKE INCOMING");
        esperar(2000);

        System.out.print(limpiar);
        System.out.flush();

        String[] ecuaciones = {
            "Φ = WUγ + RUρ + SUγUρ",
            "W = -SUγΦ",
            "AU = (GMek⁻²)¹/³",
            "n = πr²"
        };

        for (String ec : ecuaciones) {
            System.out.println(ec);
            esperar(1000);
        }

        System.out.print(limpiar);
        System.out.flush();
        esperar(2000);

        String[] planetas1 = {"PLUTON", "SATURNO", "JUPITER"};
        
        for (String p : planetas1) {
            System.out.print("Viajando a " + p + "... ");
            esperar(1500);
            System.out.println("\r\033[K" + rojo + tachado + p + reset);
            esperar(500);
        }

        System.out.print("VIAJANDO A EL SOL" + reset);
        esperar(3000);
        System.out.println("\r\033[K" + rojo + "SOL IMPACTADO!!!" + reset);
        esperar(1500);

        System.out.print(limpiar);
        System.out.flush();

        System.out.println("           EXPANSION DE SUPERNOVA\n");
        esperar(1500);
        System.out.print(limpiar);
        System.out.flush();

        String[] planetas2 = {"MERCURIO", "VENUS"};
        
        for (String p : planetas2) {
            System.out.print(p + reset);
            esperar(500);
            System.out.println("\r\033[K" + rojo + tachado + p + reset);
            esperar(800);
        }
        System.out.println("Cloud");
        esperar(2000);
        System.out.print(limpiar);
        System.out.flush();
        esperar(3000);
        System.out.println("Gomen, Amanai");
        esperar(1500);
        System.out.print(limpiar);
        System.out.flush();

        Random rng = new Random();
        String simbolos = "¡@#$%^&*()_+-=[]{}|;':,.<>/?αβγδεζηθικλμνξοπρστυφχψω" +
                        "█▓▒░▚▞▌▐▀▄0123456789ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ" +
                        "ᚠᚢᚦᚨᚱᚲᚷᚹᚺᚻᚼᚽᚾᚿᛀᛁᛂᛃᛄᛅᛆᛇᛈᛉᛊᛋᛌᛍᛎᛏᛐᛑᛒᛓᛔᛕᛖᛗᛘᛙᛚᛛᛜᛝᛞᛟᛠᛡᛢᛣᛤᛥᛦᛧᛨᛩᛪ";
        
        long tiempoFinal = System.currentTimeMillis() + 3000;

        while (System.currentTimeMillis() < tiempoFinal) {
            StringBuilder linea = new StringBuilder();

            int anchoLinea = rng.nextInt(80) + 40; 
            
            for (int i = 0; i < anchoLinea; i++) {
                linea.append(simbolos.charAt(rng.nextInt(simbolos.length())));
            }

            System.out.println(rojo + linea.toString() + reset);
            
            try { Thread.sleep(5); } catch (InterruptedException e) {}
        }

        System.out.print(limpiar);
        System.out.flush();
        esperar(200);

        cloud.recibirDMG(Integer.MAX_VALUE);
        esperar(3000);
        System.out.print(limpiar);
        System.out.flush();
    }
}

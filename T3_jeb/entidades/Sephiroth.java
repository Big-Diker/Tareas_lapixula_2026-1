package entidades;

import componentes.Estadisticas;

public class Sephiroth extends Enemigo{
    private int contadorSuperNova;

    public Sephiroth() {
        contadorSuperNova = 0;
        super("Sephiroth", 0, 0, 
        new Estadisticas(500, 500, 0, 0, 40, 0), 0.9f);
    }

    @Override
    public void atacar(Jugador cloud) {
        super.atacar(cloud);
        avanzarContadorSuperNova(cloud);
    }

    public void resetSuperNova() {
        contadorSuperNova = 0;
    }

    public void avanzarContadorSuperNova(Jugador cloud) {
        contadorSuperNova += 1;
        System.out.println("Contador de Super Nova -> " + contadorSuperNova);

        if (contadorSuperNova == 1) {
            System.out.println("   (No dejes que llegue a 10!! Recuerda que puedes reiniciarlo con tu Limit Breaker)");
        }
        else if (contadorSuperNova > 5 && contadorSuperNova < 8) {
            System.out.println("    Sientes una energia maligna creciendo...");
        }
        else if (contadorSuperNova < 10) {
            System.out.println("    EMPIEZAS A SUDAR FRIO");
        }
        else if (contadorSuperNova == 10) {
            System.out.println("ggwp");
            esperar(1000);
            lanzarSuperNova(cloud);
        }
    }

    private void esperar(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void lanzarSuperNova(Jugador cloud) {        
        String rojo = "\u001B[31m";
        String tachado = "\u001B[9m";
        String reset = "\u001B[0m";
        String limpiar = "\033[H\033[2J";
        System.out.print(limpiar);
        System.out.flush();
        esperar(2000);

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

        System.out.println("\n\n");
        esperar(2000);

        String[] planetas1 = {"PLUTON", "SATURNO", "JUPITER"};
        
        for (String p : planetas1) {
            System.out.print("Viajando a " + p + "... ");
            esperar(1500);
            System.out.println("\r\033[K" + rojo + tachado + p + reset);
            esperar(500);
        }

        System.out.println("VIAJANDO A EL SOL");
        esperar(3000);
        System.out.println(rojo + "SOL IMPACTADO!!!" + reset + System.lineSeparator() + "\n\n" +
        "           EXPANSION DE SUPERNOVA INICIADA\n");
        esperar(1000);

        String[] planetas2 = {"MERCURIO", "VENUS"};
        
        for (String p : planetas2) {
            System.out.println(rojo + tachado + p + reset);
            esperar(1000);
        }

        esperar(1500);
        System.err.println("            LA SUPERNOVA TE ALCANZA (FFAAAAH)\n");
        esperar(1000);
        cloud.recibirDMG(Integer.MAX_VALUE);
    }
}

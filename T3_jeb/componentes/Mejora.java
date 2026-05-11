package componentes;

import entidades.Jugador;

public class Mejora {
    private String nombre;
    private int costoChatarra;
    private TipoStat statAfectado;
    private int valorBono;

    /**
     * Constructor de la clase Mejora.
     * @param nombre Nombre de la mejora.
     * @param costoChatarra Valor en chatarra necesario para comprar la mejora.
     * @param statAfectado Tipo de estadística que será incrementada.
     * @param valorBono Cantidad de puntos que se sumarán a la estadística.
     */
    public Mejora(String nombre, int costoChatarra, TipoStat statAfectado, int valorBono) {
        this.nombre = nombre;
        this.costoChatarra = costoChatarra;
        this.statAfectado = statAfectado;
        this.valorBono = valorBono;
    }

    /**
     * Verifica si el jugador tiene suficiente chatarra y, de ser así, aplica el incremento a la estadística correspondiente.
     * Deduce el costo de la chatarra del inventario del jugador.
     * @param cloud Jugador al que se le aplica la mejora.
     */
    public void aplicarMejora(Jugador cloud) {
        if (cloud.getChatarra() >= this.costoChatarra) {
            cloud.setChatarra(cloud.getChatarra() - this.costoChatarra);
            
            switch (this.statAfectado) {
                case HP_MAX:
                    cloud.setHpMaximo(cloud.getHpMaximo() + valorBono); // Sube la statmax
                    cloud.setHpActual(cloud.getHpActual() + valorBono); // Cura lo que subio de la stat
                    System.out.println("Alimento obtenido! Obtienes +" + valorBono + " de HP Máx.");
                    break;
                case MP_MAX:
                    cloud.setMpMaximo(cloud.getMpMaximo() + valorBono);
                    cloud.setMpActual(cloud.getMpActual() + valorBono);
                    System.out.println("Alimento obtenido! Obtienes +" + valorBono + " de MP Máx.");
                    break;
                case FUERZA:
                    cloud.setFuerza(cloud.getFuerza() + valorBono);
                    System.out.println("Alimento obtenido! Obtienes +" + valorBono + " de Fuerza.");
                    break;
            }
        }
        else {
            System.err.println("Tai ultra pato feo sangre (Te falta chatarra).");
        }
    }

    public String getNombre() { return nombre; }
}
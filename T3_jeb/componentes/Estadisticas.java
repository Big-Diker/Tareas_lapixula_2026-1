package componentes;

public class Estadisticas {
    private int hpActual;
    private int hpMaximo;
    private int mpActual;
    private int mpMaximo;
    private int fuerza;
    private int magia;

    /**
     * Constructor de la clase Estadisticas.
     * Inicializa los valores actuales y máximos de vida y magia, además de la fuerza y magia base.
     * @param hpActual Puntos de vida actuales.
     * @param hpMaximo Puntos de vida máximos.
     * @param mpActual Puntos de magia actuales.
     * @param mpMaximo Puntos de magia máximos.
     * @param fuerza Poder de ataque físico.
     * @param magia Poder de ataque mágico.
     */
    public Estadisticas(int hpActual, int hpMaximo, int mpActual, int mpMaximo, int fuerza, int magia) {
        this.hpActual = hpActual;
        this.hpMaximo = hpMaximo;
        this.mpActual = mpActual;
        this.mpMaximo = mpMaximo;
        this.fuerza = fuerza;
        this.magia = magia;
    }

    /**
     * Reduce los puntos de vida actuales asegurando que no desciendan de 0.
     * @param valor Cantidad de daño recibido a restar.
     */
    public void recibirDMG(int valor) {
        this.hpActual -= valor;
        if (this.hpActual < 0) {
            this.hpActual = 0;
        }
    }

    public int getHpMaximo() { return hpMaximo; }
    public int getHpActual() { return hpActual; }
    public int getMpMaximo() { return mpMaximo; }
    public int getMpActual() { return mpActual; }
    public int getFuerza()   { return fuerza;   }
    public int getMagia()    {  return magia;   }


    public void setHpMaximo(int hpMaximo) { this.hpMaximo = hpMaximo; }
    public void setHpActual(int hpActual) { this.hpActual = hpActual; }
    public void setMpMaximo(int mpMaximo) { this.mpMaximo = mpMaximo; }
    public void setMpActual(int mpActual) { this.mpActual = mpActual; }
    public void setFuerza(int fuerza)     { this.fuerza = fuerza;     }
    public void setMagia(int magia)       { this.magia = magia;       }
}

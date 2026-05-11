package componentes;

public class Materia {
    private String nombre;
    private Elemento elemento;
    
    /**
     * Constructor de la clase Materia.
     * @param nombre Nombre descriptivo de la materia.
     * @param elemento Tipo de elemento asociado a la materia.
     */
    public Materia(String nombre, Elemento elemento) {
        this.nombre = nombre;
        this.elemento = elemento;
    }

    public String getNombre() { return nombre; }
    public Elemento getElemento() { return elemento; }
}

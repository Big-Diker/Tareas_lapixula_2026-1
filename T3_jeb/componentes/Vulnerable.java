package componentes;

public interface Vulnerable {
    /**
     * Evalúa la debilidad ante un elemento mágico.
     * @param elementoMagia Elemento del ataque a evaluar.
     * @return Multiplicador de daño correspondiente a la afinidad elemental.
     */
    public double evaluarDebilidad(Elemento elementoMagia);
}

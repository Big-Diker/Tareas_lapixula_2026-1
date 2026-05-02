import entidades.Sephiroth;
import entidades.Jugador;

public class Prueba {
    public static void main(String[] args) {

        Jugador cloud = new Jugador();
        Sephiroth seph = new Sephiroth();
        
        seph.lanzarSuperNova(cloud);

    }
}
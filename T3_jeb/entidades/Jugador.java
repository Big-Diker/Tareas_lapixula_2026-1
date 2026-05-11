package entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import componentes.Elemento;
import componentes.Estadisticas;
import componentes.Materia;

public class Jugador {
    public String nombre = "Cloud";
    private int nivel;
    private int xpActual;
    private int chatarra;
    private int limiteActual;
    private Estadisticas stats;
    private List<Materia> mochila;
    private Arma busterSword;

    /**
     * Constructor de la clase Jugador.
     * Inicializa a Cloud con nivel 1, estadísticas base, mochila vacía y su arma inicial.
     */
    public Jugador() {
        nivel = 1;
        xpActual = 0;
        chatarra = 0;
        limiteActual = 0;
        stats = new Estadisticas(200, 200, 50, 50, 15, 15);
        mochila = new ArrayList<>();
        busterSword = new Arma();
    }

    /**
     * Verifica si los puntos de vida actuales del jugador han llegado a cero o menos.
     * @return true si el jugador está muerto, false en caso contrario.
     */
    public boolean estaMuerto() {
        if (stats.getHpActual() <= 0) { return true; }
        else { return false; }
    }

    /**
     * Redirige el flujo del combate hacia el método de ataque específico según el tipo de daño seleccionado.
     * @param tipoDeDano Tipo de ataque a realizar ("FISICO", "MAGICO" o "LIMITE").
     * @param enemigo Objeto Enemigo que recibirá el ataque.
     * @param elemento Tipo de elemento a utilizar (aplica para ataques mágicos).
     */
    public void atacar(String tipoDeDano, Enemigo enemigo, Elemento elemento) {
        if (tipoDeDano.equals("FISICO")) {
            ataqueFisico(enemigo);
        }

        else if (tipoDeDano.equals("MAGICO")) {
            ataqueMagico(enemigo, elemento);
        } 

        else if (tipoDeDano.equals("LIMITE")) {
            ataqueLimite(enemigo);
        }
    }
    
    /**
     * Ejecuta un ataque físico contra un enemigo basándose en el daño del arma y carga la barra de límite.
     * @param enemigo Enemigo objetivo del ataque.
     */
    public void ataqueFisico(Enemigo enemigo) {
        int dano = busterSword.calcularDanoFisico();
        enemigo.recibirDMG(dano, Elemento.FISICO);
        cargarLimite(dano);
    }

    /**
     * Ejecuta un ataque mágico o una curación dependiendo del elemento seleccionado.
     * Si el elemento es CURA, restaura los puntos de vida del jugador en lugar de atacar.
     * @param enemigo Enemigo objetivo del ataque (ignorado si el elemento es CURA).
     * @param elemento Elemento mágico que determina el tipo de daño o curación.
     */
    public void ataqueMagico(Enemigo enemigo, Elemento elemento) {
        int dano = busterSword.calcularDanoMagico(elemento);
        // Me encantaria dejar este check aqui por encapsulacion, pero me consumiria el turno igualmente asi que lo manejo en ejecutarBucleCombate() en Zona
        /* if (costoMP > getMpActual()) {
            System.out.println("MP insuficiente.");
            return;
        }*/
        if (elemento == Elemento.CURA) {
            curarHp(dano);
            System.out.println("Cloud usa CURA y recupera " + dano + " HP.");
        }
        else {
            enemigo.recibirDMG(dano, elemento);
            System.out.println("Cloud lanza " + elemento + " e inflige " + dano + " de daño!");
            cargarLimite(dano);
        }
        restarMP(elemento);
    }

    /**
     * Ejecuta el ataque especial definitivo del jugador y vacía la barra de límite.
     * Reinicia el contador de Supernova si el enemigo objetivo es Sephiroth.
     * @param enemigo Enemigo objetivo del ataque.
     */
    public void ataqueLimite(Enemigo enemigo) {
        int dano = busterSword.calcularDanoLimite();
        enemigo.recibirDMG(dano, Elemento.LIMITE);
        setLimite(0);
        if (enemigo instanceof Sephiroth) { ((Sephiroth) enemigo).resetSuperNova(); }
    }

    /**
     * Aumenta la barra de límite del jugador en proporción al daño infligido, con un tope máximo de 100.
     * @param dano Cantidad de daño infligido en el turno.
     */
    public void cargarLimite(int dano) {
        setLimite(getLimiteActual() + (dano/5));
        if (limiteActual > 100) {
            setLimite(100);
        }
    }
    
    /**
     * Reduce los puntos de vida del jugador según el daño recibido y carga su límite pasivamente.
     * Incluye una validación especial para imprimir el daño letal de la Supernova.
     * @param dano Cantidad de daño recibido.
     */
    public void recibirDMG(int dano) {
        stats.setHpActual(stats.getHpActual() - dano);

        limiteActual += (dano / 2);
        if (limiteActual > 100) { limiteActual = 100; } // Overflow de limite

        if (dano == Integer.MAX_VALUE) { // Super Nova de Sephiroth
            System.out.println("CLOUD RECIBE " + dano + " PUNTOS DE DAÑO");
        }
    }

    /**
     * Despliega un menú interactivo en consola para visualizar, equipar y desequipar materias
     * entre la mochila y el arma del jugador, respetando el límite de ranuras.
     */
    @SuppressWarnings("resource")
    public void gestionarEquipo() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n\n        Equipamiento\n");
            System.out.println("1. Ver Materias en Mochila (" + mochila.size() + ")");
            System.out.println("2. Ver Materias Equipadas en " + busterSword.getNombre() + " (" + busterSword.getMateriasEquipadas().size() + ")");
            System.out.println("3. Equipar Materia");
            System.out.println("4. Desequipar Materia");
            System.out.println("0. Volver");

            int op = sc.nextInt();
            if (op == 0) break;

            switch (op) {
                case 1:
                    if (mochila.isEmpty()) System.out.println("Mochila vacía.");
                    else mochila.forEach(m -> System.out.println("- " + m.getNombre() + " (" + m.getElemento() + ")"));
                    break;
                case 2:
                    if (busterSword.getMateriasEquipadas().isEmpty()) System.out.println("No hay materias en el arma.");
                    else busterSword.getMateriasEquipadas().forEach(m -> System.out.println("- " + m.getNombre()));
                    break;
                case 3:
                    if (mochila.isEmpty()) {
                        System.out.println("No tienes nada que equipar.");
                    }
                    else if (busterSword.getMateriasEquipadas().size() >= 5) {
                        System.err.println("Teni la hoja toda tuneada ya (Limite de 5 materias");
                    }
                    else {
                        System.out.println("Selecciona materia para equipar:");
                        for (int i = 0; i < mochila.size(); i++) {
                            System.out.println((i + 1) + ". " + mochila.get(i).getNombre());
                        }
                        int sel = sc.nextInt() - 1;
                        if (sel >= 0 && sel < mochila.size()) {
                            Materia m = mochila.remove(sel);
                            busterSword.getMateriasEquipadas().add(m);
                            System.out.println("¡" + m.getNombre() + " equipada!");
                        }
                    }
                    break;
                case 4:
                    List<Materia> equipadas = busterSword.getMateriasEquipadas();
                    if (equipadas.isEmpty()) {
                        System.out.println("El arma no tiene materias.");
                    } else {
                        System.out.println("Selecciona materia para desequipar:");
                        for (int i = 0; i < equipadas.size(); i++) {
                            System.out.println((i + 1) + ". " + equipadas.get(i).getNombre());
                        }
                        int sel = sc.nextInt() - 1;
                        if (sel >= 0 && sel < equipadas.size()) {
                            Materia m = equipadas.remove(sel);
                            mochila.add(m);
                            System.out.println("¡" + m.getNombre() + " devuelta a la mochila!");
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Verifica si la experiencia actual supera el umbral necesario para subir de nivel.
     * Si es así, aumenta el nivel del jugador, mejora sus estadísticas base y se llama recursivamente.
     */
    public void lvlUp(){
        int umbralLvlUp = this.nivel * 10;
        
        if (this.xpActual >= umbralLvlUp) {
            this.xpActual -= umbralLvlUp; // "Cobra" la experiencia necesaria para subir de nivel
            this.nivel += 1;
            
            subirHp(10);
            subirMp(5);
            subirFuerza(4);
            subirMagia(6);;
            
            System.out.println(this.nombre + " ha subido a nivel " + this.nivel + "!" + System.lineSeparator() + 
            "   -MaxHP↗  10 (Nuevo -> " + stats.getHpMaximo() + ")" + System.lineSeparator() +
            "   -MaxMP↗  5  (Nuevo -> " + stats.getMpMaximo() + ")" + System.lineSeparator() +
            "   -Fuerza↗ 4  (Nuevo -> " + stats.getFuerza() + ")" + System.lineSeparator() +
            "   -Magia↗  6  (Nuevo -> " + stats.getMagia() + ")" + System.lineSeparator()
            );
        
            lvlUp(); // Recursividad por si es subida multiple de nivel
        }
    }

    /**
     * Código de desarrollador para subir automáticamente a nivel 20, maximizar estadísticas
     * y otorgar las materias esenciales para acceder directamente a la batalla del Núcleo.
     */
    public void cheatSubirNivel20() {
        this.nivel = 20;
        this.stats.setHpMaximo(1500);
        this.stats.setHpActual(1500);
        this.stats.setMpMaximo(300);
        this.stats.setMpActual(300);
        this.stats.setFuerza(80);
        this.stats.setMagia(100);
        
        this.mochila.add(new Materia("Materia de Fuego (Cheat)", Elemento.FUEGO));
        this.mochila.add(new Materia("Materia de Cura (Cheat)", Elemento.CURA));
        
        System.out.println("\n\nYou dirty cheater.");
    }



    public class Arma {
        public String nombre = "Buster Sword";
        private List<Materia> materiasEquipadas;

        /**
         * Constructor de la clase Arma.
         * Inicializa la lista interna de materias equipadas.
         */
        public Arma() {
            this.materiasEquipadas = new ArrayList<>();
        }

        /**
         * Calcula el daño mágico a infligir considerando la magia del jugador
         * y un multiplicador basado en las materias repetidas del mismo elemento.
         * @param elemento Elemento mágico a utilizar.
         * @return Daño mágico final calculado.
         */
        public int calcularDanoMagico(Elemento elemento) {
            int cantMaterias = getCantMaterias(elemento);
            int dano = stats.getMagia() * (int)(1.0 + (0.5 * cantMaterias));
            return dano;
        }

        /**
         * Calcula el daño físico estándar aplicando un multiplicador fijo a la fuerza base.
         * @return Daño físico final calculado.
         */
        public int calcularDanoFisico() {
            int danoFisico = (int)(stats.getFuerza() * 1.25);
            return danoFisico;
        }

        /**
         * Calcula el daño del ataque especial Límite aplicando un daño altamente superior.
         * @return Daño de límite calculado.
         */
        public int calcularDanoLimite() {
            int danoLimite = stats.getFuerza() * 5;
            return danoLimite;
        }

        /**
         * Retorna una lista de los elementos mágicos actualmente equipados, sin duplicados.
         * @param tipo Filtro de búsqueda (1 para magia ofensiva, 2 para magia defensiva/CURA).
         * @return Lista de elementos disponibles para usar según el tipo solicitado.
         */
        public List<Elemento> getElementosPorTipo(int tipo) {
            List<Elemento> disponibles = new ArrayList<>();
            for (Materia m : materiasEquipadas) {
                if (tipo == 1 && m.getElemento() != Elemento.CURA) {
                    if (!disponibles.contains(m.getElemento())) disponibles.add(m.getElemento());
                } else if (tipo == 2 && m.getElemento() == Elemento.CURA) {
                    if (!disponibles.contains(m.getElemento())) disponibles.add(m.getElemento());
                }
            }
            return disponibles;
        }

        /**
         * Cuenta cuántas materias de un elemento específico se encuentran equipadas en el arma.
         * @param elemento Elemento a buscar en las ranuras del arma.
         * @return Cantidad de materias de ese tipo encontradas.
         */
        public int getCantMaterias(Elemento elemento) {
            int n = 0;
            for (Materia m : materiasEquipadas) {
                if (m.getElemento() == elemento) n++;
            }
            return n;
        }

        public List<Materia> getMateriasEquipadas() { return materiasEquipadas; }
        public String getNombre() { return nombre; }
    }




    public int getNivel()    { return nivel;  }
    public int getHpActual() { return stats.getHpActual(); }
    public int getHpMaximo() { return stats.getHpMaximo(); }
    public int getMpActual() { return stats.getMpActual(); }
    public int getMpMaximo() { return stats.getMpMaximo(); }
    public int getChatarra() { return chatarra; }
    public int getLimiteActual() { return limiteActual; }
    public String getNombre(){ return nombre; }
    public List<Materia> getMochila() { return mochila; }
    public Arma getBusterSword() { return busterSword; }
    public int getFuerza() { return stats.getFuerza(); }

    /**
     * Calcula el costo de puntos de magia (MP) necesario para lanzar un hechizo.
     * Escala dependiendo de la cantidad de materias del mismo elemento equipadas.
     * @param elemento Elemento mágico a evaluar.
     * @return El costo total de lanzamiento en MP.
     */
    public int costoMP(Elemento elemento) { return (10 + (5*busterSword.getCantMaterias(elemento))); }

    /**
     * Limpia completamente la lista de materias de la mochila y reduce la chatarra a cero.
     * Se invoca como penalización cuando el jugador es derrotado en ciertas zonas de exploración.
     */
    public void vaciarMochilaYChatarra() {
    chatarra = 0;
    mochila.clear();
    }
    public void subirHp(int hp) {
        stats.setHpMaximo(stats.getHpMaximo() + hp);
        stats.setHpActual(stats.getHpActual() + hp);
    }
    public void curarHp(int hp) {
        int cura = getHpActual() + hp;
        stats.setHpActual(cura);
        if (getHpActual() > getHpMaximo()) { // Overflow de cura
            setHpActual(getHpMaximo());
        }
    }
    public void subirMp(int mp) {
        stats.setMpMaximo(stats.getMpMaximo() + mp);
        stats.setMpActual(stats.getMpActual() + mp);
    }
    public void restarMP(Elemento elemento) { 
        stats.setMpActual(stats.getMpActual() - costoMP(elemento)); 
    }
    public void subirFuerza(int fuerza) { stats.setFuerza(stats.getFuerza() + fuerza); }
    public void subirMagia(int magia)   { stats.setMagia(stats.getMagia() + magia);    }
    /**
     * Añade puntos de experiencia al jugador e invoca el chequeo de subida de nivel.
     * @param xp Puntos de experiencia obtenidos tras derrotar enemigos.
     */
    public void recibirXP(int xp) {
        this.xpActual += xp;
        lvlUp();
    }
    public void recibirChatarra(int chatarra) {
        setChatarra(getChatarra() + chatarra);
    }

    public void setChatarra(int chatarra) { this.chatarra = chatarra; }
    public void setLimite(int limiteActual) { this.limiteActual = limiteActual; }
    public void setHpActual(int hp) { stats.setHpActual(hp); }
    public void setMpActual(int mp) { stats.setMpActual(mp); }
    public void setHpMaximo(int hp) { stats.setHpMaximo(hp); }
    public void setMpMaximo(int mp) { stats.setMpMaximo(mp); }
    public void setFuerza(int fuerza) { stats.setFuerza(fuerza); }

}

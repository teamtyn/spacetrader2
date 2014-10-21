package spacetrader2.star_system;

/**
 * Wormhole, currently does not work, from one StarSystem to another
 * @author David Purcell
 */
public class Wormhole {
    private StarSystem system1;
    private StarSystem system2;
    
    public Wormhole(StarSystem system1, StarSystem system2) {
        this.system1 = system1;
        this.system2 = system2;
    }
}
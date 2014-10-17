package spacetrader.star_system;

/**
 * Wormhole, currently does not work, from one StarSystem to another
 * @author David Purcell
 */
public class Wormhole {
    private final StarSystem system1;
    private final StarSystem system2;

    public Wormhole(StarSystem system1, StarSystem system2) {
        this.system1 = system1;
        this.system2 = system2;
    }
}
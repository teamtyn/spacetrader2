package spacetrader.star_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Basically the standard set of solar system names, we currently have 125
 * @author Ryan Burns
 */
public class StarSystemNames {
    private final List<String> nameSet;

    public StarSystemNames() {
        nameSet = new ArrayList<>();
        nameSet.add("Acamar");
        nameSet.add("Adahn");
        nameSet.add("Aldea");
        nameSet.add("Andevian");
        nameSet.add("Antedi");
        nameSet.add("Balosnee");
        nameSet.add("Baratas");
        nameSet.add("Brax");
        nameSet.add("Bretel");
        nameSet.add("Burnsea");
        nameSet.add("Calondia");
        nameSet.add("Campor");
        nameSet.add("Capelle");
        nameSet.add("Carzon");
        nameSet.add("Castor");
        nameSet.add("Cestus");
        nameSet.add("Cheron");
        nameSet.add("Courteney");
        nameSet.add("Daled");
        nameSet.add("Damast");
        nameSet.add("Davlos");
        nameSet.add("Deneb");
        nameSet.add("Deneva");
        nameSet.add("Devidia");
        nameSet.add("Dotsonia");
        nameSet.add("Draylon");
        nameSet.add("Drema");
        nameSet.add("Endor");
        nameSet.add("Esmee");
        nameSet.add("Exo");
        nameSet.add("Ferris");
        nameSet.add("Festen");
        nameSet.add("Fourmi");
        nameSet.add("Frolix");
        nameSet.add("Gemulon");
        nameSet.add("Guinifer");
        nameSet.add("Hades");
        nameSet.add("Hamlet");
        nameSet.add("Helena");
        nameSet.add("Hulst");
        nameSet.add("Iodine");
        nameSet.add("Iralius");
        nameSet.add("Janus");
        nameSet.add("Japori");
        nameSet.add("Jarada");
        nameSet.add("Jason");
        nameSet.add("Kaylon");
        nameSet.add("Khefka");
        nameSet.add("Kira");
        nameSet.add("Klaatu");
        nameSet.add("Klaestron");
        nameSet.add("Korma");
        nameSet.add("Kravat");
        nameSet.add("Krios");
        nameSet.add("Kuceraville");
        nameSet.add("Laertes");
        nameSet.add("Largo");
        nameSet.add("Lave");
        nameSet.add("Ligon");
        nameSet.add("Lowry");
        nameSet.add("Magrat");
        nameSet.add("Malcoria");
        nameSet.add("Melina");
        nameSet.add("Mentar");
        nameSet.add("Merik");
        nameSet.add("Mintaka");
        nameSet.add("Montor");
        nameSet.add("Mordan");
        nameSet.add("Myrthe");
        nameSet.add("Nelvana");
        nameSet.add("New de Leon");
        nameSet.add("Nix");
        nameSet.add("Nyle");
        nameSet.add("Odet");
        nameSet.add("Og");
        nameSet.add("Omega");
        nameSet.add("Omphalos");
        nameSet.add("Orias");
        nameSet.add("Othello");
        nameSet.add("Parade");
        nameSet.add("Penthara");
        nameSet.add("Picard");
        nameSet.add("Pollux");
        nameSet.add("Purcelloria");
        nameSet.add("Quator");
        nameSet.add("Rakhar");
        nameSet.add("Ran");
        nameSet.add("Regulas");
        nameSet.add("Relva");
        nameSet.add("Rhymus");
        nameSet.add("Rochani");
        nameSet.add("Rubicum");
        nameSet.add("Rutia");
        nameSet.add("Sarpeidon");
        nameSet.add("Sefalla");
        nameSet.add("Seltrice");
        nameSet.add("Sigma");
        nameSet.add("Sol");
        nameSet.add("Somari");
        nameSet.add("Stakoron");
        nameSet.add("Styris");
        nameSet.add("Talani");
        nameSet.add("Tamus");
        nameSet.add("Tantalos");
        nameSet.add("Tanuga");
        nameSet.add("Tarchannen");
        nameSet.add("Terosa");
        nameSet.add("Thera");
        nameSet.add("Titan");
        nameSet.add("Torin");
        nameSet.add("Triacus");
        nameSet.add("Turkana");
        nameSet.add("Tyrus");
        nameSet.add("Umberlee");
        nameSet.add("Utopia");
        nameSet.add("Vadera");
        nameSet.add("Vagra");
        nameSet.add("Vandor");
        nameSet.add("Ventax");
        nameSet.add("Xenon");
        nameSet.add("Xerxes");
        nameSet.add("Yew");
        nameSet.add("Yojimbo");
        nameSet.add("Zalkon");
        nameSet.add("Zuul");
        
        Collections.shuffle(nameSet);
    }

    public String getName() {
        if (!nameSet.isEmpty()) {
            return nameSet.remove(0);
        } else {
            System.out.println("No more solar system names for now!");
            System.exit(1);
            return null;
        }
    }
}

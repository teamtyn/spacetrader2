package spacetrader.star_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Basically the standard set of solar system names, we currently have 125
 * @author Ryan Burns
 */
public class StarSystemNames {
    private static final List<String> nameList = new ArrayList<>();
    static {
        nameList.add("Acamar");
        nameList.add("Adahn");
        nameList.add("Aldea");
        nameList.add("Andevian");
        nameList.add("Antedi");
        nameList.add("Balosnee");
        nameList.add("Baratas");
        nameList.add("Brax");
        nameList.add("Bretel");
        nameList.add("Burnsea");
        nameList.add("Calondia");
        nameList.add("Campor");
        nameList.add("Capelle");
        nameList.add("Carzon");
        nameList.add("Castor");
        nameList.add("Cestus");
        nameList.add("Cheron");
        nameList.add("Courteney");
        nameList.add("Daled");
        nameList.add("Damast");
        nameList.add("Davlos");
        nameList.add("Deneb");
        nameList.add("Deneva");
        nameList.add("Devidia");
        nameList.add("Dotsonia");
        nameList.add("Draylon");
        nameList.add("Drema");
        nameList.add("Endor");
        nameList.add("Esmee");
        nameList.add("Exo");
        nameList.add("Ferris");
        nameList.add("Festen");
        nameList.add("Fourmi");
        nameList.add("Frolix");
        nameList.add("Gemulon");
        nameList.add("Guinifer");
        nameList.add("Hades");
        nameList.add("Hamlet");
        nameList.add("Helena");
        nameList.add("Hulst");
        nameList.add("Iodine");
        nameList.add("Iralius");
        nameList.add("Janus");
        nameList.add("Japori");
        nameList.add("Jarada");
        nameList.add("Jason");
        nameList.add("Kaylon");
        nameList.add("Khefka");
        nameList.add("Kira");
        nameList.add("Klaatu");
        nameList.add("Klaestron");
        nameList.add("Korma");
        nameList.add("Kravat");
        nameList.add("Krios");
        nameList.add("Kuceraville");
        nameList.add("Laertes");
        nameList.add("Largo");
        nameList.add("Lave");
        nameList.add("Ligon");
        nameList.add("Lowry");
        nameList.add("Magrat");
        nameList.add("Malcoria");
        nameList.add("Melina");
        nameList.add("Mentar");
        nameList.add("Merik");
        nameList.add("Mintaka");
        nameList.add("Montor");
        nameList.add("Mordan");
        nameList.add("Myrthe");
        nameList.add("Nelvana");
        nameList.add("New de Leon");
        nameList.add("Nix");
        nameList.add("Nyle");
        nameList.add("Odet");
        nameList.add("Og");
        nameList.add("Omega");
        nameList.add("Omphalos");
        nameList.add("Orias");
        nameList.add("Othello");
        nameList.add("Parade");
        nameList.add("Penthara");
        nameList.add("Picard");
        nameList.add("Pollux");
        nameList.add("Purcelloria");
        nameList.add("Quator");
        nameList.add("Rakhar");
        nameList.add("Ran");
        nameList.add("Regulas");
        nameList.add("Relva");
        nameList.add("Rhymus");
        nameList.add("Rochani");
        nameList.add("Rubicum");
        nameList.add("Rutia");
        nameList.add("Sarpeidon");
        nameList.add("Sefalla");
        nameList.add("Seltrice");
        nameList.add("Sigma");
        nameList.add("Sol");
        nameList.add("Somari");
        nameList.add("Stakoron");
        nameList.add("Styris");
        nameList.add("Talani");
        nameList.add("Tamus");
        nameList.add("Tantalos");
        nameList.add("Tanuga");
        nameList.add("Tarchannen");
        nameList.add("Terosa");
        nameList.add("Thera");
        nameList.add("Titan");
        nameList.add("Torin");
        nameList.add("Triacus");
        nameList.add("Turkana");
        nameList.add("Tyrus");
        nameList.add("Umberlee");
        nameList.add("Utopia");
        nameList.add("Vadera");
        nameList.add("Vagra");
        nameList.add("Vandor");
        nameList.add("Ventax");
        nameList.add("Xenon");
        nameList.add("Xerxes");
        nameList.add("Yew");
        nameList.add("Yojimbo");
        nameList.add("Zalkon");
        nameList.add("Zuul");
        
        Collections.shuffle(nameList);
    }

    public static String getName() {
        if (!nameList.isEmpty()) {
            return nameList.remove(0);
        } else {
            System.out.println("No more solar system names for now!");
            System.exit(1);
            return null;
        }
    }
}

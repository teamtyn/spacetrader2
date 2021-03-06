package spacetrader.star_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import spacetrader.GameModel;

/**
 * Generates planet names based on their original government.
 * @author Nico de Leon
 */
public class PlanetNames {
    private static final EnumMap<Government.Type, List<String>> nameLists =
        new EnumMap<>(Government.Type.class);

    static {
        List<String> anarchicNames = new ArrayList<>();
        for (int count = 0; count < 200; count++) {
            int nameInt = GameModel.getRandom().nextInt(308915776); // 26 ** 6
            char[] nameChars = Integer.toString(nameInt, 26).toCharArray();
            for (int i = 0; i < nameChars.length; i++) {
                if (nameChars[i] <= '9') {
                    nameChars[i] += 'a' - '0';
                } else {
                    nameChars[i] += 10;
                }
            }
            anarchicNames.add(new String(nameChars));
        }
        nameLists.put(Government.Type.ANARCHY, anarchicNames);

        List<String> capitalistCorporateNames = new ArrayList<>();
        capitalistCorporateNames.add("Abbott");
        capitalistCorporateNames.add("Actavis");
        capitalistCorporateNames.add("Aetna");
        capitalistCorporateNames.add("Agilent");
        capitalistCorporateNames.add("Alexion");
        capitalistCorporateNames.add("Allergan");
        capitalistCorporateNames.add("Amgen");
        capitalistCorporateNames.add("Bard");
        capitalistCorporateNames.add("Baxter");
        capitalistCorporateNames.add("Becton");
        capitalistCorporateNames.add("Bergen");
        capitalistCorporateNames.add("Boston");
        capitalistCorporateNames.add("Bristol");
        capitalistCorporateNames.add("Cardinal");
        capitalistCorporateNames.add("Celgene");
        capitalistCorporateNames.add("Cerner");
        capitalistCorporateNames.add("Cigna");
        capitalistCorporateNames.add("Covidien");
        capitalistCorporateNames.add("DaVita");
        capitalistCorporateNames.add("Edwards");
        capitalistCorporateNames.add("Gilead");
        capitalistCorporateNames.add("Hospira");
        capitalistCorporateNames.add("Humana");
        capitalistCorporateNames.add("Johnson");
        capitalistCorporateNames.add("Lilly");
        capitalistCorporateNames.add("McKesson");
        capitalistCorporateNames.add("Merck");
        capitalistCorporateNames.add("Mylan");
        capitalistCorporateNames.add("Patterson");
        capitalistCorporateNames.add("Perkin");
        capitalistCorporateNames.add("Perrigo");
        capitalistCorporateNames.add("Pfizer");
        capitalistCorporateNames.add("Quest");
        capitalistCorporateNames.add("Stryker");
        capitalistCorporateNames.add("Tenet");
        capitalistCorporateNames.add("Varian");
        capitalistCorporateNames.add("Vertex");
        capitalistCorporateNames.add("Waters");
        capitalistCorporateNames.add("Zimmer");
        capitalistCorporateNames.add("Zoetis");
        Collections.shuffle(capitalistCorporateNames, GameModel.getRandom());
        nameLists.put(Government.Type.CAPITALIST, capitalistCorporateNames);
        nameLists.put(Government.Type.CORPORATE, capitalistCorporateNames);
    }

    public static String getName(Government government) {
        Government.Type governmentType = government.getType();
        while (nameLists.get(governmentType) == null || nameLists.get(governmentType).isEmpty()) {
            governmentType = Government.Type.values()[GameModel.getRandom().nextInt(Government.Type.values().length)];
        }
        return nameLists.get(governmentType).remove(0);
    }
}

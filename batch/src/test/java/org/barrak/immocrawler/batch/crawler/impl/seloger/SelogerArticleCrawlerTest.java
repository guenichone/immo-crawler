package org.barrak.immocrawler.batch.crawler.impl.seloger;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.barrak.immocrawler.batch.utils.ParserUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class SelogerArticleCrawlerTest {

    private SelogerArticleCrawler crawler = new SelogerArticleCrawler();

    @Test
    public void search() {
        SearchResultDocument article = new SearchResultDocument();
        article.setUrl("https://www.seloger.com/annonces/achat/maison/audun-le-roman-54/136317365.htm?enterprise=0&LISTING-LISTpg=5&natures=1,2,4&places=%7bci%3a570041%7d&price=100000%2f500000&projects=2&proximity=0,10&qsversion=1.0&types=2,4&bd=ListToDetail");
        article.setCity("kanfen");
        article.setTitle("Maison 6 pièces à Kanfen");

        crawler.updateDetails(article);

        assertThat(article).isNotNull();

        assertThat(article.getImageUrls()).hasSize(4);
        assertThat(article.getDescription()).isEqualTo("AUDUN LE ROMAN A 10 mn A PROXIMITÉ DES ÉCOLES, COLLÈGE, COMMERCES Pavillon neuf LABEL BBC 6 pièces de 115 m² habitable Maison de 1/2 niveau 90 m² + 25 m² de combles aménageables  4 chambres, cuisine ouverte sur le salon séjour de 50 m², salle de bain avec douche à l'italienne 120X120 et baignoire, 1/2 sous-sol 50 m² avec 2 places de garage, buanderie, 2 places de parking. Chauffage au gaz au sol Sur un terrain d'environ 5 ares. A proximité: THIONVILLE, LONGWY, BEUVILLERS, AUMETZ, BOULANGE, AUDUN LE TICHE, LANDRES, TRIEUX, FONTOY Possibilitée de financement total sans apport  Prêt à taux 0% Pas d'acompte à la réservation Frais de notaire réduit  Prix: 179900euros Pour plus de renseignements ou RDV TEL.");
    }

    @Test
    public void testRegEx() {
        final String regex = "Object\\.defineProperty\\( ConfigDetail, '(?<key>.*)', \\{\\n +value: \"(?<value>.*)\"";
        final String string = "\n" +
                "    /**\n" +
                "     * Module contenant les infos de l'annonce\n" +
                "     *\n" +
                "     * Il est recommandé de setter de nouvelles propriétés via Object.defineProperty() en enumerable : true\n" +
                "     *\n" +
                "     * writable et configurable sont par défaut à false, toutes les props initiales de l'Object sont ainsi rendues immuables\n" +
                "     *\n" +
                "     * @global\n" +
                "     * @type {Object}\n" +
                "     */\n" +
                "    (function(){\n" +
                "        \"use strict\";\n" +
                "\n" +
                "        window.ConfigDetail = {};\n" +
                "\n" +
                "        /* IdSelector : id de la div main de la page détail */\n" +
                "        Object.defineProperty( ConfigDetail, 'idSelector', {\n" +
                "          value: \"annonce-134279313-30103870\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* idAnnonce : id de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'idAnnonce', {\n" +
                "          value: \"134279313\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* idAgence : id de l'agence */\n" +
                "        Object.defineProperty( ConfigDetail, 'idAgence', {\n" +
                "          value: \"103870\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* idTiers : id tiers de l'agence */\n" +
                "        Object.defineProperty( ConfigDetail, 'idTiers', {\n" +
                "          value: \"71901\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* dpeC : note en chiffre du DPE */\n" +
                "        Object.defineProperty( ConfigDetail, 'dpeC', {\n" +
                "          value: \"51\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* dpeL : note en chiffre du DPE */\n" +
                "        Object.defineProperty( ConfigDetail, 'dpeL', {\n" +
                "          value: \"B\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* codeInsee : Code INSEE de la commune */\n" +
                "        Object.defineProperty( ConfigDetail, 'codeInsee', {\n" +
                "          value: \"540029\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* etage : a quel etage */\n" +
                "        Object.defineProperty( ConfigDetail, 'etage', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* nbChambres : nombre de chambres */\n" +
                "        Object.defineProperty( ConfigDetail, 'nbChambres', {\n" +
                "          value: \"4\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* nbPhotos : nombre de photos */\n" +
                "        Object.defineProperty( ConfigDetail, 'nbPhotos', {\n" +
                "          value: \"5\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* nbPieces : nombre de pieces */\n" +
                "        Object.defineProperty( ConfigDetail, 'nbPieces', {\n" +
                "          value: \"6\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* surfaceT : surface du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'surfaceT', {\n" +
                "          value: \"115\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* cp : Code postal de la ville */\n" +
                "        Object.defineProperty( ConfigDetail, 'cp', {\n" +
                "          value: \"54560\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* prix : Prix de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'prix', {\n" +
                "          value: \"179900\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* prodVisibilite : tag de visibilité du produit */\n" +
                "        Object.defineProperty( ConfigDetail, 'prodVisibilite', {\n" +
                "          value: \"AD:AC:AN:BC:AD:AC:AN:BC:AW\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* couplage : si annonce CMI ou Louer-Vite */\n" +
                "        Object.defineProperty( ConfigDetail, 'couplage', {\n" +
                "          value: \"CMI\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* typeTransaction : Type transaction de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'typeTransaction', {\n" +
                "          value: \"vente\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* idTypeChauffage : Type de chauffage de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'idTypeChauffage', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* idTypeCuisine : Type de cuisine de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'idTypeCuisine', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* balcon : Nombre de balcons de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'balcon', {\n" +
                "          value: \"0\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* eau : Nombre de salle d'eau de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'eau', {\n" +
                "          value: \"0\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* bain : Nombre de salle de bains de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'bain', {\n" +
                "          value: \"0\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* mensualite : Mensualité de l'annonce achat */\n" +
                "        Object.defineProperty( ConfigDetail, 'mensualite', {\n" +
                "          value: \"742\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "\n" +
                "        /* idPublication : idPublication de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'idPublication', {\n" +
                "          value: \"30103870\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* idTT : id Type Transaction de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'idTT', {\n" +
                "          value: \"2\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* idTypeBien : id Type Bien de l'annonce */\n" +
                "        Object.defineProperty( ConfigDetail, 'idTypeBien', {\n" +
                "          value: \"2\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* typeBien : Type Bien de l'annonce' */\n" +
                "        Object.defineProperty( ConfigDetail, 'typeBien', {\n" +
                "          value: \"Projet de construction\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* telNumber : Numéro tel du contact de l'annonce' */\n" +
                "        Object.defineProperty( ConfigDetail, 'telNumber', {\n" +
                "          value: \"06 11 30 24 69\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* objectMail : objet du mail */\n" +
                "        Object.defineProperty( ConfigDetail, 'objectMail', {\n" +
                "          value: \"Vente projet de construction 115m² Audun le Roman\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* introMail {string} : texte intro du mail */\n" +
                "        Object.defineProperty( ConfigDetail, 'introMail', {\n" +
                "          value: \"Bonjour, \\n\\nVoici une annonce intéressante que je viens de découvrir sur SeLoger :\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* nbRoom {string} : nombre de chambres du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'nbRoomText', {\n" +
                "          value: \"6 pièces\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* surface {string} : surface du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'surface', {\n" +
                "          value: \"115 m²\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* ville {string} : ville du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'ville', {\n" +
                "          value: \"Audun le Roman\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* idQuartier {string} : code du quartier du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'idQuartier', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* nomQuartier {string} : ville du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'nomQuartier', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* zipcode {string} : zipcode du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'zipcode', {\n" +
                "          value: \"54560\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* price {string} : zipcode du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'price', {\n" +
                "          value: \"179 900\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        // TODO : add raw price\n" +
                "        /* price {string} : zipcode du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'rawPrice', {\n" +
                "          value: \"179900\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        /* descriptionBien {string} : description texte  du bien */\n" +
                "        Object.defineProperty( ConfigDetail, 'descriptionBien', {\n" +
                "          value: \"AUDUN LE ROMAN A 10 mn\\r\\nA PROXIMITÉ DES ÉCOLES, COLLÈGE, COMMERCES\\r\\nPavillon neuf LABEL BBC 6 pièces de 115 m² habitable\\r\\nMaison de 1/2 niveau 90 m² + 25 m² de combles aménageables \\r\\n4 chambres, cuisine ouverte sur le salon séjour de 50 m², salle de bain avec douche à l\\'italienne 120X120 et baignoire, 1/2 sous-sol 50 m² avec 2 places de garage, buanderie, 2 places de parking.\\r\\nChauffage au gaz au sol\\r\\nSur un terrain d\\'environ 5 ares.\\r\\nA proximité: THIONVILLE, LONGWY, BEUVILLERS, AUMETZ, BOULANGE, AUDUN LE TICHE, LANDRES, TRIEUX, FONTOY\\r\\nPossibilitée de financement total sans apport \\r\\nPrêt à taux 0%\\r\\nPas d\\'acompte à la réservation\\r\\nFrais de notaire réduit \\r\\nPrix: 179900euros\\r\\nPour plus de renseignements ou RDV TEL.\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "\n" +
                "        /* TODO : à retirer : temp pour les besoins de la nouvelle map en attente du webservice */\n" +
                "        Object.defineProperty( ConfigDetail, 'mapModeAffichage', {\n" +
                "          value: \"Polygon\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "        Object.defineProperty( ConfigDetail, 'mapBoundingboxNortheastLatitude', {\n" +
                "          value: \"49.38379\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "        Object.defineProperty( ConfigDetail, 'mapBoundingboxNortheastLongitude', {\n" +
                "          value: \"5.91986\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "        Object.defineProperty( ConfigDetail, 'mapBoundingboxSouthwestLatitude', {\n" +
                "          value: \"49.34405\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "        Object.defineProperty( ConfigDetail, 'mapBoundingboxSouthwestLongitude', {\n" +
                "          value: \"5.86785\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "        Object.defineProperty( ConfigDetail, 'mapCoordonneesLatitude', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "        Object.defineProperty( ConfigDetail, 'mapCoordonneesLongitude', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "        Object.defineProperty( ConfigDetail, 'mapPoly', {\n" +
                "          value: \"POLYGON ((5.8678493102143978 49.37254072387158, 5.8678494113446833 49.372539191538863, 5.87060963319258 49.3705290695155, 5.8756894914907294 49.369379089889129, 5.8787791259286442 49.366219463660222, 5.8803388258351035 49.361620347309916, 5.8785390498446795 49.360199688056959, 5.8824784410157775 49.357419876478147, 5.8816693018182571 49.356630716175559, 5.8816693460192457 49.356629243801933, 5.8847396039204813 49.354249081495368, 5.8896994967789693 49.354318992721829, 5.8906387313278268 49.353030042735895, 5.8847590348692567 49.3460102629067, 5.8880200018974787 49.3440490004228, 5.8963308388244089 49.3468694552928, 5.8979509973225905 49.351270075324976, 5.8959613026146336 49.357049289131247, 5.8984106768380329 49.357649263624225, 5.8980312083394084 49.358969417108959, 5.9183506796005281 49.367349266435141, 5.9179211574624979 49.369069492950153, 5.9198609969706553 49.370130074753419, 5.9189608716091744 49.371960491026258, 5.9178506998123366 49.373930713950919, 5.913420185120561 49.3763109829333, 5.9113497747052293 49.374441144746584, 5.9083305025397186 49.375530881607389, 5.8990306362309495 49.383200771515305, 5.89754997012297 49.383790999899063, 5.8941199478492763 49.382181079786236, 5.8920397831587676 49.382920975911667, 5.8818194424737671 49.3797708307744, 5.8818191732962362 49.379769437499938, 5.8827582588238236 49.378390780696328, 5.8741296226162172 49.377260926354992, 5.8678493102143978 49.37254072387158))\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "        Object.defineProperty( ConfigDetail, 'mapLevel', {\n" +
                "          value: \"Argent\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'urlRetourRecherche', {\n" +
                "          value: '//www.seloger.com/recherche.htm?enterprise=0&natures=1,2,4&places=[{ci:540149}]&projects=2,5&proximity=0,10&qsversion=1.0&types=1,2&bd=DetailToList_SL',\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'logoAgence', {\n" +
                "          value: \"https://v.seloger.com/s/width/105/logos/0/g/u/r/0gurd1y6iw1tjei2613gib3nqyvkagwi21j1s7h2c.jpg\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'nomAgence', {\n" +
                "          value: \"PAVILLONS STILL LORRAINE\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'urlAgence', {\n" +
                "          value: '#?bd=Contact_EmConfirm_agence',\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'idTypeNatureBien', {\n" +
                "          value: \"4\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'omnitureProduct', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'positionAva', {\n" +
                "          value: \"\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'requete', {\n" +
                "          value: \"bd=ListToDetail&enterprise=0&natures=1,2,4&places=%5b%7bci%3a540149%7d%5d&projects=2,5&proximity=0,10&qsversion=1.0&types=1,2\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'urlAnnonce', {\n" +
                "          value: 'https://www.seloger.com/annonces/achat/maison/audun-le-roman-54/134279313.htm?enterprise=0&natures=1,2,4&places=%5b%7bci%3a540149%7d%5d&projects=2,5&proximity=0,10&qsversion=1.0&types=1,2#?bd=Contact_EmConfirm_ann',\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'urlPhoto', {\n" +
                "          value: 'https://v.seloger.com/s/width/400/visuels/1/i/9/o/1i9o8b9bsc54nugkcdlpvkky9bg794k6dth1mmsog.jpg',\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'isLV', {\n" +
                "          value: \"False\",\n" +
                "          enumerable: true,\n" +
                "        })\n" +
                "\n" +
                "        Object.defineProperty( ConfigDetail, 'isCMI', {\n" +
                "          value: \"1\",\n" +
                "          enumerable: true\n" +
                "        });\n" +
                "\n" +
                "        \n" +
                "\n" +
                "    }())";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            System.out.println("Group key: " + matcher.group("key"));
            System.out.println("Group value: " + matcher.group("value"));
        }

        List<Map<String, String>> result = ParserUtils.matchesByRegexGroup(string, regex, "key", "value");

        assertThat(result).isNotEmpty();
    }
}

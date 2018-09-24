package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.barrak.crawler.database.document.SearchResultDetailsDocument;
import org.barrak.crawler.database.document.SearchResultDocument;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImmoRegionArticleCrawlerTest {

    private ImmoRegionArticleCrawler crawler = new ImmoRegionArticleCrawler();

    @Test
    public void testKenfen() {

        String url = "https://www.immoregion.fr/vente/maison/kanfen/id-5995430.html";

        SearchResultDocument article = new SearchResultDocument();
        article.setUrl(url);
        article.setCity("kanfen");
        article.setTitle("Maison 6 pièces à Kanfen");

        SearchResultDetailsDocument details = crawler.getDetails(article);

        assertThat(details).isNotNull();
        assertThat(details.getUrl()).isEqualTo(url);
        assertThat(details.getCity()).isEqualTo("kanfen");
        assertThat(details.getPrice()).isEqualTo(330000);
        assertThat(details.getDescription()).isEqualTo("Proche de la frontière Luxembourgeoise situé sur la commune d'HETTANGE SOETRICH nouvelle construction de 124m² habitables.Salon séjour,cuisine. " +
                "3 ou 4 chambres au choix.Garage avec cellier.Chauffage gaz ,chauffage au sol dans la partie jour.Volets motorisés dans toutes les pièces,porte de garage sectionnelle.Carrelages au sol dans l'entrée ,salon séjour,cuisine,wc,salle de bains,parquets dans les chambres,2wc,douche à l'italienne.Raccordements compris aux différents réseaux " +
                "Pas d'acompte à verser à la réservation.Possibilité de financement en totalité.Possibilité de faire réaliser une étude de financement par notre partenaire financier gratuitement et sans engagement. " +
                "Nous nous occupons de toutes vos démarches administratives tout au long de la procédure de votre dossier. " +
                "Frais de notaire réduit. " +
                "Pour plus de renseignements ou RDV tél 06 03 87 30 70");
        assertThat(details.getLandSurface()).isEqualTo(3.5);
        assertThat(details.getHomeSurface()).isEqualTo(124);
        assertThat(details.getNbRooms()).isEqualTo(6);
    }

    @Test
    public void testSerrouville() {

        String url = "https://www.immoregion.fr/vente/maison/serrouville/id-5943181.html";

        SearchResultDocument article = new SearchResultDocument();
        article.setUrl(url);
        article.setCity("serrouville");
        article.setTitle("Maison mitoyenne 5 pièces à Serrouville");

        SearchResultDetailsDocument details = crawler.getDetails(article);

        assertThat(details).isNotNull();
        assertThat(details.getUrl()).isEqualTo(url);
        assertThat(details.getCity()).isEqualTo("serrouville");
        assertThat(details.getPrice()).isEqualTo(148000);
        assertThat(details.getDescription()).isEqualTo("A VOIR ! Maison mitoyenne rénovée en partie, d'environ 100m². " +
                "Composée: " +
                "d’une jolie entrée, " +
                "d'un grand espace de vie de 52m², comprenant une grande cuisine équipée avec îlot central, idéal pour les petits-déjeuners en famille et d' un salon séjour avec accès sur la terrasse et le jardin, " +
                "d’une salle de bain avec douche italienne, " +
                "d'un wc séparé. " +
                "Au premier étage, d'une grande mezzanine de 16m² pouvant servir d'espace bureau ou espace détente, " +
                "d'une chambre de 14m² avec son dressing de 4m². " +
                "Au deuxième étage, de 2 chambres de 10 et 15,8m², et d'un petit bureau. " +
                "D’un grand garage motorisé, isolé, de 54m2. " +
                "Et d’un jardin sur l’arrière de la maison. " +
                "Des travaux ont été effectués dans la maison : toiture, isolation, chauffage, électricité, sanitaires et carrelages rez-de-chaussée. " +
                "Quelques travaux de finitions sont à prévoir. " +
                "Chauffage au fioul. " +
                "Double vitrage sur toute la maison, avec volets roulants. " +
                "A 5 minutes de toutes commodités commerces, écoles et services médicaux. " +
                "A 15 minutes du Luxembourg (Esch/Alzette) et 15 minutes de Thionville. " +
                "PROCHE ACCÈS AUTOROUTIERS (5min). " +
                "FRAIS D’AGENCE A CHARGE VENDEUR.");
        assertThat(details.getLandSurface()).isEqualTo(-1);
        assertThat(details.getHomeSurface()).isEqualTo(100);
        assertThat(details.getNbRooms()).isEqualTo(5);
    }

}
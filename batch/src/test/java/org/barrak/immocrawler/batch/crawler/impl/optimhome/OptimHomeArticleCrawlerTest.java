package org.barrak.immocrawler.batch.crawler.impl.optimhome;

import org.barrak.immocrawler.database.document.SearchResultDocument;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OptimHomeArticleCrawlerTest {

    private OptimHomeArticleCrawler crawler = new OptimHomeArticleCrawler();

    @Test
    public void testAlgrange() {

        String url = "https://www.optimhome.com/annonces/vente/algrange/maison_de_caractre/1177266-vente-algrange";

        SearchResultDocument article = new SearchResultDocument();
        article.setUrl(url);
        article.setCity("algrange");
        article.setTitle("Maison de caractère ALGRANGE (57)");
        article.setPrice(353600);
        article.setNbRooms(-1);
        article.setHomeSurface(-1);

        crawler.updateDetails(article);

        assertThat(article).isNotNull();
        assertThat(article.getUrl()).isEqualTo(url);
        assertThat(article.getCity()).isEqualTo("algrange");
        assertThat(article.getPrice()).isEqualTo(353600);

        assertThat(article.getDescription()).isEqualTo("Cette maison individuelle est cadastrée sur un terrain de 11 ares avec la particularité d'avoir un appartement de près de 83m² en rez de chaussée ( avec entrée séparée ) . Il était loué environ 650€ par mois et actuellement libre d'occupation. La maison est composée de 3 chambres ( 12m², 18m² et 10.5m²) et une pièce de 18m² peut faire une 4eme chambre ou un coin buro ou Cosi . Le salon séjour de près de 39m² est équipée d'une cheminée avec insert. La cuisine toute équipée de 14m² donne accès à une véranda de 28m² avec une vue remarquable. Un accès vers les combles aménageables d'environ 50m² est déjà crée. Au niveau des extérieurs une terrasse de plus de 50 m² entièrement carrelée, est équipée d'un barbecue et d'un jacuzzi et donne accès sur un magnifique sauna . Les honoraires sont à la charge du vendeur. Contactez Christian DILLMANN, Agent commercial OptimHome (RSAC N°820531465 Greffe de THIONVILLE) 06 58 25 30 33 http://www.dillmann.optimhome.com (réf. 830035814201 / 403959 )");

        assertThat(article.getLandSurface()).isEqualTo(11);
        assertThat(article.getHomeSurface()).isEqualTo(261);
        assertThat(article.getNbRooms()).isEqualTo(10);
        assertThat(article.getInternalReference()).isEqualTo("830035814201");
        assertThat(article.getExternalReference()).isEqualTo("403959");
    }
}

package org.barrak.immocrawler.batch.crawler.impl.leboncoin;

import org.barrak.immocrawler.database.model.ArticleDocument;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LeboncoinArticleCrawlerTest {

    private LeboncoinArticleCrawler crawler = new LeboncoinArticleCrawler();

    @Test
    public void testAlgrange() {

        String url = "https://www.leboncoin.fr/ventes_immobilieres/1503402023.htm/";

        ArticleDocument article = new ArticleDocument();
        article.setUrl(url);
        article.setCity("herserange");
        article.setTitle("Maison Herserange");
        article.setPrice(353600);
        article.setNbRooms(13);
        article.setHomeSurface(360);

        crawler.updateDetails(article);

        assertThat(article).isNotNull();
        assertThat(article.getUrl()).isEqualTo(url);
        assertThat(article.getCity()).isEqualTo("herserange");
        assertThat(article.getPrice()).isEqualTo(353600);

        assertThat(article.getDescription()).isEqualTo("Maison de construction traditionnelle de 1962, sur trois niveaux et avec petit jardin en étages. 3 grands garages, un cellier, salle de bains (douche à l'italienne) rénovée en 2017, cuisine, salle à manger / salon, 3 chambres + 1 chambre mansardée et un grand grenier sous combles.");

        assertThat(article.getLandSurface()).isEqualTo(-1);
        assertThat(article.getHomeSurface()).isEqualTo(360);
        assertThat(article.getNbRooms()).isEqualTo(13);
        assertThat(article.getInternalReference()).isNull();
        assertThat(article.getExternalReference()).isNull();

        assertThat(article.getImageUrls()).contains("https://img7.leboncoin.fr/ad-image/89f674c4dcea43d6c57cdd7106c1f3d412c11dfb.jpg");
    }
}

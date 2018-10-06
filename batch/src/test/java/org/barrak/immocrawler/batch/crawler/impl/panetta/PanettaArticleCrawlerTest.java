package org.barrak.immocrawler.batch.crawler.impl.panetta;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PanettaArticleCrawlerTest {

    private PanettaArticleCrawler crawler = new PanettaArticleCrawler();

    @Test
    public void getDetails() {
        SearchResultDocument article = new SearchResultDocument();
        article.setUrl("http://www.panetta-immobilier.fr/neuf/maison/meurthe-et-moselle/briey/mont-saint-martin/6036985");
        article.setCity("kanfen");
        article.setTitle("Maison 6 pièces à Kanfen");

        crawler.updateDetails(article);

        assertThat(article).isNotNull();
        assertThat(article.getDescription()).isEqualToIgnoringNewLines("Dans un petit lotissement, proche de la frontière, beau pavillon individuel T7 sur 4 ares. " +
                "RDC : Sas d'entrée extérieur avec sbaie coulissante, Grand hall d’entrée, WC indépendants,, accès garage 2 voitures sortant sur la terrasse arrière et le jardin, cuisine entièrement équipée séparée du séjour-salon traversant donnant sur deux terrasses dont une couverte sur l'arrière et grade cour sur l'avant. " +
                "1er étage : Halle de nuit desservant quatre chambres et un bureau, salle de bain avec douche, vasque, WC indépendants. " +
                "Combles non aménageables au dessus " +
                "Un grand garage avec belle hauteur sous plafond (camping-car ou camionnette) " +
                "Prise TV et internet dans chaque chambre " +
                "Double accès à la maison avant et arrière " +
                "DV PVC OB Volets motorisés dans toute la maison. " +
                "CC GAZ de ville " +
                "Sous/sol d'environ 100 m²");
    }
}
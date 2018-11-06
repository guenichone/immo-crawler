package org.barrak.immocrawler.utils;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.RealEstateTypeEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;

import java.util.Random;

public class ArticleDocumentFactory {

    private Random random;
    private RandomStringUtilsWrapper randomStringUtils;
    private RandomEnumUtils randomEnumUtils;

    public ArticleDocumentFactory() {
        this(new Random());
    }

    public ArticleDocumentFactory(long seed) {
        this(new Random(seed));
    }

    public ArticleDocumentFactory(Random random) {
        this.random = random;
        this.randomStringUtils = new RandomStringUtilsWrapper(random);
        this.randomEnumUtils = new RandomEnumUtils(random);
    }

    public ArticleDocument createArticleDocumentWithPrice(String id, int price) {
        ArticleDocument res = createArticleDocument(id);
        res.setPrice(price);
        return res;
    }

    public ArticleDocument createArticleDocumentWithLandSurface(String id, int landSurface) {
        ArticleDocument res = createArticleDocument(id);
        res.setLandSurface(landSurface);
        return res;
    }

    public ArticleDocument createArticleDocumentWithLandAndHomeSurface(String id, int landSurface, int homeSurface) {
        ArticleDocument res = createArticleDocument(id);
        res.setLandSurface(landSurface);
        res.setHomeSurface(homeSurface);
        return res;
    }

    public ArticleDocument createArticleDocument(String id) {
        String url = "www." + randomStringUtils.randomAlphabetic(15) + ".com";
        ProviderEnum providerEnum = randomEnumUtils.randomEnum(ProviderEnum.class);
        RealEstateTypeEnum realEstateTypeEnum = randomEnumUtils.randomEnum(RealEstateTypeEnum.class);
        ArticleDocument res = new ArticleDocument(id, url, providerEnum, realEstateTypeEnum, randomStringUtils.randomAlphabetic(10), 100000 + random.nextInt(100000));

        return res;
    }
}

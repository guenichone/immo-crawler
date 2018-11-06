package org.barrak.immocrawler.utils;

import org.barrak.immocrawler.database.model.UserDocument;

import java.util.Random;

public class UserDocumentFactory {

    private Random random;
    private RandomStringUtilsWrapper randomStringUtils;

    public UserDocumentFactory() {
        this(new Random());
    }

    public UserDocumentFactory(long seed) {
        this(new Random(seed));
    }

    public UserDocumentFactory(Random random) {
        this.random = random;
        this.randomStringUtils = new RandomStringUtilsWrapper(random);
    }

    public UserDocument createUser(String email) {
        String password = randomStringUtils.randomAlphanumeric(8);
        String firstName = randomStringUtils.randomAlphabetic(6);
        String lastName = randomStringUtils.randomAlphabetic(6);
        return createUser(email, password, firstName, lastName);
    }

    public UserDocument createUser(String email, String password, String firstName, String lastName) {
        UserDocument res = new UserDocument(email, password, firstName, lastName);
        return res;
    }
}

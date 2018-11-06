package org.barrak.immocrawler.database.repository;

import org.barrak.immocrawler.database.config.TestConfig;
import org.barrak.immocrawler.database.model.UserDocument;
import org.barrak.immocrawler.utils.UserDocumentFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {UserRepository.class, TestConfig.class})
@EnableAutoConfiguration
@DataMongoTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDocumentFactory userDocumentFactory;

    @Test
    public void findByEmail() {
        UserDocument res = userRepository.findByEmail("test1@crawler.com");
        assertThat(res).isNotNull();
        assertThat(res.getEmail()).isEqualTo("test1@crawler.com");
        assertThat(res.getPassword()).isEqualTo("pwd");
        assertThat(res.getFirstName()).isEqualTo("bob");
        assertThat(res.getLastName()).isEqualTo("john");
    }

    @Test
    public void findByInvalidEmail() {
        UserDocument res = userRepository.findByEmail("test1@crawler.commmmm");
        assertThat(res).isNull();
    }

    @BeforeAll
    public void createUserDocumentData() {
        // String id, String url, ProviderEnum provider, RealEstateTypeEnum realEstateTypeEnum, String city, int price
        UserDocument userDocument1 = userDocumentFactory.createUser("test1@crawler.com", "pwd", "bob", "john");
        UserDocument userDocument2 = userDocumentFactory.createUser("test2@crawler.com");
        UserDocument userDocument3 = userDocumentFactory.createUser("test3@crawler.com");

        userRepository.saveAll(Arrays.asList(userDocument1, userDocument2, userDocument3));
    }
}

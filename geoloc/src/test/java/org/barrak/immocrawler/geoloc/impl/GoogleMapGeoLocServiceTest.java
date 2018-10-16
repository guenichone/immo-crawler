package org.barrak.immocrawler.geoloc.impl;

import org.barrak.immocrawler.geoloc.model.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = GoogleMapGeoLocService.class)
@RunWith(SpringRunner.class)
public class GoogleMapGeoLocServiceTest {

    @Autowired
    private GoogleMapGeoLocService googleMapGeoLocService;

    @Test
    public void getLocationCrusnes() {
        Location result = googleMapGeoLocService.getLocation("crusnes france");

        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("Crusnes");
        assertThat(result.getPostalCode()).isEqualTo("54680");
        assertThat(result.getLat()).isEqualTo(49.4337502);
        assertThat(result.getLng()).isEqualTo(5.9197426);
    }

    @Test
    public void getLocationAumetz() {
        Location result = googleMapGeoLocService.getLocation("aumetz");

        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("Aumetz");
        assertThat(result.getPostalCode()).isEqualTo("57710");
        assertThat(result.getLat()).isEqualTo(49.41791500000001);
        assertThat(result.getLng()).isEqualTo(5.9423784);
    }

    @Test
    public void getBrehainLaVille() {
        Location result = googleMapGeoLocService.getLocation("brehain-la-ville");

        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("Bréhain-la-Ville");
        assertThat(result.getPostalCode()).isEqualTo("54190");
        assertThat(result.getLat()).isEqualTo(49.4402742);
        assertThat(result.getLng()).isEqualTo(5.8809391);
    }
}
package org.barrak.immocrawler.batch.config;

import org.barrak.immocrawler.geoloc.impl.GoogleMapGeoLocService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = GoogleMapGeoLocService.class)
public class GeoLocConfig {

}

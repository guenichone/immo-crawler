package org.barrak.immocrawler.geoloc.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LocationType;
import org.barrak.immocrawler.geoloc.IGeoLocService;
import org.barrak.immocrawler.geoloc.exceptions.NoSuchLocationException;
import org.barrak.immocrawler.geoloc.exceptions.TooManyLocationException;
import org.barrak.immocrawler.geoloc.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapGeoLocService implements IGeoLocService {

    @Value("${google.api.key}")
    private String apiKey;

    @Override
    public Location getLocation(String search) {
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            GeocodingResult[] results = GeocodingApi.geocode(context, search).await();

            if (results.length == 0) {
                throw new NoSuchLocationException("There is no places corresponding to : " + search);
            } else if (results.length > 1){
                throw new TooManyLocationException("There is too many places corresponding to : " + search);
            }

            GeocodingResult geoResult = results[0];
            String city = geoResult.addressComponents[0].shortName;

            // Redo the request to get postal code
            results = GeocodingApi.newRequest(context).latlng(geoResult.geometry.location).await();
            geoResult = results[0];

            double lat = geoResult.geometry.location.lat;
            double lng = geoResult.geometry.location.lng;

            AddressComponent[] addresses = geoResult.addressComponents;

            return new Location(city, addresses[addresses.length - 1].shortName, lat, lng);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}

package org.barrak.immocrawler.geoloc.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import org.barrak.immocrawler.geoloc.IGeoLocService;
import org.barrak.immocrawler.geoloc.exceptions.NoSuchLocationException;
import org.barrak.immocrawler.geoloc.exceptions.TooManyLocationException;
import org.barrak.immocrawler.geoloc.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GoogleMapGeoLocService implements IGeoLocService {

    @Value("${google.api.key}")
    private String apiKey;

    // TODO: Review me
    private Map<String, Location> cache = new HashMap<>();

    @Override
    public Location getLocation(String search) {
        if (cache.containsKey(search)) {
            return cache.get(search);
        }

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

            double lat = geoResult.geometry.location.lat;
            double lng = geoResult.geometry.location.lng;

            String postalCode = getPostalCode(context, geoResult);

            Location result = new Location(city, postalCode, lat, lng);
            cache.put(search, result);
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private String getPostalCode(GeoApiContext context, GeocodingResult result) throws InterruptedException, ApiException, IOException {
        Optional<String> postalCode = getPostalCodeFromAddressComponent(result.addressComponents);

        if (postalCode.isPresent()) {
            return postalCode.get();
        } else {
            // Redo the request to get postal code from geometry
            GeocodingResult[] results = GeocodingApi.newRequest(context).latlng(result.geometry.location).await();
            GeocodingResult geoResult = results[0];

            postalCode = getPostalCodeFromAddressComponent(geoResult.addressComponents);
            if (postalCode.isPresent()) {
                return postalCode.get();
            } else {
                throw new NoSuchLocationException("Unable to find a valid postal code from this address.");
            }
        }
    }

    private Optional<String> getPostalCodeFromAddressComponent(AddressComponent[] addresses) {
        return Arrays.stream(addresses)
                .filter(component -> Arrays.stream(component.types)
                        .anyMatch(type -> type == AddressComponentType.POSTAL_CODE))
                .map(component -> component.shortName)
                .findFirst();
    }
}

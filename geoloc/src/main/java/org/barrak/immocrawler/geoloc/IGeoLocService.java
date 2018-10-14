package org.barrak.immocrawler.geoloc;

import com.google.maps.errors.ApiException;
import org.barrak.immocrawler.geoloc.model.Location;

import java.io.IOException;

public interface IGeoLocService {

    Location getLocation(String search) throws InterruptedException, ApiException, IOException;
}

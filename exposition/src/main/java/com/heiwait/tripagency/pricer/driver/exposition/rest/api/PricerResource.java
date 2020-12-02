package com.heiwait.tripagency.pricer.driver.exposition.rest.api;

import com.heiwait.tripagency.pricer.domain.Destination;
import com.heiwait.tripagency.pricer.domain.TravelClass;
import com.heiwait.tripagency.pricer.domain.Trip;
import com.heiwait.tripagency.pricer.domain.TripPricer;
import com.heiwait.tripagency.pricer.driven.repository.jdbctemplate.TripRepositoryJdbcTemplateAdapter;
import com.heiwait.tripagency.pricer.driven.repository.mock.TripRepositoryMockAdapter;
import com.heiwait.tripagency.pricer.driven.repository.springdata.TripRepositoryJpaAdapter;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pricer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PricerResource {

    private final TripPricer tripPricer = new TripPricer();
    private final TripRepositoryMockAdapter tripRepositoryMockAdapter;
    private final TripRepositoryJpaAdapter tripRepositoryJpaAdapter;
    private final TripRepositoryJdbcTemplateAdapter tripRepositoryJdbcTemplateAdapter;


    public PricerResource(
            final TripRepositoryMockAdapter tripRepositoryMockAdapter,
            final TripRepositoryJpaAdapter tripRepositoryJpaAdapter,
            final TripRepositoryJdbcTemplateAdapter tripRepositoryJdbcTemplateAdapter) {
        this.tripRepositoryMockAdapter = tripRepositoryMockAdapter;
        this.tripRepositoryJpaAdapter = tripRepositoryJpaAdapter;
        this.tripRepositoryJdbcTemplateAdapter = tripRepositoryJdbcTemplateAdapter;
    }

    @ApiOperation(value = "Compute travel fees", notes = "Returns the price of a trip")
    @GetMapping(value = {"/{destination}/travelClass/{travelClass}/priceTripWithHardCodedValues"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> priceTripWithHardCodedValues(
            @PathVariable(value = "destination") String destinationName,
            @PathVariable(value = "travelClass") TravelClass travelClass) {

        Destination destination = new Destination(destinationName);
        checkDestination(destination);
        Trip trip = tripRepositoryMockAdapter.findTripByDestination(destination);
        Integer travelPrice = tripPricer.priceTrip(travelClass, trip);
        return new ResponseEntity<>(travelPrice, HttpStatus.OK);
    }

    @ApiOperation(value = "Compute travel fees", notes = "Returns the price of a trip")
    @GetMapping(value = {"/{destination}/travelClass/{travelClass}/priceTripWithJPA"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> priceTripWithJPA(
            @PathVariable(value = "destination") String destinationName,
            @PathVariable(value = "travelClass") TravelClass travelClass) {

        Destination destination = new Destination(destinationName);
        checkDestination(destination);
        Trip trip = tripRepositoryJpaAdapter.findTripByDestination(destination);
        Integer travelPrice = tripPricer.priceTrip(travelClass, trip);
        return new ResponseEntity<>(travelPrice, HttpStatus.OK);
    }

    @ApiOperation(value = "Compute travel fees", notes = "Returns the price of a trip")
    @GetMapping(value = {"/{destination}/travelClass/{travelClass}/priceTripWithJdbcTemplate"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> priceTripWithJdbcTemplate(
            @PathVariable(value = "destination") String destinationName,
            @PathVariable(value = "travelClass") TravelClass travelClass) {

        Destination destination = new Destination(destinationName);
        checkDestination(destination);
        Trip trip = tripRepositoryJdbcTemplateAdapter.findTripByDestination(destination);
        Integer travelPrice = tripPricer.priceTrip(travelClass, trip);
        return new ResponseEntity<>(travelPrice, HttpStatus.OK);
    }

    private void checkDestination(final Destination destination) {
        if (destination == null)
            throw new IllegalArgumentException();
    }
}
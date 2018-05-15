package com.heiwait.tripagency.infrastructure.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TripJpaRepository extends JpaRepository<TripEntity, String> {

    @Query("select trip from TripEntity trip where trip.destination = :destination")
    TripEntity findTripByDestination(@Param("destination") final String destination);
}

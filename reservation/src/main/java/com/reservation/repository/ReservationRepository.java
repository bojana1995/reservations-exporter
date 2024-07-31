package com.reservation.repository;

import com.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link Reservation} entities.
 * Extends the {@link JpaRepository} to provide CRUD operations and custom query methods.
 *
 * @author Bojana Samardzic
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Finds reservations by asset ID and market ID.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @return a list of reservations matching the criteria
     */
    List<Reservation> findByAssetIdAndMarketId(UUID assetId, UUID marketId);

    /**
     * Finds reservations by asset ID, market ID and a timestamp range.
     *
     * @param assetId  the unique identifier of the asset
     * @param marketId the unique identifier of the market
     * @param from     the start of the timestamp range
     * @param to       the end of the timestamp range
     * @return a list of reservations matching the criteria
     */
    List<Reservation> findByAssetIdAndMarketIdAndTimestampBetween(UUID assetId, UUID marketId, LocalDateTime from, LocalDateTime to);
}

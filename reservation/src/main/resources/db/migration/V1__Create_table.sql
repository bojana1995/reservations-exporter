-- Create the 'reservation' table if it does not already exist.
-- This table stores reservation details including timestamps, asset IDs, market IDs, bid IDs, and values.

CREATE TABLE IF NOT EXISTS reservation (
    id SERIAL PRIMARY KEY,                              -- Unique identifier for each reservation (auto-incremented)
    timestamp TIMESTAMPTZ NOT NULL,                     -- Timestamp of the reservation (with timezone)
    asset_id UUID NOT NULL,                             -- Unique identifier for the asset related to the reservation
    market_id UUID NOT NULL,                            -- Unique identifier for the market related to the reservation
    positive_bid_id UUID,                               -- Unique identifier for the positive bid (nullable)
    negative_bid_id UUID,                               -- Unique identifier for the negative bid (nullable)
    positive_value DOUBLE PRECISION NOT NULL,           -- Value of positive bids (in a floating-point format)
    positive_capacity_price DOUBLE PRECISION NOT NULL,  -- Price of positive capacity (in a floating-point format)
    positive_energy_price DOUBLE PRECISION NOT NULL,    -- Price of positive energy (in a floating-point format)
    negative_value DOUBLE PRECISION NOT NULL,           -- Value of negative bids (in a floating-point format)
    negative_capacity_price DOUBLE PRECISION NOT NULL,  -- Price of negative capacity (in a floating-point format)
    negative_energy_price DOUBLE PRECISION NOT NULL,    -- Price of negative energy (in a floating-point format)
    updated_at TIMESTAMPTZ NOT NULL                     -- Timestamp of the last update (with timezone)
);

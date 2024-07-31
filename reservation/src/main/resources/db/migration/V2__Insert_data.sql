-- Insert a new reservation record into the 'reservation' table
-- The record includes various attributes such as timestamps, asset IDs, market IDs, bid IDs, and values.

-- Insert statement 1
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1024, '2022-10-24T14:15:22Z', '9179b887-04ef-4ce5-ab3a-b5bbd39ea3c8', '8a5075bf-2552-4119-b135-61ddcfd37ba2',
        '8a5075bf-2552-4119-b135-61ddcfd37ba2', '8a5075bf-2552-4119-b135-61ddcfd37ba2',
        200000, 1.5, 1.5, 250000, 2.0, 2.0, '2022-10-10T11:42:12.794363Z');

-- Insert statement 2
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1045, '2024-05-15T12:00:00Z', 'a1b2c3d4-e5f6-7890-ab12-cd34ef567890', 'b2c3d4e5-f6d7-8901-ab23-cd45ef678901',
        'b2c3d4e5-f6d7-8901-ab23-cd45ef678901', 'b2c3d4e5-f6d7-8901-ab23-cd45ef678901',
        300000, 2.0, 1.8, 350000, 2.5, 2.3, '2024-05-10T12:00:00.654321Z');

-- Insert statement 3
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1051, '2024-06-15T11:30:00Z', '21ac2fdb-0387-446d-ab99-ce75ab9af971', '8689cd2c-a1dc-4704-937b-71446e7b823a',
        '8689cd2c-a1dc-4704-937b-71446e7b823a', '8689cd2c-a1dc-4704-937b-71446e7b823a',
        270000, 1.9, 1.7, 330000, 2.4, 2.3, '2024-06-10T11:30:00.654321Z');

-- Insert statement 4
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1052, '2024-07-01T12:45:00Z', '64693fe8-0176-4c61-a5e6-bfa5c2e257ee', '58a97cff-adb7-41a9-8e50-b1eebde90511',
        '58a97cff-adb7-41a9-8e50-b1eebde90511', '58a97cff-adb7-41a9-8e50-b1eebde90511',
        290000, 2.2, 2.0, 340000, 2.7, 2.5, '2024-06-25T12:45:00.123456Z');

-- Insert statement 5
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1053, '2024-07-15T13:00:00Z', '66bd5ed1-4aa7-4097-8f0d-1f390ecb3820', '22f748c3-dd77-4a91-821c-571434be0973',
        '22f748c3-dd77-4a91-821c-571434be0973', '22f748c3-dd77-4a91-821c-571434be0973',
        310000, 2.3, 2.1, 360000, 2.8, 2.6, '2024-07-10T13:00:00.987654Z');

-- Insert statement 6
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1054, '2024-08-01T14:15:00Z', 'ee995ed7-d7ae-4c6d-a3f6-f47a4754bf61', 'f523ad47-98b2-45cd-a459-6106f3b8c187',
        'f523ad47-98b2-45cd-a459-6106f3b8c187', 'f523ad47-98b2-45cd-a459-6106f3b8c187',
        320000, 2.4, 2.2, 370000, 2.9, 2.7, '2024-07-25T14:15:00.654321Z');

-- Insert statement 7
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1055, '2024-08-15T15:30:00Z', '3179b029-3146-4e7d-9c2c-b479d0f1d0d9', 'd04b656c-67d7-434e-9139-af4c21b64a2a',
        'd04b656c-67d7-434e-9139-af4c21b64a2a', 'd04b656c-67d7-434e-9139-af4c21b64a2a',
        330000, 2.5, 2.3, 380000, 3.0, 2.8, '2024-08-10T15:30:00.987654Z');

-- Insert statement 8
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1056, '2024-09-01T16:45:00Z', '1431e25b-0fe4-4700-93c7-517aead54697', '35b5de81-d622-4d51-8867-3983522aa4e1',
        '35b5de81-d622-4d51-8867-3983522aa4e1', '35b5de81-d622-4d51-8867-3983522aa4e1',
        340000, 2.6, 2.4, 390000, 3.1, 2.9, '2024-08-25T16:45:00.123456Z');

-- Insert statement 9
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1057, '2024-09-15T17:00:00Z', '9fc44971-5b7f-4550-8f0e-af976b0d9af2', '67f107c8-c4fd-4e86-bc40-b0f038995b6e',
        '67f107c8-c4fd-4e86-bc40-b0f038995b6e', '67f107c8-c4fd-4e86-bc40-b0f038995b6e',
        350000, 2.7, 2.5, 400000, 3.2, 3.0, '2024-09-10T17:00:00.654321Z');

-- Insert statement 10
INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
                         positive_value, positive_capacity_price, positive_energy_price,
                         negative_value, negative_capacity_price, negative_energy_price, updated_at)
VALUES (1058, '2024-10-01T18:15:00Z', '54531a7b-0ca1-4cce-8283-b5408f1a362c', '2fd8eaa5-2828-4ee9-a191-3ea62fd9533b',
        '2fd8eaa5-2828-4ee9-a191-3ea62fd9533b', '2fd8eaa5-2828-4ee9-a191-3ea62fd9533b',
        360000, 2.8, 2.6, 410000, 3.3, 3.1, '2024-09-25T18:15:00.987654Z');

-- The following is commented out, but it would add another reservation record
-- INSERT INTO reservation (id, timestamp, asset_id, market_id, positive_bid_id, negative_bid_id,
--                          positive_value, positive_capacity_price, positive_energy_price,
--                          negative_value, negative_capacity_price, negative_energy_price, updated_at)
-- VALUES (1059, '2022-09-24T14:15:22Z', '9179b887-04ef-4ce5-ab3a-b5bbd39ea3c8', '8a5075bf-2552-4119-b135-61ddcfd37ba2',
--         '8a5075bf-2552-4119-b135-61ddcfd37ba2', '8a5075bf-2552-4119-b135-61ddcfd37ba2',
--         100000, 1.5, 1.5, 150000, 2.0, 2.0, '2022-09-10T11:42:12.794363Z');
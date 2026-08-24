INSERT OR IGNORE INTO locations (locationId, name, area, type, latitude, longitude) VALUES
  ('1001', 'UGCS', 'University of Ghana', 'Building', 5.037037, -0.091091),
  ('1002', 'Balme Library', 'University of Ghana', 'Building', 5.037074, -0.091182);

INSERT OR IGNORE INTO roads (fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) VALUES
  ('1001', '1002', 0.30, 180, 1.0);

INSERT OR IGNORE INTO service_requests (requestId, source, destination, category, urgency, timeSubmitted, deadline, status) VALUES
  ('CLN0001', '1001', '1001', 'Cleaning', 4, '2026-08-01T09:03:00', '2026-08-02T10:00', 'open');

INSERT OR IGNORE INTO resources (resourceId, type, homeLocation, capacity, availabilityStatus) VALUES
  ('RESC001', 'Cleaner', '1002', 1, 'available');

INSERT OR IGNORE INTO algorithm_runs (algorithmName, inputSize, timeNs, memoryKb, dateRun) VALUES
  ('BTree_insert_demo', 10, 125000, 512, '2026-08-13T10:00:00');

INSERT OR IGNORE INTO audit_events (eventType, description, relatedEntityId, performedBy, eventTime) VALUES
  ('SCHEMA_INIT', 'Database schema created and sample data inserted', NULL, 'Member2', '2026-08-13T10:00:00');

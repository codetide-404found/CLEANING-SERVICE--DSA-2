PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS locations (
    locationId   TEXT PRIMARY KEY,
    name         TEXT NOT NULL,
    area         TEXT NOT NULL,
    type         TEXT NOT NULL,
    latitude     REAL NOT NULL,
    longitude    REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS roads (
    roadId              INTEGER PRIMARY KEY AUTOINCREMENT,
    fromLocationId      TEXT NOT NULL,
    toLocationId        TEXT NOT NULL,
    distance            REAL NOT NULL CHECK (distance >= 0),
    travelTime          REAL NOT NULL CHECK (travelTime >= 0),
    roadConditionWeight REAL NOT NULL CHECK (roadConditionWeight >= 0),
    FOREIGN KEY (fromLocationId) REFERENCES locations(locationId) ON DELETE CASCADE,
    FOREIGN KEY (toLocationId)   REFERENCES locations(locationId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS service_requests (
    requestId     TEXT PRIMARY KEY,
    source        TEXT NOT NULL,
    destination   TEXT NOT NULL,
    category      TEXT NOT NULL,
    urgency       INTEGER NOT NULL CHECK (urgency BETWEEN 1 AND 5),
    timeSubmitted TEXT NOT NULL,
    deadline      TEXT,
    status        TEXT NOT NULL DEFAULT 'open' CHECK (status IN ('open','pending','in_progress','completed','closed','cancelled')),
    FOREIGN KEY (source)      REFERENCES locations(locationId) ON DELETE RESTRICT,
    FOREIGN KEY (destination) REFERENCES locations(locationId) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS resources (
    resourceId          TEXT PRIMARY KEY,
    type                TEXT NOT NULL,
    homeLocation        TEXT NOT NULL,
    capacity            INTEGER NOT NULL CHECK (capacity >= 0),
    availabilityStatus  TEXT NOT NULL DEFAULT 'available' CHECK (availabilityStatus IN ('available','busy','unavailable','offline')),
    FOREIGN KEY (homeLocation) REFERENCES locations(locationId) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS algorithm_runs (
    runId         INTEGER PRIMARY KEY AUTOINCREMENT,
    algorithmName TEXT NOT NULL,
    inputSize     INTEGER NOT NULL CHECK (inputSize >= 0),
    timeNs        INTEGER NOT NULL CHECK (timeNs >= 0),
    memoryKb      INTEGER NOT NULL CHECK (memoryKb >= 0),
    dateRun       TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS audit_events (
    eventId         INTEGER PRIMARY KEY AUTOINCREMENT,
    eventType       TEXT NOT NULL,
    description     TEXT NOT NULL,
    relatedEntityId TEXT,
    performedBy     TEXT,
    eventTime       TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_roads_from ON roads(fromLocationId);
CREATE INDEX IF NOT EXISTS idx_roads_to ON roads(toLocationId);
CREATE INDEX IF NOT EXISTS idx_requests_status ON service_requests(status);
CREATE INDEX IF NOT EXISTS idx_requests_urgency ON service_requests(urgency);
CREATE INDEX IF NOT EXISTS idx_resources_home ON resources(homeLocation);

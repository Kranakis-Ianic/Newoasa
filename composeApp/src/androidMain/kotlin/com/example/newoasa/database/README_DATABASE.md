# Transit Database Documentation

## Overview

This Room database stores transit line and station data for the Athens public transport system, including:
- **Buses** (OASA buses)
- **Trolleys** (Electric trolleybuses)
- **Metro** (Athens Metro lines 1, 2, 3)
- **Tram** (Athens Tram lines)
- **Suburban Railway** (Proastiakos - future support)

## Database Schema

### Tables

#### 1. `transit_lines`
Stores information about transit lines.

**Columns:**
- `id` (PRIMARY KEY, AUTO INCREMENT)
- `lineNumber` (TEXT) - Line identifier (e.g., "022", "1", "T6")
- `category` (TEXT) - Type: "bus", "trolley", "metro", "tram", "suburban"
- `displayName` (TEXT) - Display name (e.g., "Bus 022")
- `routeCount` (INTEGER) - Number of routes
- `routeIds` (TEXT) - Comma-separated route IDs
- `color` (TEXT) - Hex color code
- `isActive` (BOOLEAN) - Whether line is operational
- `description` (TEXT, NULLABLE)
- `createdAt` (INTEGER) - Timestamp
- `updatedAt` (INTEGER) - Timestamp

**Indexes:**
- Unique index on (`lineNumber`, `category`)
- Index on `category`

#### 2. `stations`
Stores station/stop information with type-specific fields.

**Columns:**
- `id` (PRIMARY KEY, AUTO INCREMENT)
- `lineId` (INTEGER, FOREIGN KEY) - References `transit_lines.id`
- `stationType` (TEXT) - Type: "bus_stop", "trolley_stop", "metro_station", "tram_stop", "suburban_station"
- `name` (TEXT) - Station name
- `stopCode` (TEXT, UNIQUE) - OASA stop code or station code
- `orderOnLine` (INTEGER) - Sequence number on the line
- `latitude` (REAL)
- `longitude` (REAL)

**Bus/Trolley Specific:**
- `address` (TEXT, NULLABLE) - Street address
- `direction` (TEXT, NULLABLE) - Direction description

**Metro/Suburban Specific:**
- `hasAccessibility` (BOOLEAN) - Wheelchair accessible
- `hasElevators` (BOOLEAN)
- `hasEscalators` (BOOLEAN)
- `hasParking` (BOOLEAN)
- `hasBikeParking` (BOOLEAN)
- `ticketZone` (INTEGER, NULLABLE) - Fare zone
- `platforms` (TEXT, NULLABLE) - Platform numbers
- `depthMeters` (INTEGER, NULLABLE) - Station depth
- `levelType` (TEXT, NULLABLE) - "underground", "elevated", "ground"

**Tram Specific:**
- `platformType` (TEXT, NULLABLE) - "island", "side", "both"
- `hasShelter` (BOOLEAN)

**Common:**
- `connections` (TEXT, NULLABLE) - Connected lines
- `isTransferStation` (BOOLEAN)
- `openingYear` (INTEGER, NULLABLE)
- `notes` (TEXT, NULLABLE)
- `isActive` (BOOLEAN)
- `createdAt` (INTEGER)
- `updatedAt` (INTEGER)

**Indexes:**
- Index on `stationType`
- Index on `lineId`
- Unique index on `stopCode`
- Composite index on (`latitude`, `longitude`)

## Usage Examples

### 1. Initialize Database

```kotlin
// In your Application class or MainActivity
import com.example.newoasa.database.DatabaseModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseModule.initialize(this)
    }
}
```

### 2. Migrate Data from GeoJSON

```kotlin
import com.example.newoasa.database.migration.DataMigrationHelper

val migrationHelper = DataMigrationHelper(context)

// Check if migration is needed
if (migrationHelper.needsMigration()) {
    lifecycleScope.launch {
        migrationHelper.migrateAllData()
    }
}
```

### 3. Access DAOs

```kotlin
import com.example.newoasa.database.DatabaseModule

val transitLineDao = DatabaseModule.provideTransitLineDao()
val stationDao = DatabaseModule.provideStationDao()

// Get all metro lines
val metroLines = transitLineDao.getMetroLines()

// Get stations for a specific line
val stations = stationDao.getStationsByLine(lineId)
```

### 4. Use Repository

```kotlin
import com.example.newoasa.database.repository.TransitDataRepository
import com.example.newoasa.database.DatabaseModule

val repository = TransitDataRepository(
    DatabaseModule.provideTransitLineDao(),
    DatabaseModule.provideStationDao()
)

// Observe metro lines with Flow
repository.getMetroLines().collect { lines ->
    // Update UI
}

// Get statistics
val stats = repository.getStatistics()
println("Total lines: ${stats.totalLines}")
println("Total stations: ${stats.totalStations}")
```

### 5. Query Examples

```kotlin
// Search lines
val results = transitLineDao.searchLines("022")

// Get transfer stations
val transfers = stationDao.getTransferStations()

// Get accessible metro stations
val accessibleStations = stationDao.getAccessibleStations()

// Get stations in a geographic area
val nearbyStations = stationDao.getStationsInBounds(
    minLat = 37.97,
    maxLat = 37.99,
    minLng = 23.72,
    maxLng = 23.74
)
```

## Migration Guide

### Adding New Tables

1. Create new Entity class
2. Add to `@Database` annotation in `TransitDatabase.kt`
3. Increment database version
4. Create migration strategy

### Version Management

Current version: **1**

For production, replace `fallbackToDestructiveMigration()` with proper migrations:

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add migration SQL here
    }
}

Room.databaseBuilder(...)
    .addMigrations(MIGRATION_1_2)
    .build()
```

## Performance Tips

1. **Use Flows** for reactive UI updates
2. **Batch inserts** when migrating data
3. **Index frequently queried columns**
4. **Use transactions** for multiple operations
5. **Limit query results** with pagination

## Testing

```kotlin
// Clear database for testing
DatabaseModule.clearAllData()

// Or use in-memory database
val testDb = Room.inMemoryDatabaseBuilder(
    context,
    TransitDatabase::class.java
).build()
```

## Future Enhancements

- [ ] Add suburban railway data
- [ ] Real-time arrival information
- [ ] Route planning tables
- [ ] Favorites/bookmarks
- [ ] Offline schedule caching
- [ ] User preferences

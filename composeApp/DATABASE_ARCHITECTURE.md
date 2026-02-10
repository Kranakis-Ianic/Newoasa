# Database Architecture

## Overview

The New OASA app uses a multiplatform database architecture with expect/actual pattern:

- **commonMain**: Platform-agnostic interfaces and models
- **androidMain**: Room database implementation
- **iosMain**: Stub implementation (TODO: SQLDelight or native)

## Folder Structure

```
composeApp/src/
├── commonMain/kotlin/com/example/newoasa/database/
│   ├── ITransitDatabase.kt          # Common interface
│   ├── TransitRepository.kt         # Repository pattern
│   └── models/                      # Platform-agnostic models
│       ├── TransitLine.kt
│       └── Station.kt
│
├── androidMain/kotlin/com/example/newoasa/database/
│   ├── TransitDatabase.kt           # Room database class
│   ├── RoomTransitDatabase.kt       # Adapter to ITransitDatabase
│   ├── ModelConverters.kt           # Entity ↔ Model conversions
│   ├── TransitDatabaseActual.kt     # Android actual implementation
│   ├── entities/                    # Room entities
│   ├── dao/                         # Room DAOs
│   ├── migration/                   # Database migrations
│   └── repository/                  # Legacy repositories
│
└── iosMain/kotlin/com/example/newoasa/database/
    └── TransitDatabaseActual.kt     # iOS actual implementation (stub)
```

## Architecture Diagram

```
┌─────────────────────────────────────────────────┐
│                 CommonMain                      │
│                                                 │
│  ┌──────────────────────────────────────┐      │
│  │   ITransitDatabase (interface)       │      │
│  │   - insertLine()                     │      │
│  │   - getAllLines()                    │      │
│  │   - searchStations()                 │      │
│  │   ... etc                            │      │
│  └──────────────────────────────────────┘      │
│                     ↑                           │
│                     │ uses                      │
│  ┌──────────────────────────────────────┐      │
│  │   TransitRepository                  │      │
│  │   - getAllLines()                    │      │
│  │   - getBusLines()                    │      │
│  │   - searchStations()                 │      │
│  └──────────────────────────────────────┘      │
│                                                 │
│  ┌──────────────────────────────────────┐      │
│  │   Models (data classes)              │      │
│  │   - TransitLine                      │      │
│  │   - Station                          │      │
│  └──────────────────────────────────────┘      │
└─────────────────────────────────────────────────┘
                    ↓ expect/actual
         ┌──────────┴──────────┐
         ↓                     ↓
┌──────────────────┐   ┌──────────────────┐
│   AndroidMain    │   │     iOSMain      │
│                  │   │                  │
│  RoomTransit     │   │  IosTransit      │
│  Database        │   │  Database (stub) │
│  (actual)        │   │  (actual)        │
│                  │   │                  │
│  Uses Room DB:   │   │  TODO: Use       │
│  - Entities      │   │  SQLDelight or   │
│  - DAOs          │   │  native DB       │
│  - Migrations    │   │                  │
└──────────────────┘   └──────────────────┘
```

## Usage in Common Code

### Basic Usage

```kotlin
import com.example.newoasa.database.TransitRepository

class MyViewModel {
    private val repository = TransitRepository()
    
    fun loadBusLines() {
        repository.getBusLines().collect { lines ->
            // Update UI with bus lines
        }
    }
}
```

### Direct Database Access

```kotlin
import com.example.newoasa.database.getTransitDatabase

val database = getTransitDatabase()
val allLines = database.getAllLines()
```

## Android Setup

In your `MainActivity.kt` or `Application` class:

```kotlin
import com.example.newoasa.database.initializeTransitDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize database
        initializeTransitDatabase(this)
        
        // ...
    }
}
```

## Models

### TransitLine

Represents a transit line (bus, metro, tram, etc.):

```kotlin
data class TransitLine(
    val id: Long,
    val lineNumber: String,          // "X95", "M1", "550"
    val category: String,            // "bus", "metro", "tram", etc.
    val displayName: String,         // "Χολαργός - Πειραιάς"
    val routeCount: Int,
    val routeIds: List<String>,
    val color: String,               // Hex color code
    val isActive: Boolean,
    val description: String?
)
```

### Station

Represents a stop or station:

```kotlin
data class Station(
    val id: Long,
    val lineId: Long,
    val stationType: String,         // "bus_stop", "metro_station", etc.
    val name: String,
    val stopCode: String,
    val orderOnLine: Int,
    val latitude: Double,
    val longitude: Double,
    
    // Type-specific fields
    val hasAccessibility: Boolean,
    val platforms: List<String>,
    val connections: List<String>,
    val isTransferStation: Boolean
)
```

## Benefits

✅ **Shared business logic** - Repository works on all platforms  
✅ **Type-safe** - Compile-time checks across platforms  
✅ **Flexible** - Different implementations per platform  
✅ **Testable** - Easy to mock for unit tests  
✅ **Clean architecture** - Separation of concerns

## iOS Implementation (TODO)

The iOS implementation is currently a stub. To implement:

1. **Option A: SQLDelight**
   - Cross-platform SQL database
   - Type-safe SQL queries
   - Generates code for all platforms

2. **Option B: Core Data**
   - Native iOS database
   - Requires Swift interop

3. **Option C: Realm**
   - Cross-platform mobile database
   - Easy to set up

## Migration from Old Architecture

The old Room-specific code in `androidMain/database/repository/` can be deprecated once all code uses the new `TransitRepository` from commonMain.

### Migration Steps:

1. ✅ Create common interfaces (`ITransitDatabase`)
2. ✅ Create common models (`TransitLine`, `Station`)
3. ✅ Create adapter (`RoomTransitDatabase`)
4. ✅ Create repository (`TransitRepository`)
5. ⏳ Update UI code to use `TransitRepository`
6. ⏳ Remove old repository implementations
7. ⏳ Implement iOS version

## Testing

### Unit Tests (commonTest)

```kotlin
class TransitRepositoryTest {
    @Test
    fun testGetBusLines() = runTest {
        val mockDb = MockTransitDatabase()
        val repository = TransitRepository(mockDb)
        
        val busLines = repository.getBusLines().first()
        assertEquals(10, busLines.size)
    }
}
```

### Android Integration Tests

Use in-memory Room database for testing.

## Performance Considerations

- **Pagination**: Implement for large datasets
- **Caching**: Repository can cache frequently accessed data
- **Indexing**: Room entities have proper indexes
- **Flow**: Use Kotlin Flow for reactive updates

## Future Enhancements

- [ ] Implement iOS database
- [ ] Add favorites/bookmarks
- [ ] Offline sync with backend
- [ ] Real-time arrival data
- [ ] Route planning
- [ ] User preferences

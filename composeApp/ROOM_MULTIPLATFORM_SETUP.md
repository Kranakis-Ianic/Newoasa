# Room Multiplatform Setup Guide

## Overview

The New OASA app now uses **Room Multiplatform** to share the database implementation across Android and iOS. This eliminates the need for separate database implementations and allows you to write database code once in `commonMain`.

## Architecture

```
commonMain/database/room/
├── entities/
│   ├── TransitLineEntity.kt    # Shared entity definitions
│   └── StationEntity.kt
├── dao/
│   ├── TransitLineDao.kt       # Shared DAO interfaces
│   └── StationDao.kt
├── TransitDatabase.kt          # Shared database class
└── DatabaseProvider.kt         # expect/actual for platform builders

androidMain/database/room/
└── DatabaseProvider.android.kt  # Android-specific builder

iosMain/database/room/
└── DatabaseProvider.ios.kt      # iOS-specific builder
```

## Gradle Configuration

Add the following to your `composeApp/build.gradle.kts`:

### 1. Add Room dependencies in `commonMain`

```kotlin
sourceSets {
    commonMain.dependencies {
        // Room Multiplatform
        implementation("androidx.room:room-runtime:2.7.0-alpha01")  // or latest version
        implementation("androidx.sqlite:sqlite-bundled:2.5.0-alpha01")
    }
}
```

### 2. Add KSP plugin

In your root `build.gradle.kts`:

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
}
```

In `composeApp/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")  // Add this
}
```

### 3. Configure KSP for Room

Add to `composeApp/build.gradle.kts`:

```kotlin
dependencies {
    // Room KSP
    add("kspCommonMainMetadata", "androidx.room:room-compiler:2.7.0-alpha01")
    add("kspAndroid", "androidx.room:room-compiler:2.7.0-alpha01")
    add("kspIosX64", "androidx.room:room-compiler:2.7.0-alpha01")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0-alpha01")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0-alpha01")
}

// Room KSP configuration
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}
```

## Android Setup

In your `MainActivity.kt` or `Application` class:

```kotlin
import com.example.newoasa.database.room.initializeDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Room database
        initializeDatabase(this)
        
        setContent {
            App()
        }
    }
}
```

## iOS Setup

No initialization needed! The database will be created automatically when first accessed.

## Usage from Common Code

### Basic Usage

```kotlin
import com.example.newoasa.database.room.getTransitRoomDatabase

class TransitViewModel {
    private val database = getTransitRoomDatabase()
    private val lineDao = database.transitLineDao()
    private val stationDao = database.stationDao()
    
    fun loadAllLines() {
        lineDao.getAllLinesFlow().collect { lines ->
            // Update UI
        }
    }
    
    suspend fun insertLine(line: TransitLineEntity) {
        lineDao.insertLine(line)
    }
}
```

### Using with Repository Pattern

```kotlin
import com.example.newoasa.database.room.getTransitRoomDatabase

class TransitRepository {
    private val database = getTransitRoomDatabase()
    
    fun getAllLines() = database.transitLineDao().getAllLinesFlow()
    
    fun searchLines(query: String) = database.transitLineDao().searchLinesFlow(query)
    
    suspend fun insertLines(lines: List<TransitLineEntity>) {
        database.transitLineDao().insertLines(lines)
    }
}
```

## Database Schema

### Transit Lines Table

```sql
CREATE TABLE transit_lines (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lineNumber TEXT NOT NULL,
    category TEXT NOT NULL,
    displayName TEXT NOT NULL,
    routeCount INTEGER,
    routeIds TEXT,
    color TEXT NOT NULL,
    isActive INTEGER NOT NULL,
    description TEXT,
    createdAt INTEGER,
    updatedAt INTEGER,
    UNIQUE(lineNumber, category)
);
```

### Stations Table

```sql
CREATE TABLE stations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lineId INTEGER NOT NULL,
    stationType TEXT NOT NULL,
    name TEXT NOT NULL,
    stopCode TEXT NOT NULL UNIQUE,
    orderOnLine INTEGER NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    -- ... additional fields
    FOREIGN KEY(lineId) REFERENCES transit_lines(id) ON DELETE CASCADE
);
```

## Benefits of Room Multiplatform

✅ **Write Once, Run Everywhere** - Database code in commonMain works on both platforms  
✅ **Type-Safe Queries** - Compile-time verification of SQL queries  
✅ **Flow Support** - Reactive database updates with Kotlin Flow  
✅ **No Duplication** - Single source of truth for database logic  
✅ **Full Room Features** - Relations, indexes, migrations, etc.  
✅ **Better Testing** - Shared tests for database operations

## Migration from Old Architecture

The old Android-only Room database in `androidMain/database/` can be deprecated. The new Room MP database in `commonMain/database/room/` provides the same functionality with cross-platform support.

### Migration Steps:

1. ✅ Move entities to commonMain/database/room/entities/
2. ✅ Move DAOs to commonMain/database/room/dao/
3. ✅ Create TransitDatabase in commonMain
4. ✅ Create platform-specific builders (expect/actual)
5. ⏳ Update repositories to use new database
6. ⏳ Update UI code
7. ⏳ Remove old androidMain database code
8. ⏳ Add database migrations if needed

## Advanced Features

### Database Migrations

```kotlin
actual fun getDatabaseBuilder(): RoomDatabase.Builder<TransitDatabase> {
    return Room.databaseBuilder<TransitDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
    .build()
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE transit_lines ADD COLUMN newField TEXT")
    }
}
```

### Pre-populating Database

```kotlin
actual fun getDatabaseBuilder(): RoomDatabase.Builder<TransitDatabase> {
    return Room.databaseBuilder<TransitDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
    .createFromAsset("databases/transit.db")  // Copy from assets
    .build()
}
```

### In-Memory Database (Testing)

```kotlin
fun getInMemoryDatabase(): TransitDatabase {
    return Room.inMemoryDatabaseBuilder<TransitDatabase>()
        .build()
}
```

## Testing

### Common Tests

```kotlin
class TransitDatabaseTest {
    private lateinit var database: TransitDatabase
    private lateinit var lineDao: TransitLineDao
    
    @Before
    fun setup() {
        database = getInMemoryDatabase()
        lineDao = database.transitLineDao()
    }
    
    @Test
    fun testInsertAndRetrieve() = runTest {
        val line = TransitLineEntity(
            lineNumber = "X95",
            category = "bus",
            displayName = "X95 Express",
            color = "#FF0000"
        )
        
        lineDao.insertLine(line)
        val retrieved = lineDao.getLineByNumberAndCategory("X95", "bus")
        
        assertEquals(line.lineNumber, retrieved?.lineNumber)
    }
}
```

## Troubleshooting

### KSP Errors

If you see KSP-related errors:
1. Run `./gradlew clean`
2. Invalidate caches and restart IDE
3. Check that KSP plugin version matches Kotlin version

### iOS Build Errors

If iOS fails to build:
1. Ensure you've added KSP for all iOS targets
2. Check that sqlite-bundled is included in commonMain
3. Verify Room version is 2.7.0-alpha01 or later

### Database File Location

- **Android**: `/data/data/com.example.newoasa/databases/transit_database.db`
- **iOS**: `~/Documents/transit_database.db`

## Resources

- [Room Multiplatform Documentation](https://developer.android.com/kotlin/multiplatform/room)
- [Room Migration Guide](https://developer.android.com/training/data-storage/room/migrating-db-versions)
- [KSP Documentation](https://kotlinlang.org/docs/ksp-overview.html)

## Next Steps

1. Update your `build.gradle.kts` with Room MP dependencies
2. Add KSP plugin configuration
3. Initialize database in Android MainActivity
4. Start using `getTransitRoomDatabase()` in your ViewModels
5. Test on both Android and iOS

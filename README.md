# Newoasa - Athens Transit App

This is a Kotlin Multiplatform project targeting Android, iOS.

A comprehensive transit application for Athens, Greece, featuring interactive maps with Metro, Tram, and Suburban railway lines, along with clickable station information.

## Features

- 🗺️ **Interactive Transit Map** - View Athens Metro, Tram, and Suburban railway lines
- 🚇 **Station Information** - Click on any station to see details and connecting lines
- 🌐 **Multi-line Stations** - Automatically shows all lines serving each station
- 📱 **Cross-platform** - Works on Android and iOS

## Project Structure

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that's common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple's CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you're sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/scripts](./scripts) contains Python scripts for processing transit data
  - `generate_transit_repository.py` - Generates Kotlin repository from GeoJSON files
  - `combine_transit_lines.py` - Combines line files and extracts stations

## Getting Started

### Prerequisites

- JDK 17 or higher
- Android Studio Ladybug or later
- Xcode 14+ (for iOS development)
- Python 3.7+ (for data processing scripts)

### Setup

1. Clone the repository:
```bash
git clone https://github.com/Kranakis-Ianic/newoasa.git
cd newoasa
```

2. Generate combined transit data files:
```bash
python scripts/combine_transit_lines.py
```

This will create:
- `combined_metro_lines.geojson` - All metro lines combined
- `combined_tram_lines.geojson` - All tram lines combined  
- `combined_suburban_lines.geojson` - All suburban lines combined
- `all_transit_stations.geojson` - All stations with line information

3. Build and run

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE's toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE's toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

## Documentation

- [Station Integration Guide](./docs/STATION_INTEGRATION.md) - How to integrate station cards into the map
- [Scripts Usage Guide](./docs/SCRIPTS_USAGE.md) - Detailed documentation for Python data processing scripts

## Key Components

### Station System

- **TransitStation** - Data model for stations with support for multiple lines
- **StationParser** - Utilities for parsing GeoJSON and clustering nearby stations
- **StationCard** - UI component displaying station information and connected lines

### Transit Lines

- Metro Lines (M1, M2, M3) - 🟢 Always visible on map
- Tram Lines (T6, T7, T7A) - 🟪 Always visible on map
- Suburban Railway (S1, S2) - 🔵 Always visible on map

## Data Processing

Transit data is processed using Python scripts that:

1. Scan all GeoJSON files in the project
2. Combine route segments from multiple files per line
3. Extract and deduplicate stations
4. Merge stations serving multiple lines
5. Generate combined files for efficient loading

See [Scripts Usage Guide](./docs/SCRIPTS_USAGE.md) for more details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project uses transit data from OpenStreetMap contributors.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
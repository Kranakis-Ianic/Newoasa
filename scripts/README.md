# GeoJSON Processing Scripts

## combine_geojson.py

Combines all individual metro line GeoJSON files into a single `final_all_lines.geojson` file.

### What it does:

- ✅ Combines all `.geojson` files from `Metro lines/` directory
- ✅ Includes **LineString** features (the transit lines)
- ✅ Includes **Point** features (the stations)
- ✅ Extracts `colour` property from nested relations if needed
- ✅ Creates output at `files/geojson/final_all_lines.geojson`

### How to run:

```bash
# From project root directory
python3 scripts/combine_geojson.py
```

### Output:

The script will show:
- Number of files processed
- Total features combined
- Count of lines vs stations

Example output:
```
🚇 Combining GeoJSON files...
📁 Input: .../Metro lines
📄 Output: .../final_all_lines.geojson

Processed: metro_1_Kifissia_→_Piraeus.geojson
Processed: metro_2_Anthoupoli_→_Elliniko.geojson
...

✅ Combined 6 files
📊 Total features: 450
   - Lines: 6
   - Stations: 444
💾 Output: final_all_lines.geojson

✨ Done!
```

### Requirements:

- Python 3.6+
- No external dependencies needed (uses standard library)

### After running:

1. The `final_all_lines.geojson` file will be created
2. Rebuild your app to include the new resource file
3. The map will now display:
   - All transit lines with colors
   - All stations as circles

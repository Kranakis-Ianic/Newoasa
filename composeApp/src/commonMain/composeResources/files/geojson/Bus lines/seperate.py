import json
import os
from collections import defaultdict

# 1. Paths
INPUT = "bus_trolley_lines.geojson"
TROLLEY_DIR = "trolley"
BUS_DIR = "bus"

# 2. Create output folders if they don't exist
os.makedirs(TROLLEY_DIR, exist_ok=True)
os.makedirs(BUS_DIR, exist_ok=True)

# 3. Read the original GeoJSON
with open(INPUT, "r", encoding="utf-8") as f:
    data = json.load(f)

# 4. Group features by (type, line number)
groups = defaultdict(list)

for feat in data["features"]:
    props = feat.get("properties", {})
    line_code = props.get("LINE") or "Unknown"
    kind = props.get("q2wHide_LINE2")  # 'ΤΡΟΛΛΕΥ' or 'ΛΕΩΦΟΡΕΙΑ'

    if kind == "ΤΡΟΛΛΕΥ":
        folder = TROLLEY_DIR
        base = "trolley"
    else:
        folder = BUS_DIR
        base = "bus"

    # Optional: add a human-readable name
    name = f"{base.capitalize()} {line_code}"
    props["name"] = name
    feat["properties"] = props

    groups[(folder, line_code)].append(feat)

# 5. Write one file per line in the correct folder
for (folder, line_code), feats in groups.items():
    out = {
        "type": "FeatureCollection",
        "features": feats
    }
    filename = f"{base}_{line_code}.geojson" if line_code != "Unknown" else f"{base}_unknown.geojson"
    # base depends on folder
    if folder == TROLLEY_DIR:
        base_prefix = "trolley"
    else:
        base_prefix = "bus"
    filename = f"{base_prefix}_{line_code}.geojson"

    out_path = os.path.join(folder, filename)
    with open(out_path, "w", encoding="utf-8") as f:
        json.dump(out, f, ensure_ascii=False, indent=2)
    print("Wrote", out_path)

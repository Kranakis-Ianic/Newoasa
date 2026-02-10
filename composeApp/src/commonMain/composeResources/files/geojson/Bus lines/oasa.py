import os
import json
import requests
import re
import time

# --- Configuration ---
BASE_URL = "http://telematics.oasa.gr/api/"
HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
    "Accept": "application/json, text/plain, */*"
}

# Known Trolley Line IDs in Athens
TROLLEY_IDS = {
    "1", "2", "3", "4", "5", "6", 
    "10", "11", "12", "14", "15", 
    "16", "17", "18", "19", "19B", 
    "20", "21", "24", "25"
}

def clean_json_response(text):
    """
    Extracts JSON from an HTML body if necessary.
    Removes BOM and HTML tags to isolate the JSON structure.
    """
    text = text.strip().lstrip('\ufeff')
    
    # Use regex to capture the JSON object/array between the first {/[ and last }/]
    match = re.search(r'([\[{].*[\]}])', text, re.DOTALL)
    if match:
        return match.group(1)
    
    # Fallback: aggressive tag stripping if regex fails
    clean = re.sub(r'<[^>]+>', '', text).strip()
    return clean

def fetch_api(params):
    try:
        response = requests.get(BASE_URL, params=params, headers=HEADERS, timeout=30)
        response.raise_for_status()
        
        cleaned_text = clean_json_response(response.text)
        if not cleaned_text:
            return None
            
        return json.loads(cleaned_text)
    except (json.JSONDecodeError, requests.RequestException):
        return None

def main():
    # 1. Create base folders
    os.makedirs("buses", exist_ok=True)
    os.makedirs("trolleys", exist_ok=True)

    print("Fetching Lines...")
    lines_data = fetch_api({"act": "webGetLines"})
    
    if not lines_data:
        print("Failed to retrieve lines. Please check your connection or the API status.")
        return

    print(f"Found {len(lines_data)} lines. Processing...")

    for line in lines_data:
        line_code = line.get("LineCode")
        line_id = line.get("LineID")
        line_descr = line.get("LineDescr")
        
        if not line_code:
            continue

        # Determine base type folder
        type_folder = "trolleys" if line_id in TROLLEY_IDS else "buses"
        
        # Create specific folder for this line (e.g., buses/732)
        line_folder = os.path.join(type_folder, str(line_id))
        os.makedirs(line_folder, exist_ok=True)
        
        print(f"Processing Line {line_id}: {line_descr}")

        # 2. Get Routes for this Line
        routes_data = fetch_api({"act": "webGetRoutes", "p1": line_code})
        
        if routes_data:
            for route in routes_data:
                route_code = route.get("RouteCode")
                route_descr = route.get("RouteDescr")
                route_type = route.get("RouteType")

                # Define filename for this specific route (e.g., route_3822.geojson)
                route_filename = os.path.join(line_folder, f"route_{route_code}.geojson")

                # Initialize FeatureCollection for this specific route
                feature_collection = {
                    "type": "FeatureCollection",
                    "properties": {
                        "LineCode": line_code,
                        "LineID": line_id,
                        "LineDescr": line_descr,
                        "RouteCode": route_code,
                        "RouteDescr": route_descr,
                        "RouteType": route_type
                    },
                    "features": []
                }

                # 3. Get Details AND Stops for this Route
                data = fetch_api({"act": "webGetRoutesDetailsAndStops", "p1": route_code})
                
                if data:
                    # --- Process Route Geometry (Path) ---
                    route_points = data.get("details", [])
                    if route_points:
                        coords = []
                        # Sort by order to ensure correct path drawing
                        try:
                            sorted_points = sorted(route_points, key=lambda x: int(x.get("routed_order", 0)))
                        except ValueError:
                            sorted_points = route_points

                        for pt in sorted_points:
                            try:
                                lng = float(pt.get("routed_x"))
                                lat = float(pt.get("routed_y"))
                                coords.append([lng, lat])
                            except (ValueError, TypeError):
                                continue
                        
                        if coords:
                            feature_collection["features"].append({
                                "type": "Feature",
                                "geometry": {
                                    "type": "LineString",
                                    "coordinates": coords
                                },
                                "properties": {
                                    "type": "route_path",
                                    "RouteCode": route_code
                                }
                            })

                    # --- Process Stops ---
                    stops = data.get("stops", [])
                    if stops:
                        for stop in stops:
                            try:
                                lat = float(stop.get("StopLat", 0))
                                lng = float(stop.get("StopLng", 0))
                                
                                if lat != 0 and lng != 0:
                                    feature_collection["features"].append({
                                        "type": "Feature",
                                        "geometry": {
                                            "type": "Point",
                                            "coordinates": [lng, lat]
                                        },
                                        "properties": {
                                            "type": "stop",
                                            "StopCode": stop.get("StopCode"),
                                            "StopID": stop.get("StopID"),
                                            "StopDescr": stop.get("StopDescr"),
                                            "StopOrder": stop.get("RouteStopOrder"),
                                            "StopType": stop.get("StopType")
                                        }
                                    })
                            except (ValueError, TypeError):
                                continue
                
                # Save the route-specific GeoJSON
                with open(route_filename, 'w', encoding='utf-8') as f:
                    json.dump(feature_collection, f, ensure_ascii=False, indent=2)
                
                # Small delay between routes
                time.sleep(0.05)
        
        # Small delay between lines
        time.sleep(0.1)

    print("Done generating route files.")

if __name__ == "__main__":
    main()

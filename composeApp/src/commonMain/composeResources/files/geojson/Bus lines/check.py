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

TROLLEY_IDS = {
    "1", "2", "3", "4", "5", "6", 
    "10", "11", "12", "14", "15", 
    "16", "17", "18", "19", "19B", 
    "20", "21", "24", "25"
}

def clean_json_response(text):
    text = text.strip().lstrip('\ufeff')
    match = re.search(r'([\[{].*[\]}])', text, re.DOTALL)
    if match:
        return match.group(1)
    return re.sub(r'<[^>]+>', '', text).strip()

def fetch_api(params):
    try:
        response = requests.get(BASE_URL, params=params, headers=HEADERS, timeout=10)
        response.raise_for_status()
        cleaned_text = clean_json_response(response.text)
        if not cleaned_text:
            return None
        return json.loads(cleaned_text)
    except Exception:
        return None

def main():
    print("--- Starting Verification ---")
    print("Fetching Line Master List from API...")
    lines_data = fetch_api({"act": "webGetLines"})
    
    if not lines_data:
        print("CRITICAL: Could not fetch lines from API. Verification aborted.")
        return

    total_lines = len(lines_data)
    total_routes = 0
    
    missing_lines = []
    missing_routes = []
    
    print(f"Checking {total_lines} lines...")
    
    for index, line in enumerate(lines_data):
        line_code = line.get("LineCode")
        line_id = line.get("LineID")
        line_descr = line.get("LineDescr")
        
        if not line_code:
            continue

        # Determine expected folder path
        type_folder = "trolleys" if line_id in TROLLEY_IDS else "buses"
        line_folder = os.path.join(type_folder, str(line_id))
        
        # 1. Check if Line Folder Exists
        if not os.path.isdir(line_folder):
            missing_lines.append(f"{line_id} ({line_descr})")
            print(f"[MISSING LINE] Folder not found: {line_folder}")
            # If folder missing, all routes are effectively missing, but we can't fetch them all efficiently without querying
            # We will query to count them properly for the report
        
        # Fetch routes for this line to check individual files
        routes_data = fetch_api({"act": "webGetRoutes", "p1": line_code})
        
        if routes_data:
            total_routes += len(routes_data)
            for route in routes_data:
                route_code = route.get("RouteCode")
                
                # Expected filename
                expected_file = os.path.join(line_folder, f"route_{route_code}.geojson")
                
                # 2. Check if Route File Exists
                if not os.path.isfile(expected_file):
                    missing_routes.append(f"Line {line_id} -> Route {route_code}")
                    # Only print if the line folder actually existed (otherwise it's redundant)
                    if os.path.isdir(line_folder):
                        print(f"   [MISSING ROUTE] {expected_file}")
        
        # Progress indicator every 10 lines
        if (index + 1) % 10 == 0:
            print(f"Processed {index + 1}/{total_lines} lines...")
            time.sleep(0.1) # Be nice to the API

    # --- Final Report ---
    print("\n" + "="*40)
    print("VERIFICATION REPORT")
    print("="*40)
    print(f"Total Lines in API:   {total_lines}")
    print(f"Total Routes in API:  {total_routes}")
    print("-" * 20)
    print(f"Missing Lines:        {len(missing_lines)}")
    print(f"Missing Routes:       {len(missing_routes)}")
    print("="*40)

    if missing_lines:
        print("\nMISSING LINES (Folders):")
        for m in missing_lines[:20]:
            print(f" - {m}")
        if len(missing_lines) > 20:
            print(f" ... and {len(missing_lines) - 20} more.")

    if missing_routes:
        print("\nMISSING ROUTES (Files):")
        # Filter out routes that are missing simply because the whole line is missing
        isolated_missing = [r for r in missing_routes if not any(line_id in r for line_id in [m.split()[0] for m in missing_lines])]
        
        if isolated_missing:
            for m in isolated_missing[:20]:
                print(f" - {m}")
            if len(isolated_missing) > 20:
                print(f" ... and {len(isolated_missing) - 20} more.")
        else:
            print(" (All missing routes are due to missing line folders)")

    if not missing_lines and not missing_routes:
        print("\nSUCCESS: All lines and routes match the API!")

if __name__ == "__main__":
    main()

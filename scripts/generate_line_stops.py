#!/usr/bin/env python3
"""
Script to generate line_stops.json from final_all_stations.geojson
This extracts all metro, tram, and suburban railway stations and organizes them by route.
"""

import json
import os
from collections import defaultdict

def load_stations(stations_file_path):
    """Load stations from GeoJSON file"""
    with open(stations_file_path, 'r', encoding='utf-8') as f:
        return json.load(f)

def extract_routes_and_stops(stations_data):
    """Extract routes and their stops from station data"""
    routes_dict = defaultdict(lambda: {
        'stops': [],
        'stop_ids': set()  # Track station IDs to avoid duplicates
    })
    
    for feature in stations_data['features']:
        if '@relations' not in feature['properties']:
            continue
        
        coords = feature['geometry']['coordinates']
        station_id = feature['properties']['@id']
        station_name = feature['properties'].get('name', '')
        station_name_en = feature['properties'].get('name:en', station_name)
        
        # Process each relation (route) this station belongs to
        for relation in feature['properties']['@relations']:
            role = relation.get('role', '')
            # Skip exit-only or entry-only stops
            if 'exit_only' in role or 'entry_only' in role:
                continue
                
            reltags = relation.get('reltags', {})
            route_type = reltags.get('route', '')
            
            # Only process subway, tram, and train routes
            if route_type not in ['subway', 'tram', 'train']:
                continue
            
            line_ref = reltags.get('ref', '')
            from_station = reltags.get('from', '')
            to_station = reltags.get('to', '')
            color = reltags.get('colour', '#000000')
            line_name_el = reltags.get('name', '')
            line_name_en = reltags.get('name:en', line_name_el)
            
            # Create a unique route key
            route_key = f"{line_ref}_{from_station}_{to_station}"
            
            # Initialize route if not exists
            if 'line_ref' not in routes_dict[route_key]:
                routes_dict[route_key]['line_ref'] = line_ref
                routes_dict[route_key]['line_name'] = line_name_el
                routes_dict[route_key]['line_name_en'] = line_name_en
                routes_dict[route_key]['from'] = from_station
                routes_dict[route_key]['to'] = to_station
                routes_dict[route_key]['color'] = color
                routes_dict[route_key]['route_type'] = route_type
            
            # Add station if not already in route
            if station_id not in routes_dict[route_key]['stop_ids']:
                stop_info = {
                    'id': station_id,
                    'name': station_name,
                    'nameEn': station_name_en,
                    'lat': coords[1],
                    'lon': coords[0]
                }
                routes_dict[route_key]['stops'].append(stop_info)
                routes_dict[route_key]['stop_ids'].add(station_id)
    
    return routes_dict

def create_route_object(route_key, route_data, route_id_prefix):
    """Create a route object in the required format"""
    line_ref = route_data['line_ref']
    
    # Determine terminal names for English
    terminal1_en = route_data['from']
    terminal2_en = route_data['to']
    
    # Map common Greek terminal names to English
    name_map = {
        'Πειραιάς': 'Piraeus', 'Πειραιά': 'Piraeus',
        'Κηφισιά': 'Kifissia',
        'Ανθούπολη': 'Anthoupoli',
        'Ελληνικό': 'Elliniko',
        'Αεροδρόμιο': 'Airport',
        'Δημοτικό Θέατρο': 'Dimotiko Theatro',
        'Σύνταγμα': 'Syntagma',
        'Πικροδάφνη': 'Pikrodafni',
        'Ασκληπιείο Βούλας': 'Asklipiio Voulas',
        'Αγία Τριάδα': 'Agia Triada',
        'Κιάτο': 'Kiato',
        'Χαλκίδα': 'Chalkida',
        'Άνω Λιόσια': 'Ano Liosia',
        'Αθήνα': 'Athens'
    }
    
    terminal1_en = name_map.get(route_data['from'], route_data['from'])
    terminal2_en = name_map.get(route_data['to'], route_data['to'])
    
    # Create direction strings
    direction_el = f"{route_data['from']} → {route_data['to']}"
    direction_en = f"{terminal1_en} → {terminal2_en}"
    
    # Determine line name prefix
    if route_data['route_type'] == 'subway':
        line_name = f"Μετρό {line_ref}"
        line_name_en = f"Metro {line_ref}"
    elif route_data['route_type'] == 'tram':
        line_name = f"Τραμ {line_ref}"
        line_name_en = f"Tram {line_ref}"
    else:  # train
        line_name = f"Προαστιακός {line_ref}"
        line_name_en = f"Train {line_ref}"
    
    # Create route ID
    route_id = f"{line_ref}_{route_id_prefix}"
    
    # Remove the stop_ids set (used only for tracking)
    stops = route_data['stops']
    
    return {
        'routeId': route_id,
        'lineId': line_ref,
        'lineName': line_name,
        'lineNameEn': line_name_en,
        'lineRef': line_ref,
        'direction': direction_el,
        'directionEn': direction_en,
        'terminal1': route_data['from'],
        'terminal1En': terminal1_en,
        'terminal2': route_data['to'],
        'terminal2En': terminal2_en,
        'color': route_data['color'],
        'routeType': route_data['route_type'],
        'stops': stops
    }

def main():
    # File paths
    script_dir = os.path.dirname(os.path.abspath(__file__))
    project_root = os.path.dirname(script_dir)
    stations_file = os.path.join(project_root, 'composeApp/src/commonMain/composeResources/files/geojson/final_all_stations.geojson')
    output_file = os.path.join(project_root, 'composeApp/src/commonMain/composeResources/files/line_stops.json')
    
    print("Loading stations data...")
    stations_data = load_stations(stations_file)
    
    print(f"Processing {len(stations_data['features'])} station features...")
    routes_dict = extract_routes_and_stops(stations_data)
    
    print(f"\nFound {len(routes_dict)} routes:")
    for route_key in sorted(routes_dict.keys()):
        route = routes_dict[route_key]
        print(f"  {route['line_ref']}: {route['from']} → {route['to']} ({len(route['stops'])} stops, {route['route_type']})")
    
    # Convert to output format
    routes_list = []
    route_counter = defaultdict(int)
    
    for route_key in sorted(routes_dict.keys()):
        route_data = routes_dict[route_key]
        line_ref = route_data['line_ref']
        
        # Generate a counter-based route ID suffix
        route_counter[line_ref] += 1
        route_id_suffix = f"ROUTE{route_counter[line_ref]}"
        
        route_obj = create_route_object(route_key, route_data, route_id_suffix)
        routes_list.append(route_obj)
    
    # Create final output
    output_data = {'routes': routes_list}
    
    # Write to file
    print(f"\nWriting {len(routes_list)} routes to {output_file}...")
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(output_data, f, ensure_ascii=False, indent=2)
    
    print("Done!")
    
    # Print summary by line
    line_summary = defaultdict(list)
    for route in routes_list:
        line_summary[route['lineId']].append(route)
    
    print("\n=== Summary by Line ===")
    for line_id in sorted(line_summary.keys()):
        routes = line_summary[line_id]
        total_stops = sum(len(r['stops']) for r in routes)
        print(f"{line_id}: {len(routes)} routes, {total_stops} total stop entries")

if __name__ == '__main__':
    main()

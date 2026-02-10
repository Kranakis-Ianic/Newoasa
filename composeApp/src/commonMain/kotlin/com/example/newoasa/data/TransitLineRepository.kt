package com.example.newoasa.data

/**
 * Auto-generated Transit Line Repository
 * Generated from GeoJSON folder structure
 * 
 * DO NOT EDIT MANUALLY!
 * Run scripts/generate_transit_repository.py to regenerate this file
 * 
 * Generated: 2026-02-10 17:02:00
 * Total Lines: 291
 * Total Routes: 700
 */

/**
 * Represents a transit line with its routes
 * @property lineNumber The line identifier (e.g., "022", "1", "309Β")
 * @property category The category of transit ("buses", "trolleys", "metro", "tram")
 * @property routeIds List of route IDs for this line
 * @property routePaths Resource paths to the GeoJSON files for each route
 */
data class TransitLine(
    val lineNumber: String,
    val category: String,
    val routeIds: List<String>,
    val routePaths: List<String>
) {
    /**
     * Returns the display name for this line
     */
    val displayName: String
        get() = when (category) {
            "buses" -> "Bus $lineNumber"
            "trolleys" -> "Trolley $lineNumber"
            "metro" -> "Metro Line $lineNumber"
            "tram" -> "Tram Line $lineNumber"
            else -> "Line $lineNumber"
        }
    
    /**
     * Returns true if this is a bus line
     */
    val isBus: Boolean get() = category == "buses"
    
    /**
     * Returns true if this is a trolley line
     */
    val isTrolley: Boolean get() = category == "trolleys"

    /**
     * Returns true if this is a metro line
     */
    val isMetro: Boolean get() = category == "metro"

    /**
     * Returns true if this is a tram line
     */
    val isTram: Boolean get() = category == "tram"
    
    /**
     * Returns the base path to this line's geojson folder
     * Note: For Metro and Tram which use a flat structure, this points to the category folder
     */
    val basePath: String get() = when(category) {
        "metro" -> "files/geojson/Metro lines"
        "tram" -> "files/geojson/Tram lines"
        "buses" -> "files/geojson/Bus lines/$lineNumber"
        "trolleys" -> "files/geojson/Trolley lines/$lineNumber"
        else -> "files/geojson/$category/$lineNumber"
    }
}

/**
 * Repository providing access to all transit lines
 * This is a singleton object that holds all transit line data
 */
object TransitLineRepository {
    private val lines: List<TransitLine> = listOf(
        TransitLine(
            lineNumber = "14",
            category = "buses",
            routeIds = listOf("1997", "1998"),
            routePaths = listOf("files/geojson/Bus lines/14/route_1997.geojson", "files/geojson/Bus lines/14/route_1998.geojson")
        ),
        TransitLine(
            lineNumber = "16",
            category = "buses",
            routeIds = listOf("2079"),
            routePaths = listOf("files/geojson/Bus lines/16/route_2079.geojson")
        ),
        TransitLine(
            lineNumber = "19Β",
            category = "buses",
            routeIds = listOf("4044"),
            routePaths = listOf("files/geojson/Bus lines/19Β/route_4044.geojson")
        ),
        TransitLine(
            lineNumber = "022",
            category = "buses",
            routeIds = listOf("3494", "4213", "4214"),
            routePaths = listOf("files/geojson/Bus lines/022/route_3494.geojson", "files/geojson/Bus lines/022/route_4213.geojson", "files/geojson/Bus lines/022/route_4214.geojson")
        ),
        TransitLine(
            lineNumber = "026",
            category = "buses",
            routeIds = listOf("1799", "5563"),
            routePaths = listOf("files/geojson/Bus lines/026/route_1799.geojson", "files/geojson/Bus lines/026/route_5563.geojson")
        ),
        TransitLine(
            lineNumber = "031",
            category = "buses",
            routeIds = listOf("4689", "4690"),
            routePaths = listOf("files/geojson/Bus lines/031/route_4689.geojson", "files/geojson/Bus lines/031/route_4690.geojson")
        ),
        TransitLine(
            lineNumber = "032",
            category = "buses",
            routeIds = listOf("5643", "5670"),
            routePaths = listOf("files/geojson/Bus lines/032/route_5643.geojson", "files/geojson/Bus lines/032/route_5670.geojson")
        ),
        TransitLine(
            lineNumber = "035",
            category = "buses",
            routeIds = listOf("2953", "4538"),
            routePaths = listOf("files/geojson/Bus lines/035/route_2953.geojson", "files/geojson/Bus lines/035/route_4538.geojson")
        ),
        TransitLine(
            lineNumber = "036",
            category = "buses",
            routeIds = listOf("3093", "4948", "4949"),
            routePaths = listOf("files/geojson/Bus lines/036/route_3093.geojson", "files/geojson/Bus lines/036/route_4948.geojson", "files/geojson/Bus lines/036/route_4949.geojson")
        ),
        TransitLine(
            lineNumber = "040",
            category = "buses",
            routeIds = listOf("5512", "5513", "5535"),
            routePaths = listOf("files/geojson/Bus lines/040/route_5512.geojson", "files/geojson/Bus lines/040/route_5513.geojson", "files/geojson/Bus lines/040/route_5535.geojson")
        ),
        TransitLine(
            lineNumber = "046",
            category = "buses",
            routeIds = listOf("1821", "5330"),
            routePaths = listOf("files/geojson/Bus lines/046/route_1821.geojson", "files/geojson/Bus lines/046/route_5330.geojson")
        ),
        TransitLine(
            lineNumber = "049",
            category = "buses",
            routeIds = listOf("2995", "3086"),
            routePaths = listOf("files/geojson/Bus lines/049/route_2995.geojson", "files/geojson/Bus lines/049/route_3086.geojson")
        ),
        TransitLine(
            lineNumber = "051",
            category = "buses",
            routeIds = listOf("3967", "3968", "4737"),
            routePaths = listOf("files/geojson/Bus lines/051/route_3967.geojson", "files/geojson/Bus lines/051/route_3968.geojson", "files/geojson/Bus lines/051/route_4737.geojson")
        ),
        TransitLine(
            lineNumber = "052",
            category = "buses",
            routeIds = listOf("4570"),
            routePaths = listOf("files/geojson/Bus lines/052/route_4570.geojson")
        ),
        TransitLine(
            lineNumber = "054",
            category = "buses",
            routeIds = listOf("5361", "5668"),
            routePaths = listOf("files/geojson/Bus lines/054/route_5361.geojson", "files/geojson/Bus lines/054/route_5668.geojson")
        ),
        TransitLine(
            lineNumber = "057",
            category = "buses",
            routeIds = listOf("1810"),
            routePaths = listOf("files/geojson/Bus lines/057/route_1810.geojson")
        ),
        TransitLine(
            lineNumber = "060",
            category = "buses",
            routeIds = listOf("3447"),
            routePaths = listOf("files/geojson/Bus lines/060/route_3447.geojson")
        ),
        TransitLine(
            lineNumber = "101",
            category = "buses",
            routeIds = listOf("5651", "5652"),
            routePaths = listOf("files/geojson/Bus lines/101/route_5651.geojson", "files/geojson/Bus lines/101/route_5652.geojson")
        ),
        TransitLine(
            lineNumber = "106",
            category = "buses",
            routeIds = listOf("2759", "5331"),
            routePaths = listOf("files/geojson/Bus lines/106/route_2759.geojson", "files/geojson/Bus lines/106/route_5331.geojson")
        ),
        TransitLine(
            lineNumber = "109",
            category = "buses",
            routeIds = listOf("5653", "5654", "5656"),
            routePaths = listOf("files/geojson/Bus lines/109/route_5653.geojson", "files/geojson/Bus lines/109/route_5654.geojson", "files/geojson/Bus lines/109/route_5656.geojson")
        ),
        TransitLine(
            lineNumber = "112",
            category = "buses",
            routeIds = listOf("4986", "4987"),
            routePaths = listOf("files/geojson/Bus lines/112/route_4986.geojson", "files/geojson/Bus lines/112/route_4987.geojson")
        ),
        TransitLine(
            lineNumber = "115",
            category = "buses",
            routeIds = listOf("2187", "3256", "3687"),
            routePaths = listOf("files/geojson/Bus lines/115/route_2187.geojson", "files/geojson/Bus lines/115/route_3256.geojson", "files/geojson/Bus lines/115/route_3687.geojson")
        ),
        TransitLine(
            lineNumber = "116",
            category = "buses",
            routeIds = listOf("2159", "3032"),
            routePaths = listOf("files/geojson/Bus lines/116/route_2159.geojson", "files/geojson/Bus lines/116/route_3032.geojson")
        ),
        TransitLine(
            lineNumber = "117",
            category = "buses",
            routeIds = listOf("1934", "3019"),
            routePaths = listOf("files/geojson/Bus lines/117/route_1934.geojson", "files/geojson/Bus lines/117/route_3019.geojson")
        ),
        TransitLine(
            lineNumber = "120",
            category = "buses",
            routeIds = listOf("3373", "3375", "5478"),
            routePaths = listOf("files/geojson/Bus lines/120/route_3373.geojson", "files/geojson/Bus lines/120/route_3375.geojson", "files/geojson/Bus lines/120/route_5478.geojson")
        ),
        TransitLine(
            lineNumber = "122",
            category = "buses",
            routeIds = listOf("4019", "4020", "4581"),
            routePaths = listOf("files/geojson/Bus lines/122/route_4019.geojson", "files/geojson/Bus lines/122/route_4020.geojson", "files/geojson/Bus lines/122/route_4581.geojson")
        ),
        TransitLine(
            lineNumber = "123",
            category = "buses",
            routeIds = listOf("2493", "3955"),
            routePaths = listOf("files/geojson/Bus lines/123/route_2493.geojson", "files/geojson/Bus lines/123/route_3955.geojson")
        ),
        TransitLine(
            lineNumber = "124",
            category = "buses",
            routeIds = listOf("4047", "4048", "5479"),
            routePaths = listOf("files/geojson/Bus lines/124/route_4047.geojson", "files/geojson/Bus lines/124/route_4048.geojson", "files/geojson/Bus lines/124/route_5479.geojson")
        ),
        TransitLine(
            lineNumber = "126",
            category = "buses",
            routeIds = listOf("5332", "5333"),
            routePaths = listOf("files/geojson/Bus lines/126/route_5332.geojson", "files/geojson/Bus lines/126/route_5333.geojson")
        ),
        TransitLine(
            lineNumber = "128",
            category = "buses",
            routeIds = listOf("1928", "4225", "4226", "5481"),
            routePaths = listOf("files/geojson/Bus lines/128/route_1928.geojson", "files/geojson/Bus lines/128/route_4225.geojson", "files/geojson/Bus lines/128/route_4226.geojson", "files/geojson/Bus lines/128/route_5481.geojson")
        ),
        TransitLine(
            lineNumber = "130",
            category = "buses",
            routeIds = listOf("5514"),
            routePaths = listOf("files/geojson/Bus lines/130/route_5514.geojson")
        ),
        TransitLine(
            lineNumber = "131",
            category = "buses",
            routeIds = listOf("2560", "4227"),
            routePaths = listOf("files/geojson/Bus lines/131/route_2560.geojson", "files/geojson/Bus lines/131/route_4227.geojson")
        ),
        TransitLine(
            lineNumber = "136",
            category = "buses",
            routeIds = listOf("5284"),
            routePaths = listOf("files/geojson/Bus lines/136/route_5284.geojson")
        ),
        TransitLine(
            lineNumber = "137",
            category = "buses",
            routeIds = listOf("5285"),
            routePaths = listOf("files/geojson/Bus lines/137/route_5285.geojson")
        ),
        TransitLine(
            lineNumber = "140",
            category = "buses",
            routeIds = listOf("5043", "5483", "5650", "5690"),
            routePaths = listOf("files/geojson/Bus lines/140/route_5043.geojson", "files/geojson/Bus lines/140/route_5483.geojson", "files/geojson/Bus lines/140/route_5650.geojson", "files/geojson/Bus lines/140/route_5690.geojson")
        ),
        TransitLine(
            lineNumber = "141",
            category = "buses",
            routeIds = listOf("2552", "4916", "4917"),
            routePaths = listOf("files/geojson/Bus lines/141/route_2552.geojson", "files/geojson/Bus lines/141/route_4916.geojson", "files/geojson/Bus lines/141/route_4917.geojson")
        ),
        TransitLine(
            lineNumber = "142",
            category = "buses",
            routeIds = listOf("5663"),
            routePaths = listOf("files/geojson/Bus lines/142/route_5663.geojson")
        ),
        TransitLine(
            lineNumber = "154",
            category = "buses",
            routeIds = listOf("3895"),
            routePaths = listOf("files/geojson/Bus lines/154/route_3895.geojson")
        ),
        TransitLine(
            lineNumber = "162",
            category = "buses",
            routeIds = listOf("5116"),
            routePaths = listOf("files/geojson/Bus lines/162/route_5116.geojson")
        ),
        TransitLine(
            lineNumber = "164",
            category = "buses",
            routeIds = listOf("1925", "3411", "4049", "4050", "4244", "4246", "4249"),
            routePaths = listOf("files/geojson/Bus lines/164/route_1925.geojson", "files/geojson/Bus lines/164/route_3411.geojson", "files/geojson/Bus lines/164/route_4049.geojson", "files/geojson/Bus lines/164/route_4050.geojson", "files/geojson/Bus lines/164/route_4244.geojson", "files/geojson/Bus lines/164/route_4246.geojson", "files/geojson/Bus lines/164/route_4249.geojson")
        ),
        TransitLine(
            lineNumber = "171",
            category = "buses",
            routeIds = listOf("3897", "4015", "4018", "5372"),
            routePaths = listOf("files/geojson/Bus lines/171/route_3897.geojson", "files/geojson/Bus lines/171/route_4015.geojson", "files/geojson/Bus lines/171/route_4018.geojson", "files/geojson/Bus lines/171/route_5372.geojson")
        ),
        TransitLine(
            lineNumber = "201",
            category = "buses",
            routeIds = listOf("3567", "4280", "4281"),
            routePaths = listOf("files/geojson/Bus lines/201/route_3567.geojson", "files/geojson/Bus lines/201/route_4280.geojson", "files/geojson/Bus lines/201/route_4281.geojson")
        ),
        TransitLine(
            lineNumber = "203",
            category = "buses",
            routeIds = listOf("3427", "4982", "5694"),
            routePaths = listOf("files/geojson/Bus lines/203/route_3427.geojson", "files/geojson/Bus lines/203/route_4982.geojson", "files/geojson/Bus lines/203/route_5694.geojson")
        ),
        TransitLine(
            lineNumber = "204",
            category = "buses",
            routeIds = listOf("3428", "4264", "5695"),
            routePaths = listOf("files/geojson/Bus lines/204/route_3428.geojson", "files/geojson/Bus lines/204/route_4264.geojson", "files/geojson/Bus lines/204/route_5695.geojson")
        ),
        TransitLine(
            lineNumber = "204Β",
            category = "buses",
            routeIds = listOf("3430", "4265", "5696"),
            routePaths = listOf("files/geojson/Bus lines/204Β/route_3430.geojson", "files/geojson/Bus lines/204Β/route_4265.geojson", "files/geojson/Bus lines/204Β/route_5696.geojson")
        ),
        TransitLine(
            lineNumber = "205",
            category = "buses",
            routeIds = listOf("5181", "5182", "5184", "5185", "5482"),
            routePaths = listOf("files/geojson/Bus lines/205/route_5181.geojson", "files/geojson/Bus lines/205/route_5182.geojson", "files/geojson/Bus lines/205/route_5184.geojson", "files/geojson/Bus lines/205/route_5185.geojson", "files/geojson/Bus lines/205/route_5482.geojson")
        ),
        TransitLine(
            lineNumber = "206",
            category = "buses",
            routeIds = listOf("2556", "5314"),
            routePaths = listOf("files/geojson/Bus lines/206/route_2556.geojson", "files/geojson/Bus lines/206/route_5314.geojson")
        ),
        TransitLine(
            lineNumber = "209",
            category = "buses",
            routeIds = listOf("5120", "5121"),
            routePaths = listOf("files/geojson/Bus lines/209/route_5120.geojson", "files/geojson/Bus lines/209/route_5121.geojson")
        ),
        TransitLine(
            lineNumber = "211",
            category = "buses",
            routeIds = listOf("5122"),
            routePaths = listOf("files/geojson/Bus lines/211/route_5122.geojson")
        ),
        TransitLine(
            lineNumber = "212",
            category = "buses",
            routeIds = listOf("1951", "1952"),
            routePaths = listOf("files/geojson/Bus lines/212/route_1951.geojson", "files/geojson/Bus lines/212/route_1952.geojson")
        ),
        TransitLine(
            lineNumber = "214",
            category = "buses",
            routeIds = listOf("5044"),
            routePaths = listOf("files/geojson/Bus lines/214/route_5044.geojson")
        ),
        TransitLine(
            lineNumber = "217",
            category = "buses",
            routeIds = listOf("5515", "5516"),
            routePaths = listOf("files/geojson/Bus lines/217/route_5515.geojson", "files/geojson/Bus lines/217/route_5516.geojson")
        ),
        TransitLine(
            lineNumber = "218",
            category = "buses",
            routeIds = listOf("2034", "2810", "2899"),
            routePaths = listOf("files/geojson/Bus lines/218/route_2034.geojson", "files/geojson/Bus lines/218/route_2810.geojson", "files/geojson/Bus lines/218/route_2899.geojson")
        ),
        TransitLine(
            lineNumber = "219",
            category = "buses",
            routeIds = listOf("2215", "2809"),
            routePaths = listOf("files/geojson/Bus lines/219/route_2215.geojson", "files/geojson/Bus lines/219/route_2809.geojson")
        ),
        TransitLine(
            lineNumber = "220",
            category = "buses",
            routeIds = listOf("5369"),
            routePaths = listOf("files/geojson/Bus lines/220/route_5369.geojson")
        ),
        TransitLine(
            lineNumber = "221",
            category = "buses",
            routeIds = listOf("5370", "5371"),
            routePaths = listOf("files/geojson/Bus lines/221/route_5370.geojson", "files/geojson/Bus lines/221/route_5371.geojson")
        ),
        TransitLine(
            lineNumber = "224",
            category = "buses",
            routeIds = listOf("1754", "5045"),
            routePaths = listOf("files/geojson/Bus lines/224/route_1754.geojson", "files/geojson/Bus lines/224/route_5045.geojson")
        ),
        TransitLine(
            lineNumber = "227",
            category = "buses",
            routeIds = listOf("3593", "4783", "5454", "5455"),
            routePaths = listOf("files/geojson/Bus lines/227/route_3593.geojson", "files/geojson/Bus lines/227/route_4783.geojson", "files/geojson/Bus lines/227/route_5454.geojson", "files/geojson/Bus lines/227/route_5455.geojson")
        ),
        TransitLine(
            lineNumber = "229",
            category = "buses",
            routeIds = listOf("5523", "5536"),
            routePaths = listOf("files/geojson/Bus lines/229/route_5523.geojson", "files/geojson/Bus lines/229/route_5536.geojson")
        ),
        TransitLine(
            lineNumber = "230",
            category = "buses",
            routeIds = listOf("5647", "5648"),
            routePaths = listOf("files/geojson/Bus lines/230/route_5647.geojson", "files/geojson/Bus lines/230/route_5648.geojson")
        ),
        TransitLine(
            lineNumber = "235",
            category = "buses",
            routeIds = listOf("3608"),
            routePaths = listOf("files/geojson/Bus lines/235/route_3608.geojson")
        ),
        TransitLine(
            lineNumber = "237",
            category = "buses",
            routeIds = listOf("2557", "5315"),
            routePaths = listOf("files/geojson/Bus lines/237/route_2557.geojson", "files/geojson/Bus lines/237/route_5315.geojson")
        ),
        TransitLine(
            lineNumber = "242",
            category = "buses",
            routeIds = listOf("2120", "2267"),
            routePaths = listOf("files/geojson/Bus lines/242/route_2120.geojson", "files/geojson/Bus lines/242/route_2267.geojson")
        ),
        TransitLine(
            lineNumber = "250",
            category = "buses",
            routeIds = listOf("2108", "3388", "3818"),
            routePaths = listOf("files/geojson/Bus lines/250/route_2108.geojson", "files/geojson/Bus lines/250/route_3388.geojson", "files/geojson/Bus lines/250/route_3818.geojson")
        ),
        TransitLine(
            lineNumber = "300",
            category = "buses",
            routeIds = listOf("1898", "4284"),
            routePaths = listOf("files/geojson/Bus lines/300/route_1898.geojson", "files/geojson/Bus lines/300/route_4284.geojson")
        ),
        TransitLine(
            lineNumber = "301",
            category = "buses",
            routeIds = listOf("3587", "5585", "5586", "5697", "5698"),
            routePaths = listOf("files/geojson/Bus lines/301/route_3587.geojson", "files/geojson/Bus lines/301/route_5585.geojson", "files/geojson/Bus lines/301/route_5586.geojson", "files/geojson/Bus lines/301/route_5697.geojson", "files/geojson/Bus lines/301/route_5698.geojson")
        ),
        TransitLine(
            lineNumber = "301Β",
            category = "buses",
            routeIds = listOf("2544", "2545", "3267", "3275"),
            routePaths = listOf("files/geojson/Bus lines/301Β/route_2544.geojson", "files/geojson/Bus lines/301Β/route_2545.geojson", "files/geojson/Bus lines/301Β/route_3267.geojson", "files/geojson/Bus lines/301Β/route_3275.geojson")
        ),
        TransitLine(
            lineNumber = "302",
            category = "buses",
            routeIds = listOf("3588", "5633"),
            routePaths = listOf("files/geojson/Bus lines/302/route_3588.geojson", "files/geojson/Bus lines/302/route_5633.geojson")
        ),
        TransitLine(
            lineNumber = "304",
            category = "buses",
            routeIds = listOf("2055", "2056"),
            routePaths = listOf("files/geojson/Bus lines/304/route_2055.geojson", "files/geojson/Bus lines/304/route_2056.geojson")
        ),
        TransitLine(
            lineNumber = "305",
            category = "buses",
            routeIds = listOf("2036", "2037"),
            routePaths = listOf("files/geojson/Bus lines/305/route_2036.geojson", "files/geojson/Bus lines/305/route_2037.geojson")
        ),
        TransitLine(
            lineNumber = "306",
            category = "buses",
            routeIds = listOf("2427", "3592", "3946", "4142"),
            routePaths = listOf("files/geojson/Bus lines/306/route_2427.geojson", "files/geojson/Bus lines/306/route_3592.geojson", "files/geojson/Bus lines/306/route_3946.geojson", "files/geojson/Bus lines/306/route_4142.geojson")
        ),
        TransitLine(
            lineNumber = "307",
            category = "buses",
            routeIds = listOf("2513", "3984", "5069", "5071"),
            routePaths = listOf("files/geojson/Bus lines/307/route_2513.geojson", "files/geojson/Bus lines/307/route_3984.geojson", "files/geojson/Bus lines/307/route_5069.geojson", "files/geojson/Bus lines/307/route_5071.geojson")
        ),
        TransitLine(
            lineNumber = "308",
            category = "buses",
            routeIds = listOf("2059", "2060", "3974"),
            routePaths = listOf("files/geojson/Bus lines/308/route_2059.geojson", "files/geojson/Bus lines/308/route_2060.geojson", "files/geojson/Bus lines/308/route_3974.geojson")
        ),
        TransitLine(
            lineNumber = "309",
            category = "buses",
            routeIds = listOf("5053", "5054", "5335"),
            routePaths = listOf("files/geojson/Bus lines/309/route_5053.geojson", "files/geojson/Bus lines/309/route_5054.geojson", "files/geojson/Bus lines/309/route_5335.geojson")
        ),
        TransitLine(
            lineNumber = "309Β",
            category = "buses",
            routeIds = listOf("5559", "5560"),
            routePaths = listOf("files/geojson/Bus lines/309Β/route_5559.geojson", "files/geojson/Bus lines/309Β/route_5560.geojson")
        ),
        TransitLine(
            lineNumber = "310",
            category = "buses",
            routeIds = listOf("4855", "4861", "4901", "5686", "5687", "5688"),
            routePaths = listOf("files/geojson/Bus lines/310/route_4855.geojson", "files/geojson/Bus lines/310/route_4861.geojson", "files/geojson/Bus lines/310/route_4901.geojson", "files/geojson/Bus lines/310/route_5686.geojson", "files/geojson/Bus lines/310/route_5687.geojson", "files/geojson/Bus lines/310/route_5688.geojson")
        ),
        TransitLine(
            lineNumber = "311",
            category = "buses",
            routeIds = listOf("5366", "5397", "5398"),
            routePaths = listOf("files/geojson/Bus lines/311/route_5366.geojson", "files/geojson/Bus lines/311/route_5397.geojson", "files/geojson/Bus lines/311/route_5398.geojson")
        ),
        TransitLine(
            lineNumber = "314",
            category = "buses",
            routeIds = listOf("3100", "3103", "4462", "4463", "4714", "4715", "4824", "4825", "4872", "4873", "5396", "5476"),
            routePaths = listOf("files/geojson/Bus lines/314/route_3100.geojson", "files/geojson/Bus lines/314/route_3103.geojson", "files/geojson/Bus lines/314/route_4462.geojson", "files/geojson/Bus lines/314/route_4463.geojson", "files/geojson/Bus lines/314/route_4714.geojson", "files/geojson/Bus lines/314/route_4715.geojson", "files/geojson/Bus lines/314/route_4824.geojson", "files/geojson/Bus lines/314/route_4825.geojson", "files/geojson/Bus lines/314/route_4872.geojson", "files/geojson/Bus lines/314/route_4873.geojson", "files/geojson/Bus lines/314/route_5396.geojson", "files/geojson/Bus lines/314/route_5476.geojson")
        ),
        TransitLine(
            lineNumber = "316",
            category = "buses",
            routeIds = listOf("2292", "5301"),
            routePaths = listOf("files/geojson/Bus lines/316/route_2292.geojson", "files/geojson/Bus lines/316/route_5301.geojson")
        ),
        TransitLine(
            lineNumber = "319",
            category = "buses",
            routeIds = listOf("3620", "3920", "5127", "5128", "5612"),
            routePaths = listOf("files/geojson/Bus lines/319/route_3620.geojson", "files/geojson/Bus lines/319/route_3920.geojson", "files/geojson/Bus lines/319/route_5127.geojson", "files/geojson/Bus lines/319/route_5128.geojson", "files/geojson/Bus lines/319/route_5612.geojson")
        ),
        TransitLine(
            lineNumber = "323",
            category = "buses",
            routeIds = listOf("3342"),
            routePaths = listOf("files/geojson/Bus lines/323/route_3342.geojson")
        ),
        TransitLine(
            lineNumber = "324",
            category = "buses",
            routeIds = listOf("5701"),
            routePaths = listOf("files/geojson/Bus lines/324/route_5701.geojson")
        ),
        TransitLine(
            lineNumber = "326",
            category = "buses",
            routeIds = listOf("2770"),
            routePaths = listOf("files/geojson/Bus lines/326/route_2770.geojson")
        ),
        TransitLine(
            lineNumber = "330",
            category = "buses",
            routeIds = listOf("5052", "5572"),
            routePaths = listOf("files/geojson/Bus lines/330/route_5052.geojson", "files/geojson/Bus lines/330/route_5572.geojson")
        ),
        TransitLine(
            lineNumber = "400",
            category = "buses",
            routeIds = listOf("4939", "5202", "5231", "5232"),
            routePaths = listOf("files/geojson/Bus lines/400/route_4939.geojson", "files/geojson/Bus lines/400/route_5202.geojson", "files/geojson/Bus lines/400/route_5231.geojson", "files/geojson/Bus lines/400/route_5232.geojson")
        ),
        TransitLine(
            lineNumber = "402",
            category = "buses",
            routeIds = listOf("2068", "2069"),
            routePaths = listOf("files/geojson/Bus lines/402/route_2068.geojson", "files/geojson/Bus lines/402/route_2069.geojson")
        ),
        TransitLine(
            lineNumber = "405",
            category = "buses",
            routeIds = listOf("2617", "3711", "3713"),
            routePaths = listOf("files/geojson/Bus lines/405/route_2617.geojson", "files/geojson/Bus lines/405/route_3711.geojson", "files/geojson/Bus lines/405/route_3713.geojson")
        ),
        TransitLine(
            lineNumber = "406",
            category = "buses",
            routeIds = listOf("4666", "4667"),
            routePaths = listOf("files/geojson/Bus lines/406/route_4666.geojson", "files/geojson/Bus lines/406/route_4667.geojson")
        ),
        TransitLine(
            lineNumber = "407",
            category = "buses",
            routeIds = listOf("3252", "3254"),
            routePaths = listOf("files/geojson/Bus lines/407/route_3252.geojson", "files/geojson/Bus lines/407/route_3254.geojson")
        ),
        TransitLine(
            lineNumber = "409",
            category = "buses",
            routeIds = listOf("4705", "4712"),
            routePaths = listOf("files/geojson/Bus lines/409/route_4705.geojson", "files/geojson/Bus lines/409/route_4712.geojson")
        ),
        TransitLine(
            lineNumber = "410",
            category = "buses",
            routeIds = listOf("1963", "2615", "3394", "3456", "3457"),
            routePaths = listOf("files/geojson/Bus lines/410/route_1963.geojson", "files/geojson/Bus lines/410/route_2615.geojson", "files/geojson/Bus lines/410/route_3394.geojson", "files/geojson/Bus lines/410/route_3456.geojson", "files/geojson/Bus lines/410/route_3457.geojson")
        ),
        TransitLine(
            lineNumber = "411",
            category = "buses",
            routeIds = listOf("2326", "2835", "4167", "4209"),
            routePaths = listOf("files/geojson/Bus lines/411/route_2326.geojson", "files/geojson/Bus lines/411/route_2835.geojson", "files/geojson/Bus lines/411/route_4167.geojson", "files/geojson/Bus lines/411/route_4209.geojson")
        ),
        TransitLine(
            lineNumber = "416",
            category = "buses",
            routeIds = listOf("2847", "4290", "4291", "4292"),
            routePaths = listOf("files/geojson/Bus lines/416/route_2847.geojson", "files/geojson/Bus lines/416/route_4290.geojson", "files/geojson/Bus lines/416/route_4291.geojson", "files/geojson/Bus lines/416/route_4292.geojson")
        ),
        TransitLine(
            lineNumber = "418",
            category = "buses",
            routeIds = listOf("2848", "2849"),
            routePaths = listOf("files/geojson/Bus lines/418/route_2848.geojson", "files/geojson/Bus lines/418/route_2849.geojson")
        ),
        TransitLine(
            lineNumber = "420",
            category = "buses",
            routeIds = listOf("5502", "5672"),
            routePaths = listOf("files/geojson/Bus lines/420/route_5502.geojson", "files/geojson/Bus lines/420/route_5672.geojson")
        ),
        TransitLine(
            lineNumber = "421",
            category = "buses",
            routeIds = listOf("4508", "4577"),
            routePaths = listOf("files/geojson/Bus lines/421/route_4508.geojson", "files/geojson/Bus lines/421/route_4577.geojson")
        ),
        TransitLine(
            lineNumber = "444",
            category = "buses",
            routeIds = listOf("4909", "4910"),
            routePaths = listOf("files/geojson/Bus lines/444/route_4909.geojson", "files/geojson/Bus lines/444/route_4910.geojson")
        ),
        TransitLine(
            lineNumber = "446",
            category = "buses",
            routeIds = listOf("2169", "2170"),
            routePaths = listOf("files/geojson/Bus lines/446/route_2169.geojson", "files/geojson/Bus lines/446/route_2170.geojson")
        ),
        TransitLine(
            lineNumber = "447",
            category = "buses",
            routeIds = listOf("1986", "2838", "3446"),
            routePaths = listOf("files/geojson/Bus lines/447/route_1986.geojson", "files/geojson/Bus lines/447/route_2838.geojson", "files/geojson/Bus lines/447/route_3446.geojson")
        ),
        TransitLine(
            lineNumber = "450",
            category = "buses",
            routeIds = listOf("2072", "2073", "3907"),
            routePaths = listOf("files/geojson/Bus lines/450/route_2072.geojson", "files/geojson/Bus lines/450/route_2073.geojson", "files/geojson/Bus lines/450/route_3907.geojson")
        ),
        TransitLine(
            lineNumber = "451",
            category = "buses",
            routeIds = listOf("2840", "3647", "3648", "3908"),
            routePaths = listOf("files/geojson/Bus lines/451/route_2840.geojson", "files/geojson/Bus lines/451/route_3647.geojson", "files/geojson/Bus lines/451/route_3648.geojson", "files/geojson/Bus lines/451/route_3908.geojson")
        ),
        TransitLine(
            lineNumber = "460",
            category = "buses",
            routeIds = listOf("2127", "2128"),
            routePaths = listOf("files/geojson/Bus lines/460/route_2127.geojson", "files/geojson/Bus lines/460/route_2128.geojson")
        ),
        TransitLine(
            lineNumber = "461",
            category = "buses",
            routeIds = listOf("2173", "2797", "3649", "3650"),
            routePaths = listOf("files/geojson/Bus lines/461/route_2173.geojson", "files/geojson/Bus lines/461/route_2797.geojson", "files/geojson/Bus lines/461/route_3649.geojson", "files/geojson/Bus lines/461/route_3650.geojson")
        ),
        TransitLine(
            lineNumber = "500",
            category = "buses",
            routeIds = listOf("1889", "1890", "2783", "2900"),
            routePaths = listOf("files/geojson/Bus lines/500/route_1889.geojson", "files/geojson/Bus lines/500/route_1890.geojson", "files/geojson/Bus lines/500/route_2783.geojson", "files/geojson/Bus lines/500/route_2900.geojson")
        ),
        TransitLine(
            lineNumber = "501",
            category = "buses",
            routeIds = listOf("2229"),
            routePaths = listOf("files/geojson/Bus lines/501/route_2229.geojson")
        ),
        TransitLine(
            lineNumber = "503",
            category = "buses",
            routeIds = listOf("2188", "4273", "4470"),
            routePaths = listOf("files/geojson/Bus lines/503/route_2188.geojson", "files/geojson/Bus lines/503/route_4273.geojson", "files/geojson/Bus lines/503/route_4470.geojson")
        ),
        TransitLine(
            lineNumber = "504",
            category = "buses",
            routeIds = listOf("4240"),
            routePaths = listOf("files/geojson/Bus lines/504/route_4240.geojson")
        ),
        TransitLine(
            lineNumber = "507",
            category = "buses",
            routeIds = listOf("1968", "1969"),
            routePaths = listOf("files/geojson/Bus lines/507/route_1968.geojson", "files/geojson/Bus lines/507/route_1969.geojson")
        ),
        TransitLine(
            lineNumber = "509",
            category = "buses",
            routeIds = listOf("4706", "4707", "4708", "4955", "4957", "4958"),
            routePaths = listOf("files/geojson/Bus lines/509/route_4706.geojson", "files/geojson/Bus lines/509/route_4707.geojson", "files/geojson/Bus lines/509/route_4708.geojson", "files/geojson/Bus lines/509/route_4955.geojson", "files/geojson/Bus lines/509/route_4957.geojson", "files/geojson/Bus lines/509/route_4958.geojson")
        ),
        TransitLine(
            lineNumber = "509Β",
            category = "buses",
            routeIds = listOf("5613", "5614"),
            routePaths = listOf("files/geojson/Bus lines/509Β/route_5613.geojson", "files/geojson/Bus lines/509Β/route_5614.geojson")
        ),
        TransitLine(
            lineNumber = "522",
            category = "buses",
            routeIds = listOf("5391", "5392"),
            routePaths = listOf("files/geojson/Bus lines/522/route_5391.geojson", "files/geojson/Bus lines/522/route_5392.geojson")
        ),
        TransitLine(
            lineNumber = "523",
            category = "buses",
            routeIds = listOf("2740", "2994"),
            routePaths = listOf("files/geojson/Bus lines/523/route_2740.geojson", "files/geojson/Bus lines/523/route_2994.geojson")
        ),
        TransitLine(
            lineNumber = "524",
            category = "buses",
            routeIds = listOf("2289"),
            routePaths = listOf("files/geojson/Bus lines/524/route_2289.geojson")
        ),
        TransitLine(
            lineNumber = "526",
            category = "buses",
            routeIds = listOf("4741"),
            routePaths = listOf("files/geojson/Bus lines/526/route_4741.geojson")
        ),
        TransitLine(
            lineNumber = "527",
            category = "buses",
            routeIds = listOf("2230"),
            routePaths = listOf("files/geojson/Bus lines/527/route_2230.geojson")
        ),
        TransitLine(
            lineNumber = "530",
            category = "buses",
            routeIds = listOf("3589", "3590", "3591", "3909", "3910"),
            routePaths = listOf("files/geojson/Bus lines/530/route_3589.geojson", "files/geojson/Bus lines/530/route_3590.geojson", "files/geojson/Bus lines/530/route_3591.geojson", "files/geojson/Bus lines/530/route_3909.geojson", "files/geojson/Bus lines/530/route_3910.geojson")
        ),
        TransitLine(
            lineNumber = "535",
            category = "buses",
            routeIds = listOf("4489"),
            routePaths = listOf("files/geojson/Bus lines/535/route_4489.geojson")
        ),
        TransitLine(
            lineNumber = "535Α",
            category = "buses",
            routeIds = listOf("5491"),
            routePaths = listOf("files/geojson/Bus lines/535Α/route_5491.geojson")
        ),
        TransitLine(
            lineNumber = "536",
            category = "buses",
            routeIds = listOf("4196", "4197"),
            routePaths = listOf("files/geojson/Bus lines/536/route_4196.geojson", "files/geojson/Bus lines/536/route_4197.geojson")
        ),
        TransitLine(
            lineNumber = "537",
            category = "buses",
            routeIds = listOf("4537", "4559", "4927", "4928"),
            routePaths = listOf("files/geojson/Bus lines/537/route_4537.geojson", "files/geojson/Bus lines/537/route_4559.geojson", "files/geojson/Bus lines/537/route_4927.geojson", "files/geojson/Bus lines/537/route_4928.geojson")
        ),
        TransitLine(
            lineNumber = "541",
            category = "buses",
            routeIds = listOf("2117"),
            routePaths = listOf("files/geojson/Bus lines/541/route_2117.geojson")
        ),
        TransitLine(
            lineNumber = "543",
            category = "buses",
            routeIds = listOf("5434"),
            routePaths = listOf("files/geojson/Bus lines/543/route_5434.geojson")
        ),
        TransitLine(
            lineNumber = "543Α",
            category = "buses",
            routeIds = listOf("5435"),
            routePaths = listOf("files/geojson/Bus lines/543Α/route_5435.geojson")
        ),
        TransitLine(
            lineNumber = "550",
            category = "buses",
            routeIds = listOf("2951", "4373"),
            routePaths = listOf("files/geojson/Bus lines/550/route_2951.geojson", "files/geojson/Bus lines/550/route_4373.geojson")
        ),
        TransitLine(
            lineNumber = "560",
            category = "buses",
            routeIds = listOf("1829", "1830"),
            routePaths = listOf("files/geojson/Bus lines/560/route_1829.geojson", "files/geojson/Bus lines/560/route_1830.geojson")
        ),
        TransitLine(
            lineNumber = "602",
            category = "buses",
            routeIds = listOf("2143", "4186", "4187", "4654"),
            routePaths = listOf("files/geojson/Bus lines/602/route_2143.geojson", "files/geojson/Bus lines/602/route_4186.geojson", "files/geojson/Bus lines/602/route_4187.geojson", "files/geojson/Bus lines/602/route_4654.geojson")
        ),
        TransitLine(
            lineNumber = "604",
            category = "buses",
            routeIds = listOf("5364"),
            routePaths = listOf("files/geojson/Bus lines/604/route_5364.geojson")
        ),
        TransitLine(
            lineNumber = "605",
            category = "buses",
            routeIds = listOf("1806", "2509"),
            routePaths = listOf("files/geojson/Bus lines/605/route_1806.geojson", "files/geojson/Bus lines/605/route_2509.geojson")
        ),
        TransitLine(
            lineNumber = "608",
            category = "buses",
            routeIds = listOf("4178", "4906", "4907", "4908", "5346"),
            routePaths = listOf("files/geojson/Bus lines/608/route_4178.geojson", "files/geojson/Bus lines/608/route_4906.geojson", "files/geojson/Bus lines/608/route_4907.geojson", "files/geojson/Bus lines/608/route_4908.geojson", "files/geojson/Bus lines/608/route_5346.geojson")
        ),
        TransitLine(
            lineNumber = "610",
            category = "buses",
            routeIds = listOf("2144", "4364", "4365"),
            routePaths = listOf("files/geojson/Bus lines/610/route_2144.geojson", "files/geojson/Bus lines/610/route_4364.geojson", "files/geojson/Bus lines/610/route_4365.geojson")
        ),
        TransitLine(
            lineNumber = "619",
            category = "buses",
            routeIds = listOf("2219"),
            routePaths = listOf("files/geojson/Bus lines/619/route_2219.geojson")
        ),
        TransitLine(
            lineNumber = "622",
            category = "buses",
            routeIds = listOf("5432", "5645"),
            routePaths = listOf("files/geojson/Bus lines/622/route_5432.geojson", "files/geojson/Bus lines/622/route_5645.geojson")
        ),
        TransitLine(
            lineNumber = "640",
            category = "buses",
            routeIds = listOf("2362", "3198"),
            routePaths = listOf("files/geojson/Bus lines/640/route_2362.geojson", "files/geojson/Bus lines/640/route_3198.geojson")
        ),
        TransitLine(
            lineNumber = "642",
            category = "buses",
            routeIds = listOf("2182", "4298"),
            routePaths = listOf("files/geojson/Bus lines/642/route_2182.geojson", "files/geojson/Bus lines/642/route_4298.geojson")
        ),
        TransitLine(
            lineNumber = "651",
            category = "buses",
            routeIds = listOf("4368", "4947"),
            routePaths = listOf("files/geojson/Bus lines/651/route_4368.geojson", "files/geojson/Bus lines/651/route_4947.geojson")
        ),
        TransitLine(
            lineNumber = "653",
            category = "buses",
            routeIds = listOf("2157", "4159", "4300"),
            routePaths = listOf("files/geojson/Bus lines/653/route_2157.geojson", "files/geojson/Bus lines/653/route_4159.geojson", "files/geojson/Bus lines/653/route_4300.geojson")
        ),
        TransitLine(
            lineNumber = "700",
            category = "buses",
            routeIds = listOf("4370"),
            routePaths = listOf("files/geojson/Bus lines/700/route_4370.geojson")
        ),
        TransitLine(
            lineNumber = "701",
            category = "buses",
            routeIds = listOf("4969"),
            routePaths = listOf("files/geojson/Bus lines/701/route_4969.geojson")
        ),
        TransitLine(
            lineNumber = "702",
            category = "buses",
            routeIds = listOf("2295", "3917"),
            routePaths = listOf("files/geojson/Bus lines/702/route_2295.geojson", "files/geojson/Bus lines/702/route_3917.geojson")
        ),
        TransitLine(
            lineNumber = "703",
            category = "buses",
            routeIds = listOf("3794", "5229", "5578", "5579"),
            routePaths = listOf("files/geojson/Bus lines/703/route_3794.geojson", "files/geojson/Bus lines/703/route_5229.geojson", "files/geojson/Bus lines/703/route_5578.geojson", "files/geojson/Bus lines/703/route_5579.geojson")
        ),
        TransitLine(
            lineNumber = "704",
            category = "buses",
            routeIds = listOf("5175"),
            routePaths = listOf("files/geojson/Bus lines/704/route_5175.geojson")
        ),
        TransitLine(
            lineNumber = "705",
            category = "buses",
            routeIds = listOf("4970"),
            routePaths = listOf("files/geojson/Bus lines/705/route_4970.geojson")
        ),
        TransitLine(
            lineNumber = "706",
            category = "buses",
            routeIds = listOf("5058", "5059", "5060"),
            routePaths = listOf("files/geojson/Bus lines/706/route_5058.geojson", "files/geojson/Bus lines/706/route_5059.geojson", "files/geojson/Bus lines/706/route_5060.geojson")
        ),
        TransitLine(
            lineNumber = "709",
            category = "buses",
            routeIds = listOf("3385"),
            routePaths = listOf("files/geojson/Bus lines/709/route_3385.geojson")
        ),
        TransitLine(
            lineNumber = "711",
            category = "buses",
            routeIds = listOf("3489", "3490"),
            routePaths = listOf("files/geojson/Bus lines/711/route_3489.geojson", "files/geojson/Bus lines/711/route_3490.geojson")
        ),
        TransitLine(
            lineNumber = "712",
            category = "buses",
            routeIds = listOf("5211", "5362"),
            routePaths = listOf("files/geojson/Bus lines/712/route_5211.geojson", "files/geojson/Bus lines/712/route_5362.geojson")
        ),
        TransitLine(
            lineNumber = "719",
            category = "buses",
            routeIds = listOf("5673"),
            routePaths = listOf("files/geojson/Bus lines/719/route_5673.geojson")
        ),
        TransitLine(
            lineNumber = "720",
            category = "buses",
            routeIds = listOf("5356", "5357"),
            routePaths = listOf("files/geojson/Bus lines/720/route_5356.geojson", "files/geojson/Bus lines/720/route_5357.geojson")
        ),
        TransitLine(
            lineNumber = "721",
            category = "buses",
            routeIds = listOf("4879"),
            routePaths = listOf("files/geojson/Bus lines/721/route_4879.geojson")
        ),
        TransitLine(
            lineNumber = "723",
            category = "buses",
            routeIds = listOf("5689"),
            routePaths = listOf("files/geojson/Bus lines/723/route_5689.geojson")
        ),
        TransitLine(
            lineNumber = "724",
            category = "buses",
            routeIds = listOf("2447"),
            routePaths = listOf("files/geojson/Bus lines/724/route_2447.geojson")
        ),
        TransitLine(
            lineNumber = "725",
            category = "buses",
            routeIds = listOf("4328", "4358"),
            routePaths = listOf("files/geojson/Bus lines/725/route_4328.geojson", "files/geojson/Bus lines/725/route_4358.geojson")
        ),
        TransitLine(
            lineNumber = "726",
            category = "buses",
            routeIds = listOf("2654"),
            routePaths = listOf("files/geojson/Bus lines/726/route_2654.geojson")
        ),
        TransitLine(
            lineNumber = "727",
            category = "buses",
            routeIds = listOf("2477", "4036", "4897"),
            routePaths = listOf("files/geojson/Bus lines/727/route_2477.geojson", "files/geojson/Bus lines/727/route_4036.geojson", "files/geojson/Bus lines/727/route_4897.geojson")
        ),
        TransitLine(
            lineNumber = "728",
            category = "buses",
            routeIds = listOf("4401", "4402", "4403", "4409", "4530"),
            routePaths = listOf("files/geojson/Bus lines/728/route_4401.geojson", "files/geojson/Bus lines/728/route_4402.geojson", "files/geojson/Bus lines/728/route_4403.geojson", "files/geojson/Bus lines/728/route_4409.geojson", "files/geojson/Bus lines/728/route_4530.geojson")
        ),
        TransitLine(
            lineNumber = "730",
            category = "buses",
            routeIds = listOf("2878", "4305", "5463", "5464"),
            routePaths = listOf("files/geojson/Bus lines/730/route_2878.geojson", "files/geojson/Bus lines/730/route_4305.geojson", "files/geojson/Bus lines/730/route_5463.geojson", "files/geojson/Bus lines/730/route_5464.geojson")
        ),
        TransitLine(
            lineNumber = "731",
            category = "buses",
            routeIds = listOf("1851", "2445", "3468", "4662"),
            routePaths = listOf("files/geojson/Bus lines/731/route_1851.geojson", "files/geojson/Bus lines/731/route_2445.geojson", "files/geojson/Bus lines/731/route_3468.geojson", "files/geojson/Bus lines/731/route_4662.geojson")
        ),
        TransitLine(
            lineNumber = "732",
            category = "buses",
            routeIds = listOf("4361", "4362"),
            routePaths = listOf("files/geojson/Bus lines/732/route_4361.geojson", "files/geojson/Bus lines/732/route_4362.geojson")
        ),
        TransitLine(
            lineNumber = "733",
            category = "buses",
            routeIds = listOf("2226"),
            routePaths = listOf("files/geojson/Bus lines/733/route_2226.geojson")
        ),
        TransitLine(
            lineNumber = "734",
            category = "buses",
            routeIds = listOf("5094"),
            routePaths = listOf("files/geojson/Bus lines/734/route_5094.geojson")
        ),
        TransitLine(
            lineNumber = "735",
            category = "buses",
            routeIds = listOf("5131"),
            routePaths = listOf("files/geojson/Bus lines/735/route_5131.geojson")
        ),
        TransitLine(
            lineNumber = "740",
            category = "buses",
            routeIds = listOf("3241", "3245", "3259"),
            routePaths = listOf("files/geojson/Bus lines/740/route_3241.geojson", "files/geojson/Bus lines/740/route_3245.geojson", "files/geojson/Bus lines/740/route_3259.geojson")
        ),
        TransitLine(
            lineNumber = "747",
            category = "buses",
            routeIds = listOf("4329", "4971", "4972"),
            routePaths = listOf("files/geojson/Bus lines/747/route_4329.geojson", "files/geojson/Bus lines/747/route_4971.geojson", "files/geojson/Bus lines/747/route_4972.geojson")
        ),
        TransitLine(
            lineNumber = "748",
            category = "buses",
            routeIds = listOf("1828", "3338", "3486"),
            routePaths = listOf("files/geojson/Bus lines/748/route_1828.geojson", "files/geojson/Bus lines/748/route_3338.geojson", "files/geojson/Bus lines/748/route_3486.geojson")
        ),
        TransitLine(
            lineNumber = "749",
            category = "buses",
            routeIds = listOf("4414", "4415"),
            routePaths = listOf("files/geojson/Bus lines/749/route_4414.geojson", "files/geojson/Bus lines/749/route_4415.geojson")
        ),
        TransitLine(
            lineNumber = "750",
            category = "buses",
            routeIds = listOf("1899", "3463", "3630", "3631", "3632", "4551", "4552"),
            routePaths = listOf("files/geojson/Bus lines/750/route_1899.geojson", "files/geojson/Bus lines/750/route_3463.geojson", "files/geojson/Bus lines/750/route_3630.geojson", "files/geojson/Bus lines/750/route_3631.geojson", "files/geojson/Bus lines/750/route_3632.geojson", "files/geojson/Bus lines/750/route_4551.geojson", "files/geojson/Bus lines/750/route_4552.geojson")
        ),
        TransitLine(
            lineNumber = "752",
            category = "buses",
            routeIds = listOf("3378", "3379", "3876", "4880"),
            routePaths = listOf("files/geojson/Bus lines/752/route_3378.geojson", "files/geojson/Bus lines/752/route_3379.geojson", "files/geojson/Bus lines/752/route_3876.geojson", "files/geojson/Bus lines/752/route_4880.geojson")
        ),
        TransitLine(
            lineNumber = "755",
            category = "buses",
            routeIds = listOf("4512", "4513"),
            routePaths = listOf("files/geojson/Bus lines/755/route_4512.geojson", "files/geojson/Bus lines/755/route_4513.geojson")
        ),
        TransitLine(
            lineNumber = "755Β",
            category = "buses",
            routeIds = listOf("4514", "4515"),
            routePaths = listOf("files/geojson/Bus lines/755Β/route_4514.geojson", "files/geojson/Bus lines/755Β/route_4515.geojson")
        ),
        TransitLine(
            lineNumber = "790",
            category = "buses",
            routeIds = listOf("5134", "5135"),
            routePaths = listOf("files/geojson/Bus lines/790/route_5134.geojson", "files/geojson/Bus lines/790/route_5135.geojson")
        ),
        TransitLine(
            lineNumber = "800",
            category = "buses",
            routeIds = listOf("4731"),
            routePaths = listOf("files/geojson/Bus lines/800/route_4731.geojson")
        ),
        TransitLine(
            lineNumber = "801",
            category = "buses",
            routeIds = listOf("5074"),
            routePaths = listOf("files/geojson/Bus lines/801/route_5074.geojson")
        ),
        TransitLine(
            lineNumber = "803",
            category = "buses",
            routeIds = listOf("3345", "3347", "3477", "3478", "4663"),
            routePaths = listOf("files/geojson/Bus lines/803/route_3345.geojson", "files/geojson/Bus lines/803/route_3347.geojson", "files/geojson/Bus lines/803/route_3477.geojson", "files/geojson/Bus lines/803/route_3478.geojson", "files/geojson/Bus lines/803/route_4663.geojson")
        ),
        TransitLine(
            lineNumber = "805",
            category = "buses",
            routeIds = listOf("3849", "4603", "4935"),
            routePaths = listOf("files/geojson/Bus lines/805/route_3849.geojson", "files/geojson/Bus lines/805/route_4603.geojson", "files/geojson/Bus lines/805/route_4935.geojson")
        ),
        TransitLine(
            lineNumber = "806",
            category = "buses",
            routeIds = listOf("4307", "4822"),
            routePaths = listOf("files/geojson/Bus lines/806/route_4307.geojson", "files/geojson/Bus lines/806/route_4822.geojson")
        ),
        TransitLine(
            lineNumber = "807",
            category = "buses",
            routeIds = listOf("4820"),
            routePaths = listOf("files/geojson/Bus lines/807/route_4820.geojson")
        ),
        TransitLine(
            lineNumber = "809",
            category = "buses",
            routeIds = listOf("4314", "4823"),
            routePaths = listOf("files/geojson/Bus lines/809/route_4314.geojson", "files/geojson/Bus lines/809/route_4823.geojson")
        ),
        TransitLine(
            lineNumber = "810",
            category = "buses",
            routeIds = listOf("4844", "4918"),
            routePaths = listOf("files/geojson/Bus lines/810/route_4844.geojson", "files/geojson/Bus lines/810/route_4918.geojson")
        ),
        TransitLine(
            lineNumber = "811",
            category = "buses",
            routeIds = listOf("4863", "4864", "4865", "4866"),
            routePaths = listOf("files/geojson/Bus lines/811/route_4863.geojson", "files/geojson/Bus lines/811/route_4864.geojson", "files/geojson/Bus lines/811/route_4865.geojson", "files/geojson/Bus lines/811/route_4866.geojson")
        ),
        TransitLine(
            lineNumber = "813",
            category = "buses",
            routeIds = listOf("1770", "1771"),
            routePaths = listOf("files/geojson/Bus lines/813/route_1770.geojson", "files/geojson/Bus lines/813/route_1771.geojson")
        ),
        TransitLine(
            lineNumber = "814",
            category = "buses",
            routeIds = listOf("4481", "4482", "5574"),
            routePaths = listOf("files/geojson/Bus lines/814/route_4481.geojson", "files/geojson/Bus lines/814/route_4482.geojson", "files/geojson/Bus lines/814/route_5574.geojson")
        ),
        TransitLine(
            lineNumber = "815",
            category = "buses",
            routeIds = listOf("5433", "5646"),
            routePaths = listOf("files/geojson/Bus lines/815/route_5433.geojson", "files/geojson/Bus lines/815/route_5646.geojson")
        ),
        TransitLine(
            lineNumber = "816",
            category = "buses",
            routeIds = listOf("2228", "2811"),
            routePaths = listOf("files/geojson/Bus lines/816/route_2228.geojson", "files/geojson/Bus lines/816/route_2811.geojson")
        ),
        TransitLine(
            lineNumber = "817",
            category = "buses",
            routeIds = listOf("3040", "3041"),
            routePaths = listOf("files/geojson/Bus lines/817/route_3040.geojson", "files/geojson/Bus lines/817/route_3041.geojson")
        ),
        TransitLine(
            lineNumber = "818",
            category = "buses",
            routeIds = listOf("2000", "4341"),
            routePaths = listOf("files/geojson/Bus lines/818/route_2000.geojson", "files/geojson/Bus lines/818/route_4341.geojson")
        ),
        TransitLine(
            lineNumber = "819",
            category = "buses",
            routeIds = listOf("4000", "4023", "5150"),
            routePaths = listOf("files/geojson/Bus lines/819/route_4000.geojson", "files/geojson/Bus lines/819/route_4023.geojson", "files/geojson/Bus lines/819/route_5150.geojson")
        ),
        TransitLine(
            lineNumber = "820",
            category = "buses",
            routeIds = listOf("3576"),
            routePaths = listOf("files/geojson/Bus lines/820/route_3576.geojson")
        ),
        TransitLine(
            lineNumber = "821",
            category = "buses",
            routeIds = listOf("1916"),
            routePaths = listOf("files/geojson/Bus lines/821/route_1916.geojson")
        ),
        TransitLine(
            lineNumber = "822",
            category = "buses",
            routeIds = listOf("2712", "4930", "4931", "4932"),
            routePaths = listOf("files/geojson/Bus lines/822/route_2712.geojson", "files/geojson/Bus lines/822/route_4930.geojson", "files/geojson/Bus lines/822/route_4931.geojson", "files/geojson/Bus lines/822/route_4932.geojson")
        ),
        TransitLine(
            lineNumber = "823",
            category = "buses",
            routeIds = listOf("4934", "5399"),
            routePaths = listOf("files/geojson/Bus lines/823/route_4934.geojson", "files/geojson/Bus lines/823/route_5399.geojson")
        ),
        TransitLine(
            lineNumber = "824",
            category = "buses",
            routeIds = listOf("1891", "3515", "4344"),
            routePaths = listOf("files/geojson/Bus lines/824/route_1891.geojson", "files/geojson/Bus lines/824/route_3515.geojson", "files/geojson/Bus lines/824/route_4344.geojson")
        ),
        TransitLine(
            lineNumber = "825",
            category = "buses",
            routeIds = listOf("4625", "4627", "5192"),
            routePaths = listOf("files/geojson/Bus lines/825/route_4625.geojson", "files/geojson/Bus lines/825/route_4627.geojson", "files/geojson/Bus lines/825/route_5192.geojson")
        ),
        TransitLine(
            lineNumber = "826",
            category = "buses",
            routeIds = listOf("3586", "4345", "4346", "4597"),
            routePaths = listOf("files/geojson/Bus lines/826/route_3586.geojson", "files/geojson/Bus lines/826/route_4345.geojson", "files/geojson/Bus lines/826/route_4346.geojson", "files/geojson/Bus lines/826/route_4597.geojson")
        ),
        TransitLine(
            lineNumber = "827",
            category = "buses",
            routeIds = listOf("5080"),
            routePaths = listOf("files/geojson/Bus lines/827/route_5080.geojson")
        ),
        TransitLine(
            lineNumber = "828",
            category = "buses",
            routeIds = listOf("4367"),
            routePaths = listOf("files/geojson/Bus lines/828/route_4367.geojson")
        ),
        TransitLine(
            lineNumber = "829",
            category = "buses",
            routeIds = listOf("4583", "5503", "5504", "5570"),
            routePaths = listOf("files/geojson/Bus lines/829/route_4583.geojson", "files/geojson/Bus lines/829/route_5503.geojson", "files/geojson/Bus lines/829/route_5504.geojson", "files/geojson/Bus lines/829/route_5570.geojson")
        ),
        TransitLine(
            lineNumber = "830",
            category = "buses",
            routeIds = listOf("4475", "4518", "4519"),
            routePaths = listOf("files/geojson/Bus lines/830/route_4475.geojson", "files/geojson/Bus lines/830/route_4518.geojson", "files/geojson/Bus lines/830/route_4519.geojson")
        ),
        TransitLine(
            lineNumber = "831",
            category = "buses",
            routeIds = listOf("1886", "2805", "3636", "3637", "4527", "4528"),
            routePaths = listOf("files/geojson/Bus lines/831/route_1886.geojson", "files/geojson/Bus lines/831/route_2805.geojson", "files/geojson/Bus lines/831/route_3636.geojson", "files/geojson/Bus lines/831/route_3637.geojson", "files/geojson/Bus lines/831/route_4527.geojson", "files/geojson/Bus lines/831/route_4528.geojson")
        ),
        TransitLine(
            lineNumber = "832",
            category = "buses",
            routeIds = listOf("2458", "2646"),
            routePaths = listOf("files/geojson/Bus lines/832/route_2458.geojson", "files/geojson/Bus lines/832/route_2646.geojson")
        ),
        TransitLine(
            lineNumber = "833",
            category = "buses",
            routeIds = listOf("2644", "2758"),
            routePaths = listOf("files/geojson/Bus lines/833/route_2644.geojson", "files/geojson/Bus lines/833/route_2758.geojson")
        ),
        TransitLine(
            lineNumber = "836",
            category = "buses",
            routeIds = listOf("5496", "5497"),
            routePaths = listOf("files/geojson/Bus lines/836/route_5496.geojson", "files/geojson/Bus lines/836/route_5497.geojson")
        ),
        TransitLine(
            lineNumber = "837",
            category = "buses",
            routeIds = listOf("3582", "4296", "4526"),
            routePaths = listOf("files/geojson/Bus lines/837/route_3582.geojson", "files/geojson/Bus lines/837/route_4296.geojson", "files/geojson/Bus lines/837/route_4526.geojson")
        ),
        TransitLine(
            lineNumber = "838",
            category = "buses",
            routeIds = listOf("5082", "5083"),
            routePaths = listOf("files/geojson/Bus lines/838/route_5082.geojson", "files/geojson/Bus lines/838/route_5083.geojson")
        ),
        TransitLine(
            lineNumber = "841",
            category = "buses",
            routeIds = listOf("4027", "4030", "4032"),
            routePaths = listOf("files/geojson/Bus lines/841/route_4027.geojson", "files/geojson/Bus lines/841/route_4030.geojson", "files/geojson/Bus lines/841/route_4032.geojson")
        ),
        TransitLine(
            lineNumber = "842",
            category = "buses",
            routeIds = listOf("4028", "4031"),
            routePaths = listOf("files/geojson/Bus lines/842/route_4028.geojson", "files/geojson/Bus lines/842/route_4031.geojson")
        ),
        TransitLine(
            lineNumber = "843",
            category = "buses",
            routeIds = listOf("4003", "4026"),
            routePaths = listOf("files/geojson/Bus lines/843/route_4003.geojson", "files/geojson/Bus lines/843/route_4026.geojson")
        ),
        TransitLine(
            lineNumber = "845",
            category = "buses",
            routeIds = listOf("4252", "4867"),
            routePaths = listOf("files/geojson/Bus lines/845/route_4252.geojson", "files/geojson/Bus lines/845/route_4867.geojson")
        ),
        TransitLine(
            lineNumber = "846",
            category = "buses",
            routeIds = listOf("4476", "4477", "4520"),
            routePaths = listOf("files/geojson/Bus lines/846/route_4476.geojson", "files/geojson/Bus lines/846/route_4477.geojson", "files/geojson/Bus lines/846/route_4520.geojson")
        ),
        TransitLine(
            lineNumber = "848",
            category = "buses",
            routeIds = listOf("4479", "4480"),
            routePaths = listOf("files/geojson/Bus lines/848/route_4479.geojson", "files/geojson/Bus lines/848/route_4480.geojson")
        ),
        TransitLine(
            lineNumber = "852",
            category = "buses",
            routeIds = listOf("3629", "4395"),
            routePaths = listOf("files/geojson/Bus lines/852/route_3629.geojson", "files/geojson/Bus lines/852/route_4395.geojson")
        ),
        TransitLine(
            lineNumber = "855",
            category = "buses",
            routeIds = listOf("1816", "1817", "3850", "4257", "4258"),
            routePaths = listOf("files/geojson/Bus lines/855/route_1816.geojson", "files/geojson/Bus lines/855/route_1817.geojson", "files/geojson/Bus lines/855/route_3850.geojson", "files/geojson/Bus lines/855/route_4257.geojson", "files/geojson/Bus lines/855/route_4258.geojson")
        ),
        TransitLine(
            lineNumber = "856",
            category = "buses",
            routeIds = listOf("1892", "3625", "3688"),
            routePaths = listOf("files/geojson/Bus lines/856/route_1892.geojson", "files/geojson/Bus lines/856/route_3625.geojson", "files/geojson/Bus lines/856/route_3688.geojson")
        ),
        TransitLine(
            lineNumber = "859",
            category = "buses",
            routeIds = listOf("2645", "4387"),
            routePaths = listOf("files/geojson/Bus lines/859/route_2645.geojson", "files/geojson/Bus lines/859/route_4387.geojson")
        ),
        TransitLine(
            lineNumber = "860",
            category = "buses",
            routeIds = listOf("5537", "5538", "5539"),
            routePaths = listOf("files/geojson/Bus lines/860/route_5537.geojson", "files/geojson/Bus lines/860/route_5538.geojson", "files/geojson/Bus lines/860/route_5539.geojson")
        ),
        TransitLine(
            lineNumber = "861",
            category = "buses",
            routeIds = listOf("4234", "4565"),
            routePaths = listOf("files/geojson/Bus lines/861/route_4234.geojson", "files/geojson/Bus lines/861/route_4565.geojson")
        ),
        TransitLine(
            lineNumber = "861Β",
            category = "buses",
            routeIds = listOf("5129", "5130"),
            routePaths = listOf("files/geojson/Bus lines/861Β/route_5129.geojson", "files/geojson/Bus lines/861Β/route_5130.geojson")
        ),
        TransitLine(
            lineNumber = "862",
            category = "buses",
            routeIds = listOf("2146", "2150"),
            routePaths = listOf("files/geojson/Bus lines/862/route_2146.geojson", "files/geojson/Bus lines/862/route_2150.geojson")
        ),
        TransitLine(
            lineNumber = "863",
            category = "buses",
            routeIds = listOf("4544", "4545"),
            routePaths = listOf("files/geojson/Bus lines/863/route_4544.geojson", "files/geojson/Bus lines/863/route_4545.geojson")
        ),
        TransitLine(
            lineNumber = "864",
            category = "buses",
            routeIds = listOf("2293"),
            routePaths = listOf("files/geojson/Bus lines/864/route_2293.geojson")
        ),
        TransitLine(
            lineNumber = "865",
            category = "buses",
            routeIds = listOf("4604"),
            routePaths = listOf("files/geojson/Bus lines/865/route_4604.geojson")
        ),
        TransitLine(
            lineNumber = "866",
            category = "buses",
            routeIds = listOf("5436", "5437", "5438", "5439", "5440"),
            routePaths = listOf("files/geojson/Bus lines/866/route_5436.geojson", "files/geojson/Bus lines/866/route_5437.geojson", "files/geojson/Bus lines/866/route_5438.geojson", "files/geojson/Bus lines/866/route_5439.geojson", "files/geojson/Bus lines/866/route_5440.geojson")
        ),
        TransitLine(
            lineNumber = "868",
            category = "buses",
            routeIds = listOf("4702", "4703"),
            routePaths = listOf("files/geojson/Bus lines/868/route_4702.geojson", "files/geojson/Bus lines/868/route_4703.geojson")
        ),
        TransitLine(
            lineNumber = "871",
            category = "buses",
            routeIds = listOf("4254", "5164"),
            routePaths = listOf("files/geojson/Bus lines/871/route_4254.geojson", "files/geojson/Bus lines/871/route_5164.geojson")
        ),
        TransitLine(
            lineNumber = "871Τ",
            category = "buses",
            routeIds = listOf("5165", "5166"),
            routePaths = listOf("files/geojson/Bus lines/871Τ/route_5165.geojson", "files/geojson/Bus lines/871Τ/route_5166.geojson")
        ),
        TransitLine(
            lineNumber = "876",
            category = "buses",
            routeIds = listOf("3874", "4255", "4267", "4268", "4334"),
            routePaths = listOf("files/geojson/Bus lines/876/route_3874.geojson", "files/geojson/Bus lines/876/route_4255.geojson", "files/geojson/Bus lines/876/route_4267.geojson", "files/geojson/Bus lines/876/route_4268.geojson", "files/geojson/Bus lines/876/route_4334.geojson")
        ),
        TransitLine(
            lineNumber = "878",
            category = "buses",
            routeIds = listOf("5169", "5441", "5442"),
            routePaths = listOf("files/geojson/Bus lines/878/route_5169.geojson", "files/geojson/Bus lines/878/route_5441.geojson", "files/geojson/Bus lines/878/route_5442.geojson")
        ),
        TransitLine(
            lineNumber = "879",
            category = "buses",
            routeIds = listOf("5108", "5170"),
            routePaths = listOf("files/geojson/Bus lines/879/route_5108.geojson", "files/geojson/Bus lines/879/route_5170.geojson")
        ),
        TransitLine(
            lineNumber = "881",
            category = "buses",
            routeIds = listOf("4826", "4827", "4828", "4829"),
            routePaths = listOf("files/geojson/Bus lines/881/route_4826.geojson", "files/geojson/Bus lines/881/route_4827.geojson", "files/geojson/Bus lines/881/route_4828.geojson", "files/geojson/Bus lines/881/route_4829.geojson")
        ),
        TransitLine(
            lineNumber = "890",
            category = "buses",
            routeIds = listOf("4374", "4376"),
            routePaths = listOf("files/geojson/Bus lines/890/route_4374.geojson", "files/geojson/Bus lines/890/route_4376.geojson")
        ),
        TransitLine(
            lineNumber = "891",
            category = "buses",
            routeIds = listOf("2857"),
            routePaths = listOf("files/geojson/Bus lines/891/route_2857.geojson")
        ),
        TransitLine(
            lineNumber = "892",
            category = "buses",
            routeIds = listOf("2858", "4405", "4407", "5178"),
            routePaths = listOf("files/geojson/Bus lines/892/route_2858.geojson", "files/geojson/Bus lines/892/route_4405.geojson", "files/geojson/Bus lines/892/route_4407.geojson", "files/geojson/Bus lines/892/route_5178.geojson")
        ),
        TransitLine(
            lineNumber = "904",
            category = "buses",
            routeIds = listOf("2162"),
            routePaths = listOf("files/geojson/Bus lines/904/route_2162.geojson")
        ),
        TransitLine(
            lineNumber = "906",
            category = "buses",
            routeIds = listOf("1895", "4250"),
            routePaths = listOf("files/geojson/Bus lines/906/route_1895.geojson", "files/geojson/Bus lines/906/route_4250.geojson")
        ),
        TransitLine(
            lineNumber = "909",
            category = "buses",
            routeIds = listOf("4369", "4372", "4483", "4484", "4485"),
            routePaths = listOf("files/geojson/Bus lines/909/route_4369.geojson", "files/geojson/Bus lines/909/route_4372.geojson", "files/geojson/Bus lines/909/route_4483.geojson", "files/geojson/Bus lines/909/route_4484.geojson", "files/geojson/Bus lines/909/route_4485.geojson")
        ),
        TransitLine(
            lineNumber = "910",
            category = "buses",
            routeIds = listOf("2551", "4299", "4301"),
            routePaths = listOf("files/geojson/Bus lines/910/route_2551.geojson", "files/geojson/Bus lines/910/route_4299.geojson", "files/geojson/Bus lines/910/route_4301.geojson")
        ),
        TransitLine(
            lineNumber = "911",
            category = "buses",
            routeIds = listOf("2274"),
            routePaths = listOf("files/geojson/Bus lines/911/route_2274.geojson")
        ),
        TransitLine(
            lineNumber = "911Α",
            category = "buses",
            routeIds = listOf("3820"),
            routePaths = listOf("files/geojson/Bus lines/911Α/route_3820.geojson")
        ),
        TransitLine(
            lineNumber = "912",
            category = "buses",
            routeIds = listOf("5661"),
            routePaths = listOf("files/geojson/Bus lines/912/route_5661.geojson")
        ),
        TransitLine(
            lineNumber = "914",
            category = "buses",
            routeIds = listOf("4287", "4356", "4386"),
            routePaths = listOf("files/geojson/Bus lines/914/route_4287.geojson", "files/geojson/Bus lines/914/route_4356.geojson", "files/geojson/Bus lines/914/route_4386.geojson")
        ),
        TransitLine(
            lineNumber = "915",
            category = "buses",
            routeIds = listOf("3476", "4359", "4423"),
            routePaths = listOf("files/geojson/Bus lines/915/route_3476.geojson", "files/geojson/Bus lines/915/route_4359.geojson", "files/geojson/Bus lines/915/route_4423.geojson")
        ),
        TransitLine(
            lineNumber = "Α1",
            category = "buses",
            routeIds = listOf("5517", "5518", "5561"),
            routePaths = listOf("files/geojson/Bus lines/Α1/route_5517.geojson", "files/geojson/Bus lines/Α1/route_5518.geojson", "files/geojson/Bus lines/Α1/route_5561.geojson")
        ),
        TransitLine(
            lineNumber = "Α2",
            category = "buses",
            routeIds = listOf("5566", "5567", "5568", "5569"),
            routePaths = listOf("files/geojson/Bus lines/Α2/route_5566.geojson", "files/geojson/Bus lines/Α2/route_5567.geojson", "files/geojson/Bus lines/Α2/route_5568.geojson", "files/geojson/Bus lines/Α2/route_5569.geojson")
        ),
        TransitLine(
            lineNumber = "Α3",
            category = "buses",
            routeIds = listOf("3614", "5349", "5350"),
            routePaths = listOf("files/geojson/Bus lines/Α3/route_3614.geojson", "files/geojson/Bus lines/Α3/route_5349.geojson", "files/geojson/Bus lines/Α3/route_5350.geojson")
        ),
        TransitLine(
            lineNumber = "Α5",
            category = "buses",
            routeIds = listOf("2138", "3609"),
            routePaths = listOf("files/geojson/Bus lines/Α5/route_2138.geojson", "files/geojson/Bus lines/Α5/route_3609.geojson")
        ),
        TransitLine(
            lineNumber = "Α7",
            category = "buses",
            routeIds = listOf("2085", "2086", "4622", "4623"),
            routePaths = listOf("files/geojson/Bus lines/Α7/route_2085.geojson", "files/geojson/Bus lines/Α7/route_2086.geojson", "files/geojson/Bus lines/Α7/route_4622.geojson", "files/geojson/Bus lines/Α7/route_4623.geojson")
        ),
        TransitLine(
            lineNumber = "Α8",
            category = "buses",
            routeIds = listOf("1824", "1825", "2598", "2599"),
            routePaths = listOf("files/geojson/Bus lines/Α8/route_1824.geojson", "files/geojson/Bus lines/Α8/route_1825.geojson", "files/geojson/Bus lines/Α8/route_2598.geojson", "files/geojson/Bus lines/Α8/route_2599.geojson")
        ),
        TransitLine(
            lineNumber = "Α10",
            category = "buses",
            routeIds = listOf("3690", "3691", "4677", "4678"),
            routePaths = listOf("files/geojson/Bus lines/Α10/route_3690.geojson", "files/geojson/Bus lines/Α10/route_3691.geojson", "files/geojson/Bus lines/Α10/route_4677.geojson", "files/geojson/Bus lines/Α10/route_4678.geojson")
        ),
        TransitLine(
            lineNumber = "Α11",
            category = "buses",
            routeIds = listOf("5270", "5271"),
            routePaths = listOf("files/geojson/Bus lines/Α11/route_5270.geojson", "files/geojson/Bus lines/Α11/route_5271.geojson")
        ),
        TransitLine(
            lineNumber = "Α13",
            category = "buses",
            routeIds = listOf("1873", "3536"),
            routePaths = listOf("files/geojson/Bus lines/Α13/route_1873.geojson", "files/geojson/Bus lines/Α13/route_3536.geojson")
        ),
        TransitLine(
            lineNumber = "Α15",
            category = "buses",
            routeIds = listOf("1837", "2980", "4664"),
            routePaths = listOf("files/geojson/Bus lines/Α15/route_1837.geojson", "files/geojson/Bus lines/Α15/route_2980.geojson", "files/geojson/Bus lines/Α15/route_4664.geojson")
        ),
        TransitLine(
            lineNumber = "Β1",
            category = "buses",
            routeIds = listOf("5519", "5520", "5521", "5522"),
            routePaths = listOf("files/geojson/Bus lines/Β1/route_5519.geojson", "files/geojson/Bus lines/Β1/route_5520.geojson", "files/geojson/Bus lines/Β1/route_5521.geojson", "files/geojson/Bus lines/Β1/route_5522.geojson")
        ),
        TransitLine(
            lineNumber = "Β2",
            category = "buses",
            routeIds = listOf("5662", "5666", "5667"),
            routePaths = listOf("files/geojson/Bus lines/Β2/route_5662.geojson", "files/geojson/Bus lines/Β2/route_5666.geojson", "files/geojson/Bus lines/Β2/route_5667.geojson")
        ),
        TransitLine(
            lineNumber = "Β5",
            category = "buses",
            routeIds = listOf("2167", "2168"),
            routePaths = listOf("files/geojson/Bus lines/Β5/route_2167.geojson", "files/geojson/Bus lines/Β5/route_2168.geojson")
        ),
        TransitLine(
            lineNumber = "Β9",
            category = "buses",
            routeIds = listOf("2042", "2043"),
            routePaths = listOf("files/geojson/Bus lines/Β9/route_2042.geojson", "files/geojson/Bus lines/Β9/route_2043.geojson")
        ),
        TransitLine(
            lineNumber = "Β10",
            category = "buses",
            routeIds = listOf("2137", "3758"),
            routePaths = listOf("files/geojson/Bus lines/Β10/route_2137.geojson", "files/geojson/Bus lines/Β10/route_3758.geojson")
        ),
        TransitLine(
            lineNumber = "Β11",
            category = "buses",
            routeIds = listOf("5272", "5273"),
            routePaths = listOf("files/geojson/Bus lines/Β11/route_5272.geojson", "files/geojson/Bus lines/Β11/route_5273.geojson")
        ),
        TransitLine(
            lineNumber = "Β12",
            category = "buses",
            routeIds = listOf("3491", "3693", "5674"),
            routePaths = listOf("files/geojson/Bus lines/Β12/route_3491.geojson", "files/geojson/Bus lines/Β12/route_3693.geojson", "files/geojson/Bus lines/Β12/route_5674.geojson")
        ),
        TransitLine(
            lineNumber = "Β15",
            category = "buses",
            routeIds = listOf("2204", "2979"),
            routePaths = listOf("files/geojson/Bus lines/Β15/route_2204.geojson", "files/geojson/Bus lines/Β15/route_2979.geojson")
        ),
        TransitLine(
            lineNumber = "Γ12",
            category = "buses",
            routeIds = listOf("5101"),
            routePaths = listOf("files/geojson/Bus lines/Γ12/route_5101.geojson")
        ),
        TransitLine(
            lineNumber = "Ε14",
            category = "buses",
            routeIds = listOf("3487", "3488"),
            routePaths = listOf("files/geojson/Bus lines/Ε14/route_3487.geojson", "files/geojson/Bus lines/Ε14/route_3488.geojson")
        ),
        TransitLine(
            lineNumber = "Ε90",
            category = "buses",
            routeIds = listOf("4941", "5534"),
            routePaths = listOf("files/geojson/Bus lines/Ε90/route_4941.geojson", "files/geojson/Bus lines/Ε90/route_5534.geojson")
        ),
        TransitLine(
            lineNumber = "Χ14",
            category = "buses",
            routeIds = listOf("2184", "2185"),
            routePaths = listOf("files/geojson/Bus lines/Χ14/route_2184.geojson", "files/geojson/Bus lines/Χ14/route_2185.geojson")
        ),
        TransitLine(
            lineNumber = "Χ93",
            category = "buses",
            routeIds = listOf("5675", "5676"),
            routePaths = listOf("files/geojson/Bus lines/Χ93/route_5675.geojson", "files/geojson/Bus lines/Χ93/route_5676.geojson")
        ),
        TransitLine(
            lineNumber = "Χ95",
            category = "buses",
            routeIds = listOf("2051", "2052"),
            routePaths = listOf("files/geojson/Bus lines/Χ95/route_2051.geojson", "files/geojson/Bus lines/Χ95/route_2052.geojson")
        ),
        TransitLine(
            lineNumber = "Χ96",
            category = "buses",
            routeIds = listOf("3008", "3196", "5532", "5533"),
            routePaths = listOf("files/geojson/Bus lines/Χ96/route_3008.geojson", "files/geojson/Bus lines/Χ96/route_3196.geojson", "files/geojson/Bus lines/Χ96/route_5532.geojson", "files/geojson/Bus lines/Χ96/route_5533.geojson")
        ),
        TransitLine(
            lineNumber = "Χ97",
            category = "buses",
            routeIds = listOf("5373", "5374", "5375", "5376"),
            routePaths = listOf("files/geojson/Bus lines/Χ97/route_5373.geojson", "files/geojson/Bus lines/Χ97/route_5374.geojson", "files/geojson/Bus lines/Χ97/route_5375.geojson", "files/geojson/Bus lines/Χ97/route_5376.geojson")
        ),
        TransitLine(
            lineNumber = "1",
            category = "trolleys",
            routeIds = listOf("5529", "5530"),
            routePaths = listOf("files/geojson/Trolley lines/1/route_5529.geojson", "files/geojson/Trolley lines/1/route_5530.geojson")
        ),
        TransitLine(
            lineNumber = "2",
            category = "trolleys",
            routeIds = listOf("4914", "5196"),
            routePaths = listOf("files/geojson/Trolley lines/2/route_4914.geojson", "files/geojson/Trolley lines/2/route_5196.geojson")
        ),
        TransitLine(
            lineNumber = "3",
            category = "trolleys",
            routeIds = listOf("1989", "1990"),
            routePaths = listOf("files/geojson/Trolley lines/3/route_1989.geojson", "files/geojson/Trolley lines/3/route_1990.geojson")
        ),
        TransitLine(
            lineNumber = "4",
            category = "trolleys",
            routeIds = listOf("2024", "4915"),
            routePaths = listOf("files/geojson/Trolley lines/4/route_2024.geojson", "files/geojson/Trolley lines/4/route_4915.geojson")
        ),
        TransitLine(
            lineNumber = "5",
            category = "trolleys",
            routeIds = listOf("1991", "1992"),
            routePaths = listOf("files/geojson/Trolley lines/5/route_1991.geojson", "files/geojson/Trolley lines/5/route_1992.geojson")
        ),
        TransitLine(
            lineNumber = "6",
            category = "trolleys",
            routeIds = listOf("1987", "1988"),
            routePaths = listOf("files/geojson/Trolley lines/6/route_1987.geojson", "files/geojson/Trolley lines/6/route_1988.geojson")
        ),
        TransitLine(
            lineNumber = "10",
            category = "trolleys",
            routeIds = listOf("1994", "5510"),
            routePaths = listOf("files/geojson/Trolley lines/10/route_1994.geojson", "files/geojson/Trolley lines/10/route_5510.geojson")
        ),
        TransitLine(
            lineNumber = "11",
            category = "trolleys",
            routeIds = listOf("1995", "4913"),
            routePaths = listOf("files/geojson/Trolley lines/11/route_1995.geojson", "files/geojson/Trolley lines/11/route_4913.geojson")
        ),
        TransitLine(
            lineNumber = "12",
            category = "trolleys",
            routeIds = listOf("3530", "5086"),
            routePaths = listOf("files/geojson/Trolley lines/12/route_3530.geojson", "files/geojson/Trolley lines/12/route_5086.geojson")
        ),
        TransitLine(
            lineNumber = "15",
            category = "trolleys",
            routeIds = listOf("2013", "3145", "4242", "4540"),
            routePaths = listOf("files/geojson/Trolley lines/15/route_2013.geojson", "files/geojson/Trolley lines/15/route_3145.geojson", "files/geojson/Trolley lines/15/route_4242.geojson", "files/geojson/Trolley lines/15/route_4540.geojson")
        ),
        TransitLine(
            lineNumber = "17",
            category = "trolleys",
            routeIds = listOf("5137"),
            routePaths = listOf("files/geojson/Trolley lines/17/route_5137.geojson")
        ),
        TransitLine(
            lineNumber = "18",
            category = "trolleys",
            routeIds = listOf("2027", "2028"),
            routePaths = listOf("files/geojson/Trolley lines/18/route_2027.geojson", "files/geojson/Trolley lines/18/route_2028.geojson")
        ),
        TransitLine(
            lineNumber = "19",
            category = "trolleys",
            routeIds = listOf("2029", "2030"),
            routePaths = listOf("files/geojson/Trolley lines/19/route_2029.geojson", "files/geojson/Trolley lines/19/route_2030.geojson")
        ),
        TransitLine(
            lineNumber = "20",
            category = "trolleys",
            routeIds = listOf("1961", "3766"),
            routePaths = listOf("files/geojson/Trolley lines/20/route_1961.geojson", "files/geojson/Trolley lines/20/route_3766.geojson")
        ),
        TransitLine(
            lineNumber = "21",
            category = "trolleys",
            routeIds = listOf("3627"),
            routePaths = listOf("files/geojson/Trolley lines/21/route_3627.geojson")
        ),
        TransitLine(
            lineNumber = "021",
            category = "trolleys",
            routeIds = listOf("2484", "4198", "4199"),
            routePaths = listOf("files/geojson/Trolley lines/021/route_2484.geojson", "files/geojson/Trolley lines/021/route_4198.geojson", "files/geojson/Trolley lines/021/route_4199.geojson")
        ),
        TransitLine(
            lineNumber = "024",
            category = "trolleys",
            routeIds = listOf("2640"),
            routePaths = listOf("files/geojson/Trolley lines/024/route_2640.geojson")
        ),
        TransitLine(
            lineNumber = "24",
            category = "trolleys",
            routeIds = listOf("1962"),
            routePaths = listOf("files/geojson/Trolley lines/24/route_1962.geojson")
        ),
        TransitLine(
            lineNumber = "025",
            category = "trolleys",
            routeIds = listOf("1797", "5562"),
            routePaths = listOf("files/geojson/Trolley lines/025/route_1797.geojson", "files/geojson/Trolley lines/025/route_5562.geojson")
        ),
        TransitLine(
            lineNumber = "25",
            category = "trolleys",
            routeIds = listOf("4293", "5102"),
            routePaths = listOf("files/geojson/Trolley lines/25/route_4293.geojson", "files/geojson/Trolley lines/25/route_5102.geojson")
        ),
        TransitLine(
            lineNumber = "1",
            category = "metro",
            routeIds = listOf("main"),
            routePaths = listOf("files/geojson/Metro lines/metro_line_1.geojson")
        ),
        TransitLine(
            lineNumber = "2",
            category = "metro",
            routeIds = listOf("main"),
            routePaths = listOf("files/geojson/Metro lines/metro_line_2.geojson")
        ),
        TransitLine(
            lineNumber = "3",
            category = "metro",
            routeIds = listOf("main"),
            routePaths = listOf("files/geojson/Metro lines/metro_line_3.geojson")
        ),
        TransitLine(
            lineNumber = "T6",
            category = "tram",
            routeIds = listOf("main"),
            routePaths = listOf("files/geojson/Tram lines/tram_line_T6.geojson")
        ),
        TransitLine(
            lineNumber = "T7",
            category = "tram",
            routeIds = listOf("main"),
            routePaths = listOf("files/geojson/Tram lines/tram_line_T7.geojson")
        )
    )
    
    /**
     * Get all transit lines
     */
    fun getAllLines(): List<TransitLine> = lines
    
    /**
     * Get a specific line by its number
     * @param lineNumber The line number to search for
     * @return The TransitLine if found, null otherwise
     */
    fun getLineByNumber(lineNumber: String): TransitLine? {
        return lines.find { it.lineNumber.equals(lineNumber, ignoreCase = true) }
    }
    
    /**
     * Get all lines in a specific category
     * @param category The category ("buses", "trolleys", "metro", "tram")
     * @return List of transit lines in that category
     */
    fun getLinesByCategory(category: String): List<TransitLine> {
        return lines.filter { it.category.equals(category, ignoreCase = true) }
    }
    
    /**
     * Get all bus lines
     */
    fun getBusLines(): List<TransitLine> = getLinesByCategory("buses")
    
    /**
     * Get all trolley lines
     */
    fun getTrolleyLines(): List<TransitLine> = getLinesByCategory("trolleys")

    /**
     * Get all metro lines
     */
    fun getMetroLines(): List<TransitLine> = getLinesByCategory("metro")

    /**
     * Get all tram lines
     */
    fun getTramLines(): List<TransitLine> = getLinesByCategory("tram")
    
    /**
     * Search for lines matching a query
     * @param query The search query
     * @return List of matching transit lines
     */
    fun searchLines(query: String): List<TransitLine> {
        return lines.filter { it.lineNumber.contains(query, ignoreCase = true) }
    }
    
    /**
     * Get statistics about the repository
     */
    fun getStats(): RepositoryStats {
        return RepositoryStats(
            totalLines = lines.size,
            totalBusLines = getBusLines().size,
            totalTrolleyLines = getTrolleyLines().size,
            totalMetroLines = getMetroLines().size,
            totalTramLines = getTramLines().size,
            totalRoutes = lines.sumOf { it.routeIds.size }
        )
    }
}

/**
 * Statistics about the transit line repository
 */
data class RepositoryStats(
    val totalLines: Int,
    val totalBusLines: Int,
    val totalTrolleyLines: Int,
    val totalMetroLines: Int,
    val totalTramLines: Int,
    val totalRoutes: Int
)

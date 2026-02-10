package com.example.newoasa.data

/**
 * Transit line categories
 */
enum class TransitCategory {
    METRO,
    TRAM,
    SUBURBAN,
    BUS,
    TROLLEY
}

/**
 * Represents a single transit line with its routes
 */
data class TransitLine(
    val lineNumber: String,
    val displayName: String,
    val category: TransitCategory,
    val routePaths: List<String>,  // Paths to GeoJSON files
    val routeIds: List<String> = routePaths  // For compatibility
) {
    val isMetro: Boolean get() = category == TransitCategory.METRO
    val isTram: Boolean get() = category == TransitCategory.TRAM
    val isSuburban: Boolean get() = category == TransitCategory.SUBURBAN
    val isBus: Boolean get() = category == TransitCategory.BUS
    val isTrolley: Boolean get() = category == TransitCategory.TROLLEY
}

/**
 * Repository for all transit lines
 * Auto-generated from GeoJSON folder structure
 */
object TransitLineRepository {
    
    /**
     * Get all Metro lines (1, 2, 3)
     */
    fun getMetroLines(): List<TransitLine> {
        return listOf(
        TransitLine(
            lineNumber = "1",
            displayName = "Metro Line 1",
            category = TransitCategory.METRO,
            routePaths = listOf(
            "files/geojson/Metro lines/1/metro_1_ Kifissia → Piraeus.geojson",
            "files/geojson/Metro lines/1/metro_1_ Piraeus → Kifissia.geojson"
            )
        ),
        TransitLine(
            lineNumber = "2",
            displayName = "Metro Line 2",
            category = TransitCategory.METRO,
            routePaths = listOf(
            "files/geojson/Metro lines/2/metro_2_ Anthoupoli → Elliniko.geojson",
            "files/geojson/Metro lines/2/metro_2_ Elliniko → Anthoupoli.geojson"
            )
        ),
        TransitLine(
            lineNumber = "3",
            displayName = "Metro Line 3",
            category = TransitCategory.METRO,
            routePaths = listOf(
            "files/geojson/Metro lines/3/metro_3_ Airport → Dimotiko Theatro.geojson",
            "files/geojson/Metro lines/3/metro_3_ Dimotiko Theatro → Airport.geojson"
            )
        )
        )
    }
    
    /**
     * Get all Tram lines (T6, T7)
     */
    fun getTramLines(): List<TransitLine> {
        return listOf(
        TransitLine(
            lineNumber = "T6",
            displayName = "Tram T6",
            category = TransitCategory.TRAM,
            routePaths = listOf(
            "files/geojson/Tram lines/T6/tram_t6_ Pikrodafni → Syntagma.geojson",
            "files/geojson/Tram lines/T6/tram_t6_ Syntagma → Pikrodafni.geojson"
            )
        ),
        TransitLine(
            lineNumber = "T7",
            displayName = "Tram T7",
            category = TransitCategory.TRAM,
            routePaths = listOf(
            "files/geojson/Tram lines/T7/tram_t7_ Agia Triada → Asklipiio Voulas.geojson",
            "files/geojson/Tram lines/T7/tram_t7_ Asklipiio Voulas → Agia Triada.geojson"
            )
        )
        )
    }
    
    /**
     * Get all Suburban Railway lines (A1, A2, A3, A4)
     */
    fun getSuburbanLines(): List<TransitLine> {
        return listOf(
        TransitLine(
            lineNumber = "A1",
            displayName = "Suburban Railway A1",
            category = TransitCategory.SUBURBAN,
            routePaths = listOf(
            "files/geojson/Suburban Railway lines/A1/suburban_a1_ Airport → Piraeus.geojson",
            "files/geojson/Suburban Railway lines/A1/suburban_a1_ Piraeus → Airport.geojson"
            )
        ),
        TransitLine(
            lineNumber = "A2",
            displayName = "Suburban Railway A2",
            category = TransitCategory.SUBURBAN,
            routePaths = listOf(
            "files/geojson/Suburban Railway lines/A2/suburban_a2_ Airport → Ano Liosia.geojson",
            "files/geojson/Suburban Railway lines/A2/suburban_a2_ Ano Liosia → Airport.geojson"
            )
        ),
        TransitLine(
            lineNumber = "A3",
            displayName = "Suburban Railway A3",
            category = TransitCategory.SUBURBAN,
            routePaths = listOf(
            "files/geojson/Suburban Railway lines/A3/suburban_a3_ Athens → Chalcis.geojson",
            "files/geojson/Suburban Railway lines/A3/suburban_a3_ Chalcis → Athens.geojson"
            )
        ),
        TransitLine(
            lineNumber = "A4",
            displayName = "Suburban Railway A4",
            category = TransitCategory.SUBURBAN,
            routePaths = listOf(
            "files/geojson/Suburban Railway lines/A4/suburban_a4_ Kiato → Piraeus.geojson",
            "files/geojson/Suburban Railway lines/A4/suburban_a4_ Piraeus → Kiato.geojson"
            )
        )
        )
    }
    
    /**
     * Get all Bus lines
     */
    fun getBusLines(): List<TransitLine> {
        return listOf(
        TransitLine(
            lineNumber = "021",
            displayName = "Bus 021",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/021/route_2484.geojson",
            "files/geojson/Bus lines/buses/021/route_4198.geojson",
            "files/geojson/Bus lines/buses/021/route_4199.geojson"
            )
        ),
        TransitLine(
            lineNumber = "022",
            displayName = "Bus 022",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/022/route_3494.geojson",
            "files/geojson/Bus lines/buses/022/route_4213.geojson",
            "files/geojson/Bus lines/buses/022/route_4214.geojson"
            )
        ),
        TransitLine(
            lineNumber = "024",
            displayName = "Bus 024",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/024/route_2640.geojson"
            )
        ),
        TransitLine(
            lineNumber = "025",
            displayName = "Bus 025",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/025/route_1797.geojson",
            "files/geojson/Bus lines/buses/025/route_5562.geojson"
            )
        ),
        TransitLine(
            lineNumber = "026",
            displayName = "Bus 026",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/026/route_1799.geojson",
            "files/geojson/Bus lines/buses/026/route_5563.geojson"
            )
        ),
        TransitLine(
            lineNumber = "031",
            displayName = "Bus 031",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/031/route_4689.geojson",
            "files/geojson/Bus lines/buses/031/route_4690.geojson"
            )
        ),
        TransitLine(
            lineNumber = "032",
            displayName = "Bus 032",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/032/route_5643.geojson",
            "files/geojson/Bus lines/buses/032/route_5670.geojson"
            )
        ),
        TransitLine(
            lineNumber = "035",
            displayName = "Bus 035",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/035/route_2953.geojson",
            "files/geojson/Bus lines/buses/035/route_4538.geojson"
            )
        ),
        TransitLine(
            lineNumber = "036",
            displayName = "Bus 036",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/036/route_3093.geojson",
            "files/geojson/Bus lines/buses/036/route_4948.geojson",
            "files/geojson/Bus lines/buses/036/route_4949.geojson"
            )
        ),
        TransitLine(
            lineNumber = "040",
            displayName = "Bus 040",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/040/route_5512.geojson",
            "files/geojson/Bus lines/buses/040/route_5513.geojson",
            "files/geojson/Bus lines/buses/040/route_5535.geojson"
            )
        ),
        TransitLine(
            lineNumber = "046",
            displayName = "Bus 046",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/046/route_1821.geojson",
            "files/geojson/Bus lines/buses/046/route_5330.geojson"
            )
        ),
        TransitLine(
            lineNumber = "049",
            displayName = "Bus 049",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/049/route_2995.geojson",
            "files/geojson/Bus lines/buses/049/route_3086.geojson"
            )
        ),
        TransitLine(
            lineNumber = "051",
            displayName = "Bus 051",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/051/route_3967.geojson",
            "files/geojson/Bus lines/buses/051/route_3968.geojson",
            "files/geojson/Bus lines/buses/051/route_4737.geojson"
            )
        ),
        TransitLine(
            lineNumber = "052",
            displayName = "Bus 052",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/052/route_4570.geojson"
            )
        ),
        TransitLine(
            lineNumber = "054",
            displayName = "Bus 054",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/054/route_5361.geojson",
            "files/geojson/Bus lines/buses/054/route_5668.geojson"
            )
        ),
        TransitLine(
            lineNumber = "057",
            displayName = "Bus 057",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/057/route_1810.geojson"
            )
        ),
        TransitLine(
            lineNumber = "060",
            displayName = "Bus 060",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/060/route_3447.geojson"
            )
        ),
        TransitLine(
            lineNumber = "101",
            displayName = "Bus 101",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/101/route_5651.geojson",
            "files/geojson/Bus lines/buses/101/route_5652.geojson"
            )
        ),
        TransitLine(
            lineNumber = "106",
            displayName = "Bus 106",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/106/route_2759.geojson",
            "files/geojson/Bus lines/buses/106/route_5331.geojson"
            )
        ),
        TransitLine(
            lineNumber = "109",
            displayName = "Bus 109",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/109/route_5653.geojson",
            "files/geojson/Bus lines/buses/109/route_5654.geojson",
            "files/geojson/Bus lines/buses/109/route_5656.geojson"
            )
        ),
        TransitLine(
            lineNumber = "112",
            displayName = "Bus 112",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/112/route_4986.geojson",
            "files/geojson/Bus lines/buses/112/route_4987.geojson"
            )
        ),
        TransitLine(
            lineNumber = "115",
            displayName = "Bus 115",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/115/route_2187.geojson",
            "files/geojson/Bus lines/buses/115/route_3256.geojson",
            "files/geojson/Bus lines/buses/115/route_3687.geojson"
            )
        ),
        TransitLine(
            lineNumber = "116",
            displayName = "Bus 116",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/116/route_2159.geojson",
            "files/geojson/Bus lines/buses/116/route_3032.geojson"
            )
        ),
        TransitLine(
            lineNumber = "117",
            displayName = "Bus 117",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/117/route_1934.geojson",
            "files/geojson/Bus lines/buses/117/route_3019.geojson"
            )
        ),
        TransitLine(
            lineNumber = "120",
            displayName = "Bus 120",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/120/route_3373.geojson",
            "files/geojson/Bus lines/buses/120/route_3375.geojson",
            "files/geojson/Bus lines/buses/120/route_5478.geojson"
            )
        ),
        TransitLine(
            lineNumber = "122",
            displayName = "Bus 122",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/122/route_4019.geojson",
            "files/geojson/Bus lines/buses/122/route_4020.geojson",
            "files/geojson/Bus lines/buses/122/route_4581.geojson"
            )
        ),
        TransitLine(
            lineNumber = "123",
            displayName = "Bus 123",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/123/route_2493.geojson",
            "files/geojson/Bus lines/buses/123/route_3955.geojson"
            )
        ),
        TransitLine(
            lineNumber = "124",
            displayName = "Bus 124",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/124/route_4047.geojson",
            "files/geojson/Bus lines/buses/124/route_4048.geojson",
            "files/geojson/Bus lines/buses/124/route_5479.geojson"
            )
        ),
        TransitLine(
            lineNumber = "126",
            displayName = "Bus 126",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/126/route_5332.geojson",
            "files/geojson/Bus lines/buses/126/route_5333.geojson"
            )
        ),
        TransitLine(
            lineNumber = "128",
            displayName = "Bus 128",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/128/route_1928.geojson",
            "files/geojson/Bus lines/buses/128/route_4225.geojson",
            "files/geojson/Bus lines/buses/128/route_4226.geojson",
            "files/geojson/Bus lines/buses/128/route_5481.geojson"
            )
        ),
        TransitLine(
            lineNumber = "130",
            displayName = "Bus 130",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/130/route_5514.geojson"
            )
        ),
        TransitLine(
            lineNumber = "131",
            displayName = "Bus 131",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/131/route_2560.geojson",
            "files/geojson/Bus lines/buses/131/route_4227.geojson"
            )
        ),
        TransitLine(
            lineNumber = "136",
            displayName = "Bus 136",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/136/route_5284.geojson"
            )
        ),
        TransitLine(
            lineNumber = "137",
            displayName = "Bus 137",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/137/route_5285.geojson"
            )
        ),
        TransitLine(
            lineNumber = "140",
            displayName = "Bus 140",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/140/route_5043.geojson",
            "files/geojson/Bus lines/buses/140/route_5483.geojson",
            "files/geojson/Bus lines/buses/140/route_5650.geojson",
            "files/geojson/Bus lines/buses/140/route_5690.geojson"
            )
        ),
        TransitLine(
            lineNumber = "141",
            displayName = "Bus 141",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/141/route_2552.geojson",
            "files/geojson/Bus lines/buses/141/route_4916.geojson",
            "files/geojson/Bus lines/buses/141/route_4917.geojson"
            )
        ),
        TransitLine(
            lineNumber = "142",
            displayName = "Bus 142",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/142/route_5663.geojson"
            )
        ),
        TransitLine(
            lineNumber = "154",
            displayName = "Bus 154",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/154/route_3895.geojson"
            )
        ),
        TransitLine(
            lineNumber = "162",
            displayName = "Bus 162",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/162/route_5116.geojson"
            )
        ),
        TransitLine(
            lineNumber = "164",
            displayName = "Bus 164",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/164/route_1925.geojson",
            "files/geojson/Bus lines/buses/164/route_3411.geojson",
            "files/geojson/Bus lines/buses/164/route_4049.geojson",
            "files/geojson/Bus lines/buses/164/route_4050.geojson",
            "files/geojson/Bus lines/buses/164/route_4244.geojson",
            "files/geojson/Bus lines/buses/164/route_4246.geojson",
            "files/geojson/Bus lines/buses/164/route_4249.geojson"
            )
        ),
        TransitLine(
            lineNumber = "171",
            displayName = "Bus 171",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/171/route_3897.geojson",
            "files/geojson/Bus lines/buses/171/route_4015.geojson",
            "files/geojson/Bus lines/buses/171/route_4018.geojson",
            "files/geojson/Bus lines/buses/171/route_5372.geojson"
            )
        ),
        TransitLine(
            lineNumber = "201",
            displayName = "Bus 201",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/201/route_3567.geojson",
            "files/geojson/Bus lines/buses/201/route_4280.geojson",
            "files/geojson/Bus lines/buses/201/route_4281.geojson"
            )
        ),
        TransitLine(
            lineNumber = "203",
            displayName = "Bus 203",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/203/route_3427.geojson",
            "files/geojson/Bus lines/buses/203/route_4982.geojson",
            "files/geojson/Bus lines/buses/203/route_5694.geojson"
            )
        ),
        TransitLine(
            lineNumber = "204",
            displayName = "Bus 204",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/204/route_3428.geojson",
            "files/geojson/Bus lines/buses/204/route_4264.geojson",
            "files/geojson/Bus lines/buses/204/route_5695.geojson"
            )
        ),
        TransitLine(
            lineNumber = "204Β",
            displayName = "Bus 204Β",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/204Β/route_3430.geojson",
            "files/geojson/Bus lines/buses/204Β/route_4265.geojson",
            "files/geojson/Bus lines/buses/204Β/route_5696.geojson"
            )
        ),
        TransitLine(
            lineNumber = "205",
            displayName = "Bus 205",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/205/route_5181.geojson",
            "files/geojson/Bus lines/buses/205/route_5182.geojson",
            "files/geojson/Bus lines/buses/205/route_5184.geojson",
            "files/geojson/Bus lines/buses/205/route_5185.geojson",
            "files/geojson/Bus lines/buses/205/route_5482.geojson"
            )
        ),
        TransitLine(
            lineNumber = "206",
            displayName = "Bus 206",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/206/route_2556.geojson",
            "files/geojson/Bus lines/buses/206/route_5314.geojson"
            )
        ),
        TransitLine(
            lineNumber = "209",
            displayName = "Bus 209",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/209/route_5120.geojson",
            "files/geojson/Bus lines/buses/209/route_5121.geojson"
            )
        ),
        TransitLine(
            lineNumber = "211",
            displayName = "Bus 211",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/211/route_5122.geojson"
            )
        ),
        TransitLine(
            lineNumber = "212",
            displayName = "Bus 212",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/212/route_1951.geojson",
            "files/geojson/Bus lines/buses/212/route_1952.geojson"
            )
        ),
        TransitLine(
            lineNumber = "214",
            displayName = "Bus 214",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/214/route_5044.geojson"
            )
        ),
        TransitLine(
            lineNumber = "217",
            displayName = "Bus 217",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/217/route_5515.geojson",
            "files/geojson/Bus lines/buses/217/route_5516.geojson"
            )
        ),
        TransitLine(
            lineNumber = "218",
            displayName = "Bus 218",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/218/route_2034.geojson",
            "files/geojson/Bus lines/buses/218/route_2810.geojson",
            "files/geojson/Bus lines/buses/218/route_2899.geojson"
            )
        ),
        TransitLine(
            lineNumber = "219",
            displayName = "Bus 219",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/219/route_2215.geojson",
            "files/geojson/Bus lines/buses/219/route_2809.geojson"
            )
        ),
        TransitLine(
            lineNumber = "220",
            displayName = "Bus 220",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/220/route_5369.geojson"
            )
        ),
        TransitLine(
            lineNumber = "221",
            displayName = "Bus 221",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/221/route_5370.geojson",
            "files/geojson/Bus lines/buses/221/route_5371.geojson"
            )
        ),
        TransitLine(
            lineNumber = "224",
            displayName = "Bus 224",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/224/route_1754.geojson",
            "files/geojson/Bus lines/buses/224/route_5045.geojson"
            )
        ),
        TransitLine(
            lineNumber = "227",
            displayName = "Bus 227",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/227/route_3593.geojson",
            "files/geojson/Bus lines/buses/227/route_4783.geojson",
            "files/geojson/Bus lines/buses/227/route_5454.geojson",
            "files/geojson/Bus lines/buses/227/route_5455.geojson"
            )
        ),
        TransitLine(
            lineNumber = "229",
            displayName = "Bus 229",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/229/route_5523.geojson",
            "files/geojson/Bus lines/buses/229/route_5536.geojson"
            )
        ),
        TransitLine(
            lineNumber = "230",
            displayName = "Bus 230",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/230/route_5647.geojson",
            "files/geojson/Bus lines/buses/230/route_5648.geojson"
            )
        ),
        TransitLine(
            lineNumber = "235",
            displayName = "Bus 235",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/235/route_3608.geojson"
            )
        ),
        TransitLine(
            lineNumber = "237",
            displayName = "Bus 237",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/237/route_2557.geojson",
            "files/geojson/Bus lines/buses/237/route_5315.geojson"
            )
        ),
        TransitLine(
            lineNumber = "242",
            displayName = "Bus 242",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/242/route_2120.geojson",
            "files/geojson/Bus lines/buses/242/route_2267.geojson"
            )
        ),
        TransitLine(
            lineNumber = "250",
            displayName = "Bus 250",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/250/route_2108.geojson",
            "files/geojson/Bus lines/buses/250/route_3388.geojson",
            "files/geojson/Bus lines/buses/250/route_3818.geojson"
            )
        ),
        TransitLine(
            lineNumber = "300",
            displayName = "Bus 300",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/300/route_1898.geojson",
            "files/geojson/Bus lines/buses/300/route_4284.geojson"
            )
        ),
        TransitLine(
            lineNumber = "301",
            displayName = "Bus 301",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/301/route_3587.geojson",
            "files/geojson/Bus lines/buses/301/route_5585.geojson",
            "files/geojson/Bus lines/buses/301/route_5586.geojson",
            "files/geojson/Bus lines/buses/301/route_5697.geojson",
            "files/geojson/Bus lines/buses/301/route_5698.geojson"
            )
        ),
        TransitLine(
            lineNumber = "301Β",
            displayName = "Bus 301Β",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/301Β/route_2544.geojson",
            "files/geojson/Bus lines/buses/301Β/route_2545.geojson",
            "files/geojson/Bus lines/buses/301Β/route_3267.geojson",
            "files/geojson/Bus lines/buses/301Β/route_3275.geojson"
            )
        ),
        TransitLine(
            lineNumber = "302",
            displayName = "Bus 302",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/302/route_3588.geojson",
            "files/geojson/Bus lines/buses/302/route_5633.geojson"
            )
        ),
        TransitLine(
            lineNumber = "304",
            displayName = "Bus 304",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/304/route_2055.geojson",
            "files/geojson/Bus lines/buses/304/route_2056.geojson"
            )
        ),
        TransitLine(
            lineNumber = "305",
            displayName = "Bus 305",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/305/route_2036.geojson",
            "files/geojson/Bus lines/buses/305/route_2037.geojson"
            )
        ),
        TransitLine(
            lineNumber = "306",
            displayName = "Bus 306",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/306/route_2427.geojson",
            "files/geojson/Bus lines/buses/306/route_3592.geojson",
            "files/geojson/Bus lines/buses/306/route_3946.geojson",
            "files/geojson/Bus lines/buses/306/route_4142.geojson"
            )
        ),
        TransitLine(
            lineNumber = "307",
            displayName = "Bus 307",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/307/route_2513.geojson",
            "files/geojson/Bus lines/buses/307/route_3984.geojson",
            "files/geojson/Bus lines/buses/307/route_5069.geojson",
            "files/geojson/Bus lines/buses/307/route_5071.geojson"
            )
        ),
        TransitLine(
            lineNumber = "308",
            displayName = "Bus 308",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/308/route_2059.geojson",
            "files/geojson/Bus lines/buses/308/route_2060.geojson",
            "files/geojson/Bus lines/buses/308/route_3974.geojson"
            )
        ),
        TransitLine(
            lineNumber = "309",
            displayName = "Bus 309",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/309/route_5053.geojson",
            "files/geojson/Bus lines/buses/309/route_5054.geojson",
            "files/geojson/Bus lines/buses/309/route_5335.geojson"
            )
        ),
        TransitLine(
            lineNumber = "309Β",
            displayName = "Bus 309Β",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/309Β/route_5559.geojson",
            "files/geojson/Bus lines/buses/309Β/route_5560.geojson"
            )
        ),
        TransitLine(
            lineNumber = "310",
            displayName = "Bus 310",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/310/route_4855.geojson",
            "files/geojson/Bus lines/buses/310/route_4861.geojson",
            "files/geojson/Bus lines/buses/310/route_4901.geojson",
            "files/geojson/Bus lines/buses/310/route_5686.geojson",
            "files/geojson/Bus lines/buses/310/route_5687.geojson",
            "files/geojson/Bus lines/buses/310/route_5688.geojson"
            )
        ),
        TransitLine(
            lineNumber = "311",
            displayName = "Bus 311",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/311/route_5366.geojson",
            "files/geojson/Bus lines/buses/311/route_5397.geojson",
            "files/geojson/Bus lines/buses/311/route_5398.geojson"
            )
        ),
        TransitLine(
            lineNumber = "314",
            displayName = "Bus 314",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/314/route_3100.geojson",
            "files/geojson/Bus lines/buses/314/route_3103.geojson",
            "files/geojson/Bus lines/buses/314/route_4462.geojson",
            "files/geojson/Bus lines/buses/314/route_4463.geojson",
            "files/geojson/Bus lines/buses/314/route_4714.geojson",
            "files/geojson/Bus lines/buses/314/route_4715.geojson",
            "files/geojson/Bus lines/buses/314/route_4824.geojson",
            "files/geojson/Bus lines/buses/314/route_4825.geojson",
            "files/geojson/Bus lines/buses/314/route_4872.geojson",
            "files/geojson/Bus lines/buses/314/route_4873.geojson",
            "files/geojson/Bus lines/buses/314/route_5396.geojson",
            "files/geojson/Bus lines/buses/314/route_5476.geojson"
            )
        ),
        TransitLine(
            lineNumber = "316",
            displayName = "Bus 316",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/316/route_2292.geojson",
            "files/geojson/Bus lines/buses/316/route_5301.geojson"
            )
        ),
        TransitLine(
            lineNumber = "319",
            displayName = "Bus 319",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/319/route_3620.geojson",
            "files/geojson/Bus lines/buses/319/route_3920.geojson",
            "files/geojson/Bus lines/buses/319/route_5127.geojson",
            "files/geojson/Bus lines/buses/319/route_5128.geojson",
            "files/geojson/Bus lines/buses/319/route_5612.geojson"
            )
        ),
        TransitLine(
            lineNumber = "323",
            displayName = "Bus 323",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/323/route_3342.geojson"
            )
        ),
        TransitLine(
            lineNumber = "324",
            displayName = "Bus 324",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/324/route_5701.geojson"
            )
        ),
        TransitLine(
            lineNumber = "326",
            displayName = "Bus 326",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/326/route_2770.geojson"
            )
        ),
        TransitLine(
            lineNumber = "330",
            displayName = "Bus 330",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/330/route_5052.geojson",
            "files/geojson/Bus lines/buses/330/route_5572.geojson"
            )
        ),
        TransitLine(
            lineNumber = "400",
            displayName = "Bus 400",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/400/route_4939.geojson",
            "files/geojson/Bus lines/buses/400/route_5202.geojson",
            "files/geojson/Bus lines/buses/400/route_5231.geojson",
            "files/geojson/Bus lines/buses/400/route_5232.geojson"
            )
        ),
        TransitLine(
            lineNumber = "402",
            displayName = "Bus 402",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/402/route_2068.geojson",
            "files/geojson/Bus lines/buses/402/route_2069.geojson"
            )
        ),
        TransitLine(
            lineNumber = "405",
            displayName = "Bus 405",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/405/route_2617.geojson",
            "files/geojson/Bus lines/buses/405/route_3711.geojson",
            "files/geojson/Bus lines/buses/405/route_3713.geojson"
            )
        ),
        TransitLine(
            lineNumber = "406",
            displayName = "Bus 406",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/406/route_4666.geojson",
            "files/geojson/Bus lines/buses/406/route_4667.geojson"
            )
        ),
        TransitLine(
            lineNumber = "407",
            displayName = "Bus 407",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/407/route_3252.geojson",
            "files/geojson/Bus lines/buses/407/route_3254.geojson"
            )
        ),
        TransitLine(
            lineNumber = "409",
            displayName = "Bus 409",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/409/route_4705.geojson",
            "files/geojson/Bus lines/buses/409/route_4712.geojson"
            )
        ),
        TransitLine(
            lineNumber = "410",
            displayName = "Bus 410",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/410/route_1963.geojson",
            "files/geojson/Bus lines/buses/410/route_2615.geojson",
            "files/geojson/Bus lines/buses/410/route_3394.geojson",
            "files/geojson/Bus lines/buses/410/route_3456.geojson",
            "files/geojson/Bus lines/buses/410/route_3457.geojson"
            )
        ),
        TransitLine(
            lineNumber = "411",
            displayName = "Bus 411",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/411/route_2326.geojson",
            "files/geojson/Bus lines/buses/411/route_2835.geojson",
            "files/geojson/Bus lines/buses/411/route_4167.geojson",
            "files/geojson/Bus lines/buses/411/route_4209.geojson"
            )
        ),
        TransitLine(
            lineNumber = "416",
            displayName = "Bus 416",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/416/route_2847.geojson",
            "files/geojson/Bus lines/buses/416/route_4290.geojson",
            "files/geojson/Bus lines/buses/416/route_4291.geojson",
            "files/geojson/Bus lines/buses/416/route_4292.geojson"
            )
        ),
        TransitLine(
            lineNumber = "418",
            displayName = "Bus 418",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/418/route_2848.geojson",
            "files/geojson/Bus lines/buses/418/route_2849.geojson"
            )
        ),
        TransitLine(
            lineNumber = "420",
            displayName = "Bus 420",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/420/route_5502.geojson",
            "files/geojson/Bus lines/buses/420/route_5672.geojson"
            )
        ),
        TransitLine(
            lineNumber = "421",
            displayName = "Bus 421",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/421/route_4508.geojson",
            "files/geojson/Bus lines/buses/421/route_4577.geojson"
            )
        ),
        TransitLine(
            lineNumber = "444",
            displayName = "Bus 444",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/444/route_4909.geojson",
            "files/geojson/Bus lines/buses/444/route_4910.geojson"
            )
        ),
        TransitLine(
            lineNumber = "446",
            displayName = "Bus 446",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/446/route_2169.geojson",
            "files/geojson/Bus lines/buses/446/route_2170.geojson"
            )
        ),
        TransitLine(
            lineNumber = "447",
            displayName = "Bus 447",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/447/route_1986.geojson",
            "files/geojson/Bus lines/buses/447/route_2838.geojson",
            "files/geojson/Bus lines/buses/447/route_3446.geojson"
            )
        ),
        TransitLine(
            lineNumber = "450",
            displayName = "Bus 450",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/450/route_2072.geojson",
            "files/geojson/Bus lines/buses/450/route_2073.geojson",
            "files/geojson/Bus lines/buses/450/route_3907.geojson"
            )
        ),
        TransitLine(
            lineNumber = "451",
            displayName = "Bus 451",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/451/route_2840.geojson",
            "files/geojson/Bus lines/buses/451/route_3647.geojson",
            "files/geojson/Bus lines/buses/451/route_3648.geojson",
            "files/geojson/Bus lines/buses/451/route_3908.geojson"
            )
        ),
        TransitLine(
            lineNumber = "460",
            displayName = "Bus 460",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/460/route_2127.geojson",
            "files/geojson/Bus lines/buses/460/route_2128.geojson"
            )
        ),
        TransitLine(
            lineNumber = "461",
            displayName = "Bus 461",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/461/route_2173.geojson",
            "files/geojson/Bus lines/buses/461/route_2797.geojson",
            "files/geojson/Bus lines/buses/461/route_3649.geojson",
            "files/geojson/Bus lines/buses/461/route_3650.geojson"
            )
        ),
        TransitLine(
            lineNumber = "500",
            displayName = "Bus 500",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/500/route_1889.geojson",
            "files/geojson/Bus lines/buses/500/route_1890.geojson",
            "files/geojson/Bus lines/buses/500/route_2783.geojson",
            "files/geojson/Bus lines/buses/500/route_2900.geojson"
            )
        ),
        TransitLine(
            lineNumber = "501",
            displayName = "Bus 501",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/501/route_2229.geojson"
            )
        ),
        TransitLine(
            lineNumber = "503",
            displayName = "Bus 503",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/503/route_2188.geojson",
            "files/geojson/Bus lines/buses/503/route_4273.geojson",
            "files/geojson/Bus lines/buses/503/route_4470.geojson"
            )
        ),
        TransitLine(
            lineNumber = "504",
            displayName = "Bus 504",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/504/route_4240.geojson"
            )
        ),
        TransitLine(
            lineNumber = "507",
            displayName = "Bus 507",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/507/route_1968.geojson",
            "files/geojson/Bus lines/buses/507/route_1969.geojson"
            )
        ),
        TransitLine(
            lineNumber = "509",
            displayName = "Bus 509",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/509/route_4706.geojson",
            "files/geojson/Bus lines/buses/509/route_4707.geojson",
            "files/geojson/Bus lines/buses/509/route_4708.geojson",
            "files/geojson/Bus lines/buses/509/route_4955.geojson",
            "files/geojson/Bus lines/buses/509/route_4957.geojson",
            "files/geojson/Bus lines/buses/509/route_4958.geojson"
            )
        ),
        TransitLine(
            lineNumber = "509Β",
            displayName = "Bus 509Β",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/509Β/route_5613.geojson",
            "files/geojson/Bus lines/buses/509Β/route_5614.geojson"
            )
        ),
        TransitLine(
            lineNumber = "522",
            displayName = "Bus 522",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/522/route_5391.geojson",
            "files/geojson/Bus lines/buses/522/route_5392.geojson"
            )
        ),
        TransitLine(
            lineNumber = "523",
            displayName = "Bus 523",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/523/route_2740.geojson",
            "files/geojson/Bus lines/buses/523/route_2994.geojson"
            )
        ),
        TransitLine(
            lineNumber = "524",
            displayName = "Bus 524",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/524/route_2289.geojson"
            )
        ),
        TransitLine(
            lineNumber = "526",
            displayName = "Bus 526",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/526/route_4741.geojson"
            )
        ),
        TransitLine(
            lineNumber = "527",
            displayName = "Bus 527",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/527/route_2230.geojson"
            )
        ),
        TransitLine(
            lineNumber = "530",
            displayName = "Bus 530",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/530/route_3589.geojson",
            "files/geojson/Bus lines/buses/530/route_3590.geojson",
            "files/geojson/Bus lines/buses/530/route_3591.geojson",
            "files/geojson/Bus lines/buses/530/route_3909.geojson",
            "files/geojson/Bus lines/buses/530/route_3910.geojson"
            )
        ),
        TransitLine(
            lineNumber = "535",
            displayName = "Bus 535",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/535/route_4489.geojson"
            )
        ),
        TransitLine(
            lineNumber = "535Α",
            displayName = "Bus 535Α",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/535Α/route_5491.geojson"
            )
        ),
        TransitLine(
            lineNumber = "536",
            displayName = "Bus 536",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/536/route_4196.geojson",
            "files/geojson/Bus lines/buses/536/route_4197.geojson"
            )
        ),
        TransitLine(
            lineNumber = "537",
            displayName = "Bus 537",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/537/route_4537.geojson",
            "files/geojson/Bus lines/buses/537/route_4559.geojson",
            "files/geojson/Bus lines/buses/537/route_4927.geojson",
            "files/geojson/Bus lines/buses/537/route_4928.geojson"
            )
        ),
        TransitLine(
            lineNumber = "541",
            displayName = "Bus 541",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/541/route_2117.geojson"
            )
        ),
        TransitLine(
            lineNumber = "543",
            displayName = "Bus 543",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/543/route_5434.geojson"
            )
        ),
        TransitLine(
            lineNumber = "543Α",
            displayName = "Bus 543Α",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/543Α/route_5435.geojson"
            )
        ),
        TransitLine(
            lineNumber = "550",
            displayName = "Bus 550",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/550/route_2951.geojson",
            "files/geojson/Bus lines/buses/550/route_4373.geojson"
            )
        ),
        TransitLine(
            lineNumber = "560",
            displayName = "Bus 560",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/560/route_1829.geojson",
            "files/geojson/Bus lines/buses/560/route_1830.geojson"
            )
        ),
        TransitLine(
            lineNumber = "602",
            displayName = "Bus 602",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/602/route_2143.geojson",
            "files/geojson/Bus lines/buses/602/route_4186.geojson",
            "files/geojson/Bus lines/buses/602/route_4187.geojson",
            "files/geojson/Bus lines/buses/602/route_4654.geojson"
            )
        ),
        TransitLine(
            lineNumber = "604",
            displayName = "Bus 604",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/604/route_5364.geojson"
            )
        ),
        TransitLine(
            lineNumber = "605",
            displayName = "Bus 605",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/605/route_1806.geojson",
            "files/geojson/Bus lines/buses/605/route_2509.geojson"
            )
        ),
        TransitLine(
            lineNumber = "608",
            displayName = "Bus 608",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/608/route_4178.geojson",
            "files/geojson/Bus lines/buses/608/route_4906.geojson",
            "files/geojson/Bus lines/buses/608/route_4907.geojson",
            "files/geojson/Bus lines/buses/608/route_4908.geojson",
            "files/geojson/Bus lines/buses/608/route_5346.geojson"
            )
        ),
        TransitLine(
            lineNumber = "610",
            displayName = "Bus 610",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/610/route_2144.geojson",
            "files/geojson/Bus lines/buses/610/route_4364.geojson",
            "files/geojson/Bus lines/buses/610/route_4365.geojson"
            )
        ),
        TransitLine(
            lineNumber = "619",
            displayName = "Bus 619",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/619/route_2219.geojson"
            )
        ),
        TransitLine(
            lineNumber = "622",
            displayName = "Bus 622",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/622/route_5432.geojson",
            "files/geojson/Bus lines/buses/622/route_5645.geojson"
            )
        ),
        TransitLine(
            lineNumber = "640",
            displayName = "Bus 640",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/640/route_2362.geojson",
            "files/geojson/Bus lines/buses/640/route_3198.geojson"
            )
        ),
        TransitLine(
            lineNumber = "642",
            displayName = "Bus 642",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/642/route_2182.geojson",
            "files/geojson/Bus lines/buses/642/route_4298.geojson"
            )
        ),
        TransitLine(
            lineNumber = "651",
            displayName = "Bus 651",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/651/route_4368.geojson",
            "files/geojson/Bus lines/buses/651/route_4947.geojson"
            )
        ),
        TransitLine(
            lineNumber = "653",
            displayName = "Bus 653",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/653/route_2157.geojson",
            "files/geojson/Bus lines/buses/653/route_4159.geojson",
            "files/geojson/Bus lines/buses/653/route_4300.geojson"
            )
        ),
        TransitLine(
            lineNumber = "700",
            displayName = "Bus 700",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/700/route_4370.geojson"
            )
        ),
        TransitLine(
            lineNumber = "701",
            displayName = "Bus 701",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/701/route_4969.geojson"
            )
        ),
        TransitLine(
            lineNumber = "702",
            displayName = "Bus 702",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/702/route_2295.geojson",
            "files/geojson/Bus lines/buses/702/route_3917.geojson"
            )
        ),
        TransitLine(
            lineNumber = "703",
            displayName = "Bus 703",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/703/route_3794.geojson",
            "files/geojson/Bus lines/buses/703/route_5229.geojson",
            "files/geojson/Bus lines/buses/703/route_5578.geojson",
            "files/geojson/Bus lines/buses/703/route_5579.geojson"
            )
        ),
        TransitLine(
            lineNumber = "704",
            displayName = "Bus 704",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/704/route_5175.geojson"
            )
        ),
        TransitLine(
            lineNumber = "705",
            displayName = "Bus 705",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/705/route_4970.geojson"
            )
        ),
        TransitLine(
            lineNumber = "706",
            displayName = "Bus 706",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/706/route_5058.geojson",
            "files/geojson/Bus lines/buses/706/route_5059.geojson",
            "files/geojson/Bus lines/buses/706/route_5060.geojson"
            )
        ),
        TransitLine(
            lineNumber = "709",
            displayName = "Bus 709",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/709/route_3385.geojson"
            )
        ),
        TransitLine(
            lineNumber = "711",
            displayName = "Bus 711",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/711/route_3489.geojson",
            "files/geojson/Bus lines/buses/711/route_3490.geojson"
            )
        ),
        TransitLine(
            lineNumber = "712",
            displayName = "Bus 712",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/712/route_5211.geojson",
            "files/geojson/Bus lines/buses/712/route_5362.geojson"
            )
        ),
        TransitLine(
            lineNumber = "719",
            displayName = "Bus 719",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/719/route_5673.geojson"
            )
        ),
        TransitLine(
            lineNumber = "720",
            displayName = "Bus 720",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/720/route_5356.geojson",
            "files/geojson/Bus lines/buses/720/route_5357.geojson"
            )
        ),
        TransitLine(
            lineNumber = "721",
            displayName = "Bus 721",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/721/route_4879.geojson"
            )
        ),
        TransitLine(
            lineNumber = "723",
            displayName = "Bus 723",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/723/route_5689.geojson"
            )
        ),
        TransitLine(
            lineNumber = "724",
            displayName = "Bus 724",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/724/route_2447.geojson"
            )
        ),
        TransitLine(
            lineNumber = "725",
            displayName = "Bus 725",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/725/route_4328.geojson",
            "files/geojson/Bus lines/buses/725/route_4358.geojson"
            )
        ),
        TransitLine(
            lineNumber = "726",
            displayName = "Bus 726",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/726/route_2654.geojson"
            )
        ),
        TransitLine(
            lineNumber = "727",
            displayName = "Bus 727",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/727/route_2477.geojson",
            "files/geojson/Bus lines/buses/727/route_4036.geojson",
            "files/geojson/Bus lines/buses/727/route_4897.geojson"
            )
        ),
        TransitLine(
            lineNumber = "728",
            displayName = "Bus 728",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/728/route_4401.geojson",
            "files/geojson/Bus lines/buses/728/route_4402.geojson",
            "files/geojson/Bus lines/buses/728/route_4403.geojson",
            "files/geojson/Bus lines/buses/728/route_4409.geojson",
            "files/geojson/Bus lines/buses/728/route_4530.geojson"
            )
        ),
        TransitLine(
            lineNumber = "730",
            displayName = "Bus 730",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/730/route_2878.geojson",
            "files/geojson/Bus lines/buses/730/route_4305.geojson",
            "files/geojson/Bus lines/buses/730/route_5463.geojson",
            "files/geojson/Bus lines/buses/730/route_5464.geojson"
            )
        ),
        TransitLine(
            lineNumber = "731",
            displayName = "Bus 731",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/731/route_1851.geojson",
            "files/geojson/Bus lines/buses/731/route_2445.geojson",
            "files/geojson/Bus lines/buses/731/route_3468.geojson",
            "files/geojson/Bus lines/buses/731/route_4662.geojson"
            )
        ),
        TransitLine(
            lineNumber = "732",
            displayName = "Bus 732",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/732/route_4361.geojson",
            "files/geojson/Bus lines/buses/732/route_4362.geojson"
            )
        ),
        TransitLine(
            lineNumber = "733",
            displayName = "Bus 733",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/733/route_2226.geojson"
            )
        ),
        TransitLine(
            lineNumber = "734",
            displayName = "Bus 734",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/734/route_5094.geojson"
            )
        ),
        TransitLine(
            lineNumber = "735",
            displayName = "Bus 735",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/735/route_5131.geojson"
            )
        ),
        TransitLine(
            lineNumber = "740",
            displayName = "Bus 740",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/740/route_3241.geojson",
            "files/geojson/Bus lines/buses/740/route_3245.geojson",
            "files/geojson/Bus lines/buses/740/route_3259.geojson"
            )
        ),
        TransitLine(
            lineNumber = "747",
            displayName = "Bus 747",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/747/route_4329.geojson",
            "files/geojson/Bus lines/buses/747/route_4971.geojson",
            "files/geojson/Bus lines/buses/747/route_4972.geojson"
            )
        ),
        TransitLine(
            lineNumber = "748",
            displayName = "Bus 748",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/748/route_1828.geojson",
            "files/geojson/Bus lines/buses/748/route_3338.geojson",
            "files/geojson/Bus lines/buses/748/route_3486.geojson"
            )
        ),
        TransitLine(
            lineNumber = "749",
            displayName = "Bus 749",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/749/route_4414.geojson",
            "files/geojson/Bus lines/buses/749/route_4415.geojson"
            )
        ),
        TransitLine(
            lineNumber = "750",
            displayName = "Bus 750",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/750/route_1899.geojson",
            "files/geojson/Bus lines/buses/750/route_3463.geojson",
            "files/geojson/Bus lines/buses/750/route_3630.geojson",
            "files/geojson/Bus lines/buses/750/route_3631.geojson",
            "files/geojson/Bus lines/buses/750/route_3632.geojson",
            "files/geojson/Bus lines/buses/750/route_4551.geojson",
            "files/geojson/Bus lines/buses/750/route_4552.geojson"
            )
        ),
        TransitLine(
            lineNumber = "752",
            displayName = "Bus 752",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/752/route_3378.geojson",
            "files/geojson/Bus lines/buses/752/route_3379.geojson",
            "files/geojson/Bus lines/buses/752/route_3876.geojson",
            "files/geojson/Bus lines/buses/752/route_4880.geojson"
            )
        ),
        TransitLine(
            lineNumber = "755",
            displayName = "Bus 755",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/755/route_4512.geojson",
            "files/geojson/Bus lines/buses/755/route_4513.geojson"
            )
        ),
        TransitLine(
            lineNumber = "755Β",
            displayName = "Bus 755Β",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/755Β/route_4514.geojson",
            "files/geojson/Bus lines/buses/755Β/route_4515.geojson"
            )
        ),
        TransitLine(
            lineNumber = "790",
            displayName = "Bus 790",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/790/route_5134.geojson",
            "files/geojson/Bus lines/buses/790/route_5135.geojson"
            )
        ),
        TransitLine(
            lineNumber = "800",
            displayName = "Bus 800",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/800/route_4731.geojson"
            )
        ),
        TransitLine(
            lineNumber = "801",
            displayName = "Bus 801",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/801/route_5074.geojson"
            )
        ),
        TransitLine(
            lineNumber = "803",
            displayName = "Bus 803",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/803/route_3345.geojson",
            "files/geojson/Bus lines/buses/803/route_3347.geojson",
            "files/geojson/Bus lines/buses/803/route_3477.geojson",
            "files/geojson/Bus lines/buses/803/route_3478.geojson",
            "files/geojson/Bus lines/buses/803/route_4663.geojson"
            )
        ),
        TransitLine(
            lineNumber = "805",
            displayName = "Bus 805",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/805/route_3849.geojson",
            "files/geojson/Bus lines/buses/805/route_4603.geojson",
            "files/geojson/Bus lines/buses/805/route_4935.geojson"
            )
        ),
        TransitLine(
            lineNumber = "806",
            displayName = "Bus 806",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/806/route_4307.geojson",
            "files/geojson/Bus lines/buses/806/route_4822.geojson"
            )
        ),
        TransitLine(
            lineNumber = "807",
            displayName = "Bus 807",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/807/route_4820.geojson"
            )
        ),
        TransitLine(
            lineNumber = "809",
            displayName = "Bus 809",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/809/route_4314.geojson",
            "files/geojson/Bus lines/buses/809/route_4823.geojson"
            )
        ),
        TransitLine(
            lineNumber = "810",
            displayName = "Bus 810",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/810/route_4844.geojson",
            "files/geojson/Bus lines/buses/810/route_4918.geojson"
            )
        ),
        TransitLine(
            lineNumber = "811",
            displayName = "Bus 811",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/811/route_4863.geojson",
            "files/geojson/Bus lines/buses/811/route_4864.geojson",
            "files/geojson/Bus lines/buses/811/route_4865.geojson",
            "files/geojson/Bus lines/buses/811/route_4866.geojson"
            )
        ),
        TransitLine(
            lineNumber = "813",
            displayName = "Bus 813",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/813/route_1770.geojson",
            "files/geojson/Bus lines/buses/813/route_1771.geojson"
            )
        ),
        TransitLine(
            lineNumber = "814",
            displayName = "Bus 814",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/814/route_4481.geojson",
            "files/geojson/Bus lines/buses/814/route_4482.geojson",
            "files/geojson/Bus lines/buses/814/route_5574.geojson"
            )
        ),
        TransitLine(
            lineNumber = "815",
            displayName = "Bus 815",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/815/route_5433.geojson",
            "files/geojson/Bus lines/buses/815/route_5646.geojson"
            )
        ),
        TransitLine(
            lineNumber = "816",
            displayName = "Bus 816",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/816/route_2228.geojson",
            "files/geojson/Bus lines/buses/816/route_2811.geojson"
            )
        ),
        TransitLine(
            lineNumber = "817",
            displayName = "Bus 817",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/817/route_3040.geojson",
            "files/geojson/Bus lines/buses/817/route_3041.geojson"
            )
        ),
        TransitLine(
            lineNumber = "818",
            displayName = "Bus 818",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/818/route_2000.geojson",
            "files/geojson/Bus lines/buses/818/route_4341.geojson"
            )
        ),
        TransitLine(
            lineNumber = "819",
            displayName = "Bus 819",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/819/route_4000.geojson",
            "files/geojson/Bus lines/buses/819/route_4023.geojson",
            "files/geojson/Bus lines/buses/819/route_5150.geojson"
            )
        ),
        TransitLine(
            lineNumber = "820",
            displayName = "Bus 820",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/820/route_3576.geojson"
            )
        ),
        TransitLine(
            lineNumber = "821",
            displayName = "Bus 821",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/821/route_1916.geojson"
            )
        ),
        TransitLine(
            lineNumber = "822",
            displayName = "Bus 822",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/822/route_2712.geojson",
            "files/geojson/Bus lines/buses/822/route_4930.geojson",
            "files/geojson/Bus lines/buses/822/route_4931.geojson",
            "files/geojson/Bus lines/buses/822/route_4932.geojson"
            )
        ),
        TransitLine(
            lineNumber = "823",
            displayName = "Bus 823",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/823/route_4934.geojson",
            "files/geojson/Bus lines/buses/823/route_5399.geojson"
            )
        ),
        TransitLine(
            lineNumber = "824",
            displayName = "Bus 824",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/824/route_1891.geojson",
            "files/geojson/Bus lines/buses/824/route_3515.geojson",
            "files/geojson/Bus lines/buses/824/route_4344.geojson"
            )
        ),
        TransitLine(
            lineNumber = "825",
            displayName = "Bus 825",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/825/route_4625.geojson",
            "files/geojson/Bus lines/buses/825/route_4627.geojson",
            "files/geojson/Bus lines/buses/825/route_5192.geojson"
            )
        ),
        TransitLine(
            lineNumber = "826",
            displayName = "Bus 826",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/826/route_3586.geojson",
            "files/geojson/Bus lines/buses/826/route_4345.geojson",
            "files/geojson/Bus lines/buses/826/route_4346.geojson",
            "files/geojson/Bus lines/buses/826/route_4597.geojson"
            )
        ),
        TransitLine(
            lineNumber = "827",
            displayName = "Bus 827",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/827/route_5080.geojson"
            )
        ),
        TransitLine(
            lineNumber = "828",
            displayName = "Bus 828",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/828/route_4367.geojson"
            )
        ),
        TransitLine(
            lineNumber = "829",
            displayName = "Bus 829",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/829/route_4583.geojson",
            "files/geojson/Bus lines/buses/829/route_5503.geojson",
            "files/geojson/Bus lines/buses/829/route_5504.geojson",
            "files/geojson/Bus lines/buses/829/route_5570.geojson"
            )
        ),
        TransitLine(
            lineNumber = "830",
            displayName = "Bus 830",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/830/route_4475.geojson",
            "files/geojson/Bus lines/buses/830/route_4518.geojson",
            "files/geojson/Bus lines/buses/830/route_4519.geojson"
            )
        ),
        TransitLine(
            lineNumber = "831",
            displayName = "Bus 831",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/831/route_1886.geojson",
            "files/geojson/Bus lines/buses/831/route_2805.geojson",
            "files/geojson/Bus lines/buses/831/route_3636.geojson",
            "files/geojson/Bus lines/buses/831/route_3637.geojson",
            "files/geojson/Bus lines/buses/831/route_4527.geojson",
            "files/geojson/Bus lines/buses/831/route_4528.geojson"
            )
        ),
        TransitLine(
            lineNumber = "832",
            displayName = "Bus 832",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/832/route_2458.geojson",
            "files/geojson/Bus lines/buses/832/route_2646.geojson"
            )
        ),
        TransitLine(
            lineNumber = "833",
            displayName = "Bus 833",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/833/route_2644.geojson",
            "files/geojson/Bus lines/buses/833/route_2758.geojson"
            )
        ),
        TransitLine(
            lineNumber = "836",
            displayName = "Bus 836",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/836/route_5496.geojson",
            "files/geojson/Bus lines/buses/836/route_5497.geojson"
            )
        ),
        TransitLine(
            lineNumber = "837",
            displayName = "Bus 837",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/837/route_3582.geojson",
            "files/geojson/Bus lines/buses/837/route_4296.geojson",
            "files/geojson/Bus lines/buses/837/route_4526.geojson"
            )
        ),
        TransitLine(
            lineNumber = "838",
            displayName = "Bus 838",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/838/route_5082.geojson",
            "files/geojson/Bus lines/buses/838/route_5083.geojson"
            )
        ),
        TransitLine(
            lineNumber = "841",
            displayName = "Bus 841",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/841/route_4027.geojson",
            "files/geojson/Bus lines/buses/841/route_4030.geojson",
            "files/geojson/Bus lines/buses/841/route_4032.geojson"
            )
        ),
        TransitLine(
            lineNumber = "842",
            displayName = "Bus 842",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/842/route_4028.geojson",
            "files/geojson/Bus lines/buses/842/route_4031.geojson"
            )
        ),
        TransitLine(
            lineNumber = "843",
            displayName = "Bus 843",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/843/route_4003.geojson",
            "files/geojson/Bus lines/buses/843/route_4026.geojson"
            )
        ),
        TransitLine(
            lineNumber = "845",
            displayName = "Bus 845",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/845/route_4252.geojson",
            "files/geojson/Bus lines/buses/845/route_4867.geojson"
            )
        ),
        TransitLine(
            lineNumber = "846",
            displayName = "Bus 846",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/846/route_4476.geojson",
            "files/geojson/Bus lines/buses/846/route_4477.geojson",
            "files/geojson/Bus lines/buses/846/route_4520.geojson"
            )
        ),
        TransitLine(
            lineNumber = "848",
            displayName = "Bus 848",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/848/route_4479.geojson",
            "files/geojson/Bus lines/buses/848/route_4480.geojson"
            )
        ),
        TransitLine(
            lineNumber = "852",
            displayName = "Bus 852",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/852/route_3629.geojson",
            "files/geojson/Bus lines/buses/852/route_4395.geojson"
            )
        ),
        TransitLine(
            lineNumber = "855",
            displayName = "Bus 855",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/855/route_1816.geojson",
            "files/geojson/Bus lines/buses/855/route_1817.geojson",
            "files/geojson/Bus lines/buses/855/route_3850.geojson",
            "files/geojson/Bus lines/buses/855/route_4257.geojson",
            "files/geojson/Bus lines/buses/855/route_4258.geojson"
            )
        ),
        TransitLine(
            lineNumber = "856",
            displayName = "Bus 856",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/856/route_1892.geojson",
            "files/geojson/Bus lines/buses/856/route_3625.geojson",
            "files/geojson/Bus lines/buses/856/route_3688.geojson"
            )
        ),
        TransitLine(
            lineNumber = "859",
            displayName = "Bus 859",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/859/route_2645.geojson",
            "files/geojson/Bus lines/buses/859/route_4387.geojson"
            )
        ),
        TransitLine(
            lineNumber = "860",
            displayName = "Bus 860",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/860/route_5537.geojson",
            "files/geojson/Bus lines/buses/860/route_5538.geojson",
            "files/geojson/Bus lines/buses/860/route_5539.geojson"
            )
        ),
        TransitLine(
            lineNumber = "861",
            displayName = "Bus 861",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/861/route_4234.geojson",
            "files/geojson/Bus lines/buses/861/route_4565.geojson"
            )
        ),
        TransitLine(
            lineNumber = "861Β",
            displayName = "Bus 861Β",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/861Β/route_5129.geojson",
            "files/geojson/Bus lines/buses/861Β/route_5130.geojson"
            )
        ),
        TransitLine(
            lineNumber = "862",
            displayName = "Bus 862",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/862/route_2146.geojson",
            "files/geojson/Bus lines/buses/862/route_2150.geojson"
            )
        ),
        TransitLine(
            lineNumber = "863",
            displayName = "Bus 863",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/863/route_4544.geojson",
            "files/geojson/Bus lines/buses/863/route_4545.geojson"
            )
        ),
        TransitLine(
            lineNumber = "864",
            displayName = "Bus 864",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/864/route_2293.geojson"
            )
        ),
        TransitLine(
            lineNumber = "865",
            displayName = "Bus 865",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/865/route_4604.geojson"
            )
        ),
        TransitLine(
            lineNumber = "866",
            displayName = "Bus 866",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/866/route_5436.geojson",
            "files/geojson/Bus lines/buses/866/route_5437.geojson",
            "files/geojson/Bus lines/buses/866/route_5438.geojson",
            "files/geojson/Bus lines/buses/866/route_5439.geojson",
            "files/geojson/Bus lines/buses/866/route_5440.geojson"
            )
        ),
        TransitLine(
            lineNumber = "868",
            displayName = "Bus 868",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/868/route_4702.geojson",
            "files/geojson/Bus lines/buses/868/route_4703.geojson"
            )
        ),
        TransitLine(
            lineNumber = "871",
            displayName = "Bus 871",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/871/route_4254.geojson",
            "files/geojson/Bus lines/buses/871/route_5164.geojson"
            )
        ),
        TransitLine(
            lineNumber = "871Τ",
            displayName = "Bus 871Τ",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/871Τ/route_5165.geojson",
            "files/geojson/Bus lines/buses/871Τ/route_5166.geojson"
            )
        ),
        TransitLine(
            lineNumber = "876",
            displayName = "Bus 876",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/876/route_3874.geojson",
            "files/geojson/Bus lines/buses/876/route_4255.geojson",
            "files/geojson/Bus lines/buses/876/route_4267.geojson",
            "files/geojson/Bus lines/buses/876/route_4268.geojson",
            "files/geojson/Bus lines/buses/876/route_4334.geojson"
            )
        ),
        TransitLine(
            lineNumber = "878",
            displayName = "Bus 878",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/878/route_5169.geojson",
            "files/geojson/Bus lines/buses/878/route_5441.geojson",
            "files/geojson/Bus lines/buses/878/route_5442.geojson"
            )
        ),
        TransitLine(
            lineNumber = "879",
            displayName = "Bus 879",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/879/route_5108.geojson",
            "files/geojson/Bus lines/buses/879/route_5170.geojson"
            )
        ),
        TransitLine(
            lineNumber = "881",
            displayName = "Bus 881",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/881/route_4826.geojson",
            "files/geojson/Bus lines/buses/881/route_4827.geojson",
            "files/geojson/Bus lines/buses/881/route_4828.geojson",
            "files/geojson/Bus lines/buses/881/route_4829.geojson"
            )
        ),
        TransitLine(
            lineNumber = "890",
            displayName = "Bus 890",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/890/route_4374.geojson",
            "files/geojson/Bus lines/buses/890/route_4376.geojson"
            )
        ),
        TransitLine(
            lineNumber = "891",
            displayName = "Bus 891",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/891/route_2857.geojson"
            )
        ),
        TransitLine(
            lineNumber = "892",
            displayName = "Bus 892",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/892/route_2858.geojson",
            "files/geojson/Bus lines/buses/892/route_4405.geojson",
            "files/geojson/Bus lines/buses/892/route_4407.geojson",
            "files/geojson/Bus lines/buses/892/route_5178.geojson"
            )
        ),
        TransitLine(
            lineNumber = "904",
            displayName = "Bus 904",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/904/route_2162.geojson"
            )
        ),
        TransitLine(
            lineNumber = "906",
            displayName = "Bus 906",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/906/route_1895.geojson",
            "files/geojson/Bus lines/buses/906/route_4250.geojson"
            )
        ),
        TransitLine(
            lineNumber = "909",
            displayName = "Bus 909",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/909/route_4369.geojson",
            "files/geojson/Bus lines/buses/909/route_4372.geojson",
            "files/geojson/Bus lines/buses/909/route_4483.geojson",
            "files/geojson/Bus lines/buses/909/route_4484.geojson",
            "files/geojson/Bus lines/buses/909/route_4485.geojson"
            )
        ),
        TransitLine(
            lineNumber = "910",
            displayName = "Bus 910",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/910/route_2551.geojson",
            "files/geojson/Bus lines/buses/910/route_4299.geojson",
            "files/geojson/Bus lines/buses/910/route_4301.geojson"
            )
        ),
        TransitLine(
            lineNumber = "911",
            displayName = "Bus 911",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/911/route_2274.geojson"
            )
        ),
        TransitLine(
            lineNumber = "911Α",
            displayName = "Bus 911Α",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/911Α/route_3820.geojson"
            )
        ),
        TransitLine(
            lineNumber = "912",
            displayName = "Bus 912",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/912/route_5661.geojson"
            )
        ),
        TransitLine(
            lineNumber = "914",
            displayName = "Bus 914",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/914/route_4287.geojson",
            "files/geojson/Bus lines/buses/914/route_4356.geojson",
            "files/geojson/Bus lines/buses/914/route_4386.geojson"
            )
        ),
        TransitLine(
            lineNumber = "915",
            displayName = "Bus 915",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/915/route_3476.geojson",
            "files/geojson/Bus lines/buses/915/route_4359.geojson",
            "files/geojson/Bus lines/buses/915/route_4423.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α1",
            displayName = "Bus Α1",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α1/route_5517.geojson",
            "files/geojson/Bus lines/buses/Α1/route_5518.geojson",
            "files/geojson/Bus lines/buses/Α1/route_5561.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α10",
            displayName = "Bus Α10",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α10/route_3690.geojson",
            "files/geojson/Bus lines/buses/Α10/route_3691.geojson",
            "files/geojson/Bus lines/buses/Α10/route_4677.geojson",
            "files/geojson/Bus lines/buses/Α10/route_4678.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α11",
            displayName = "Bus Α11",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α11/route_5270.geojson",
            "files/geojson/Bus lines/buses/Α11/route_5271.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α13",
            displayName = "Bus Α13",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α13/route_1873.geojson",
            "files/geojson/Bus lines/buses/Α13/route_3536.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α15",
            displayName = "Bus Α15",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α15/route_1837.geojson",
            "files/geojson/Bus lines/buses/Α15/route_2980.geojson",
            "files/geojson/Bus lines/buses/Α15/route_4664.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α2",
            displayName = "Bus Α2",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α2/route_5566.geojson",
            "files/geojson/Bus lines/buses/Α2/route_5567.geojson",
            "files/geojson/Bus lines/buses/Α2/route_5568.geojson",
            "files/geojson/Bus lines/buses/Α2/route_5569.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α3",
            displayName = "Bus Α3",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α3/route_3614.geojson",
            "files/geojson/Bus lines/buses/Α3/route_5349.geojson",
            "files/geojson/Bus lines/buses/Α3/route_5350.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α5",
            displayName = "Bus Α5",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α5/route_2138.geojson",
            "files/geojson/Bus lines/buses/Α5/route_3609.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α7",
            displayName = "Bus Α7",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α7/route_2085.geojson",
            "files/geojson/Bus lines/buses/Α7/route_2086.geojson",
            "files/geojson/Bus lines/buses/Α7/route_4622.geojson",
            "files/geojson/Bus lines/buses/Α7/route_4623.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Α8",
            displayName = "Bus Α8",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Α8/route_1824.geojson",
            "files/geojson/Bus lines/buses/Α8/route_1825.geojson",
            "files/geojson/Bus lines/buses/Α8/route_2598.geojson",
            "files/geojson/Bus lines/buses/Α8/route_2599.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Β1",
            displayName = "Bus Β1",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Β1/route_5519.geojson",
            "files/geojson/Bus lines/buses/Β1/route_5520.geojson",
            "files/geojson/Bus lines/buses/Β1/route_5521.geojson",
            "files/geojson/Bus lines/buses/Β1/route_5522.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Β10",
            displayName = "Bus Β10",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Β10/route_2137.geojson",
            "files/geojson/Bus lines/buses/Β10/route_3758.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Β11",
            displayName = "Bus Β11",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Β11/route_5272.geojson",
            "files/geojson/Bus lines/buses/Β11/route_5273.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Β12",
            displayName = "Bus Β12",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Β12/route_3491.geojson",
            "files/geojson/Bus lines/buses/Β12/route_3693.geojson",
            "files/geojson/Bus lines/buses/Β12/route_5674.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Β15",
            displayName = "Bus Β15",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Β15/route_2204.geojson",
            "files/geojson/Bus lines/buses/Β15/route_2979.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Β2",
            displayName = "Bus Β2",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Β2/route_5662.geojson",
            "files/geojson/Bus lines/buses/Β2/route_5666.geojson",
            "files/geojson/Bus lines/buses/Β2/route_5667.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Β5",
            displayName = "Bus Β5",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Β5/route_2167.geojson",
            "files/geojson/Bus lines/buses/Β5/route_2168.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Β9",
            displayName = "Bus Β9",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Β9/route_2042.geojson",
            "files/geojson/Bus lines/buses/Β9/route_2043.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Γ12",
            displayName = "Bus Γ12",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Γ12/route_5101.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Ε14",
            displayName = "Bus Ε14",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Ε14/route_3487.geojson",
            "files/geojson/Bus lines/buses/Ε14/route_3488.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Ε90",
            displayName = "Bus Ε90",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Ε90/route_4941.geojson",
            "files/geojson/Bus lines/buses/Ε90/route_5534.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Χ14",
            displayName = "Bus Χ14",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Χ14/route_2184.geojson",
            "files/geojson/Bus lines/buses/Χ14/route_2185.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Χ93",
            displayName = "Bus Χ93",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Χ93/route_5675.geojson",
            "files/geojson/Bus lines/buses/Χ93/route_5676.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Χ95",
            displayName = "Bus Χ95",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Χ95/route_2051.geojson",
            "files/geojson/Bus lines/buses/Χ95/route_2052.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Χ96",
            displayName = "Bus Χ96",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Χ96/route_3008.geojson",
            "files/geojson/Bus lines/buses/Χ96/route_3196.geojson",
            "files/geojson/Bus lines/buses/Χ96/route_5532.geojson",
            "files/geojson/Bus lines/buses/Χ96/route_5533.geojson"
            )
        ),
        TransitLine(
            lineNumber = "Χ97",
            displayName = "Bus Χ97",
            category = TransitCategory.BUS,
            routePaths = listOf(
            "files/geojson/Bus lines/buses/Χ97/route_5373.geojson",
            "files/geojson/Bus lines/buses/Χ97/route_5374.geojson",
            "files/geojson/Bus lines/buses/Χ97/route_5375.geojson",
            "files/geojson/Bus lines/buses/Χ97/route_5376.geojson"
            )
        )
        )
    }
    
    /**
     * Get all Trolley lines
     */
    fun getTrolleyLines(): List<TransitLine> {
        return listOf(
        TransitLine(
            lineNumber = "1",
            displayName = "Trolley 1",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/1/route_5529.geojson",
            "files/geojson/Bus lines/trolleys/1/route_5530.geojson"
            )
        ),
        TransitLine(
            lineNumber = "10",
            displayName = "Trolley 10",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/10/route_1994.geojson",
            "files/geojson/Bus lines/trolleys/10/route_5510.geojson"
            )
        ),
        TransitLine(
            lineNumber = "11",
            displayName = "Trolley 11",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/11/route_1995.geojson",
            "files/geojson/Bus lines/trolleys/11/route_4913.geojson"
            )
        ),
        TransitLine(
            lineNumber = "12",
            displayName = "Trolley 12",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/12/route_3530.geojson",
            "files/geojson/Bus lines/trolleys/12/route_5086.geojson"
            )
        ),
        TransitLine(
            lineNumber = "14 ",
            displayName = "Trolley 14 ",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/14 /route_1997.geojson",
            "files/geojson/Bus lines/trolleys/14 /route_1998.geojson"
            )
        ),
        TransitLine(
            lineNumber = "15",
            displayName = "Trolley 15",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/15/route_2013.geojson",
            "files/geojson/Bus lines/trolleys/15/route_3145.geojson",
            "files/geojson/Bus lines/trolleys/15/route_4242.geojson",
            "files/geojson/Bus lines/trolleys/15/route_4540.geojson"
            )
        ),
        TransitLine(
            lineNumber = "16",
            displayName = "Trolley 16",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/16/route_2079.geojson"
            )
        ),
        TransitLine(
            lineNumber = "17",
            displayName = "Trolley 17",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/17/route_5137.geojson"
            )
        ),
        TransitLine(
            lineNumber = "18",
            displayName = "Trolley 18",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/18/route_2027.geojson",
            "files/geojson/Bus lines/trolleys/18/route_2028.geojson"
            )
        ),
        TransitLine(
            lineNumber = "19",
            displayName = "Trolley 19",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/19/route_2029.geojson",
            "files/geojson/Bus lines/trolleys/19/route_2030.geojson"
            )
        ),
        TransitLine(
            lineNumber = "19Β",
            displayName = "Trolley 19Β",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/19Β/route_4044.geojson"
            )
        ),
        TransitLine(
            lineNumber = "2",
            displayName = "Trolley 2",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/2/route_4914.geojson",
            "files/geojson/Bus lines/trolleys/2/route_5196.geojson"
            )
        ),
        TransitLine(
            lineNumber = "20",
            displayName = "Trolley 20",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/20/route_1961.geojson",
            "files/geojson/Bus lines/trolleys/20/route_3766.geojson"
            )
        ),
        TransitLine(
            lineNumber = "21",
            displayName = "Trolley 21",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/21/route_3627.geojson"
            )
        ),
        TransitLine(
            lineNumber = "24",
            displayName = "Trolley 24",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/24/route_1962.geojson"
            )
        ),
        TransitLine(
            lineNumber = "25",
            displayName = "Trolley 25",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/25/route_4293.geojson",
            "files/geojson/Bus lines/trolleys/25/route_5102.geojson"
            )
        ),
        TransitLine(
            lineNumber = "3",
            displayName = "Trolley 3",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/3/route_1989.geojson",
            "files/geojson/Bus lines/trolleys/3/route_1990.geojson"
            )
        ),
        TransitLine(
            lineNumber = "4",
            displayName = "Trolley 4",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/4/route_2024.geojson",
            "files/geojson/Bus lines/trolleys/4/route_4915.geojson"
            )
        ),
        TransitLine(
            lineNumber = "5",
            displayName = "Trolley 5",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/5/route_1991.geojson",
            "files/geojson/Bus lines/trolleys/5/route_1992.geojson"
            )
        ),
        TransitLine(
            lineNumber = "6",
            displayName = "Trolley 6",
            category = TransitCategory.TROLLEY,
            routePaths = listOf(
            "files/geojson/Bus lines/trolleys/6/route_1987.geojson",
            "files/geojson/Bus lines/trolleys/6/route_1988.geojson"
            )
        )
        )
    }
    
    /**
     * Get all lines (for search)
     */
    fun getAllLines(): List<TransitLine> {
        return getMetroLines() + 
               getTramLines() + 
               getSuburbanLines() + 
               getBusLines() + 
               getTrolleyLines()
    }
    
    /**
     * Search lines by line number or display name
     */
    fun searchLines(query: String): List<TransitLine> {
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isEmpty()) {
            return emptyList()
        }
        
        return getAllLines().filter {
            it.lineNumber.lowercase().contains(normalizedQuery) ||
            it.displayName.lowercase().contains(normalizedQuery)
        }
    }
}

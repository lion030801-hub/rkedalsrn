fun rawToKpa(raw: Int, unit: String): Double? {
    if (raw !in 0..254) return null                        // 255 = 무효
    return when (unit.lowercase()) {
        "psi" -> raw * 6.894757
        "kpa" -> raw * 5.0
        "bar" -> raw / 10.0 * 100.0
        else -> null
    }
}
// 권장 공기압 ±15% 밖이면 CHECK(점검 필요), 권장값 없으면 값만 표시, 데이터 없으면 "조회 불가"

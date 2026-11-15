override suspend fun tripCumulativeDrivingDistanceKm(): Double? =
    (call(listOf("getTripCumulativeDrivingDistance")) as? Number)?.toDouble()?.times(DISTANCE_TO_KM)
// 브레이크 페달·속도·기어는 후보 메서드 이름 목록에서 찾고, 없으면 null("조회 불가")

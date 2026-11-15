class WearEstimator(private val m: CostModel = CostModel()) {
    fun estimate(s: TripSummary): WearEstimate? {
        val km = s.distanceKm ?: return null              // 거리 신호 없으면 금액을 만들지 않음
        val hard = s.hardBrakeEvents
        val normal = (s.brakeEvents - (hard ?: 0)).coerceAtLeast(0)
        val load = normal + m.hardWeight * (hard ?: 0)

        val brakeFactor = (load / km / m.baselineBrakeLoadPerKm).coerceIn(0.5, 2.5)
        val tireFactor = if (hard != null)
            (1.0 + 0.5 * ((hard / km) / m.baselineHardPerKm - 1.0)).coerceIn(0.75, 1.75) else 1.0

        val padBase  = m.padPriceWon  / m.padLifeKm  * km
        val tireBase = m.tirePriceWon / m.tireLifeKm * km
        val padSaved  = max(0.0, padBase  - padBase  * brakeFactor)   // 기준 주행 대비 절약
        val tireSaved = max(0.0, tireBase - tireBase * tireFactor)
        return WearEstimate(km, padSaved.roundToInt(), tireSaved.roundToInt(),
            km * brakeFactor / m.padLifeKm * 100, km * tireFactor / m.tireLifeKm * 100,
            (padSaved.roundToInt() + tireSaved.roundToInt()) / m.wonPerPoint)
    }
}

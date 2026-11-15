class TripTracker(
    private val hardDecelMps2: Double = 3.0,   // 약 0.3g
    private val windowMs: Long = 1_000,
    private val minSpanMs: Long = 400
) {
    private var startOdoKm: Double? = null
    private var lastOdoKm: Double? = null
    private var prev: VehicleSample? = null
    private var brakeEvents = 0
    private var hardBrakeEvents = 0
    private var speedSeen = false
    private var hardInThisPress = false
    private val speedHistory = ArrayDeque<Pair<Long, Double>>()

    fun onSample(s: VehicleSample): BrakeEvent {
        if (s.odometerKm != null) {                       // 주행거리: 시작·종료 차이
            if (startOdoKm == null) startOdoKm = s.odometerKm
            lastOdoKm = s.odometerKm
        }
        var event = BrakeEvent.NONE
        val wasOn = prev?.brakePedalOn ?: false

        if (!wasOn && s.brakePedalOn) {                   // 미작동 → 작동 = 제동 이벤트
            brakeEvents++; hardInThisPress = false; event = BrakeEvent.BRAKE
        }

        val speed = s.speedKmh
        if (speed != null) {                              // 속도 신호가 있을 때만 감속도 계산
            speedSeen = true
            speedHistory.addLast(s.timestampMs to speed)
            while (speedHistory.isNotEmpty() && s.timestampMs - speedHistory.first().first > windowMs)
                speedHistory.removeFirst()
            val ref = speedHistory.firstOrNull()
            if (s.brakePedalOn && !hardInThisPress && ref != null) {
                val span = s.timestampMs - ref.first
                if (span >= minSpanMs) {
                    val decel = (ref.second - speed) / 3.6 / (span / 1000.0)
                    if (decel >= hardDecelMps2) {         // 페달 ON + 감속도 초과 = 급제동
                        hardBrakeEvents++; hardInThisPress = true; event = BrakeEvent.HARD_BRAKE
                    }
                }
            }
        } else speedHistory.clear()

        if (!s.brakePedalOn) hardInThisPress = false
        prev = s
        return event
    }

    fun summary() = TripSummary(
        distanceKm = if (startOdoKm != null && lastOdoKm != null)
            (lastOdoKm!! - startOdoKm!!).coerceAtLeast(0.0) else null,
        brakeEvents = brakeEvents,
        hardBrakeEvents = if (speedSeen) hardBrakeEvents else null,   // 속도 없으면 페달 횟수만
        durationMs = ((prev?.timestampMs ?: 0L)).coerceAtLeast(0L)
    )
}

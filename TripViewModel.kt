source.samples().first { s ->
    if (s.gear in listOf(Gear.DRIVE, Gear.REVERSE, Gear.NEUTRAL)) driving = true
    val event = tracker.onSample(s)
    val est = estimator.estimate(tracker.summary())

    when (event) {
        BrakeEvent.HARD_BRAKE -> {
            if (bannerShown) hardAfterBanner++
            if (hardPolicy.onEvent(s.timestampMs)) showBanner()  // 아이콘 주황 + 배너 4초 후 자동 사라짐
        }
        BrakeEvent.BRAKE ->
            if (!tracker.speedSignalAvailable && pedalPolicy.onEvent(s.timestampMs)) showBanner()
        else -> Unit
    }
    _state.update { it.copy(liveSavedWon = est?.savedWon ?: 0, distanceKm = tracker.summary().distanceKm) }

    if (driving && s.gear == Gear.PARK) { finishTrip(); true } else false   // P단 → 요약 카드
}

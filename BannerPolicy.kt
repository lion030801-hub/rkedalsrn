class BannerPolicy(val repeatCount: Int = 2, val windowMs: Long = 60_000, val cooldownMs: Long = 60_000) {
    private val times = ArrayDeque<Long>(); private var lastShown: Long? = null
    fun onEvent(now: Long): Boolean {
        times.addLast(now)
        while (times.isNotEmpty() && now - times.first() > windowMs) times.removeFirst()
        if (times.size < repeatCount) return false
        if (lastShown != null && now - lastShown!! < cooldownMs) return false   // 반복 안내 방지
        lastShown = now; times.clear(); return true
    }
}

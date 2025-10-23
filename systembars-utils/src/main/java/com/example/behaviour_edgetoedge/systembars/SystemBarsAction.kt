import androidx.annotation.Keep

/**
 * Actions that control the visibility/behavior of system UI surfaces (status bar, navigation bar, IME).
 *
 * Use with [SystemBarsController.controlSystemBars] to show/hide bars or toggle the keyboard.
 *
 * Typical usage:
 * ```
 * // Show both bars with transient behavior disabled (default):
 * controller.controlSystemBars(SystemBarsAction.SHOW_SYSTEM_BARS)
 *
 * // Fully immersive (hide status + nav), swipe to reveal:
 * controller.controlSystemBars(SystemBarsAction.HIDE_SYSTEM_BARS)
 * ```
 */
@Keep
enum class SystemBarsAction {

    /** Show both **status** and **navigation** bars (no IME change). */
    SHOW_SYSTEM_BARS,

    /** Hide **status** bar, show **navigation** bar (no IME change). */
    HIDE_STATUS_SHOW_NAV,

    /** Hide **navigation** bar, show **status** bar (no IME change). */
    HIDE_NAV_SHOW_STATUS,

    /** Hide both **status** and **navigation** bars (immersive-style; IME unchanged). */
    HIDE_SYSTEM_BARS,

    /** Hide the on-screen keyboard (IME) only; system bars unchanged. */
    HIDE_KEYBOARD,

    /** Show the on-screen keyboard (IME) only; system bars unchanged. */
    SHOW_KEYBOARD,

    /** Hide **status bar**, **navigation bar**, and **IME** (everything). */
    HIDE_ALL,

    /** Show **status bar**, **navigation bar**, and **IME** (everything). */
    SHOW_ALL
}

package com.example.behaviour_edgetoedge.systembars

import SystemBarsAction
import android.app.Activity
import android.view.View

/**
 * Creates a new [SystemBarsController] bound to this [Activity].
 *
 * Use this when you need fine-grained control over system bars (status/navigation)
 * beyond the convenience [controlSystemBars] extension.
 *
 * @receiver the host [Activity].
 * @return a new [SystemBarsController] instance for this Activity.
 *
 * @see controlSystemBars
 * @see SystemBarsController
 */
fun Activity.getSystemBarsController(): SystemBarsController {
    return SystemBarsController(this)
}

/**
 * High-level helper to show/hide and style system bars on this [Activity].
 *
 * This wraps [SystemBarsController.controlSystemBars] and lets you:
 * - Show/hide status & navigation bars via [type]
 * - Toggle dark icons/text for status & nav bars
 * - Apply optional padding equal to system insets to a target [view] (or the decor view)
 *
 * Typical usage:
 * ```
 * // Make content edge-to-edge with light status icons and keep bottom inset padding:
 * activity.controlSystemBars(
 *     type = SystemBarsAction.SHOW_SYSTEM_BARS,
 *     darkStatusBars = true,
 *     darkNavigationBars = true,
 *     topInsetPadding = true,
 *     bottomInsetPadding = true
 * )
 * ```
 *
 * @receiver the host [Activity].
 * @param type The system bar action to perform (e.g., show/hide immersive). Defaults to
 * [SystemBarsAction.SHOW_SYSTEM_BARS].
 * @param darkStatusBars Whether status bar icons/text should be dark (true), light (false),
 * or unchanged (null). Defaults to `null` (no change).
 * @param darkNavigationBars Whether navigation bar icons should be dark (true), light (false),
 * or unchanged (null). Defaults to `null` (no change).
 * @param topInsetPadding If `true`, applies top padding equal to the status bar height to the
 * target view (or decor view when [view] is null). Default: `true`.
 * @param bottomInsetPadding If `true`, applies bottom padding equal to the navigation bar height
 * to the target view (or decor view when [view] is null). Default: `true`.
 * @param view Optional target view to receive inset padding. When `null`, the window's decor view
 * is used. Default: `null`.
 * @return The [SystemBarsAction] that was applied (useful for logging/telemetry).
 *
 * @see getSystemBarsController
 * @see SystemBarsController.controlSystemBars
 * @see SystemBarsAction
 */
fun Activity.controlSystemBars(
    type: SystemBarsAction = SystemBarsAction.SHOW_SYSTEM_BARS,
    darkStatusBars: Boolean? = null,
    darkNavigationBars: Boolean? = null,
    topInsetPadding: Boolean = true,
    bottomInsetPadding: Boolean = true,
    view: View? = null
): SystemBarsAction {
    return getSystemBarsController().controlSystemBars(
        type, darkStatusBars, darkNavigationBars, topInsetPadding, bottomInsetPadding, view
    )
}

/**
 * Returns the current status bar height in **pixels** for this [Activity].
 *
 * This method delegates to [SystemBarsController.getPapaStatusBarHeightCustom] and is safe
 * to call from UI code when computing fallback paddings for edge-to-edge layouts.
 *
 * @receiver the host [Activity].
 * @return Status bar height in pixels (≥ 0).
 *
 * @see getNavigationBarHeight
 * @see SystemBarsController.getPapaStatusBarHeightCustom
 */
fun Activity.getStatusBarHeight(): Int {
    return getSystemBarsController().getPapaStatusBarHeightCustom()
}

/**
 * Returns the current navigation bar height in **pixels** for this [Activity].
 *
 * This method delegates to [SystemBarsController.getPapaNavigationBarHeightCustom] and is safe
 * to call from UI code when computing fallback paddings for edge-to-edge layouts.
 *
 * @receiver the host [Activity].
 * @return Navigation bar height in pixels (≥ 0). Returns 0 on gesture-only devices or in
 * certain immersive modes.
 *
 * @see getStatusBarHeight
 * @see SystemBarsController.getPapaNavigationBarHeightCustom
 */
fun Activity.getNavigationBarHeight(): Int {
    return getSystemBarsController().getPapaNavigationBarHeightCustom()
}

/**
 * Checks whether the **system theme** (not your app theme) is currently set to dark mode.
 *
 * Useful for aligning status/navigation icon colors with the user's system preference when
 * your UI mirrors the system theme.
 *
 * @receiver the host [Activity].
 * @return `true` if the system is in dark theme, otherwise `false`.
 *
 * @see controlSystemBars
 * @see SystemBarsController.isSystemDarkTheme
 */
fun Activity.isSystemDarkTheme(): Boolean {
    return getSystemBarsController().isSystemDarkTheme()
}


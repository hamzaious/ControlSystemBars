package com.example.behaviour_edgetoedge.systembars

import SystemBarsAction
import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Helper for controlling Android system bars (status & navigation) and computing
 * safe fallback paddings when edge-to-edge content is used.
 *
 * Typical flow:
 * - Call [controlSystemBars] to show/hide and style bars.
 * - Use [getPapaStatusBarHeightCustom] / [getPapaNavigationBarHeightCustom] as
 *   fallbacks when real-time insets are unavailable (e.g., during first layout).
 *
 * @property activity The host [Activity] whose window/system bars are being controlled.
 */
class SystemBarsController(private val activity: Activity) {

    /**
     * Shows/hides and styles the system bars for the current window, and optionally applies
     * top/bottom padding equal to the current system bar insets (with robust fallbacks).
     *
     * Behavior details:
     * - Sets `isAppearanceLightStatusBars` / `isAppearanceLightNavigationBars` using the provided
     *   booleans; if `null`, it derives from [isSystemDarkTheme] (dark theme → light icons = false).
     * - Uses transient bars by swipe for hide actions, default behavior for show actions.
     * - Applies padding to [view] (or the window content view when null) based on current insets,
     *   falling back to heuristics when insets are 0 (e.g., pre-layout or OEM quirks).
     *
     * @param type The system bar action to perform (e.g., show/hide combinations).
     * @param darkStatusBars `true` to request dark status-bar icons, `false` for light icons,
     *                       or `null` to derive from system dark theme.
     * @param darkNavigationBars `true` to request dark nav-bar icons, `false` for light icons,
     *                           or `null` to derive from system dark theme.
     * @param topInsetPadding If `true`, apply top padding equal to status-bar inset/fallback.
     * @param bottomInsetPadding If `true`, apply bottom padding equal to nav-bar inset/fallback.
     * @param view Optional target view to receive padding; defaults to the window content view.
     * @return The [SystemBarsAction] actually applied (useful for logging/telemetry).
     */
    fun controlSystemBars(
        type: SystemBarsAction = SystemBarsAction.SHOW_SYSTEM_BARS,
        darkStatusBars: Boolean? = null,
        darkNavigationBars: Boolean? = null,
        topInsetPadding: Boolean = true,
        bottomInsetPadding: Boolean = true,
        view: View? = null
    ): SystemBarsAction {
        setPadding(topInsetPadding, bottomInsetPadding, view)

        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView).apply {
            // If caller didn't specify, derive icon lightness from system dark theme:
            // In dark theme → want LIGHT icons → isAppearanceLight... = false
            val systemDark = isSystemDarkTheme()
            isAppearanceLightStatusBars = darkStatusBars ?: !systemDark
            isAppearanceLightNavigationBars = darkNavigationBars ?: !systemDark

            systemBarsBehavior = when (type) {
                SystemBarsAction.SHOW_SYSTEM_BARS,
                SystemBarsAction.SHOW_ALL -> WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                else -> WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        when (type) {
            SystemBarsAction.SHOW_SYSTEM_BARS -> {
                controller.show(WindowInsetsCompat.Type.systemBars())
            }
            SystemBarsAction.HIDE_STATUS_SHOW_NAV -> {
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.show(WindowInsetsCompat.Type.navigationBars())
            }
            SystemBarsAction.HIDE_NAV_SHOW_STATUS -> {
                controller.hide(WindowInsetsCompat.Type.navigationBars())
                controller.show(WindowInsetsCompat.Type.statusBars())
            }
            SystemBarsAction.HIDE_SYSTEM_BARS -> {
                controller.hide(WindowInsetsCompat.Type.systemBars())
            }
            SystemBarsAction.HIDE_KEYBOARD -> {
                controller.hide(WindowInsetsCompat.Type.ime())
            }
            SystemBarsAction.SHOW_KEYBOARD -> {
                controller.show(WindowInsetsCompat.Type.ime())
            }
            // Hide everything: status + nav + IME
            SystemBarsAction.HIDE_ALL -> {
                controller.hide(
                    WindowInsetsCompat.Type.statusBars() or
                            WindowInsetsCompat.Type.navigationBars() or
                            WindowInsetsCompat.Type.ime()
                )
            }
            // Show everything
            SystemBarsAction.SHOW_ALL -> {
                controller.show(
                    WindowInsetsCompat.Type.statusBars() or
                            WindowInsetsCompat.Type.navigationBars() or
                            WindowInsetsCompat.Type.ime()
                )
            }
        }

        return type
    }

    /**
     * Installs an insets listener on [view] (or the content view) and applies padding equal to
     * current system bar insets. When the platform returns 0 (e.g., first layout / immersive /
     * gesture-only nav), it falls back to heuristics via
     * [getPapaStatusBarHeightCustom] / [getPapaNavigationBarHeightCustom].
     *
     * @param applyTopInset If `true`, apply top padding for the status bar area.
     * @param applyBottomInset If `true`, apply bottom padding for the navigation bar area.
     * @param view The target view to pad; defaults to `android.R.id.content` when `null`.
     */
    private fun setPadding(applyTopInset: Boolean, applyBottomInset: Boolean, view: View? = null) {
        ViewCompat.setOnApplyWindowInsetsListener(view ?: activity.findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val top = if (applyTopInset) {
                if (systemBars.top > 0) systemBars.top else getPapaStatusBarHeightCustom()
            } else 0

            val bottom = if (applyBottomInset) {
                if (systemBars.bottom > 0) systemBars.bottom else getPapaNavigationBarHeightCustom()
            } else 0

            v.setPadding(systemBars.left, top, systemBars.right, bottom)
            insets
        }
    }

    /**
     * Returns a robust estimate of the **status bar height in pixels**.
     *
     * Priority:
     * 1. On Android P+ with a display cutout, uses `safeInsetTop` when available.
     * 2. Otherwise, uses a density-based heuristic by screen height buckets.
     *
     * Notes:
     * - Useful as a *fallback* when real-time insets are not yet available.
     * - Logging is included for diagnostic purposes.
     *
     * @return Status bar height in pixels (≥ 0).
     */
    fun getPapaStatusBarHeightCustom(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val insets = activity.window.decorView.rootWindowInsets
            val cutout = insets?.displayCutout
            Log.d("DpValueTag", "getPapaStatusBarHeightCustom P+ path")
            if (cutout != null && cutout.safeInsetTop > 0) return cutout.safeInsetTop
        }

        // Fallback for non-cutout / pre-P devices
        val density = activity.resources.displayMetrics.density
        val heightDp = activity.resources.displayMetrics.heightPixels / density
        val dpValue = when {
            heightDp < 600 -> 24
            heightDp < 720 -> 25
            heightDp < 800 -> 26
            heightDp < 900 -> 27
            else -> 32
        }
        Log.d("DpValueTag", "getPapaStatusBarHeightCustom fallback")
        return (dpValue * density).toInt()
    }

    /**
     * Returns a robust estimate of the **navigation bar height in pixels**.
     *
     * Priority:
     * 1. On Android R+, checks visibility and reads `WindowInsets.Type.navigationBars()` or
     *    `WindowInsets.Type.systemGestures()` as appropriate.
     * 2. On O–Q, uses `stableInsetBottom` when available.
     * 3. Otherwise, uses a density-based heuristic by screen height buckets.
     *
     * Notes:
     * - Gesture-only navigation can legitimately report 0 height.
     * - Returns 0 for devices with no nav bar or when in immersive modes where the bar is hidden.
     *
     * @return Navigation bar height in pixels (≥ 0).
     */
    fun getPapaNavigationBarHeightCustom(): Int {
        val density = activity.resources.displayMetrics.density

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insets = activity.window.decorView.rootWindowInsets
            val navVisible = insets.isVisible(WindowInsetsCompat.Type.navigationBars())
            val height = if (navVisible) {
                val navBarInsets = insets?.getInsets(WindowInsets.Type.navigationBars())
                navBarInsets?.bottom ?: 0
            } else {
                val navBarGesture = insets?.getInsets(WindowInsets.Type.systemGestures())
                navBarGesture?.bottom ?: 0
            }
            Log.d("DpValueTag", "getPapaNavigationBarHeightCustom R+ path")
            if (height > 0) return height

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val insets = activity.window.decorView.rootWindowInsets
            @Suppress("DEPRECATION")
            val height = insets?.stableInsetBottom ?: 0
            Log.d("DpValueTag", "getPapaNavigationBarHeightCustom O–Q path")
            if (height > 0) return height
        }

        // Heuristic fallback
        val screenHeightDp = activity.resources.displayMetrics.heightPixels / density
        val dpValue = when {
            screenHeightDp < 600 -> 48    // phones (portrait)
            screenHeightDp < 800 -> 50
            screenHeightDp < 900 -> 52
            else -> 64                    // tablets / landscape modes
        }
        return (dpValue * density).toInt()
    }

    /**
     * Returns whether the **system** (not your app) is currently in dark theme.
     *
     * Implementation detail:
     * - `UI_MODE_NIGHT_YES` → dark theme → returns `true`
     * - `UI_MODE_NIGHT_NO`  → light theme → returns `false`
     *
     * @return `true` if system is in dark (night) mode, otherwise `false`.
     */
    fun isSystemDarkTheme(): Boolean {
        val uiMode = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return uiMode == Configuration.UI_MODE_NIGHT_YES
    }
}

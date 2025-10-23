[![](https://jitpack.io/v/hamzaious/ControlSystemBars.svg)](https://jitpack.io/#hamzaious/ControlSystemBars)

# ControlSystemBars

**ControlSystemBars** is a powerful and flexible custom , production-ready abstraction to control
Android system bars (status & navigation) and apply safe inset padding—while keeping your
implementation hidden.

## Gradle Integration

### Step A: Add Maven Repository

In your project-level **build.gradle** or **settings.gradle** file, add the JitPack repository:

```
repositories {
    google()
    mavenCentral()
    maven { url "https://jitpack.io" }
}
```  

### Step B: Add Dependencies

Next, include the library in your app-level **build.gradle** file. Replace x.x.x with the latest
version [![](https://jitpack.io/v/hamzaious/ControlSystemBars.svg)](https://jitpack.io/#hamzaious/ControlSystemBars)

```
implementation("com.github.hamzaious:ControlSystemBars:v1.0.0")
```

## Implementation

### Usage Example:

```
controlSystemBars(
            type = SystemBarsAction.SHOW_SYSTEM_BARS,
            topInsetPadding = true,
            bottomInsetPadding = true
        )
```

### Use input to modify:

```
    // Show both **status** and **navigation** bars (no IME change). 
    SystemBarsAction.SHOW_SYSTEM_BARS,
```
```
    // Hide **status** bar, show **navigation** bar (no IME change). 
    SystemBarsAction.HIDE_STATUS_SHOW_NAV,
```
```
    // Hide **navigation** bar, show **status** bar (no IME change). 
    SystemBarsAction.HIDE_NAV_SHOW_STATUS,
```
```
    // Hide both **status** and **navigation** bars (immersive-style; IME unchanged). 
    SystemBarsAction.HIDE_SYSTEM_BARS,
```
```
    // Hide the on-screen keyboard (IME) only; system bars unchanged. 
    SystemBarsAction.HIDE_KEYBOARD,
```
```
    // Show the on-screen keyboard (IME) only; system bars unchanged. 
    SSystemBarsAction.HOW_KEYBOARD,
```
```
    // Hide **status bar**, **navigation bar**, and **IME** (everything). 
    SystemBarsAction.HIDE_ALL,
```
```
    // Show **status bar**, **navigation bar**, and **IME** (everything). 
    SystemBarsAction.SHOW_ALL
```
### Update Status Bar Themes:
#### For System Default to pick theme and apply to system bars don't pass the values.
```
controlSystemBars(
            type = SystemBarsAction.SHOW_SYSTEM_BARS,
            topInsetPadding = true,
            bottomInsetPadding = true
        )
```
#### To set custom status bar or nav bar theme color set as following:
```
// This will apply dark theme to status and navbar. Also can set false to apply light theme status 
// and navbar
controlSystemBars(
            type = SystemBarsAction.SHOW_SYSTEM_BARS,
            darkStatusBars = true,
            darkNavigationBars = true,
            topInsetPadding = true,
            bottomInsetPadding = true
        )
```


![screenshot](https://github.com/hamzaious/ControlSystemBars/blob/master/screenshot/demo.mp4)

# Acknowledgements

This work would not have been possible without the invaluable contributions of **Nisar Bahoo** & *
*Hamza Arshad**. His expertise, dedication, and unwavering support have been instrumental in
bringing this project to fruition.

![screenshot]()

We are deeply grateful for Nisar Bahoo's involvement and his belief in the importance of this work.
His contributions have made a significant impact, and we are honored to have had the opportunity to
collaborate with him.

# LICENSE

Copyright 2023 Hypersoft Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

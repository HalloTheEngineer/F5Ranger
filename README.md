# F5 Ranger

F5 Ranger is a client-side Fabric mod for Minecraft that allows players to smoothly adjust their third-person camera distance using the scroll wheel. 

It is designed to be unobtrusive and highly configurable, making it ideal for builders, content creators, and players who want better control over their perspective.

## Features

### Smooth Distance Adjustment

> Hold a modifier key (Default: Left Alt) and scroll the mouse wheel to adjust your camera distance in real-time.

### Dynamic Scroll Speed

> The further the camera is from the player, the faster it scrolls, allowing for quick transitions at high distances and precision at close range.

### Configurable Smoothing

> Enable or disable camera lerping for a cinematic "sliding" effect or instant feedback.

### No Clip Mode

> An optional toggle that allows the camera to pass through solid blocks instead of colliding with them.

### Integrated UI

> A custom camera distance slider is added directly to the vanilla Options screen, centered perfectly for easy access.

### ModMenu & Cloth Config Integration

> Full support for ModMenu allows you to tweak every setting from the in-game menu.

## Installation

1. Ensure you are using Fabric Loader for Minecraft 1.21.1.

2. Download the latest version of F5 Ranger and [Cloth Config](https://modrinth.com/mod/cloth-config) from Modrinth.

3. Place the .jar files in your Minecraft mods folder.

4. (Recommended) Install [ModMenu](https://modrinth.com/mod/modmenu) to access the configuration screen.

## Configuration

The following options are available via the configuration menu:

|                          |                                                                                        |
|--------------------------|----------------------------------------------------------------------------------------|
| Camera Distance          | The current target distance of the third-person camera.                                |
| Minimum/Maximum Distance | Limits for how close or far the camera can go.                                         |
| Scroll Sensitivity       | Adjusts how much each scroll notch changes the distance.                               |
| Smoothing Speed          | Controls how quickly the camera catches up to the target distance.                     |
| No Clip                  | Toggles whether the camera should ignore block collisions.                             |
| Vanilla Second Person    | Toggle whether custom distances should apply to the front-facing (Second Person) view. |


## Usage

> While in a third-person perspective (F5):

- Adjust Distance: Hold Left Alt (default) and use the Scroll Wheel or use the slider in MC settings.

- Change Modifier: The keybind can be remapped in the standard Minecraft Controls menu under the F5 Ranger category.

## License

This project is licensed under the MIT License.
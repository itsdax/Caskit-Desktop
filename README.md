# Caskit Desktop Application
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) 

## Features

##### Supported Platforms:
- Windows Vista and up
- ~~Mac OSX~~ Capture controls issue.

##### Completed:
- Screen Capture
- Screen Record
- Audio integration
- Upload and clip URL
- Use direct URL
- Hotkey/Macro for capture
- FPS slider (up to 30fps)
- Include cursor in capture (Optional)

##### To Do:
- Grab Speaker output for videos `-blocked by java limitations on sound api. Will need to work with JNI`
- Dynamic recording controls
- Linux Support `-issue with consumuing key for hotkey/macros on linux`

## Tray Application

![](https://cdn.caskit.io/hJTAD59f.png)

## Building

Installing will create a shaded jar.

```
maven install
```
## Notes
Not all image resources are included in the project due to limitations of our image licenses. The project will 
use a placeholder for the images not included.


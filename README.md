# react-native-galaxywatch

## Getting started

`$ npm install react-native-galaxywatch --save`

### Mostly automatic installation

`$ react-native link react-native-galaxywatch`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-galaxywatch` and add `Galaxywatch.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libGalaxywatch.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.GalaxywatchPackage;` to the imports at the top of the file
  - Add `new GalaxywatchPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-galaxywatch'
  	project(':react-native-galaxywatch').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-galaxywatch/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-galaxywatch')
  	```


## Usage
```javascript
import Galaxywatch from 'react-native-galaxywatch';

// TODO: What to do with the module?
Galaxywatch;
```

# react-native-smart-space-floor-map

A project to display floor image and desks over it with zooming feature for Smart Space App.

## Features
- Display Image with zooming
- Render Desks over the image
- Allow to click to the desk and set active style

## About

This project was started in the cause of solving issue Image Zooming and drawing in Android


## Installation

Install package from npm

```sh
npm install --save @nois/smart-space-floor-map
```
In App Gradle
```gradle
...

dependencies {
    ...

    compile project(':smart-space-floor-map')
    
    ...
}
```

In `android/settings.gradle`
```gradle
...

include ':smart-space-floor-map'
project(':smart-space-floor-map').projectDir = new File(rootProject.projectDir, '../node_modules/smart-space-floor-map/android/release')
```

Manually register module in `MainApplication.java`:

```java
import com.south32.oraclecms.floormap.FloorMapPackage;  // <--- Import Package

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
      @Override
      protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
      }

      @Override
      protected List<ReactPackage> getPackages() {

      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new FloorMapPackage() // <---- Add the Package
      );
    }
  };

  ....
}
```

## Usage

Check out the `example` folder for sample code

## Development

Uncomment the `application` that include the `activity` in `AndroidManifest.xml` and run the project to dev. Remember to comment it again to make a release.

## Release
- Run following command to update release folder: `$ npm run release`


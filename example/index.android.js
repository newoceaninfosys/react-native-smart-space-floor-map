/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Platform
} from 'react-native';

import RNFetchBlob from 'react-native-fetch-blob'
import FloorMap from 'smart-space-floor-map';

export default class example extends Component {
  constructor(props){
    super(props);
    console.log('123');

    this.state = {
      uri: ''
    }
  }

  componentDidMount(){
    RNFetchBlob
      .config({
        fileCache : true,
        // by adding this option, the temp files will have a file extension
        // appendExt : 'png'
      })
      .fetch('GET', 'http://s1.picswalls.com/wallpapers/2016/03/29/beautiful-nature-wallpaper_042325903_304.jpg', {
        //some headers ..
      })
      .then((res) => {
        // the temp file path with file extension `png`
        console.log('The file saved to ', res.path())
        // Beware that when using a file path as Image source on Android,
        // you must prepend "file://"" before the file path
        this.setState({
          uri: Platform.OS === 'android' ? 'file://' + res.path()  : '' + res.path()
        });
      })
  }

  renderFloorMap(){
    const { uri } = this.state;
   if(uri && uri.length > 0){
    return <FloorMap uri={uri} style={{flex: 1, height: 100, width: 400, backgroundColor: '#e5e5e5'}} />
   }
   return null;
  }

  render() {
    return (
      <View style={styles.container}>
        {this.renderFloorMap()}
        <Text style={styles.welcome}>
          Welcome to React Native! 123
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.android.js
        </Text>
        <Text style={styles.instructions}>
          Double tap R on your keyboard to reload,{'\n'}
          Shake or press menu button for dev menu
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('example', () => example);

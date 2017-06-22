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
  Platform,
  TouchableOpacity,
  DeviceEventEmitter
} from 'react-native';

import RNFetchBlob from 'react-native-fetch-blob'
import FloorMap from 'smart-space-floor-map';

export default class example extends Component {
  constructor(props){
    super(props);
    console.log('123');

    this.state = {
      uri: '',
      activeDesk: null
    }
  }

  componentWillMount(){
    console.log('componentWillMount')
    DeviceEventEmitter.addListener('FloorMapOnSelect', (e: Event)=>{
      console.log('FloorMapOnSelect', e);
    });
  }

  componentDidMount(){
    RNFetchBlob
      .config({
        fileCache : true,
        // by adding this option, the temp files will have a file extension
        // appendExt : 'png'
      })
      .fetch('GET', 'http://nois.newoceaninfosys.com:44411/media/73738b977efb4f1bbdc6372bfade4ed5-lg-1.jpg', {
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
      });

      setTimeout(()=>{
        this.setState({activeDesk: {x: 200, y: 200, color: '#00ff00'} });
      }, 5000);
  }

  renderFloorMap(){
    const { uri } = this.state;
   if(uri && uri.length > 0){
    return <FloorMap radius={15} activeDesk={this.state.activeDesk} desks={[
      {x: 100, y: 100, color: '#000000'}, {x: 200, y: 200, color: '#00ff00'}, {x: 300, y: 300, color: '#0000ff'}, {x: 400, y: 400}
      ]} uri={uri} style={{height: 500, width: 400, backgroundColor: '#e5e5e5'}} />
   }
   return null;
  }

  changeImage(){
    console.log('Load new image');
    RNFetchBlob
      .config({
        fileCache : true,
        // by adding this option, the temp files will have a file extension
        // appendExt : 'png'
      })
      .fetch('GET', "https://south32smartspace.oraclecms.com/media/1ffbc41a344c4949887736025f4c3f08-original-1.jpg", {
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
      });
  }

  render() {
    return (
      <View style={styles.container}>
        {this.renderFloorMap()}
        <TouchableOpacity 
          style={{height: 40, marginTop: 20, marginBottom: 20}}
          onPress={()=>{
              this.changeImage();
          }}>
        <Text>Change Picture</Text>
        </TouchableOpacity>
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

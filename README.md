# react-native-video-gstreamer

Android only

`npm install --save react-native-video-gstreamer`

Features:
- url srtreaming
- play / stop / fullscreen controls
- volumne control
- device orientation control 

Todo:
- [ ] determine which file formats and urls are possible to play
- [x] install guide
- [ ] add native jni part for recompiling jniLibs with different needs and options
- [ ] clean up example
- ...

#Install Guide

1. npm install --save react-native-video-gstreamer

2. In android/setting.gradle
    ```
    ...
    include ':ReactNativeMediaPlayerAndroid', ':app'
    project(':ReactNativeMediaPlayerAndroid').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-video-gstreamer/android')
    ```
    
3. In android/app/build.gradle, add ReactNativeMediaPlayerAndroid library project.
    ```
    dependencies {
        ....
        compile project(':ReactNativeMediaPlayerAndroid') 
    }
    ```
4. Register Module in MainAcrivity.java (src/main/java/.../MainActivity.Java)
    ```
    ....
    import com.nick.reactnativemediaplayer.MediaPlayerPackage;  //add new package component framework for media player
    import com.nick.devicecontroller.DeviceControllerPackage;  //add new package component framework for device control 
    .....
    
     .addPackage(new MainReactPackage()) //add new React Native Package for media player
     .addPackage(new MediaPlayerPackage()) 
     .addPackage(new DeviceControllerPackage(this)) 
    ```
5. extract jni libaries 
    ```
    'node_modules/react-native-video-gestreamer/jiniLibs' 
    ```
    and copy the folder (armeabi, armeabi-v7a, ...) into 
    ```
    node_modules/react-native-video-gestreamer/android/src/main/jniLibs/
    ```

#Example Usage

```Javascript
'use strict';

var React = require('react-native');
var MediaPlayer = require('react-native-video-gstreamer');
var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TextInput,
  TouchableOpacity,
  ToastAndroid
} = React;

var StreamingState = {
    DEINITIALIZED       : 0
    , INITIALIZING      : 1
    , INITIALIZED       : 2
    , PLAYING_REQUEST   : 3
    , PLAYING           : 4
    , PAUSED            : 5
    , DEINITIALIZING    : 6
    , ERROR             : 7
};

var StreamingEvent = {
    STREAM_STATE_CHANGED    : 0
    , VIDEO_SIZE_CHANGED    : 1
    , STREAM_ERROR          : 2
    , STREAM_MESSAGE        : 3
};

var ScreenOrientation = {
    SCREEN_ORIENTATION_UNSPECIFIED : -1
    , SCREEN_ORIENTATION_LANDSCAPE : 0
    , SCREEN_ORIENTATION_PORTRAIT : 1
    , SCREEN_ORIENTATION_USER : 2
    , SCREEN_ORIENTATION_SENSOR : 4
};
var mediaPlayer = require('react-native').NativeModules.MediaPlayerAndroidModule;
var deviceController = require('react-native').NativeModules.DeviceContollerModule;
var ReactNativeStreamingPlayer = React.createClass({
    mediaStatus : StreamingState.DEINITIALIZED,
    mediaURL : "udp://239.255.0.1:1234",
    mediaVolume: 10,
    
    /*
        setURL function:
        @param1: uri
        @param2: flag aspect ratio
    */
    
    /*
        setVolume function:
        @param: volume (range: 0~100) 0:mute
    */
    selectURL: function() {
        //ToastAndroid.show(this.state.textValue, ToastAndroid.LONG );
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.setURL(this.state.textValue, true);
        
    },
    
    play: function() {
        if (this.mediaStatus == StreamingState.DEINITIALIZED) {
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setURL(this.state.textValue, true);
        } else {
            mediaPlayer.play();
        }
        
    },
    
    pause: function() {
        //mediaPlayer.pause();
        this.mediaVolume += 10;
        if (this.mediaVolume > 100) {
            this.mediaVolume = 100;   
        }
        mediaPlayer.setVolume(this.mediaVolume);
    },
    
    stop: function() {
        this.mediaVolume = 30;
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.stop();
        deviceController.setOrientation(ScreenOrientation.SCREEN_ORIENTATION_UNSPECIFIED);
    },
    
    fullScreen: function() {
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setURL(this.state.textValue, false);
        deviceController.setOrientation(ScreenOrientation.SCREEN_ORIENTATION_LANDSCAPE);
    },
    
    getInitialState: function() {
        return {
            textValue: this.mediaURL
            , mediaStatus: this.mediaStatus
        }   
    },
    
    updateText: function(text) {
        this.mediaURL = text;
        this.setState({
            textValue: this.mediaURL
            , mediaStatus: this.mediaStatus
        });   
    },
    onStreamEvent: function(data) {
        this.mediaStatus = data.streamState;
        this.setState({
            textValue: this.mediaURL
            , mediaStatus: this.mediaStatus
        }); 
    },
    
    _renderURI: function () {
        if (this.state.mediaStatus == StreamingState.PLAYING) {
            return (<View></View>);
        } else {
            return (<View>
                        <Text style={styles.welcome}>
                            Welcome to React Native Player!
                        </Text>
                        <View style={styles.uribar}>
                            <Text style={styles.toolbarButton}>
                                URL:
                            </Text>

                            <TextInput 
                                ref={component => this._textURI = component} style={styles.textInput}
                                onChangeText={this.updateText} controlled={true}
                                value={this.state.textValue}>
                            </TextInput>

                            <TouchableOpacity onPress={this.selectURL}>
                                <Text style={styles.controlButton}>
                                    Set URL
                                </Text>
                            </TouchableOpacity>
                        </View>
                    </View>);
        }
    },
    _renderPlayer: function() {
        if (this.state.mediaStatus != StreamingState.PLAYING) {
            return (<View>
                    <MediaPlayer
                        ref={component => this._mediaPlayer = component}
                        style={{ height: 400, width: 350}}
                        onChange={this.onStreamEvent} 
                        />
                </View>);
        } else {
            return (<View>
                    <MediaPlayer
                        ref={component => this._mediaPlayer = component}
                        style={{ height: 500, width: 350}}
                        onChange={this.onStreamEvent} 
                        />
                </View>);            
        }
    },
    _renderScreen: function() {
       return (
            <View style={styles.container}>
                {this._renderURI()}  
                <View style={styles.toolbar}>
                    <TouchableOpacity onPress={this.play}>
                        <Text style={styles.controlButton}>Play</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={this.pause}>
                        <Text style={styles.controlButton}>Pause</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={this.stop}>
                        <Text style={styles.controlButton}>Stop</Text>
                    </TouchableOpacity>
                        
                    <TouchableOpacity onPress={this.fullScreen}>
                        <Text style={styles.controlButton}>Full</Text>
                    </TouchableOpacity>
                </View>
                {this._renderPlayer()}  
                  
            </View>
        ); 
    }, 
    render: function() {
        return this._renderScreen();
    }
});

var styles = StyleSheet.create({
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
    uribar:{
        alignItems: 'center',
        paddingTop:10,
        paddingBottom:10,
        flexDirection:'row'    //Step 1
    },
    toolbar:{
        alignItems: 'center',
        paddingTop:10,
        paddingBottom:10,
        flexDirection:'row'    //Step 1
    },
        controlButton:{
            color:'#000',
            backgroundColor:'#aeaeae',
            justifyContent: 'center', 
            alignItems: 'center',
            textAlign: 'center',
            fontWeight:'bold',
            margin:10,
            fontSize: 20,
            paddingLeft: 20
        },
    textInput:{
        marginLeft: 10,
        width: 200,
        borderWidth: 1,
        borderColor: "#888888",
        height: 40
    }
});

AppRegistry.registerComponent('ReactNativeStreamingPlayer', () => ReactNativeStreamingPlayer);

```

#credits
All credits to Nicoloas Hagenov (nicoloas.hagenov327@gmail.com) for the good work done. 


#disclaimer

<b>This project depend upon other libaries such as [gstreamer/plugins](http://gstreamer.freedesktop.org/), which may have different license. Keep this in mind.</b>


MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.



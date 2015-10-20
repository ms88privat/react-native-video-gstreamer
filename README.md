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
(not tested yet!)

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
5. extract jni libaries 'node_modules/react-native-video-gestreamer/jiniLibs' & copy them (armeabi, armeabi-v7a, ...) into into src/main/jniLibs/

#Example Usage

```Javascript
'use strict';

var React = require('react-native');
var MediaPlayer = require('react-native-mediaplayer');
var {bp, vw, vh} = require('react-native-relative-units')(375);
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
    mediaURL : "udp://172.20.1.31:1234",
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
                        style={{ height: vh * 40, width:  bp * 350}}
                        onChange={this.onStreamEvent} 
                        />
                </View>);
        } else {
            return (<View>
                    <MediaPlayer
                        ref={component => this._mediaPlayer = component}
                        style={{ height: vh * 80, width:  bp * 350}}
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
        width: bp * 200,
        borderWidth: 1,
        borderColor: "#888888",
        height: 40
    }
});

AppRegistry.registerComponent('ReactNativeStreamingPlayer', () => ReactNativeStreamingPlayer);

```

All credits to Nicoloas Hagenov for the good work done.

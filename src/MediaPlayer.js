'use strict';

var React = require('react-native');
var { requireNativeComponent, PropTypes, View } = React;

var NativeMediaPlayer = requireNativeComponent('MediaPlayerAndroidComponent', MediaPlayer);

class MediaPlayer extends React.Component {
  constructor() {
    super();
    this._onChange = this._onChange.bind(this);
  }

  _onChange(event) {
    if (this.props.onChange) {
      this.props.onChange(event.nativeEvent);
    }
  }

  render() {
    return (
      <NativeMediaPlayer
        {...this.props} onChange={this._onChange}/>
    );
  }
}

MediaPlayer.propTypes = {
  ...View.propTypes,
  onChange: PropTypes.func
};

module.exports = MediaPlayer;
/**
 * @providesModule FloorMap
 */

'use strict';

import { NativeModules } from 'react-native';
// NativeModules.FloorMap;
import { PropTypes } from 'react';
import { requireNativeComponent, View } from 'react-native';

var iface = {
	name: 'FloorMap',
	propTypes: {
		uri: PropTypes.string,
		maxZoom: PropTypes.number,
		radius: PropTypes.number,
		strokeWidth: PropTypes.number,
		desks: PropTypes.array,
		activeDesk: PropTypes.object,
		activeColor: PropTypes.string,
		disabledTap: PropTypes.bool,
		...View.propTypes // include the default view properties
	}
};


module.exports = requireNativeComponent('FloorMap'/*SimpleViewManager*/, iface);

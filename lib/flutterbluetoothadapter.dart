import 'dart:async';

import 'package:flutter/services.dart';

class Flutterbluetoothadapter {
  MethodChannel _channel = const MethodChannel('flutterbluetoothadapter');

  Future<String> platformVersion() async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<List<String>> getDevices() async {
    var devices = await _channel.invokeMethod('getBtDevices');
    print("HERE - ${devices}");
    return devices;
  }

  Future<String> getDevice(int index) async {
    var device = await _channel.invokeMethod('getBtDevice');
    print("HERE - ${device}");
    return device;
  }

  Future<bool> checkBluetooth() async {
    bool result = await _channel.invokeMethod('checkBt');
    print("HERE ${result}");
    return result;
  }
}

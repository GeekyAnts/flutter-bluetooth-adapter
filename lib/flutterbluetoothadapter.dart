import 'dart:async';

import 'package:flutter/services.dart';

class Flutterbluetoothadapter {
  MethodChannel _channel = const MethodChannel('flutterbluetoothadapter');

  static const EventChannel _connectionStatusEventChannel =
      const EventChannel('connection_status');

  static const EventChannel _receivedMessagesEventChannel =
      const EventChannel('recieved_message_events');

  Future<bool> initBlutoothConnection(String uuid) async {
    return await _channel
        .invokeMethod('initBlutoothConnection', {"uuid": uuid});
  }

  Future<List<BtDevice>> getDevices() async {
    List devices = await _channel.invokeMethod('getBtDevices');
    List<BtDevice> responseList = [];
    devices.forEach((element) {
      BtDevice device = BtDevice.fromJson(element);
      responseList.add(device);
    });
    return responseList;
  }

  Future<BtDevice> getDevice(String address) async {
    Map device =
        await _channel.invokeMethod('getBtDevice', {"address": address});
    if (device == null) {
      return null;
    }
    BtDevice btDevice = BtDevice.fromJson(device);
    return btDevice;
  }

  Future<bool> checkBluetooth() async {
    bool result = await _channel.invokeMethod('checkBt');
    print("HERE ${result}");
    return result;
  }

  Future<bool> startServer() async {
    var result = await _channel.invokeMethod('startServer');
    print("HERE ${result}");
    return true;
  }

  Future<bool> startClient(int position, [bool isSecure = false]) async {
    print("HERE $position");
    var result = await _channel.invokeMethod('startClient', {
      "index": position ?? 0,
      "isSecure": isSecure ?? false,
    });
    print("HERE ${result}");
    return true;
  }

  Future<bool> sendMessage(String message, {bool sendByteByByte}) async {
    bool result = await _channel.invokeMethod('sendMessage', {
      "message": message ?? "",
      "sendByteByByte": sendByteByByte ?? false,
    });
    print("HERE ${result}");
    return result ?? false;
  }

  Stream<dynamic> connectionStatus() {
    return _connectionStatusEventChannel.receiveBroadcastStream();
  }

  Stream<dynamic> receiveMessages() {
    return _receivedMessagesEventChannel.receiveBroadcastStream();
  }
}

class BtDevice {
  String address;
  String name;

  BtDevice(this.address, this.name);

  BtDevice.fromJson(Map data) {
    address = data["address"];
    name = data["name"];
  }
}

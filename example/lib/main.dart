import 'dart:async';

import 'package:bluetoothadapter/bluetoothadapter.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Bluetoothadapter flutterbluetoothadapter = Bluetoothadapter();
  StreamSubscription _btConnectionStatusListener, _btReceivedMessageListener;
  String _connectionStatus = "NONE";
  List<BtDevice> devices = [];
  String _recievedMessage;
  TextEditingController _controller = TextEditingController();

  @override
  void initState() {
    super.initState();
    flutterbluetoothadapter
        .initBlutoothConnection("20585adb-d260-445e-934b-032a2c8b2e14");
    flutterbluetoothadapter
        .checkBluetooth()
        .then((value) => print(value.toString()));
    _startListening();
  }

  _startListening() {
    _btConnectionStatusListener =
        flutterbluetoothadapter.connectionStatus().listen((dynamic status) {
      setState(() {
        _connectionStatus = status.toString();
      });
    });
    _btReceivedMessageListener =
        flutterbluetoothadapter.receiveMessages().listen((dynamic newMessage) {
      setState(() {
        _recievedMessage = newMessage.toString();
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                Flexible(
                  fit: FlexFit.tight,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: RaisedButton(
                      onPressed: () async {
                        await flutterbluetoothadapter.startServer();
                      },
                      child: Text('LISTEN'),
                    ),
                  ),
                ),
                Flexible(
                  fit: FlexFit.tight,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: RaisedButton(
                      onPressed: () async {
                        devices = await flutterbluetoothadapter.getDevices();
                        setState(() {});
                      },
                      child: Text('LIST DEVICES'),
                    ),
                  ),
                )
              ],
            ),
            Text("STATUS - $_connectionStatus"),
            Padding(
              padding: const EdgeInsets.symmetric(
                horizontal: 8.0,
                vertical: 20,
              ),
              child: ListView(
                shrinkWrap: true,
                children: _createDevices(),
              ),
            ),
            Text(
              _recievedMessage ?? "NO MESSAGE",
              style: TextStyle(fontSize: 24),
            ),
            Row(
              children: <Widget>[
                Flexible(
                  flex: 4,
                  fit: FlexFit.tight,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: TextField(
                      controller: _controller,
                      decoration: InputDecoration(hintText: "Write message"),
                    ),
                  ),
                ),
                Flexible(
                  fit: FlexFit.tight,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: RaisedButton(
                      onPressed: () {
                        flutterbluetoothadapter.sendMessage(
                            _controller.text ?? "no msg",
                            sendByteByByte: false);
//                        flutterbluetoothadapter.sendMessage(".",
//                            sendByteByByte: true);
                        _controller.text = "";
                      },
                      child: Text('SEND'),
                    ),
                  ),
                )
              ],
            )
          ],
        ),
      ),
    );
  }

  _createDevices() {
    if (devices.isEmpty) {
      return [
        Center(
          child: Text("No Paired Devices listed..."),
        )
      ];
    }
    List<Widget> deviceList = [];
    devices.forEach((element) {
      deviceList.add(
        InkWell(
          key: UniqueKey(),
          onTap: () {
            flutterbluetoothadapter.startClient(devices.indexOf(element), true);
          },
          child: Container(
            padding: EdgeInsets.all(4),
            decoration: BoxDecoration(border: Border.all()),
            child: Text(
              element.name.toString(),
              style: TextStyle(fontSize: 18),
            ),
          ),
        ),
      );
    });
    return deviceList;
  }
}

# bluetoothadapter

A Flutter plugin to expose the features of Bluetooth Adapter for **Android only**. The primary purpose of this project was to communicate between flutter and Rasburry Pie using Bluetooth.

## Main Features
- Setting up a UUID from user end.
- Checking Bluetooth connection status and giving alerts if its off or not right.
- Getting list of paired devices.
- Get a particular paired device info.
- Start Bluetooth server.
- Start Bluetooth client.
- Start Bluetooth client.
- Send message to a connected device.
- Stream for listening to received messages.
- Stream for listening to connection status (CONNECTED, CONNECTING, CONNECTION FAILED, LISTENING).

### Getting Started

For a full example please check /example folder. Here are only the most important parts of the code to illustrate how to use the library.

    //Initiatinng the bluetooth adapter
    Bluetoothadapter flutterbluetoothadapter = Bluetoothadapter();

	//Listening to the connection status listener
	String _connectionStatus = "NONE";
    StreamSubscription _btConnectionStatusListener =flutterbluetoothadapter.connectionStatus().listen((dynamic status) {
      setState(() {
        _connectionStatus = status.toString();
      });
    });

	//Listening to the recieved messages
	String _recievedMessage;
    StreamSubscription _btReceivedMessageListener = flutterbluetoothadapter.receiveMessages().listen((dynamic newMessage) {
      setState(() {
        _recievedMessage = newMessage.toString();
      });
    });

#### Getting paired devices

    List<BtDevice> devices = await flutterbluetoothadapter.getDevices();

#### Getting paired device

    await flutterbluetoothadapter.getDevice(deviceAddress);

#### Starting BT server

    await flutterbluetoothadapter.startServer();

#### Starting BT client

    flutterbluetoothadapter.startClient(devices.indexOf(element), true);

#### Sending message

    flutterbluetoothadapter.sendMessage(messageString);

#### Check BT connection
	await flutterbluetoothadapter.checkBluetooth(); //returns bool

#### Set UUID
	flutterbluetoothadapter.initBlutoothConnection(uuid);


package com.aditya.flutterbluetoothadapter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import io.flutter.app.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterbluetoothadapterPlugin
 */
public class FlutterbluetoothadapterPlugin implements FlutterPlugin, MethodCallHandler {
    private MethodChannel channel;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice[] btDevices;
    Registrar activityRegistrar;
    Activity activity;
    SendRecieve sendRecieve;
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;
    private static final String APP_NAME = "BtChat";
    private static final UUID MY_UUID = UUID.fromString("20585adb-d260-445e-934b-032a2c8b2e14");

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case STATE_LISTENING:
//                    status.setText("Listening");
                    break;

                case STATE_CONNECTING:
//                    status.setText("Connecting");
                    break;

                case STATE_CONNECTED:
//                    status.setText("Connected");
                    break;

                case STATE_CONNECTION_FAILED:
//                    status.setText("Connection Failed");
                    break;

                case STATE_MESSAGE_RECEIVED:
//                    byte[] readBuffer = (byte[]) msg.obj;
//                    String tempMsg = new String(readBuffer, 0, msg.arg1);
//                    message.setText(tempMsg);
                    break;
            }
            return true;
        }
    });

    public FlutterbluetoothadapterPlugin() {
        this.activityRegistrar = null;
        this.activity = null;
    }

    public FlutterbluetoothadapterPlugin(Registrar registrar) {
        this.activityRegistrar = registrar;
        this.activity = (FlutterActivity) registrar.activity();
    }


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutterbluetoothadapter");
        channel.setMethodCallHandler(this);
    }

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutterbluetoothadapter");
        channel.setMethodCallHandler(new FlutterbluetoothadapterPlugin(registrar));

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "getBtDevices":
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                btDevices = new BluetoothDevice[devices.size()];
                ArrayList<String> strings = new ArrayList<String>();
                int counter = 0;
                for (BluetoothDevice device : devices) {
                    strings.add(device.getName());
                    btDevices[counter] = device;
                    counter++;
                }
                result.success(strings);
                break;

            case "getBtDevice":
                if ((btDevices != null) || (btDevices.length > 0))
                    result.success(btDevices[0].getName());
                else
                    result.success("NO DEVICES FOUND!");
                break;

            case "checkBt":
                checkBluetoothOnOff(result);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    private String checkBluetoothOnOff(Result result) {
        if (bluetoothAdapter == null) {
            //Bluetooth is not supported
            result.success(false);
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                result.success(false);
            } else {
                result.success(true);
            }
        }
        return "Unknown status";
    }

    private class ServerClass extends Thread {
        private BluetoothServerSocket serverSocket;

        public ServerClass() {
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            BluetoothSocket socket = null;
            while (socket == null) {
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }
                if (socket != null) {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);
                    // write some code for send recieve message here
                    sendRecieve = new SendRecieve(socket);
                    sendRecieve.start();
                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1) {
            device = device1;
            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
                sendRecieve = new SendRecieve(socket);
                sendRecieve.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    private class SendRecieve extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendRecieve(BluetoothSocket socket) {
            bluetoothSocket = socket;
            InputStream tempInputStr = null;
            OutputStream tempOutputStr = null;

            try {
                tempInputStr = bluetoothSocket.getInputStream();
                tempOutputStr = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = tempInputStr;
            outputStream = tempOutputStr;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

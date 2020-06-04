package com.aditya.flutterbluetoothadapter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Set;

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
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;


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
}

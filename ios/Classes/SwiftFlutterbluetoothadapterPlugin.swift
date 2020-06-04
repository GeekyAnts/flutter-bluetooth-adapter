import Flutter
import UIKit

public class SwiftFlutterbluetoothadapterPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutterbluetoothadapter", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterbluetoothadapterPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}

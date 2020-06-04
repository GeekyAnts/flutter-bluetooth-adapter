import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutterbluetoothadapter/flutterbluetoothadapter.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutterbluetoothadapter');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Flutterbluetoothadapter.platformVersion, '42');
  });
}

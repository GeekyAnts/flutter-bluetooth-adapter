#import "FlutterbluetoothadapterPlugin.h"
#if __has_include(<flutterbluetoothadapter/flutterbluetoothadapter-Swift.h>)
#import <flutterbluetoothadapter/flutterbluetoothadapter-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutterbluetoothadapter-Swift.h"
#endif

@implementation FlutterbluetoothadapterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterbluetoothadapterPlugin registerWithRegistrar:registrar];
}
@end

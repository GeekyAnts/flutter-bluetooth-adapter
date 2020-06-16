#import "BluetoothadapterPlugin.h"
#if __has_include(<bluetoothadapter/bluetoothadapter-Swift.h>)
#import <bluetoothadapter/bluetoothadapter-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "bluetoothadapter-Swift.h"
#endif

@implementation BluetoothadapterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftBluetoothadapterPlugin registerWithRegistrar:registrar];
}
@end

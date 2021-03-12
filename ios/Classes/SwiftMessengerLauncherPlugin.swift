import Flutter
import UIKit

public class SwiftMessengerLauncherPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "messenger_launcher", binaryMessenger: registrar.messenger())
    let instance = SwiftMessengerLauncherPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if (call.method == "launchMessenger") {
        let params = call.arguments as! Dictionary<String,String>
        let messengerId: String = params["messengerId"]!
        let messengerUrl = "fb-messenger://user-thread/"+messengerId
        if UIApplication.shared.canOpenURL(URL(string: messengerUrl)!) {
            UIApplication.shared.openURL(URL(string: messengerUrl)!)
        }else {
            let url = "https://m.me/"+messengerId
            UIApplication.shared.openURL(URL(string: url)!)
        }

    }

  }
}

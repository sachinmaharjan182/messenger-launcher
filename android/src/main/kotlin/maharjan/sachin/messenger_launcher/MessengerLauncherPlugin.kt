package maharjan.sachin.messenger_launcher

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** MessengerLauncherPlugin */
class MessengerLauncherPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  val MESSENGER = "com.facebook.orca"
  val MESSENGER_LITE = "com.facebook.mlite"

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "messenger_launcher")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "launchMessenger") {
      val messengerId: String = call.argument("messengerId")!!
      if (hasMessenger()) {
        openMessenger(messengerId)
      } else {
        val intent = Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://m.me/$messengerId"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
      }
    }else {
      result.notImplemented()
    }
  }

  private fun hasMessenger(): Boolean {
    return  hasApp(MESSENGER_LITE) || hasApp(MESSENGER)
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  fun openMessenger(messengerId: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    if (hasApp(MESSENGER)) {
      intent.setPackage(MESSENGER)
      intent.data = Uri.parse("fb-messenger://user/$messengerId")
    }else {
      intent.data = Uri.parse("fb-messenger-lite://user/$messengerId")
      intent.setPackage(MESSENGER_LITE)
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
  }

  private fun hasApp(appPackage: String): Boolean {
    val pm: PackageManager = context.packageManager
    var appInstalled: Boolean

    try {
      pm.getPackageInfo(appPackage, PackageManager.GET_ACTIVITIES)
      appInstalled = true
    } catch (e: PackageManager.NameNotFoundException) {
      appInstalled = false
    }

    return appInstalled
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}

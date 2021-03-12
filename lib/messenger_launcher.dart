
import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class MessengerLauncher {
  static const MethodChannel _channel =
      const MethodChannel('messenger_launcher');

  static void launchMessenger(String messengerId) async {
    return await _channel.invokeMethod(
      'launchMessenger',
      {
        "messengerId": messengerId,
      },
    );
  }
}

import 'package:blue_flutter/list_activity.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class MainActivity extends StatefulWidget {
  @override
  _MainActivityState createState() => _MainActivityState();
}

class _MainActivityState extends State<MainActivity> {
  static const platform = const MethodChannel('flutter.native/helper');


  // Future<void> addToPrintNative(String text, bool center, bool bold) async {
  //   try {
  //     String result = await platform.invokeMethod('addToPrint', {
  //       "text": text,
  //       "center": center,
  //       "bold": bold
  //     });
  //
  //     print('result from al is $result');
  //   } on PlatformException catch (e) {
  //     print("Failed to Invoke: '${e.message}'.");
  //   }
  //
  // }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: Center(
          child: MaterialButton(
            color: Colors.blue,
            onPressed: (){
              Navigator.push(
                context,
                // ignore: always_specify_types
                MaterialPageRoute(
                  builder: (context) => ListActivity(),
                ),
              );
            },
            child: Text(
              'Print'
            ),
          ),
        ),
      ),
    );
  }
}

import 'package:blue_flutter/list_activity.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class MainActivity extends StatefulWidget {
  @override
  _MainActivityState createState() => _MainActivityState();
}

class _MainActivityState extends State<MainActivity> {
  static const platform = const MethodChannel('flutter.native/helper');


  Future<void> checkBlueToothDeviceConnectedNative() async {
    try {
      bool result = await platform.invokeMethod('checkBluetoothDeviceConnected');

      if(result == true){

        printNative();
      }else{
        Navigator.push(
          context,
          // ignore: always_specify_types
          MaterialPageRoute(
            builder: (context) => ListActivity(),
          ),
        );
      }

    } on PlatformException catch (e) {
      print("Failed to Invoke: '${e.message}'.");
    }

  }

  Future<void> printNative() async {
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
      content: Text(
        'Printing',
        style: TextStyle(color: Colors.white),
      ),
      backgroundColor: Colors.green,
    ));
    try {

      await platform.invokeMethod('print');

    } on PlatformException catch (e) {
      print("Failed to Invoke: '${e.message}'.");
    }

  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: Column(
          children: [
            MaterialButton(
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
                  'Pair Printer'
              ),
            ),
            MaterialButton(
              color: Colors.blue,
              onPressed: (){
                checkBlueToothDeviceConnectedNative();
              },
              child: Text(
                  'Print'
              ),
            ),
          ],
        ),
      ),
    );
  }
}

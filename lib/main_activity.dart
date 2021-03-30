import 'package:blue_flutter/app_provider.dart';
import 'package:blue_flutter/list_activity.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

class MainActivity extends StatefulWidget {
  @override
  _MainActivityState createState() => _MainActivityState();
}

class _MainActivityState extends State<MainActivity> {
  static const platform = const MethodChannel('flutter.native/helper');

  
  Future<void> printNative() async {
    if(Provider.of<AppProvider>(context, listen: false).printerName.isEmpty
        && Provider.of<AppProvider>(context, listen: false).printerAddress.isEmpty ){
      Navigator.push(
        context,
        // ignore: always_specify_types
        MaterialPageRoute(
          builder: (context) => ListActivity(),
        ),
      );
      return;
    }

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
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
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
                printNative();
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

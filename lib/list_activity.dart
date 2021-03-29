import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ListActivity extends StatefulWidget {
  @override
  _ListActivityState createState() => _ListActivityState();
}

class _ListActivityState extends State<ListActivity> {
  bool isSwitched = false;
  String deviceName = 'No Devices';
  static const platform = const MethodChannel('flutter.native/helper');

  @override
  void initState() {
    blueToothCheckEnabledNative();
    super.initState();
  }

  Future<void> blueToothEnableNative() async {
    try {
      bool result = await platform.invokeMethod('bluetoothEnable');

      print('enable native is $result');
      if(result == true){
        setState(() {
          isSwitched = true;
        });
      }else{
        print("Bluetooth not supported");
      }

    } on PlatformException catch (e) {
      print("Failed to Invoke: '${e.message}'.");
    }

  }

  Future<void> blueToothDisableNative() async {
    try {
      bool result = await platform.invokeMethod('blueToothDisable');

      if(result == true){
        setState(() {
          isSwitched = false;
        });
      }

    } on PlatformException catch (e) {
      print("Failed to Invoke: '${e.message}'.");
    }

  }

  Future<void> blueToothCheckEnabledNative() async {
    try {
     bool result = await platform.invokeMethod('checkIfEnabled');
     if(result == true){
       setState(() {
         isSwitched = true;
       });
     }else{
       isSwitched = false;
     }

      print('is switchd is $isSwitched');
    } on PlatformException catch (e) {
      print("Failed to Invoke: '${e.message}'.");
    }

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        margin: EdgeInsets.only(top: 50),
        child: Column(
          children: [
            Container(
              padding: EdgeInsets.all(5.0),
              width: MediaQuery.of(context).size.width,
              color: Colors.black26,
              child: Text(
                  'Bluetooth Status Enabled'
              ),
            ),

            Container(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  Switch(
                      value: isSwitched,
                      onChanged: toggleSwitch,
                    activeColor: Colors.green,
                    activeTrackColor: Colors.greenAccent
                  )
                ],
              ),
            ),

            Container(
              width: MediaQuery.of(context).size.width,
              padding: EdgeInsets.all(8.0),
              color: Colors.blue,
              child: Center(
                child: Text(
                  'SCAN',
                  style: TextStyle(
                    color: Colors.white
                  ),
                ),
              )
            ),

            Container(
              width: MediaQuery.of(context).size.width,
              color: Colors.black26,
              padding: EdgeInsets.all(6.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  RichText(
                    text: TextSpan(
                        text: 'Connected:',
                        style: TextStyle(
                          color: Colors.black
                        ),
                        children: [
                          TextSpan(
                            text: " $deviceName",
                            style: TextStyle(
                              color: Colors.blue
                            ),
                          ),

                        ]),
                  ),
                  SizedBox(height: 6,),
                  Text('Found(tap to connect):'),
                  SizedBox(height: 6,),
                  Text('Paired:'),
                ],
              ),
            ),

            Expanded(
                child: ListView(

                )
            )
          ],
        ),
      ),
    );
  }

  void toggleSwitch(bool value){
    if(isSwitched == false) {
      blueToothEnableNative();

    } else {
      blueToothDisableNative();
    }
  }
}

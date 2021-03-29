import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:bluetooth_thermal_printer/bluetooth_thermal_printer.dart';

class ListActivity extends StatefulWidget {
  @override
  _ListActivityState createState() => _ListActivityState();
}

class _ListActivityState extends State<ListActivity> {
  bool isSwitched = false;
  String deviceName = 'No Devices';
  static const platform = const MethodChannel('flutter.native/helper');
  List availableBluetoothDevices = [];

  @override
  void initState() {
    blueToothCheckEnabledNative();
    super.initState();
  }

  Future<void> getBluetooth() async {
    final List bluetooths = await BluetoothThermalPrinter.getBluetooths;
    print("Print bluetooth $bluetooths");
    setState(() {
      availableBluetoothDevices = bluetooths;
    });
  }

  Future<void> blueToothEnableNative() async {
    try {
      await platform.invokeMethod('bluetoothEnable');

      bool blueStatus = await platform.invokeMethod('blueToothStatus');


      if(blueStatus == true){
        setState(() {
          isSwitched = true;
        });
        getBluetooth();
      }else{
        isSwitched = false;
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
       getBluetooth();
     }else{
       isSwitched = false;
     }

      print('is switchd is $isSwitched');
    } on PlatformException catch (e) {
      print("Failed to Invoke: '${e.message}'.");
    }

  }

  Future<void> pairPrinterNative(String name, String address) async {
    try {
      String result = await platform.invokeMethod('pairPrinter', {
        "name" : name,
        "address": address
      });

      print('result from pair is $result');

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
                child: Column(
                  children: [
                    Container(
                      child: Center(
                        child: CircularProgressIndicator(
                          valueColor: AlwaysStoppedAnimation<Color>(Colors.blue),
                        ),
                      ),
                      width: 40,
                      height: 40,
                    ),
                    ListView.builder(
                      itemCount: availableBluetoothDevices.length > 0
                          ? availableBluetoothDevices.length
                          : 0,
                      itemBuilder: (context, index) {
                        return ListTile(
                          onTap: () {
                            String select = availableBluetoothDevices[index];
                            List list = select.split("#");
                            String name = list[0];
                            String mac = list[1];
                            pairPrinterNative(name, mac);
                          },
                          title: Text('${availableBluetoothDevices[index]}'),
                          subtitle: Text("Click to connect"),
                        );
                      },
                    )
                  ],
                ),
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

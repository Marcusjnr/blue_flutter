package com.blue_flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.IOException
import java.util.*

class MainActivity: FlutterActivity(), Runnable {
    private val CHANNEL = "flutter.native/helper"
    private val applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var mBluetoothSocket: BluetoothSocket? = null
    var mBluetoothDevice: BluetoothDevice? = null
    var mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val REQUEST_ENABLE_BT = 2
    private var bluetoothEnabled = false
    private var  mHandler: Handler? = null

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            // Note: this method is invoked on the main thread.
            call, result ->
            if(call.method == "bluetoothEnable") {
                if(mBluetoothAdapter == null){
                    result.success(false)
                }else{
                    if(!mBluetoothAdapter!!.isEnabled){
                        startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1)
                        if(bluetoothEnabled){
                            result.success(true)
                        }else{
                            result.success(false)
                        }
                    }
                }

            }else if(call.method == "checkIfEnabled"){
                if(mBluetoothAdapter == null){
                    result.success(false)
                }else{
                    if(!mBluetoothAdapter!!.isEnabled){
                        result.success(false)
                    }else{
                        result.success(true)
                    }
                }
            } else if(call.method == "blueToothDisable"){

                mBluetoothAdapter?.disable()
                result.success(true)

            }else if(call.method == "blueToothStatus"){
                if(changedBlueToothEnabled()){
                    result.success(true)
                }else{
                    result.success(false)
                }
            }else if(call.method == "pairPrinter"){

                val address = call.argument<String>("address")

                mBluetoothDevice = mBluetoothAdapter?.getRemoteDevice(address)

                val mBlutoothConnectThread = Thread(this)
                mBlutoothConnectThread.start()

                @SuppressLint("HandlerLeak")
                mHandler = object : Handler() {
                    override fun handleMessage(msg: Message) {
                        val status = msg.obj as String
                        if(status == "good"){
                            result.success(true)
                        }else{
                            result.success(false)
                        }

                    }
                }
                //result.success("name is ${mBluetoothDevice?.name} and address is ${mBluetoothDevice?.address}")
            }else{
                result.notImplemented()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    private fun changedBlueToothEnabled() : Boolean{
        return bluetoothEnabled
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

     when(requestCode){
         1 -> {
             if(requestCode == Activity.RESULT_OK){
                 bluetoothEnabled = true
             }else{
                 bluetoothEnabled = false
             }
         }
     }
    }


    private fun closeSocket(nOpenSocket: BluetoothSocket) {
        try {
            nOpenSocket.close()
        } catch (ex: IOException) {

        }
    }

    private fun bluetoothOnCheck() : Boolean{
        var blueToothOn = false
        if(mBluetoothAdapter == null){

        }else{
            blueToothOn = mBluetoothAdapter!!.isEnabled
        }
        return blueToothOn
    }

    override fun run() {
        var message: Message = Message()
        try {
            mBluetoothSocket = mBluetoothDevice
                    ?.createRfcommSocketToServiceRecord(applicationUUID)
            mBluetoothAdapter?.cancelDiscovery()
            mBluetoothSocket?.connect()
            var messageString = ""
            message = mHandler!!.obtainMessage()
            messageString = "good"
            message.obj = messageString
            mHandler!!.sendMessage(message)
        }catch (eConnectException: IOException){
            closeSocket(mBluetoothSocket!!)
            var messageString = ""
            message = mHandler!!.obtainMessage()
            messageString = "bad"
            message.obj = messageString
            mHandler!!.sendMessage(message)
        }
    }

}

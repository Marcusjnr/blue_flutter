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
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.IOException
import java.lang.reflect.Method
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and

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

    var c = Calendar.getInstance()
    var df = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US)
    val formattedDate = df.format(c.time)

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
            }else if(call.method == "checkBluetoothDeviceConnected"){
                val response = isConnected(mBluetoothDevice!!)
               if(response == true){
                   result.success(true)
               }else{
                   result.success(false)
               }
            }else if(call.method == "print"){
                p1()
            }else{
                result.notImplemented()
            }
        }
    }


    fun p1() {
        val t: Thread = object : Thread() {
            override fun run() {
                try {
                    val os = mBluetoothSocket!!.outputStream
                    var header = ""
                    var he = ""
                    var blank = ""
                    var header2 = ""
                    var BILL = ""
                    var vio = ""
                    var header3 = ""
                    var mvdtail = ""
                    var header4 = ""
                    var offname = ""
                    var time = ""
                    var copy = ""
                    val checktop_status = ""
                    blank = "\n\n"
                    he = "      EFULLTECH NIGERIA\n"
                    he = "$he********************************\n\n"
                    header = "FULL NAME:\n"
                    BILL = "Steve Barn" + "\n"
                    BILL = """
                        $BILL================================
                        
                        """.trimIndent()
                    header2 = "COMPANY'S NAME:\n"
                    vio = "Trade Depot" + "\n"
                    vio = """
                        $vio================================
                        
                        """.trimIndent()
                    header3 = "AGE:\n"
                    mvdtail = "100" + "\n"
                    mvdtail = """
                        $mvdtail================================
                        
                        """.trimIndent()
                    header4 = "AGENT DETAILS:\n"
                    offname = "Stuff" + "\n"
                    offname = """
                        $offname--------------------------------
                        
                        """.trimIndent()
                    time = formattedDate + "\n\n"
                    copy = "-Customer's Copy\n\n\n\n\n\n\n\n\n"
                    os.write(blank.toByteArray())
                    os.write(he.toByteArray())
                    os.write(header.toByteArray())
                    os.write(BILL.toByteArray())
                    os.write(header2.toByteArray())
                    os.write(vio.toByteArray())
                    os.write(header3.toByteArray())
                    os.write(mvdtail.toByteArray())
                    os.write(header4.toByteArray())
                    os.write(offname.toByteArray())
                    os.write(checktop_status.toByteArray())
                    os.write(time.toByteArray())
                    os.write(copy.toByteArray())


                    // Setting height
                    val gs = 29
                    os.write(intToByteArray(gs).toInt())
                    val h = 150
                    os.write(intToByteArray(h).toInt())
                    val n = 170
                    os.write(intToByteArray(n).toInt())

                    // Setting Width
                    val gs_width = 29
                    os.write(intToByteArray(gs_width).toInt())
                    val w = 119
                    os.write(intToByteArray(w).toInt())
                    val n_width = 2
                    os.write(intToByteArray(n_width).toInt())
                } catch (e: java.lang.Exception) {
                    Log.e("PrintActivity", "Exe ", e)
                }
            }
        }
        t.start()
    }

    fun intToByteArray(value: Int): Byte {
        val b = ByteBuffer.allocate(4).putInt(value).array()
        for (k in b.indices) {
            println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]))
        }
        return b[3]
    }

    fun isConnected(device: BluetoothDevice): Boolean {
        return try {
            val m: Method = device.javaClass.getMethod(
                "isConnected")
            m.invoke(device) as Boolean
        } catch (e: Exception) {
            throw IllegalStateException(e)
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

//public class UnicodeFormatter{
//    companion object{
//        fun byteToHex(b: Byte): String? {
//            // Returns hex String representation of byte b
//            val hexDigit = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//                    'a', 'b', 'c', 'd', 'e', 'f')
//            val array = charArrayOf(hexDigit[b shr 4 and 0x0f], hexDigit[b and 0x0f])
//            return String(array)
//        }
//
//        fun charToHex(c: Char): String? {
//            // Returns hex String representation of char c
//            val hi = (c.toInt() ushr 8).toByte()
//            val lo = (c.toInt() and 0xff).toByte()
//            return UnicodeFormatter.byteToHex(hi) + UnicodeFormatter.byteToHex(lo)
//        }
//    }
//
//
//}

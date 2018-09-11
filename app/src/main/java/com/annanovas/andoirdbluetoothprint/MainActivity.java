package com.annanovas.andoirdbluetoothprint;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormatSymbols;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements Runnable {
    protected static final String TAG = "MY_BLUETOOTH";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    String BILL = "" ;
    private static final String SPACE_COUNT_1 = " ";
    private static final String SPACE_COUNT_2 = "  ";
    private static final String SPACE_COUNT_3 = "   ";
    private static final String SPACE_COUNT_4 = "    ";
    private static final String SPACE_COUNT_5 = "     ";
    private static final String SPACE_COUNT_6 = "      ";
    private static final String SPACE_COUNT_7 = "       ";
    private static final String SPACE_COUNT_8 = "        ";
    private static final String SPACE_COUNT_9 = "         ";
    private static final String SPACE_COUNT_10 = "          ";
    private static final String SPACE_COUNT_11 = "           ";
    private static final String SPACE_COUNT_12 = "            ";
    private static final String SPACE_COUNT_13 = "             ";
    private static final String SPACE_COUNT_14 = "              ";
    private static final String SPACE_COUNT_15 = "               ";
    private static final String SPACE_COUNT_16 = "                ";
    private static final String SPACE_COUNT_17 = "                 ";
    private static final String SPACE_COUNT_18 = "                  ";
    private static final String SPACE_COUNT_19 = "                   ";
    private static final String SPACE_COUNT_20 = "                    ";
    private static final String SPACE_COUNT_21 = "                     ";
    private static final String SPACE_COUNT_22 = "                      ";
    private static final String SPACE_COUNT_23 = "                       ";
    private static final String SPACE_COUNT_24 = "                        ";
    private static final String SPACE_COUNT_25 = "                         ";
    private static final String SPACE_COUNT_26 = "                          ";
    private static final String SPACE_COUNT_27 = "                           ";
    private static final String SPACE_COUNT_28 = "                            ";
    private static final String SPACE_COUNT_29 = "                             ";
    private static final String SPACE_COUNT_30 = "                              ";
    private static final String SPACE_COUNT_31 = "                               ";
    private static final String SPACE_COUNT_32 = "                                ";
    private String[] comaSparatedarray;


    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.activity_main);
        //myPrint();
        mScan = (Button) findViewById(R.id.Scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(MainActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(MainActivity.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        mPrint = (Button) findViewById(R.id.mPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                Thread t = new Thread() {
                    public void run() {
                        try {
                            OutputStream os = mBluetoothSocket
                                    .getOutputStream();
                            BILL = "";

                            /*BILL = "                 XXXX MART\n"
                                    + "           XXX.AA.BB.CC.     \n " +
                                    "           NO 25 ABC ABCDE    \n" +
                                    "            XXXXX YYYYYY      \n" +
                                    "             MMM 590019      \n\n";*/

                            BILL = BILL + printingProcedureStoreInfo("AnnaNovas Store");
                            BILL = BILL + printingProcedureMakeAddress("South Banasree, Rampura, Dhaka, Dakhin Banasree Project Road, Dhaka 1219");
                            //BILL = BILL + printingProcedureStoreInfo("MohammadPur, Dhaka-1219");
                            BILL = BILL
                                    + "--------------------------------\n\n";
                            BILL = printingProcedure() ;
                            for(int i = 0 ; i < 6 ; i++){
                                if(i == 0){
                                    BILL = printingProcedureProductInfo("CocaCola 1.25ltr", "65.00", "1", "65.00");
                                }
                                else if(i == 1){
                                    BILL = printingProcedureProductInfo("Ruchi Chanachur - 250gm", "45.00", "2", "90.00");
                                }
                                else if(i == 2){
                                    BILL = printingProcedureProductInfo("Pepsi - 2 ltr", "100.00", "10", "1000.00");
                                }
                                else{
                                    BILL = printingProcedureProductInfo("CocaCola 1.25ltr", "65.00", "1", "65.00");
                                }

                            }

                            BILL = BILL+"\n\n\n\n" ;



                            /*BILL = BILL + String.format("%1$2s %2$8s %3$5s %4$12s", "Item", "MRP", "Qty", "Total");
                            BILL = BILL+"\n" ;
                            BILL = BILL
                                    + "--------------------------------\n\n";*/

                            /*BILL = BILL
                                    + "-----------------------------------------------";*/
                            //BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "5", "10", "50.00");

                            /*BILL = BILL+"\n" ;
                            BILL = BILL + "CocaCola 1.25 ltr" ;
                            BILL = BILL+"\n" ;
                            BILL = BILL + String.format("%1$2s %2$8s %3$5s %4$12s", "", "65.00", "1", "650000");
                            BILL = BILL+"\n\n" ;*/
                            Log.d("BILL", BILL);



                            /*BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
                            BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
                            BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

                            BILL = BILL
                                    + "\n-----------------------------------------------";
                            BILL = BILL + "\n\n ";

                            BILL = BILL + "                   Total Qty:" + "      " + "85" + "\n";
                            BILL = BILL + "                   Total Value:" + "     " + "700.00" + "\n";

                            BILL = BILL
                                    + "-----------------------------------------------\n";
                            BILL = BILL + "\n\n ";*/

                            //os.write(BILL.getBytes());
                            os.write(new Formatter().bold().get());
                            os.write(BILL.getBytes(), 0, BILL.getBytes().length);
                            //This is printer specific code you can comment ==== > Start

                            // Setting height
                            int gs = 29;
                            os.write(intToByteArray(gs));
                            int h = 104;
                            os.write(intToByteArray(h));
                            int n = 162;
                            os.write(intToByteArray(n));

                            // Setting Width
                            int gs_width = 29;
                            os.write(intToByteArray(gs_width));
                            int w = 119;
                            os.write(intToByteArray(w));
                            int n_width = 2;
                            os.write(intToByteArray(n_width));


                        } catch (Exception e) {
                            Log.e("MainActivity", "Exe ", e);
                        }
                    }
                };
                t.start();
            }
        });

        mDisc = (Button) findViewById(R.id.dis);
        mDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (mBluetoothAdapter != null)
                    mBluetoothAdapter.disable();
            }
        });

    }// onCreate

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);

                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(MainActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(MainActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    private void connectDevice(){
        try {
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mBluetoothSocket=mBluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            mBluetoothSocket.connect();
            //mBluetoothConnectProgressDialog.dismiss();

        } catch (IOException e) {
            //mBluetoothConnectProgressDialog.dismiss();
            Log.d("CONNECT_EXCEPTION", String.valueOf(e));
            e.printStackTrace();
        }
    }

    public void run() {
        /*try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);

        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            if(mBluetoothConnectProgressDialog.isShowing()){
                mBluetoothConnectProgressDialog.dismiss();
            }
            return;
        }*/
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
        }
        catch (Exception e) {
            Log.e("create_socket","Error creating socket");
        }
        try{
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
            Log.e("socket_connect","Connected");
        }
        catch (IOException e){
            Log.e("",e.getMessage());
            try {
                Log.e("socketConnection","trying fallback...");

                mBluetoothSocket =(BluetoothSocket) mBluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mBluetoothDevice,1);
                mBluetoothSocket.connect();
                mHandler.sendEmptyMessage(0);

                Log.e("socket_connect","Connected");
            }
            catch (Exception e2) {
                Log.e("socket_connect", "Couldn't establish Bluetooth connection!");
            }
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public static class Formatter {
        /** The format that is being build on */
        private byte[] mFormat;

        public Formatter() {
            // Default:
            mFormat = new byte[]{27, 33, 0};
        }
        public byte[] get() {
            return mFormat;
        }

        public Formatter bold() {
            // Apply bold:
            mFormat[2] = ((byte) (0x8 | mFormat[2]));
            return this;
        }

    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    public void myPrint(){
        BILL = "";

        BILL = "                   XXXX MART    \n"
                + "                   XX.AA.BB.CC.     \n " +
                "                 NO 25 ABC ABCDE    \n" +
                "                  XXXXX YYYYYY      \n" +
                "                   MMM 590019091      \n";
        BILL = BILL
                + "-----------------------------------------------\n";


        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "Rate", "Totel");
        BILL = BILL + "\n";
        BILL = BILL
                + "-----------------------------------------------";
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001000", "5", "10", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002000", "10", "5", "50.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003000", "20", "10", "200.00");
        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

        BILL = BILL
                + "\n-----------------------------------------------";
        BILL = BILL + "\n\n ";

        BILL = BILL + "                   Total Qty:" + "      " + "85" + "\n";
        BILL = BILL + "                   Total Value:" + "     " + "700.00" + "\n";

        BILL = BILL
                + "-----------------------------------------------\n";
        BILL = BILL + "\n\n ";
        Log.d("MY_PRINTER", BILL);
    }

    private String printingProcedureStoreInfo(String msg){
        String finalMsg = "" ;
        if(msg != null && msg.length() > 0){
            msg = msg.replace(" ,", ",") ;
            int len = 32 - msg.length() ;
            int spaceCount = len / 2 ;
            if(!((spaceCount * 2) == len)){
                spaceCount ++ ;
            }
            Log.d("spaceCount", String.valueOf(spaceCount));
            for(int i = 1 ; i <= spaceCount ; i++){
                finalMsg = finalMsg +" " ;
            }
            finalMsg = finalMsg + msg + "\n" ;
        }
        return finalMsg ;
    }

    private String printingProcedure(){
        BILL = BILL + "Item      MRP   Qty        Total"+"\n" ;
        BILL = BILL + "--------------------------------\n";
        BILL = BILL+"\n" ;
        return BILL ;
    }

    private String printingProcedureProductInfo(String productName, String MRP, String Qty, String total){
        BILL = BILL + productName + "\n" ;
        int MRPspaceCount = 13 - MRP.length() ;
        for(int i = 1 ; i <= MRPspaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+MRP ;
        int QtySpaceCount = 6 - Qty.length() ;
        for(int i = 1 ; i <= QtySpaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+Qty ;
        int totalpaceCount = 13 - total.length() ;
        for(int i = 1 ; i <= totalpaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+total ;
        BILL = BILL+"\n" ;
        return BILL ;
    }

    private String printingProcedureMakeAddress(String address) {
        String[] commaSplited = address.split(",");
        //comaSparatedarray = splited.split(",");
        String addressString = "" ;
        String tmpString = "" ;
        if(commaSplited.length > 0){
            for(int i = 0; i < commaSplited.length ; i++){
                Log.d("COMA_STRING:", commaSplited[i]);
                String[] spaceSplited = commaSplited[i].split(" ") ;

                //WITHOUT SPACE STRING
                if(!(spaceSplited.length > 0)){
                    int len = tmpString.length() + commaSplited[i].length() ;
                    if(i != (commaSplited.length - 1)){
                        len++ ;
                    }
                    if(len < 32){
                        tmpString = tmpString + commaSplited[i];
                        if(i != (commaSplited.length - 1)){
                            tmpString = tmpString + " " ;
                        }
                        else if(i == (commaSplited.length - 1)){
                            addressString = addressString + printingProcedureStoreInfo(tmpString) ;
                            //addressString = addressString + tmpString+"\n" ;
                        }
                        Log.d("PureString", commaSplited[i]) ;
                    }
                    else{
                        addressString = addressString + printingProcedureStoreInfo(tmpString+",") ;
                        //addressString = addressString + tmpString+","+"\n" ;
                        tmpString = "" ;
                    }

                }

                //WITH SPACE STRING
                for(int j = 0 ; j < spaceSplited.length ; j++){
                    int len = tmpString.length() + spaceSplited[j].length() ;
                    if(j != (spaceSplited.length - 1)){
                        len++ ;
                    }

                    if(len < 32){
                        tmpString = tmpString + spaceSplited[j];
                        if(j != (spaceSplited.length - 1)){
                            tmpString = tmpString + " " ;
                        }
                        else if(i == (commaSplited.length - 1)){
                            addressString = addressString + printingProcedureStoreInfo(tmpString) ;
                            //addressString = addressString + tmpString+"\n" ;
                        }
                        Log.d("SpaceString", spaceSplited[j]) ;
                    }
                    else{
                        addressString = addressString + printingProcedureStoreInfo(tmpString+",");
                        //addressString = addressString + tmpString+","+"\n" ;
                        tmpString = "" ;
                        j-- ;

                    }
                    /*if(j == (spaceSplited.length - 1)){
                        int len2 = tmpString.length() + spaceSplited[i].length()+1 ;
                        addressString = addressString + tmpString+","+"\n" ;
                    }*/
                }
            }
        }

        else{
            String[] spaceSplited = address.split(" ");
            for(int j = 0 ; j < spaceSplited.length ; j++){
                int len = tmpString.length() + spaceSplited[j].length() ;
                if(j != (spaceSplited.length - 1)){
                    len++ ;
                }

                if(len < 32){
                    tmpString = tmpString + spaceSplited[j];
                    if(j != (spaceSplited.length - 1)){
                        tmpString = tmpString + " " ;
                    }
                    else if(j == (spaceSplited.length - 1)){
                        addressString = addressString + printingProcedureStoreInfo(tmpString) ;
                        //addressString = addressString + tmpString+"\n" ;
                    }
                    Log.d("SpaceString", spaceSplited[j]) ;
                }
                else{
                    addressString = addressString + printingProcedureStoreInfo(tmpString+",");
                    //addressString = addressString + tmpString+","+"\n" ;
                    tmpString = "" ;
                    j-- ;

                }
            }
        }
        Log.d("addressString", "\n"+addressString) ;
        return addressString ;
    }


}

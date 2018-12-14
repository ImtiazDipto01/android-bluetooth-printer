package com.annanovas.andoirdbluetoothprint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PrinttingActivity extends Activity implements Runnable {
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

    private String subTotalStr = "Sub total:" ;
    private String vatStr = "(+)vat:" ;
    private String discountStr = "(-)Discount:" ;
    private String roundingStr = "(-)Rounding:" ;
    private String netPayableStr = "Net Payable:" ;
    private Bitmap bitmap ;
    private int img = R.drawable.ic_android_black_2 ;

    Button btnGallery, btnCamera ;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private int camera_flag = -1 ;
    private ImageView imageView ;
    private static  final int PICK_BAR_CODE = 1, PICK_COMPANY = 2, REQUEST_CAMERA = 3, SELECT_FILE = 4 ;
    private ImageUtils imageUtils;
    private String imageUri;
    private Bitmap selectedImage;
    private Uri photoURI ;



    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.activity_printer);
        /*Drawable d = getResources().getDrawable(R.drawable.instagram);
        bitmap = drawableToBitmap(d) ;*/
        getBitmapFromURL();
        //myPrint();
        mScan = (Button) findViewById(R.id.Scan);
        imageUtils = new ImageUtils(getApplicationContext());
        btnGallery = findViewById(R.id.gallery);
        btnCamera = findViewById(R.id.camera) ;
        imageView = findViewById(R.id.iv_demo_image);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_flag = SELECT_FILE ;
                CheckPermission();
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_flag = REQUEST_CAMERA ;
                CheckPermission();
            }
        });

        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(PrinttingActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent,
                                REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(PrinttingActivity.this,
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

                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_name);
                            if(bitmap != null){
                                byte[] command = Utils.decodeBitmap(bitmap);
                                os.write(Utils.ESC_ALIGN_CENTER);
                                os.write(command);
                            }else{
                                Log.e("BitmapNull", "the file isn't exists");
                            }

                            os.write(Utils.ESC_ALIGN_LEFT);
                            BILL = BILL + printingProcedureStoreInfo("AnnaNovas Shop");
                            BILL = BILL + printingProcedureMakeAddress("South Banasree, Rampura, Dhaka, Dakhin Banasree Project Road, Dhaka 1219");
                            //BILL = BILL + printingProcedureStoreInfo("MohammadPur, Dhaka-1219");
                            BILL = BILL
                                    + "--------------------------------\n\n";
                            /*BILL = printingProcedure() ;
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

                            }*/

                            /*BILL = BILL
                                    + "--------------------------------\n\n";

                            BILL = printingProcedureFinalSec("1000.00","15.00", "1015.00", "5.00", "0.00", "1010.00");

                            BILL = BILL+"\n\n" ;*/
                            BILL = BILL + printingProcedureStoreInfo("Powered by AnnaNovas IT") ;

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
                            //os.write(EncodingUtils.getBytes(BILL, "ISO-8859-1"));

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
                            Log.e("PrinttingActivity", "Exe ", e);
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
                                 Intent data) {
        super.onActivityResult(mRequestCode, mResultCode, data);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:{
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = data.getExtras();
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
            }


            case REQUEST_ENABLE_BT:{
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(PrinttingActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(PrinttingActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case SELECT_FILE:{
                if(mResultCode == RESULT_OK){
                    Bitmap selectedImage = null;
                    Uri imageUri = CropImage.getPickImageResultUri(this, data);
                    cropRequest(imageUri);
                }

                break;
            }
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:{
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (mResultCode == RESULT_OK) {
                    if (camera_flag == REQUEST_CAMERA) {
                        try {
                            Uri resultUri = result.getUri();
                            File file = new File(String.valueOf(resultUri));

                            imageUri = imageUtils.getRealPathFromURI(resultUri);
                            Bitmap selectedImage = imageUtils.decodeBitmapFromUri(resultUri, 100);
                            if (selectedImage != null) {

                                RequestOptions options = new RequestOptions()
                                        //.override(imageDimen, imageDimen)
                                        .transforms(new FitCenter())
                                        .placeholder(R.drawable.ic_no_product_image)
                                        .error(R.drawable.ic_no_product_image)
                                        .priority(Priority.HIGH);


                                Glide.with(this)
                                        .load(imageUri)
                                        .apply(options)
                                        .into(imageView);
                            }
                        } catch (FileNotFoundException e) {

                        }
                    }
                    else if (camera_flag == SELECT_FILE) {
                        Log.e("SELECT_FILE", "yes");
                        Bitmap selectedImage = null;
                        Uri resultUri = result.getUri();
                        try {
                            imageUri = imageUtils.getRealPathFromURI(resultUri);
                            selectedImage = imageUtils.decodeBitmapFromUri(resultUri, 100);
                            selectedImage = imageUtils.imageProcess(selectedImage, imageUri);
                            if (selectedImage != null) {

                                RequestOptions options = new RequestOptions()
                                        .transforms(new FitCenter())
                                        .placeholder(R.drawable.ic_no_product_image)
                                        .error(R.drawable.ic_no_product_image)
                                        .priority(Priority.HIGH);

                                File file = new File(imageUri);
                                if (file.exists()) {
                                    Glide.with(this)
                                            .load(imageUri)
                                            .apply(options)
                                            .into(imageView);
                                } else {
                                    Glide.with(this)
                                            .load(Uri.parse("file:///android_asset/SampleProductImage/" + imageUri))
                                            .apply(options)
                                            .into(imageView);
                                }

                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            }
            case REQUEST_CAMERA:{
                cropRequest(photoURI);
            }
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
                try {
                    mBluetoothSocket = (BluetoothSocket) mBluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mBluetoothDevice, 2);
                    mBluetoothSocket.connect();
                    mHandler.sendEmptyMessage(0);
                    Log.e("socket_connect_2","Connected");
                }
                catch (Exception e3) {
                    Log.e("socket_connect_2", "Couldn't establish Bluetooth connection!");
                }
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
            Toast.makeText(PrinttingActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
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

    private String printingProcedureFinalSec(String subTotal, String vat, String includeVatTotal, String discount, String rounding, String netPayable){
        //SUBTOTAL
        int spaceCount = 13 - subTotalStr.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+subTotalStr ;
        spaceCount = 19 - subTotal.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+subTotal ;
        BILL = BILL+"\n" ;

        //VAT
        spaceCount = 13 - vatStr.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+vatStr ;
        spaceCount = 19 - vat.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+vat ;
        BILL = BILL+"\n" ;

        BILL = BILL + "--------------------------------";
        BILL = BILL+"\n" ;

        //INCLUDE VAT TOTAL
        spaceCount = 32 - includeVatTotal.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+includeVatTotal ;
        BILL = BILL+"\n" ;

        //DISCOUNT
        spaceCount = 13 - discountStr.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+discountStr ;
        spaceCount = 19 - discount.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+discount ;
        BILL = BILL+"\n" ;

        //ROUNDING
        spaceCount = 13 - roundingStr.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+roundingStr ;
        spaceCount = 19 - rounding.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+rounding ;
        BILL = BILL+"\n" ;

        BILL = BILL + "--------------------------------";
        BILL = BILL+"\n" ;


        //NET PAYABLE
        spaceCount = 13 - netPayableStr.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+netPayableStr ;
        spaceCount = 19 - netPayable.length() ;
        for(int i = 1 ; i <= spaceCount ; i++){
            BILL = BILL+" " ;
        }
        BILL = BILL+netPayable ;
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


    private boolean patternMatching(String input){
        final Pattern pattern = Pattern.compile("^[A-Za-z, 0-9!@#$%^&*()<>?:;./+-]++$");
        boolean flag = false ;
        if (!pattern.matcher(input).matches()) {
            Log.d("String_matching", "Invalid String") ;
            //throw new IllegalArgumentException("Invalid String");
            flag = false ;
        }
        else{
            Log.d("String_matching", "valid String") ;
            flag = true ;
        }
        return  flag ;
    }

    public void printPhoto(int img) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    private void getBitmapFromURL()
    {
        ProcessJSON processJSON = new ProcessJSON();
        processJSON.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @SuppressLint("StaticFieldLeak")
    private class ProcessJSON extends AsyncTask<String, String, Boolean> {

        String src = "http://d297eo5mdmmrj6.cloudfront.net/userImage/userThumbImage/2018/12/5c137a209cbf1_1544780320.png" ;

        ProcessJSON() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean result = true;
            try
            {
                URL url = new URL(src);
                HttpURLConnection connection;
                connection=(HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.e(TAG, "getBitmapFromURL: "+e);
                return false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                bitmap = getResizedBitmap(bitmap, 100, 100) ;
            }

        }

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        Log.e("getResizedBitmap", "true") ;
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    private void CheckPermission() {
        final int hasCameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        final int hasStoragePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final List<String> listPermissionNeeded = new ArrayList<>();
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionNeeded.isEmpty()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showMessageOKCancel(getResources().getString(R.string.alert_message_image), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                ActivityCompat.requestPermissions(PrinttingActivity.this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), REQUEST_CODE_ASK_PERMISSIONS);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(PrinttingActivity.this, getResources().getString(R.string.toast_suggestion), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
        else {
            //openBottomSheet();
            if(camera_flag == SELECT_FILE){
                selectGallery();
            }
            else if(camera_flag == REQUEST_CAMERA){
                openCamera();
            }
        }
    }

    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PrinttingActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void cropRequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropWindowSize(0,0)
                .start(this);
    }

    private void openCamera(){
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = imageUtils.createImageFile();
            imageUri = String.valueOf(file);
            photoURI = FileProvider.getUriForFile(PrinttingActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Map<String, Integer> perms = new HashMap<>();
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, getResources().getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                        //openBottomSheet();
                        if(camera_flag == 1){
                            selectGallery();
                        }
                        else if(camera_flag == 2){
                            openCamera();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




}

package com.dvrblacktech.viewmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;


import static android.Manifest.permission.RECORD_AUDIO;
import static android.content.ContentValues.TAG;


public class MainActivity extends Activity {
    private WebView mWebView;
    private WebView mWebView1;
    ZoomControls simpleZoomControls;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_RECORD_AUDIO = 101;
    private static final int STORAGE_PERMISSION_CODE = 102;

    private PermissionRequest myRequest;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    Button mButton,upButton,downButton,leftButton,rightButton,controlButton,homeButton;
    EditText mEdit;
    String weburl;
    int aaa=0;


    @Override
    protected void onStart() {
        super.onStart();



        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }


    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission},
                    requestCode);
        } else {

        }

    }


    @SuppressLint("SourceLockedOrientationActivity")

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        simpleZoomControls = (ZoomControls) findViewById(R.id.simpleZoomControl);
        upButton = (Button)findViewById(R.id.buttonUp);
        downButton = (Button)findViewById(R.id.buttonDown);
        leftButton = (Button)findViewById(R.id.buttonLeft);
        rightButton = (Button)findViewById(R.id.buttonRight);
        homeButton = (Button)findViewById(R.id.home);

        upButton.setVisibility(View.GONE);
        downButton.setVisibility(View.GONE);
        leftButton.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);
        simpleZoomControls.hide();


        mWebView = (WebView) findViewById(R.id.webView);
        String newUA= "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36 Edg/83.0.478.58";
        mWebView.getSettings().setUserAgentString(newUA);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                myRequest = request;

                for (String permission : request.getResources()) {
                    switch (permission) {
                        case "android.webkit.resource.AUDIO_CAPTURE": {
                            askForPermission(request.getOrigin().toString(), RECORD_AUDIO, REQUEST_RECORD_AUDIO);
                            break;
                        }
                    }
                }
            }
        });

        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }
            }
        });



        mButton = (Button)findViewById(R.id.button3);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mEdit   = (EditText)findViewById(R.id.editText);
                weburl = mEdit.getText().toString();
                mWebView.loadUrl(weburl);
                mButton.setVisibility(View.GONE);
                mEdit.setVisibility(View.GONE);
            }
        });


        controlButton = (Button)findViewById(R.id.control);

        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (aaa==0){

                    upButton.setVisibility(View.VISIBLE);
                    downButton.setVisibility(View.VISIBLE);
                    leftButton.setVisibility(View.VISIBLE);
                    rightButton.setVisibility(View.VISIBLE);
                    simpleZoomControls.show();
                    aaa=1;

                }

                else {
                    upButton.setVisibility(View.GONE);
                    downButton.setVisibility(View.GONE);
                    leftButton.setVisibility(View.GONE);
                    rightButton.setVisibility(View.GONE);
                    simpleZoomControls.hide();
                    aaa=0;

                }


            }
        });


        mWebView1 = (WebView) findViewById(R.id.webView2);
        mWebView1.getSettings().setJavaScriptEnabled(true);
        mWebView1.getSettings().setBuiltInZoomControls(true);
        mWebView1.getSettings().setDisplayZoomControls(false);
        mWebView1.getSettings().setLoadWithOverviewMode(true);
        mWebView1.getSettings().setUseWideViewPort(true);
        mWebView1.loadUrl("https://dvrblacktech.000webhostapp.com/ide.htm");

        mWebView1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });






        // perform setOnZoomInClickListener event on ZoomControls
        simpleZoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calculate current scale x and y value of ImageView
                float x = mWebView.getScaleX();
                float y = mWebView.getScaleY();
                // set increased value of scale x and y to perform zoom in functionality


                mWebView.setScaleX((float) (x + 0.1));
                mWebView.setScaleY((float) (y + 0.1));



                // display a toast to show Zoom In Message on Screen
                Toast.makeText(getApplicationContext(),"Zoom In",Toast.LENGTH_SHORT).show();
                mWebView.getSettings().setBuiltInZoomControls(true);
                // hide the ZoomControls from the screen


            }
        });
        // perform setOnZoomOutClickListener event on ZoomControls
        simpleZoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calculate current scale x and y value of ImageView
                float x = mWebView.getScaleX();
                float y = mWebView.getScaleY();
                // set decreased value of scale x and y to perform zoom out functionality
                mWebView.setScaleX((float) (x - 0.1));
                mWebView.setScaleY((float) (y - 0.1));
                // display a toast to show Zoom Out Message on Screen
                Toast.makeText(getApplicationContext(),"Zoom Out",Toast.LENGTH_SHORT).show();
                // hide the ZoomControls from the screen

            }
        });


        upButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                float yy = mWebView.getY();
                mWebView.setY((float)(yy-36));


            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                float yy = mWebView.getY();
                mWebView.setY((float)(yy+36));

            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                float xx = mWebView.getX();
                mWebView.setX((float)(xx+36));

            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                float xx = mWebView.getX();
                mWebView.setX((float)(xx-36));
            }
        });



        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mWebView1 = (WebView) findViewById(R.id.webView2);
                mWebView1.getSettings().setJavaScriptEnabled(true);
                mWebView1.getSettings().setBuiltInZoomControls(true);
                mWebView1.getSettings().setDisplayZoomControls(false);
                mWebView1.getSettings().setLoadWithOverviewMode(true);
                mWebView1.getSettings().setUseWideViewPort(true);
                mWebView1.loadUrl("https://dvrblacktech.000webhostapp.com/ide.htm");
            }
        });


        mWebView1.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimeType);
                //------------------------COOKIE!!------------------------
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });
    }


    // To handle &quot;Back&quot; key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView1.canGoBack()) {
            mWebView1.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mWebView1.canGoBack()) {
            mWebView1.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void askForPermission(String origin, String permission, int requestCode) {
        Log.d("WebView", "inside askForPermission for" + origin + "with" + permission);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission},
                        requestCode);
            }
        } else {
            myRequest.grant(myRequest.getResources());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "MICROPHONE Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "MICROPHONE Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "STORAGE Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "STORAGE Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



}



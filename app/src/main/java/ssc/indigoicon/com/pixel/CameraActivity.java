package ssc.indigoicon.com.pixel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.FrameLayout;

import ssc.indigoicon.com.pixel.utils.CameraPreview;

public class CameraActivity extends AppCompatActivity  {

    private static final String TAG = "CameraActivity";


    private android.hardware.Camera mCamera;
    private CameraPreview mPreview;
    FrameLayout preview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

        getWindow ( ).setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView ( R.layout.activity_camera );


        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);






    }

    public static android.hardware.Camera getCameraInstance(){
        android.hardware.Camera c = null;
        try {
            c = android.hardware.Camera.open(); // attempt to get a Camera instance

        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable}

    }

    private android.hardware.Camera.PictureCallback mPicture = new android.hardware.Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] bytes, android.hardware.Camera camera) {

        }


    };





}

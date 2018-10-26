package ssc.indigoicon.com.pixel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ssc.indigoicon.com.pixel.utils.CameraPreview;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    ImageView setting,camera,capture,gallery,flash;
    private Camera mCamera;
    private CameraPreview mPreview;
    FrameLayout preview;
    boolean inPreview, recording, isFlash;
    Camera.CameraInfo currentCamInfo;
    int currentCameraId;
    private File fileTemp = null;
    private Uri fileUri = null;
    private String videoPath = "";
    boolean isfrontCameraPreview = false;
    boolean isVideo = false;
    KProgressHUD hud;
    private MediaRecorder mediaRecorder;
    public Camera.Parameters mParameters;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static final int MY_PERMISSIONS_REQUEST_AUDIO = 123;
    private boolean isActive;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static int PICK_IMAGE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView ( R.layout.activity_home );



        initializations();
        onClickListeners();
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);





    }

    private void showFileChooser() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);
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
        public void onPictureTaken(byte[] bytes , android.hardware.Camera camera) {

            File pictureFile = getOutputMediaFile ( );
            if (pictureFile == null) {
                return;

            }
            if (pictureFile == null) {
                return;
            }
            try {
//
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedbimap = Bitmap
                        .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                FileOutputStream fos = new FileOutputStream(pictureFile.getAbsolutePath());
                rotatedbimap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                fos.close();
                Log.i("File", pictureFile.getAbsolutePath());
                try {

                        Intent i = new Intent(HomeActivity.this, ImagePreviewActivity.class);
                        i.putExtra("Filepath", pictureFile.getAbsolutePath());
                        i.putExtra("CameraMood", isfrontCameraPreview);
                        //i.putExtra("IsVideo", isVideo);
                        isfrontCameraPreview = false;
                        startActivity(i);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }

    };


    private File getOutputMediaFile() {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;

    }


    private void onClickListeners() {

        capture.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {

                mCamera.takePicture(null, null, mPicture);


            }
        } );

        camera.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                isfrontCameraPreview = true;
                if (inPreview) {
                    mCamera.stopPreview();
                }

                mCamera.release();

                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                mCamera = Camera.open(currentCameraId);

                setCameraDisplayOrientation(HomeActivity.this, currentCameraId, mCamera);
                try {
                    mCamera.setPreviewDisplay(mPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();

            }
        } );

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFlash = !isFlash;
                if (isFlash) {
                    setFlash(true);
                    flash.setBackgroundResource(R.drawable.flashon);
                } else {
                    setFlash(false);
                    flash.setBackgroundResource(R.drawable.flashoff);
                }

            }
        });

        gallery.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                showFileChooser();

            }
        } );




        }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }



    public void setFlash(boolean flash) {
        mParameters = mCamera.getParameters();
        if (flash) {
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(mParameters);
        } else {
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParameters);
        }
    }











    private void initializations() {

        capture = findViewById ( R.id.capture );
        setting = findViewById ( R.id.setting );
        camera = findViewById ( R.id.camera );
        flash = findViewById ( R.id.flash );
        gallery = findViewById ( R.id.gallery );
        hud = KProgressHUD.create(this)
                .setCancellable(false)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

    }








}

package ssc.indigoicon.com.pixel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.io.File;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class ImagePreviewActivity extends AppCompatActivity {

    ImageView back_button, download, img_captureimage_preview;
    String filepath = "";
    File file, compressedImageFile, videoThumbnail;
    boolean isfrontCameraPreview;
    Intent intent;
    Bitmap bmThumbnail;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    int imageCounter = 0;
    int videoCounter = 0;
    KProgressHUD hud;
    PhotoEditor mPhotoEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView ( R.layout.activity_image_preview );
        initializations ( );
        onClickListeners();





        intent = getIntent ( );

        if (!intent.getExtras ( ).isEmpty ( )) {
                filepath = intent.getStringExtra ( "Filepath" );
                file = new File ( filepath );
                filepath = intent.getStringExtra ( "Filepath" );
                isfrontCameraPreview = intent.getBooleanExtra ( "CameraMood" , false );
                file = new File ( filepath );
                    Picasso.get ().load ( file ).into ( img_captureimage_preview );


                }
            }

    private void onClickListeners() {

        back_button.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent ( ImagePreviewActivity.this,HomeActivity.class );
                startActivity ( intent );

            }
        } );


    }


    private void initializations() {

        back_button = findViewById ( R.id.back_button );
        download = findViewById ( R.id.download );
        img_captureimage_preview = findViewById ( R.id.img_captureimage_preview );




    }




}

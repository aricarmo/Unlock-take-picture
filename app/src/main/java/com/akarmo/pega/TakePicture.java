package com.akarmo.pega;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by arilsoncarmo on 1/8/15.
 */
public class TakePicture {
    private static final File DESTINY_DIR = new File("/sdcard/Pega/");

    public static String takePicture(final String fileName) {
        Camera camera = null;
        try {
            int camIdx = 1; // front camera
            Camera.getCameraInfo(camIdx, new Camera.CameraInfo());
            camera = Camera.open(camIdx);

            camera.setPreviewTexture(new SurfaceTexture(0));
            camera.startPreview();
            camera.takePicture(null, null, new Camera.PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    saveFile(data, fileName);
                    camera.release();
                }
            });

        }catch (Exception e){
            if (camera != null) {
                camera.release();
            }
        }
        return fileName;
    }

    public static void saveFile(byte[] data, String fileName) throws RuntimeException {
        if (!DESTINY_DIR.exists() && !DESTINY_DIR.mkdirs()) {
            return;
        }
        File mainPicture = new File(DESTINY_DIR, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(mainPicture);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("Image could not be saved.", e);
        }
    }

}

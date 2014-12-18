package careeril.com.criminalintent.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import careeril.com.criminalintent.R;

/**
 * Created by Lordsnow1991 on 12/11/14.
 */
@SuppressWarnings("deprecation")
public class CrimeCameraFragment extends Fragment {


    public static final String EXTRA_PHOTO_FILENAME =
            "careril.com.android.criminalintent.photo_filename";
    static final String TAG = "CrimeCameraFragment";
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback()

    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // Create a filename
            String filename = UUID.randomUUID().toString() + ".jpg";
            // Save the jpeg data to disk
            FileOutputStream os = null;
            boolean success = true;

            try {
                os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                os.write(data);
            } catch (Exception e) {
                Log.e(TAG, "Error writing to file " + filename, e);
                success = false;
            } finally {
                try {
                    if (os != null)
                        os.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing file " + filename, e);
                    success = false;
                }
            }
            if (success) {
                Log.i(TAG, "JPEG saved at " + filename);
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME, filename);
                getActivity().setResult(Activity.RESULT_OK, i);

            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }

            getActivity().finish();
        }
    };
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private ImageButton mShutterButton;
    private View mProgressContainer;
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            // Display the progress indicator
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
        getActivity().getActionBar().hide();
        mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);


        ImageButton takePictureButton = (ImageButton) v
                .findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }

                //getActivity().finish();
            }
        });
        mSurfaceView = (SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        // setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated,
        // but are required for Camera preview to work on pre-3.0 devices.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                // Tell the camera to use this surface as its preview area
                try {
                    if (mCamera != null) {
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException exception) {
                    Log.e(TAG, "Error setting up preview display", exception);
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                // We can no longer display on this surface, so stop the preview.
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                if (mCamera == null) {
                    // preview surface does not exist
                    return;
                }


                // stop preview before making changes
                try {
                    mCamera.stopPreview();
                } catch (Exception e) {
                    // ignore: tried to stop a non-existent preview
                }

                // set preview size and make any resize, rotate or
                // reformatting changes here


                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
                parameters.setPreviewSize(s.width, s.height);
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
                parameters.setPictureSize(s.width, s.height);

                mCamera.setParameters(parameters);


                mCamera.setParameters(parameters);
                // start preview with new settings
                try {

                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();

                } catch (Exception e) {
                    Log.d(TAG, "Error starting camera preview: " + e.getMessage());
                }
            }


//


        });


        return v;


    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera = Camera.open();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * A simple algorithm to get the largest size available. For a more
     * robust version, see CameraPreview.java in the ApiDemos
     * sample app from Android.
     */
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }


}

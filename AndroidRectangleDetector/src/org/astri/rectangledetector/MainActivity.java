package org.astri.rectangledetector;

import org.astri.arprocessing.camera.CameraDataListener;
import org.astri.arprocessing.camera.CameraHandler;
import org.astri.rendering.RenderSurface;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends Activity implements CameraDataListener{

	static {
		System.loadLibrary("opencv_java");
		System.loadLibrary("rectangledetector");
	}
	
	private static final String TAG = "TemplateActivity";
	
	private CameraHandler camera;
	
	@Override
	public void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		// This if using rendering on Android side
		setContentView(R.layout.main);

		initCamera();
	}
	
	private void initCamera(){
		
		// This if using rendering on Android side
		SurfaceView cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
		
		// This if using rendering on native side
		RenderSurface sv = new RenderSurface(this);
		addContentView(sv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		sv.bringToFront();
		
		camera = new CameraHandler();
		camera.setPreviewHolder(cameraPreview);
		camera.setDataListener(this);
		Log.d(TAG, "finished camera init");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		camera.resumeCamera();
		startProcessing();

	}

	@Override
	public void onPause() {
		
		stopProcessing();
		camera.pauseCamera();
		super.onPause();
	}

	
	@Override
	public void receiveCameraFrame(byte[] data, int width, int height) {
		
		setCameraFrame(data);
		
	}
	
	private native void startProcessing();
	private native void stopProcessing();
	private native void setCameraFrame(byte[] frame);

}
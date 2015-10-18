package com.library.mediaplayer.streaming.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;


import com.android.volley.AuthFailureError;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
/**
 * This class provides utility methods for downloading, storing and loading images from/to the internal storage. 
 * Note that the image downloading process is asynchronous, so it uses the interface {@link ImageDownloader.IBitmapReceiver} 
 * for notifying the user about the downloading outcomes.
 */
public class ImageDownloader {
	
	/**
	 * An ImageDownloader user must provide this interface for receiving notifications about image donwloading and storing.
	 *  
	 */
	public interface IBitmapReceiver {
		
		/**
		 * Called when a bitmap was successfully downloaded
		 * @param imageDownloader the ImageDownloader that has triggered this callback
		 * @param image the downloaded bitmap
		 */
		public void onBitmapDownloaded(ImageDownloader imageDownloader, Bitmap image);
		
		/**
		 * Called when the image downloading failed for some raison
		 * @param imageDownloader the ImageDownloader that has triggered this callback
		 * @param ex the exception raised during the downloading process
		 */
		public void onBitmapDownloadingError(ImageDownloader imageDownloader, Exception ex);
		
		/**
		 * Called when the bitmp was stored in to the internal storage
		 * @param imageDownloader the ImageDownloader that has triggered this callback
		 * @param filename the name of the stored file
		 */
		public void onBitmapSaved(ImageDownloader imageDownloader, String filename);
		
		/**
		 * Called when the image saving failed for some raison
		 * @param imageDownloader the ImageDownloader that has triggered this callback
		 * @param ex the exception raised during the saving process
		 */
		public void onBitmapSavingError(ImageDownloader imageDownloader, Exception ex);
	}
	
	private String username;
	private String password;
    private Context ctx;
    
    private static final String TAG = "ImageDownloader";
    
    IBitmapReceiver receiver = null;
    
    
    /**
     * This class handles asynchronous image downloads. Once a image has been successfully downloaded,  
     * it calls the method {@link ImageDownloader.IBitmapReceiver#onBitmapReady(Bitmap)}}
     * @param receiver the object that will receive the downloaded image
     * @param ctx the activity context
     * @param username the user name (needed for authentication)
     * @param password the password (needed for authentication)
     */
	public ImageDownloader(IBitmapReceiver receiver, Context ctx, String username, String password)
	{
		this.username = username;
		this.password = password;
		this.ctx = ctx;
		this.receiver = receiver;
	}
	
	/**
	 *  asynchronously downloads an image from the web 
	 * @param url the url image
	 */
	public void downloadImage(String url)
    {     
		ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {
			
		    @Override
		    public void onResponse(Bitmap response) {
		    	receiver.onBitmapDownloaded(ImageDownloader.this, response);
		    }
		    
		}, 0, 0, null, null) {
			
	
		    @Override
			public Map<String, String> getHeaders() throws AuthFailureError {
		    	  HashMap<String, String> params = new HashMap<String, String>();
		    	  String creds = String.format("%s:%s",username,password);
		    	  String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
		    	  params.put("Authorization", auth);
		    	  return params;
		      }
		};
		
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.ctx);

        // Add the request to the RequestQueue.
        queue.add(ir);

    }
    
	public void logAppFileNames()
	{
		String [] files = ctx.getFilesDir().list();
		for (String fn : files)
		{
			Log.d("TAG", "File:" + fn);
		}
	}
	
	/**
	 * Save a bitmap into to internal storage
	 * @param image the bitmap to save
	 * @param filename the filename of the saved bitmap
	 */
		public void saveImageToInternalStorage(Bitmap image,String filename)
		{
		try {
			FileOutputStream fos = ctx.openFileOutput(filename+ ".jpg", Context.MODE_PRIVATE);
			image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			                // 100 means no compression, the lower you go, the stronger the compression
			fos.close();
			Log.d(TAG, "Image saved on dir:" + ctx.getFilesDir().getAbsolutePath() );
			receiver.onBitmapSaved(this, filename);
		}
		catch (Exception e) {
			Log.e("saveToInternalStorage():" , e.getMessage());
			receiver.onBitmapSavingError(this, e);
				}
		}
   
		
		/**
		 * Get the list of .jpeg images stored in the internal archive (private to the activity context passed as argument)
		 * @param ctx the activity context
		 * @return the list of .jpg images 
		 */
		public static File [] getInternalImages(Context ctx)
		{
			return getInternalImages(ctx, ".jpg");
		}
		
		/**
		 * Get the list of images stored in the internal archive (private to the activity context passed as argument)
		 * @param ctx the activity context
		 * @param filter the image type filter (e.g: ".jpg")
		 * @return the list of images
		 */
		public static File [] getInternalImages(Context ctx, final String filter)
		{
			File appDir = ctx.getFilesDir();
			File[] jpgFiles = appDir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					 
					return pathname.exists() && pathname.getAbsolutePath().endsWith(filter);
				}});
			
			// sort by date
			Arrays.sort(jpgFiles, new Comparator()
			{
			    public int compare(Object o1, Object o2) {

			        if (((File)o1).lastModified() > ((File)o2).lastModified()) {
			            return -1;
			        } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
			            return 1;
			        } else {
			            return 0;
			        }
			    }

			}); 
			return  jpgFiles;
		}
		
		/**
		 * Deletes an image   from the internal storage 
		 * @param ctx the context the activity context
		 * @param filename the name (no path) )of the file to be deleted 
		 * @return true if the file was deleted , false otherwise
		 */
		public static boolean deleteInternalFile(Context ctx, String filename) {
			File filePath = new File(ctx.getFilesDir().getAbsolutePath() + "/" + filename);
			
			return filePath.exists() && filePath.delete();
		}
		
		/**
		 * Loads an image from the internal storage. The loaded bitmap will be sent to the callback method {@link IBitmapReceiver#onBitmapDownloaded(ImageDownloader, Bitmap)}
		 * @param filename the name of the file to load
		 */
		public void loadImageFromInternalStorage(String filename) {

			Bitmap thumbnail = null;

			
			try {
				File filePath = ctx.getFileStreamPath(filename);
				FileInputStream fi = new FileInputStream(filePath);
				thumbnail = BitmapFactory.decodeStream(fi);
				
			
				} catch (Exception ex) {
						Log.e( "getThumbnail() on internal storage", ex.getMessage());
						receiver.onBitmapDownloadingError(this, ex);
						return;
				}
			receiver.onBitmapDownloaded(ImageDownloader.this, thumbnail);
			}
}

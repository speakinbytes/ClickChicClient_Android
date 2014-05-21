package com.speakinbytes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.speakinbytes.utils.constants.Constants;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

public class Files {

	private static final String TAG = "FILES";
	/**
	 * Parseo de un fichero m3u de audio
	 *  
	 * @param urlStream String con la URL del m3u
	 * @return String con la url del stream de audio
	 */
	public static String getUrlStream(String urlStream) {
		// Si nos llega un m3u, nos quedamos la primera url de un fichero de
		// audio
		String buena = null;

		BufferedReader reader = null;
		try {
			URL url = new URL(urlStream);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));

			String aux = null;
			buena = null;
			while (buena == null) {
				aux = reader.readLine();

				if (aux == null) {
					break;
				} else {
					if (!aux.startsWith("#") && aux.length() > 5) {
						buena = aux;
					}
				}
			}
		} catch (Exception e) {
			if(Constants.debug)
				Log.e(TAG, "Error parseando el fichero m3u de la radio\n"+e.toString());
			buena = urlStream;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					if(Constants.debug)
						Log.e(TAG, e.toString());
				}
			}

		}
		return buena;
	}

	/**
	 * Convierte un fichero a String
	 * 
	 * @param f fichero origen
	 * @return String contenido en el fichero
	 */
	public static String streamToString(InputStream f) {

		byte[] b = null;
		try {
			b = new byte[f.available()];
			f.read(b);
		} catch (IOException e) {
			if(Constants.debug)
				Log.e(TAG, e.toString());
		}

		String result = new String(b);

		return result;
	}

	
	/**
	 * Copia una imagen a la galeria de fotos
	 * 
	 * @param imagePath
	 *            Ruta a la imagen
	 * @return la url creada
	 */
	@SuppressLint("DefaultLocale")
	public static Uri saveImageEntry(Context context, String imagePath, String nameApp) {

		File imageFile = new File(imagePath);

		ContentValues v = new ContentValues();
		v.put(Images.Media.TITLE, nameApp + imageFile.getName());
		v.put(Images.Media.DISPLAY_NAME, nameApp + imageFile.getName());
		v.put(Images.Media.MIME_TYPE, "image/jpeg");

		File f = new File(imagePath);
		File parent = f.getParentFile();
		String path = parent.toString().toLowerCase();
		String name = parent.getName().toLowerCase();
		v.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
		v.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		v.put(Images.Media.SIZE, f.length());
		f = null;

		v.put("_data", imagePath);
		ContentResolver c = context.getContentResolver();
		Uri uri = null;
		try {
			uri = c.insert(Images.Media.EXTERNAL_CONTENT_URI, v);
		} catch (Exception e) {
			if(Constants.debug)
				Log.e(TAG, "Error " + e.toString());
		}
		return uri;
	}
	
	/**
	 * Obtiene la direcci�n de un recurso a trav�s de un ContenProvider pas�ndole la uri:
	 * (por ejemplo: content://media/external/video/media) 
	 * 
	 * @param context contexto
 	 * @param file File con el fichero que contiene los datos
	 * @param uriBase String base de la uri
	 * @return
	 */
	public static Uri getFileContentUri(Context context, File file, String uriBase) {
		String filePath = file.getAbsolutePath();
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Video.Media._ID },
				MediaStore.Video.Media.DATA + "=? ", new String[] { filePath },
				null);

		if (cursor != null && cursor.moveToFirst()) {
			int id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.MediaColumns._ID));
			//uri = "content://media/external/video/media"
			Uri baseUri = Uri.parse(uriBase);
			return Uri.withAppendedPath(baseUri, "" + id);
		} else {
			if (file.exists()) {
				ContentValues values = new ContentValues();
				values.put(MediaStore.Video.Media.DATA, filePath);
				return context.getContentResolver().insert(
						MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
			} else {
				return null;
			}
		}
	}

	/**
	 * Elimina un fichero situadio en la direcci�n que se le pasa como par�metro
	 * 
	 * @param context Context de la aplicaci�n
	 * 
	 * @param uri Uri localizaci�n del fichero
	 * 
	 * @return true si se elimin� correctamente
	 *         false si no se pudo eliminar
	 */
	public static boolean deleteFileContentUri(Context context, Uri uri) {

		if (context.getContentResolver().delete(uri, null, null) > 0) {
			return true;
		} else {
			return false;
		}

	}
	
	public static void updateSD(Context mContext)
	{
		((Application)mContext).sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+Environment.getExternalStorageDirectory())));
	}
}

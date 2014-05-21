package com.speakinbytes.utils.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;

import com.speakinbytes.utils.R;
import com.speakinbytes.utils.Dates;
import com.speakinbytes.utils.constants.Constants;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class ConnectionManager {

	private static HttpClient client;
	private static AndroidHttpClient clientAndroid;
	private static final String TAG = "CONNECTION_MANAGER";
	private static BasicHttpContext mHttpContext;
	private static boolean bRedirect;

	/**
	 * Ejecuci�n de conexiones con reintento.
	 * 
	 * @param url
	 *            conexi�n
	 * @param retry
	 *            n�mero de reintentos
	 * @return respuesta
	 * 
	 * @throws Exception
	 */
	public static String executeHttpGetWithRetry(String url, int retry)
			throws Exception {
		int count = 0;

		while (count < retry) {
			count += 1;

			try {
				String response = getExecuteHttp(url, false);
				return response;
			} catch (Exception e) {
				if (count < retry) {
					if(Constants.debug)
						Log.e(TAG, "Error " + e.toString());
				} else {
					throw e;
				}
			}
		}
		return null;
	}

	/**
	 * M�todo que ejecuta una petici�n Http basado en el DefaultHttpClient,
	 * asignando redirecci�n y buscando un header determinado.
	 * 
	 * @param ctx
	 *            Contexto de la aplicaci�n
	 * @param url
	 *            String con la url de la petici�n
	 * @param headerName
	 *            String con el nombre de la cabecera
	 * @param redirect
	 *            boolean que permite o no la redirecci�n
	 * @return String valor del header determinado
	 * 
	 */
	public static String getHeadersHttpRedirect(Context ctx, String url,
			String headerName, boolean redirect) {
		client = CustomHttpClient.getHttpClient();
		HttpClientParams.setRedirecting(client.getParams(), redirect);
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(httpget);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				Header[] headers = response.getHeaders(headerName);
				if (headers != null && headers.length != 0) {
					String newUrl = headers[0].getValue();
					if (headerName.equals("Date"))
						return getDate(response);
					
					return formatDate(newUrl);
				}
			}
		} catch (ClientProtocolException e) {
			if(Constants.debug)
				Log.e(TAG, e.toString());
		} catch (IOException e) {
			if(Constants.debug)
				Log.e(TAG, e.toString());
		}
		return null;
	}
	
	private static String formatDate(String fecha)
	{
		Date date;
		try {
			SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss",
					Locale.US);
			date = df.parse(fecha.replaceAll("\\p{Cntrl}", ""));
			// df.format(date);
			SimpleDateFormat date_format_gmt = new SimpleDateFormat(
					"dd-MM-yyyy HH:mm:ss");
			String t = date_format_gmt.format(date);
			return t;
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static String getDate(HttpResponse response)
			throws IllegalStateException, IOException {
		String newUrl = null;
		Header[] headers = response.getHeaders("Date");
		if (headers != null && headers.length != 0)
			newUrl = headers[0].getValue();

		if (newUrl != null) {
			String gmt;
			StringBuffer builder = new StringBuffer();
			InputStream content = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			content.close();
			gmt = builder.toString();
			String[] date = new String[3];

			date[0] = newUrl.substring(0, newUrl.indexOf("GMT"));
			date[1] = newUrl.substring(newUrl.indexOf("GMT"), newUrl.length());
			date[2] = "GMT+" + gmt;

			if (date != null && date[0] != null) {
				String dateStr = Dates.parseRTVEDate(date[1], date[2], date[0]);
				return dateStr;
			} else {
				return null;
			}
		} else {
			Date d = new Date();
			return Dates.dateToString(d, Constants.FULL_GUION);
		}

	}

	public static void setHttpContext(BasicHttpContext context) {
		mHttpContext = context;
	}

	/**
	 * M�todo que ejecuta una petici�n Http basado en el DefaultHttpClient
	 * 
	 * @param url
	 *            String con la url a la que se quiere realizar la petici�n
	 */
	public static String getExecuteHttp(String url, boolean bError ) throws Exception {

		BufferedReader in = null;
		StringBuilder builder = new StringBuilder();
		String result = null;
		HttpResponse response;
		try {
			client = CustomHttpClient.getHttpClient();
			
			client = CustomHttpClient.sslClient(client);
			HttpGet request = new HttpGet(url);
			request.addHeader("Accept-Encoding", "gzip");
			HttpClientParams.setRedirecting(client.getParams(), true);
			client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true); 
			if (mHttpContext != null)
				response = client.execute(request, mHttpContext);
			else
				response = client.execute(request);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if(Constants.debug)
				Log.i(TAG, url+" Response "+statusCode);
			if (statusCode == HttpStatus.SC_OK|| (bError && statusCode == HttpStatus.SC_NOT_FOUND)) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();

				if (entity.getContentEncoding() != null
						&& "gzip".equalsIgnoreCase(entity.getContentEncoding()
								.getValue())) {
					result = uncompressInputStream(content);
				} else {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					content.close();
					result = builder.toString();
				}
			
			}
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
		
	}

	/**
	 * M�todo que ejecuta una petici�n Http basado en el DefaultHttpClient
	 * 
	 * @param url
	 *            String con la url a la que se quiere realizar la petici�n
	 */
	public static String postExecuteHttp(String url, String query, boolean bError)
			throws Exception {

		BufferedReader in = null;
		StringBuilder builder = new StringBuilder();
		String result = null;
		HttpResponse response;
		try {
			client = CustomHttpClient.getHttpClient();
			client = CustomHttpClient.sslClient(client);
			HttpClientParams.setRedirecting(client.getParams(), true);
			HttpPost request = new HttpPost(url);
			request.addHeader("Accept-Encoding", "gzip");
			if (query != null) {
				StringEntity se;
				try {
					se = new StringEntity(query, HTTP.UTF_8);
					se.setContentType("application/x-www-form-urlencoded");
					request.setEntity(se);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
			//client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true); 
			if (mHttpContext != null)
				response = client.execute(request, mHttpContext);
			else
				response = client.execute(request);
			if(Constants.debug)
				Log.i(TAG, url.toString());
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == HttpStatus.SC_OK || (bError && statusCode == HttpStatus.SC_NOT_FOUND)) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				result="";
				if (entity.getContentEncoding() != null
						&& "gzip".equalsIgnoreCase(entity.getContentEncoding()
								.getValue())) {
					result = uncompressInputStream(content);
				} else {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					content.close();
					result = builder.toString();
				}

			}
			
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}

	/**
	 * M�todo que ejecuta una petici�n Http basado en el AndroidHttpClient
	 * 
	 * @param url
	 *            String con la url a la que se quiere realizar la petici�n
	 */
	public static String getExecuteAndroidHttp(String url) {
		try {
			clientAndroid = CustomHttpClient.getAndroidClient();
			HttpGet request = new HttpGet(url);
			String response = clientAndroid.execute(request,
					new BasicResponseHandler());

			clientAndroid.close();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (clientAndroid != null) {
				clientAndroid.close();
			}
		}
		return null;
	}

	/**
	 * M�todo que descomprime la respuesta de una petici�n con codificaci�n GZIP
	 * 
	 * @param inputStream
	 *            InputStream con los datos que hay que descomprimir
	 */

	private static String uncompressInputStream(InputStream inputStream)
			throws IOException {
		StringBuilder value = new StringBuilder();

		GZIPInputStream gzipIn = null;
		InputStreamReader inputReader = null;
		BufferedReader reader = null;

		try {
			gzipIn = new GZIPInputStream(inputStream);
			inputReader = new InputStreamReader(gzipIn, "UTF-8");
			reader = new BufferedReader(inputReader);

			String line = "";
			while ((line = reader.readLine()) != null) {
				value.append(line + "\n");
			}
		} finally {
			try {
				if (gzipIn != null) {
					gzipIn.close();
				}

				if (inputReader != null) {
					inputReader.close();
				}

				if (reader != null) {
					reader.close();
				}

			} catch (IOException io) {
				if(Constants.debug)
					Log.e(TAG, io.toString());
				// io.printStackTrace();
			}

		}

		return value.toString();
	}

}

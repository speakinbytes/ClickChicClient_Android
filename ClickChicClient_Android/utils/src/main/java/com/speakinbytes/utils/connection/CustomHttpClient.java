package com.speakinbytes.utils.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.net.http.AndroidHttpClient;


public class CustomHttpClient {

	private static DefaultHttpClient customHttpClient;

	private static AndroidHttpClient customAndroid;
	
	private static int toCon, toSocket;

	private static String userAgent = "Mozilla/5.0 (Linux; U; Android 2.2.1; es-es; Nexus One Build/FRG83) "
			+ "AppleWebKit/533.1 (KHTML, like Gecko) Version 4.0 Mobile Safari/533.1";

	
	/** A private Constructor prevents instantiation */
	private CustomHttpClient() {
	}
	/**
	 * 
	 * @param toConS tiempo de timeout en milisegundos hasta que se establece la conexi�n
	 * @param toSocketS tiempo de timeout de socket en milisegundos hasta esperando por los datos
	 */
	public static void inicParams(int toConS, int toSocketS)
	{
		toCon = toConS;
		toSocket = toSocketS;
		
	}

	/**
	 * Crea un singleton HttpClient
	 * 
	 * @return DefaultHttpClient
	 */
	public static synchronized DefaultHttpClient getHttpClient() {
		if (customHttpClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpClientParams.setRedirecting(params, true);
			
			//params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params, userAgent);
			
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used. 
			//int timeoutConnection = 6000;
			int timeoutConnection = toCon;
			HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			//int timeoutSocket = 20000;
			int timeoutSocket = toSocket;
			HttpConnectionParams.setSoTimeout(params, timeoutSocket);

			/*ConnManagerParams.setTimeout(params, 10000);
			HttpConnectionParams.setConnectionTimeout(params, 100000);
			HttpConnectionParams.setSoTimeout(params, 100000);*/

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));

			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			
			customHttpClient = new DefaultHttpClient(conMgr, params);

		}
		return customHttpClient;
	}

	public static synchronized AndroidHttpClient getAndroidClient() {

		if (customAndroid == null) {
			customAndroid = AndroidHttpClient.newInstance(userAgent);

		}
		return customAndroid;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static HttpClient sslClient(HttpClient client) {
	    try {
	        X509TrustManager tm = new X509TrustManager() { 
	            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
	            }

	            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
	            }

	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        };
	        SSLContext ctx = SSLContext.getInstance("TLS");
	        ctx.init(null, new TrustManager[]{tm}, null);
	        SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
	        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        ClientConnectionManager ccm = client.getConnectionManager();
	        SchemeRegistry sr = ccm.getSchemeRegistry();
	        sr.register(new Scheme("https", ssf, 443));
	        HttpProtocolParams.setVersion(client.getParams(), HttpVersion.HTTP_1_1);
			HttpClientParams.setRedirecting(client.getParams(), true);
			
			//params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpProtocolParams.setContentCharset(client.getParams(),
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(client.getParams(), true);
			HttpProtocolParams.setUserAgent(client.getParams(), userAgent);
	        return new DefaultHttpClient(ccm, client.getParams());
	    } catch (Exception ex) {
	        return null;
	    }
	}
	
	public static class MySSLSocketFactory extends SSLSocketFactory {
	     SSLContext sslContext = SSLContext.getInstance("TLS");

	     public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	         super(truststore);

	         TrustManager tm = new X509TrustManager() {
	             public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	             }

	             public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	             }

	             public X509Certificate[] getAcceptedIssuers() {
	                 return null;
	             }
	         };

	         sslContext.init(null, new TrustManager[] { tm }, null);
	     }

	     public MySSLSocketFactory(SSLContext context) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
	        super(null);
	        sslContext = context;
	     }

	     @Override
	     public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	         return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	     }

	     @Override
	     public Socket createSocket() throws IOException {
	         return sslContext.getSocketFactory().createSocket();
	     }
	}

}

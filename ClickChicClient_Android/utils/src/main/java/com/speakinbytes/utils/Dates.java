package com.speakinbytes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.speakinbytes.utils.R;
import com.speakinbytes.utils.connection.ConnectionManager;
import com.speakinbytes.utils.constants.Constants;
import com.speakinbytes.utils.constants.CustomPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class Dates {


	/**
	 * Obtiene una fecha a partir de un string.
	 * 
	 * @param fechaText String con la fecha
	 * @param type int C�digo correspondiente al formato de fecha que se le est� pasando
	 * 
	 * @return Date fecha parseada a partir del String original
	 * 
	 * @throws java.text.ParseException
	 */
	public static Date StringToDate(String fechaText, int type)
			throws ParseException {
		SimpleDateFormat formatoDelTexto = chooseFormat(type);
		return formatoDelTexto.parse(fechaText);
	}

	/**
	 * Obtiene una fecha a partir de un timestamp en milisegundos
	 * 
	 * @param millis milisegundos
	 * 
	 * @return Date fecha correspondiente
	 */
	public static Date millisToDate(long millis) {
		Date resultdate = new Date(millis);
		return resultdate;
	}

	/**
	 * Obtiene un String a partir de una fecha. Devuelve la fecha en formato de 00 a 23 horas.
	 * 
	 * @param date Date con la fecha
	 * @param type int C�digo correspondiente al formato de fecha que se quiere como resultado
	 * 
	 * @return String fecha parseada a partir de la fecha original
	 * 
	 * @throws java.text.ParseException
	 */
	public static String dateToString(Date date, int type) {
		SimpleDateFormat sdf = chooseFormat(type);
		String s = sdf.format(date);
		if(type==Constants.API)
		{
			String hora = s.substring(8, s.length());
			if(hora.startsWith("24"))
			{
				String result = hora.replaceFirst("24", "00");
				s = s.replaceAll(hora,result);
			}
		}
			
		return s;
	}

	/**
	 * A partir de una fecha en formato string obtiene el timestamp en
	 * milisegundos, siempre con un formato de fecha completo dd:MM:yyyy
	 * hh:mm:ss
	 * 
	 * @param millis
	 * @return string con el valor
	 */
	public static String millisToString(long millis) {

		return dateToString(millisToDate(millis), Constants.FULL);
	}

	/**
	 * Convierte el calendario apartir de una fecha
	 */
	public static Calendar DateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal;

	}

	/**
	 * Convierte milisegundos a minutos y segundos y se lo pone a un textview
	 * 
	 * @param millis
	 *            tiempo en milisegundos
	 * @param t
	 *            TextView al que poner el tiempo
	 */
	public static void setTime(int millis, TextView t) {

		long time = millis / 1000;
		String seconds = Integer.toString((int) (time % 60));
		String minutes = Integer.toString((int) ((time % 3600) / 60));
		String hours = Integer.toString((int) (time / 3600));

		for (int i = 0; i < 2; i++) {

			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}

		if (hours.equals("0") || hours.equals("00")) {
			t.setText(minutes + ":" + seconds);
		} else {
			t.setText(hours + ":" + minutes + ":" + seconds);
		}
	}

	/**
	 * Devuelve la hora en formato xxh xxm xxs a partir de un n�mero de milisegundos.
	 * 
	 * @param millis int milisegundos correspondientes a la hora
	 * 
	 * @return String con el tiempo en formato:
	 *         xxh xxm xxs si son horas
	 *         xxm xxs si son minutos y no horas
	 */
	public static String getTime(int millis) {

		long time = millis / 1000;
		String seconds = Integer.toString((int) (time % 60));
		String minutes = Integer.toString((int) ((time % 3600) / 60));
		String hours = Integer.toString((int) (time / 3600));

		for (int i = 0; i < 2; i++) {

			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}

		if((minutes.equals("0") || minutes.equals("00")) && (hours.equals("0") || hours.equals("00"))){
			return seconds + "s";
		}
		else if (hours.equals("0") || hours.equals("00")) {
			return minutes + "m " + seconds + "s";			
		}
		else
			return hours + "h " + minutes + "m " + seconds + "s";
	}

	/**
	 * Devuelve el n�mero de horas, minutos y segundos correspondientes a un n�mero de milisegundos.
	 * 
	 * @param millis int milisegundos correspondientes al
	 * 
	 * @return int[3] int[0] - horas
	 *  			  int[1] - minutos
	 * 				  int[2] - segundos
	 */
	public static int[] getTimeInt(int millis) {

		long time = millis / 1000;
		String seconds = Integer.toString((int) (time % 60));
		String minutes = Integer.toString((int) ((time % 3600) / 60));
		String hours = Integer.toString((int) (time / 3600));
		int result[] = new int[3];
		for (int i = 0; i < 2; i++) {

			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}

			result[0] = Integer.parseInt(hours);
			result[1] = Integer.parseInt(minutes);
			result[2] = Integer.parseInt(seconds);
			return result;
	
	}

	/**
	 * Modifica la fecha que se le pasa por par�metro incrementando o
	 * decrementando su valor. Pueden ser horas, minutos o segundos
	 * 
	 * @param date
	 *            fecha que se quiere modificar
	 * @param type
	 *            1=hora, 2=minuto, 3=segundo
	 * @return
	 */
	public static String modify(Date date, int type, int value) {
		SimpleDateFormat s = chooseFormat(Constants.API);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch (type) {
		case Constants.HOURS:
			c.add(Calendar.HOUR, value);
			break;
		case Constants.MINUTES:
			c.add(Calendar.MINUTE, value);
			break;
		case Constants.SECONDS:
			c.add(Calendar.SECOND, value);
			break;
		}
		String output = s.format(c.getTime());
		return output;
	}

	/**
	 * Obtiene el formato de fecha que se quiere obtener
	 * 
	 * @param type
	 *            tipo de formato
	 * @return formato
	 */
	public static SimpleDateFormat chooseFormat(int type) {
		switch (type) {
		case Constants.API:
			return new SimpleDateFormat("ddMMyyyykkmmss");

		case Constants.FULL:
			return new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

		case Constants.ONLY_TIME:
			return new SimpleDateFormat("kk:mm:ss");

		case Constants.ONLY_DATE:
			return new SimpleDateFormat("dd/MM/yyyy");
		case Constants.FULL_GUION:
			return new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
		case Constants.FULL_GUION_INVERSE:
			return new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		case Constants.ONLY_HOUR:
			return new SimpleDateFormat("kk:mm");
		case Constants.FULL_NO_GUION:
			return new SimpleDateFormat("yyyyMMddkkmmss");
		default:
			return new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
		}
	}

	/**
	 * Obtiene y garda la fecha del consumer de timezone de RTVE
	 * 
	 * @param ctx Context de la aplicaci�n
	 * @return boolean true si obtuvo y almacen� la fecha correctamente
	 *                 false si la fecha es null 
	 */
	public static boolean getDate(final Context ctx) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String date = ConnectionManager.getHeadersHttpRedirect(ctx, ctx.getString(R.string.dateConsumer), "Date", false);
				if(date!=null)
				{
					if(Constants.debug)
						Log.i("DATES", "Get Date "+date);
					CustomPreferences.setPreferencesDate(ctx, date);
					
				}
				
			}
		});
		t.start();
		return true;
		
	}


	/**
	 * Parseo eficiente de la fecha recibida de la petici�n del Timezone de RTVE. 
	 * Se formatea la fecha para que se adapte al timezone determinado en la petici�n. 
	 * 
	 * @param origin_time_zone timezone origen
	 * @param target_time_zone timezone al que hay que pasarlo
	 * @param fecha String con la fecha obtenida en la cabecera de la petici�n
	 * 
	 * @return String con la fecha parseada
	 */
	public static String parseRTVEDate(String origin_time_zone,
			String target_time_zone, String fecha) {
		TimeZone tz = TimeZone.getTimeZone(origin_time_zone);
		SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss",
				Locale.US);
		df.setTimeZone(tz);
		// Eliminar los caracteres de control
		Date date;
		try {
			date = df.parse(fecha.replaceAll("\\p{Cntrl}", ""));
			// df.format(date);
			SimpleDateFormat date_format_gmt = new SimpleDateFormat(
					"dd-MM-yyyy HH:mm:ss");
			date_format_gmt.setTimeZone(TimeZone.getTimeZone(target_time_zone));
			return date_format_gmt.format(date);
		} catch (ParseException e) {

			e.printStackTrace();
			return null;
		}

	}
}

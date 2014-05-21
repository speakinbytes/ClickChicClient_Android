package com.speakinbytes.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;

public class MyHandlerException implements
		UncaughtExceptionHandler {
	private final Context myContext;
	private final Class<?> myActivityClass;
	 // uncaught exception handler variable
    private UncaughtExceptionHandler defaultUEH;
	public MyHandlerException(Context context, Class<?> c) {
		defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		myContext = context;
		myActivityClass = c;
	}

	public void uncaughtException(Thread thread, Throwable exception) {
		
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		System.err.println(stackTrace);// You can use LogCat too
		Intent intent = new Intent(myContext, myActivityClass);
		String s = stackTrace.toString();
		// you can use this String to know what caused the exception and in
		// which Activity
		intent.putExtra("uncaughtException",
				"Exception is: " + stackTrace.toString());
		intent.putExtra("stacktrace", s);
		myContext.startActivity(intent);
		// for restarting the Activity
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		// re-throw critical exception further to the os (important)
        defaultUEH.uncaughtException(thread, exception);
	}
}

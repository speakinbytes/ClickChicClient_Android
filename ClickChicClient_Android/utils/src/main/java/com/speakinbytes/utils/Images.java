package com.speakinbytes.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.view.View;

public class Images {

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledBitmapFromFile(String filePath,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

    /*
		 * Making image in circular shape
		 */
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {

        int targetWidth = 70;
        int targetHeight = 70;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(
                ((float) targetWidth) / 2,
                ((float) targetHeight) / 2,
                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
                Path.Direction.CW);
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        // paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawOval(new RectF(0, 0, targetWidth, targetHeight), paint);
        // paint.setColor(Color.TRANSPARENT);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(
                sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap
                        .getHeight()), new RectF(0, 0, targetWidth,
                        targetHeight), paint);
        return targetBitmap;
    }

	public static Bitmap getRoundedCornerBitmapScaled(Context context,
			Bitmap bitmap, float upperLeft, float upperRight, float lowerRight,
			float lowerLeft) {

		float densityMultiplier = context.getResources().getDisplayMetrics().density;

		// scale incoming bitmap to appropriate px size given arguments and
		// display dpi
		Bitmap output;

		if (bitmap == null) {
			// create empty bitmap for drawing
			output = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
		} else {
			// create empty bitmap for drawing
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
		}

		// get canvas for empty bitmap
		Canvas canvas = new Canvas(output);
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// scale the rounded corners appropriately given dpi
		upperLeft *= densityMultiplier;
		upperRight *= densityMultiplier;
		lowerRight *= densityMultiplier;
		lowerLeft *= densityMultiplier;

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		// fill the canvas with transparency
		canvas.drawARGB(0, 0, 0, 0);

		// draw the rounded corners around the image rect. clockwise, starting
		// in upper left.
		canvas.drawCircle(upperLeft, upperLeft, upperLeft, paint);
		canvas.drawCircle(width - upperRight, upperRight, upperRight, paint);
		canvas.drawCircle(width - lowerRight, height - lowerRight, lowerRight,
				paint);
		canvas.drawCircle(lowerLeft, height - lowerLeft, lowerLeft, paint);

		// fill in all the gaps between circles. clockwise, starting at top.
		RectF rectT = new RectF(upperLeft, 0, width - upperRight, height / 2);
		RectF rectR = new RectF(width / 2, upperRight, width, height
				- lowerRight);
		RectF rectB = new RectF(lowerLeft, height / 2, width - lowerRight,
				height);
		RectF rectL = new RectF(0, upperLeft, width / 2, height - lowerLeft);

		canvas.drawRect(rectT, paint);
		canvas.drawRect(rectR, paint);
		canvas.drawRect(rectB, paint);
		canvas.drawRect(rectL, paint);

		// set up the rect for the image
		Rect imageRect = new Rect(0, 0, width, height);

		// set up paint object such that it only paints on Color.WHITE
		// paint.setXfermode(new AvoidXfermode(Color.WHITE, 255,
		// AvoidXfermode.Mode.TARGET));
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		// draw resized bitmap onto imageRect in canvas, using paint as
		// configured above
		try {
			canvas.drawBitmap(bitmap, imageRect, imageRect, paint);
		} catch (Exception e) {

		}
		return output;
	}
	
}

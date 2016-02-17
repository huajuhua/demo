package com.ark.newark_phone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

public class BackgroundHelper extends View {
	public BackgroundHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public StateListDrawable setbg(Integer resId) {
		Integer[] mButtonState = { resId };
		return setbg(mButtonState);
	}

	public StateListDrawable setbg(Integer normalResId, Integer selectedResId) {
		Integer[] mButtonState = { normalResId, selectedResId };
		return setbg(mButtonState);
	}

	public StateListDrawable setbg(Integer normalResId, Integer selectedResId,
			Integer pressedResId) {
		Integer[] mButtonState = { normalResId, selectedResId, pressedResId };
		return setbg(mButtonState);
	}

	// 以下这个方法也可以把你的图片数组传过来，以StateListDrawable来设置图片状态，来表现button的各中状态。未选
	// 中，按下，选中效果。
	private StateListDrawable setbg(Integer[] mImageIds) {

		StateListDrawable bg = new StateListDrawable();
		if (mImageIds == null)
			return bg;

		Drawable normal = this.getResources().getDrawable(mImageIds[0]);
		Drawable selected = null;
		Drawable pressed = null;

		int count = mImageIds.length;
		if (count == 1) {
			Bitmap bitmap = ((BitmapDrawable) normal).getBitmap();
			selected = pressed = getDrawableFocused(bitmap);

		} else if (count == 2) {
			selected = pressed = this.getResources().getDrawable(mImageIds[1]);
		} else if (count == 3) {
			selected = this.getResources().getDrawable(mImageIds[1]);
			pressed = this.getResources().getDrawable(mImageIds[2]);
		}

		bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
		bg.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
		bg.addState(View.ENABLED_STATE_SET, normal);
		bg.addState(View.FOCUSED_STATE_SET, selected);
		bg.addState(View.EMPTY_STATE_SET, normal);
		return bg;
	}

	private Drawable getDrawableFocused(Bitmap bitmap) {
		Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.ARGB_8888);
		int brightness = -45;
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

		Canvas canvas = new Canvas(bmp);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		Drawable drawable = new BitmapDrawable(bmp);
		return drawable;
	}

	public static void setBackgroundEffect(View view, Integer normalResId,
			Integer selectedResId, Integer pressedResId) {
		BackgroundHelper helper = new BackgroundHelper(view.getContext());
		view.setBackgroundDrawable(helper.setbg(normalResId, selectedResId,
				pressedResId));
		helper = null;
	}

	public static void setBackgroundEffect(View view, Integer normalResId,
			Integer selectedResId) {
		BackgroundHelper helper = new BackgroundHelper(view.getContext());
		view.setBackgroundDrawable(helper.setbg(normalResId, selectedResId));
		helper = null;
	}

	public static void setBackgroundEffect(View view, Integer normalResId) {
		BackgroundHelper helper = new BackgroundHelper(view.getContext());
		view.setBackgroundDrawable(helper.setbg(normalResId));
		helper = null;
	}
}

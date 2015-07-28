package com.demo.core;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codescan.MipcaActivityCapture;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.OutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ScanCodeActivity extends MipcaActivityCapture {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/**
	 * 处理扫描结果
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		try{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(ScanCodeActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			//bundle.putParcelable("bitmap", barcode);
			resultIntent.putExtras(bundle);
			//使用下面 的会自动返回到调用它的界面
			this.setResult(RESULT_OK, resultIntent);
		}
		ScanCodeActivity.this.finish();
		}catch(Exception e){
			Log.v("saomiao",e.getMessage());
		}
	}
	 /** 
     * 生成RQ二维码 
     *  
     * @author wuhongbo 
     * @param str 
     *            内容 
     * @param height 
     *            高度（px） 
     *  
     */  
    /** 
     * 编码 
     * @param contents 
     * @param width 
     * @param height 
     * @param imgPath 
     */  
   public Bitmap createCode(String str,int widthAndHeight) { 
		Bitmap qrCodeBitmap = null;
		try {
			qrCodeBitmap = EncodingHandler.createQRCode(str,widthAndHeight);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qrCodeBitmap;
}
}
final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	
	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}


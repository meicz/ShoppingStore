package com.shoppingstore.app.internal;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.shoppingstore.app.exception.BusException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.util.Log;

/**
 * 访问网络工具类
 * @author meicunzhi
 * @date 2015-10-18 下午5:18:14
 */
public abstract class WebUtils {
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";
	private static final String METHOD_PUT = "PUT";
	private static final String METHOD_DELETE = "DELETE";
	
	private static int CONNECTTIMEOUT = 6000;//6秒
	private static int READTIMEOUT = 15000;//15秒
	
	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params) {
		return doPost(url, params, DEFAULT_CHARSET, CONNECTTIMEOUT, READTIMEOUT);
	}
	
	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout) {
		return doPost(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout,
			int readTimeout) {
		String charsetype = "application/x-www-form-urlencoded;charset=" + charset;
		String content; 
		String result = "";
		content = buildQuery(params, charset);
		result = _doPost(url, charsetype, charset, content, connectTimeout, readTimeout);
		
		return result;
	}
	
	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param ctype 请求类型
	 * @param content 请求字符串
	 * @return 响应字符串
	 * @throws IOException
	 */
	private static String _doPost(String url, String charsetype, String charset, String content, 
			int connectTimeout, int readTimeout) { 
		HttpURLConnection http = null;
		String result = "";
		try {
			URL ur1l;
			ur1l = new URL(url);
			http = (HttpURLConnection) ur1l.openConnection(); 
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setUseCaches(false);
			http.setConnectTimeout(connectTimeout);
			http.setReadTimeout(readTimeout);
			http.setRequestMethod(METHOD_POST);
			http.setRequestProperty("Content-Type", charsetype);
			http.connect(); 
			OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), charset);
			osw.write(content);
			osw.flush();
			osw.close(); 
			if (http.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), charset));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}
				in.close();
			}
			else{
				throw new BusException(String.valueOf(http.getResponseCode()), "网络连接请求错误");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new BusException(e); 
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			throw new BusException("01", "网络连接超时"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			if(e instanceof BusException){
        		BusException busEx = (BusException) e;
        		throw busEx;
        	} 
			else{
				throw new BusException(e); 
			} 
		} finally {
			if (http != null) http.disconnect(); 
		} 
		 
		return result;
	}
	
	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params) {
		return doGet(url, params, DEFAULT_CHARSET, CONNECTTIMEOUT, READTIMEOUT);
	}
	
	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params, int connectTimeout, int readTimeout) {
		return doGet(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP GET请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doGet(String url, Map<String, String> params, String charset, int connectTimeout,
			int readTimeout){
		String charsetype = "application/x-www-form-urlencoded;charset=" + charset;
		String content;
		String result = "";
		content = buildQuery(params, charset);
		result = _doGet(url, charsetype, charset, content, connectTimeout, readTimeout);
		
		return result;
	}
	
	/**
	 * 执行HTTP POST请求。
	 * 
	 * @param url 请求地址
	 * @param ctype 请求类型
	 * @param content 请求字符串
	 * @return 响应字符串
	 * @throws IOException
	 */
	private static String _doGet(String url, String charsetype, String charset, String content, 
			int connectTimeout, int readTimeout) { 
		HttpURLConnection http = null;
		String result = "";
		try {
			URL ur1l;
			ur1l = new URL(url + "?" + content);
			http = (HttpURLConnection) ur1l.openConnection(); 
			http.setDoInput(true); 
			http.setUseCaches(false);
			http.setConnectTimeout(connectTimeout);
			http.setReadTimeout(readTimeout);
			http.setRequestMethod(METHOD_GET);
			http.setRequestProperty("Content-Type", charsetype); 
			http.connect();  
			if (http.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), charset));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}
				in.close();
			}
			else{
				throw new BusException(String.valueOf(http.getResponseCode()), "网络连接请求错误");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw new BusException(e); 
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw new BusException("01", "网络连接超时"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			if(e instanceof BusException){
        		BusException busEx = (BusException) e;
        		throw busEx;
        	} 
			else{
				throw new BusException(e); 
			} 
		} finally {
			if (http != null) http.disconnect(); 
		} 
		 
		return result;
	} 
	
	/**
	 * 执行HTTP PUT请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPut(String url, Map<String, String> params) {
		return doPut(url, params, DEFAULT_CHARSET, CONNECTTIMEOUT, READTIMEOUT);
	}
	
	/**
	 * 执行HTTP PUT请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPut(String url, Map<String, String> params, int connectTimeout, int readTimeout) {
		return doPut(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP PUT请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doPut(String url, Map<String, String> params, String charset, int connectTimeout,
			int readTimeout) {
		String charsetype = "application/x-www-form-urlencoded;charset=" + charset;
		String content; 
		String result = "";
		content = buildQuery(params, charset);
		result = _doPut(url, charsetype, charset, content, connectTimeout, readTimeout);  
		
		return result;
	}
	
	/**
	 * 执行HTTP PUT请求。
	 * 
	 * @param url 请求地址
	 * @param ctype 请求类型
	 * @param content 请求字符串
	 * @return 响应字符串
	 * @throws IOException
	 */
	private static String _doPut(String url, String charsetype, String charset, String content, 
			int connectTimeout, int readTimeout) { 
		HttpURLConnection http = null;
		String result = "";
		try {
			URL ur1l;
			ur1l = new URL(url);
			http = (HttpURLConnection) ur1l.openConnection(); 
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setUseCaches(false);
			http.setConnectTimeout(connectTimeout);
			http.setReadTimeout(readTimeout);
			http.setRequestMethod(METHOD_PUT);
			http.setRequestProperty("Content-Type", charsetype);
			http.connect(); 
			OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), charset);
			osw.write(content);
			osw.flush();
			osw.close(); 
			if (http.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), charset));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}
				in.close();
			}
			else{
				throw new BusException(String.valueOf(http.getResponseCode()), "网络连接请求错误");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusException(e); 
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusException("01", "网络连接超时"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e instanceof BusException){
        		BusException busEx = (BusException) e;
        		throw busEx;
        	} 
			else{
				throw new BusException(e); 
			} 
		} finally {
			if (http != null) http.disconnect(); 
		}  
		 
		return result;
	}
	
	/**
	 * 执行HTTP DELETE请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doDelete(String url, Map<String, String> params) {
		return doDelete(url, params, DEFAULT_CHARSET, CONNECTTIMEOUT, READTIMEOUT);
	}
	
	/**
	 * 执行HTTP DELETE请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doDelete(String url, Map<String, String> params, int connectTimeout, int readTimeout) {
		return doDelete(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	/**
	 * 执行HTTP DELETE请求。
	 * 
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 字符集，如UTF-8, GBK, GB2312
	 * @return 响应字符串
	 * @throws IOException
	 */
	public static String doDelete(String url, Map<String, String> params, String charset, int connectTimeout,
			int readTimeout) {
		String charsetype = "application/x-www-form-urlencoded;charset=" + charset;
		String content; 
		String result = "";
		content = buildQuery(params, charset);
		result = _doDelete(url, charsetype, charset, content, connectTimeout, readTimeout);  
		
		return result;
	}
	
	/**
	 * 执行HTTP DELETE请求。
	 * 
	 * @param url 请求地址
	 * @param ctype 请求类型
	 * @param content 请求字符串
	 * @return 响应字符串
	 * @throws IOException
	 */
	private static String _doDelete(String url, String charsetype, String charset, String content, 
			int connectTimeout, int readTimeout) { 
		HttpURLConnection http = null;
		String result = "";
		try {
			URL ur1l;
			ur1l = new URL(url + "?" + content);
			http = (HttpURLConnection) ur1l.openConnection(); 
			http.setDoInput(true); 
			http.setUseCaches(false);
			http.setConnectTimeout(connectTimeout);
			http.setReadTimeout(readTimeout);
			http.setRequestMethod(METHOD_DELETE);
			http.setRequestProperty("Content-Type", charsetype); 
			http.connect();  
			if (http.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), charset));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					result += inputLine;
				}
				in.close();
			}
			else{
				throw new BusException(String.valueOf(http.getResponseCode()), "网络连接请求错误");
			} 
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusException(e); 
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BusException("01", "网络连接超时"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e instanceof BusException){
        		BusException busEx = (BusException) e;
        		throw busEx;
        	} 
			else{
				throw new BusException(e); 
			} 
		} finally {
			if (http != null) http.disconnect(); 
		} 
		 
		return result;
	} 
	
	/**
	 * 获取图片信息，有异常不抛出继续执行
	 * @param url
	 * @return
	 */
	public static Bitmap doGetBitmap(String url) {
		Bitmap bitmap = null;   
		HttpURLConnection http = null;
		String result = "";
		int scale = 1;
		int width = 450;
		int height = 518;
		try {
			URL ur1l;
			ur1l = new URL(url);
			http = (HttpURLConnection) ur1l.openConnection(); 
			http.setDoInput(true); 
			http.setUseCaches(false);
			http.setConnectTimeout(CONNECTTIMEOUT);
			http.setReadTimeout(READTIMEOUT);
			http.setRequestMethod(METHOD_GET);
			http.setRequestProperty("Content-Type", DEFAULT_CHARSET); 
			http.connect();  
			if (http.getResponseCode() == 200) {
				InputStream in = http.getInputStream();    
				byte[] bytes = getBytes(in);
				//设置图片的质量
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
				options.inSampleSize = 1;//computeSampleSize(options, -1, width * height); 
				options.inPurgeable = true;  //
				options.inInputShareable = true;  //
				options.inDither = false;  
				options.inPurgeable = true;  
				options.inJustDecodeBounds = false;  
				bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
				//bitmap = BitmapFactory.decodeStream(in); 
				in.close();
			}
			/*else{
				throw new BusException(String.valueOf(http.getResponseCode()), "网络连接请求错误");
			}*/
		} catch(OutOfMemoryError e){
			e.printStackTrace();			
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new BusException(e); 
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new BusException("01", "网络连接超时"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			/*if(e instanceof BusException){
				BusException busEx = (BusException) e;
				busEx.showException();  
				throw busEx;
			} 
			else{ 
				BusException busEx = new BusException(e);
				busEx.showException();  
				throw busEx;
			}*/
		} finally {
			if (http != null) http.disconnect(); 
		} 
		
		return bitmap;
	}
	
	public static Drawable loadImageFromNetwork(String imageUrl)
	{
	 Drawable drawable = null;
	 try {
	  // 可以在这里通过文件名来判断，是否本地有此图片
	  drawable = Drawable.createFromStream(
	    new URL(imageUrl).openStream(), "image.jpg");
	 } catch (IOException e) {
	  Log.d("test", e.getMessage());
	 }
	 if (drawable == null) {
	  Log.d("test", "null drawable");
	 } else {
	  Log.d("test", "not null drawable");
	 }
	 
	 return drawable ;
	}
	  
	private static String buildQuery(Map<String, String> params, String charset) {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;
		query.append("&");
		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (name != null && name.length() > 0 && value != null) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				try {
					query.append(name).append("=").append(URLEncoder.encode(value, charset));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return query.toString();
	}
	
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);
	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }
	    return roundedSize;
	}
	
	private static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;
	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 :
	            (int) Math.min(Math.floor(w / minSideLength),
	            Math.floor(h / minSideLength));
	    if (upperBound < lowerBound) {
	        return lowerBound;
	    }
	    if ((maxNumOfPixels == -1) &&
	            (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}    
	
	private static byte[] getBytes(InputStream is) {  
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] b = new byte[2048]; 
		int len = 0; 
		try { 
			while ((len = is.read(b, 0, 2048)) != -1) { 
				baos.write(b, 0, len); 
				baos.flush(); 
			} 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		byte[] bytes = baos.toByteArray(); 
		return bytes; 
	} 
}

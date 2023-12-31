package com.hotent.ueditor.define;

import java.util.HashMap;
import java.util.Map;

public class FileType {

	public static final String JPG = "JPG";
	public static final String JPEG = "JPEG";
	public static final String BMP = "BMP";
	public static final String PNG = "PNG";
	public static final String GIF = "GIF";
	public static final String MP4 = "MP4";
	
	private static final Map<String, String> types = new HashMap<String, String>(){
		private static final long serialVersionUID = -935543810424292061L;

	{
		
		put( FileType.JPG, ".jpg" );
		put( FileType.JPEG, ".jpeg" );
		put( FileType.BMP, ".bmp" );
		put( FileType.PNG, ".png" );
		put( FileType.GIF, ".gif" );
		put( FileType.MP4, ".mp4" );
	}};
	
	public static String getSuffix ( String key ) {
		return FileType.types.get( key );
	}
	
	/**
	 * 根据给定的文件名,获取其后缀信息
	 * @param filename
	 * @return
	 */
	public static String getSuffixByFilename ( String filename ) {
		filename = filename.substring( filename.lastIndexOf( "." ) ).toLowerCase();
		if(!types.containsValue(filename.toLowerCase()))
			return types.get(FileType.JPG);
		return filename;
		
	}
	
}

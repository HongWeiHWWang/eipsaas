package com.hotent.ueditor.hunter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hotent.base.util.AppUtil;
import com.hotent.ueditor.PathFormat;
import com.hotent.ueditor.define.AppInfo;
import com.hotent.ueditor.define.BaseState;
import com.hotent.ueditor.define.MIMEType;
import com.hotent.ueditor.define.MultiState;
import com.hotent.ueditor.define.State;
import com.hotent.ueditor.upload.StorageService;

/**
 * 图片抓取器
 * @author hancong03@baidu.com
 *
 */
public class ImageHunter {
	@Autowired
	private StorageService storageService;
	@SuppressWarnings("unused")
	private String filename = null;
	@SuppressWarnings("unused")
	private String savePath = null;
	@SuppressWarnings("unused")
	private String rootPath = null;
	private List<String> allowTypes = null;
	private long maxSize = -1;
	
	private List<String> filters = null;
	
	public ImageHunter ( Map<String, Object> conf ) {
		
		this.filename = (String)conf.get( "filename" );
		this.savePath = (String)conf.get( "savePath" );
		this.rootPath = (String)conf.get( "rootPath" );
		this.maxSize = (Long)conf.get( "maxSize" );
		this.allowTypes = Arrays.asList( (String[])conf.get( "allowFiles" ) );
		this.filters = Arrays.asList( (String[])conf.get( "filter" ) );
		
	}
	
	public State capture ( String[] list ) {
		
		MultiState state = new MultiState( true );
		
		for ( String source : list ) {
			state.addState( captureRemoteData( source ) );
		}
		
		return state;
		
	}

	public State captureRemoteData ( String urlStr ) {
		
		HttpURLConnection connection = null;
		URL url = null;
		String suffix = null;
		
		try {
			url = new URL( urlStr );

			if ( !validHost( url.getHost() ) ) {
				return new BaseState( false, AppInfo.PREVENT_HOST );
			}
			
			connection = (HttpURLConnection) url.openConnection();
		
			connection.setInstanceFollowRedirects( true );
			connection.setUseCaches( true );
		
			if ( !validContentState( connection.getResponseCode() ) ) {
				return new BaseState( false, AppInfo.CONNECTION_ERROR );
			}
			
			suffix = MIMEType.getSuffix( connection.getContentType() );
			
			if ( !validFileType( suffix ) ) {
				return new BaseState( false, AppInfo.NOT_ALLOW_FILE_TYPE );
			}
			
			if ( !validFileSize( connection.getContentLength() ) ) {
				return new BaseState( false, AppInfo.MAX_SIZE );
			}
			
			if(storageService==null) {
				storageService = AppUtil.getBean(StorageService.class);
			}
			State state = storageService.saveFileByInputStream( connection.getInputStream(), urlStr );
			
			if ( state.isSuccess() ) {
				state.putInfo( "source", urlStr );
			}
			
			return state;
			
		} catch ( Exception e ) {
			return new BaseState( false, AppInfo.REMOTE_FAIL );
		}
		
	}
	
	@SuppressWarnings("unused")
	private String getPath ( String savePath, String filename, String suffix  ) {
		
		return PathFormat.parse( savePath + suffix, filename );
		
	}
	
	private boolean validHost ( String hostname ) {
		
		return !filters.contains( hostname );
		
	}
	
	private boolean validContentState ( int code ) {
		
		return HttpURLConnection.HTTP_OK == code;
		
	}
	
	private boolean validFileType ( String type ) {
		
		return this.allowTypes.contains( type );
		
	}
	
	private boolean validFileSize ( int size ) {
		return size < this.maxSize;
	}
	
}

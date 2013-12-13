package com.smilingliuwei.configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Set;

/**
 * 用于读取properties配置文件
 * properties文件内可以含有中文
 * @author wei
 *
 */
public final class Configuration {
	
	private static String FILE_SUFFIX = ".compile";
	
	private static String ERROR_PREFIX = "\n[ERROR] >> ";
	
	private String fileCharset = "UTF-8";

	private String fileRelativePath = "";
	
	private Properties conf = new Properties();
	

	public Configuration( String fileRelativePath ) {
		this.init(fileRelativePath, this.fileCharset);
	}
	
	public Configuration( String fileRelativePath, String charset ) {
		this.init(fileRelativePath, charset);
	}

	private void init( String fileRelativePath, String charset ){
		
		this.fileRelativePath= fileRelativePath;
		this.fileCharset = charset;
		InputStream inStream = null;
		File srcFile = null;
		File dstFile = null;

		try {
			srcFile = new File( new URI( ClassLoader.getSystemResource("").toString() + this.fileRelativePath ) );
			dstFile = new File( new URI( ClassLoader.getSystemResource("").toString() + this.fileRelativePath + Configuration.FILE_SUFFIX ) );

			Native2Ascii.nativeToAscii(
					srcFile,
					dstFile,
					this.fileCharset );

			inStream = new FileInputStream( dstFile );
			this.conf.load( inStream );
		}
		catch( FileNotFoundException e ) {
			System.out.println( Configuration.ERROR_PREFIX + e.getMessage() );
		}
		catch( IOException e ) {
			System.out.println( Configuration.ERROR_PREFIX + e.getMessage() );
		}
		catch( URISyntaxException e ) {
			System.out.println( Configuration.ERROR_PREFIX + e.getMessage() );
		}
		finally {
			Native2Ascii.closeQuietly( inStream );
		}
	}
	
	public String getProperty( String key ) {
		return this.conf.getProperty( key );
	}
	
	public Set<String> getkeySet() {
		return this.conf.stringPropertyNames();
	}
	
	public Properties getProperties() {
		return this.conf;
	}
	
}

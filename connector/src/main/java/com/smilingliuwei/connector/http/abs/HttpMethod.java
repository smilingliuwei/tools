package com.smilingliuwei.connector.http.abs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.smilingliuwei.connector.common.Conf;
import com.smilingliuwei.connector.common.Conf.PRINT;
import com.smilingliuwei.connector.exception.BuildArgumentsException;
import com.smilingliuwei.connector.exception.BuildHeaderException;
import com.smilingliuwei.connector.http.HttpConnection;

public abstract class HttpMethod implements HttpConnection {
	
	private String charset = "UTF-8";

	private char separator = '&';              // 参数之间的分隔符
	
	private char hyphen = '=';                 // Key<hyphen>value
	
	private String linebreak = "\r\n";         // 构建http请求头需要
	
	private Map<String, String> headerMap;     // 存储http请求头
	
	private Map<String, String> argumentsMap;  // 存储http请求参数
	
	private String argumentsData;              // 存储非键值对的请求参数，比如JSON
	
	private SocketChannel channel;
	
	private SocketAddress address;

	
	public void appendArgument(String key, String value) {

	    if( null == this.argumentsMap ) {
	        this.argumentsMap = new HashMap<String, String>();
	    }
	    
	    this.argumentsMap.put( key, value );
	}
	
	public void appendHeader(String key, String value) {
		
	    if( null == this.headerMap ) {
	        this.headerMap = new HashMap<String, String>();
	    }

	    this.headerMap.put( key, value );
	}

	public void buildArguments(Map<String, String> argumentsMap) {
	    
	    if( null == argumentsMap ) {
	        return;
	    }
	    
	    this.argumentsMap.putAll( argumentsMap );
	}
	
	public void buildArgumentString(String argumentsData ) {

		this.argumentsData = argumentsData;

	}
	
	public void buildHeader(Map<String, String> headerMap) {
		
		if( (null == headerMap) || headerMap.isEmpty() ) {
		    return;
		}
		if( null == this.headerMap ) {
		    this.headerMap = new HashMap<String, String>();
		}
		
		this.headerMap.putAll( headerMap );
	}
	
	public void connect( String remote ) throws IOException {
	    
	    this.connect(remote, 80);
	}
	
	public void connect( String remote, int port ) throws IOException {
		
		this.address = new InetSocketAddress( remote, port );
		this.channel = SocketChannel.open();
		this.channel.configureBlocking( false );
		this.channel.connect( this.address );
		
		this.appendHeader( "Host", remote );

		for( System.out.println( Conf.getProperty( PRINT.INFO_PREFIX ) + "开始连接 " + remote );
		        true; ) {
			System.out.println( Conf.getProperty( PRINT.CHILD1_PREFIX ) + ".....");

			// 只有非阻塞模式可以用Channel.finishConnect()检测是否已连接了此通道
			if( this.channel.finishConnect() ) {
				System.out.println( Conf.getProperty( PRINT.INFO_PREFIX ) + "连接成功" );
				break;
			}
			else {
				try { Thread.sleep( 20 ); }
				catch( InterruptedException e ) { }
			}
		}
		
	}
	
	public void disconnect() throws IOException {
		
		if( this.channel.isOpen() ) {
			this.channel.close();
		}
		
	}
	
	public boolean isConnected() {

		return this.channel.isConnected();
	}
	
	public String receive() throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate( 1024 * 10 );
		StringBuilder builder = new StringBuilder();
		long timeout = 5000;
		long timeoutStart = 0;
		
		for (boolean startFlag = false; true;) {
			buffer.clear();
			int byteNum = this.channel.read(buffer);

			if (byteNum != 0) {
				startFlag = true;
				timeoutStart = 0;
				if (byteNum == -1) {
					break;
				}
				buffer.flip();
				builder.append(new String(buffer.array(), this.charset));
			} else if ((byteNum == 0) && (startFlag)) {
				timeoutStart = (timeoutStart == 0) ? System.currentTimeMillis()
						: timeoutStart;
				if ((System.currentTimeMillis() - timeoutStart) > timeout) {
					break;
				}
				try { Thread.sleep(500); }
				catch( InterruptedException e ) { }
			}
		}

		return builder.toString();
	}
	
	public void refresh() {
	    // TODO Auto-generated method stub
	    
	}
	
	public void send(String path) throws IOException {

	    ByteBuffer buffer = ByteBuffer.allocate( 1024 * 2 );
	    StringBuilder data = new StringBuilder();
	    
	    data.append( this.buildRequestLine( path ) );
	    data.append( this.linebreak );
	    data.append( this.buildHeaderString() );
	    data.append( this.linebreak + this.linebreak );
	    data.append( this.buildArgumentString() );
	    
	    buffer.put( data.toString().getBytes( this.charset ) );
	    buffer.flip();
	    this.channel.write( buffer );

	    System.out.println( Conf.getProperty( PRINT.INFO_PREFIX ) + "HTTP Header :\n" + data.toString() );
	}
	
	private String buildHeaderString() {
	    
	    if( null == this.headerMap ) {
	        throw new BuildHeaderException( "没有设置HTTP请求头" );
	    }
	    
	    String temp = this.headerMap.toString();
	    temp = temp.replaceAll( ", ", this.linebreak );
	    temp = temp.replaceAll( "=", ": " );
	    temp = temp.replace( '{', ' ' );
	    temp = temp.replace( '}', ' ' );
	    temp = temp.trim();

	    return temp;
	}
	
	private String buildArgumentString() {
	    
	    if( ( null != this.argumentsMap ) && ( null != this.argumentsData ) ) {
	        throw new BuildArgumentsException(
	                "HttpMethod.buildArguments(),HttpMethod.buildArgumentString() 只能使用其中一个" );
	    }

	    if( null != this.argumentsMap ) {
	        String temp = this.argumentsMap.toString();
	        temp = temp.replace( ',', this.separator );
	        temp = temp.replace( '=', this.hyphen );
    	    temp = temp.replace( '{', ' ' );
    	    temp = temp.replace( '}', ' ' );
    	    temp = temp.trim();

	        return temp;
	    }
	    
	    return ( null == this.argumentsData ) ? "" : this.argumentsData.toString();
	}
	
	/**
	 * Http请求原型：
	 * < request line >
	 * < http header >
	 * < blank line >
	 * < request body >
	 */
	protected abstract String buildRequestLine( String path );
	

	/* ========================== Getter && Setter =========================== */
	
	public void setSeparator( char separator ) {
		this.separator = separator;
	}
	
	public void setHyphen( char hyphen ) {
		this.hyphen = hyphen;
	}
	
	public String getLineBreak() {
	    return this.linebreak;
	}

}

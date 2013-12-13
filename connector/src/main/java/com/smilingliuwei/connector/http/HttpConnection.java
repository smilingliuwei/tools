package com.smilingliuwei.connector.http;

import java.io.IOException;
import java.util.Map;

import com.smilingliuwei.connector.Connection;

public interface HttpConnection extends Connection {
	
	/**
	 * 添加HTTP请求参数 one by one,键值对
	 * @param key
	 * @param value
	 */
	public abstract void appendArgument( String key, String value );
	
	/**
	 * 添加HTTP请求头 one by one，键值对
	 * @param key
	 * @param value
	 */
	public abstract void appendHeader( String key, String value );
	
	/**
	 * 构建Http请求参数，键值对
	 * @param argumentMap
	 */
	public abstract void buildArguments( Map<String, String> argumentsMap );
	
	/**
	 * 构建Http请求参数，JSON
	 * @param argumentString
	 */
	public abstract void buildArgumentString( String argumentsData );
	
	/**
	 * 构建Http请求头
	 * @param headerMap
	 */
	public abstract void buildHeader( Map<String, String> headerMap );
	
	/**
	 * 默认使用80端口
	 * @param remote
	 */
	public abstract void connect( String remote ) throws IOException;
	
	/**
	 * 重置
	 */
	public abstract void refresh();

    /**
     * 向远端发送信息
     * @throws IOException
     */
    public abstract void send( String path ) throws IOException;

}

package com.smilingliuwei.connector;

import java.io.IOException;

public interface Connection {

	/**
	 * 连接远端
	 * @param remote
	 * @param port
	 * @throws IOException
	 */
	public abstract void connect( String remote, int port ) throws IOException;
	
	/**
	 * 断开与远端的连接
	 * @throws IOException
	 */
	public abstract void disconnect() throws IOException;
	
	/**
	 * 是否已与远端建立连接
	 * @return
	 */
	public abstract boolean isConnected();
	
	/**
	 * 从远端接收信息
	 * @return
	 * @throws IOException
	 */
	public abstract String receive() throws IOException;

}

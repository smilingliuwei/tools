package com.smilingliuwei.connector.exception;

import com.smilingliuwei.connector.common.Conf;
import com.smilingliuwei.connector.common.Conf.PRINT;

public abstract class BaseException extends RuntimeException {

	/**
	 * connections 所有异常的超类
	 */
	private static final long serialVersionUID = -5527740800799163003L;
	
	public BaseException() {
		super( Conf.getProperty( PRINT.ERROR_PREFIX ) + "connections 发生异常\n" );
	}
	
	public BaseException( String msg ) {
		super( Conf.getProperty( PRINT.ERROR_PREFIX ) + msg + "\n" );
	}
	
	public BaseException( Throwable throwable ) {
		super( Conf.getProperty( PRINT.ERROR_PREFIX ) + "connections 发生异常\n", throwable );
	}
	
	public BaseException( String msg, Throwable throwable ) {
		super( Conf.getProperty( PRINT.ERROR_PREFIX ) + msg + "\n", throwable );
	}
	
}

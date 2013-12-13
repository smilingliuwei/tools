package com.smilingliuwei.connector;

import java.io.IOException;

import com.smilingliuwei.connector.http.HttpConnection;
import com.smilingliuwei.connector.http.impl.PostMethod;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws IOException
    {
        assertTrue( true );
        
        HttpConnection request = new PostMethod();
        
        request.connect( "passport.baidu.com" );
        request.appendHeader( "User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)" );
        request.appendHeader( "Connection", "Keep-Alive" );
        request.appendArgument( "login", "" );
        request.send( "/v2/api/" );
        System.out.println( "\n" + request .receive() );
    }
}

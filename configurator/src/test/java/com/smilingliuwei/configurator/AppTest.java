package com.smilingliuwei.configurator;

import java.util.Iterator;
import java.util.Properties;

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
    public void testApp()
    {
        assertTrue( true );
        Configuration configurations = new Configuration(
        		"global.properties",
        		"UTF-8");
		
		Properties properties = configurations.getProperties();

		String test1 = properties.getProperty( "test1" );
		String test2 = properties.getProperty( "test2" );
		
		System.out.println( "\n============\n" + test1 + "\n" + test2 + "\n============\n");
		
		Iterator<String> iterator = configurations.getkeySet().iterator();
		String key = null;
		while( iterator.hasNext() ) {
			key = iterator.next();
			System.out.println( "key: " + key + "\tvalue: " + configurations.getProperty( key ) );
		}
    }
}

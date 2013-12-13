package com.smilingliuwei.configurator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;

/**
 * 为properties文件转码，可以避免因properties文件中含有中文造成的麻烦
 * @author wei
 *
 */
public class Native2Ascii {
	
	public static String backupFile( String fileName )
			throws FileNotFoundException,IOException {

		String suffix = ".backup";
		byte [] buffer = new byte[4096];
		FileInputStream fileIn = new FileInputStream( fileName );
		FileOutputStream fileOut = new FileOutputStream( fileName + suffix );
			
		try{
			int length = 0;
			while( ( length = fileIn.read( buffer ) ) != -1 ) {
				fileOut.write( buffer, 0, length );
			}
		}
		finally {
			closeQuietly( fileIn );
			closeQuietly( fileOut );
		}
		
		return fileName + suffix;
	}

	public static String nativeToAscii(CharSequence cs) {

		if (cs == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cs.length(); i++) {
			char c = cs.charAt(i);
			if (c <= '~') {
				sb.append(c);
			} else {
				sb.append("\\u");
				String hex = Integer.toHexString(c);
				for (int j = hex.length(); j < 4; j++) {
					sb.append('0');
				}
				sb.append(hex);
			}
		}

		return sb.toString();
	}

	public static void nativeToAscii(File src, File dst, String encoding)
			throws IOException {

		BufferedReader input = null;
		BufferedWriter output = null;

		try {
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(src), encoding));
			output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(dst), "US-ASCII"));

			char[] buffer = new char[4096];
			int len;
			while ((len = input.read(buffer)) != -1) {
				output.write(nativeToAscii(CharBuffer.wrap(buffer, 0, len)));
			}
		}
		finally {
			closeQuietly(input);
			closeQuietly(output);
		}
	}

	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			}
			catch (IOException e) { }
		}
	}
}

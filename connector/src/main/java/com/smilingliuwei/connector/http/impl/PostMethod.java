package com.smilingliuwei.connector.http.impl;

import com.smilingliuwei.connector.http.abs.HttpMethod;

public class PostMethod extends HttpMethod {

    protected String buildRequestLine(String path) {
        
        path = path.isEmpty() ? "/" : path;
        String requestLine = "POST " + path + " HTTP/1.1";
        this.appendHeader( "Content-Type", "application/x-www-form-urlencoded" );

        return requestLine;
    }

}

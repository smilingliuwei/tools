package com.smilingliuwei.connector.http.impl;

import com.smilingliuwei.connector.http.abs.HttpMethod;

public class GetMethod extends HttpMethod {

    protected String buildRequestLine( String path ) {
        
        path = path.isEmpty() ? "/" : path;

        return "GET " + path + " HTTP/1.1";
    }

}

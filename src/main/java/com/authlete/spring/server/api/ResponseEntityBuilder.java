/*
 * Copyright (C) 2017 Authlete, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */
package com.authlete.spring.server.api;


import java.util.List;
import java.util.Map;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ResponseEntityBuilder
{
    private int mStatus;
    private HttpHeaders mHeaders;
    private Object mBody;


    public ResponseEntityBuilder()
    {
        mStatus  = HttpStatus.OK.value();
        mHeaders = new HttpHeaders();
    }


    public ResponseEntityBuilder status(int status)
    {
        mStatus = status;

        return this;
    }


    public ResponseEntityBuilder ok()
    {
        return status(HttpStatus.OK.value());
    }


    public ResponseEntityBuilder header(String headerName, String headerValue)
    {
        if (headerName == null)
        {
            return this;
        }

        mHeaders.add(headerName, headerValue);

        return this;
    }


    public ResponseEntityBuilder contentType(String value)
    {
        return header(HttpHeaders.CONTENT_TYPE, value);
    }


    public ResponseEntityBuilder body(Object body)
    {
        mBody = body;

        return this;
    }


    public ResponseEntityBuilder response(Response response)
    {
        if (response == null)
        {
            return this;
        }

        // Replace the HTTP status code.
        mStatus = response.getStatus();

        // Replace the HTTP headers.
        mHeaders = extractHeaders(response);

        // Replace the response body.
        mBody = response.getEntity();

        return this;
    }


    public ResponseEntityBuilder exception(WebApplicationException exception)
    {
        if (exception == null)
        {
            return this;
        }

        return response(exception.getResponse());
    }


    /**
     * Convert HTTP headers of the response into an
     * {@code HttpHeaders} instance.
     */
    private static HttpHeaders extractHeaders(Response response)
    {
        HttpHeaders targetHeaders = new HttpHeaders();
        MultivaluedMap<String, String> sourceHeaders = response.getStringHeaders();

        // If the response does not have any header.
        if (sourceHeaders == null)
        {
            // Return an empty header list.
            return targetHeaders;
        }

        // For each header name
        for (Map.Entry<String, List<String>> entry : sourceHeaders.entrySet())
        {
            // Header name
            String headerName = entry.getKey();

            // Header values
            List<String> headerValues = entry.getValue();

            // If the header name is not valid.
            if (headerName == null || headerName.length() == 0)
            {
                // Ignore the header.
                continue;
            }

            // If header values are not available.
            if (headerValues == null || headerValues.size() == 0)
            {
                // Add the header with a 'null' value.
                targetHeaders.add(headerName, null);
                continue;
            }

            // For each header value
            for (String headerValue : headerValues)
            {
                // Add the pair of the header name and the header value.
                targetHeaders.add(headerName, headerValue);
            }
        }

        return targetHeaders;
    }


    public ResponseEntity<?> build()
    {
        return ResponseEntity.status(mStatus).headers(mHeaders).body(mBody);
    }
}

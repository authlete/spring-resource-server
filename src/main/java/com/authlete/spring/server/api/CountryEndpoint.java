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


import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.ws.rs.WebApplicationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.neovisionaries.i18n.CountryCode;


/**
 * An endpoint that returns country code information.
 *
 * <p>
 * The API path is <code>"/api/country/<i>{countryCode}</i>"</code> where
 * <code><i>{countryCode}</i></code> is an ISO 3166-1 alpha-2, alpha-3
 * or numeric code (case-insensitive). For example, {@code JP},
 * {@code JPN} and {@code 392}.
 * </p>
 *
 * <p>
 * The response is a JSON that contains the following.
 * </p>
 *
 * <blockquote>
 * <ol>
 *   <li>Country name
 *   <li>ISO 3166-1 alpha-2 code
 *   <li>ISO 3166-1 alpha-3 code
 *   <li>ISO 3166-1 numeric code
 *   <li>Currency
 * </ol>
 * </blockquote>
 *
 * <p>
 * Below is an example response from this API.
 * </p>
 *
 * <blockquote>
 * <pre>
 * {
 *   "name": "Japan",
 *   "alpha2": "JP",
 *   "alpha3": "JPN",
 *   "numeric": 392,
 *   "currency": "JPY"
 * }
 * </pre>
 * </blockquote>
 *
 * <p>
 * This endpoint is protected by OAuth 2.0 access tokens as
 * defined in <a href="https://tools.ietf.org/html/rfc6750"
 * >RFC 6750</a> (The OAuth 2.0 Authorization Framework:
 * Bearer Token Usage).
 * </p>
 */
@RestController
@RequestMapping("/api/country/{countryCode}")
public class CountryEndpoint extends SpringResourceEndpoint
{
    /**
     * GET /api/country/{countryCode}
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object get(
            // HTTP header: Authorization
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorization,

            // Query parameter: access_token
            @RequestParam(value = "access_token", required = false)
            String accessToken,

            // Path parameter: countryCode
            @PathVariable("countryCode")
            String countryCode
    )
    {
        // Extract an access token from either the Authorization header or
        // the request parameters. The Authorization header takes precedence.
        // See RFC 6750 (Bearer Token Usage) about the standard ways to accept
        // an access token from a client application.
        String token = extractAccessToken(authorization, accessToken);

        // Validate the access token, collect information about the country
        // identified by the country code, and return the information.
        return process(token, countryCode);
    }


    /**
     * POST /api/country/{countryCode}
     */
    @RequestMapping(
            method   = RequestMethod.POST,
            consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ResponseEntity<?> post(
            // HTTP header: Authorization
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorization,

            // Form parameter: access_token
            @RequestParam(value = "access_token", required = false)
            String accessToken,

            // Path parameter: countryCode
            @PathVariable("countryCode") String countryCode
    )
    {
        // Extract an access token from either the Authorization header or
        // the request parameters. The Authorization header takes precedence.
        // See RFC 6750 (Bearer Token Usage) about the standard ways to accept
        // an access token from a client application.
        String token = extractAccessToken(authorization, accessToken);

        // Validate the access token, collect information about the country
        // identified by the country code, and return the information.
        return process(token, countryCode);
    }


    private ResponseEntity<?> process(String accessToken, String countryCode)
    {
        // Validate the access token. Because this endpoint does not require
        // any scopes, here we use the simplest variant of validateAccessToken()
        // methods which does not take 'requiredScopes' argument. See the JavaDoc
        // of BaseResourceEndpoint (authlete-java-jaxrs) for details.
        //
        // validateAccessToken() throws a WebApplicationException when the
        // given access token is invalid. The response contained in the
        // exception complies with RFC 6750, so you don't have to build the
        // content of WWW-Authenticate header in the error response by yourself.
        //
        // If you want to get information about the access token (e.g. the
        // subject of the user and the scopes associated with the access
        // token), use the object returned from validateAccessToken() method.
        // It is an instance of AccessTokenInfo class. If you want to get
        // information even in the case where validateAccessToken() throws
        // an exception, call AuthleteApi.introspect(IntrospectionRequest)
        // directly.
        try
        {
            // Validate the access token.
            validateAccessToken(accessToken);
        }
        catch (WebApplicationException e)
        {
            // Convert the exception into a ResponseEntity instance.
            return toResponseEntity(e);
        }

        // If validateAccessToken() did not throw an exception, the access
        // token presented by the client application is valid.

        // Build the content.
        Object content = buildContent(countryCode);

        // Build the response.
        return new ResponseEntityBuilder()
                .ok()
                .contentType("application/json;charset=UTF-8")
                .body(content)
                .build();
    }


    private Map<String, Object> buildContent(String countryCode)
    {
        // Look up a CountryCode instance that has the ISO 3166-1 code.
        CountryCode cc = lookup(countryCode);

        Map<String, Object> data = new LinkedHashMap<String, Object>();

        if (cc != null)
        {
            // Pack the data into a Map.
            data.put("name",     cc.getName());
            data.put("alpha2",   cc.getAlpha2());
            data.put("alpha3",   cc.getAlpha3());
            data.put("numeric",  cc.getNumeric());
            data.put("currency", cc.getCurrency());
        }

        return data;
    }


    /**
     * Look up a {@link CountryCode} instance from an ISO 3166-1 code.
     *
     * @param countryCode
     *         ISO 3166-1 code (alpha-2, alpha-3, or numeric).
     *
     * @return
     *         A {@link CountryCode} instance that corresponds to the
     *         given code. If the given code is not valid, {@code null}
     *         is returned.
     */
    private CountryCode lookup(String countryCode)
    {
        if (countryCode == null)
        {
            // Not found.
            return null;
        }

        // Interpret the code as an ISO 3166-1 alpha-2 or alpha-3 code.
        CountryCode cc = CountryCode.getByCodeIgnoreCase(countryCode);

        if (cc != null)
        {
            // Found.
            return cc;
        }

        try
        {
            // Interpret the code as an ISO 3166-1 numeric code.
            return CountryCode.getByCode(Integer.parseInt(countryCode));
        }
        catch (NumberFormatException e)
        {
            // Not found.
            return null;
        }
    }
}

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


import com.authlete.jakarta.AccessTokenInfo;
import com.authlete.jakarta.BaseResourceEndpoint;
import jakarta.ws.rs.WebApplicationException;
import org.springframework.http.ResponseEntity;
import com.authlete.common.api.AuthleteApi;
import com.authlete.common.api.AuthleteApiFactory;


public class SpringResourceEndpoint extends BaseResourceEndpoint
{
    /**
     * Get an instance of the {@link AuthleteApi} interface.
     *
     * <p>
     * Change the implementation of this method or override
     * this method as necessary.
     * </p>
     *
     * @return
     *         An instance of the {@link AuthleteApi} interface.
     */
    protected AuthleteApi getAuthleteApi()
    {
        // Get an implementation of AuthleteApi interface.
        return AuthleteApiFactory.getDefaultApi();
    }


    /**
     * Validate an access token.
     *
     * @param accessToken
     *         An access token to be validated.
     *
     * @return
     *         Information about the access token. If you want to
     *         get more information, call
     *         {@code AuthleteApi.introspect(IntrospectionRequest)}
     *         directly.
     *
     * @throws WebApplicationException
     *         The access token is invalid.
     */
    protected AccessTokenInfo validateAccessToken(String accessToken)
    {
        // Call a method defined in the super class.
        return validateAccessToken(getAuthleteApi(), accessToken);
    }


    /**
     * Validate an access token.
     *
     * @param accessToken
     *         An access token to be validated.
     *
     * @param requiredScopes
     *         Scopes that the access token must have.
     *
     * @return
     *         Information about the access token. If you want to
     *         get more information, call
     *         {@code AuthleteApi.introspect(IntrospectionRequest)}
     *         directly.
     *
     * @throws WebApplicationException
     *         The access token is invalid.
     */
    protected AccessTokenInfo validateAccessToken(
            String accessToken, String[] requiredScopes)
    {
        // Call a method defined in the super class.
        return validateAccessToken(
                getAuthleteApi(), accessToken, requiredScopes);
    }


    /**
     * Validate an access token.
     *
     * @param accessToken
     *         An access token to be validated.
     *
     * @param requiredScopes
     *         Scopes that the access token must have.
     *
     * @param requiredSubject
     *         Subject (unique identifier of a user) that the
     *         access token must be associated with.
     *
     * @return
     *         Information about the access token. If you want to
     *         get more information, call
     *         {@code AuthleteApi.introspect(IntrospectionRequest)}
     *         directly.
     *
     * @throws WebApplicationException
     *         The access token is invalid.
     */
    protected AccessTokenInfo validateAccessToken(
            String accessToken, String[] requiredScopes, String requiredSubject)
    {
        // Call a method defined in the super class.
        return validateAccessToken(
                getAuthleteApi(), accessToken, requiredScopes, requiredSubject);
    }


    /**
     * Convert a {@link WebApplicationException} instance to
     * a {@link ResponseEntity} instance.
     */
    protected ResponseEntity<?> toResponseEntity(WebApplicationException exception)
    {
        return new ResponseEntityBuilder().exception(exception).build();
    }


    @Override
    protected void onError(WebApplicationException exception)
    {
        // Overriding this method to suppress exception.printStackTrace().
    }
}

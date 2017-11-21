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


import javax.ws.rs.core.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.authlete.common.api.AuthleteApiFactory;
import com.authlete.jaxrs.BaseUserInfoEndpoint;


/**
 * An implementation of userinfo endpoint (<a href=
 * "http://openid.net/specs/openid-connect-core-1_0.html#UserInfo"
 * >OpenID Connect Core 1&#x2E;0, 5&#x2E;3&#x2E; UserInfo Endpoint</a>).
 *
 * <p>
 * This endpoint is protected by OAuth 2.0 access tokens as
 * defined in <a href="https://tools.ietf.org/html/rfc6750"
 * >RFC 6750</a> (The OAuth 2.0 Authorization Framework:
 * Bearer Token Usage). Note that access tokens for this
 * endpoint must have the {@code "openid"} scope. This is
 * a requirement by the specification of userinfo endpoint.
 * </p>
 *
 * <p>
 * {@code Content-Type} of responses from this endpoint is
 * either {@code application/json} or {@code application/jwt}.
 * See the specifications for details.
 * </p>
 *
 * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#UserInfo"
 *      >OpenID Connect Core 10, 5.3. UserInfo Endpoint</a>
 */
@RestController
@RequestMapping("/api/userinfo")
public class UserInfoEndpoint extends BaseUserInfoEndpoint
{
    /**
     * The userinfo endpoint for {@code GET} method.
     *
     * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#UserInfoRequest"
     *      >OpenID Connect Core 1.0, 5.3.1. UserInfo Request</a>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> get(
            // HTTP header: Authorization
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorization,

            // Query parameter: access_token
            @RequestParam(value = "access_token", required = false)
            String accessToken
    )
    {
        // Handle the userinfo request.
        return handle(extractAccessToken(authorization, accessToken));
    }


    /**
     * The userinfo endpoint for {@code POST} method.
     *
     * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#UserInfoRequest"
     *      >OpenID Connect Core 1.0, 5.3.1. UserInfo Request</a>
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
            String accessToken
    )
    {
        // Handle the userinfo request.
        return handle(extractAccessToken(authorization, accessToken));
    }


    /**
     * Handle the userinfo request.
     */
    private ResponseEntity<?> handle(String accessToken)
    {
        // Handle the userinfo request.
        Response response = handle(AuthleteApiFactory.getDefaultApi(),
                new UserInfoRequestHandlerSpiImpl(), accessToken);

        return new ResponseEntityBuilder().response(response).build();
    }
}

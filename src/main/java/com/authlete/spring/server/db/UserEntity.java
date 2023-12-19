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
package com.authlete.spring.server.db;


import com.authlete.common.dto.Address;
import com.authlete.common.types.StandardClaims;
import com.authlete.common.types.User;


/**
 * Dummy user entity that represents a user record.
 */
public class UserEntity implements User
{
    /**
     * The subject (unique identifier) of the user.
     */
    private String subject;


    /**
     * The name of the user.
     */
    private String name;


    /**
     * The email address of the user.
     */
    private String email;


    /**
     * The postal address of the user.
     */
    private Address address;


    /**
     * The phone number of the user.
     */
    private String phoneNumber;


    /**
     * Constructor with initial values.
     */
    public UserEntity(
            String subject, String name,
            String email, Address address, String phoneNumber)
    {
        this.subject     = subject;
        this.name        = name;
        this.email       = email;
        this.address     = address;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String getSubject()
    {
        return subject;
    }


    @Override
    public Object getClaim(String claimName, String languageTag)
    {
        if (claimName == null)
        {
            return null;
        }

        // See "OpenID Connect Core 1.0, 5. Claims".
        switch (claimName)
        {
            case StandardClaims.NAME:
                // "name" claim. This claim can be requested by including "profile"
                // in "scope" parameter of an authorization request.
                return name;

            case StandardClaims.EMAIL:
                // "email" claim. This claim can be requested by including "email"
                // in "scope" parameter of an authorization request.
                return email;

            case StandardClaims.ADDRESS:
                // "address" claim. This claim can be requested by including "address"
                // in "scope" parameter of an authorization request.
                return address;

            case StandardClaims.PHONE_NUMBER:
                // "phone_number" claim. This claim can be requested by included "phone"
                // in "scope" parameter of an authorization request.
                return phoneNumber;

            default:
                // Unsupported claim.
                return null;
        }
    }

    @Override
    public Object getAttribute(String attributeName)
    {
        if (attributeName == null)
        {
            return null;
        }

        switch (attributeName)
        {
            case "subject":
                // The code of the user.
                return subject;

            default:
                // Unsupported attribute.
                return null;
        }
    }
}

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


import com.authlete.common.types.User;
import com.authlete.jakarta.spi.UserInfoRequestHandlerSpiAdapter;
import com.authlete.spring.server.db.UserDao;


/**
 * Implementation of {@link com.authlete.jakarta.spi.UserInfoRequestHandlerSpiAdapter
 * UserInfoRequestHandlerSpi} interface which needs to be given to the
 * constructor of {@link com.authlete.jakarta.spi.UserInfoRequestHandlerSpiAdapter
 * UserInfoRequestHandler}.
 */
public class UserInfoRequestHandlerSpiImpl extends UserInfoRequestHandlerSpiAdapter
{
    private User mUser;


    @Override
    public void prepareUserClaims(String subject, String[] claimNames)
    {
        // Look up a user who has the subject.
        mUser = UserDao.getBySubject(subject);
    }


    @Override
    public Object getUserClaim(String claimName, String languageTag)
    {
        // If looking up a user has failed in prepareUserClaims().
        if (mUser == null)
        {
            // No claim is available.
            return null;
        }

        // Get the value of the claim.
        return mUser.getClaim(claimName, languageTag);
    }
}

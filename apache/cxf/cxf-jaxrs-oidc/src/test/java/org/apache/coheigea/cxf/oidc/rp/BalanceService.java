/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.coheigea.cxf.oidc.rp;


import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * A "Balance" service for a bank.
 */
@Path("/balance/")
public class BalanceService {
    protected static Map<String, Integer> balances = new HashMap<String, Integer>();
    
    @Context
    private SecurityContext securityContext;
    
    public BalanceService() {
        // Create initial account for bob
        balances.put("bob", 1000);
    }
    
    @GET
    @Path("/{user}")
    @Produces("text/plain,application/json")
    public int getBalance(@PathParam("user") String user) {
        authenticateUser(user);
        
        if (balances.containsKey(user)) {
            return balances.get(user);
        }
        
        throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    
    @POST
    @Path("/{user}")
    public void setBalance(@PathParam("user") String user, int amount) {
        authenticateUser(user);
        
        if (!balances.containsKey(user)) {
            balances.put(user, amount);
        }
    }
    
    protected void authenticateUser(String user) {
        if (securityContext.getUserPrincipal() == null || user == null
            || !user.equals(securityContext.getUserPrincipal().getName())) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}



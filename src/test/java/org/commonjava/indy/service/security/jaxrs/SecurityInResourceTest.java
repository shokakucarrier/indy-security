/**
 * Copyright (C) 2022 Red Hat, Inc. (https://github.com/Commonjava/indy-security)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.indy.service.security.jaxrs;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

@QuarkusTest
public class SecurityInResourceTest
{
    @Test
    public void testPublic()
    {
        given().when().get( "/api/public" ).then().statusCode( OK.getStatusCode() );
    }

    @Test
    public void testWriteAdminWithoutAuth()
    {
        given().when().put( "/api/admin/resource" ).then().statusCode( FORBIDDEN.getStatusCode() );
        given().when().post( "/api/admin/resource" ).then().statusCode( FORBIDDEN.getStatusCode() );
        given().when().delete( "/api/admin/resource" ).then().statusCode( FORBIDDEN.getStatusCode() );
    }

    @Test
    @TestSecurity( roles = "user", user = "user" )
    public void testWriteAdminWithWrongRole()
    {
        given().when().put( "/api/admin/resource" ).then().statusCode( FORBIDDEN.getStatusCode() );
        given().when().post( "/api/admin/resource" ).then().statusCode( FORBIDDEN.getStatusCode() );
        given().when().delete( "/api/admin/resource" ).then().statusCode( FORBIDDEN.getStatusCode() );
    }

    @Test
    @TestSecurity( roles = "admin", user = "admin" )
    public void testWriteAdminWithAuth()
    {
        given().when().put( "/api/admin/resource" ).then().statusCode( CREATED.getStatusCode() );
        given().when().post( "/api/admin/resource" ).then().statusCode( CREATED.getStatusCode() );
        given().when().delete( "/api/admin/resource" ).then().statusCode( NO_CONTENT.getStatusCode() );
    }

    @Test
    public void testGetStore()
    {
        given().when().get( "/api/admin/stores/testtype/test" ).then().statusCode( OK.getStatusCode() );
    }

    @Test
    @TestSecurity( roles = "admin", user = "admin" )
    public void testWriteStoreWithWrongRole()
    {
        given().when().put( "/api/admin/stores/testtype/test" ).then().statusCode( FORBIDDEN.getStatusCode() );
        given().when().post( "/api/admin/stores/testtype/test" ).then().statusCode( FORBIDDEN.getStatusCode() );
        given().when().delete( "/api/admin/stores/testtype/test" ).then().statusCode( FORBIDDEN.getStatusCode() );
    }

    @Test
    @TestSecurity( roles = "power-user", user = "pouser" )
    public void testWriteStoreWithAuth()
    {
        given().when().put( "/api/admin/stores/testtype/test" ).then().statusCode( CREATED.getStatusCode() );
        given().when().post( "/api/admin/stores/testtype/test" ).then().statusCode( CREATED.getStatusCode() );
        given().when().delete( "/api/admin/stores/testtype/test" ).then().statusCode( NO_CONTENT.getStatusCode() );
    }
}

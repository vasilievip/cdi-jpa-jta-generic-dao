/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javafiction.common.persistence.test;

import com.acme.model.Product;
import com.acme.services.ProductService;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.naming.InitialContext;


@RunWith(CdiTestRunner.class)
public class ProductServiceTest {

    @Inject
    ProductService service;

    @BeforeClass
    public static void setUp() throws Exception {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;MODE=Oracle;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        InitialContext context = new InitialContext();
        context.bind("java:/jdbc/testDS1", dataSource);
    }

    @AfterClass
    public static void tearDown() throws Exception {
    }

    @Test
    public void testFindAll() throws Exception {
        System.out.println(service.save(new Product("product1")));
        System.out.println(service.save2(new Product("product2")));
        Assert.assertEquals(2, service.findAll().size());
        Assert.assertEquals(2, service.findAll2().size());
    }

    @Test
    public void testFindByExample() throws Exception {
//        service.
    }
}

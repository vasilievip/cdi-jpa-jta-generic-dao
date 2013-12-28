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
package com.acme.services;


import com.acme.model.Product;
import com.acme.persistence.DeltaspikeProductDAO;
import com.acme.persistence.SpringDataProductDAO;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Named
public class ProductService {

    @Inject
    SpringDataProductDAO springProductDAO;

    @Inject
    DeltaspikeProductDAO deltaspikeProductDAO;

    @Transactional
    public List<Product> findAll() {
        assert springProductDAO != null;
        List<Product> products = new ArrayList<Product>();
        Iterator<Product> productIterator = springProductDAO.findAll().iterator();
        while (productIterator.hasNext()) {
            products.add(productIterator.next());
        }
        return products;
    }

    @Transactional
    public Long save(Product product) {
        assert springProductDAO != null;
        springProductDAO.save(product);
        return product.getId();
    }

    @Transactional
    public List<Product> findAll2() {
        assert deltaspikeProductDAO != null;
        List<Product> products = new ArrayList<Product>();
        Iterator<Product> productIterator = deltaspikeProductDAO.findAll().iterator();
        while (productIterator.hasNext()) {
            products.add(productIterator.next());
        }
        return products;
    }

    @Transactional
    public Long save2(Product product) {
        assert deltaspikeProductDAO != null;
        deltaspikeProductDAO.save(product);
        return product.getId();
    }
}

// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/** Servlet responsible for creating new news entities. */
@WebServlet("/new-news")
public class NewNewsServlet extends HttpServlet {

    //POST METHOD. It'll receive the user's data to create new entities in the Datastore
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    // Initializing variables that will storage the info fetched for the new entity
    String newsLink = request.getParameter("link");
    String newsTitle = request.getParameter("title");
    String newsPlace = request.getParameter("place");
    long timestamp = System.currentTimeMillis();

    // Creating the instance of the Datastore class to interact with it!
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    // Creating a Key Factory for the kind of news (each entity has an associated key!)
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("News");
  
    // Creating news entities, by setting their properties!
    FullEntity newsEntity =
        Entity.newBuilder(keyFactory.newKey())
        // adding entity properties
            .set("link", newsLink)
            .set("title", newsTitle)
            .set("place", newsPlace)
            .set("timestamp", timestamp)
        // building the entity
            .build();
        // storing the entity!
    datastore.put(newsEntity);

    response.sendRedirect("/index.html");
  }
}
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
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import com.google.sps.data.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for listing news for the user. */
@WebServlet("/list-news")
public class ListNewsServlet extends HttpServlet {

    // Creating a class News to structuring local entities and manipulate them
    public class News implements Serializable{
        private long id;
        private String newsLink;
        private String newsTitle; 
        private String newsPlace;
        private long timestamp;

        public News(long id, String newsLink, String newsTitle, String newsPlace, long timestamp) {
            this.id = id;
            this.newsLink = newsLink;
            this.newsTitle = newsTitle;
            this.newsPlace = newsPlace;
            this.timestamp = timestamp;
        }

        public long id() {
            return id;
        }
        public String newsLink() {
            return newsLink;
        }
        public String newsTitle() {
            return newsTitle;
        }
        public String newsPlace() {
            return newsPlace;
        }
        public long timestamp() {
            return timestamp;
        }
        public void setId(long id) {
            this.id = id;
        }
        public void setNewsLink(String newsLink) {
            this.newsLink = newsLink;
        }
        public void setNewsTitle(String newsTitle) {
            this.newsTitle = newsTitle;
        }
        public void setNewsPlace(String newsPlace) {
            this.newsPlace = newsPlace;
        }
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    // GET METHOD. It'll send the info placed in the Datastore to the user, once they request it.
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    // Creating an instance of the Datastore to manipulate it!
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    
    // Creating a Query instance using the News Entity
    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("News").setOrderBy(OrderBy.desc("timestamp")).build();
    // results contains ALL of the entities in Datastore with that kind!
        QueryResults<Entity> results = datastore.run(query);

    // Creating an array of news to manipulate the query
    List<News> newsRequested = new ArrayList<>();

    // Iterating through the entities queried
    while (results.hasNext()) {
      // Auxiliar to keep iterating!
      Entity entity = results.next();
      
      // Storing the queried data of each entity (destructuring it)
      long id = entity.getKey().getId();
      String newsLink = entity.getString("link");
      String newsTitle = entity.getString("title");
      String newsPlace = entity.getString("place");
      long timestamp = entity.getLong("timestamp");

      News item = new News(id, newsLink, newsTitle, newsPlace, timestamp);
      newsRequested.add(item);
    }

    // Sending the response in JSON format for the client to understand it
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(newsRequested));
  }
}

package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.sps.data.Location;

@WebServlet("/resource")
public class ResourceServlet extends HttpServlet {
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType("text/html;");
        response.getWriter().println("<h1>News and Resources from Your Area!</h1>");
        response.getWriter().println("https://www.nytimes.com/2021/03/18/nyregion/asian-hate-crimes.html");
        response.getWriter().println("https://theconversation.com/african-american-teens-face-mental-health-crisis-but-are-less-likely-than-whites-to-get-treatment-140697");
        response.getWriter().println("https://www.plannedparenthood.org/");

        final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        final Query<Entity> query = Query.newEntityQueryBuilder().setKind("Resource")
                .setOrderBy(OrderBy.desc("timestamp")).build();
        final QueryResults<Entity> results = datastore.run(query);

        while (results.hasNext()) {
            final Entity entity = results.next();
            final String resource = entity.getString("title");
            response.getWriter().println("<li>" + resource + "</li>");
            
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("location");
        System.out.println(request);
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("Location");
        System.out.println(keyFactory.newKey());
        System.out.println(title);
        FullEntity locationEntity = Entity.newBuilder(keyFactory.newKey())
            .set("location", title)
            .set("timestamp", System.currentTimeMillis())
            .build();
        System.out.println(locationEntity);
        datastore.put(locationEntity);

        response.sendRedirect("/index.html");
    }
}
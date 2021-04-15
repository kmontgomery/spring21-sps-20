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
import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.Gson;

@WebServlet("/resource")
public class ResourceServlet extends HttpServlet {
    ArrayList<String> resources = new ArrayList<>(Arrays.asList("https://www.nytimes.com/2021/03/18/nyregion/asian-hate-crimes.html", 
        "https://www.plannedparenthood.org/", "https://theconversation.com/african-american-teens-face-mental-health-crisis-but-are-less-likely-than-whites-to-get-treatment-140697 "));
    String json = convertToJsonUsingGson(resources);
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        response.setContentType("text/html;");
        response.getWriter().println("<h1>News and Resources from Your Area!</h1>");
        response.getWriter().println(json);

        final Query<Entity> query = Query.newEntityQueryBuilder().setKind("Resource")
                .setOrderBy(OrderBy.desc("timestamp")).build();
        final QueryResults<Entity> results = datastore.run(query);

        //List<String> resources = new ArrayList<>();
        //resources.add("https://www.nytimes.com/2021/03/18/nyregion/asian-hate-crimes.html");
        //resources.add("https://www.plannedparenthood.org/");
        //resources.add("https://theconversation.com/african-american-teens-face-mental-health-crisis-but-are-less-likely-than-whites-to-get-treatment-140697");
        while (results.hasNext()) {
            final Entity entity = results.next();
            final String resource = entity.getString("title");
            // for(String link : resources) {
                //response.getWriter().println("<li>" + link + "</li>");
            //}
            
        }
        //String json = converToJsonUsingGson(resources);
        //response.getWriter().println(json);
    }

    public String convertToJsonUsingGson(ArrayList<String> messageList2) {
        Gson gson = new Gson();
        String json = gson.toJson(messageList2);
        return json;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("userLocation");
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

        response.sendRedirect("/index.html");
    }
}
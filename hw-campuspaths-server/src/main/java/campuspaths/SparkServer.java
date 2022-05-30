/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.DijkstraPathfinder;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class SparkServer
{

    public static void main(String[] args)
    {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        CampusMap campusMap = new CampusMap();
        Gson gson = new Gson();

        Spark.get("/getBuildings", new Route()
        {
            @Override
            public Object handle(Request request,
                                 Response response) throws Exception
            {
                return gson.toJson(campusMap.buildingNames());
            }
        });


        Spark.get("/getPath", new Route()
        {
            @Override
            public Object handle(Request request,
                                 Response response) throws Exception
            {
                String src = request.queryParams("src");
                String dest = request.queryParams("dest");

                if (src == null || dest == null || !campusMap.shortNameExists(src) ||
                        !campusMap.shortNameExists(dest))
                {
                    Spark.halt(400, src == null ? "src cannot be null!" :
                            (dest == null ? "dest cannot be null!" :
                                    "Building " + (!campusMap.shortNameExists(src) ? src : dest) +
                                            " does not exist!"));
                }

                return gson.toJson(campusMap.findShortestPath(src, dest));
            }
        });
    }

}

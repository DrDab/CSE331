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
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Configures a Spark HTTP server to respond to HTTP requests with
 * path /getBuildings returning a JSON object of UW buildings' short names
 * mapped to their full names, and path /getPath?src={SOURCE_BLDG_NAME}&dest={DEST_BLDG_NAME}
 * returning a JSON representation of the Path instance representing the shortest path
 * from UW source and destination buildings with shorthand names SOURCE_BLDG_NAME and
 * DEST_BLDG_NAME respectively.
 */
public class SparkServer
{
    /**
     * Configures the Spark HTTP server to respond to the HTTP requests to get
     * the shortest path between two UW buildings and list all the available UW
     * buildings to pathfind between.
     *
     * @param args, the arguments to be passed to the main method.
     * @spec.requires args != null
     */
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
                // return a JSON object of all the campus map's building shorthand names
                // mapped to their full names.
                return gson.toJson(campusMap.buildingNames());
            }
        });


        Spark.get("/getPath", new Route()
        {
            @Override
            public Object handle(Request request,
                                 Response response)
            {
                // source building parameter (must be shorthand name)
                String src = request.queryParams("src");

                // destination building parameter (must be shorthand name)
                String dest = request.queryParams("dest");

                if (src == null || dest == null || !campusMap.shortNameExists(src) ||
                        !campusMap.shortNameExists(dest))
                {
                    // if the source/destination building parameters are null
                    // or aren't in the campus map, throw error 400.
                    Spark.halt(400, src == null ? "src cannot be null!" :
                            (dest == null ? "dest cannot be null!" :
                                    "Building " + (!campusMap.shortNameExists(src) ? src : dest) +
                                            " does not exist!"));
                }

                // otherwise return the JSON representation of the shortest path
                // from the source building to the destination building
                return gson.toJson(campusMap.findShortestPath(src, dest));
            }
        });
    }

}

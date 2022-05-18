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

package pathfinder;

import graph.DirectedGraph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI
{
    private DijkstraPathfinder<Point> pFinder;
    private Map<String, CampusBuilding> shortNameBldgMap;

    public CampusMap()
    {
        DirectedGraph<Point, Double> graph = new DirectedGraph<>();
        shortNameBldgMap = new HashMap<>();

        List<CampusBuilding> buildings =
                CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> paths =
                CampusPathsParser.parseCampusPaths("campus_paths.csv");

        for (CampusBuilding cb : buildings)
        {
            Point p = new Point(cb.getX(), cb.getY());
            graph.addNode(p);
            shortNameBldgMap.put(cb.getShortName(), cb);
        }

        for (CampusPath cp : paths)
        {
            Point srcPoint = new Point(cp.getX1(), cp.getY1());
            Point destPoint = new Point(cp.getX2(), cp.getY2());

            if (!graph.hasNode(srcPoint))
                graph.addNode(srcPoint);

            if (!graph.hasNode(destPoint))
                graph.addNode(destPoint);

            // assuming no duplicate paths, this is valid :)
            graph.addEdge(srcPoint, destPoint, cp.getDistance());
        }

        pFinder = new DijkstraPathfinder<>(graph);
    }

    @Override
    public boolean shortNameExists(String shortName)
    {
        return shortNameBldgMap.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName)
    {
        if (!shortNameExists(shortName))
            throw new IllegalArgumentException(
                    String.format("No node exists with short name \"%s\"!", shortName));

        return shortNameBldgMap.get(shortName).getLongName();
    }

    @Override
    public Map<String, String> buildingNames()
    {
        Map<String, String> res = new HashMap<>();

        for (String shortName : shortNameBldgMap.keySet())
            res.put(shortName, shortNameBldgMap.get(shortName).getLongName());

        return res;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName)
    {
        if (startShortName == null || endShortName == null)
        {
            throw new IllegalArgumentException(String.format("%s cannot be, but is null!",
                    startShortName == null && endShortName == null ?
                            "startShortName and endShortName" :
                            startShortName == null ? "startShortName" : "endShortName"));
        }

        if (!shortNameBldgMap.containsKey(startShortName) ||
                !shortNameBldgMap.containsKey(endShortName))
        {
            throw new IllegalArgumentException(String.format("Buildings by short-name %s don't exist!",
                    (!shortNameBldgMap.containsKey(startShortName) &&
                            !shortNameBldgMap.containsKey(endShortName)) ?
                            startShortName + " and " + endShortName :
                            !shortNameBldgMap.containsKey(startShortName) ?
                                    startShortName : endShortName));
        }

        CampusBuilding srcBldg = shortNameBldgMap.get(startShortName);
        CampusBuilding destBldg = shortNameBldgMap.get(endShortName);
        Point srcPoint = new Point(srcBldg.getX(), srcBldg.getY());
        Point destPoint = new Point(destBldg.getX(), destBldg.getY());

        return pFinder.getShortestPath(srcPoint, destPoint);
    }

}

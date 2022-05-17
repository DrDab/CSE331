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

package pathfinder.scriptTestRunner;

import graph.DirectedGraph;
import pathfinder.DijkstraPathfinder;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver
{
    private final Map<String, DirectedGraph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w)
    {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void findPath(List<String> arguments)
    {
        if (arguments.size() != 3)
            throw new CommandException("Bad arguments to findPath: " + arguments);

        String graphName = arguments.get(0);
        String sourceNode = arguments.get(1);
        String destNode = arguments.get(2);
        findPath(graphName, sourceNode, destNode);
    }

    private void findPath(String graphName, String sourceNode, String destNode)
    {
        DirectedGraph<String, Double> graph = graphs.get(graphName);

        if (!graph.hasNode(sourceNode) || !graph.hasNode(destNode))
        {
            output.println("unknown: node");
        }
        else
        {
            DijkstraPathfinder<String> finder = new DijkstraPathfinder<>(graph);
            Path<String> path = finder.getShortestPath(sourceNode, destNode);

            output.printf("path from %s to %s:%n");

            if (path == null)
            {
                output.println("no path found");
            }
            else
            {
                for (Path<String>.Segment<String> seg : path)
                {
                    output.printf("%s to %s with weight %.3f%n", seg.getStart(), seg.getEnd(),
                            seg.getCost());
                }

                output.printf("total cost: %.3f%n", path.getCost());
            }
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new DirectedGraph<>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        DirectedGraph<String, Double> graph = graphs.get(graphName);
        graph.addNode(nodeName);
        output.printf("added node %s to %s%n", nodeName, graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, Double.valueOf(edgeLabel));
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        DirectedGraph<String, Double> graph = graphs.get(graphName);
        graph.addEdge(parentName, childName, edgeLabel);
        output.printf("added edge %.3f from %s to %s in %s%n", edgeLabel, parentName,
                childName, graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        DirectedGraph<String, Double> graph = graphs.get(graphName);
        List<String> nodes = graph.getNodes();
        Collections.sort(nodes);

        String nodesStr = "";
        for (String node : nodes)
        {
            nodesStr += " " + node;
        }

        output.printf("%s contains:%s%n", graphName, nodesStr);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        DirectedGraph<String, Double> graph = graphs.get(graphName);
        Set<String> nodeSet = graph.getChildNodes(parentName);
        List<String> nodes = new ArrayList<>(nodeSet);
        Collections.sort(nodes);

        String edgesStr = "";

        for (String destName : nodes)
        {
            List<Double> edges = graph.getEdges(parentName, destName);
            Collections.sort(edges);

            for (Double edge : edges)
            {
                edgesStr += String.format(" %s(%.3f)", destName, edge);
            }
        }

        output.printf("the children of %s in %s are:%s%n", parentName, graphName, edgesStr);
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3496;
    }
}

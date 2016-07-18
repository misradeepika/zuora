package com.misradeepika;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Finds popular paths from given a list of users and the visited pages.
 *
 * @author Deepika Misra
 */
public class PopularPathFinder {

    public static void main(String[] args) {
        //Test-case 1: Every user visits less than two pages
        List<PageVisit> input1 = new ArrayList<>();
        input1.add(new PageVisit("U1", "home"));
        input1.add(new PageVisit("U1", "subscribers"));
        input1.add(new PageVisit("U2", "home"));
        input1.add(new PageVisit("U2", "subscribers"));
        input1.add(new PageVisit("U3", "home"));
        input1.add(new PageVisit("U3", "export"));
        List<List<String>> result1 = findPopularPaths(input1, 1);
        System.out.println("Results for test case 1");
        printOutResult(result1); // Output: I got nothing
        //Test-case 2: only one user visits more than 2 pages and we need the top 1 path
        List<PageVisit> input2 = new ArrayList<>();
        input2.add(new PageVisit("U1", "home"));
        input2.add(new PageVisit("U1", "subscribers"));
        input2.add(new PageVisit("U1", "filter"));
        input2.add(new PageVisit("U2", "home"));
        input2.add(new PageVisit("U2", "subscribers"));
        input2.add(new PageVisit("U3", "home"));
        input2.add(new PageVisit("U3", "export"));
        List<List<String>> result2 = findPopularPaths(input2, 1);
        System.out.println("Results for test case 2");
        printOutResult(result2); // Output: home -> subscribers -> filter
        //Test-case 2: only one user visits more than 2 pages and we need the top 2 paths
        List<PageVisit> input3 = new ArrayList<>();
        input3.add(new PageVisit("U1", "home"));
        input3.add(new PageVisit("U1", "subscribers"));
        input3.add(new PageVisit("U1", "filter"));
        input3.add(new PageVisit("U2", "home"));
        input3.add(new PageVisit("U2", "subscribers"));
        input3.add(new PageVisit("U3", "home"));
        input3.add(new PageVisit("U3", "export"));
        List<List<String>> result3 = findPopularPaths(input3, 2);
        System.out.println("Results for test case 3");
        printOutResult(result3); // Output: home -> subscribers -> filter
        //Test-case 3: Many user visits more than 2 pages and we need the top 3 paths
        List<PageVisit> input4 = new ArrayList<>();
        input4.add(new PageVisit("U1", "home"));
        input4.add(new PageVisit("U1", "subscribers"));
        input4.add(new PageVisit("U1", "filter"));
        input4.add(new PageVisit("U1", "show"));
        input4.add(new PageVisit("U2", "home"));
        input4.add(new PageVisit("U2", "subscribers"));
        input4.add(new PageVisit("U2", "filter"));
        input4.add(new PageVisit("U2", "about"));
        input4.add(new PageVisit("U3", "home"));
        input4.add(new PageVisit("U3", "export"));
        input4.add(new PageVisit("U3", "about"));
        input4.add(new PageVisit("U3", "subscribers"));
        List<List<String>> result4 = findPopularPaths(input4, 3);
        System.out.println("Results for test case 4");
        printOutResult(result4); // Output: home -> subscribers -> filter, home -> export -> about, subscribers -> filter -> show
    }

    private static void printOutResult(List<List<String>> resultPaths) {
        if (resultPaths.isEmpty()) {
            System.out.println("I got nothing");
        }
        for (List<String> resultPath : resultPaths) {
            System.out.println(resultPath.get(0) + " -> " + resultPath.get(1) + " -> " + resultPath.get(2) + "\n");
        }
    }

    public static List<List<String>> findPopularPaths(List<PageVisit> pageVisits, int topN) {
        List<List<String>> popularPaths = new ArrayList<>();
        //transform the input into a map with the user as the key and the list of pages they have visited as the value
        Map<String, List<String>> userVisits = perUserVists(pageVisits);
        // Build a tree with the pages visited by a user as nodes
        PathTree tree = buildPathTree(userVisits);
        // Build a SortedMap from the tree with the frequency total for every possible 3-node deep sub tree as the key 
        // and the list of sub-trees as the total
        TreeMap<Integer, List<List<String>>> pathFreqMap = buildPathFreqMap(tree);
        // Reverse the SortedMap to get the frequencies descendingly
        Collection<List<List<String>>> topPaths = pathFreqMap.descendingMap().values();
        // Get the topN 3-node lists from the values.
        Iterator<List<List<String>>> iterator = topPaths.iterator();
        while (popularPaths.size() < topN && iterator.hasNext()) {
            List<List<String>> paths = iterator.next();
            if (paths.size() <= topN - popularPaths.size()) {
                popularPaths.addAll(paths);
            } else {
                popularPaths.addAll(paths.subList(0, topN - popularPaths.size()));
            }
        }
        return popularPaths;
    }

    private static Map<String, List<String>> perUserVists(List<PageVisit> pageVisits) {
        Map<String, List<String>> perUserVisits = new HashMap<>();
        for(PageVisit pageVisit: pageVisits) {
            String user = pageVisit.getUser();
            List<String> pages = perUserVisits.getOrDefault(user, new ArrayList<>());
            pages.add(pageVisit.getPage());
            perUserVisits.put(user, pages);
        }
        return perUserVisits;
    }

    private static PathTree buildPathTree(Map<String, List<String>> userVisits) {
        PageNode root = null;
        for (List<String> path : userVisits.values()) {
            PageNode currentRoot = root;
            for (int i=0; i<path.size(); i++) {
                if (currentRoot == null) {
                    root = new PageNode(path.get(i));
                    currentRoot = root;
                } else if (i == 0) {
                    //Assume that all users start at the common page
                    currentRoot.incrementCount();
                } else {
                    PageNode childNode = currentRoot.getChildNode(path.get(i));
                    if (childNode == null) {
                        childNode = currentRoot.addChildNode(path.get(i));
                    } else {
                        childNode.incrementCount();
                    }
                    currentRoot = childNode;
                }
            }
        }
        return new PathTree(root);
    }

    // Breadth first traversal to get every possible 3-node deep sub tree along with the frequency total in a map.
    private static TreeMap<Integer, List<List<String>>> buildPathFreqMap(PathTree tree) {
        TreeMap<Integer, List<List<String>>> freqMap = new TreeMap<>();
        PageNode parent = tree.getRoot();
        Stack<PageNode> nodes = new Stack<>();
        nodes.push(parent);
        while (!nodes.empty()) {
            PageNode currentRoot = nodes.pop();
            for (PageNode child : currentRoot.getChildren()) {
                if (child.hasChildren()) {
                    nodes.push(child);
                    for (PageNode grandChild : child.getChildren()) {
                        int freq = currentRoot.getCount() + child.getCount() + grandChild.getCount();
                        List<List<String>> pathsWithFreq = freqMap.getOrDefault(freq, new ArrayList<>());
                        pathsWithFreq.add(Arrays.asList(currentRoot.getPage(), child.getPage(), grandChild.getPage()));
                        freqMap.put(freq, pathsWithFreq);
                    }
                }
            }
        }
        return freqMap;
    }
}

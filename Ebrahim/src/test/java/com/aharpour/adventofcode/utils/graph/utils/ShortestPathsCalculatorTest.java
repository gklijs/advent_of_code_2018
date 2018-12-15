package com.aharpour.adventofcode.utils.graph.utils;

import com.aharpour.adventofcode.utils.graph.AbstractGraph;
import com.aharpour.adventofcode.utils.graph.WeightedGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShortestPathsCalculatorTest {

    private WeightedGraph graph = new WeightedGraph();
    private ShortestPathsCalculator calculator = new ShortestPathsCalculator(graph);

    @Before
    public void init() {
        graph.addRelationShip("A", "B", 1);
        graph.addRelationShip("A", "C", 2);
        graph.addRelationShip("D", "C", 3);
        graph.addRelationShip("B", "D", 5);
        graph.addRelationShip("F", "D", 7);
        graph.addRelationShip("F", "A", 12);
    }

    @Test
    public void distanceToDTest() {
        List<Path> paths = calculator.getPaths(graph.getNode("A"), graph.getNode("D"));
        assertEquals(1, paths.size());
        assertEquals(5, paths.get(0).getLength());
    }

    @Test
    public void distanceToFTest() {
        List<Path> paths = calculator.getPaths(graph.getNode("A"), graph.getNode("F"));
        assertEquals(2, paths.size());
        assertEquals(12, paths.get(0).getLength());
    }

    @Test
    public void CBlockedTest() {
        AbstractGraph.Node c = graph.getNode("C");
        c.setBlocked(true);

        List<Path> paths = calculator.getPaths(graph.getNode("A"), graph.getNode("F"));

        assertEquals(1, paths.size());
        assertEquals(12, paths.get(0).getLength());
    }

    @Test
    public void DBlockedTest() {
        AbstractGraph.Node d = graph.getNode("D");
        d.setBlocked(true);

        List<Path> paths = calculator.getPaths(graph.getNode("A"), graph.getNode("F"));

        assertEquals(1, paths.size());
        assertEquals(12, paths.get(0).getLength());
    }


}
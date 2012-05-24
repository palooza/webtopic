/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.algorithms.cluster.VoltageClusterer;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.filters.FilterUtils;

import java.io.*;
import java.util.*;

/**
 *
 * @author hoshun
 */
public class RecursiveClusterer {

    public RecursiveClusterer(DirectedGraph<Integer, Integer> graph, int clusterMinSize, int clusterMaxSize) {
        this.graph = graph;
        this.clusterMinSize = clusterMinSize;
        this.clusterMaxSize = clusterMaxSize;
    }

    public Collection<Set<Integer>> cluster() {

        Collection<Integer> subVertices = graph.getVertices();

        PriorityQueue<VerticeCollection> clusterHeap = new PriorityQueue<VerticeCollection>(10, Collections.reverseOrder());
        clusterHeap.add(new VerticeCollection(subVertices));

        Collection<Set<Integer>> unbreakableClusters = new ArrayList<Set<Integer>>();

        while (!clusterHeap.isEmpty() && clusterHeap.peek().size > clusterMaxSize) {
            VerticeCollection vc = clusterHeap.remove();
            Collection<Integer> vertices = vc.vertices;
            int origSize = vc.size;

            Collection<Set<Integer>> newMemberSets = _bicluster(vertices);

            for (Set<Integer> newMemberSet : newMemberSets) {
                if (newMemberSet.size() >= clusterMinSize) {// filter cluster that are too small
                    int curSize = newMemberSet.size();

                    if ( curSize > 0.95 * origSize) {
                        // don't break unbreakable clusters
                        if(curSize < 1000){
                            unbreakableClusters.add(newMemberSet);
                        }else{
                            // discared the graph because it is too large
                            System.out.println("Discard cluster size: " + curSize);
                        }
                    } else {
                        clusterHeap.add(new VerticeCollection(newMemberSet));
                    }
                }
            }
        }

        // output the valid clusters in the heap
        Collection<Set<Integer>> results = new ArrayList<Set<Integer>>();

        while (!clusterHeap.isEmpty()) {
            Set<Integer> cluster = new TreeSet(clusterHeap.remove().vertices);
            results.add(cluster);
        }

        // add unbreakabe set 
        for (Set<Integer> cluster : unbreakableClusters) {
            System.out.println("unbreakable cluster of size: " + cluster.size());
            results.add(cluster);
        }
        return results;
    }

    // using voltage clusterer to divide to cluster into 2 
    private Collection<Set<Integer>> _bicluster(Collection<Integer> vertices) {
        DirectedGraph<Integer, Integer> subgraph = FilterUtils.createInducedSubgraph(vertices, graph);
        
        if(subgraph.getEdgeCount() == 0 || subgraph.getVertexCount() == 0){
            return new ArrayList<Set<Integer>>();
        }
        
        VoltageClusterer<Integer, Integer> vclusterer = new VoltageClusterer<Integer, Integer>(subgraph, 2);
        Collection<Set<Integer>> clusters = vclusterer.cluster(2);
        return clusters;
    }
    
    
    private int clusterMinSize;
    private int clusterMaxSize;
    private DirectedGraph<Integer, Integer> graph;
}

/**
 * helper class
 *
 * @author hoshun
 */
class VerticeCollection implements Comparable<VerticeCollection> {

    public VerticeCollection(Collection<Integer> vertices) {
        this.vertices = vertices;
        size = vertices.size();
    }

    @Override
    public int compareTo(VerticeCollection o) {
        return ((Integer) size).compareTo(o.size);
    }
    public final Collection<Integer> vertices;
    public final int size;
}
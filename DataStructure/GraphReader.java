/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csi2510_d4_pkg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 * modifie par Simon Paquette 300044038
 */
public class GraphReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String edgesFilename = "email-dnc.edges";
        Graph                       graph = readGraph(edgesFilename);
        List<Integer>               nodes = graph.getGraphNodes();
        Map<Integer, List<Integer>> edges = graph.getGraphEdges();
        
        System.out.println("Number of nodes in the Graph: " + nodes.size());
        
        
        
        long start = System.currentTimeMillis();
        
        //Map to keep an association between a node and his PR value
        Map<Integer, Double> nodePr = new HashMap<>(); 
        
        //Set all PR value to 1
        for(Integer node : nodes) {
        	nodePr.put(node, 1.0);
        }
        
        final int ITERATION = 10;	//number of iteration made to stabilize the PR value 
        final double d =  0.85;		//damping factor
        Integer c;					//number of links coming out of Ti
        Integer node;				//node number
        Double pr; 					//PR value
        
        //n iteration to calculate and stabilize the PR value of each node
        System.out.println("Number of iteration to stabilize de pr value : "+ITERATION+"\nwith a damping factor of "+d+"\n");
        for (int i = 0; i < ITERATION; i++) {
        	for(Map.Entry<Integer, Double> entry : nodePr.entrySet()) {
        		node = entry.getKey();
        		pr = 0.0;
        		if (edges.containsKey(node)) {
        			
        			//neighbour's node
        			for(Integer edge : edges.get(node)) {
        				c = 0;
        				
        				//neighbour of the neighbour's node
        				for(Integer neighbour : edges.get(edge)) {
        					c++;
        				}
        				
        				//add the neighbour pr
        				if (c>0) {
        					pr += nodePr.get(edge)/c;	//pr += PR(Ti)/C(Ti)
        				}
        			}
        		}
        		pr = (1-d) + d*(pr);				//PR(A) = (1-d) + d (PR(T1)/C(T1) + ... + PR(Tn)/C(Tn))
        		nodePr.replace(node,pr);
        	}
        }
        
        final int HIGHEST_PAGERANK = 10;	//n node with the highest pr
        double maxValue;					//highest pr
        Integer maxNode;					//node with highest pr

        for (int i = 0; i < HIGHEST_PAGERANK ; i++) {
        	maxValue = -1;
        	maxNode = -1;
        	for(Map.Entry<Integer, Double> entry : nodePr.entrySet()) {
        		node = entry.getKey();
        		pr = entry.getValue();
        		if (pr.compareTo(maxValue) > 0) {
        			maxValue = pr;
        			maxNode = node;	
        		}
        	}
        	System.out.println(i+1+" : The node "+maxNode+" has a pr value of "+maxValue);
        	nodePr.remove(maxNode);
        	
        }
        
        long end = System.currentTimeMillis();
        double total = (end-start)/1000;
        System.out.println("\nTotal time : "+total+"s");
        
        
        
        //Suppress this part
        /*
        for(Integer node : nodes) {
            System.out.println("Node number: " + node);
            System.out.print("Adjacent Nodes: ");
            if (edges.containsKey(node)) {
                for(Integer edge : edges.get(node)) {
                    System.out.print(edge + " ");
                }
            }
            System.out.println();
            System.out.println("------------------------------------");
        }
        */
    }
    
    private static Graph readGraph(String edgesFilename) throws FileNotFoundException, IOException {
        System.getProperty("user.dir");
        URL edgesPath = GraphReader.class.getResource(edgesFilename);
        BufferedReader csvReader = new BufferedReader(new FileReader(edgesPath.getFile()));
        String row;
        List<Integer>               nodes = new ArrayList<Integer>();
        Map<Integer, List<Integer>> edges = new HashMap<Integer, List<Integer>>(); 
        
        boolean first = false;
        while ((row = csvReader.readLine()) != null) {
            if (!first) {
                first = true;
                continue;
            }
            
            String[] data = row.split(",");
            
            Integer u = Integer.parseInt(data[0]);
            Integer v = Integer.parseInt(data[1]);
            
            if (!nodes.contains(u)) {
                nodes.add(u);
            }
            if (!nodes.contains(v)) {
                nodes.add(v);
            }
            
            if (!edges.containsKey(u)) {
                // Create a new list of adjacent nodes for the new node u
                List<Integer> l = new ArrayList<Integer>();
                l.add(v);
                edges.put(u, l);
            } else {
                edges.get(u).add(v);
            }
        }
        
        for (Integer node : nodes) {
            if (!edges.containsKey(node)) {
                edges.put(node, new ArrayList<Integer>());
            }
        }
        
        csvReader.close();
        return new Graph(nodes, edges);
    }
    
}

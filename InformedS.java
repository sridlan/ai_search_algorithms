// AI Search Algorithms 
//A* & Greedy best first - Graph versions

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class InformedS {
	// Attributes
	String node, goalNode;
		

	Map<String, Integer> heuristicCosts = new HashMap<String, Integer> (
			  Map.of("A",5, "B", 6, "C",8, "D", 4,
					  "E",4, "F", 5, "G",2, "H", 0));
	
	Map<String, Integer> stepCosts = new HashMap<String, Integer> (
			  Map.of( "AB", 3, "AC", 3, "BD", 2, "CF", 3,
					 "DE", 4, "EF", 1, "EG",2, "FG", 3, "GH", 2));
	
	Map<String, Integer> frontierMap = new HashMap<>();
	
	Map<String, Integer> exploredMap = new HashMap<>();
	
	// To trace back the successful path
	Map<String, String> predecessorMap = new HashMap<>();
			
	public String informedSearch(String initialState, String goalState, String searchType)
	{
		System.out.println("Search type: "+ searchType);
		node = initialState;
		if (node.equals(goalState))
			{
				System.out.println("Success!");
				return node;
			}
		else 
			{
				frontierMap.put(node, heuristicCosts.get(node));
			 }
			
		do {
			
			String exploreNode = Collections.min(frontierMap.entrySet(), Entry.comparingByValue()).getKey();
			System.out.println("Frontier map: " + frontierMap);
			
			exploredMap.put(exploreNode, frontierMap.get(exploreNode));
			frontierMap.remove(exploreNode);					
			System.out.println("Explore node with least cost: " + exploreNode +" = "+ exploredMap.get(exploreNode));
			
			List<String> adjacentEdges = new ArrayList<>();
			
			// Get the edges from step costs map, which are the result of actions on the node	
			stepCosts.forEach((k, v) -> {
	            if (k.charAt(0) == exploreNode.charAt(exploreNode.length()-1)){
	            	adjacentEdges.add(k);
	            }
	        });
			System.out.println("Child nodes of " + exploreNode + ":"+ adjacentEdges);
				
			for(String childNode : adjacentEdges) {
				if (!frontierMap.containsKey(childNode) && !exploredMap.containsKey(childNode)) 				
				{
					predecessorMap.put(childNode, exploreNode);
					
					if (String.valueOf(childNode.charAt(childNode.length()-1)).equals(goalState) )
					{
						System.out.println("Success!");
						System.out.println("Path: "+ getPredecessors(childNode));
						System.out.println("Total path cost: " + getPathCost(childNode));
						return childNode;
					}
						
					else {
						if (searchType.equals("greedyBF")) {
							frontierMap.put(childNode, 
									heuristicCosts.get(String.valueOf(childNode.charAt(childNode.length()-1))));
						}
						else if (searchType.equals("aStar")) {
							frontierMap.put(childNode, 
									(heuristicCosts.get(String.valueOf(childNode.charAt(childNode.length()-1))) 
											+ getPathCost(childNode)) );
							}
						}
				}
			}
		} while (!frontierMap.isEmpty());
		
		//  Return failure when frontier is empty
		System.out.println("Failure to find goal node: " + goalState);
		return "Failure";
	}
			
	public List<String> getPredecessors(String goalnode) {
		String currentNode = goalnode;
		List<String> parentList = new ArrayList<>();		
		// Trace back parents from node
		while (currentNode != null) {
			parentList.add(currentNode);
			currentNode = predecessorMap.get(currentNode);
		}		
		// Reverse the parents list
		Collections.reverse(parentList);
		return parentList;
	}
	
	// Function to get path cost up to the node
	public Integer getPathCost(String pathNode) {
		String currentNode = pathNode;
		Integer currentPathCost = 0;
		List<String> parentsList = getPredecessors(currentNode);
		for(String n: parentsList) {
			if (stepCosts.get(n) != null) {
				currentPathCost = currentPathCost + stepCosts.get(n);
			}
		}
		return currentPathCost;
	}
		
	public static void main(String[] args) {
		// Initialise new object
		InformedS myInfS = new InformedS();
		
		// Test informed search method
		System.out.println(myInfS.informedSearch("A", "H", "aStar"));
    System.out.println(myInfS.informedSearch("A", "H", "greedyBF"));

	}
}

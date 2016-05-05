import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Airline {
	
	static Scanner sc = new Scanner(System.in);
	static EdgeWeightedGraph myGraph;
	static String inputFile;

	public static void main(String[] args) throws IOException {
		
		System.out.println("Please enter a filename with airplane routes: ");
		inputFile = sc.next();
		
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		myGraph = new EdgeWeightedGraph(br);
		
		
		while(true){
			System.out.println("Please enter the number corresponding to one of the following options:\n"
					+ "1. Show the entire list of direct routes, distances and prices\n"
					+ "2. Display a minimum spanning tree for the service routes based on distances\n"
					+ "3. Search for the shortest or cheapest path between two cities\n"
					+ "4. Enter a dollar amount to see all trips whose cost is less than or equal to that amount.\n"
					+ "5. Add a new route to the schedule\n"
					+ "6. Remove a route from the schedule\n"
					+ "7. Save changes to file and Quit");
			
			
		    int i = sc.nextInt();
			
		    switch(i){
		    	case 1: showRoutes();
		    		break;
		    	case 2: displayMST();
		    		break;
		    	case 3: searchForShortestCheapest();
	    			break;
		    	case 4: routesCheaperThan();
	    			break;
		    	case 5: addRoute();
	    			break;
		    	case 6: removeRoute();
	    			break;
		    	case 7: saveQuit();
	    			break;
	    			
		    }
		    

		}
		
	}
	

	public static void showRoutes(){
		System.out.println(myGraph.showAllRoutes());
	}
	
	public static void displayMST(){
		PrimMST mst = new PrimMST(myGraph);
        for (Edge e : mst.edges()) {
            System.out.println(e);
        }
	}
	
	public static void searchForShortestCheapest(){
		System.out.println("Please enter the source city of your search: ");
		String city1 = sc.next();
		System.out.println("Please enter the destination city of your search: ");
		String city2 = sc.next();
		System.out.println("Please enter the number corresponding to one of the following options:\n"
				+ "1. Search for the shortest path based on total miles\n"
				+ "2. Search for the cheapest path based on price\n"
				+ "3. Search for the shortest path based on number of stops\n");
		
		int i = sc.nextInt();
		int s = myGraph.getCityNum(city1);
		int d = myGraph.getCityNum(city2);
	    switch(i){
	    	case 1: 
	    		
	    		
	    		// compute shortest paths
	            DijkstraUndirectedSPDistance spd = new DijkstraUndirectedSPDistance(myGraph, s);


	            // print shortest path
	        //    for (int t = 0; t < myGraph.V(); t++) {
	                if (spd.hasPathTo(d)) {
	                //    System.out.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
	                    for (Edge e : spd.pathTo(d)) {
	                    	System.out.print(e + "   ");
	                    }
	                    System.out.println();
	                }
	                else {
	                	System.out.printf("%s to %s         no path\n", city1, city2);
	                }
	       //     }
	    		break;
	    	case 2:
    		// compute shortest paths
            DijkstraUndirectedSPPrice spp = new DijkstraUndirectedSPPrice(myGraph, s);


            // print shortest path
        //    for (int t = 0; t < myGraph.V(); t++) {
                if (spp.hasPathTo(d)) {
                //    System.out.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                    for (Edge e : spp.pathTo(d)) {
                    	System.out.print(e + "   ");
                    }
                    System.out.println();
                }
                else {
                	System.out.printf("%s to %s         no path\n", city1, city2);
                }
       //     }
    		break;
	    	
	    	case 3: 
	    		BreadthFirstPaths bfs = new BreadthFirstPaths(myGraph, s);

	    //        for (int v = 0; v < myGraph.V(); v++) {
	                if (bfs.hasPathTo(d)) {
	                	System.out.printf("%s to %s (%d stops): ", city1, city2, bfs.distTo(d));
	                    for (int x : bfs.pathTo(d)) {
	                        if (x == s) System.out.print(myGraph.getCityName(x));
	                        else        System.out.print("-" + myGraph.getCityName(x));
	                    }
	                    System.out.println();
	                }

	                else {
	                	System.out.printf("%d to %d (-):  not connected\n", city1, city2);
	                }

	       //     }
    			break;
	    }
	}
	
	public static void routesCheaperThan(){
		
		System.out.println("Please enter a price: ");
		double price = sc.nextDouble();
		
	    int count = 0;
        for(int s = 0; s < myGraph.V(); s++){
            // print shortest path
        	DijkstraUndirectedSPPrice spd = new DijkstraUndirectedSPPrice(myGraph, s);
            
        	for (int d = 0; d < myGraph.V(); d++) {
        		
        		if (spd.hasPathTo(d) && d != s && s < d) {   
             //       System.out.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));				 && totalCost < price
        			String lastCityFrom = "";
        			String lastCityTo = "";
        			StringBuilder sb = new StringBuilder();
        			if(spd.distTo(d) <= price){
        				count += 2;
        				System.out.print("Cost: " + "$" + spd.distTo(d) + " Path: ");
        				for (Edge e : spd.pathTo(d)) {
        					System.out.print(e.vCity() + " " + "$" + e.price() + " ");
        					lastCityFrom = e.wCity();
        					sb.insert(0, "$" + e.price() + " " + e.vCity() + " " );
        					
        				}
        				System.out.println(lastCityFrom);
        				System.out.print("Cost: " + "$" + spd.distTo(d) + " Path: ");
        				System.out.println(lastCityFrom + " " + sb);
        			}
        		}
                
            }
        }
        System.out.println(count);
	}
	
	public static void addRoute(){
		System.out.println("Please enter the vertex of the source city of the route: ");
		int city1Num = sc.nextInt() - 1;
		System.out.println("Please enter the vertex of the destination city of the route: ");
		int city2Num = sc.nextInt() - 1;
		System.out.println("Please enter the distance of the route: ");
		int distance = sc.nextInt();
		System.out.println("Please enter the price of the route: ");
		double price = sc.nextDouble();
		
		String city1Name = myGraph.getCityName(city1Num);
		String city2Name = myGraph.getCityName(city2Num);
		
		Edge newEdge = new Edge(city1Num, city1Name, city2Num, city2Name, distance, price);
		myGraph.addEdge(newEdge);
	}
	
	public static void removeRoute() throws IOException{
		System.out.println("Please enter the vertex of the source city of the route: ");
		int city1Num = sc.nextInt() ;
		System.out.println("Please enter the vertex of the destination city of the route: ");
		int city2Num = sc.nextInt() ;
		
		File tempFile = new File("myTempFile.txt");
		File myFile = new File(inputFile);
		
		String line;
		String routeToRemove = city1Num + " " + city2Num;
		
		FileWriter fw = new FileWriter(tempFile);
        BufferedWriter bw = new BufferedWriter(fw);
        
        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);

        while((line = br.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = line.trim();
            if(trimmedLine.length() > routeToRemove.length()){
            	if(trimmedLine.substring(0 , routeToRemove.length()).equals(routeToRemove)) continue;
            }
            bw.write(line + System.getProperty("line.separator"));
        }
        br.close();
        bw.close(); 
        
  /*      boolean success = tempFile.renameTo(myFile);
        if(!success)
        	System.out.println("Didnt rename");
  */
        BufferedReader reader = new BufferedReader(new FileReader("myTempFile.txt"));
		myGraph = new EdgeWeightedGraph(reader);
        reader.close();
	}
	
	public static void saveQuit() throws IOException{
		FileWriter fw = new FileWriter(inputFile);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(myGraph.getFileString());
        bw.close(); 
	}

}

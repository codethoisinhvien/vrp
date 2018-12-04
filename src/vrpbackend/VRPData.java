/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrpbackend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *
 * @author Phongthien
 */
public class VRPData {
     private int numberOfTrucks;
    private int graphDimension;// số luowmhj khách hàng
    private int vehicleCapacity;// khách hàng 

    private Node nodes[];
    public double[][] CostMatrix;

    public VRPData(final String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

            String line;

            boolean demand = false;

            while ((line = br.readLine()) != null) {
                if (line.contains("DIMENSION")) {
                    this.graphDimension = Integer.parseInt(line.split(":")[1].trim());
                    this.nodes = new Node[graphDimension];
                } else if (line.contains("CAPACITY")) {
                    vehicleCapacity = Integer.parseInt(line.split(":")[1].trim());
                    break;
                }
            }
 line = br.readLine();
while ((line = br.readLine()) != null) {
                 if (line.contains("DEPOT_SECTION"))
                    break;
                if (line.contains("DEMAND_SECTION")) {
                    demand = true;
                   
                }
              if(!demand){
                 
                 String []tokens = line.replaceAll("\\s","-").split("-");
                  
                 nodes[Integer.parseInt(tokens[0])-1]= new Node(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),Integer.parseInt(tokens[0]));
                     
                }
              else{
                  if(!line.contains("DEMAND_SECTION")){
                  String []tokens = line.replaceAll("\\s","-").split("-");
                  
                 nodes[Integer.parseInt(tokens[0])-1].setDemand(Integer.parseInt(tokens[1]));
                     
                  }
              }
               
                this.numberOfTrucks= graphDimension;
}
        

           
        
    }
     public VRPData(final String file, final int numberOfTrucks) throws IOException {
        this(file);
        this.numberOfTrucks = numberOfTrucks;
        
    }

public void createCostMatrix(){
      CostMatrix = new double[this.getGraphDimension()][this.getGraphDimension()];
      for (int i = 0; i < this.getGraphDimension(); i++) {
            for (int j = i + 1; j < this.getGraphDimension(); j++) 
            {                                      
                CostMatrix[i][j] = this.getNode(i).distanceTo(this.getNode(j));
                CostMatrix[j][i] = this.getNode(i).distanceTo(this.getNode(j));
                
            }
        }
     ; 
     
  }
    public int getNumberOfTrucks() {
        return numberOfTrucks;
    }

    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

    public Node getNode(final int p) {
        return nodes[p];
    }

    public int getGraphDimension() {
        return graphDimension;
    }

    public int getTotalDemand() {
        return Arrays.stream(this.nodes).mapToInt(Node::getDemand).sum();
    }
    
    
    
}

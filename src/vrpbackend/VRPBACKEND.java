/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrpbackend;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Phongthien
 */
public class VRPBACKEND {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CloneNotSupportedException, IOException {
     VRPData a= new VRPData("C:\\Users\\Phongthien\\Desktop\\Vrp-Set-X\\X\\X-n120-k6.vrp");
     //"C:\\Users\\Phongthien\\Desktop\\Vrp-Set-X\\X\\X-n115-k10.vrp"
     //C:\\Users\\Phongthien\\Desktop\\Vrp-Set-X\\X\\X-n957-k87.vrp
     a.createCostMatrix();
     VRP b = new VRP();
     b.capacity=a.getVehicleCapacity();
     b.NoCustomers=a.getGraphDimension();
     for(int i=0;i<a.getGraphDimension();i++){
         b.nodes.add(a.getNode(i));
        
     }
      
      b.GreedySolution(a.CostMatrix);
     b.IntraLocalSearch(a.CostMatrix);
       b.ILS(a.CostMatrix);
      b.show();
        System.out.println(b.cost);
      
    }
}

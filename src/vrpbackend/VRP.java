/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrpbackend;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.print.Collation;

/**
 *
 * @author Phongthien
 */
public class VRP {
  public  ArrayList<Node> nodes = new ArrayList<>();
  public  Roads roads = new Roads();
  public  Roads bestSolution = new Roads();
  public  double bestSolutionCost = 0;
  public  int NoCustomers;// số khách
  public  int capacity    ;// tải trọng
  public  double cost  ;  
 
  public boolean isNotCustomerExist(){
      for (int i = 1; i < nodes.size(); i++)
        {
            if (!nodes.get(i).isRouted)
                return true;
        }
     
        return false;
  }
   public void GreedySolution( double[][] CostMatrix) {
      roads.roads.clear();
      
        double CandCost,EndCost;
         int vehIndex = 0;
          roads.add(new Route(capacity));
          roads.get(vehIndex).route.add(nodes.get(0));
        while (this.isNotCustomerExist()) {
           
            int CustIndex = 0;
            Node Candidate = null;
            double minCost = (float) Double.MAX_VALUE;
            
            // bat dau duong moi
            for (int i = 1; i <NoCustomers; i++) 
                if (nodes.get(i).isRouted == false) {
                    if (roads.get(vehIndex).CheckIfFits(nodes.get(i).getDemand())) {
                        // chưa đủ tải trong 
                        CandCost = CostMatrix[roads.get(vehIndex).CurLoc][i];
                        if (minCost >= CandCost) {
                            minCost = CandCost;
                            
                            CustIndex = i;
                          Candidate = i>=nodes.size()?   null :nodes.get(i);
                           
                        }
                    }
                }
            

            if ( Candidate  == null)
            {
                //không khách hàng đủ ddieuf kien nưa
               
                    if (roads.get(vehIndex).CurLoc != 0) {
                        
                        EndCost = CostMatrix[roads.get(vehIndex).CurLoc][0];
                        roads.get(vehIndex).addNode(nodes.get(0));
                        this.cost +=  EndCost;
                        System.out.println(roads.get(vehIndex).load);
                       
                    }
                   vehIndex = vehIndex+1;
                   
                   roads.add(new Route(capacity));
              if(roads.get(vehIndex).route.isEmpty()){
                roads.get(vehIndex).addNode(nodes.get(0));
               }
                     ;
                                  // kêt thuc 1 route
                   
             // chuyên sang route
             
            }
            else
            {
                // co khách
                roads.get(vehIndex).addNode(Candidate);
                this.cost += minCost;
                  nodes.get(CustIndex).isRouted = true;
                
            }
            
        }
        // trở về kho
        EndCost = CostMatrix[roads.get(vehIndex).CurLoc][0];
        roads.get(vehIndex).route.add(nodes.get(0));
        this.cost +=  EndCost;
      
    }
public void IntraLocalSearch(double[][] CostMatrix){
         Route rt;
        double BestNCost,AtCost;
        
        int SwapIndexA = -1, SwapIndexB = -1, SwapRoute =-1;

        int MAX_ITERATIONS = 1000000;
        int iteration_number= 0;

        boolean Termination = false;

        while (!Termination)
        {
            iteration_number++;
            BestNCost = Double.MAX_VALUE;

            for (int VehIndex = 0; VehIndex < this.roads.roads.size(); VehIndex++) {
                rt = this.roads.get(VehIndex);
                int RoutLength = rt.route.size();

                for (int i = 1; i < RoutLength - 1; i++) { 

                    for (int j =  1 ; (j < RoutLength-1); j++) {

                        if ( ( j != i ) && (j != i-1) ) { 

                            double MinusCost1 = CostMatrix[rt.route.get(i-1).getIndex()-1][rt.route.get(i).getIndex()-1];
                            double MinusCost2 =  CostMatrix[rt.route.get(i).getIndex()-1][rt.route.get(i+1).getIndex()-1];
                            double MinusCost3 =   CostMatrix[rt.route.get(j).getIndex()-1][rt.route.get(j+1).getIndex()-1];
                          

                            double AddedCost1 =  CostMatrix[rt.route.get(i-1).getIndex()-1][rt.route.get(i+1).getIndex()-1];
                        
                            double AddedCost2 = CostMatrix[rt.route.get(j).getIndex()-1][rt.route.get(i).getIndex()-1];
                                 
                            double AddedCost3 = CostMatrix[rt.route.get(i).getIndex()-1][rt.route.get(j+1).getIndex()-1];
                          
                            AtCost = AddedCost1 + AddedCost2 + AddedCost3
                                    - MinusCost1 - MinusCost2 - MinusCost3;

                            if (AtCost < BestNCost) {
                                BestNCost = AtCost;
                                SwapIndexA  = i;
                                SwapIndexB  = j;
                                SwapRoute = VehIndex;

                            }
                        }
                    }
                }
            }

            if (BestNCost < 0) {

                rt = this.roads.get(SwapRoute);

                Node SwapNode = rt.route.get(SwapIndexA);

                rt.route.remove(SwapIndexA);

                if (SwapIndexA < SwapIndexB)
                { rt.route.add(SwapIndexB, SwapNode); }
                else
                { rt.route.add(SwapIndexB+1, SwapNode); }

               
                this.cost  += BestNCost;
            }else{
               Termination = true; 
            }
            

            if (iteration_number == MAX_ITERATIONS)
            {
                Termination = true;
            }
        }
 }
public void InterLocalSearch(double[][] CostMatrix){
            Route RouteFrom;
            Route RouteTo;

        int MovingNodeDemand = 0;
        int IndexDemand = 0;
        int VehIndexFrom,VehIndexTo;
        double BestNCost,NeigthboorCost;

        int SwapIndexA = -1, SwapIndexB = -1, SwapRouteFrom =-1, SwapRouteTo=-1;

        int MAX_ITERATIONS = 10000;
        int iteration_number= 0;

        boolean Termination = false;

        while (!Termination)
        {
            iteration_number++;
            BestNCost = Double.MAX_VALUE;

            for (VehIndexFrom = 0;  VehIndexFrom < this.roads.roads.size();  VehIndexFrom++) {
                RouteFrom = this.roads.roads.get(VehIndexFrom);
                int RoutFromLength = RouteFrom.route.size();
                for (int i = 1; i <  RouteFrom.route.size() - 1; i++) {

                    for (VehIndexTo = 0; VehIndexTo <this.roads.roads.size(); VehIndexTo++) {
                        RouteTo =  this.roads.roads.get(VehIndexTo);
                        int RouteTolength = RouteTo.route.size();
                        for (int j = 1; (j < RouteTo.route.size() - 1); j++) {

                            MovingNodeDemand = RouteFrom.route.get(i).getDemand();
                            if (   this.roads.roads.get(VehIndexTo).CheckIfFits(MovingNodeDemand) )
                            {
                                if (( (VehIndexFrom == VehIndexTo) && ((j == i) || (j == i - 1)) ) == false)  // Not a move that Changes solution cost
                                {
                                    double MinusCost1 = CostMatrix[RouteFrom.route.get(i - 1).getIndex()-1][RouteFrom.route.get(i).getIndex()-1];
                                    double MinusCost2 = CostMatrix[RouteFrom.route.get(i).getIndex()-1][RouteFrom.route.get(i + 1).getIndex()-1];
                                    double MinusCost3 = CostMatrix[RouteTo.route.get(j).getIndex()-1][RouteTo.route.get(j + 1).getIndex()-1];

                                    double AddedCost1 = CostMatrix[RouteFrom.route.get(i - 1).getIndex()-1][RouteFrom.route.get(i + 1).getIndex()-1];
                                    double AddedCost2 = CostMatrix[RouteTo.route.get(j).getIndex()-1][RouteFrom.route.get(i).getIndex()-1];
                                    double AddedCost3 = CostMatrix[RouteFrom.route.get(i).getIndex()-1][RouteTo.route.get(j + 1).getIndex()-1];

                                    NeigthboorCost = AddedCost1 + AddedCost2 + AddedCost3
                                            - MinusCost1 - MinusCost2 - MinusCost3;

                                    if (NeigthboorCost < BestNCost) {
                                        BestNCost = NeigthboorCost;
                                        SwapIndexA = i;
                                        SwapIndexB = j;
                                        SwapRouteFrom = VehIndexFrom;
                                        SwapRouteTo = VehIndexTo;
                                        IndexDemand=MovingNodeDemand;

                                    }
                                }
                            }
                        }
                        
                    }
                    
                }
            }

            if (BestNCost < 0) {

                RouteFrom = this.roads.roads.get(SwapRouteFrom);
                RouteTo = this.roads.roads.get(SwapRouteTo);
                this.roads.roads.set(SwapRouteFrom, null);
                this.roads.roads.set(SwapRouteTo, null);

                Node SwapNode = RouteFrom.route.get(SwapIndexA);

                RouteFrom.route.remove(SwapIndexA);

                if (SwapRouteFrom == SwapRouteTo) {
                    if (SwapIndexA < SwapIndexB) {
                        RouteTo.route.add(SwapIndexB, SwapNode);
                    } else {
                        RouteTo.route.add(SwapIndexB + 1, SwapNode);
                    }
                }
                else
                {
                    RouteTo.route.add(SwapIndexB+1, SwapNode);
                }

                   this.roads.roads.set(SwapRouteFrom, RouteFrom);
                    this.roads.roads.set(SwapRouteTo, RouteTo);
                    
                this.roads.roads.get(SwapRouteFrom).load -= IndexDemand;

            
                this.roads.roads.get(SwapRouteTo).load  += IndexDemand;

                
                this.cost  += BestNCost;
            }
            
            if (iteration_number == MAX_ITERATIONS)
            {
                Termination = true;
            }
        }
     

        
 }



public void ILS(double[][] CostMatrix) throws CloneNotSupportedException{
    int i=0;
    this.copy();
    long startTime = System.currentTimeMillis();
     long endTime = System.currentTimeMillis();
   while(endTime-startTime<180000){
       for(int j=0;j<roads.roads.size();j++){
           if(j+1<roads.roads.size())
            Collections.swap(roads.roads, j, j+1);
        for(int o=1;o < roads.roads.get(j).route.size()-3;o++){
              
           double minusCost1 = CostMatrix[roads.roads.get(j).route.get(o-1).getIndex()-1][roads.roads.get(j).route.get(o).getIndex()-1];
           double minusCost2= CostMatrix[roads.roads.get(j).route.get(o+1).getIndex()-1][roads.roads.get(j).route.get(o+2).getIndex()-1];
              ;    
                   Collections.swap(roads.roads.get(j).route, o, o+1);
                  
          
           double addCost1 = CostMatrix[roads.roads.get(j).route.get(o-1).getIndex()-1][roads.roads.get(j).route.get(o).getIndex()-1];
           double addCost2= CostMatrix[roads.roads.get(j).route.get(o+1).getIndex()-1][roads.roads.get(j).route.get(o+2).getIndex()-1];

        this.cost=this.cost-minusCost2-minusCost1+addCost1+addCost2;
        }
       
        this.InterLocalSearch(CostMatrix);
        
       // this.IntraLocalSearch(CostMatrix);
       if(this.cost<this.bestSolutionCost){
           this.copy();
       }
       System.out.println(this.cost);
       endTime = System.currentTimeMillis();
       if(endTime-startTime>180000){
           break;
       }
       }
      
   }
}
   public void  copy() throws CloneNotSupportedException{
       bestSolution.roads.clear();
       for(int i=0;i<this.roads.roads.size();i++){
       bestSolution.roads.add(this.roads.roads.get(i).clone());
   }
       bestSolutionCost=this.cost;
   }
    public void  show( ){
      for (int i=0;i<bestSolution.roads.size();i++){
          bestSolution.roads.get(i).showRoad();
      }
        System.out.println(bestSolutionCost);
    }

  
    
}

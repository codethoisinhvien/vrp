/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrpbackend;

import java.util.ArrayList;

/**
 *
 * @author Phongthien
 */
public class Route {
    public ArrayList<Node> route;
    public int load=0;// lượng hàng đang có
    public int capacity ;// tải trong 
    public int totalCost=0;// tổng quảng đường đi
    public int CurLoc=0;// vi trí hiện tại 
    public Route(int capacity) {
        this.route = new ArrayList<Node>();
        this.capacity =capacity;
    }
    public void showRoad(){
        String a ="";
        for (int i = 0; i < route.size(); i++) {
            a+=route.get(i).getIndex()+"->";
        }
        System.out.println(a+":  "+load);
    }
    public boolean CheckIfFits(int dem) 
    {
        return ((load + dem <= capacity));
    }
    public boolean CheckSwapNode(int in,int out){
        return (capacity>=load+in-out);
    }
    public  void addNode(Node a){
        this.route.add(a);
        this.load+=a.getDemand();
        this.CurLoc=a.getIndex()-1;
    }
    public  Route clone() throws CloneNotSupportedException{
        Route  clone = new Route(capacity);
        for (int i = 0; i <route.size() ; i++) {
            clone.addNode(route.get(i).clone());
        }
        clone.CurLoc=this.CurLoc;
        clone.load= this.load;
        clone.totalCost = this.totalCost;
       return clone;
    }
}

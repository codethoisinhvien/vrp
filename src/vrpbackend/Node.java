/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vrpbackend;

/**
 *
 * @author Phongthien
 */
public class Node implements Cloneable{
    private int x;
    private int y;
    private int index;
    private int demand;
    public int futureDemand;
    public  boolean isRouted;
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
   


    Node(int x, int y, int index) {
        this(x, y, index, 0);
    }

    Node(final int x, final int y, final int index, final int demand) {
        this.x = x;
        this.y = y;
        this.demand = demand;
        this.index = index;
        this.isRouted=false;
                
       
    }
    
    public double distanceTo( Node node) {
        return Math.sqrt(Math.pow((node.x - this.x), 2) + Math.pow((node.y - this.y), 2));
    }

    public int getDemand() {
        return demand;
    }


    public int getIndex() {
        return index;
    }

    public void setDemand(int demand) {
        this.demand = demand;
         this.futureDemand=demand;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ") - Demand: " + this.demand;
    }
    public  Node clone() throws CloneNotSupportedException{
        return (Node) super.clone();
    }
    
}

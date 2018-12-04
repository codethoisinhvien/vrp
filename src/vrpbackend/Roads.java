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
public class Roads {
    ArrayList<Route> roads;
    public  double cost=0;
    public Roads() {
        roads = new ArrayList();
    }
    public  Roads clone () throws CloneNotSupportedException{
        Roads clone = new Roads();
        for(int i=0;i<roads.size();i++){
            clone.roads.add(roads.get(i).clone());
        }
        clone.cost=cost;
        return clone;
    };
    public  void add(Route e){
        roads.add(e);
    }
     public  Route get(int i){
      return roads.get(i);
    }
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.meps;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "dummyManagedBean")
@RequestScoped
public class dummyManagedBean {

    /**
     * Creates a new instance of dummyManagedBean
     */
    public dummyManagedBean() {
    }
    
}

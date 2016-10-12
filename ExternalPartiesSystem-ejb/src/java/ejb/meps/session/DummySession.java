/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.meps.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class DummySession implements DummySessionLocal {
    @PersistenceContext(unitName = "ExternalPartiesSystem-ejbPU")
    private EntityManager em;

}

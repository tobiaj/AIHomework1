package Agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class CuratorAgent extends Agent {

  public void setup() {
      System.out.println("The Curator agent " + getLocalName() + " has started");

      registerService();



  }

    private void registerService() {

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() );

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("curatorAgent");
        serviceDescription.setName(getName()); //getName() will return the name of which you start the agent
        dfd.addServices(serviceDescription);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}

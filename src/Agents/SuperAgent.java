package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class SuperAgent extends Agent {

    public SuperAgent(){

    }

    public static AID getCuratorAID(Agent agent) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("curatorAgent");
        template.addServices(serviceDescription);

        try {
            DFAgentDescription[] result = DFService.search(agent, template);
            if (result.length > 0) {
                return result[0].getName();
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        return null;
    }

}

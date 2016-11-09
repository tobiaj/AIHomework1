package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SenderBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class ProfilerAgents extends Agent{
    private String age;
    private String occupation;
    private String gender;
    private String interests;
    private String[] visitedItems;

    @Override
    protected void setup() {

        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();
                serviceDescription.setType("virtual-tou");

                template.addServices(serviceDescription);

                try {
                    DFAgentDescription [] result = DFService.search(myAgent, template);
                    System.out.println(result[0].getName());

                } catch (FIPAException e) {
                    e.printStackTrace();
                }

            }
        });

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent("Tobbe");
        message.addReceiver(new AID("receiver", AID.ISLOCALNAME));
        addBehaviour(new SenderBehaviour(this, message));

    }

}

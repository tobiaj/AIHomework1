package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class TourGuideAgent extends Agent{

    @Override
    protected void setup(){

        //calls the parent constructor with no arguments
        super.setup();
        System.out.println("The tour guide agent " + getLocalName() + " has started");
        System.out.println(getName());

        registerService();

        waitForTourRequestMessages();



        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message = receive();
                    if (message != null){
                        System.out.println( " - " +
                                myAgent.getLocalName() + " <- " +
                                message.getContent());
                    }
                    else {
                        block();
                    }
                }
        });

    }

    private void waitForTourRequestMessages() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() );

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("virtual-tour");
        serviceDescription.setName("tour-guide-agent");
        dfd.addServices(serviceDescription);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private void registerService() {
    }

}

package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.SimpleAchieveREInitiator;
import jade.proto.states.MsgReceiver;

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


    }

    private void waitForTourRequestMessages() {

        System.out.println("Kommer jag hit och väntar på ett meddelande");
        MessageTemplate tourGuideRequest = MessageTemplate.MatchOntology("tour");
        addBehaviour(new MsgReceiver(this, tourGuideRequest, Long.MAX_VALUE, null, null) {
            protected void handleMessage(ACLMessage message) {

                System.out.println("Tour guide agent received a tour request from the profiler agent");

                AID AID = getCuratorAID();

                ACLMessage requestToCurator = new ACLMessage(ACLMessage.REQUEST);
                requestToCurator.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                requestToCurator.setOntology("requestCurator");
                message.addReceiver(AID);
                requestToCurator.setContent(message.getContent());



                addBehaviour(new SimpleAchieveREInitiator(TourGuideAgent.this, requestToCurator){
                    protected void handleInform(ACLMessage message) {

                        System.out.println("Received artifacts???? from the curator");
                        ACLMessage reply = requestToCurator.createReply();
                        reply.setPerformative(ACLMessage.INFORM);

                        reply.setContent("artifacts");

                        send(reply);


                    }

                });

            }


            private AID getCuratorAID() {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();
                serviceDescription.setType("curatorAgent");
                template.addServices(serviceDescription);

                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    if (result.length > 0) {
                        return result[0].getName();
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }

                return null;
            }

        });

    }


    private void registerService() {

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() );

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("TourGuideAgent");
        serviceDescription.setName(getName());
        dfd.addServices(serviceDescription);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

}

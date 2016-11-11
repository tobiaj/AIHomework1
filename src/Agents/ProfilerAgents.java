package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.ArrayList;
import userAndArtifacts.User;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class ProfilerAgents extends Agent{
    private User user;
    private ArrayList tourGuides;

    @Override
    protected void setup() {
        super.setup();

        System.out.println("The Profiler guide agent " + getLocalName() + " has started");
        System.out.println(getName());


        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("TourGuideAgent");
        SearchConstraints search = new SearchConstraints();

        template.addServices(serviceDescription);

        Subscribe subscribe = new Subscribe(this, DFService.createSubscriptionMessage(this, getDefaultDF(), template, search));

        addBehaviour(subscribe);

    }

    private void requestATour() {
        ACLMessage requestATourGuideMessage = new ACLMessage(ACLMessage.REQUEST);
        requestATourGuideMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); //Varför använder alla den här, det funkar ju utan
        System.out.println("tourGuides: " + tourGuides.get(0));
        requestATourGuideMessage.addReceiver((AID) tourGuides.get(0));
        requestATourGuideMessage.setContent("tobbe");//This should be the user.
        requestATourGuideMessage.setOntology("tour");//Message that you match on in tourGuide
        send(requestATourGuideMessage);
    }

    private void addTourGuide(DFAgentDescription[] result) {
        tourGuides = new ArrayList();
        for (int i = 0; i < result.length ; i++){
            tourGuides.add(result[i].getName());
        }
    }


    //Den här behövs kanske inte?
    private void findTourGuides() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("TourGuideAgent");

        template.addServices(serviceDescription);

        try {
            DFAgentDescription [] result = DFService.search(this, template);

            if (result.length >0){
                tourGuides = new ArrayList();
                for (int i = 0; i < result.length ; i++){
                    tourGuides.add(result[i].getName());
                }

            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public class Subscribe extends SubscriptionInitiator {

        public Subscribe(Agent agent, ACLMessage message){
            super(agent, message);

        }

        protected void handleInform(ACLMessage inform){
            try {
                DFAgentDescription[] result = DFService.decodeNotification(inform.getContent());
                if (result.length > 0) {
                    System.out.println("Profiler agent " + getLocalName() + " received a subscription message from DF with name " + getDefaultDF());
                    addTourGuide(result);

                    requestATour();
                }
            } catch (FIPAException e) {
                e.printStackTrace();
            }

        }


    }

}

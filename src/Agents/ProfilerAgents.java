package Agents;

import com.sun.org.apache.regexp.internal.RE;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREInitiator;
import jade.proto.SubscriptionInitiator;
import jade.proto.states.MsgReceiver;
import jade.util.leap.ArrayList;
import userAndArtifacts.User;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class ProfilerAgents extends SuperAgent{
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

        MessageTemplate messageTemplate = MessageTemplate.MatchOntology("reply");

        ReplyReceiver replyReceiver = new ReplyReceiver(this, messageTemplate, Long.MAX_VALUE, null, null);

        addBehaviour(replyReceiver);

    }

    private void requestATour() {
        ACLMessage requestATourGuideMessage = new ACLMessage(ACLMessage.REQUEST);
        requestATourGuideMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); //Varför använder alla den här, det funkar ju utan
        System.out.println("tourGuides: " + tourGuides.get(0));
        requestATourGuideMessage.addReceiver((AID) tourGuides.get(0));
        requestATourGuideMessage.setContent("tobbe");//This should be the user.
        requestATourGuideMessage.setOntology("tour");//Message that you match on in tourGuide


        TickerBehaviour messageSpam = new TickerBehaviour(ProfilerAgents.this, 10000) {
            @Override
            protected void onTick() {
                send(requestATourGuideMessage);
            }
        };

        addBehaviour(messageSpam);

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
                    System.out.println("Profiler agent " + getLocalName() + " received a subscription message from SuperAgent with name " + getDefaultDF());
                    addTourGuide(result);

                    requestATour();

                }
            } catch (FIPAException e) {
                e.printStackTrace();
            }

        }


    }

    public class ReplyReceiver extends MsgReceiver{

        public ReplyReceiver(Agent myAgent, MessageTemplate messageTemplate, long deadline, DataStore ds, Object msgKey) {
            super(myAgent, messageTemplate, deadline, ds, msgKey);

        }

        @Override
        protected void handleMessage(ACLMessage msg) {
            System.out.println("Kommer jag hitt till handleMessage i reply receiver ");
            System.out.println(msg.getContent());

            AID AID = getCuratorAID(ProfilerAgents.this);

            System.out.println("Hittar jag curator nu i profiler agents :" + AID);
            ACLMessage requestToCurator = new ACLMessage(ACLMessage.REQUEST);
            requestToCurator.setOntology("artifactsInfo");
            requestToCurator.addReceiver(AID);
            requestToCurator.setContent("Meddelandet från profiler agent som ska till curator agent");

            GetArtifacts getArtifacts = new GetArtifacts(ProfilerAgents.this, requestToCurator);

            addBehaviour(getArtifacts);
        }


    }


    public class GetArtifacts extends SimpleAchieveREInitiator {

        public GetArtifacts(Agent agent, ACLMessage requestToCurator) {
            super(agent, requestToCurator);
        }

        @Override
        protected ACLMessage prepareRequest(ACLMessage msg) {
            System.out.println("kommer jag hit till get Artifacts");
            return super.prepareRequest(msg);
        }

        @Override
        protected void handleInform(ACLMessage msg){
            System.out.println("KLARA?");
            System.out.println(msg.getContent());
            super.handleInform(msg);


        }


    }



}

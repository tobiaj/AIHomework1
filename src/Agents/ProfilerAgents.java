package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREInitiator;
import jade.proto.SubscriptionInitiator;
import jade.proto.states.MsgReceiver;
import jade.util.leap.ArrayList;
import userAndArtifacts.User;

import java.io.IOException;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class ProfilerAgents extends SuperAgent{
    private User user;
    private ArrayList tourGuides;
    private ArrayList iDsOfArtifacts;
    private ArrayList listOfArtifactsWithInformation;

    @Override
    protected void setup() {
        super.setup();

        System.out.println("The Profiler guide agent " + getLocalName() + " has started");
        System.out.println(getName());

        ParallelBehaviour paralell = new ParallelBehaviour();

        paralell.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                createSubscription();

            }
        });

        paralell.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                user = new User();
            }
        });

        addBehaviour(paralell);

        MessageTemplate messageTemplate = MessageTemplate.MatchOntology("reply");

        ReplyReceiver replyReceiver = new ReplyReceiver(this, messageTemplate, Long.MAX_VALUE, null, null);

        addBehaviour(replyReceiver);

    }

    private void createSubscription() {

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
        //requestATourGuideMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); //Varför använder alla den här, det funkar ju utan
        System.out.println("tourGuides: " + tourGuides.get(0));
        requestATourGuideMessage.addReceiver((AID) tourGuides.get(0));//BEHÖVER FIXAS ?????
        try {
            requestATourGuideMessage.setContentObject(user);//This should be the user.
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        protected void handleMessage(ACLMessage message) {
            System.out.println("Kommer jag hitt till handleMessage i reply receiver ");

            try {
                iDsOfArtifacts = (ArrayList) message.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            AID AID = getCuratorAID(ProfilerAgents.this);

            System.out.println("Hittar jag curator nu i profiler agents :" + AID);
            ACLMessage requestToCurator = new ACLMessage(ACLMessage.REQUEST);
            requestToCurator.setOntology("artifactsInfo");
            requestToCurator.addReceiver(AID);
            try {
                requestToCurator.setContentObject(iDsOfArtifacts);
            } catch (IOException e) {
                e.printStackTrace();
            }

            GetArtifacts getArtifacts = new GetArtifacts(ProfilerAgents.this, requestToCurator);

            addBehaviour(getArtifacts);
        }

        @Override
        public int onEnd() {
            myAgent.addBehaviour(this);
            return super.onEnd();
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
        protected void handleInform(ACLMessage message){
            super.handleInform(message);
            System.out.println("KLARA?");

            try {
                listOfArtifactsWithInformation = (ArrayList) message.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            System.out.println("Det här innehåller listan i slutet \n");
            System.out.println(listOfArtifactsWithInformation);

        }
/*
        @Override
        public int onEnd() {
            myAgent.addBehaviour(this);
            return super.onEnd();
        }*/
    }



}

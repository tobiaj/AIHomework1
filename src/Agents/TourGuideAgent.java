package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
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
public class TourGuideAgent extends SuperAgent {

    @Override
    protected void setup() {

        //calls the parent constructor with no arguments
        super.setup();
        System.out.println("The tour guide agent " + getLocalName() + " has started");
        System.out.println(getName());

        registerService();

        waitForTourRequestMessages();

    }

    private void waitForTourRequestMessages() {

        //System.out.println("Kommer jag hit och väntar på ett meddelande");
        MessageTemplate tourGuideRequest = MessageTemplate.MatchOntology("tour");

        MessageReceiver messageReceiver = new MessageReceiver(this, tourGuideRequest, Long.MAX_VALUE, null, null);

        addBehaviour(messageReceiver);

    }


    private void registerService() {

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

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


    class MessageReceiver extends MsgReceiver {
        private Agent myAgent;
        private MessageTemplate messageTemplate;
        private long deadline;
        private DataStore ds;
        private Object msgKey;


        public MessageReceiver(Agent myAgent, MessageTemplate messageTemplate, long deadline, DataStore ds, Object msgKey) {
            super(myAgent, messageTemplate, deadline, ds, msgKey);
            this.messageTemplate = messageTemplate;
            this.myAgent = myAgent;
            this.deadline = deadline;
            this.ds = ds;
            this.msgKey = msgKey;
        }

        public void handleMessage(ACLMessage message) {

            System.out.println("Tour guide agent received a tour request from the profiler agent");

            AID AID = getCuratorAID(TourGuideAgent.this);

            System.out.println("Hittar jag curator? :" + AID);
            ACLMessage requestToCurator = new ACLMessage(ACLMessage.REQUEST);
            requestToCurator.setOntology("artifactsRequest");
            requestToCurator.addReceiver(AID);
            requestToCurator.setContent(message.getContent());

            HandleTourRequestMessage handle = new HandleTourRequestMessage(TourGuideAgent.this, requestToCurator, message);
            addBehaviour(handle);


        }


        @Override
        public int onEnd() {
            myAgent.addBehaviour(this);
            return super.onEnd();
        }
    }

    public class HandleTourRequestMessage extends SimpleAchieveREInitiator {
        private ACLMessage originalMessage;

        public HandleTourRequestMessage(Agent agent, ACLMessage requestToCurator, ACLMessage originalMessage) {
            super(agent, requestToCurator);
            this.originalMessage = originalMessage;
        }

        @Override
        protected ACLMessage prepareRequest(ACLMessage msg) {
            System.out.println("kommer jag hit till prepare i handle tour request ");
            System.out.println(msg.getContent());
            System.out.println(msg.getOntology());
            return super.prepareRequest(msg);
        }

        @Override
        protected void handleInform(ACLMessage msg){

            super.handleInform(msg);

            System.out.println(msg.getContent() + " content from curator");

            System.out.println("Received artifacts???? from the curator");
            ACLMessage reply = originalMessage.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            reply.setOntology("reply");

            reply.setContent(msg.getContent());

            send(reply);

        }

        @Override
        public int onEnd() {
            myAgent.addBehaviour(this);
            return super.onEnd();
        }
    }

}


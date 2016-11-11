package Agents;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class CuratorAgent extends SuperAgent {

  public void setup() {
      super.setup();
      System.out.println("The Curator agent " + getLocalName() + " has started");

      registerService();

      ParallelBehaviour parallelBehaviour = new ParallelBehaviour();

      SequentialBehaviour seq = new SequentialBehaviour();

      MessageTemplate messageTemplate = MessageTemplate.MatchOntology("artifactsRequest");
      MessageTemplate messageTemplate2 = MessageTemplate.MatchOntology("artifactsInfo");

      ArtifactsRequest artifactsRequest = new ArtifactsRequest(this, messageTemplate);

      //addBehaviour(artifactsRequest);

      ArtifactsInfo artifactsInfo = new ArtifactsInfo(this, messageTemplate2);

      //addBehaviour(artifactsInfo);

      parallelBehaviour.addSubBehaviour(artifactsRequest);
      parallelBehaviour.addSubBehaviour(artifactsInfo);

      addBehaviour(parallelBehaviour);

      //addBehaviour(artifactsRequest);

      //seq.addSubBehaviour();



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

    class ArtifactsRequest extends SimpleAchieveREResponder {

        public ArtifactsRequest(Agent agent, MessageTemplate messageTemplate) {
            super(agent, messageTemplate);
            System.out.println("Hamnar jag här i artifactsRequest?");
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            System.out.println("Kommer jag hit till prepareresponse i artifactsRequest");

            ACLMessage reply = request.createReply();
            reply.setContent("tobbe är bäst");
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }

        @Override
        public int onEnd() {
            myAgent.addBehaviour(this);
            return super.onEnd();
        }
    }

    class ArtifactsInfo extends SimpleAchieveREResponder {

        public ArtifactsInfo(Agent agent, MessageTemplate messageTemplate) {
            super(agent, messageTemplate);
            System.out.println("Hamnar jag här i artifactsInfo?");
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            System.out.println("Kommer jag hit till artifactsinfo");

            ACLMessage reply = request.createReply();
            reply.setContent("tobbe skickar artifacts");
            reply.setPerformative(ACLMessage.INFORM);
            return reply;
        }

        @Override
        public int onEnd() {
            myAgent.addBehaviour(this);
            return super.onEnd();
        }
    }
}

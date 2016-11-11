package Agents;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

/**
 * Created by tobiaj on 2016-11-09.
 */
public class CuratorAgent extends Agent {

  public void setup() {
      super.setup();
      System.out.println("The Curator agent " + getLocalName() + " has started");

      registerService();

      SequentialBehaviour seq = new SequentialBehaviour();

      MessageTemplate messageTemplate = MessageTemplate.MatchOntology("artifactsRequest");

      ArtifactsRequest artifactsRequest = new ArtifactsRequest(this, messageTemplate);

      seq.addSubBehaviour(artifactsRequest);

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
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return super.prepareResponse(request);
        }
    }
}

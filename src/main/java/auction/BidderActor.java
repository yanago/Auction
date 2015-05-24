package auction;



import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class BidderActor extends UntypedActor {

	final String name;
	public BidderActor(String name){
		this.name=name;
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Double) {
			try {
				Double price=(Double)message;

				Integer askPrice =  price.intValue();
				Random r = new Random();
				//Randomly generate some  bidding based on made up "biz. logic"
				double increment=Math.ceil(askPrice/4);
				Double orderNum= price/2+Double.valueOf(r.nextInt((askPrice+(int)increment)));
				Bid bid = new Bid(self(),orderNum);
				getSender().tell(bid,getSelf());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();			}

		}
		else if (message instanceof String) {
			System.out.println(self().path().name().toString() +"\tREPLY::"+message);

		}
		 else
	        {
	            unhandled( message );
	        }





	}

	 public static void main(String... args) {
		 ActorSystem _system = ActorSystem.create("FutureUsageExample");
			ActorRef bidder = _system.actorOf(new Props(
					BidderActor.class));
			ActorRef processor = _system.actorOf(new Props(
					Auctioneer .class));
			processor.tell("YO",bidder);
			processor.tell(Integer.valueOf(456),processor);

			}


}

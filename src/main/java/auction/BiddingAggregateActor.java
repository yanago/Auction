package auction;



import java.util.List;
import java.util.TreeMap;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class BiddingAggregateActor extends UntypedActor {
	Item item=null;
	@Override
	//This method aggregates the recieved bids and stores them in TreeMap , thus figuirng out the highest bidder
	public void onReceive(Object message) throws Exception {

	 if (message instanceof List) {
			List l= (List)message;

			TreeMap<Double,String> highestBid=new TreeMap<Double,String>();
			for(int n =0;n<l.size();n++){
				Bid b =(Bid) l.get(n);

				if(b.getAmount()>item.getAmount()){
					System.out.println(b.getBidder().path().name() +" is candidate");
					highestBid.put(b.getAmount(),b.getBidder().path().name());
				}

			}
			if(highestBid.size()>=1){
			System.out.println("WINNER IS :"+highestBid.get(highestBid.lastKey())+"\t"+highestBid.lastKey());
			Status status= new Status("SOLD",highestBid.get(highestBid.lastKey()),item.getName(),highestBid.lastKey());

			final ActorRef inventoryActor = this.getContext().actorOf(new Props(InventoryActor.class));
			//send message to update status in the inventory
			inventoryActor.tell(status, self());
			}
		}
		else if (message instanceof Item) {
			item=(Item)message;
		}

	}
}

package auction;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
public class AuctionActorSystem {

	public static void main(String[] args) throws Exception {
		ActorSystem _system = ActorSystem.create("AuctionHouse");
		ActorRef auctioner = _system.actorOf(new Props(
				Auctioneer.class));
		ActorRef inventoryActor = _system.actorOf(new Props(
				InventoryActor.class));

		final Timeout timeout = new Timeout(Duration.create(10, TimeUnit.SECONDS));

	    Future<Object> future = Patterns.ask(inventoryActor, Query.LIST, timeout);

	    //Get inventory..
	    HashMap <String,Double> inventory=(HashMap<String,Double>)Await.result(future, timeout.duration());
	    System.out.println("Inventory size:"+inventory.size());
	    String name=inventory.keySet().iterator().next();  //pick an item . I could have iterated here for EACH of the items but you get the idea.
	    double amnt=inventory.get(name);

	    //Open Bidding...
	    Item item = new Item(name,amnt);
	    auctioner.tell(item,auctioner);
	    Thread.currentThread().sleep(1000);
		final Timeout timeout2 = new Timeout(Duration.create(1000, TimeUnit.SECONDS));
	    //Now inquire about the status of the item
	    Future<Object> statusResult = Patterns.ask(inventoryActor, name, timeout2);
	    Status result=(Status)Await.result(statusResult, timeout.duration());
	    System.out.println(result.getItenName()+"\t"+result.getStatus()+"\twas sold to:"+result.getWinner());


		//_system.shutdown();
	}

}

package auction;

import java.util.HashMap;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;

public class InventoryActor extends UntypedActor {
	private static HashMap <String,Status> inventoryStatus=null;
	private static HashMap <String,Double> inventoryItems=null;

	@Override
	public void preStart()  {
		//Populate Inventory. Here is hardcoded. Real world scenario would be that it gets populate by some kind of datastore.
				inventoryItems  = new HashMap<String,Double> ();
				inventoryStatus = new HashMap <String,Status>() ;
				inventoryItems.put("Andy Warholl's Scarf", 55267.23);
				inventoryItems.put("Zenith Watch", 1000.00);
				inventoryItems.put("Russian Matrioshka Doll", 55.00);
				inventoryItems.put("Silverware", 122.00);
				inventoryItems.put("Beatles Let it be", 75.00);
				inventoryItems.put("Rolex", 10000.00);
				inventoryItems.put("Compass", 20.00);
				inventoryItems.put("Hoyt Bow", 800.00);
	}


	@Override
	/*This receive 3 types of requests - List - which gives back the whole inventory. Item name query which provides status. Status update
	 * 
	 */
	public void onReceive(Object message) throws Exception {
		
		if (message instanceof Status) {
			Status status=(Status)message;
			String itemName=status.getItenName();
			System.out.println("Item:"+itemName +"\tstatus:"+status.getStatus());
			inventoryStatus.put(itemName, status);
			inventoryItems.remove(status.getItenName());
			System.out.println("Item:"+itemName +"\tremoved from inventory"+"\t"+inventoryItems.size());

		}
		else if(message instanceof String){

			String item=(String)message;
			System.out.println(self().path().name()+"\tReceived Query for:"+item);
			System.out.println(inventoryStatus.size());
		    System.out.println(inventoryItems.size());
			Status status=inventoryStatus.get(item);
			System.out.println("Status:"+status.getItenName()+"\t"+status.getStatus());
			getSender().tell(status, self());
		}
		else if(message instanceof Query){
			System.out.println(self().path().name()+"\tReceived Inventory Query");
			getSender().tell(inventoryItems, self());
		}




	}

	 public static void main(String... args) {
		 ActorSystem _system = ActorSystem.create("FutureUsageExample");


			}

}

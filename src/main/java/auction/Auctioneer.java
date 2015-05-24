package auction;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.util.Timeout;

public class Auctioneer extends UntypedActor {

	final Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));

/*
	ActorRef bidderActor = this.getContext()
             .actorOf( new Props(BidderActor.class )
                     .withRouter( new RoundRobinRouter( 10 )), "workerRouter" );

*/



	//INITIALIZE 10 BIDDERS. HARDCODED - JUST  SIMULATION . In online system this will be dynamic

	
	final ActorRef bidder1 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder1"),
			"bidder1");
	final ActorRef bidder2 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder2"),
			"bidder2");
	final ActorRef bidder3 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder3"),
			"bidder3");
	final ActorRef bidder4 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder4"),
			"bidder4");
	final ActorRef bidder5 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder5"),
			"bidder5");
	final ActorRef bidder6 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder6"),
			"bidder6");
	final ActorRef bidder7 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder7"),
			"bidder7");
	final ActorRef bidder8 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder8"),
			"bidder8");
	final ActorRef bidder9 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder9"),
			"bidder9");
	final ActorRef bidder10 = this.getContext().actorOf(Props.create(BidderActor.class,"bidder10"),
			"bidder10");

	ActorRef orderAggregateActor = getContext().actorOf(
			new Props(BiddingAggregateActor.class));

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Item) {

			Item  bidItem=(Item)message;
			final Double askPrice = bidItem.getAmount();
			final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();

			// make concurrent calls to actors

			orderAggregateActor.tell(bidItem, self());
			futures.add(ask(bidder1, askPrice, t));
			futures.add(ask(bidder2, askPrice, t));
			futures.add(ask(bidder3, askPrice, t));
			futures.add(ask(bidder4, askPrice, t));
			futures.add(ask(bidder5, askPrice, t));
			futures.add(ask(bidder6, askPrice, t));
			futures.add(ask(bidder7, askPrice, t));
			futures.add(ask(bidder8, askPrice, t));
			futures.add(ask(bidder9, askPrice, t));
			futures.add(ask(bidder10,askPrice, t));

			 System.out.println("There are " + futures.size() + " Bidders currently bidding");

		//	futures.add(ask(addressActor, userId, t));

			// set the sequence in which the reply are expected

			final Future<Iterable<Object>> aggregate = Futures.sequence(
					futures, getContext().system().dispatcher());



			// once the replies comes back, we loop through the Iterable to
			// get the replies in same order
		
			final ArrayList<Bid> bids  = new ArrayList();


			final Future<List<Bid>> aggResult = aggregate.map(
					new Mapper<Iterable<Object>, List<Bid>>() {

						@Override
						public List<Bid> apply(Iterable<Object> coll) {
							System.out.println("processing");
							final Iterator<Object> it = coll.iterator();
							while(it.hasNext()){
								Bid bid = (Bid) it.next();
								System.out.println("Bid ===> " + bid.toString());
								if(bid.getAmount()<askPrice){
									ActorRef badBid=bid.getBidder();

									badBid.tell("Bid unacceptable.Price is less than asked!",self());
								}

								bids.add(bid);


							}
							return bids;

						}
					}, getContext().system().dispatcher());
			//send them to the Aggregator to calcualte highest bid
			pipe(aggResult, getContext().system().dispatcher()).to(
					orderAggregateActor);

		}


	}
}

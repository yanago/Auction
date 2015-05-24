package auction;

import akka.actor.ActorRef;

public class Bid {

	  private double amount;
	  private final ActorRef bidder;
	  public Bid(ActorRef bidder, double amount) {
		 this.bidder=bidder;
	    this.amount = amount;
	  }

    public ActorRef getBidder() {
		return bidder;
	}

	public void setAmount(double amount) {
	    this.amount = amount;
	  }

	public double getAmount() {
	   return amount;
	 }



	  @Override
	public String toString() {
	    return bidder.path().name() + " bids " + amount;
	  }
	}

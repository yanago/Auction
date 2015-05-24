package auction;

public class Item {
	public Item(String name,double amount) {


		this.name = name;
		this.amount=amount;
	}

	public double getAmount() {
		return amount;
	}


	public String getName() {
		return name;
	}


	private final String name;
	private final double amount;


}

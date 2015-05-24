package auction;

public class Status {
	public Status(String status, String winner, String itenName, double amount) {

		this.status = status;
		this.winner = winner;
		this.itenName = itenName;
		this.amount = amount;
	}
	private final String status;
	private final String winner;
	private final String itenName;
	private final double amount;
	public String getStatus() {
		return status;
	}
	public String getWinner() {
		return winner;
	}
	public String getItenName() {
		return itenName;
	}
	public double getAmount() {
		return amount;
	}




}

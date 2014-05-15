package videostreaming.common;

public enum ProtocolMessages {
	Status("status"), StartSream("startstream"), StartingStream(
			"startingstream"), StopStream("stopstream"), StoppedStream(
			"stoppedstream"), Image("image"), Overloaded("overloaded");

	private String value;

	ProtocolMessages(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.getValue();
	}
}

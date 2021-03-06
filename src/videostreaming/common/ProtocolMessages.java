package videostreaming.common;

public enum ProtocolMessages {
	Status("status"), StartSream("startstream"), StartingStream(
			"startingstream"), StopStream("stopstream"), StoppedStream(
			"stoppedstream"), Image("image"), Data("data"), Overloaded("overloaded"), Request("request"), Response("response");

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

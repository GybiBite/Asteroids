package gybibite.asteroids;

public enum Const {
	S_WIDTH(640), S_HEIGHT(480);

	private int i;

	Const(int i) {
		this.i = i;
	}

	public int get() {
		return i;
	}
}
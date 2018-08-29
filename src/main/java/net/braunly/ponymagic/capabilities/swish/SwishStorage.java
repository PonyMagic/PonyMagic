package net.braunly.ponymagic.capabilities.swish;

public class SwishStorage implements ISwishCapability {

	private boolean canSwish = true;

	@Override
	public boolean canSwish() {
		return canSwish;
	}

	@Override
	public void setCanSwish(boolean canSwish) {
		this.canSwish = canSwish;
	}
}

package jmetest.monkeymahjongg.playground;

public class Tile {
	private Board owner;
	private int x, y, z;
	private TileGroup group;

	public Tile(Board owner, int x, int y, int z) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean isBlocked() {
		if (owner.getTile(x, y, z + 1) != null
				&& owner.getTile(x, y - 1, z + 1) != null
				&& owner.getTile(x, y + 1, z + 1) != null
				&& owner.getTile(x - 1, y, z + 1) != null
				&& owner.getTile(x + 1, y, z + 1) != null)
			return true;
		if (owner.getTile(x - 1, y, z) != null
				&& owner.getTile(x + 1, y, z) != null)
			return true;
		return false;
	}

	public void remove() {
		owner.remove(this);
		callRemoveListeners();
	}

	private void callRemoveListeners() {
		// TODO Auto-generated method stub
		
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public boolean matches(Tile tile) {
		return tile.getGroup().equals(group);
	}

	public TileGroup getGroup() {
		return group;
	}

	public void setGroup(TileGroup group) {
		this.group = group;
	}
}

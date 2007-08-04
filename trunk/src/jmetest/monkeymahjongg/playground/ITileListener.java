package jmetest.monkeymahjongg.playground;

public interface ITileListener {
	void removed(Tile tile);
	void selected(Tile tile, TileSelection selection);
}

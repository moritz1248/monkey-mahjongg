package jmetest.monkeymahjongg.playground.model;


public interface ITileListener {
	void removed(Tile tile);
	void selected(Tile tile, TileSelection selection);
}

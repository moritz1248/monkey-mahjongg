package jmetest.monkeymahjongg.playground.model;


public class SimpleTileGroup extends TileGroup {
	private String textureResource;
	public SimpleTileGroup( String textureResource ) {
		this.textureResource = textureResource;
	}
	
	@Override
	public String getTextureResource( Tile tile ) {
		return textureResource;
	}
}

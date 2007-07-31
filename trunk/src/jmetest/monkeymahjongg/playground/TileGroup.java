package jmetest.monkeymahjongg.playground;

public class TileGroup {
	private String textureResource;
	int refCount = 0;
	
	public TileGroup( String textureResource ) {
		this.textureResource = textureResource;
	}

	public String getTextureResource() {
		return textureResource;
	}

	public boolean assignTo( Tile tile )
	{
		tile.setGroup( this );
		refCount++;
		return refCount < 4;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return false;
	}

	public void reset() {
		refCount = 0;
	}
}

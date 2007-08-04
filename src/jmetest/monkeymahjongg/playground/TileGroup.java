package jmetest.monkeymahjongg.playground;

public abstract class TileGroup {

	Tile tiles[] = new Tile[] { null, null, null, null };
	protected int refCount = 0;

	public TileGroup() {
		super();
	}

	public boolean assignTo(Tile tile) {
		tile.setGroup( this );
		tiles[refCount] = tile;
		refCount++;
		return refCount < 4;
	}

	public void remove(Tile tile) {
		for( int i=0; i<4; ++i )
		{
			if( tiles[i] == tile )
				tiles[i] = null;
		}
	}

	public Hint getHint() {
		Tile first = null;
		Tile second = null;
		for (int i = 0; i < 4; ++i) {
			if (tiles[i] != null) {
				if (first == null)
					first = tiles[i];
				else {
					second = tiles[i];
					break;
				}
			}
		}
	
		if (first != null && second != null)
			return new Hint(first, second);
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return false;
	}

	public abstract String getTextureResource( Tile tile ); 
}
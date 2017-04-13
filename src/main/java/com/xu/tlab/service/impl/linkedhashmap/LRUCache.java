package com.xu.tlab.service.impl.linkedhashmap;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 3615578171194427739L;

	private int cacheSize;

	public LRUCache( int cacheSize ) {
		super( 16, 0.75f, true );
		this.cacheSize = cacheSize;
	}

	protected boolean removeEldestEntry( Map.Entry<K, V> eldest ) {
		return size() >= cacheSize;
	}

}

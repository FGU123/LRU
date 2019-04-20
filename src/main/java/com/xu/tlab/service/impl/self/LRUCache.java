package com.xu.tlab.service.impl.self;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PPXu(FGU123)
 * 自定义节点结构简单实现双向链表，节点包含前后双向指针，基于HashMap存储节点达到时间复杂度O(1)的目的，最差为O(s)，s为Map.size
 * 额外维护两个指针：链表的头、尾指针
 */
public class LRUCache<K, V> {

	private int capacity = 1 << 30;

	private Map<K, Node> map = null;

	// 头指针
	private Node head;

	// 尾指针
	private Node tail;

	public LRUCache() {
		map = new HashMap<K, Node>( capacity );
	}

	public LRUCache( int capacity ) {
		this.capacity = capacity;
		map = new HashMap<K, Node>( capacity );
	}

	/**
	 *  定义 节点的结构
	 */
	public class Node {

		private K key;

		private V value;

		// 前指针
		private Node prev;

		// 后指针
		private Node next;

	}

	public void put( K key, V value ) {

		Node node = map.get( key );
		if( null == node ) {
			node = new Node();
			node.key = key;
		}
		node.value = value;
		if( removeEldest( node ) ) {
			removeTail();
		}
		map.put( key, node );
		moveToHead( node );
	}

	/**
	 * 定义成方法，让它可以开放出去，让使用者重写来自定义逻辑
	 */
	protected boolean removeEldest( Node eldest ) {
		return map.size() >= capacity;
	}

	private void removeTail() {
		if( tail != null ) {
			remove( tail );
		}
	}

	/**
	 * 管好4个指针即可： 链表的头指针、尾指针，当前节点的前节点的后指针、后节点的前指针，
	 * 因为是当前节点是被删除的节点，所以自己的前后指针不需要管
	 *  
	 * 1. 如果当前节点是尾节点，把尾指针往前挪一个节点
	 * 2. 如果当前节点是头节点，把头指针往后挪一个节点
	 * 3. 如果当前节点的前指针不为空，前节点的后指针指向后节点
	 * 4. 如果当前节点的后指针不为空，后节点的前指针指向前节点
	 */
	public void remove( Node node ) {
		if( tail == node ) {
			tail = tail.prev;
		}
		if( head == node ) {
			head = head.next;
		}
		if( node.prev != null ) {
			node.prev.next = node.next;
		}
		if( node.next != null ) {
			node.next.prev = node.prev;
		}
		map.remove( node.key );
	}

	public V get( K key ) {
		Node node = map.get( key );
		if( node != null ) {
			moveToHead( node );
			return node.value;
		}
		return null;
	}

	/**
	 * 管好最多6个指针即可： 
	 * 链表的头指针(head)、尾指针(tail)，
	 * 被移位的节点的前节点的后指针(node.prev.next)、后节点的前指针(node.next.prev)，
	 * 被移位的节点自己的前后指针(node.prev，node.next) 
	 */
	private void moveToHead( Node node ) {

		// 头尾指针为空，说明在挪头操作之前，一个节点都没有，而当前节点为新增节点，则只需要把头尾指针都指向当前节点即可
		if( tail == null || head == null ) {
			head = tail = node;
			return;
		}

		// 如果当前节点已经为头节点，则啥也不需要做，直接返回
		if( head == node ) {
			return;
		}

		// 如果当前节点是尾节点，则挪头操作后必然要把尾指针需要改为尾节点的上一个节点
		if( tail == node ) {
			tail = tail.prev;
		}

		// 如果当前节点的前节点不为空，则把前节点的后指针指向当前节点的前节点
		if( node.prev != null ) {
			node.prev.next = node.next;
		}

		// 如果当前节点的后节点不为空，则把后节点的前指针指向当前节点的前节点
		if( node.next != null ) {
			node.next.prev = node.prev;
		}
		
		// 接下来是挪头操作：
		// 1. 前边已经判断过了头节点是存在的，那么把头节点的前指针由null指向为当前节点
		// 2. 当前节点的后指针指向头节点
		// 3. 当前节点的前指针指向null
		// 4. 头指针指向当前节点
		head.prev = node;
		node.next = head;
		node.prev = null;
		head = node;
	}

}
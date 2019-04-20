package com.xu.tlab.service.impl.self;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PPXu(FGU123)
 * �Զ���ڵ�ṹ��ʵ��˫�������ڵ����ǰ��˫��ָ�룬����HashMap�洢�ڵ�ﵽʱ�临�Ӷ�O(1)��Ŀ�ģ����ΪO(s)��sΪMap.size
 * ����ά������ָ�룺�����ͷ��βָ��
 */
public class LRUCache<K, V> {

	private int capacity = 1 << 30;

	private Map<K, Node> map = null;

	// ͷָ��
	private Node head;

	// βָ��
	private Node tail;

	public LRUCache() {
		map = new HashMap<K, Node>( capacity );
	}

	public LRUCache( int capacity ) {
		this.capacity = capacity;
		map = new HashMap<K, Node>( capacity );
	}

	/**
	 *  ���� �ڵ�Ľṹ
	 */
	public class Node {

		private K key;

		private V value;

		// ǰָ��
		private Node prev;

		// ��ָ��
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
	 * ����ɷ������������Կ��ų�ȥ����ʹ������д���Զ����߼�
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
	 * �ܺ�4��ָ�뼴�ɣ� �����ͷָ�롢βָ�룬��ǰ�ڵ��ǰ�ڵ�ĺ�ָ�롢��ڵ��ǰָ�룬
	 * ��Ϊ�ǵ�ǰ�ڵ��Ǳ�ɾ���Ľڵ㣬�����Լ���ǰ��ָ�벻��Ҫ��
	 *  
	 * 1. �����ǰ�ڵ���β�ڵ㣬��βָ����ǰŲһ���ڵ�
	 * 2. �����ǰ�ڵ���ͷ�ڵ㣬��ͷָ������Ųһ���ڵ�
	 * 3. �����ǰ�ڵ��ǰָ�벻Ϊ�գ�ǰ�ڵ�ĺ�ָ��ָ���ڵ�
	 * 4. �����ǰ�ڵ�ĺ�ָ�벻Ϊ�գ���ڵ��ǰָ��ָ��ǰ�ڵ�
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
	 * �ܺ����6��ָ�뼴�ɣ� 
	 * �����ͷָ��(head)��βָ��(tail)��
	 * ����λ�Ľڵ��ǰ�ڵ�ĺ�ָ��(node.prev.next)����ڵ��ǰָ��(node.next.prev)��
	 * ����λ�Ľڵ��Լ���ǰ��ָ��(node.prev��node.next) 
	 */
	private void moveToHead( Node node ) {

		// ͷβָ��Ϊ�գ�˵����Ųͷ����֮ǰ��һ���ڵ㶼û�У�����ǰ�ڵ�Ϊ�����ڵ㣬��ֻ��Ҫ��ͷβָ�붼ָ��ǰ�ڵ㼴��
		if( tail == null || head == null ) {
			head = tail = node;
			return;
		}

		// �����ǰ�ڵ��Ѿ�Ϊͷ�ڵ㣬��ɶҲ����Ҫ����ֱ�ӷ���
		if( head == node ) {
			return;
		}

		// �����ǰ�ڵ���β�ڵ㣬��Ųͷ�������ȻҪ��βָ����Ҫ��Ϊβ�ڵ����һ���ڵ�
		if( tail == node ) {
			tail = tail.prev;
		}

		// �����ǰ�ڵ��ǰ�ڵ㲻Ϊ�գ����ǰ�ڵ�ĺ�ָ��ָ��ǰ�ڵ��ǰ�ڵ�
		if( node.prev != null ) {
			node.prev.next = node.next;
		}

		// �����ǰ�ڵ�ĺ�ڵ㲻Ϊ�գ���Ѻ�ڵ��ǰָ��ָ��ǰ�ڵ��ǰ�ڵ�
		if( node.next != null ) {
			node.next.prev = node.prev;
		}
		
		// ��������Ųͷ������
		// 1. ǰ���Ѿ��жϹ���ͷ�ڵ��Ǵ��ڵģ���ô��ͷ�ڵ��ǰָ����nullָ��Ϊ��ǰ�ڵ�
		// 2. ��ǰ�ڵ�ĺ�ָ��ָ��ͷ�ڵ�
		// 3. ��ǰ�ڵ��ǰָ��ָ��null
		// 4. ͷָ��ָ��ǰ�ڵ�
		head.prev = node;
		node.next = head;
		node.prev = null;
		head = node;
	}

}
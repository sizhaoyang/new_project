package com.jaovo.common.model;

public class SystemContext {

	// 本质就是个map
	private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> pageIndex = new ThreadLocal<Integer>();
	private static ThreadLocal<Integer> pageOffset = new ThreadLocal<Integer>();

	/**
	 * 升序还是降序 : asc , desc
	 */
	private static ThreadLocal<String> order = new ThreadLocal<String>();
	/**
	 * 根据某个字段排序 : order by
	 */
	private static ThreadLocal<String> sort = new ThreadLocal<String>();

	public static String getOrder() {
		return order.get();
	}

	public static void setOrder(String _order) {
		order.set(_order);
	}

	public static void removeOrder() {
		order.remove();
	}

	public static String getSort() {
		return sort.get();
	}

	public static void setSort(String _sort) {
		sort.set(_sort);
	}

	public static void removeSort() {
		sort.remove();
	}

	public static int getPageOffset() {
		return pageOffset.get();
	}

	public static void setPageOffset(int _PageOffset) {
		pageOffset.set(_PageOffset);

	}

	public static void removePageOffset() {
		pageOffset.remove();
	}

	public static void setPageSize(int _pageSize) {
		pageSize.set(_pageSize);
	}

	public static int getPageSize() {
		return pageSize.get();
	}

	public static void removePageSize() {
		pageSize.remove();
	}

	public static void setPageIndex(int _pageIndex) {
		pageIndex.set(_pageIndex);
	}

	public static int getPageIndex() {
		return pageIndex.get();
	}

	public static void removePageIndex() {
		pageIndex.remove();
	}
}

package me.josvth.trade.layouts;

import org.bukkit.inventory.Inventory;

import static me.josvth.trade.managers.LanguageManager._;

public class ItemLayout {

	// Slots
	int[] leftSlots;
	int[] rightSlots;
	int[] seperatorSlots;

	int[] acceptSlots;
	int[] refuseSlots;
	int[] statusSlots;

	// Items
	ItemStackBase acceptItem;
	ItemStackBase acceptedItem;
	ItemStackBase refuseItem;
	ItemStackBase pendingItem;
	ItemStackBase seperatorItem;

	private int rows;

	public int getSize() {
		return rows * 9;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		if (rows > 6)
			rows = 6;
		if (rows < 2)
			rows = 2;
		this.rows = rows;
	}

	public void fillInventory(Inventory inventory) {

		for (int slot : getSeperatorSlots())
			inventory.setItem(slot, getSeperatorItem().createItemStack());
		for (int slot : getAcceptSlots())
			inventory.setItem(slot, getAcceptItem().createItemStack());
		for (int slot : getRefuseSlots())
			inventory.setItem(slot, getRefuseItem().createItemStack());
		for (int slot : getStatusSlots())
			inventory.setItem(slot, getPendingItem().createItemStack());

	}

	/* Getters */
	// Slots
	public int[] getAcceptSlots() {
		return acceptSlots;
	}

	public int[] getRefuseSlots() {
		return refuseSlots;
	}

	public int[] getStatusSlots() {
		return statusSlots;
	}

	public int[] getLeftSlots() {
		return leftSlots;
	}

	public int[] getRightSlots() {
		return rightSlots;
	}

	public int[] getSeperatorSlots() {
		return seperatorSlots;
	}

	// Items
	public ItemStackBase getAcceptItem() {
		return acceptItem;
	}

	public ItemStackBase getAcceptedItem() {
		return acceptedItem;
	}

	public ItemStackBase getRefuseItem() {
		return refuseItem;
	}

	public ItemStackBase getPendingItem() {
		return pendingItem;
	}

	public ItemStackBase getSeperatorItem() {
		return seperatorItem;
	}

	/* Setters */
	// Slots
	public void setLeftSlots(int[] leftSlots) {
		this.leftSlots = leftSlots;
	}

	public void setRightSlots(int[] rightSlots) {
		this.rightSlots = rightSlots;
	}

	public void setSeperatorSlots(int[] seperatorSlots) {
		this.seperatorSlots = seperatorSlots;
	}

	public void setAcceptSlots(int[] acceptSlots) {
		this.acceptSlots = acceptSlots;
	}

	public void setRefuseSlots(int[] refuseSlots) {
		this.refuseSlots = refuseSlots;
	}

	public void setStatusSlots(int[] statusSlots) {
		this.statusSlots = statusSlots;
	}

	// Items
	public void setAcceptItem(ItemStackBase acceptItem) {
		this.acceptItem = acceptItem;
	}

	public void setAcceptedItem(ItemStackBase acceptedItem) {
		this.acceptedItem = acceptedItem;
	}

	public void setRefuseItem(ItemStackBase refuseItem) {
		this.refuseItem = refuseItem;
	}

	public void setPendingItem(ItemStackBase pendingItem) {
		this.pendingItem = pendingItem;
	}

	public void setSeperatorItem(ItemStackBase seperatorItem) {
		this.seperatorItem = seperatorItem;
	}

	protected static void setActionSlots(ItemLayout layout) {

		layout.setAcceptSlots(new int[] { 3 });
		layout.setRefuseSlots(new int[] { 4 });
		layout.setStatusSlots(new int[] { 5 });

		int seperatorRows = layout.getRows() - 1;
		layout.setSeperatorSlots(new int[seperatorRows]);

		for (int i = 0; i < seperatorRows; i++)
			layout.getSeperatorSlots()[i] = 13 + i * 9;

	}

	protected static void setTradeSlots(ItemLayout layout) {

		switch (layout.getRows()) {
		case 1:
			layout.setLeftSlots(new int[] { 0, 1, 2 });
			layout.setRightSlots(new int[] { 6, 7, 8 });
			break;
		case 2:
			layout.setLeftSlots(new int[] { 0, 1, 2, 9, 10, 11, 12 });
			layout.setRightSlots(new int[] { 6, 7, 8, 14, 15, 16, 17 });
			break;
		case 3:
			layout.setLeftSlots(new int[] { 0, 1, 2, 9, 10, 11, 12, 18, 19, 20,
					21 });
			layout.setRightSlots(new int[] { 6, 7, 8, 14, 15, 16, 17, 23, 24,
					25, 26 });
			break;
		case 4:
			layout.setLeftSlots(new int[] { 0, 1, 2, 9, 10, 11, 12, 18, 19, 20,
					21, 27, 28, 29, 30 });
			layout.setRightSlots(new int[] { 6, 7, 8, 14, 15, 16, 17, 23, 24,
					25, 26, 32, 33, 34, 35 });
			break;
		case 5:
			layout.setLeftSlots(new int[] { 0, 1, 2, 9, 10, 11, 12, 18, 19, 20,
					21, 27, 28, 29, 30, 36, 37, 38, 39 });
			layout.setRightSlots(new int[] { 6, 7, 8, 14, 15, 16, 17, 23, 24,
					25, 26, 32, 33, 34, 35, 41, 42, 43, 44 });
			break;
		case 6:
			layout.setLeftSlots(new int[] { 0, 1, 2, 9, 10, 11, 12, 18, 19, 20,
					21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47, 48 });
			layout.setRightSlots(new int[] { 6, 7, 8, 14, 15, 16, 17, 23, 24,
					25, 26, 32, 33, 34, 35, 41, 42, 43, 44, 50, 51, 52, 53 });
			break;
		default:
			break;
		}
		// TODO Make calculation for this as in the CurrencyLayout
	}

	protected static void setActionItems( ItemLayout layout ) {

		layout.setAcceptItem( new ItemStackBase(35, 0, (short) 5, _( "trade.items.accept" ), null) );
		layout.setAcceptedItem( new ItemStackBase(35, 0, (short) 4, _( "trade.items.accepted" ), null ) );
		layout.setRefuseItem( new ItemStackBase(35, 0, (short) 14, _( "trade.items.refuse" ), null )	);
		layout.setPendingItem( new ItemStackBase(35, 0, (short) 8, _( "trade.items.pending" ), null ) );
		layout.setSeperatorItem( new ItemStackBase(35, 0, (short) 8, null, null ) );
		
	}

	public static ItemLayout createDefaultLayout(int rows) {

		ItemLayout layout = new ItemLayout();
		layout.setRows(rows);

		setActionSlots(layout);
		setTradeSlots(layout);
		setActionItems(layout);

		return layout;
	}

}

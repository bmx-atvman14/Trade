package me.josvth.trade.layouts;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemLayout {

	// Slots
	int[] leftSlots;
	int[] rightSlots;
	int[] seperatorSlots;

	int[] acceptSlots;
	int[] refuseSlots;
	int[] statusSlots;

	// Items
	ItemStack acceptItem;
	ItemStack refuseItem;
	ItemStack pendingItem;
	ItemStack seperatorItem;

	private int rows;
		
	public int getSize() {
		return rows * 9;
	}
	
	public int getRows() {
		return rows;
	}

	public void setRows( int rows ) {
		if ( rows > 6 ) rows = 6;
		if ( rows < 2 )	rows = 2;
		this.rows = rows;
	}
			
	public void fillInventory(Inventory inventory) {

		for ( int slot : seperatorSlots ) 	inventory.setItem( slot , seperatorItem );
		for ( int slot : acceptSlots ) 		inventory.setItem( slot , acceptItem );
		for ( int slot : refuseSlots ) 		inventory.setItem( slot , refuseItem ); 
		for ( int slot : statusSlots )		inventory.setItem( slot , pendingItem ); 

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
	public ItemStack getAcceptItem() {
		return acceptItem;
	}

	public ItemStack getRefuseItem() {
		return refuseItem;
	}

	public ItemStack getPendingItem() {
		return pendingItem;
	}

	public ItemStack getSeperatorItem() {
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
	public void setAcceptItem(ItemStack acceptItem) {
		this.acceptItem = acceptItem;
	}

	public void setRefuseItem(ItemStack refuseItem) {
		this.refuseItem = refuseItem;
	}

	public void setPendingItem(ItemStack pendingItem) {
		this.pendingItem = pendingItem;
	}

	public void setSeperatorItem(ItemStack seperatorItem) {
		this.seperatorItem = seperatorItem;
	}

	protected static void setActionSlots( ItemLayout layout ){
		
		layout.setAcceptSlots( new int[]{3} );
		layout.setRefuseSlots( new int[]{4} );
		layout.setStatusSlots( new int[]{5} );

		int seperatorRows = layout.getRows()  - 1;
		layout.setSeperatorSlots( new int[ seperatorRows ] );

		for ( int i = 0; i < seperatorRows; i++ ) layout.getSeperatorSlots()[ i ] = 13 + i * 9;
		
	}
	
	protected static void setTradeSlots( ItemLayout layout ){
		
		switch ( layout.getRows() ){
		case 1:
			layout.setLeftSlots( 	new int[]{0,1,2} );
			layout.setRightSlots( 	new int[]{6,7,8} );
			break;
		case 2:
			layout.setLeftSlots(	new int[]{0,1,2, 9,10,11,12} );
			layout.setRightSlots( 	new int[]{6,7,8, 14,15,16,17} );
			break;
		case 3:
			layout.setLeftSlots(	new int[]{0,1,2, 9,10,11,12, 18,19,20,21} );
			layout.setRightSlots( 	new int[]{6,7,8, 14,15,16,17, 23,24,25,26} );
			break;
		case 4:
			layout.setLeftSlots(	new int[]{0,1,2, 9,10,11,12, 18,19,20,21, 27,28,29,30} );
			layout.setRightSlots( 	new int[]{6,7,8, 14,15,16,17, 23,24,25,26, 32,33,34,35} );
			break;
		case 5:
			layout.setLeftSlots(	new int[]{0,1,2, 9,10,11,12, 18,19,20,21, 27,28,29,30, 36,37,38,39} );
			layout.setRightSlots( 	new int[]{6,7,8, 14,15,16,17, 23,24,25,26, 32,33,34,35, 41,42,43,44} );
			break;
		case 6:
			layout.setLeftSlots(	new int[]{0,1,2, 9,10,11,12, 18,19,20,21, 27,28,29,30, 36,37,38,39, 45,46,47,48} );
			layout.setRightSlots( 	new int[]{6,7,8, 14,15,16,17, 23,24,25,26, 32,33,34,35, 41,42,43,44, 50,51,52,53} );
			break;
		default: break;
		}
		// TODO Make calculation for this as in the CurrencyLayout
	}
	
	protected static void setActionItems( ItemLayout layout ) {
		layout.setAcceptItem( new ItemStack(35, 0, (short) 5) 	);
		layout.setRefuseItem( new ItemStack(35, 0, (short) 14)	);
		layout.setPendingItem( new ItemStack(35, 0, (short) 8) 	);
		layout.setSeperatorItem( new ItemStack(280, 0) 			);
		// TODO Add item names if API is added;
	}

	public static ItemLayout createDefaultLayout( int rows ) {

		ItemLayout layout = new ItemLayout();
		layout.setRows( rows );
		
		setActionSlots( layout );
		setTradeSlots( layout );
		setActionItems( layout );
		
		return layout;
	}

}

package me.josvth.trade.exchangeinterfaces;

import me.josvth.trade.layouts.ItemLayout;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ItemInterface implements InventoryHolder {

	Inventory inventory;
	ItemLayout layout;
	
	public ItemInterface( String nameOtherPlayer, ItemLayout layout ){
		this.inventory = Bukkit.getServer().createInventory( this, layout.getSize(), ItemInterface.generateTitle( nameOtherPlayer ) );
		this.layout = layout; 
		layout.fillInventory( inventory );
	}

	private ItemLayout getLayout(){
		return layout;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public void accept(){
		for ( int slot : getLayout().getAcceptSlots() ) inventory.setItem(slot, getLayout().getPendingItem());
	}

	public void deny(){
		for ( int slot : getLayout().getAcceptSlots() ) inventory.setItem(slot, getLayout().getAcceptItem());
	}

	public void acceptOther(){
		for ( int slot : getLayout().getStatusSlots() ) inventory.setItem(slot, getLayout().getAcceptItem());
	}

	public void denyOther(){
		for ( int slot : getLayout().getStatusSlots() ) inventory.setItem(slot, getLayout().getPendingItem());
	}

	public ItemStack[] getItems( Side side ) {
		ItemStack[] items = new ItemStack[ getLayout().getRows() * 4 - 1 ];
		switch ( side ) {
		case LEFT:
			for ( int i = 0; i < getLayout().getLeftSlots().length; i++ ) 	items[i] = inventory.getItem( getLayout().getLeftSlots()[i] );
			break;
		case RIGHT:
			for ( int i = 0; i < getLayout().getRightSlots().length; i++ )	items[i] = inventory.getItem( getLayout().getRightSlots()[i] );
			break;
		}
		System.out.print(items.length);
		return items;
	}

	public int getSize() {
		return getLayout().getRows() * 9;
	}

	public boolean isAcceptSlot(int slot) {
		for ( int accept : getLayout().getAcceptSlots() ) if ( accept == slot ) return true;
		return false;
	}

	public boolean isRefuseSlot(int slot) {
		for ( int refuse : getLayout().getRefuseSlots() ) if ( refuse == slot ) return true;
		return false;
	}

	// Gets the item on a index. Note that index is not the inventory index but the trading slot index
	public int getInventoryIndex ( int index, Side side ) {
		switch ( side ) {
		case LEFT:	return getLayout().getLeftSlots() [ index ];
		case RIGHT: return getLayout().getRightSlots() [ index ];	
		default: 	return -1;
		}
	}

	// Converts the inventory index to the trade index
	public int getTradeIndex ( int index, Side side  ) {
		
		if ( side.equals( Side.LEFT ) )
			for ( int i = 0; i < getLayout().getLeftSlots().length; i++ )
				if ( getLayout().getLeftSlots()[i] == index ) return i;
		
		if ( side.equals( Side.RIGHT ))
			for ( int i = 0; i < getLayout().getRightSlots().length; i++ )
				if ( getLayout().getRightSlots()[i] == index ) return i;
		
		return -1;
	}

	// Sets an item in the inventory according to the trading slot index
	public void setTradeItem( int index, ItemStack item, Side side ) {
		inventory.setItem( getInventoryIndex(index, side), item );
		inventory.setItem( getInventoryIndex(index, side), item );
	}

	public static String generateTitle(String playerName){
		String title = "     You";
		while (title.length() + playerName.length() < 32){
			title += " ";
		}
		return title += playerName;
	}
	
	// Replaces null items in an array by ItemStack(0,1)
	public static void replaceNullItems(ItemStack[] items){
		for ( int i = 0; i < items.length; i++ ) if( items[i] == null ) items[i] = new ItemStack(0, 1);
	}
	
	public enum Side {
		LEFT,
		RIGHT;
	}
	
}

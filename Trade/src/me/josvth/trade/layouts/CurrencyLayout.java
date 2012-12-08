package me.josvth.trade.layouts;

import java.util.regex.Matcher;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static me.josvth.trade.managers.LanguageManager._;

public class CurrencyLayout extends ItemLayout {

	// Slots		
	// Add currency slots
	int[] leftSmallSlots;
	int[] leftMediumSlots;
	int[] leftLargeSlots;

	// Display currency slots
	int[] leftDisplaySmallSlots;
	int[] leftDisplayMediumSlots;
	int[] leftDisplayLargeSlots;

	int[] rightDisplaySmallSlots;
	int[] rightDisplayMediumSlots;
	int[] rightDisplayLargeSlots;

	// Items
	// Currency display
	ItemStack smallItemDisplay;
	ItemStack mediumItemDisplay;
	ItemStack largeItemDisplay;

	// Currency change
	ItemStack smallItem;
	ItemStack mediumItem;
	ItemStack largeItem;
	
	// Counters
	int smallCurrency;
	int mediumCurrency;
	int largeCurrency;

	@Override
	public void fillInventory(Inventory inventory) {
		
		// Set left small items
		for ( int slot : getLeftSmallSlots() ) inventory.setItem( slot, getSmallItem() );

		// Set left medium items
		for ( int slot : getLeftMediumSlots() ) inventory.setItem( slot, getMediumItem() );

		// Set left large items
		for ( int slot : getLeftLargeSlots() ) inventory.setItem( slot, getLargeItem() );
		
		super.fillInventory(inventory);
		
	}
	
	public int[] getLeftSmallSlots() {
		return leftSmallSlots;
	}

	public void setLeftSmallSlots(int[] leftSmallSlots) {
		this.leftSmallSlots = leftSmallSlots;
	}

	public int[] getLeftMediumSlots() {
		return leftMediumSlots;
	}

	public void setLeftMediumSlots(int[] leftMediumSlots) {
		this.leftMediumSlots = leftMediumSlots;
	}

	public int[] getLeftLargeSlots() {
		return leftLargeSlots;
	}

	public void setLeftLargeSlots(int[] leftLargeSlots) {
		this.leftLargeSlots = leftLargeSlots;
	}

	public int[] getLeftDisplaySmallSlots() {
		return leftDisplaySmallSlots;
	}

	public void setLeftDisplaySmallSlots(int[] leftDisplaySmallSlots) {
		this.leftDisplaySmallSlots = leftDisplaySmallSlots;
	}

	public int[] getLeftDisplayMediumSlots() {
		return leftDisplayMediumSlots;
	}

	public void setLeftDisplayMediumSlots(int[] leftDisplayMediumSlots) {
		this.leftDisplayMediumSlots = leftDisplayMediumSlots;
	}

	public int[] getLeftDisplayLargeSlots() {
		return leftDisplayLargeSlots;
	}

	public void setLeftDisplayLargeSlots(int[] leftDisplayLargeSlots) {
		this.leftDisplayLargeSlots = leftDisplayLargeSlots;
	}

	public int[] getRightDisplaySmallSlots() {
		return rightDisplaySmallSlots;
	}

	public void setRightDisplaySmallSlots(int[] rightDisplaySmallSlots) {
		this.rightDisplaySmallSlots = rightDisplaySmallSlots;
	}

	public int[] getRightDisplayMediumSlots() {
		return rightDisplayMediumSlots;
	}

	public void setRightDisplayMediumSlots(int[] rightDisplayMediumSlots) {
		this.rightDisplayMediumSlots = rightDisplayMediumSlots;
	}

	public int[] getRightDisplayLargeSlots() {
		return rightDisplayLargeSlots;
	}

	public void setRightDisplayLargeSlots(int[] rightDisplayLargeSlots) {
		this.rightDisplayLargeSlots = rightDisplayLargeSlots;
	}

	public ItemStack getSmallItem() {
		return smallItem;
	}

	public void setSmallItem(ItemStack smallItem) {
		this.smallItem = smallItem;
	}

	public ItemStack getMediumItem() {
		return mediumItem;
	}

	public void setMediumItem(ItemStack mediumItem) {
		this.mediumItem = mediumItem;
	}

	public ItemStack getLargeItem() {
		return largeItem;
	}

	public void setLargeItem(ItemStack largeItem) {
		this.largeItem = largeItem;
	}

	public int getSmallCurrency() {
		return smallCurrency;
	}

	public void setSmallCurrency(int smallCurrency) {
		this.smallCurrency = smallCurrency;
	}

	public int getMediumCurrency() {
		return mediumCurrency;
	}

	public void setMediumCurrency(int mediumCurrency) {
		this.mediumCurrency = mediumCurrency;
	}

	public int getLargeCurrency() {
		return largeCurrency;
	}

	public void setLargeCurrency(int largeCurrency) {
		this.largeCurrency = largeCurrency;
	}

	public ItemStack getSmallItemDisplay() {
		return smallItemDisplay;
	}

	public void setSmallItemDisplay(ItemStack smallItemDisplay) {
		this.smallItemDisplay = smallItemDisplay;
	}

	public ItemStack getMediumItemDisplay() {
		return mediumItemDisplay;
	}

	public void setMediumItemDisplay(ItemStack mediumItemDisplay) {
		this.mediumItemDisplay = mediumItemDisplay;
	}

	public ItemStack getLargeItemDisplay() {
		return largeItemDisplay;
	}

	public void setLargeItemDisplay(ItemStack largeItemDisplay) {
		this.largeItemDisplay = largeItemDisplay;
	}
	
	@Override
	public CurrencyLayout clone() {
		CurrencyLayout clone = new CurrencyLayout();
		
		clone.setRows( getRows() );
		clone.setAcceptItem( getAcceptedItem() );
		clone.setAcceptedItem( getAcceptedItem() );
		clone.setRefuseItem( getRefuseItem() );
		clone.setSeperatorItem( getSeperatorItem() );
		clone.setLeftSlots( getLeftSlots() );
		clone.setRightSlots( getRightSlots() );
		
		
		clone.setSmallCurrency( getSmallCurrency() );
		clone.setMediumCurrency( getMediumCurrency() );
		clone.setLargeCurrency( getLargeCurrency() );
		
		clone.setSmallItem( getSmallItem() );
		clone.setMediumItem( getMediumItem() );
		clone.setLargeItem( getLargeItem() );
		
		clone.setSmallItemDisplay( getSmallItemDisplay() );
		clone.setMediumItemDisplay( getMediumItemDisplay() );
		clone.setLargeItemDisplay( getLargeItemDisplay() );
				
		return clone;
	}
	
	private static void setTradeSlots( CurrencyLayout layout ){
		
		// Set trade slots
		int tradingSlots = ( layout.getRows() - 2 ) * 4;
		
		layout.setLeftSlots( new int[tradingSlots] );
		layout.setRightSlots( new int[tradingSlots] );
		
		for ( int i = 0; i < tradingSlots; i++ ){
			layout.getLeftSlots()[ i ] 	= 9 +  9 * ( i / 4 ) + i % 4;
			layout.getRightSlots()[ i ] = 14 + 9 * ( i / 4 ) + i % 4;
		}

		layout.setSeperatorSlots( new int[ layout.getRows() - 1 ] );
		for ( int row = 0; row < layout.getRows() - 1; row++ ) layout.getSeperatorSlots()[ row ] = 13  + ( 9 * row );
			
	}

	private static void setCurrencySlots( CurrencyLayout layout ) {

		int add = 9 * ( layout.getRows() - 2);
		
		// Add currency slots
		layout.setLeftSmallSlots( 	new int[]{ 9  + add } );
		layout.setLeftMediumSlots(	new int[]{ 10 + add } );
		layout.setLeftLargeSlots(	new int[]{ 11 + add } );
		
		// Display currency slots
		layout.setLeftDisplaySmallSlots( 	new int[]{ 0 } );
		layout.setLeftDisplayMediumSlots( 	new int[]{ 1 } );
		layout.setLeftDisplayLargeSlots( 	new int[]{ 2 } );

		layout.setRightDisplaySmallSlots( 	new int[]{ 6 } );
		layout.setRightDisplayMediumSlots( 	new int[]{ 7 } );
		layout.setRightDisplayLargeSlots( 	new int[]{ 8 } );
	}
	
	private static void setCurrencyItems( CurrencyLayout layout, String smallLabel, String mediumLabel, String largeLabel ) {
		
		CraftItemStack smallItem	=	new CraftItemStack( 371, layout.smallCurrency  );
		smallItem.getHandle().c( smallLabel );
		
		CraftItemStack mediumItem	= 	new CraftItemStack( 266, layout.mediumCurrency );
		mediumItem.getHandle().c( mediumLabel );
		
		CraftItemStack largeItem	=	new CraftItemStack( 41,  layout.largeCurrency  );
		largeItem.getHandle().c( largeLabel );
		
		layout.setSmallItem( smallItem );
		layout.setMediumItem( mediumItem );
		layout.setLargeItem( largeItem );
				
	}
	
	private static void setCurrencyDisplayItems( CurrencyLayout layout, String smallLabel, String mediumLabel, String largeLabel ) {
		
		CraftItemStack smallItem	=	new CraftItemStack( 371 );
		smallItem.getHandle().c( smallLabel );
		
		CraftItemStack mediumItem	= 	new CraftItemStack( 266 );
		mediumItem.getHandle().c( mediumLabel );
		
		CraftItemStack largeItem	=	new CraftItemStack( 41 );
		largeItem.getHandle().c( largeLabel );
		
		layout.setSmallItemDisplay( smallItem );
		layout.setMediumItemDisplay( mediumItem );
		layout.setLargeItemDisplay( largeItem );
		
	}
	
	private static void setCurrencyAmounts(CurrencyLayout layout) {
		
		layout.setSmallCurrency( 1 );
		layout.setMediumCurrency( 10 );
		layout.setLargeCurrency( 50 );
		
	}

	public static CurrencyLayout createDefaultLayout( int rows, Economy economy ) {

		CurrencyLayout layout = new CurrencyLayout();
		layout.setRows( rows );

		ItemLayout.setActionSlots( layout );
		ItemLayout.setActionItems( layout );
		
		setTradeSlots( layout );
		setCurrencySlots( layout );
		setCurrencyAmounts( layout );
		
		String smallLabel 	= _("trade.item-label.currency.change.small", new String[][] { { "%amount%", Matcher.quoteReplacement(economy.format( layout.getSmallCurrency() ) ) } });
		String mediumLabel 	= _("trade.item-label.currency.change.medium", new String[][] { { "%amount%", Matcher.quoteReplacement(economy.format( layout.getMediumCurrency() ) ) } });
		String largeLabel 	= _("trade.item-label.currency.change.large", new String[][] { { "%amount%", Matcher.quoteReplacement(economy.format( layout.getLargeCurrency() ) ) } });
		
		setCurrencyItems( layout, smallLabel, mediumLabel, largeLabel );
		
		String smallLabelDisplay 	= _("trade.item-label.currency.display.small", new String[][] { { "%amount%", Matcher.quoteReplacement(economy.format( layout.getSmallCurrency() ) ) } });
		String mediumLabelDisplay 	= _("trade.item-label.currency.display.medium", new String[][] { { "%amount%", Matcher.quoteReplacement(economy.format( layout.getMediumCurrency() ) ) } });
		String largeLabelDisplay 	= _("trade.item-label.currency.display.large", new String[][] { { "%amount%", Matcher.quoteReplacement(economy.format( layout.getLargeCurrency() ) ) } });
		
		setCurrencyDisplayItems( layout, smallLabelDisplay, mediumLabelDisplay, largeLabelDisplay );
		
		return layout;
	}	

}

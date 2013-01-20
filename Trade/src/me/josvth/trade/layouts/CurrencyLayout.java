package me.josvth.trade.layouts;

import java.util.Arrays;
import java.util.regex.Matcher;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.josvth.trade.managers.LanguageManager._;
import static me.josvth.trade.managers.ExtensionManager.economy;

public class CurrencyLayout extends ItemLayout {

	// Slots		
	// Change currency slots
	int[] changeSmallSlots;
	int[] changeMediumSlots;
	int[] changeLargeSlots;

	// Display currency slots
	int[] leftDisplaySmallSlots;
	int[] leftDisplayMediumSlots;
	int[] leftDisplayLargeSlots;

	int[] rightDisplaySmallSlots;
	int[] rightDisplayMediumSlots;
	int[] rightDisplayLargeSlots;

	// Items
	// Currency display
	ItemStack smallDisplayItem;
	ItemStack mediumDisplayItem;
	ItemStack largeDisplayItem;

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

		System.out.print( getSmallChangeItem().toString() );
		System.out.print( getMediumChangeItem().toString() );
		System.out.print( getLargeChangeItem().toString() );

		// Set left small items
		for ( int slot : getChangeSmallSlots() ) inventory.setItem( slot, getSmallChangeItem() );

		// Set left medium items
		for ( int slot : getChangeMediumSlots() ) inventory.setItem( slot, getMediumChangeItem() );

		// Set left large items
		for ( int slot : getChangeLargeSlots() ) inventory.setItem( slot, getLargeChangeItem() );

		super.fillInventory(inventory);

	}

	// Slots
	// Change
	public int[] getChangeSmallSlots() {
		return changeSmallSlots;
	}

	public void setChangeSmallSlots(int[] changeSmallSlots) {
		this.changeSmallSlots = changeSmallSlots;
	}

	public int[] getChangeMediumSlots() {
		return changeMediumSlots;
	}

	public void setChangeMediumSlots(int[] changeMediumSlots) {
		this.changeMediumSlots = changeMediumSlots;
	}

	public int[] getChangeLargeSlots() {
		return changeLargeSlots;
	}

	public void setChangeLargeSlots(int[] changeLargeSlots) {
		this.changeLargeSlots = changeLargeSlots;
	}

	// Display
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

	// Items
	// Change
	public ItemStack getSmallChangeItem() {
		return smallItem;
	}

	public void setSmallChangeItem(ItemStack smallItem) {
		this.smallItem = smallItem;
	}

	public ItemStack getMediumChangeItem() {
		return mediumItem;
	}

	public void setMediumChangeItem(ItemStack mediumItem) {
		this.mediumItem = mediumItem;
	}

	public ItemStack getLargeChangeItem() {
		return largeItem;
	}

	public void setLargeChangeItem(ItemStack largeItem) {
		this.largeItem = largeItem;
	}

	//Display
	public ItemStack getSmallDisplayItem() {
		return smallDisplayItem;
	}

	public void setSmallDisplayItem(ItemStack smallDisplayItem) {
		this.smallDisplayItem = smallDisplayItem;
	}

	public ItemStack getMediumDisplayItem() {
		return mediumDisplayItem;
	}

	public void setMediumDisplayItem(ItemStack mediumDisplayItem) {
		this.mediumDisplayItem = mediumDisplayItem;
	}

	public ItemStack getLargeDisplayItem() {
		return largeDisplayItem;
	}

	public void setLargeDisplayItem(ItemStack largeDisplayItem) {
		this.largeDisplayItem = largeDisplayItem;
	}

	// Currency
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

		clone.setSmallChangeItem( getSmallChangeItem() );
		clone.setMediumChangeItem( getMediumChangeItem() );
		clone.setLargeChangeItem( getLargeChangeItem() );

		clone.setSmallDisplayItem( getSmallDisplayItem() );
		clone.setMediumDisplayItem( getMediumDisplayItem() );
		clone.setLargeDisplayItem( getLargeDisplayItem() );

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
		layout.setChangeSmallSlots( 	new int[]{ 9  + add } );
		layout.setChangeMediumSlots(	new int[]{ 10 + add } );
		layout.setChangeLargeSlots(		new int[]{ 11 + add } );

		// Display currency slots
		layout.setLeftDisplaySmallSlots( 	new int[]{ 0 } );
		layout.setLeftDisplayMediumSlots( 	new int[]{ 1 } );
		layout.setLeftDisplayLargeSlots( 	new int[]{ 2 } );

		layout.setRightDisplaySmallSlots( 	new int[]{ 6 } );
		layout.setRightDisplayMediumSlots( 	new int[]{ 7 } );
		layout.setRightDisplayLargeSlots( 	new int[]{ 8 } );
	}

	private static void setCurrencyChangeItems( CurrencyLayout layout, ItemStack small, ItemStack medium, ItemStack large  ) {

		layout.setSmallChangeItem( small );
		layout.setMediumChangeItem( medium );
		layout.setLargeChangeItem( large );

	}

	private static void setCurrencyDisplayItems( CurrencyLayout layout, ItemStack small, ItemStack medium, ItemStack large  ) {

		layout.setSmallDisplayItem( small );
		layout.setMediumDisplayItem( medium );
		layout.setLargeDisplayItem( large );

	}

	private static void setCurrencyAmounts(CurrencyLayout layout, int small, int medium, int large ) {

		layout.setSmallCurrency( small );
		layout.setMediumCurrency( medium );
		layout.setLargeCurrency( large );

	}

	public static CurrencyLayout createDefaultLayout( int rows ) {

		CurrencyLayout layout = new CurrencyLayout();
		layout.setRows( rows );

		ItemLayout.setActionSlots( layout );
		ItemLayout.setActionItems( layout );

		setTradeSlots( layout );
		setCurrencySlots( layout );
		setCurrencyAmounts( layout, 1, 10, 50 );

		String smallCurrency 	= economy.format( 1.0 );
		smallCurrency 	= Matcher.quoteReplacement( ( smallCurrency == null )? String.valueOf( 1.0 ) : smallCurrency );
		
		String mediumCurrency 	= economy.format( 10.0 );
		mediumCurrency 	= Matcher.quoteReplacement( ( mediumCurrency == null )? String.valueOf( 10.0 ) : mediumCurrency );

		String largeCurrency 	= economy.format( 50.0 );
		largeCurrency 	= Matcher.quoteReplacement( ( largeCurrency == null )? String.valueOf( 50.0 ) : largeCurrency );

		String loreAddSmall 	= _("trade.items.currency.lores.add", new String[][] { { "%amount%", smallCurrency } } );
		String loreAddMedium	= _("trade.items.currency.lores.add", new String[][] { { "%amount%", mediumCurrency } } );
		String loreAddLarge		= _("trade.items.currency.lores.add", new String[][] { { "%amount%", largeCurrency } } );
		
		String loreRemoveSmall 	= _("trade.items.currency.lores.remove", new String[][] { { "%amount%", smallCurrency } } );
		String loreRemoveMedium	= _("trade.items.currency.lores.remove", new String[][] { { "%amount%", mediumCurrency } } );
		String loreRemoveLarge	= _("trade.items.currency.lores.remove", new String[][] { { "%amount%", largeCurrency } } );
				
		// Create change items
		ItemStack smallChange 	= new ItemStack( 371, 1 );
		ItemMeta smallChangeMeta = smallChange.getItemMeta();
		smallChangeMeta.setDisplayName( _("trade.items.currency.labels.small", new String[][] { { "%amount%", smallCurrency } } ) );
		smallChangeMeta.setLore( Arrays.asList( new String[] { loreAddSmall, loreRemoveSmall }) );
		smallChange.setItemMeta( smallChangeMeta );
		
		ItemStack mediumChange	= new ItemStack( 266, 10 );
		ItemMeta mediumChangeMeta = mediumChange.getItemMeta();
		mediumChangeMeta.setDisplayName( _("trade.items.currency.labels.medium", new String[][] { { "%amount%", mediumCurrency } } ) );
		mediumChangeMeta.setLore( Arrays.asList( new String[] { loreAddMedium, loreRemoveMedium }) );
		mediumChange.setItemMeta( mediumChangeMeta );
		
		ItemStack largeChange 	= new ItemStack( 41, 50 );
		ItemMeta largeChangeMeta = largeChange.getItemMeta();
		largeChangeMeta.setDisplayName( _("trade.items.currency.labels.large", new String[][] { { "%amount%", largeCurrency } } ) );
		largeChangeMeta.setLore( Arrays.asList( new String[] { loreAddLarge, loreRemoveLarge }) );
		largeChange.setItemMeta( largeChangeMeta );
		
		setCurrencyChangeItems( layout, smallChange, mediumChange, largeChange );

		// Create display items
		ItemStack smallDisplay 	= new ItemStack( 371 );
		ItemMeta smallDisplayMeta = smallDisplay.getItemMeta();
		smallDisplayMeta.setLore( Arrays.asList( new String[] { loreRemoveSmall }) );
		smallDisplay.setItemMeta( smallDisplayMeta );
		
		ItemStack mediumDisplay	= new ItemStack( 266 );
		ItemMeta mediumDisplayMeta = mediumDisplay.getItemMeta();
		mediumDisplayMeta.setLore( Arrays.asList( new String[] { loreRemoveMedium }) );
		mediumDisplay.setItemMeta( mediumDisplayMeta );

		ItemStack largeDisplay 	= new ItemStack( 41 );
		ItemMeta largeDisplayMeta = largeDisplay.getItemMeta();
		largeDisplayMeta.setLore( Arrays.asList( new String[] { loreRemoveLarge }) );
		largeDisplay.setItemMeta( largeDisplayMeta );
				
		setCurrencyDisplayItems( layout, smallDisplay, mediumDisplay, largeDisplay );

		return layout;
	}	

}

package me.josvth.trade.exchangeinterfaces;

import java.util.regex.Matcher;

import me.josvth.trade.layouts.CurrencyLayout;
import static me.josvth.trade.managers.LanguageManager._;
import static me.josvth.trade.managers.ExtensionManager.economy;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class CurrencyInterface extends ItemInterface {

	int leftAmount = 0; 
	int rightAmount = 0;

	CurrencyLayout layout;

	public CurrencyInterface( CurrencyLayout layout, String otherPlayerName ) {
		super( otherPlayerName, layout );
		this.layout = layout;
	}

	private CurrencyLayout getLayout(){
		return layout;
	}

	public int getCurrency( Side side ){
		switch ( side ){
		case LEFT: 	return leftAmount;
		case RIGHT: return rightAmount;
		default: return 0;
		}
	}

	public int setCurrency( int amount, Side side ){

		int large 	= 	(int) 	Math.floor(	amount / getLayout().getLargeCurrency());
		int medium 	= 	(int) 	Math.floor( ( amount - getLayout().getLargeCurrency() * large ) / getLayout().getMediumCurrency() );
		int small 	= 	(int) 	Math.floor( ( amount - getLayout().getLargeCurrency() * large - getLayout().getMediumCurrency() * medium ) ) / getLayout().getSmallCurrency();

		ItemStack largeItem = getLayout().getLargeDisplayItem();
		largeItem.setAmount( large );
			
		ItemStack mediumItem = getLayout().getMediumDisplayItem();
		mediumItem.setAmount( medium );
	
		ItemStack smallItem = getLayout().getSmallDisplayItem();
		smallItem.setAmount( small );
		
		ItemMeta largeMeta = largeItem.getItemMeta();
		ItemMeta mediumMeta = mediumItem.getItemMeta();
		ItemMeta smallMeta = smallItem.getItemMeta();

		switch ( side ){
		case LEFT:

			String labelLeft = _( "trade.items.currency.labels.display.left", new String[][]{ { "%amount%", Matcher.quoteReplacement( economy.format( amount ) ) } } );

			for ( int slot : getLayout().getLeftDisplayLargeSlots() ) {
				if (large > 0) {
					largeMeta.setDisplayName( labelLeft );
					largeItem.setItemMeta( largeMeta );
					inventory.setItem( slot , largeItem ) ;
				} else 
					inventory.clear( slot );
			}

			for ( int slot : getLayout().getLeftDisplayMediumSlots() ) {
				if (medium > 0) {
					mediumMeta.setDisplayName( labelLeft );
					mediumItem.setItemMeta( mediumMeta );
					inventory.setItem( slot , mediumItem ) ;
				} else 
					inventory.clear( slot );
			}

			for ( int slot : getLayout().getLeftDisplaySmallSlots() ) {
				if (small > 0) {
					smallMeta.setDisplayName( labelLeft );
					smallItem.setItemMeta( smallMeta );
					inventory.setItem( slot , smallItem ) ;
				} else 
					inventory.clear( slot );
			}

			leftAmount = amount;

			break;

		case RIGHT:
			
			largeMeta.setLore( null );	// TODO do this somewhere else (maybe adding another item)?
			mediumMeta.setLore( null );	// TODO do this somewhere else (maybe adding another item)?
			smallMeta.setLore( null );	// TODO do this somewhere else (maybe adding another item)?

			String labelRight = 
			_( "trade.items.currency.labels.display.right", new String[][]{ 
					{ "%playername%", getNameOtherPlayer() },
					{ "%amount%", Matcher.quoteReplacement( economy.format( amount ) ) }
			} );

			for ( int slot : getLayout().getRightDisplayLargeSlots() ) {
				if (large > 0) {
					largeMeta.setDisplayName( labelRight );
					largeItem.setItemMeta( largeMeta );
					inventory.setItem( slot , largeItem ) ;
				} else 
					inventory.clear( slot );
			}

			for ( int slot : getLayout().getRightDisplayMediumSlots() ) {
				if (medium > 0) {
					mediumMeta.setDisplayName( labelRight );
					mediumItem.setItemMeta( mediumMeta );
					inventory.setItem( slot , mediumItem ) ;
				} else 
					inventory.clear( slot );
			}

			for ( int slot : getLayout().getRightDisplaySmallSlots() ) {
				if (small > 0) {
					smallMeta.setDisplayName( labelRight );
					smallItem.setItemMeta( smallMeta );
					inventory.setItem( slot , smallItem ) ;
				} else 
					inventory.clear( slot );
			}

			rightAmount = amount;

			break;			
		}

		return 0;
	}

	// Returns the amount represented by the currency slot 
	public int getCurrencySlot(int slot) {
		for ( int small 	: getLayout().getChangeSmallSlots() 	) if ( small 	== slot ) return getLayout().getSmallCurrency();
		for ( int medium 	: getLayout().getChangeMediumSlots() 	) if ( medium 	== slot ) return getLayout().getMediumCurrency();
		for ( int large 	: getLayout().getChangeLargeSlots() 	) if ( large 	== slot ) return getLayout().getLargeCurrency();

		for ( int smallD 	: getLayout().getLeftDisplaySmallSlots() 	) if ( smallD 	== slot ) return -1*getLayout().getSmallCurrency();
		for ( int mediumD 	: getLayout().getLeftDisplayMediumSlots() 	) if ( mediumD 	== slot ) return -1*getLayout().getMediumCurrency();
		for ( int largeD 	: getLayout().getLeftDisplayLargeSlots() 	) if ( largeD 	== slot ) return -1*getLayout().getLargeCurrency();

		return 0;
	}

}

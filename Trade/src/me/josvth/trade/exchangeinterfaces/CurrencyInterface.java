package me.josvth.trade.exchangeinterfaces;

import me.josvth.trade.layouts.CurrencyLayout;

import org.bukkit.inventory.ItemStack;

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

		ItemStack largeItem = getLayout().getLargeItem();
		largeItem.setAmount( large );

		ItemStack mediumItem = getLayout().getMediumItem();
		mediumItem.setAmount( medium );

		ItemStack smallItem = getLayout().getSmallItem();
		smallItem.setAmount( small );

		
		switch ( side ){
		case LEFT:

			for ( int slot : getLayout().getLeftDisplayLargeSlots() ) {
				if (large > 0)
					inventory.setItem( slot , largeItem ) ;
				else 
					inventory.clear( slot );
			}

			for ( int slot : getLayout().getLeftDisplayMediumSlots() ) {
				if (medium > 0) 
					inventory.setItem( slot , mediumItem ) ;
				else 
					inventory.clear( slot );
			}

			for ( int slot : getLayout().getLeftDisplaySmallSlots() ) {
				if (small > 0)
					inventory.setItem( slot , smallItem ) ;
				else 
					inventory.clear( slot );
			}

			leftAmount = amount;

			break;

		case RIGHT:

			for ( int slot : getLayout().getRightDisplayLargeSlots() ) {
				if (large > 0) 
					inventory.setItem( slot , largeItem ) ;
				else 
					inventory.clear( slot );
			}

			for ( int slot : getLayout().getRightDisplayMediumSlots() ) {
				if (medium > 0) 
					inventory.setItem( slot , mediumItem ) ;
				else 
					inventory.clear( slot );
			}

			for ( int slot : getLayout().getRightDisplaySmallSlots() ) {
				if (small > 0)
					inventory.setItem( slot , smallItem ) ;
				else 
					inventory.clear( slot );
			}

			rightAmount = amount;

			break;			
		}

		return 0;
	}

	// Returns the amount represented by the currency slot 
	public int getCurrencySlot(int slot) {
		for ( int small 	: getLayout().getLeftSmallSlots() 	) if ( small 	== slot ) return getLayout().getSmallCurrency();
		for ( int medium 	: getLayout().getLeftMediumSlots() 	) if ( medium 	== slot ) return getLayout().getMediumCurrency();
		for ( int large 	: getLayout().getLeftLargeSlots() 	) if ( large 	== slot ) return getLayout().getLargeCurrency();
		return -1;
	}
}

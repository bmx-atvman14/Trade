package me.josvth.trade.listeners;


import me.josvth.trade.Trade;
import me.josvth.trade.exchangeinterfaces.CurrencyInterface;
import me.josvth.trade.exchangeinterfaces.ItemInterface;
import me.josvth.trade.exchangeinterfaces.ItemInterface.Side;
import me.josvth.trade.exchanges.CurrencyExchange;
import me.josvth.trade.exchanges.ItemExchange;
import me.josvth.trade.managers.ConfigurationManager;
import me.josvth.trade.managers.LanguageManager;
import me.josvth.trade.managers.RequestManager;
import me.josvth.trade.managers.RequestManager.RequestMethod;
import me.josvth.trade.managers.RequestManager.RequestRestriction;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;


public class TradeListener implements Listener{

	Trade plugin;

	ConfigurationManager configurationManager; 
	RequestManager requestManager;
	LanguageManager languageManager;


	public TradeListener(Trade instance){
		plugin = instance;

		requestManager = plugin.getRequestManager();
		languageManager = plugin.getLanguageManager();
		configurationManager = plugin.getConfigurationManager();

		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}

	@EventHandler
	public void onPlayerAbandon(InventoryCloseEvent event){
		Player player = (Player)event.getPlayer();
		ItemExchange exchange = requestManager.getActiveExchanges().get( player );
		if( exchange != null && exchange.isInProgress() ) exchange.refuse(player);
	}

	@EventHandler
	public void onTradeInventoryClick(InventoryClickEvent event){

		Player player = (Player)event.getWhoClicked();

		ItemExchange exchange = requestManager.getActiveExchanges().get( player );

		if( exchange == null ) return;

		if(event.isShiftClick()){
			cancelInventoryClickEvent( event );
			return;
		}

		int inventorySlot = event.getRawSlot();

		if ( inventorySlot == -1 ) return; 		// Return if player click outside of the inventory

		// Gets the trading inventory of this player
		ItemInterface itemInterface = exchange.getInterface( player );

		// Returns if the player is using the bottom part of the inventory
		if(inventorySlot > itemInterface.getSize() - 1) return;

		// Grabs the item thats on the cursor
		ItemStack cursor = event.getCursor();

		// Grabs the item on the clicked slot
		ItemStack currentSlot = event.getCurrentItem();

		if (itemInterface.isAcceptSlot(inventorySlot)) {
			if ( exchange.hasAccepted(player) )
				exchange.denyTrade(player);
			else
				exchange.acceptTrade(player);
			cancelInventoryClickEvent( event );;
			return;
		}

		if (itemInterface.isRefuseSlot(inventorySlot)) {
			exchange.refuse(player);
			cancelInventoryClickEvent( event );
			return;
		}

		// Currency trade
		if ( exchange instanceof CurrencyExchange ) {

			CurrencyExchange currencyExchange = (CurrencyExchange) exchange;

			CurrencyInterface currencyInterface = (CurrencyInterface) itemInterface;

			int amount = currencyInterface.getCurrencySlot( inventorySlot );

			if ( amount != 0 ) {
				if ( amount > 0 )
					if ( event.isRightClick() )
						currencyExchange.removeCurrency( player, amount );
					else
						currencyExchange.addCurrency( player, amount );
				else
					currencyExchange.removeCurrency( player, -1 * amount );
				cancelInventoryClickEvent( event );
				return;
			}
		}

		int tradingSlotIndex = itemInterface.getTradeIndex( inventorySlot, Side.LEFT );

		if ( configurationManager.debugMode ) {
			player.sendMessage("TSI: " + tradingSlotIndex);
		}

		// Checks if player can use the slot
		if (tradingSlotIndex == -1) {
			languageManager.sendMessage(player, "trade.cannot-use-slot");
			cancelInventoryClickEvent( event );
			return;
		}

		// TODO make this more efficient

		ItemStack newSlot = null;

		// Calculate what is being added and removed

		if ( currentSlot.getTypeId() == 0 && cursor.getTypeId() == 0 ) return;		// nothing changed so return;

		if ( event.isLeftClick() ) {
			if ( currentSlot.getTypeId() == cursor.getTypeId() ){				// add all items to the stack
				newSlot = currentSlot.clone();
				newSlot.setAmount( currentSlot.getAmount() + cursor.getAmount() );
			} else {															// replace items
				newSlot = cursor.clone();
			}	
		}

		if ( event.isRightClick() ) {

			// split stack
			if ( currentSlot.getTypeId() != 0 && cursor.getTypeId() == 0 ){
				newSlot = currentSlot.clone();
				newSlot.setAmount( currentSlot.getAmount() / 2 );
			}

			// place all
			if ( currentSlot.getTypeId() != cursor.getTypeId() && cursor.getTypeId() != 0 ) {
				newSlot = cursor.clone();
			}

			// add 1 item to the stack
			if ( currentSlot.getTypeId() == cursor.getTypeId() && cursor.getTypeId() != 0 ) {
				newSlot = currentSlot.clone();
				newSlot.setAmount( currentSlot.getAmount() + 1 );
			}

			// place 1 item
			if ( currentSlot.getTypeId() == 0 && cursor.getTypeId() != 0 ) { 
				newSlot = cursor.clone();
				newSlot.setAmount( 1 );
			}

		}

		if ( configurationManager.debugMode ) {
			player.sendMessage("Cursor: " + cursor.toString() );
			player.sendMessage("CurrentSlot: " + currentSlot.toString() );
			player.sendMessage("NewSlot: " + newSlot );
		}

		for ( int black : configurationManager.blacklistedItems ) {
			if ( black == newSlot.getTypeId() ) {
				languageManager.sendMessage( player , "cannot-trade-item" );
				cancelInventoryClickEvent( event );
				return;
			}
		}

		// Gets the other players trading inventory
		ItemInterface otherInterface = exchange.getInterface( exchange.getOtherPlayer(player) );

		// Set the item on the right side of the others trading inventory
		otherInterface.setTradeItem( tradingSlotIndex, newSlot, Side.RIGHT );

		exchange.cancelAcceptOf( exchange.getOtherPlayer( player ) );
	}

	private void cancelInventoryClickEvent( InventoryClickEvent event ) {
		event.setCursor( event.getCursor() );
		event.setCurrentItem( event.getCurrentItem() );
		event.setCancelled( true );
		event.setResult( Result.DENY );
	}
	
	@EventHandler
	public void onRightClickPlayer(PlayerInteractEntityEvent event){

		if(!(event.getRightClicked() instanceof Player)) return;

		Player requester = event.getPlayer();
		Player requested = (Player)event.getRightClicked();

		boolean sneaking = requester.isSneaking();

		RequestRestriction restriction = requestManager.mayRequest( requester, requested, sneaking? RequestMethod.SHIFT_RIGHT_CLICK : RequestMethod.RIGHT_CLICK );

		if ( restriction.equals( RequestRestriction.IN_TRADE ) ) languageManager.sendMessage( requester, "request.in-trade");			

		if ( restriction.equals( RequestRestriction.IGNORING ) ) languageManager.sendMessage( requester, "request.ignoring.is-ignoring", new String[][]{ {"%playername%", requested.getName()} } );

		if ( restriction.equals( RequestRestriction.WAIT ) ) languageManager.sendMessage( requester, "request.please-wait");

		if ( configurationManager.debugMode ) requester.sendMessage( restriction.toString() );

		if ( !restriction.equals( RequestRestriction.ALLOW ) ) return;

		if ( requestManager.isRequested( requester, requested ) ) // checks if this is a response
			requestManager.acceptRequest( requester, requested );
		else
			requestManager.makeRequest( requester, requested );
	}
}

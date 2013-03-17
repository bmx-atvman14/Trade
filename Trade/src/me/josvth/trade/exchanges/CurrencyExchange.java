package me.josvth.trade.exchanges;

import java.util.regex.Matcher;

import me.josvth.trade.Trade;
import me.josvth.trade.exchangeinterfaces.CurrencyInterface;
import me.josvth.trade.exchangeinterfaces.ItemInterface.Side;
import me.josvth.trade.layouts.CurrencyLayout;
import static me.josvth.trade.managers.LanguageManager._s;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;

public class CurrencyExchange extends ItemExchange{

	CurrencyLayout layout;
	Economy economy;

	CurrencyInterface p1gui, p2gui;

	public CurrencyExchange(Trade instance, Player playerA, Player playerB) {
		super( instance, playerA, playerB );
		economy = plugin.getExtensionManager().getEconomy();
	}

	@Override
	public void initialize() {

		setRows();

		layout = plugin.getLayoutManager().getCurrencyLayout( rows );

		p1gui = new CurrencyInterface( layout, p2.getName() );
		p2gui = new CurrencyInterface( layout, p1.getName() );

	}

	public void addCurrency( Player player, int amount ) {

		String formatAmount = Matcher.quoteReplacement(economy.format(Math.abs(amount)));

		Player other = getOtherPlayer( player );

		CurrencyInterface currencyInterface = getInterface( player );
		CurrencyInterface othersInterface 	= getInterface( other  );

		// Take absolute value of amount for easier handling
		amount = Math.abs( amount );

		if(!economy.has(player.getName(), amount)){		// Check if the player has the money he wants to add
			_s(player, "trade.currency.no-balance", new String[][]{ {"%amount%", formatAmount} });
			return;
		}

		// Withdraw money
		EconomyResponse response = economy.withdrawPlayer( player.getName(), amount );

		// Update interfaces
		int newAmount = currencyInterface.getCurrency( Side.LEFT ) + amount;
		currencyInterface.setCurrency( newAmount, Side.LEFT );
		othersInterface.setCurrency( newAmount , Side.RIGHT );

		// Send messages
		_s(player, "trade.currency.add.self", 
				new String[][]{ {"%amount%", formatAmount}, {"%balance%", Matcher.quoteReplacement(economy.format(response.balance))} });
		_s(other, "trade.currency.add.other", 
				new String[][]{ {"%playername%", player.getName()}, {"%amount%", formatAmount} });

		// Just to be sure cancel the accepts
		denyTrade( player );
		cancelAcceptOf( other );
		if ( hasAccepted( other ) )
			_s( other, "trade.offer-changed", new String[][]{ {"%playername%", player.getName()} });

	}

	public void removeCurrency( Player player, int amount ) {

		String formatAmount = Matcher.quoteReplacement(economy.format(Math.abs(amount)));

		Player other = getOtherPlayer( player );

		CurrencyInterface currencyInterface = getInterface( player );
		CurrencyInterface othersInterface 	= getInterface( other  );

		// Check if player added enough to the trade to recall
		if( currencyInterface.getCurrency( Side.LEFT ) < amount ) {
			_s(player, "trade.currency.remove.cant", new String[][]{ {"%amount%", formatAmount} });
			return;
		}

		// Give the player back his money
		EconomyResponse response = economy.depositPlayer( player.getName(), amount );

		// Update interfaces
		int newAmount = currencyInterface.getCurrency( Side.LEFT ) - amount;
		currencyInterface.setCurrency( newAmount, Side.LEFT );
		othersInterface.setCurrency( newAmount , Side.RIGHT );

		// Send messages
		_s(player, "trade.currency.remove.self", 
				new String[][]{ {"%amount%", formatAmount}, {"%balance%", Matcher.quoteReplacement(economy.format(response.balance))} });
		_s(other, "trade.currency.remove.other", 
				new String[][]{ {"%playername%", player.getName()}, {"%amount%", formatAmount} });
		
		// Just to be sure cancel the accepts
		denyTrade( player );
		cancelAcceptOf( other );
		if ( hasAccepted( other ) )
			_s( other, "trade.offer-changed", new String[][]{ {"%playername%", player.getName()} });

	}


	@Override
	public CurrencyInterface getInterface(Player player) {
		if ( player.equals(p1) ) return p1gui;
		if ( player.equals(p2) ) return p2gui;
		return null;
	}

	@Override
	protected void giveOffers() {

		// give currency
		economy.depositPlayer(p1.getName(), p1gui.getCurrency( Side.RIGHT ));		
		economy.depositPlayer(p2.getName(), p2gui.getCurrency( Side.RIGHT  ));

		super.giveOffers();
	}

	@Override
	protected void revertOffers() {

		// revert currency
		economy.depositPlayer(p1.getName(), p1gui.getCurrency( Side.LEFT ));		
		economy.depositPlayer(p2.getName(), p2gui.getCurrency( Side.LEFT  ));

		super.revertOffers();
	}

	public int getCurrencyOffers(Player player) {
		return getInterface(player).getCurrency( Side.LEFT );
	}

}

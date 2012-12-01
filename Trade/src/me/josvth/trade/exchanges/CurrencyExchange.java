package me.josvth.trade.exchanges;

import java.util.regex.Matcher;

import me.josvth.trade.Trade;
import me.josvth.trade.exchangeinterfaces.CurrencyInterface;
import me.josvth.trade.exchangeinterfaces.ItemInterface.Side;
import me.josvth.trade.layouts.CurrencyLayout;
import me.josvth.trade.managers.LanguageManager.MessageArgument;
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

	// Note that the amount can also be negative
	public void addCurrency(Player player, int amount) {

		String formatAmount = Matcher.quoteReplacement(economy.format(Math.abs(amount)));

		Player other = getOtherPlayer( player );

		CurrencyInterface currencyInterface = getInterface( player );
		CurrencyInterface othersInterface 	= getInterface( other  );

		// Calculate adding or removing
		boolean adding = amount > 0;
		// Take absolute value of amount for easier handling
		amount = Math.abs( amount );

		if ( adding ) {		// Checks if we are adding or subtracting money to the trade

			if(!economy.has(player.getName(), amount)){		// Check if the player has the money he wants to add
				languageManager.sendMessage(player, "trade.currency.no-balance", new MessageArgument("%amount%", formatAmount));
				return;
			}

			// Withdraw money
			EconomyResponse response = economy.withdrawPlayer( player.getName(), amount );

			// Update interfaces
			int newAmount = currencyInterface.getCurrency( Side.LEFT ) + amount;
			currencyInterface.setCurrency( newAmount, Side.LEFT );
			othersInterface.setCurrency( newAmount , Side.RIGHT );

			// Send messages
			languageManager.sendMessage(player, "trade.currency.add.self", 
					new MessageArgument[]{
					new MessageArgument("%amount%",  formatAmount),
					new MessageArgument("%balance%", Matcher.quoteReplacement(economy.format(response.balance)))					
			});
			languageManager.sendMessage(other, "trade.currency.add.other", 
					new MessageArgument[]{
					new MessageArgument("%playername%",player.getName()),
					new MessageArgument("%amount%", formatAmount)
			});

			// Just to be sure cancel the accept of the other player
			super.cancelAcceptOf( other );

		} else {

			// Check if player added enough to the trade to recall
			if( currencyInterface.getCurrency( Side.LEFT ) < amount ) {
				languageManager.sendMessage(player, "trade.currency.remove.cant", new MessageArgument("%amount%", formatAmount));
				return;
			}

			// Give the player back his money
			EconomyResponse response = economy.depositPlayer( player.getName(), amount );

			// Update interfaces
			int newAmount = currencyInterface.getCurrency( Side.LEFT ) - amount;
			currencyInterface.setCurrency( newAmount, Side.LEFT );
			othersInterface.setCurrency( newAmount , Side.RIGHT );

			// Send messages
			languageManager.sendMessage(player, "trade.currency.remove.self", 
					new MessageArgument[]{
					new MessageArgument("%amount%", formatAmount),
					new MessageArgument("%balance%", Matcher.quoteReplacement(economy.format(response.balance)))					
			});
			languageManager.sendMessage(other, "trade.currency.remove.other", 
					new MessageArgument[]{
					new MessageArgument("%playername%",player.getName()),
					new MessageArgument("%amount%", formatAmount)
			});

			// Just to be sure cancel the accept of the other player
			super.cancelAcceptOf( other );

		}
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

}

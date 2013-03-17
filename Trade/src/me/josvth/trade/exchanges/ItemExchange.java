package me.josvth.trade.exchanges;

import java.io.File;

import me.josvth.trade.Trade;
import me.josvth.trade.exchangeinterfaces.ItemInterface;
import me.josvth.trade.exchangeinterfaces.ItemInterface.Side;
import me.josvth.trade.layouts.ItemLayout;
import me.josvth.trade.managers.ConfigurationManager;
import static me.josvth.trade.managers.LanguageManager._s;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemExchange {

	protected final Trade plugin;

	private final ConfigurationManager configManager;

	protected final Player p1;

	protected final Player p2;

	ItemLayout layout;

	protected int rows = 0;

	ItemInterface p1gui, p2gui;

	boolean p1acc = false, p2acc = false;

	boolean p1ref = false, p2ref = false;
	
	boolean inprogress = false;
	
	boolean finished = false;
	
	public ItemExchange(Trade instance, Player player1, Player player2) {

		plugin = instance;

		configManager = plugin.getConfigurationManager();

		p1 = player1;
		p2 = player2;

	}

	public void initialize() {

		setRows();

		layout = plugin.getLayoutManager().getItemLayout( rows );

		p1gui = new ItemInterface( p2.getName(), layout );
		p2gui = new ItemInterface( p1.getName(), layout );

	}

	protected void setRows() {

		int rowsP1 = getAllowedGuiRows(p1);
		int rowsP2 = getAllowedGuiRows(p2);

		// Yes this can be done with a xor but I'm lazy

		if(rowsP2 > rowsP1){
			if( configManager.useBiggestInterface )
				rows = rowsP2;
			else
				rows = rowsP1;
		}else{
			if( configManager.useBiggestInterface )
				rows = rowsP1;
			else
				rows = rowsP2;
		}

	}

	public boolean isInProgress(){
		return inprogress;
	}

	public Player getP1() {
		return p1;
	}
	
	public Player getP2() {
		return p2;
	}
	
	public void acceptTrade(Player who) {

		if ( who.equals( p1 ) ) {
			p1acc = true;
			getInterface( p1 ).accept();
			getInterface( p2 ).acceptOther();
		} else if ( who.equals( p2 ) ) {
			p2acc = true;
			getInterface( p2 ).accept();
			getInterface( p1 ).acceptOther();
		} else return;

		_s( who, "trade.accept.self" );
		_s( getOtherPlayer(who), "trade.accept.other", new String[][]{ {"%playername%", who.getName()} });

		if(p1acc && p2acc) finish();

	}

	public void denyTrade(Player who) {		

		if ( who.equals(p1) && p1acc ) {

			p1acc = false;

			// Change gui accept states
			getInterface( p1 ).deny();
			getInterface( p2 ).denyOther();

			return;
		}

		if ( who.equals(p2) && p2acc ) {

			p2acc = false;

			// Change gui accept states
			getInterface( p2 ).deny();
			getInterface( p1 ).denyOther();

			return;
		}

	}

	public void cancelAcceptOf(Player player) {

		if ( player.equals( p1 ) && p1acc ) {

			p1acc = false;

			// Change gui accept states
			getInterface( p1 ).deny();
			getInterface( p2 ).denyOther();

			return;
		}

		if ( player.equals( p2 ) && p2acc ) {

			p2acc = false;

			// Change gui accept states
			getInterface( p2 ).deny();
			getInterface( p1 ).denyOther();

			return;
		}

	}

	public void refuse(Player player) {

		if ( player.equals( p1 ) )
			p1ref = true;
		else if ( player.equals( p2 ) )
			p2ref = true;
		else return;
		
		_s(player, "trade.refuse.self");
		_s(getOtherPlayer(player),"trade.refuse.other", new String[][]{ {"%playername%", player.getName()} });

		stop();
		
	}

	public boolean hasAccepted(Player player) {
		if (player.equals(p1)) {
			return p1acc;
		}else if (player.equals(p2)) {
			return p2acc;
		}else{
			return false;
		}
	}

	public boolean hasRefused(Player player) {
		if (player.equals(p1)) {
			return p1ref;
		}else if (player.equals(p2)) {
			return p2ref;
		}else{
			return false;
		}
	}
	
	public ItemStack[] getOffers(Player player) {
		return ItemInterface.removeNullItems( getInterface(player).getItems(Side.LEFT) );
	}
	
	public Player getOtherPlayer(Player player) {
		if ( player.equals(p1) ) return p2;
		if ( player.equals(p2) ) return p1;
		return null;
	}

	public ItemInterface getInterface(Player player){
		if ( player.equals(p1) ) return p1gui;
		if ( player.equals(p2) ) return p2gui;
		return null;
	}

	public void start() {

		if ( finished ) 
			throw new IllegalStateException("Cannot start finished exchange.");
		
		if ( getInterface( p1 ) == null || getInterface( p2 ) == null ) return;			//TODO Throw exception?

		p1.openInventory( getInterface( p1 ).getInventory() );
		p2.openInventory( getInterface( p2 ).getInventory() );

		_s(p1, "trade.start", new String[][]{ {"%playername%", p2.getName()} });
		_s(p2, "trade.start", new String[][]{ {"%playername%", p1.getName()} });
		_s(p1, "trade.help");
		_s(p2, "trade.help");

		inprogress = true;
		
	}

	private void finish() {

		inprogress = false;

		finished = true;
		
		p1.getInventory().addItem(p1.getItemOnCursor());
		p1.setItemOnCursor(new ItemStack(0));

		p2.getInventory().addItem(p2.getItemOnCursor());
		p2.setItemOnCursor(new ItemStack(0));

		giveOffers();

		plugin.getRequestManager().getActiveExchanges().remove(p1);
		plugin.getRequestManager().getActiveExchanges().remove(p2);

		p1.closeInventory();	    
		p2.closeInventory();

		plugin.getLogManager().log(this);
		
	}

	public void stop() {

		inprogress = false;

		p1.getInventory().addItem(p1.getItemOnCursor());
		p1.setItemOnCursor(new ItemStack(0));

		p2.getInventory().addItem(p2.getItemOnCursor());
		p2.setItemOnCursor(new ItemStack(0));

		revertOffers();		

		plugin.getRequestManager().getActiveExchanges().remove(p1);
		plugin.getRequestManager().getActiveExchanges().remove(p2);

		p1.closeInventory();
		p2.closeInventory();
		
		plugin.getLogManager().log(this);
		
	}

	public void forceStopTrading() {

		_s(p1, "trade.error");
		_s(p2, "trade.error");

		stop();
	}

	protected void giveOffers() {

		ItemStack[] p1items = getInterface( p1 ).getItems( Side.RIGHT );
		ItemInterface.replaceNullItems( p1items );
		p1.getInventory().addItem( p1items );

		ItemStack[] p2items = getInterface( p2 ).getItems( Side.RIGHT );		
		ItemInterface.replaceNullItems( p2items );
		p2.getInventory().addItem( p2items );

	}

	protected void revertOffers() {	

		ItemStack[] p1items = getInterface( p1 ).getItems( Side.LEFT );
		ItemInterface.replaceNullItems( p1items );
		p1.getInventory().addItem( p1items );

		ItemStack[] p2items = getInterface( p2 ).getItems( Side.LEFT );
		ItemInterface.replaceNullItems( p2items );
		p2.getInventory().addItem( p2items );

	}

	protected int getAllowedGuiRows(Player player){
		if ( configManager.usePermissions ) {
			if( player.hasPermission("trade.gui.rows.6")){
				return 6;
			}else if( player.hasPermission("trade.gui.rows.5")){
				return 5;
			}else if( player.hasPermission("trade.gui.rows.4")){
				return 4;
			}else if( player.hasPermission("trade.gui.rows.3")){
				return 3;
			}else if( player.hasPermission("trade.gui.rows.2")){
				return 2;
			}
		}
		return configManager.defaultRows;
	}

	

	
}

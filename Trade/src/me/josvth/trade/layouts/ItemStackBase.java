package me.josvth.trade.layouts;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackBase {

	private final int type;
	private final int amount;
	private final short data;
	
	private final String displayName;
	private final List<String> lore; 
		
	public ItemStackBase(int type, int amount, short data, String displayName, List<String> lore) {
		this.type = type;
		this.amount = amount;
		this.data = data;
		this.displayName = displayName;
		this.lore = lore;
	}

	public int getType() {
		return type;
	}


	public int getAmount() {
		return amount;
	}


	public short getData() {
		return data;
	}


	public String getDisplayName() {
		return displayName;
	}


	public List<String> getLore() {
		return lore;
	}

	public ItemStack createItemStack() {
		
		ItemStack stack = new ItemStack(type, amount, data);
		
		if(displayName != null || lore != null ) {
			ItemMeta meta = stack.getItemMeta();
			
			if ( displayName != null )
				meta.setDisplayName(displayName);
				
			if ( lore != null  )
				meta.setLore(lore);
			
			stack.setItemMeta( meta );
			
		}
	
		return stack;
		
	}
		
}
